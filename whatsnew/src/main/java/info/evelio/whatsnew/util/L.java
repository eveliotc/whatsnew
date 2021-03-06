package info.evelio.whatsnew.util;

import android.util.Log;

/**
 * @author Evelio Tarazona Cáceres <evelio@evelio.info>
 */
public class L {
  public static void e(String tag, String msg, Throwable tr) {
    Log.e(tag, msg, tr);
  }

  public static void e(String tag, String msg) {
    Log.e(tag, msg);
  }

  public static void d(String tag, String msg) {
    Log.d(tag, msg);
  }

  public static void wtf(String tag, String msg) {
    Log.wtf(tag, msg);
  }

  public static void w(String tag, String msg) {
    Log.w(tag, msg);
  }


}
