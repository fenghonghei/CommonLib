package com.honghei.feng.utillib;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.IntRange;
import android.support.v4.widget.DrawerLayout;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

/**
 * author : feng
 * description ： bar工具类
 * creation time : 18-7-12下午6:11
 */
public class BarUtil {

  private static final String TAG_COLOR = "TAG_COLOR";
  private static final String TAG_ALPHA = "TAG_ALPHA";
  private static final int TAG_OFFSET = -123;
  private static final int DEFAULT_ALPHA = 255;

  private BarUtil() {
    throw new UnsupportedOperationException("u can't instantiate me...");
  }

  /******************************************************StatusBar*****************************************************/

  public static int getStatusBarHeight() {
    Resources resources = AppUtil.getApp().getResources();
    int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
    return resources.getDimensionPixelSize(resourceId);
  }

  public static void setStatusBarVisibility(Activity activity,
      boolean isVisible) {
    setStatusBarVisibility(activity.getWindow(), isVisible);
  }

  private static void setStatusBarVisibility(Window window,
      boolean isVisible) {
    if (isVisible) {
      window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    } else {
      window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
  }

  public static boolean isStatusBarVisible(Activity activity) {
    int flags = activity.getWindow().getAttributes().flags;
    return (flags & WindowManager.LayoutParams.FLAG_FULLSCREEN) == 0;
  }

  public static void setStatusBarLightMode(Activity activity, boolean isLightMode) {
    setStatusBarLightMode(activity.getWindow(), isLightMode);
  }

  private static void setStatusBarLightMode(Window window, boolean isLightMode) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      View decorView = window.getDecorView();
      if (decorView != null) {
        int vis = decorView.getSystemUiVisibility();
        if (isLightMode) {
          window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
          vis |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        } else {
          vis &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        }
        decorView.setSystemUiVisibility(vis);
      }
    }
  }

  public static void addMarginTopEqualStatusBarHeight(View view) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
      return;
    }
    Object haveSetOffset = view.getTag(TAG_OFFSET);
    if (haveSetOffset != null && (Boolean) haveSetOffset) {
      return;
    }
    MarginLayoutParams layoutParams = (MarginLayoutParams) view.getLayoutParams();
    layoutParams.setMargins(layoutParams.leftMargin,
        layoutParams.topMargin + getStatusBarHeight(),
        layoutParams.rightMargin,
        layoutParams.bottomMargin);
    view.setTag(TAG_OFFSET, true);
  }

  public static void setStatusBarColor(Activity activity, @ColorInt int color) {
    setStatusBarColor(activity, color, DEFAULT_ALPHA, false);
  }

  public static void setStatusBarColor(Activity activity, @ColorInt int color,
      @IntRange(from = 0, to = 255) int alpha) {
    setStatusBarColor(activity, color, alpha, false);
  }

  public static void setStatusBarColor(Activity activity, @ColorInt int color,
      @IntRange(from = 0, to = 255) int alpha,
      boolean isDecor) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
      return;
    }
    hideAlphaView(activity);
    transparentStatusBar(activity);
    addStatusBarColor(activity, color, alpha, isDecor);
  }


  public static void setStatusBarColor(View fakeStatusBar, @ColorInt int color) {
    setStatusBarColor(fakeStatusBar, color, DEFAULT_ALPHA);
  }

  public static void setStatusBarColor(View fakeStatusBar, @ColorInt int color,
      @IntRange(from = 0, to = 255) int alpha) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
      return;
    }
    fakeStatusBar.setVisibility(View.VISIBLE);
    transparentStatusBar((Activity) fakeStatusBar.getContext());
    ViewGroup.LayoutParams layoutParams = fakeStatusBar.getLayoutParams();
    layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
    layoutParams.height = BarUtil.getStatusBarHeight();
    fakeStatusBar.setBackgroundColor(getStatusBarColor(color, alpha));
  }

  public static void setStatusBarAlpha(Activity activity) {
    setStatusBarAlpha(activity, DEFAULT_ALPHA, false);
  }

  public static void setStatusBarAlpha(Activity activity, @IntRange(from = 0, to = 255) int alpha) {
    setStatusBarAlpha(activity, alpha, false);
  }

  public static void setStatusBarAlpha(Activity activity, @IntRange(from = 0, to = 255) int alpha,
      boolean isDecor) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
      return;
    }
    hideColorView(activity);
    transparentStatusBar(activity);
    addStatusBarAlpha(activity, alpha, isDecor);
  }

  public static void setStatusBarAlpha(View fakeStatusBar) {
    setStatusBarAlpha(fakeStatusBar, DEFAULT_ALPHA);
  }

  public static void setStatusBarAlpha(View fakeStatusBar,
      @IntRange(from = 0, to = 255) int alpha) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
      return;
    }
    fakeStatusBar.setVisibility(View.VISIBLE);
    transparentStatusBar((Activity) fakeStatusBar.getContext());
    ViewGroup.LayoutParams layoutParams = fakeStatusBar.getLayoutParams();
    layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
    layoutParams.height = BarUtil.getStatusBarHeight();
    fakeStatusBar.setBackgroundColor(Color.argb(alpha, 0, 0, 0));
  }

  public static void setStatusBarColor4Drawer(Activity activity,
      DrawerLayout drawer,
      View fakeStatusBar,
      @ColorInt int color,
      boolean isTop) {
    setStatusBarColor4Drawer(activity, drawer, fakeStatusBar, color, DEFAULT_ALPHA, isTop);
  }

  public static void setStatusBarColor4Drawer(Activity activity,
      DrawerLayout drawer,
      View fakeStatusBar,
      @ColorInt int color,
      @IntRange(from = 0, to = 255) int alpha,
      boolean isTop) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
      return;
    }
    drawer.setFitsSystemWindows(false);
    transparentStatusBar(activity);
    setStatusBarColor(fakeStatusBar, color, isTop ? alpha : 0);
    for (int i = 0, len = drawer.getChildCount(); i < len; i++) {
      drawer.getChildAt(i).setFitsSystemWindows(false);
    }
    if (isTop) {
      hideAlphaView(activity);
    } else {
      addStatusBarAlpha(activity, alpha, false);
    }
  }

  public static void setStatusBarAlpha4Drawer(Activity activity,
      DrawerLayout drawer,
      View fakeStatusBar,
      boolean isTop) {
    setStatusBarAlpha4Drawer(activity, drawer, fakeStatusBar, DEFAULT_ALPHA, isTop);
  }

  public static void setStatusBarAlpha4Drawer(Activity activity,
      DrawerLayout drawer,
      View fakeStatusBar,
      @IntRange(from = 0, to = 255) int alpha,
      boolean isTop) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
      return;
    }
    drawer.setFitsSystemWindows(false);
    transparentStatusBar(activity);
    setStatusBarAlpha(fakeStatusBar, isTop ? alpha : 0);
    for (int i = 0, len = drawer.getChildCount(); i < len; i++) {
      drawer.getChildAt(i).setFitsSystemWindows(false);
    }
    if (isTop) {
      hideAlphaView(activity);
    } else {
      addStatusBarAlpha(activity, alpha, false);
    }
  }

  private static void addStatusBarColor(Activity activity,
      int color,
      int alpha,
      boolean isDecor) {
    ViewGroup parent = isDecor ?
        (ViewGroup) activity.getWindow().getDecorView() :
        (ViewGroup) activity.findViewById(android.R.id.content);
    View fakeStatusBarView = parent.findViewWithTag(TAG_COLOR);
    if (fakeStatusBarView != null) {
      if (fakeStatusBarView.getVisibility() == View.GONE) {
        fakeStatusBarView.setVisibility(View.VISIBLE);
      }
      fakeStatusBarView.setBackgroundColor(getStatusBarColor(color, alpha));
    } else {
      parent.addView(createColorStatusBarView(parent.getContext(), color, alpha));
    }
  }

  private static void addStatusBarAlpha(Activity activity,
      int alpha,
      boolean isDecor) {
    ViewGroup parent = isDecor ?
        (ViewGroup) activity.getWindow().getDecorView() :
        (ViewGroup) activity.findViewById(android.R.id.content);
    View fakeStatusBarView = parent.findViewWithTag(TAG_ALPHA);
    if (fakeStatusBarView != null) {
      if (fakeStatusBarView.getVisibility() == View.GONE) {
        fakeStatusBarView.setVisibility(View.VISIBLE);
      }
      fakeStatusBarView.setBackgroundColor(Color.argb(alpha, 0, 0, 0));
    } else {
      parent.addView(createAlphaStatusBarView(parent.getContext(), alpha));
    }
  }

  private static void hideColorView(Activity activity) {
    ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
    View fakeStatusBarView = decorView.findViewWithTag(TAG_COLOR);
    if (fakeStatusBarView == null) {
      return;
    }
    fakeStatusBarView.setVisibility(View.GONE);
  }

  private static void hideAlphaView(Activity activity) {
    ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
    View fakeStatusBarView = decorView.findViewWithTag(TAG_ALPHA);
    if (fakeStatusBarView == null) {
      return;
    }
    fakeStatusBarView.setVisibility(View.GONE);
  }

  private static int getStatusBarColor(int color, int alpha) {
    if (alpha == 0) {
      return color;
    }
    float a = 1 - alpha / 255f;
    int red = (color >> 16) & 0xff;
    int green = (color >> 8) & 0xff;
    int blue = color & 0xff;
    red = (int) (red * a + 0.5);
    green = (int) (green * a + 0.5);
    blue = (int) (blue * a + 0.5);
    return Color.argb(255, red, green, blue);
  }

  private static View createColorStatusBarView(Context context,
      int color,
      int alpha) {
    View statusBarView = new View(context);
    statusBarView.setLayoutParams(new LinearLayout.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT, getStatusBarHeight()));
    statusBarView.setBackgroundColor(getStatusBarColor(color, alpha));
    statusBarView.setTag(TAG_COLOR);
    return statusBarView;
  }

  private static View createAlphaStatusBarView(Context context, int alpha) {
    View statusBarView = new View(context);
    statusBarView.setLayoutParams(new LinearLayout.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT, getStatusBarHeight()));
    statusBarView.setBackgroundColor(Color.argb(alpha, 0, 0, 0));
    statusBarView.setTag(TAG_ALPHA);
    return statusBarView;
  }

  public static void transparentStatusBar(Activity activity) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
      return;
    }
    Window window = activity.getWindow();
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
      int option = View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
      window.getDecorView().setSystemUiVisibility(option);
      window.setStatusBarColor(Color.TRANSPARENT);
    } else {
      window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
    }
  }

  /******************************************************ActionBar*****************************************************/

  public static int getActionBarHeight() {
    TypedValue tv = new TypedValue();
    if (AppUtil.getApp().getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
      return TypedValue.complexToDimensionPixelSize(
          tv.data, AppUtil.getApp().getResources().getDisplayMetrics()
      );
    }
    return 0;
  }

}
