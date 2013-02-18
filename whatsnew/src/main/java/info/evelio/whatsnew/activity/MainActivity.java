package info.evelio.whatsnew.activity;

import android.os.Bundle;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import info.evelio.whatsnew.R;

/**
 * Main activity.
 */
public class MainActivity extends SherlockFragmentActivity {

  /**
   * Called when the activity is first created.
   *
   * @param savedInstanceState
   * <p>If the activity is being re-initialized after
   * previously being shut down then this Bundle contains the data it most
   * recently supplied in onSaveInstanceState(Bundle).
   * <b>Note: Otherwise it is null.</b>
   * </p>
   */
  @Override
  public final void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_main);
  }

}
