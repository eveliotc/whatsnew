package info.evelio.whatsnew.task;

import com.codeslap.groundy.GroundyTask;
import com.codeslap.persistence.SqlAdapter;
import info.evelio.whatsnew.helper.PersistenceHelper;

/**
 * @author Evelio Tarazona Cáceres <evelio@evelio.info>
 */
public abstract class TaskWithPersistence extends GroundyTask {
  private SqlAdapter mAdapter;

  protected SqlAdapter getSqlAdapter() {
    if (mAdapter == null) {
      mAdapter = PersistenceHelper.getAdapter(getContext());
    }
    return mAdapter;
  }
}
