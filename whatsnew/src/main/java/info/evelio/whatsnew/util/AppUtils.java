package info.evelio.whatsnew.util;

import android.os.Build;

/**
 * @author Evelio Tarazona Cáceres <evelio@evelio.info>
 */
public class AppUtils {

  public static boolean isNinePlus() {
    return Build.VERSION.SDK_INT >= 9;
  }
}
