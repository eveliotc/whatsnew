package info.evelio.whatsnew.activity.test;

import android.test.ActivityInstrumentationTestCase2;
import info.evelio.whatsnew.activity.MainActivity;

public class HelloAndroidActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {

  public HelloAndroidActivityTest() {
    super(MainActivity.class);
  }

  public void testActivity() {
    MainActivity activity = getActivity();
    assertNotNull(activity);
  }
}

