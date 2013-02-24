package info.evelio.whatsnew.fragment;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.actionbarsherlock.app.SherlockFragment;
import info.evelio.whatsnew.R;

/**
 * @author Evelio Tarazona Cáceres <evelio@evelio.info>
 */
public class DetailFragment extends SherlockFragment {
  private TextView mPackageName;

  @Override
  public final View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_app_detail, container, false);
  }

  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    mPackageName = (TextView) view.findViewById(R.id.pacakge_text);
  }

  public void display(String packageName) {
    mPackageName.setText(String.valueOf(packageName));
  }

  public static void display(FragmentManager fm, String packageName) {
    ((DetailFragment)fm.findFragmentById(R.id.fragment_app_detail)).display(packageName);
  }
}
