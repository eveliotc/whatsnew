package info.evelio.whatsnew.helper;

import android.app.Application;
import android.content.Context;
import com.codeslap.persistence.DatabaseSpec;
import com.codeslap.persistence.Persistence;
import com.codeslap.persistence.PersistenceConfig;
import com.codeslap.persistence.SqlAdapter;
import info.evelio.whatsnew.model.ApplicationEntry;
import info.evelio.whatsnew.model.EntrySnapshot;

/**
 * @author Evelio Tarazona Cáceres <evelio@evelio.info>
 */
public class PersistenceHelper {
  private static final int DATABASE_VERSION = 1;
  private static final String DATABASE_SPEC = "info.evelio.whatsnew.spec.default";
  private static final String DATABASE_NAME = "apps.db";

  /**
   * Performs initial setup when application is first created
   *
   * @param application Application that is calling this from it's very own onCreate method
   */
  public static void onCreate(final Application application) {
    DatabaseSpec database = PersistenceConfig.registerSpec(DATABASE_SPEC, DATABASE_VERSION);
    database.match(EntrySnapshot.class);
    database.matchNotAutoIncrement(ApplicationEntry.class);
  }

  /**
   * Factory methods for SqlAdapters
   *
   * @param context Context to use
   * @return An adapter to use
   */
  public static SqlAdapter getAdapter(Context context) {
    return Persistence.getAdapter(context.getApplicationContext(), DATABASE_NAME, DATABASE_SPEC);
  }
}
