package info.evelio.whatsnew.activity;

import android.os.Bundle;
import info.evelio.whatsnew.R;
import info.evelio.whatsnew.fragment.AppListFragment;
import info.evelio.whatsnew.fragment.DetailFragment;

/**
 * Main activity.
 */
public class MainActivity extends BaseActivity implements AppListFragment.AppItemCallback {
  private boolean mMultiPane;

  /**
   * Called when the activity is first created.
   *
   * @param savedInstanceState <p>If the activity is being re-initialized after
   *                           previously being shut down then this Bundle contains the data it most
   *                           recently supplied in onSaveInstanceState(Bundle).
   *                           <bundle>Note: Otherwise it is null.</bundle>
   *                           </p>
   */
  @Override
  public final void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mMultiPane = getResources().getBoolean(R.bool.multi_pane);
    setContentView(R.layout.activity_main);
  }

  @Override
  public void onItemSelected(String packageName) {
    if (mMultiPane) {
      DetailFragment.display(getSupportFragmentManager(), packageName);
    } else {
      DetailActivity.start(this, packageName);
    }
  }

}

