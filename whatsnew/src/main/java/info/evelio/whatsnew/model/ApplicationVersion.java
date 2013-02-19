package info.evelio.whatsnew.model;

import com.codeslap.persistence.Column;
import com.codeslap.persistence.PrimaryKey;
import com.codeslap.persistence.Table;

/**
 * @author Evelio Tarazona Cáceres <evelio@evelio.info>
 */
@Table(ApplicationVersion.Contract.TABLE_NAME)
public class ApplicationVersion {
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
  public ApplicationVersion() {
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
}
