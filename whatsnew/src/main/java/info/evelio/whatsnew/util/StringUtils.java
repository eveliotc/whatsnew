package info.evelio.whatsnew.util;

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
}
