package info.evelio.whatsnew.util;

import android.content.Context;
import android.os.Bundle;
import info.evelio.whatsnew.R;

/**
 * @author Evelio Tarazona Cáceres <evelio@evelio.info>
 */
public class StringUtils {

  public static boolean isEmpty(final String value) {
    return value == null || value.length() == 0;
  }

  public static boolean isNotEmpty(final String value) {
    return !isEmpty(value);
  }

  public static String emptyIfNull(final String value) {
    return value == null ? "" : value;
  }

  /**
   * Replace all references of {@link R.string#hexcolor_replace} to given color
   *
   * @param context   Context to use
   * @param stringRes String to get
   * @param colorRes  Color to use
   * @return Template with {@link R.string#hexcolor_replace} replaced to given color
   */
  public static String hexColor(Context context, int stringRes, int colorRes, Object... formatArgs) {
    String someColor = context.getString(colorRes);
    // Color for HTML font color should not contain alpha value
    int colorLength = someColor.length();
    if (colorLength > 7) { // if it contains alpha values (some Android versions add it some others not)
      someColor = "#" + someColor.substring(colorLength - 6, colorLength);
    }
    return context.getString(stringRes, formatArgs).replaceAll(context.getString(R.string.hexcolor_replace), someColor);
  }

  public static String defaultIfEmpty(String value, long defaultValue) {
    return isEmpty(value) ? String.valueOf(defaultValue) : value;
  }

  public static String defaultIfEmpty(String value, int defaultValue) {
    return isEmpty(value) ? String.valueOf(defaultValue) : value;
  }

  public static String defaultIfEmpty(String value, byte defaultValue) {
    return isEmpty(value) ? String.valueOf(defaultValue) : value;
  }

  public static String defaultIfEmpty(String value, boolean defaultValue) {
    return isEmpty(value) ? String.valueOf(defaultValue) : value;
  }

  public static String defaultIfEmpty(String value, Object defaultValue) {
    return isEmpty(value) ? String.valueOf(defaultValue) : value;
  }

  public static String bundle(Bundle bundle) {
    if (bundle == null) {
      return "null";
    }
    // We really don't care if it is empty but calling this will unparcel any parcelled data forcing toString to spit map
    bundle.isEmpty();
    return bundle.toString();
  }
}
