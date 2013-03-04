package info.evelio.whatsnew.task;

import android.content.Context;
import android.os.Bundle;
import android.os.ResultReceiver;
import com.codeslap.groundy.Groundy;
import info.evelio.whatsnew.R;
import info.evelio.whatsnew.helper.CrawlingHelper;
import info.evelio.whatsnew.model.ApplicationEntry;
import info.evelio.whatsnew.util.L;

import static info.evelio.whatsnew.util.StringUtils.isNotEmpty;

/**
 * @author Evelio Tarazona Cáceres <evelio@evelio.info>
 */
public class UpdateChangeLog extends PackageTask {
  public static final String KEY_CHANGE_LOG = "info.evelio.whatsnew.key.CHANGE_LOG";
  public static final String KEY_PACKAGE_NAME = "info.evelio.whatsnew.key.PACKAGE_NAME";

  @Override
  protected boolean doInBackground() {
    if (!isOnline()) {
      return false;
    }
    final String packageName = getPackageName();
    addStringResult(KEY_PACKAGE_NAME, packageName);
    final Context context = getContext();
    CrawlingHelper helper = new CrawlingHelper(context.getString(R.string.crawling_lang_code));

    String changeLog;
    try {
      changeLog = helper.getChangeLog(packageName);
    } catch (Exception e) {
      L.e("tw:PUCL", "Unable to update change log for " + packageName, e);
      return false;
    }

    if (isNotEmpty(changeLog)) {
      final ApplicationEntry entry = readApplicationEntry();
      if (!changeLog.equals(entry.getChangeLog())) { // Only update if different
        makeSnapshot(entry);
        entry.setChangeLog(changeLog);
        getSqlAdapter().update(entry, WHERE_PACKAGE_NAME_EQUALS, new String[]{ packageName });
      }
    }
    addStringResult(KEY_CHANGE_LOG, changeLog);
    return true;
  }

  public static void execute(Context context, String packageName, ResultReceiver receiver) {
    final Bundle params = new Bundle(1);
    params.putString(UpdateChangeLog.PARAM_PACKAGE_NAME, packageName);
    Groundy.create(context, UpdateChangeLog.class)
        .params(params)
        .receiver(receiver)
        .execute();
  }
}
