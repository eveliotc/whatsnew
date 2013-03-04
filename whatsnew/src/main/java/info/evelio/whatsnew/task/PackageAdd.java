package info.evelio.whatsnew.task;

import android.content.Context;
import info.evelio.whatsnew.model.ApplicationEntry;
import info.evelio.whatsnew.util.L;

/**
 * @author Evelio Tarazona Cáceres <evelio@evelio.info>
 */
public class PackageAdd extends PackageTask {
  private static final String TAG = "wn:PAdd";

  @Override
  protected boolean doInBackground() {
    final String packageName = getPackageName();
    L.d(TAG, "Adding package " + packageName);
    final Context context = getContext();
    final ApplicationEntry.Builder builder = new ApplicationEntry.Builder(context.getPackageManager());
    final ApplicationEntry entry = builder.forPackage(packageName).build();
    if (entry.hasValidPackageName()) {
      L.d(TAG, "Storing " + entry);
      boolean result = getSqlAdapter().store(entry) != null;
      updateChangeLogAsync();
      return result;
    } else {
      L.e(TAG, "Builder got invalid package");
    }
    return false;
  }
}
