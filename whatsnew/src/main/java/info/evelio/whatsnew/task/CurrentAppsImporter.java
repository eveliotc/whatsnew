package info.evelio.whatsnew.task;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import com.codeslap.groundy.GroundyTask;
import info.evelio.whatsnew.helper.PersistenceHelper;
import info.evelio.whatsnew.helper.PrefsHelper;
import info.evelio.whatsnew.model.ApplicationEntry;
import info.evelio.whatsnew.util.L;

import java.util.LinkedList;
import java.util.List;

import static info.evelio.whatsnew.util.AppUtils.getLaunchableActivities;

/**
 * @author Evelio Tarazona Cáceres <evelio@evelio.info>
 */
public class CurrentAppsImporter extends GroundyTask {
  private static final String TAG = "wn:CAI";

  @Override
  protected boolean doInBackground() {
    final Context context = getContext().getApplicationContext();
    final PrefsHelper prefsHelper = new PrefsHelper(context);
    if (prefsHelper.getBool(PrefsHelper.BOOLEAN_FIRST_IMPORT_DONE)) {
      L.e(TAG, "Task being executed but flag already set to DONE!!");
      return true;
    }

    final List<ResolveInfo> resolved = getLaunchableActivities(context);
    if (resolved == null || resolved.size() < 1) {
      L.e(TAG, "Resolved list is null or empty :(");
      return false;
    }

    final List<ApplicationEntry> entries = new LinkedList<ApplicationEntry>();
    final PackageManager pm = context.getPackageManager();
    for (ResolveInfo info : resolved) {
      ApplicationEntry entry = ApplicationEntry.from(pm, info);
      if (entry != null) {
        entries.add(entry);
      }
    }
    L.d(TAG, String.format("Persisting %d of %d resolved", entries.size(), resolved.size()));
    PersistenceHelper.getAdapter(context).storeCollection(entries, null);
    L.d(TAG, "Done!");
    prefsHelper.putBool(PrefsHelper.BOOLEAN_FIRST_IMPORT_DONE, true);
    return true;
  }


}
