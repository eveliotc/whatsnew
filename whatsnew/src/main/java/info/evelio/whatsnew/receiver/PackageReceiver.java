package info.evelio.whatsnew.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.telly.groundy.Groundy;
import com.telly.groundy.GroundyTask;
import info.evelio.whatsnew.task.PackageAdd;
import info.evelio.whatsnew.task.PackageRemove;
import info.evelio.whatsnew.task.PackageReplace;
import info.evelio.whatsnew.task.PackageTask;
import info.evelio.whatsnew.util.L;

import java.util.HashMap;
import java.util.Map;

import static info.evelio.whatsnew.util.StringUtils.bundle;

/**
 * @author Evelio Tarazona Cáceres <evelio@evelio.info>
 */
public class PackageReceiver extends BroadcastReceiver {
  private static final String TAG = "wn:PR";
  private final static Map<String, ResolveTask> sTasks;
  static {
    sTasks = new HashMap<String, ResolveTask>(3);
    sTasks.put(Intent.ACTION_PACKAGE_ADDED, new NonReplacingPackageResolveTask(PackageAdd.class));
    sTasks.put(Intent.ACTION_PACKAGE_REPLACED, new PackageResolveTask(PackageReplace.class));
    sTasks.put(Intent.ACTION_PACKAGE_REMOVED, new RemovePackageResolveTask(PackageRemove.class));
  }


  @Override
  public void onReceive(Context context, Intent intent) {
    log(intent);
    if (intent == null) {
      return;
    }
    final String action = intent.getAction();

    final ResolveTask resolved = sTasks.get(action);
    final boolean invalidResolved = resolved == null;
    if (invalidResolved || !resolved.canPerformWith(intent)) {
      L.d(TAG,
          invalidResolved ? "Unable to resolve" : "Unable to perform"
          + " for action " + action);
      return;
    }
    Groundy.create(context, resolved.getTaskClass())
        .params(resolved.buildParams(intent))
        .execute();
  }

  private static class ResolveTask {
    private Class<? extends GroundyTask> mTaskClass;

    ResolveTask(Class<? extends GroundyTask> datClass) {
      mTaskClass = datClass;
    }

    public boolean canPerformWith(Intent intent) {
      return intent != null;
    }

    public Class<? extends GroundyTask> getTaskClass() {
      return mTaskClass;
    }

    public Bundle buildParams(Intent intent) {
      return Bundle.EMPTY;
    }
  }

  private static class RemovePackageResolveTask extends NonReplacingPackageResolveTask {
    RemovePackageResolveTask(Class<? extends GroundyTask> datClass) {
      super(datClass);
    }

    @Override
    public boolean canPerformWith(Intent intent) {
      return intent != null && intent.getBooleanExtra(Intent.EXTRA_DATA_REMOVED, false) && super.canPerformWith(intent);
    }
  }

  private static class NonReplacingPackageResolveTask extends PackageResolveTask {
    NonReplacingPackageResolveTask(Class<? extends GroundyTask> datClass) {
      super(datClass);
    }

    @Override
    public boolean canPerformWith(Intent intent) {
      return super.canPerformWith(intent) && !intent.getBooleanExtra(Intent.EXTRA_REPLACING, false);
    }
  }

  private static class PackageResolveTask extends ResolveTask {
    private static final String PACKAGE_SCHEME = "package";

    PackageResolveTask(Class<? extends GroundyTask> datClass) {
      super(datClass);
    }

    @Override
    public boolean canPerformWith(Intent intent) {
      return super.canPerformWith(intent) && PACKAGE_SCHEME.equalsIgnoreCase(intent.getScheme());
    }

    @Override
    public Bundle buildParams(Intent intent) {
      Bundle params = new Bundle(1);
      params.putString(PackageTask.PARAM_PACKAGE_NAME, intent.getData().getSchemeSpecificPart());
      return params;
    }
  }

