package info.evelio.whatsnew.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import com.github.kevinsawicki.wishlist.SingleTypeAdapter;
import info.evelio.whatsnew.R;
import info.evelio.whatsnew.model.ApplicationEntry;

/**
 * @author Evelio Tarazona Cáceres <evelio@evelio.info>
 */
public class ApplicationsAdapter extends SingleTypeAdapter<ApplicationEntry> {
  private static final int[] CHILD_VIEW_IDS = { R.id.app_item_icon, R.id.app_item_label };
  private final Drawable mDefaultDrawable;
  public ApplicationsAdapter(Context context) {
    super(context, R.layout.application_item);
    mDefaultDrawable = context.getResources().getDrawable(R.color.default_drawable);
  }

  @Override
  protected int[] getChildViewIds() {
    return CHILD_VIEW_IDS;
  }

  @Override
  protected void update(int position, ApplicationEntry item) {
    final Drawable icon = item.getIcon();
    imageView(0).setImageDrawable(icon != null ? item.getIcon() : mDefaultDrawable);
    final CharSequence label = item.getLabel();
    setText(1, label != null ? label : item.getPackageName());
  }
}
