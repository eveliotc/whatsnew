package info.evelio.whatsnew.task;

import com.codeslap.persistence.SqlAdapter;
import com.telly.groundy.GroundyTask;
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
