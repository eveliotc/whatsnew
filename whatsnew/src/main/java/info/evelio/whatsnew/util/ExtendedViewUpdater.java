package info.evelio.whatsnew.util;

import android.view.View;
import com.github.kevinsawicki.wishlist.ViewUpdater;

/**
 * @author Evelio Tarazona Cáceres <evelio@evelio.info>
 */
public class ExtendedViewUpdater extends ViewUpdater {
  public void clickListener(int index, View.OnClickListener listener) {
    view(index).setOnClickListener(listener);
  }

  public void clickListener(View.OnClickListener listener) {
    for (View child : childViews) {
      child.setOnClickListener(listener);
    }
  }
}
