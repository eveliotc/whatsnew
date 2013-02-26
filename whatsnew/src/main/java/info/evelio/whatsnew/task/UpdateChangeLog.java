package info.evelio.whatsnew.task;

import android.content.Context;
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
    final String packageName = getPackageName();
    L.d("wn:PUCL", "pkg:"+packageName);
    addStringResult(KEY_PACKAGE_NAME, packageName);
    L.d("wn:PUCL", "resultPkg:"+getResultData().getString(KEY_PACKAGE_NAME));
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
      makeSnapshot(entry);
      entry.setChangeLog(changeLog);
      getSqlAdapter().store(entry);
    }
    addStringResult(KEY_CHANGE_LOG, changeLog);
    return true;
  }
}
