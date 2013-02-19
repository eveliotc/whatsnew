package info.evelio.whatsnew.helper;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.widget.BaseAdapter;
import info.evelio.whatsnew.R;
import info.evelio.whatsnew.adapter.ApplicationsAdapter;
import info.evelio.whatsnew.loader.AppsLoader;
import info.evelio.whatsnew.model.ApplicationEntry;

import java.util.List;

/**
 * @author Evelio Tarazona Cáceres <evelio@evelio.info>
 */
public class AppListHelper {
  private final Context mContext;
  private final LoaderManager mLoaderManager;
  private ApplicationsAdapter mAdapter;
  private LoaderManager.LoaderCallbacks<List<ApplicationEntry>> mLoadCallbacks = new AppListCallbacks();
  private LoadingHelper mLoadingHelper;

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
    }

    @Override
    public void onLoaderReset(Loader<List<ApplicationEntry>> loader) {
      // We are good but thanks
    }
  }
}
