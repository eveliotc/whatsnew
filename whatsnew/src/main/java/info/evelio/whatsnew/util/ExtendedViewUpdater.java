package info.evelio.whatsnew.util;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
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

  public ViewGroup viewGroup(int index) {
    return (ViewGroup) childViews[index];
  }

  public void setBackground(int index, int resId) {
    childViews[index].setBackgroundResource(resId);
  }

  public void setBackground(int index, Drawable drawable) {
    //noinspection deprecation
    childViews[index].setBackgroundDrawable(drawable);
  }
}
