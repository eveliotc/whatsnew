package info.evelio.whatsnew.helper;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import info.evelio.whatsnew.R;

/**
 * @author Evelio Tarazona Cáceres <evelio@evelio.info>
 */
public class LoadingHelper {
  private View mView;
  private ViewGroup mTargetParent;

  public void onCreateView(final LayoutInflater inflater,
                           final ViewGroup container,
                           final Bundle savedInstanceState) {
    if (mView != null) {
      throw new IllegalStateException("View already created");
    }
    mView = inflater.inflate(R.layout.loading_view, container, false);
  }

  public void addTo(ViewGroup parent) {
    if (mTargetParent != null) {
      throw new IllegalStateException("Already using target parent");
    }
    autoRemove();
    if (!hasView()) {
      throw new IllegalStateException("Add requested but now view was created");
    }
    parent.addView(mView);
  }

  public void setTargetParent(ViewGroup parent) {
    if (mTargetParent != null) {
      throw new IllegalStateException("Target parent already set");
    }
    addTo(parent);
    mTargetParent = parent;
    show();
  }

  public void show() {
    if (mView != null && mView.getVisibility() != View.VISIBLE) {
      mView.setVisibility(View.VISIBLE);
    }
  }

  public void hide() {
    if (mView != null && mView.getVisibility() != View.GONE) {
      mView.setVisibility(View.GONE);
    }
  }

  public void autoRemove() {
    if (hasView()) {
      ViewParent parent = mView.getParent();
      if (parent instanceof ViewGroup) {
        ((ViewGroup) parent).removeView(mView);
      }
    }
  }

  private boolean hasView() {
    return mView != null;
  }

  public void onDestroyView() {
    autoRemove();
    mView = null;
    mTargetParent = null;
  }
}
