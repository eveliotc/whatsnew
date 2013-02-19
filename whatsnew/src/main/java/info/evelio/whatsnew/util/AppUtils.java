package info.evelio.whatsnew.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Build;

import java.util.List;

/**
 * @author Evelio Tarazona Cáceres <evelio@evelio.info>
 */
public class AppUtils {

  public static boolean isNinePlus() {
    return Build.VERSION.SDK_INT >= 9;
  }

  public static List<ResolveInfo> getLaunchableActivities(final Context context) {
    final Intent mainIntent = new Intent(Intent.ACTION_MAIN);
    mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
    return context.getPackageManager().queryIntentActivities(mainIntent, 0);
  }
}
