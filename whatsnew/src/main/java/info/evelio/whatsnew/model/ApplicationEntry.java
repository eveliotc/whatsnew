package info.evelio.whatsnew.model;

import android.content.pm.PackageInfo;
import android.content.pm.PackageItemInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import com.codeslap.persistence.Column;
import com.codeslap.persistence.Ignore;
import com.codeslap.persistence.PrimaryKey;
import com.codeslap.persistence.Table;
import info.evelio.whatsnew.util.AppUtils;
import info.evelio.whatsnew.util.L;
import info.evelio.whatsnew.util.StringUtils;

/**
 * @author Evelio Tarazona Cáceres <evelio@evelio.info>
 */
@Table(value = ApplicationEntry.Contract.TABLE_NAME)
public class ApplicationEntry {
  /**
   * Package name
   */
  @PrimaryKey(autoincrement = false)
  @Column(Contract.COLUMN_PACKAGE_NAME)
  private String packageName;
  @Column(Contract.COLUMN_PACKAGE_VERSION)
  private String packageVersion;
  @Column(Contract.COLUMN_PACKAGE_VERSION_CODE)
  private long packageVersionCode;
  @Column(Contract.COLUMN_FIRST_INSTALL_TIME)
  private long firstInstallTime;
  @Column(Contract.COLUMN_LAST_UPDATE_TIME)
  private long lastUpdateTime;
  @Ignore
  private CharSequence label;
  @Ignore
  private Drawable icon;

  /**
   * Creates an empty instance
   */
  public ApplicationEntry() {
  }

  /**
   * Retrieve current package name
   *
   * @return Current package name value might be null
   */
  public final String getPackageName() {
    return packageName;
  }

  /**
   * Sets package name to given value
   *
   * @param givenPackageName Package name to set to
   */
  public final void setPackageName(final String givenPackageName) {
    this.packageName = givenPackageName;
  }

  public String getPackageVersion() {
    return packageVersion;
  }

  public void setPackageVersion(String packageVersion) {
    this.packageVersion = packageVersion;
  }

  public long getPackageVersionCode() {
    return packageVersionCode;
  }

  public void setPackageVersionCode(long packageVersionCode) {
    this.packageVersionCode = packageVersionCode;
  }

  public long getFirstInstallTime() {
    return firstInstallTime;
  }

  public void setFirstInstallTime(long firstInstallTime) {
    this.firstInstallTime = firstInstallTime;
  }

  public long getLastUpdateTime() {
    return lastUpdateTime;
  }

  public void setLastUpdateTime(long lastUpdateTime) {
    this.lastUpdateTime = lastUpdateTime;
  }

  public CharSequence getLabel() {
    return label;
  }

  public void setLabel(CharSequence label) {
    this.label = label;
  }

  public Drawable getIcon() {
    return icon;
  }

  public void setIcon(Drawable icon) {
    this.icon = icon;
  }

  @Override
  public String toString() {
    return "ApplicationEntry{" +
        "packageName='" + packageName + '\'' +
        ", packageVersion='" + packageVersion + '\'' +
        ", packageVersionCode=" + packageVersionCode +
        ", firstInstallTime=" + firstInstallTime +
        ", lastUpdateTime=" + lastUpdateTime +
        ", label='" + label + '\'' +
        ", icon=" + icon +
        '}';
  }

  public interface Contract {
    /**
     * Table name
     */
    String TABLE_NAME = "applications";
    /**
     * Text column used for storing package name
     */
    String COLUMN_PACKAGE_NAME = "package_name";
    String COLUMN_PACKAGE_VERSION = "package_version";
    String COLUMN_PACKAGE_VERSION_CODE = "package_version_code";
    String COLUMN_FIRST_INSTALL_TIME = "first_install_time";
    String COLUMN_LAST_UPDATE_TIME = "last_update_time";
  }

  /**
   * Create a new instance mapping given info
   *
   * @param pm           Package manager to use
   * @param resolvedInfo Info to use
   * @return New instance mapped from given info or null if given info is invalid
   */
  public static ApplicationEntry from(final PackageManager pm, final ResolveInfo resolvedInfo) {
    if (resolvedInfo == null) {
      return null;
    }
    // We try first activities and broadcasts
    ApplicationEntry appEntry = from(pm, resolvedInfo.activityInfo);
    if (appEntry != null) {
      return appEntry;
    }
    // We try a service in such case
    return from(pm, resolvedInfo.serviceInfo);
  }

  /**
   * Create a new instance mapping given info
   *
   * @param pm   Package manager to use
   * @param info Info to use
   * @return New instance mapped from given info or null if given info is invalid
   */
  public static ApplicationEntry from(final PackageManager pm, final PackageItemInfo info) {
    if (info == null) {
      return null;
    }
    return from(pm, info.packageName);
  }

  public static ApplicationEntry from(final PackageManager pm, final String packageName) {
    if (StringUtils.isEmpty(packageName)) {
      return null;
    }
    ApplicationEntry appEntry = new ApplicationEntry();
    appEntry.setPackageName(packageName);
    try {
      PackageInfo packageInfo = pm.getPackageInfo(packageName, PackageManager.GET_DISABLED_COMPONENTS |
          PackageManager.GET_UNINSTALLED_PACKAGES |
          PackageManager.GET_SIGNATURES);
      appEntry.setPackageVersion(packageInfo.versionName);
      appEntry.setPackageVersionCode(packageInfo.versionCode);
      if (AppUtils.isNinePlus()) {
        appEntry.setFirstInstallTime(packageInfo.firstInstallTime);
        appEntry.setLastUpdateTime(packageInfo.lastUpdateTime);
      }
    } catch (PackageManager.NameNotFoundException e) {
      L.e("wn:appentry", "Unable to get package info", e);
    }
    return appEntry;
  }

}
