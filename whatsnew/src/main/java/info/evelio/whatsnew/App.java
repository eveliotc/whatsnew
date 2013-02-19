package info.evelio.whatsnew;

import android.app.Application;
import info.evelio.whatsnew.helper.DatabaseHelper;

/**
 * Application class.
 * @author Evelio Tarazona Cáceres <evelio@evelio.info>
 */
public class App extends Application {
  @Override
  public void onCreate() {
    super.onCreate();

    DatabaseHelper.onCreate(this);
  }
}
