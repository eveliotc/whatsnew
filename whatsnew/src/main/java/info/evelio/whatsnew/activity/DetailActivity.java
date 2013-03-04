package info.evelio.whatsnew.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import info.evelio.whatsnew.R;
import info.evelio.whatsnew.fragment.DetailFragment;

/**
 * @author Evelio Tarazona Cáceres <evelio@evelio.info>
 */
public class DetailActivity extends BaseActivity {
  private static final String EXTRA_PACKAGE_NAME = "info.evelio.whatsnew.PACKAGE_NAME";
  private static final String EXTRA_KNOWN_CHANGE_LOG = "info.evelio.whatsnew.KNOWN_CHANGE_LOG";

  @Override
  protected final void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setupActionBar();

    setContentView(R.layout.activity_detail);

    Intent intent = getIntent();
    DetailFragment.display(getSupportFragmentManager(),
        intent.getStringExtra(EXTRA_PACKAGE_NAME),
        intent.getStringExtra(EXTRA_KNOWN_CHANGE_LOG));
  }

  private void setupActionBar() {
    setTitle(R.string.details);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
  }

  public static void start(Activity activity, String packageName, String knownChangeLog) {
    Intent startMe = new Intent(activity, DetailActivity.class);
    startMe.putExtra(EXTRA_PACKAGE_NAME, packageName);
    startMe.putExtra(EXTRA_KNOWN_CHANGE_LOG, knownChangeLog);
    activity.startActivity(startMe);
  }
}
