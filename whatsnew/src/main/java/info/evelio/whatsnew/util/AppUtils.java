package info.evelio.whatsnew.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;

import java.util.List;

/**
 * @author Evelio Tarazona Cáceres <evelio@evelio.info>
 */
public class AppUtils {
  private static final String TAG = "wn:AppUtils";

  public static List<ResolveInfo> getLaunchableActivities(final Context context) {
    final Intent mainIntent = new Intent(Intent.ACTION_MAIN);
    mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
    return context.getPackageManager().queryIntentActivities(mainIntent, 0);
  }

  public static boolean launch(Activity activity, String somePackage) {
    if (StringUtils.isEmpty(somePackage)) {
      return false;
    }
    return openActivitySafely(activity, buildLaunchIntent(activity, somePackage));
  }

  public static Intent buildLaunchIntent(Activity activity, String somePackage) {
    try {
      return activity.getPackageManager().getLaunchIntentForPackage(somePackage);
    } catch (Exception e) {
      L.e(TAG, "Unable to build launch intent", e);
    }
    return null;
  }

  public static Intent buildUninstallIntent(String packageName, boolean newTask) {
    Intent uninstallIntent = new Intent(Intent.ACTION_UNINSTALL_PACKAGE);
    uninstallIntent.setData(Uri.parse("package:" + packageName));
    if (newTask) {
      uninstallIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
    }
    return uninstallIntent;
  }

  public static Intent buildAppDetailsIntent(String packageName, boolean newTask) {
    Intent openDetailsIntent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
    openDetailsIntent.addCategory(Intent.CATEGORY_DEFAULT);
    openDetailsIntent.setData(Uri.parse("package:" + packageName));
    if (newTask) {
      openDetailsIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    }
    return openDetailsIntent;
  }

  public static Intent buildStoreIntent(String packageName, boolean newTask) {
    Intent openStoreIntent = new Intent(Intent.ACTION_VIEW);
    openStoreIntent.setData(Uri.parse("market://details?id=" + packageName));
    if (newTask) {
      openStoreIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    }
    return openStoreIntent;
  }


  /**
   * Start an activity with given context
   *
   * @param context Context to use to start an activity
   * @param intent  Intent to use to start an activity
   * @return true if start activity went regular
   *         false otherwise
   */
  public static boolean openActivitySafely(Context context, Intent intent) {
    try {
      context.startActivity(intent);
      return true;
    } catch (Exception meh) {
    }
    return false;
  }

  public static boolean isMyPackage(Context context, String somePackage) {
    return context.getPackageName().equals(somePackage);
  }
}
