package info.evelio.whatsnew.activity;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import info.evelio.whatsnew.R;

/**
 * @author Evelio Tarazona Cáceres <evelio@evelio.info>
 */
public abstract class BaseActivity extends SherlockFragmentActivity {

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getSupportMenuInflater().inflate(R.menu.base_menu, menu);
    return super.onCreateOptionsMenu(menu);
  }


}
