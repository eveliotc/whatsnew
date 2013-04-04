package info.evelio.whatsnew.activity;

import android.os.Bundle;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;
import info.evelio.whatsnew.R;

/**
 * @author Evelio Tarazona Cáceres <evelio@evelio.info>
 */
public abstract class BaseActivity extends SherlockFragmentActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

    super.onCreate(savedInstanceState);

  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getSupportMenuInflater().inflate(R.menu.base_menu, menu);
    return super.onCreateOptionsMenu(menu);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    final int id = item.getItemId();
    switch (id) {
      case R.id.menu_item_about:
        displayAbout();
        return true;
      case android.R.id.home:
        return onHomeClick();
      default:
        break;
    }
    return super.onOptionsItemSelected(item);
  }

  protected boolean onHomeClick() {
    finish();
    return true;
  }

  protected final void displayAbout() {

  }
}
