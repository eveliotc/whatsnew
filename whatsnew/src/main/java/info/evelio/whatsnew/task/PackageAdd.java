package info.evelio.whatsnew.task;

import android.content.Context;
import info.evelio.whatsnew.model.ApplicationEntry;
import info.evelio.whatsnew.util.L;

/**
 * @author Evelio Tarazona Cáceres <evelio@evelio.info>
 */
public class PackageAdd extends PackageTask {
  @Override
  protected boolean doInBackground() {
    final String packageName = getPackageName();
    final Context context = getContext();
    final ApplicationEntry.Builder builder = new ApplicationEntry.Builder(context.getPackageManager());
    final ApplicationEntry entry = builder.forPackage(packageName).build();
    if (entry.hasValidPackageName()) {
      return getSqlAdapter().store(entry) != null;
    } else {
      L.e("wn:PAdd", "Builder got invalid package");
    }
    return false;
  }
}
