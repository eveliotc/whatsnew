package info.evelio.whatsnew.loader;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import com.codeslap.groundy.loader.SupportListLoader;
import com.codeslap.persistence.SqlAdapter;
import info.evelio.whatsnew.helper.PersistenceHelper;
import info.evelio.whatsnew.model.ApplicationEntry;
import info.evelio.whatsnew.util.L;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static info.evelio.whatsnew.util.StringUtils.isEmpty;

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
    final List<ApplicationEntry> persisted = adapter.findAll(ApplicationEntry.class);
    final List<ApplicationEntry> filtered = new ArrayList<ApplicationEntry>(persisted.size());
    if (persisted != null) {
      for (ApplicationEntry entry : persisted) {
        if (goodEnough(entry)) {
          filtered.add(entry);
        }
      }
    }
    return filtered;
  }

  private boolean goodEnough(ApplicationEntry entry) {
    if (entry == null || isEmpty(entry.getPackageName())) {
      return false;
    }
    final PackageManager pm = mPackageManager;
    try {
      final PackageInfo packageInfo = pm.getPackageInfo(entry.getPackageName(), 0);
      final ApplicationInfo appInfo = packageInfo.applicationInfo;
      File sourceDir = new File(appInfo.sourceDir);
      if (sourceDir.exists()) { // its mounted
        // TODO EEE we might want to cache this as it rarely changes
        entry.setLabel(appInfo.loadLabel(pm));
        entry.setIcon(appInfo.loadIcon(pm));
      }
      return true;
    } catch (Exception e) {
      L.e(TAG, "Invalid package found", e);
    }
    return false;
  }
}
