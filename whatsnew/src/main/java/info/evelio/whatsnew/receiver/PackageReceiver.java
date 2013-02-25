package info.evelio.whatsnew.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import info.evelio.whatsnew.util.L;

import java.util.Set;

/**
 * @author Evelio Tarazona Cáceres <evelio@evelio.info>
 */
public class PackageReceiver extends BroadcastReceiver {

  @Override
  public void onReceive(Context context, Intent intent) {
    StringBuilder builder = new StringBuilder("Received ")
        .append(intent);
    Set<String> keys;
    if (intent == null || intent.getExtras() == null || (keys = intent.getExtras().keySet()).isEmpty()) {
      L.d("wn:PR", builder.toString());
      return;
    }
    Bundle extras = intent.getExtras();
    for (String key : keys) {
      builder.append(" ").append(key).append(": ").append(String.valueOf(extras.get(key)));
    }
    L.d("wn:PR", builder.toString());

  }
}
