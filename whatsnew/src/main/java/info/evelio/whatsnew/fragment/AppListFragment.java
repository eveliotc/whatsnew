package info.evelio.whatsnew.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import com.actionbarsherlock.app.SherlockListFragment;
import com.codeslap.groundy.DetachableResultReceiver;
import com.codeslap.groundy.Groundy;
import info.evelio.whatsnew.R;
import info.evelio.whatsnew.helper.AppListHelper;
import info.evelio.whatsnew.helper.LoadingHelper;
import info.evelio.whatsnew.helper.PrefsHelper;
import info.evelio.whatsnew.model.ApplicationEntry;
import info.evelio.whatsnew.task.CurrentAppsImporter;
import info.evelio.whatsnew.util.L;

/**
 * @author Evelio Tarazona Cáceres <evelio@evelio.info>
 */
public class AppListFragment extends SherlockListFragment implements DetachableResultReceiver.Receiver, AppListHelper.OnLoadCallback {
  private static final String TAG = "wn:AppList";
  private LoadingHelper mLoadingHelper = new LoadingHelper();
  private AppListHelper mAppListHelper;
  private DetachableResultReceiver mReceiver;
  private boolean mIsMultiPane = false;

  private final static AppItemCallback sNoOpCallback = new AppItemCallback() {
    @Override public void onItemSelected(String packageName) {}
  };
  private AppItemCallback mItemCallback = sNoOpCallback;

  @Override
  public final View onCreateView(final LayoutInflater inflater,
                                 final ViewGroup container,
                                 final Bundle savedInstanceState) {
    mLoadingHelper.onCreateView(inflater, container, savedInstanceState);
    return inflater.inflate(R.layout.fragment_app_list, container, false);
  }

  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);

    final Context themedContext = getActivity();
    final Context context = themedContext.getApplicationContext();

    mIsMultiPane = getResources().getBoolean(R.bool.multi_pane);

    getListView().setChoiceMode(mIsMultiPane
        ? ListView.CHOICE_MODE_SINGLE
        : ListView.CHOICE_MODE_NONE);

    mLoadingHelper.setTargetParent((ViewGroup) getView().findViewById(R.id.app_list_container));
    mAppListHelper = new AppListHelper(themedContext, getLoaderManager(), mLoadingHelper);
    mAppListHelper.setLoadCallback(this);
    mAppListHelper.startLoad();
    setListAdapter(mAppListHelper.getAdapter());

    mReceiver = new DetachableResultReceiver(new Handler());
    mReceiver.setReceiver(this);
    final PrefsHelper prefsHelper = new PrefsHelper(context);
    if (!prefsHelper.getBool(PrefsHelper.BOOLEAN_FIRST_IMPORT_DONE)) {
      Groundy.create(context, CurrentAppsImporter.class)
          .receiver(mReceiver)
          .execute();
    }
  }

  @Override
  public void onListItemClick(ListView l, View v, int position, long id) {
    Object item = l.getAdapter().getItem(position);
    if (item instanceof ApplicationEntry) {
      mItemCallback.onItemSelected(((ApplicationEntry)item).getPackageName());
    } else {
      L.wtf(TAG, "Got non type item " + item + " at position " + position);
    }
  }

  @Override
  public void onDestroyView() {
    mAppListHelper.setLoadCallback(null);
    mReceiver.clearReceiver();
    mReceiver = null;
    mLoadingHelper.onDestroyView();
    super.onDestroyView();
  }

  @Override
  public void onReceiveResult(int resultCode, Bundle resultData) {
    mAppListHelper.restartLoad();
  }

  @Override
  public void onAttach(Activity activity) {
    super.onAttach(activity);

    if (activity instanceof AppItemCallback) {
      mItemCallback = (AppItemCallback) activity;
    }
  }

  @Override
  public void onDetach() {
    super.onDetach();

    mItemCallback = sNoOpCallback;
  }

  @Override
  public void onLoadFinished() {
    if (!mIsMultiPane) {
      return;
    }
    // We have to select first item
    // TODO research if there is a better way of doing this
    final ListView listView = getListView();
    ListAdapter adapter;
    if (listView != null && (adapter = listView.getAdapter()) != null && adapter.getCount() > 0) {
      listView.setItemChecked(0, true);
      onListItemClick(listView, null, 0, adapter.getItemId(0));
    }
  }

  public interface AppItemCallback {
    void onItemSelected(String packageName);
  }
}
