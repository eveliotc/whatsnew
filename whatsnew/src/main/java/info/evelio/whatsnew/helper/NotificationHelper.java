package info.evelio.whatsnew.helper;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import info.evelio.whatsnew.R;
import info.evelio.whatsnew.activity.MainActivity;
import info.evelio.whatsnew.model.ApplicationEntry;

import java.util.List;

import static info.evelio.whatsnew.helper.PrefsHelper.BOOLEAN_NOTIFICATIONS_ENABLED;

/**
 * @author Evelio Tarazona Cáceres <evelio@evelio.info>
 */
public class NotificationHelper {
  private static final int DEFAULT_NOTIFICATION_REQUEST = 2112;
  private Context mContext;
  private final String mContentTitle;
  private final String mContentText;
  private PrefsHelper mPrefs;

  public NotificationHelper(Context context) {
    mContext = context;
    mContentTitle = context.getString(R.string.app_name);
    mContentText = context.getString(R.string.some_apps_were_updated);
  }

  public boolean areNotificationsEnabled() {
    ensurePrefs();
    return mPrefs.getBool(BOOLEAN_NOTIFICATIONS_ENABLED, true);
  }

  public void setNotificationsEnabled(boolean enabled) {
    ensurePrefs();
    mPrefs.putBool(BOOLEAN_NOTIFICATIONS_ENABLED, enabled);
  }

  private void ensurePrefs() {
    if (mPrefs == null) {
      mPrefs = new PrefsHelper(mContext);
    }
  }

  public void showUpdated(List<ApplicationEntry> entries) {
    if (!areNotificationsEnabled()) {
      return;
    }
    NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext);
    builder.setAutoCancel(true)
      .setWhen(System.currentTimeMillis())
        .setSmallIcon(R.drawable.ic_stat_default)
        .setContentTitle(mContentTitle)
        .setContentText(mContentText)
        .setContentIntent(buildContentIntent())
        .setAutoCancel(true);

    if (entries != null && entries.size() > 0) {
      builder.setNumber(entries.size());

      NotificationCompat.InboxStyle style = new NotificationCompat.InboxStyle(builder);
      style.setBigContentTitle(mContentTitle).setSummaryText(mContentText);
      for (ApplicationEntry entry : entries) {
        style.addLine(entry.getDisplayableLabel());
      }
    }

    NotificationManager nm = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
    nm.notify(R.id.update_notification, builder.build());
  }

  private PendingIntent buildContentIntent() {
    Intent intent = new Intent(mContext, MainActivity.class);
    return PendingIntent.getActivity(mContext, DEFAULT_NOTIFICATION_REQUEST, intent,
        PendingIntent.FLAG_UPDATE_CURRENT);
  }
}
