package info.evelio.whatsnew.task;

import info.evelio.whatsnew.model.ApplicationEntry;

/**
 * @author Evelio Tarazona Cáceres <evelio@evelio.info>
 */
public class PackageRemove extends PackageTask {
  @Override
  protected boolean doInBackground() {
    ApplicationEntry applicationEntry = readApplicationEntry();
    applicationEntry.setRemoved(true);
    return getSqlAdapter().store(applicationEntry) != null;
  }

}