  private static void log(Intent intent) {
    StringBuilder builder = new StringBuilder("Received ")
        .append(intent);
    if (intent == null) {
      L.d(TAG, builder.toString());
      return;
    }
    builder.append(bundle(intent.getExtras()));
    L.d(TAG, builder.toString());
  }
}

  /*
    This is what it looks logged out

    Uninstall:
02-25 11:31:23.437: DEBUG/wn:PR(5646): Received Intent { act=android.intent.action.PACKAGE_REMOVED dat=package:com.actionbarsherlock.sample.fragments flg=0x8000010 cmp=info.evelio.whatsnew/.receiver.PackageReceiver (has extras) } android.intent.extra.REMOVED_FOR_ALL_USERS: true android.intent.extra.DATA_REMOVED: true android.intent.extra.UID: 10180 android.intent.extra.user_handle: 0
02-25 11:31:23.437: DEBUG/wn:PR(5646): Received Intent { act=android.intent.action.PACKAGE_FULLY_REMOVED dat=package:com.actionbarsherlock.sample.fragments flg=0x8000010 cmp=info.evelio.whatsnew/.receiver.PackageReceiver (has extras) } android.intent.extra.REMOVED_FOR_ALL_USERS: true android.intent.extra.DATA_REMOVED: true android.intent.extra.UID: 10180 android.intent.extra.user_handle: 0

02-25 11:40:44.696: DEBUG/wn:PR(5646): Received Intent { act=android.intent.action.PACKAGE_REMOVED dat=package:com.com2us.monkeybattle.normal.freefull.google.global.android.common flg=0x8000010 cmp=info.evelio.whatsnew/.receiver.PackageReceiver (has extras) } android.intent.extra.REMOVED_FOR_ALL_USERS: true android.intent.extra.DATA_REMOVED: true android.intent.extra.UID: 10180 android.intent.extra.user_handle: 0
02-25 11:40:44.706: DEBUG/wn:PR(5646): Received Intent { act=android.intent.action.PACKAGE_FULLY_REMOVED dat=package:com.com2us.monkeybattle.normal.freefull.google.global.android.common flg=0x8000010 cmp=info.evelio.whatsnew/.receiver.PackageReceiver (has extras) } android.intent.extra.REMOVED_FOR_ALL_USERS: true android.intent.extra.DATA_REMOVED: true android.intent.extra.UID: 10180 android.intent.extra.user_handle: 0


    Install:
02-25 11:37:03.950: DEBUG/wn:PR(5646): Received Intent { act=android.intent.action.PACKAGE_ADDED dat=package:com.com2us.monkeybattle.normal.freefull.google.global.android.common flg=0x8000010 cmp=info.evelio.whatsnew/.receiver.PackageReceiver (has extras) } android.intent.extra.UID: 10180 android.intent.extra.user_handle: 0

    Update:
02-25 11:43:49.633: DEBUG/wn:PR(5646): Received Intent { act=android.intent.action.PACKAGE_REMOVED dat=package:com.mxtech.videoplayer.ad flg=0x8000010 cmp=info.evelio.whatsnew/.receiver.PackageReceiver (has extras) } android.intent.extra.REMOVED_FOR_ALL_USERS: false android.intent.extra.DATA_REMOVED: false android.intent.extra.REPLACING: true android.intent.extra.user_handle: 0 android.intent.extra.UID: 10150
02-25 11:43:50.064: DEBUG/wn:PR(5646): Received Intent { act=android.intent.action.PACKAGE_ADDED dat=package:com.mxtech.videoplayer.ad flg=0x8000010 cmp=info.evelio.whatsnew/.receiver.PackageReceiver (has extras) } android.intent.extra.REPLACING: true android.intent.extra.UID: 10150 android.intent.extra.user_handle: 0


// After adding android.intent.action.PACKAGE_REPLACED

  Replace my package:
02-25 11:49:06.221: DEBUG/wn:PR(11095): Received Intent { act=android.intent.action.PACKAGE_REMOVED dat=package:info.evelio.whatsnew flg=0x8000010 cmp=info.evelio.whatsnew/.receiver.PackageReceiver (has extras) } android.intent.extra.REMOVED_FOR_ALL_USERS: false android.intent.extra.DATA_REMOVED: false android.intent.extra.REPLACING: true android.intent.extra.user_handle: 0 android.intent.extra.UID: 10171
02-25 11:49:09.965: DEBUG/wn:PR(11095): Received Intent { act=android.intent.action.PACKAGE_REPLACED dat=package:info.evelio.whatsnew flg=0x8000010 cmp=info.evelio.whatsnew/.receiver.PackageReceiver (has extras) } android.intent.extra.REPLACING: true android.intent.extra.UID: 10171 android.intent.extra.user_handle: 0

  Uninstall:
02-25 12:20:13.053: DEBUG/wn:PR(11095): Received Intent { act=android.intent.action.PACKAGE_REMOVED dat=package:com.actionbarsherlock.sample.demos flg=0x8000010 cmp=info.evelio.whatsnew/.receiver.PackageReceiver (has extras) } android.intent.extra.REMOVED_FOR_ALL_USERS: true android.intent.extra.DATA_REMOVED: true android.intent.extra.UID: 10181 android.intent.extra.user_handle: 0
02-25 12:20:13.053: DEBUG/wn:PR(11095): Received Intent { act=android.intent.action.PACKAGE_FULLY_REMOVED dat=package:com.actionbarsherlock.sample.demos flg=0x8000010 cmp=info.evelio.whatsnew/.receiver.PackageReceiver (has extras) } android.intent.extra.REMOVED_FOR_ALL_USERS: true android.intent.extra.DATA_REMOVED: true android.intent.extra.UID: 10181 android.intent.extra.user_handle: 0

  Install:
02-25 12:28:48.503: DEBUG/wn:PR(11095): Received Intent { act=android.intent.action.PACKAGE_ADDED dat=package:com.halfbrick.jetpackjoyride flg=0x8000010 cmp=info.evelio.whatsnew/.receiver.PackageReceiver (has extras) } android.intent.extra.UID: 10180 android.intent.extra.user_handle: 0

  Update:
02-25 13:00:10.281: DEBUG/wn:PR(11095): Received Intent { act=android.intent.action.PACKAGE_REMOVED dat=package:com.facebook.katana flg=0x8000010 cmp=info.evelio.whatsnew/.receiver.PackageReceiver (has extras) } android.intent.extra.REMOVED_FOR_ALL_USERS: false android.intent.extra.DATA_REMOVED: false android.intent.extra.REPLACING: true android.intent.extra.user_handle: 0 android.intent.extra.UID: 10093
02-25 13:00:10.441: DEBUG/wn:PR(11095): Received Intent { act=android.intent.action.PACKAGE_ADDED dat=package:com.facebook.katana flg=0x8000010 cmp=info.evelio.whatsnew/.receiver.PackageReceiver (has extras) } android.intent.extra.REPLACING: true android.intent.extra.UID: 10093 android.intent.extra.user_handle: 0
02-25 13:00:10.902: DEBUG/wn:PR(11095): Received Intent { act=android.intent.action.PACKAGE_REPLACED dat=package:com.facebook.katana flg=0x8000010 cmp=info.evelio.whatsnew/.receiver.PackageReceiver (has extras) } android.intent.extra.REPLACING: true android.intent.extra.UID: 10093 android.intent.extra.user_handle: 0

// Summarizing
  Uninstall:
  android.intent.action.PACKAGE_REMOVED && !android.intent.extra.REPLACING: true
  Install:
  android.intent.action.PACKAGE_ADDED (only)
  Update:
  android.intent.action.PACKAGE_REPLACED (only)
  */
