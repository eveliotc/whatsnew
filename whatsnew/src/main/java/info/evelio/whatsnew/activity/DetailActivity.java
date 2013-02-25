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
  @Override
  protected final void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setupActionBar();

    setContentView(R.layout.activity_detail);

    DetailFragment.display(getSupportFragmentManager(), getIntent().getStringExtra(EXTRA_PACKAGE_NAME));
  }

  private void setupActionBar() {
    setTitle(R.string.details);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
  }

  public static void start(Activity activity, String packageName) {
    Intent startMe = new Intent(activity, DetailActivity.class);
    startMe.putExtra(EXTRA_PACKAGE_NAME, packageName);
    activity.startActivity(startMe);
  }
}
