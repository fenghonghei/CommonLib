package com.honghei.feng.commonlib;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import com.honghei.feng.utilslib.Logger;
import com.honghei.feng.utilslib.PermissionUtil;
import com.honghei.feng.utilslib.PermissionUtil.OnPermissionRequestListener;
import com.honghei.feng.utilslib.constant.PermissionConstant;
import java.util.List;

/**
 * author : feng
 * description ： 权限测试
 * creation time : 18-7-16下午4:59
 */
public class PermissionActivity extends AppCompatActivity {

  private static final String TAG = "PermissionActivity";

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_permission);
  }

  public void essentialRequest(View view) {
    PermissionUtil.requestEssentialPermission(this, PermissionConstant.CAMERA);
  }

  public void normalRequest(View view) {
    PermissionUtil.requestPermission(this, new OnPermissionRequestListener() {
      @Override
      public void onGranted(List<String> permissions) {
        Logger.e("onGranted");
      }

      @Override
      public void onDenied(List<String> permissions) {
        Logger.e("onDenied");
      }
    }, PermissionConstant.CAMERA);
  }
}
