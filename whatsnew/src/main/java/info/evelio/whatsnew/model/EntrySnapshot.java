package info.evelio.whatsnew.model;

import com.codeslap.persistence.Column;
import com.codeslap.persistence.PrimaryKey;
import com.codeslap.persistence.Table;

/**
 * This is an snapshot taken when app was updated
 *
 * @author Evelio Tarazona Cáceres <evelio@evelio.info>
 */
@Table(EntrySnapshot.Contract.TABLE_NAME)
public class EntrySnapshot {
  @PrimaryKey
  private long id;
  @Column(Contract.COLUMN_PACKAGE_NAME)
  private String packageName;
  @Column(Contract.COLUMN_VERSION)
  private String version;
  @Column(Contract.COLUMN_VERSION_CODE)
  private long versionCode;
  @Column(Contract.COLUMN_UPDATE_TIME)
  private long updateTime = System.currentTimeMillis();
  @Column(Contract.COLUMN_CHANGE_LOG)
  private String changeLog;

  /**
   * Creates an empty instance
   */
  public EntrySnapshot() {
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getPackageName() {
    return packageName;
  }

  public void setPackageName(String packageName) {
    this.packageName = packageName;
  }

  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public long getVersionCode() {
    return versionCode;
  }

  public void setVersionCode(long versionCode) {
    this.versionCode = versionCode;
  }

  public long getUpdateTime() {
    return updateTime;
  }

  public void setUpdateTime(long updateTime) {
    this.updateTime = updateTime;
  }

  public String getChangeLog() {
    return changeLog;
  }

  public void setChangeLog(String changeLog) {
    this.changeLog = changeLog;
  }

  @Override
  public String toString() {
    return "EntrySnapshot{" +
        "id=" + id +
        ", packageName='" + packageName + '\'' +
        ", version='" + version + '\'' +
        ", versionCode=" + versionCode +
        ", updateTime=" + updateTime +
        ", changeLog='" + changeLog + '\'' +
        '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof EntrySnapshot)) return false;

    EntrySnapshot that = (EntrySnapshot) o;

    if (versionCode != that.versionCode) return false;
    if (changeLog != null ? !changeLog.equals(that.changeLog) : that.changeLog != null) return false;
    if (packageName != null ? !packageName.equals(that.packageName) : that.packageName != null) return false;
    if (version != null ? !version.equals(that.version) : that.version != null) return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result = packageName != null ? packageName.hashCode() : 0;
    result = 31 * result + (version != null ? version.hashCode() : 0);
    result = 31 * result + (int) (versionCode ^ (versionCode >>> 32));
    result = 31 * result + (changeLog != null ? changeLog.hashCode() : 0);
    return result;
  }

  public interface Contract {
    /**
     * Table name
     */
    String TABLE_NAME = "versions";
    /**
     * Text column used for storing package name
     */
    String COLUMN_PACKAGE_NAME = "package_name";
    String COLUMN_VERSION = "version";
    String COLUMN_VERSION_CODE = "version_code";
    String COLUMN_UPDATE_TIME = "update_time";
    String COLUMN_CHANGE_LOG = "change_log";
  }

  public static class Builder {
    private ApplicationEntry mEntry;

    public Builder from(ApplicationEntry entry) {
      mEntry = entry;
      return this;
    }

    public EntrySnapshot build() {
      EntrySnapshot entrySnapshot = new EntrySnapshot();
      final ApplicationEntry entry = mEntry;
      if (entry != null) {
        entrySnapshot.setPackageName( entry.getPackageName() );
        entrySnapshot.setVersion( entry.getPackageVersion() );
        entrySnapshot.setVersionCode( entry.getPackageVersionCode() );
        entrySnapshot.setChangeLog( entry.getChangeLog() );
      }
      return entrySnapshot;
    }
  }
}
