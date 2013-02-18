package info.evelio.whatsnew.activity;

import android.os.Bundle;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import info.evelio.whatsnew.R;

/**
 * @author Evelio Tarazona Cáceres <evelio@evelio.info>
 */
public class DetailActivity extends SherlockFragmentActivity {
  @Override
  protected final void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_detail);
  }
}
