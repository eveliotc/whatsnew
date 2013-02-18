package info.evelio.whatsnew.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.actionbarsherlock.app.SherlockFragment;
import info.evelio.whatsnew.R;

/**
 * @author Evelio Tarazona Cáceres <evelio@evelio.info>
 */
public class DetailFragment extends SherlockFragment {
  @Override
  public final View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_app_detail, container, false);
  }
}
