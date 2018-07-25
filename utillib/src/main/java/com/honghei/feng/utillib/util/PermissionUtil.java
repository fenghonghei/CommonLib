package com.honghei.feng.utillib.util;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RequestExecutor;
import java.util.List;

/**
 * author : feng
 * description ： 运行时权限工具类
 * creation time : 18-7-18上午10:39
 */
public class PermissionUtil {

  private PermissionUtil() {
    throw new UnsupportedOperationException("u can't instantiate me...");
  }

  /**
   * 申请app必要的权限，一般用于启动app时的权限申请，没有权限，不能进入app
   *
   * @param activity 启动的第一个activity
   * @param permissions 申请的权限
   */
  public static void requestEssentialPermission(final Activity activity, final String... permissions) {
    AndPermission.with(activity)
        .runtime()
        .permission(permissions)
        .onDenied(new Action<List<String>>() {
          @Override
          public void onAction(List<String> permissions) {
            showSettingDialog(activity, permissions, true);
          }
        }).start();
  }

  /**
   * activity申请权限
   *
   * @param activity 当前activity
   * @param permissions 申请的权限
   */
  public static void requestPermission(final Activity activity, final OnPermissionRequestListener listener,
      String... permissions) {
    AndPermission.with(activity)
        .runtime()
        .permission(permissions)
        .rationale(new Rationale<List<String>>() {
          @Override
          public void showRationale(Context context, List<String> permissions, RequestExecutor executor) {
            showRationaleDialog(context, permissions, executor);
          }
        })
        .onGranted(new Action<List<String>>() {
          @Override
          public void onAction(List<String> permissions) {
            listener.onGranted(permissions);
          }
        })
        .onDenied(new Action<List<String>>() {
          @Override
          public void onAction(List<String> permissions) {
            if (AndPermission.hasAlwaysDeniedPermission(activity, permissions)) {
              showSettingDialog(activity, permissions, false);
            } else {
              listener.onDenied(permissions);
            }
          }
        }).start();
  }

  /**
   * fragment 申请权限
   *
   * @param fragment 当前fragment
   * @param listener 权限申请监听
   * @param permissions 申请的权限
   */
  public static void requestPermission(final Fragment fragment, final OnPermissionRequestListener listener,
      String... permissions) {
    AndPermission.with(fragment)
        .runtime()
        .permission(permissions)
        .rationale(new Rationale<List<String>>() {
          @Override
          public void showRationale(Context context, List<String> permissions, RequestExecutor executor) {
            showRationaleDialog(context, permissions, executor);
          }
        })
        .onGranted(new Action<List<String>>() {
          @Override
          public void onAction(List<String> permissions) {
            listener.onGranted(permissions);
          }
        })
        .onDenied(new Action<List<String>>() {
          @Override
          public void onAction(List<String> permissions) {
            if (AndPermission.hasAlwaysDeniedPermission(fragment, permissions)) {
              showSettingDialog(fragment.getActivity(), permissions, false);
            } else {
              listener.onDenied(permissions);
            }
          }
        }).start();
  }

  /**
   * 解释app为什么需要这些权限，引导用户重新请求权限
   *
   * @param context dialog上下文
   * @param permissions 请求的权限
   * @param executor 处理用户选择
   */
  private static void showRationaleDialog(Context context, List<String> permissions, final RequestExecutor executor) {
    new AlertDialog.Builder(context)
        .setTitle("权限解释")
        .setMessage("需要使用这个功能必须要有这个权限")
        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
            executor.execute();
          }
        })
        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
            executor.cancel();
          }
        })
        .setCancelable(false)
        .create()
        .show();
  }

  /**
   * 解释没有这些权限，app无法运行，提示用户到设置界面设置
   *
   * @param activity 申请权限的activity
   * @param deniedPermissions 用户拒绝过的权限
   * @param essential 是否是必要的权限
   */
  private static void showSettingDialog(final Activity activity, List<String> deniedPermissions,
      final boolean essential) {
    new AlertDialog.Builder(activity)
        .setTitle("权限设置")
        .setMessage("去应用设置界面打开权限")
        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
            AndPermission.with(activity).runtime().setting().start();
          }
        })
        .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
            if (essential) {
              activity.finish();
            }
          }
        })
        .setCancelable(false)
        .create()
        .show();
  }

  public interface OnPermissionRequestListener {

    void onGranted(List<String> permissions);

    void onDenied(List<String> permissions);
  }
}
