package info.evelio.whatsnew.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.github.kevinsawicki.wishlist.Toaster;
import info.evelio.whatsnew.R;
import info.evelio.whatsnew.model.ApplicationEntry;
import info.evelio.whatsnew.util.AppUtils;
import info.evelio.whatsnew.util.ExtendedViewUpdater;
import info.evelio.whatsnew.util.L;

import static info.evelio.whatsnew.util.AppUtils.*;
import static info.evelio.whatsnew.util.StringUtils.isEmpty;

/**
 * @author Evelio Tarazona Cáceres <evelio@evelio.info>
 */
public class DetailFragment extends SherlockFragment {
  private static final String TAG = "wn:Detail";

  private static final int OTHER_ACTION_FLAGS = MenuItem.SHOW_AS_ACTION_ALWAYS;
  private static final int MY_ACTION_FLAGS = MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT;

  private ViewController mController = new ViewController();

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

  public void display(String packageName) {
    mController.display(packageName);
    getActivity().invalidateOptionsMenu();
  }

  private String getCurrentPackage() {
    return mController.getCurrentPackage();
  }

  public static void display(FragmentManager fm, String packageName) {
    ((DetailFragment) fm.findFragmentById(R.id.fragment_app_detail)).display(packageName);
  }

  private static class ViewController implements View.OnClickListener {
    private static final int[] sChilds = {
        R.id.app_detail_absolute_root,
        R.id.app_detail_root,
        R.id.app_detail_icon,
        R.id.app_detail_label,
        R.id.app_detail_version_label,
    };
    private DetailFragment mDetailFragment;
    private ExtendedViewUpdater mUpdater;
    private String mCurrentPackage;

    private ViewController() {
    }

    public void setFragment(DetailFragment fragment) {
      mDetailFragment = fragment;
      if (hasFragment()) {
        mUpdater = new ExtendedViewUpdater();
        mUpdater.initialize(mDetailFragment.getView(), sChilds);
      } else {
        mUpdater = null;
      }
    }

    private boolean hasFragment() {
      return mDetailFragment != null;
    }

    public void display(String packageName) {
      if (!hasFragment()) {
        return;
      }
      mCurrentPackage = packageName;
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
  }
}
