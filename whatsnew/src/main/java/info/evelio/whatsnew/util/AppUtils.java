package info.evelio.whatsnew.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;

import java.util.List;

/**
 * @author Evelio Tarazona Cáceres <evelio@evelio.info>
 */
public class AppUtils {

  public static List<ResolveInfo> getLaunchableActivities(final Context context) {
    final Intent mainIntent = new Intent(Intent.ACTION_MAIN);
    mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
    return context.getPackageManager().queryIntentActivities(mainIntent, 0);
  }
}
