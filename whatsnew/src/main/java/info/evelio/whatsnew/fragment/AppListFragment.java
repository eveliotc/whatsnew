package info.evelio.whatsnew.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.actionbarsherlock.app.SherlockListFragment;
import info.evelio.whatsnew.R;
import info.evelio.whatsnew.helper.LoadingHelper;

/**
 * @author Evelio Tarazona Cáceres <evelio@evelio.info>
 */
public class AppListFragment extends SherlockListFragment {
  private LoadingHelper mLoadingHelper = new LoadingHelper();

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

    mLoadingHelper.addTo((ViewGroup) getView().findViewById(R.id.app_list_container));
  }

  @Override
  public void onDestroyView() {
    mLoadingHelper.onDestroyView();
    super.onDestroyView();
  }
}
