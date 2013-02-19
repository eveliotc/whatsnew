package info.evelio.whatsnew.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * @author Evelio Tarazona Cáceres <evelio@evelio.info>
 */
public class PrefsHelper {
  public static final String BOOLEAN_FIRST_IMPORT_DONE = "first_time_import_done";

  private SharedPreferences mPrefs;

  public PrefsHelper(Context context) {
    mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
  }

  public void putBool(String key, boolean value) {
    mPrefs.edit().putBoolean(key, value).commit();
  }

  public boolean getBool(String key) {
    return mPrefs.getBoolean(key, false);
  }
}
