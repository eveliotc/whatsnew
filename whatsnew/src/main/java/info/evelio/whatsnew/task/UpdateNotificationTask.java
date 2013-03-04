package info.evelio.whatsnew.task;

import android.content.Context;
import com.codeslap.groundy.Groundy;
import info.evelio.whatsnew.helper.AppListHelper;
import info.evelio.whatsnew.helper.NotificationHelper;
import info.evelio.whatsnew.model.ApplicationEntry;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Evelio Tarazona Cáceres <evelio@evelio.info>
 */
public class UpdateNotificationTask extends TaskWithPersistence {
  private static final String WHERE_UPDATED_SINCE = ApplicationEntry.Contract.COLUMN_LAST_UPDATE_TIME + " > ";
  private static final long SINCE_PERIOD = 2 * 60 * 60 * 1000; // 2 hours

  @Override
  protected boolean doInBackground() {
    final NotificationHelper helper = new NotificationHelper(getContext());
    if (!helper.areNotificationsEnabled()) {
      return false;
    }
    final long since = System.currentTimeMillis() - SINCE_PERIOD;

    List<ApplicationEntry> persisted = getSqlAdapter().findAll(ApplicationEntry.class, WHERE_UPDATED_SINCE + since
        + " AND " + AppListHelper.NON_REMOVED, null);
    List<ApplicationEntry> filtered = new LinkedList<ApplicationEntry>();
    AppListHelper.filterAndLoad(persisted, filtered, getContext().getPackageManager());
    helper.showUpdated(filtered);
    return true;
  }

  public static void queue(Context context) {
    Groundy.create(context, UpdateNotificationTask.class).queue();
  }
}
