package info.evelio.whatsnew.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.codeslap.groundy.DetachableResultReceiver;
import com.codeslap.groundy.Groundy;
import com.github.kevinsawicki.wishlist.Toaster;
import info.evelio.whatsnew.R;
import info.evelio.whatsnew.model.ApplicationEntry;
import info.evelio.whatsnew.task.UpdateChangeLog;
import info.evelio.whatsnew.util.AppUtils;
import info.evelio.whatsnew.util.ExtendedViewUpdater;
import info.evelio.whatsnew.util.L;

import static info.evelio.whatsnew.util.AppUtils.*;
import static info.evelio.whatsnew.util.StringUtils.*;

/**
 * @author Evelio Tarazona Cáceres <evelio@evelio.info>
 */
public class DetailFragment extends SherlockFragment implements DetachableResultReceiver.Receiver {
  private static final String TAG = "wn:Detail";

  private static final int OTHER_ACTION_FLAGS = MenuItem.SHOW_AS_ACTION_ALWAYS;
  private static final int MY_ACTION_FLAGS = MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT;

  private ViewController mController = new ViewController();
  private DetachableResultReceiver mReceiver;
  private Handler mHandler = new Handler();

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setHasOptionsMenu(true);
  }

  @Override
  public final View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_app_detail, container, false);
  }

  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    mController.setFragment(this);
  }

  @Override
  public void onDestroyView() {
    clearReceiver();
    mController.setFragment(null);

    super.onDestroyView();
  }

  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    inflater.inflate(R.menu.details_menu, menu);
    super.onCreateOptionsMenu(menu, inflater);
  }

  @Override
  public void onPrepareOptionsMenu(Menu menu) {
    if (isEmpty(getCurrentPackage())) {
      return;
    }
    final boolean isNotMyPackage = !AppUtils.isMyPackage(getActivity(), getCurrentPackage());
    menu.findItem(R.id.menu_item_launch).setVisible(isNotMyPackage);
    menu.findItem(R.id.menu_item_uninstall).setVisible(isNotMyPackage);
    final int otherFlags = isNotMyPackage ? OTHER_ACTION_FLAGS : MY_ACTION_FLAGS;
    menu.findItem(R.id.menu_item_store).setShowAsActionFlags(otherFlags).setVisible(true);
    menu.findItem(R.id.menu_item_info).setShowAsActionFlags(otherFlags).setVisible(true);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    final int id = item.getItemId();
    switch (id) {
      case R.id.menu_item_launch:
        return launch();
      case R.id.menu_item_uninstall:
        return uninstall();
      case R.id.menu_item_store:
        return openInStore();
      case R.id.menu_item_info:
        return openInfo();
      default:
        break;
    }
    return super.onOptionsItemSelected(item);
  }

  private boolean launch() {
    return performStartActivity(
        buildLaunchIntent(getActivity(), getCurrentPackage()),
        R.string.error_unable_to_launch
    );
  }

  private boolean uninstall() {
    return performStartActivity(
        buildUninstallIntent(getCurrentPackage(), false),
        R.string.error_unable_to_launch_uninstall
    );
  }

  private boolean openInStore() {
    return performStartActivity(
        buildStoreIntent(getCurrentPackage(), false),
        R.string.error_unable_to_launch_store
    );
  }

  private boolean openInfo() {
    return performStartActivity(
        buildAppDetailsIntent(getCurrentPackage(), false),
        R.string.error_unable_to_launch_store
    );
  }

  private boolean performStartActivity(Intent intent, int errorMsg) {
    Activity activity = getActivity();
    final boolean didLaunch = openActivitySafely(activity, intent);
    if (!didLaunch) {
      Toaster.showLong(activity, errorMsg);
    }
    return didLaunch;
  }

  public void display(String packageName, String knownChangelog) {
    mController.display(packageName, knownChangelog);
    final Activity activity = getActivity();
    activity.invalidateOptionsMenu();

    if (isEmpty(knownChangelog)) { //Forced update
      setupReceiver();
      UpdateChangeLog.execute(activity.getApplicationContext(), packageName, mReceiver);
    }
  }

  private void setupReceiver() {
    clearReceiver();
    mReceiver = new DetachableResultReceiver(mHandler);
    mReceiver.setReceiver(this);
  }

  private void clearReceiver() {
    if (mReceiver != null) {
      mReceiver.clearReceiver();
      mReceiver = null;
    }
  }

  @Override
  public void onReceiveResult(int resultCode, Bundle resultData) {
    L.d(TAG, "Got code " + resultCode + " " + bundle(resultData));
    switch (resultCode) {
      case Groundy.STATUS_FINISHED:
        mController.updateChangeLog(resultData.getString(UpdateChangeLog.KEY_PACKAGE_NAME), resultData.getString(UpdateChangeLog.KEY_CHANGE_LOG));
        break;
      default:
        break;
    }
  }

  private String getCurrentPackage() {
    return mController.getCurrentPackage();
  }

  public static void display(FragmentManager fm, String packageName, String knownChangeLog) {
    ((DetailFragment) fm.findFragmentById(R.id.fragment_app_detail)).display(packageName, knownChangeLog);
  }

  private static class ViewController implements View.OnClickListener {
    private static final int[] sChilds = {
        R.id.app_detail_absolute_root,
        R.id.app_detail_root,
        R.id.app_detail_icon,
        R.id.app_detail_label,
        R.id.app_detail_version_label,
        R.id.app_detail_change_log_text,
    };
    private DetailFragment mDetailFragment;
    private ExtendedViewUpdater mUpdater;
    private String mCurrentPackage;
    private String mDefaultEmptyChangeLogMsg = "";
    private String mKnownChangeLog;

    private ViewController() {
    }

    public void setFragment(DetailFragment fragment) {
      mDetailFragment = fragment;
      if (hasFragment()) {
        if (isEmpty(mDefaultEmptyChangeLogMsg)) {
          mDefaultEmptyChangeLogMsg = mDetailFragment.getString(R.string.empty_change_log);
        }
        mUpdater = new ExtendedViewUpdater();
        mUpdater.initialize(mDetailFragment.getView(), sChilds);
      } else {
        mUpdater = null;
      }
    }

    private boolean hasFragment() {
      return mDetailFragment != null;
    }

    public void display(String packageName, String knownChangelog) {
      if (!hasFragment()) {
        return;
      }
      mCurrentPackage = packageName;
      mKnownChangeLog = knownChangelog;
      if (isEmpty(packageName)) {
        displayEmpty();
      } else {
        updateAll();
      }
    }

    private void updateAll() {
      if (!hasFragment()) {
        return;
      }
      final ApplicationEntry.Builder builder = new ApplicationEntry.Builder(mDetailFragment.getActivity().getPackageManager());

      try {
        final ApplicationEntry entry = builder.forPackage(mCurrentPackage).loadingResources().build();
        mUpdater.imageView(2).setImageDrawable(entry.getIcon());
        mUpdater.setText(3, entry.getDisplayableLabel());
        mUpdater.setText(4, entry.getDisplayableVersion());
        setChangeLog(mKnownChangeLog);
      } catch (Exception e) {
        L.e(TAG, "Unable to display item with package " + mCurrentPackage, e);
        displayEmpty();
        return;
      }

      mUpdater.setBackground(0, null);
      mUpdater.setGone(1, false);
    }

    private void displayEmpty() {
      if (!hasFragment()) {
        return;
      }
      mUpdater.setBackground(0, R.drawable.empty_placeholder);
      mUpdater.setGone(1, true);
    }

    public String getCurrentPackage() {
      return mCurrentPackage;
    }

    @Override
    public void onClick(View view) {

    }

    public void updateChangeLog(String givenPackage, String changeLog) {
      if (!hasFragment() || mCurrentPackage == null || !mCurrentPackage.equalsIgnoreCase(givenPackage)) {
        L.e(TAG, "Asked to update changelog but had no fragment or package didn't match "
            + mCurrentPackage + " != " + givenPackage);
        return;
      }
      setChangeLog(changeLog);
    }

    public void setChangeLog(String changeLog) {
      changeLog = defaultIfEmpty(changeLog, mDefaultEmptyChangeLogMsg);
      L.d(TAG, changeLog);
      mUpdater.setText(5, Html.fromHtml(changeLog));
    }
  }
}
