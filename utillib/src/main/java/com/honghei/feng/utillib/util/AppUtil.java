package com.honghei.feng.utillib.util;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Looper;
import java.util.List;

/**
 * author : feng
 * description ： app工具类
 * creation time : 18-7-12下午6:11
 */
public class AppUtil {

  private static Application sApplication;

  private AppUtil() {
    throw new UnsupportedOperationException("u can't instantiate me...");
  }

  public static void init(Application application) {
    if (sApplication == null) {
      sApplication = application;
    }
  }

  public static Application getApp() {
    if (sApplication != null) {
      return sApplication;
    }
    throw new NullPointerException("u should init first");
  }

  public static boolean isAppForeground() {
    ActivityManager am = (ActivityManager) getApp().getSystemService(Context.ACTIVITY_SERVICE);
    if (am == null) {
      return false;
    }
    List<RunningAppProcessInfo> info = am.getRunningAppProcesses();
    if (info == null || info.size() == 0) {
      return false;
    }
    for (ActivityManager.RunningAppProcessInfo aInfo : info) {
      if (aInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
        return aInfo.processName.equals(getApp().getPackageName());
      }
    }
    return false;
  }

  public static boolean isMainThread() {
    return Looper.myLooper() == Looper.getMainLooper();
  }

  public static AppInfo getAppInfo() {
    return getAppInfo(getApp().getPackageName());
  }

  private static AppInfo getAppInfo(final String packageName) {
    try {
      PackageManager pm = getApp().getPackageManager();
      PackageInfo pi = pm.getPackageInfo(packageName, 0);
      return getBean(pm, pi);
    } catch (PackageManager.NameNotFoundException e) {
      e.printStackTrace();
      return null;
    }
  }

  private static AppInfo getBean(final PackageManager pm, final PackageInfo pi) {
    if (pm == null || pi == null) {
      return null;
    }
    ApplicationInfo ai = pi.applicationInfo;
    String packageName = pi.packageName;
    String name = ai.loadLabel(pm).toString();
    Drawable icon = ai.loadIcon(pm);
    String packagePath = ai.sourceDir;
    String versionName = pi.versionName;
    int versionCode = pi.versionCode;
    boolean isSystem = (ApplicationInfo.FLAG_SYSTEM & ai.flags) != 0;
    return new AppInfo(packageName, name, icon, packagePath, versionName, versionCode, isSystem);
  }

  public static class AppInfo {

    private String packageName;
    private String name;
    private Drawable icon;
    private String packagePath;
    private String versionName;
    private int versionCode;
    private boolean isSystem;

    AppInfo(String packageName, String name, Drawable icon, String packagePath, String versionName,
        int versionCode,
        boolean isSystem) {
      this.packageName = packageName;
      this.name = name;
      this.icon = icon;
      this.packagePath = packagePath;
      this.versionName = versionName;
      this.versionCode = versionCode;
      this.isSystem = isSystem;
    }

    @Override
    public String toString() {
      return "AppInfo{" +
          "packageName='" + packageName + '\'' +
          ", name='" + name + '\'' +
          ", icon=" + icon +
          ", packagePath='" + packagePath + '\'' +
          ", versionName='" + versionName + '\'' +
          ", versionCode=" + versionCode +
          ", isSystem=" + isSystem +
          '}';
    }
  }
}
