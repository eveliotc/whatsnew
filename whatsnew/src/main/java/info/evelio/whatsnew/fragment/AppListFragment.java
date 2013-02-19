package info.evelio.whatsnew.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.actionbarsherlock.app.SherlockListFragment;
import com.codeslap.groundy.DetachableResultReceiver;
import com.codeslap.groundy.Groundy;
import info.evelio.whatsnew.R;
import info.evelio.whatsnew.helper.AppListHelper;
import info.evelio.whatsnew.helper.LoadingHelper;
import info.evelio.whatsnew.helper.PrefsHelper;
import info.evelio.whatsnew.task.CurrentAppsImporter;

/**
 * @author Evelio Tarazona Cáceres <evelio@evelio.info>
 */
public class AppListFragment extends SherlockListFragment implements DetachableResultReceiver.Receiver {
  private LoadingHelper mLoadingHelper = new LoadingHelper();
  private AppListHelper mAppListHelper;
  private DetachableResultReceiver mReceiver;

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
    mLoadingHelper.setTargetParent((ViewGroup) getView().findViewById(R.id.app_list_container));
    mAppListHelper = new AppListHelper(themedContext, getLoaderManager(), mLoadingHelper);
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
  public void onDestroyView() {
    mReceiver.clearReceiver();
    mLoadingHelper.onDestroyView();
    super.onDestroyView();
  }

  @Override
  public void onReceiveResult(int resultCode, Bundle resultData) {
    mAppListHelper.restartLoad();
  }

}
