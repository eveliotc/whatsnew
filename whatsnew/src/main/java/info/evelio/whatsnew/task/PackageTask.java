package info.evelio.whatsnew.task;

import info.evelio.whatsnew.model.ApplicationEntry;
import info.evelio.whatsnew.model.EntrySnapshot;
import info.evelio.whatsnew.util.L;

import static info.evelio.whatsnew.model.ApplicationEntry.Contract.COLUMN_PACKAGE_NAME;

/**
 * @author Evelio Tarazona Cáceres <evelio@evelio.info>
 */
public abstract class PackageTask extends TaskWithPersistence {
  public static final String PARAM_PACKAGE_NAME = "info.evelio.whatsnew.param.PACKAGE_NAME";
  static final String WHERE_PACKAGE_NAME_EQUALS = COLUMN_PACKAGE_NAME + " = ?";
  private static final String TAG = "wn:PTask";


  protected String getPackageName() {
    return getStringParam(PARAM_PACKAGE_NAME);
  }

  protected ApplicationEntry readApplicationEntry() {
    final String packageName = getPackageName();
    L.d(TAG, "Reading " + packageName);
    return getSqlAdapter()
        .findFirst(ApplicationEntry.class, WHERE_PACKAGE_NAME_EQUALS, new String[]{ packageName });
  }

  protected void makeSnapshot(ApplicationEntry entry) {
    try {
      final EntrySnapshot snapshot = new EntrySnapshot.Builder().from(entry).build();
      getSqlAdapter().store(snapshot);
    } catch (Exception e) {
      L.e(TAG, "Unable to create snapshot for " + e, e);
    }
  }

  protected void updateChangeLogAsync() {
    UpdateChangeLog.execute(getContext(), getPackageName(), null);
  }

  protected void pingNotification() {
    UpdateNotificationTask.queue(getContext());
  }

}
