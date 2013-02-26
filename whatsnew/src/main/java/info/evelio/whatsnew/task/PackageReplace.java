package info.evelio.whatsnew.task;

import android.content.Context;
import info.evelio.whatsnew.model.ApplicationEntry;
import info.evelio.whatsnew.model.EntrySnapshot;
import info.evelio.whatsnew.util.L;

/**
 * @author Evelio Tarazona Cáceres <evelio@evelio.info>
 */
public class PackageReplace extends PackageTask {
  @Override
  protected boolean doInBackground() {
    final Context context = getContext();
    final ApplicationEntry.Builder builder = new ApplicationEntry.Builder(context.getPackageManager());
    final ApplicationEntry oldEntry = readApplicationEntry();
    final ApplicationEntry applicationEntry = builder.with(oldEntry).build();

    if (applicationEntry.getPackageVersionCode() == oldEntry.getPackageVersionCode()) {
      L.e("wn:PReplace", "Package replaced but version code remains equals");
      return false;
    }

    final EntrySnapshot snapshot = new EntrySnapshot.Builder().from(oldEntry).build();
    getSqlAdapter().store(snapshot);

    return getSqlAdapter().store(applicationEntry) != null;
  }
}
