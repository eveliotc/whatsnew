package info.evelio.whatsnew.helper;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.widget.BaseAdapter;
import info.evelio.whatsnew.R;
import info.evelio.whatsnew.adapter.ApplicationsAdapter;
import info.evelio.whatsnew.loader.AppsLoader;
import info.evelio.whatsnew.model.ApplicationEntry;
import info.evelio.whatsnew.util.L;

import java.util.List;

/**
 * @author Evelio Tarazona Cáceres <evelio@evelio.info>
 */
public class AppListHelper {
  public static final String NON_REMOVED = ApplicationEntry.Contract.COLUMN_REMOVED + " = 0 ORDER BY "
        + ApplicationEntry.Contract.COLUMN_LAST_UPDATE_TIME + " DESC";

  private static final String TAG = "wn:ALH";
  private final Context mContext;
  private final LoaderManager mLoaderManager;
  private ApplicationsAdapter mAdapter;
  private LoaderManager.LoaderCallbacks<List<ApplicationEntry>> mLoadCallbacks = new AppListCallbacks();
  private LoadingHelper mLoadingHelper;
  private OnLoadCallback mLoadCallback;

  public AppListHelper(Context context, LoaderManager loaderManager, LoadingHelper loadingHelper) {
    mContext = context;
    mLoaderManager = loaderManager;
    mAdapter = new ApplicationsAdapter(context);
    mLoadingHelper = loadingHelper;
  }

  public BaseAdapter getAdapter() {
    return mAdapter;
  }

  public void startLoad() {
    mLoadingHelper.show();
    mLoaderManager.initLoader(R.id.loader_applications, null, mLoadCallbacks);
  }

  public void restartLoad() {
    mLoadingHelper.show();
    mLoaderManager.restartLoader(R.id.loader_applications, null, mLoadCallbacks);
  }

  public void setLoadCallback(OnLoadCallback loadCallback) {
    mLoadCallback = loadCallback;
  }

  private class AppListCallbacks implements LoaderManager.LoaderCallbacks<List<ApplicationEntry>> {
    @Override
    public Loader<List<ApplicationEntry>> onCreateLoader(int id, Bundle args) {
      final AppsLoader loader = new AppsLoader(mContext);
      return loader;
    }

    @Override
    public void onLoadFinished(Loader<List<ApplicationEntry>> loader, List<ApplicationEntry> data) {
      mAdapter.setItems(data);
      mLoadingHelper.hide();
      if (mLoadCallback != null) {
        mLoadCallback.onLoadFinished();
      }
    }

    @Override
    public void onLoaderReset(Loader<List<ApplicationEntry>> loader) {
      // We are good but thanks
    }
  }

  public interface OnLoadCallback {
    public void onLoadFinished();
  }

  public static void filterAndLoad(final List<ApplicationEntry> persisted,
                                   final List<ApplicationEntry> filtered,
                                   final PackageManager packageManager) {
    if (persisted != null && filtered != null) {
      for (ApplicationEntry entry : persisted) {
        if (goodEnough(entry, packageManager)) {
          filtered.add(entry);
        }
      }
    }
  }

  private static boolean goodEnough(ApplicationEntry entry, final PackageManager pm) {
    if (entry == null || !entry.hasValidPackageName() || entry.isRemoved()) {
      L.d(TAG, "Skipping " + entry);
      return false;
    }
    try {
      final PackageInfo packageInfo = pm.getPackageInfo(entry.getPackageName(), 0);
      entry.loadResources(pm, packageInfo.applicationInfo);
      return true;
    } catch (Exception e) {
      L.e(TAG, "Failure in entry " + entry, e);
    }
    return false;
  }

}
