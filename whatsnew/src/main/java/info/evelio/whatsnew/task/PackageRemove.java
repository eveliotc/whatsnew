package info.evelio.whatsnew.task;

import info.evelio.whatsnew.model.ApplicationEntry;
import info.evelio.whatsnew.util.L;

/**
 * @author Evelio Tarazona Cáceres <evelio@evelio.info>
 */
public class PackageRemove extends PackageTask {
  private static final String TAG = "wn:PRemove";

  @Override
  protected boolean doInBackground() {
    L.d(TAG, "Removing " + getPackageName());
    ApplicationEntry applicationEntry = readApplicationEntry();
    applicationEntry.setRemoved(true);
    final String packageName = applicationEntry.getPackageName();
    L.d(TAG, "Updating " + applicationEntry);
    return getSqlAdapter().update(applicationEntry, WHERE_PACKAGE_NAME_EQUALS, new String[]{ packageName }) == 1;
  }

}
