package info.evelio.whatsnew.loader;

import android.content.Context;
import android.content.pm.PackageManager;
import com.codeslap.groundy.loader.SupportListLoader;
import com.codeslap.persistence.SqlAdapter;
import info.evelio.whatsnew.helper.AppListHelper;
import info.evelio.whatsnew.helper.PersistenceHelper;
import info.evelio.whatsnew.model.ApplicationEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Evelio Tarazona Cáceres <evelio@evelio.info>
 */
public class AppsLoader extends SupportListLoader<ApplicationEntry> {
  private static final String TAG = "wn:AL";
  private PackageManager mPackageManager;

  public AppsLoader(Context context) {
    super(context);
    mPackageManager = context.getApplicationContext().getPackageManager();
  }

  @Override
  protected List<ApplicationEntry> getData() {
    final Context context = getContext();
    final SqlAdapter adapter = PersistenceHelper.getAdapter(context);
    final List<ApplicationEntry> persisted = adapter.findAll(ApplicationEntry.class, AppListHelper.NON_REMOVED, null);
    final List<ApplicationEntry> filtered = new ArrayList<ApplicationEntry>(persisted.size());
    AppListHelper.filterAndLoad(persisted, filtered, mPackageManager);
    return filtered;
  }

}
