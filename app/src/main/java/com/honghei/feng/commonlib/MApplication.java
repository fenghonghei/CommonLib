package com.honghei.feng.commonlib;

import android.app.Application;
import com.honghei.feng.utillib.util.AppUtil;

/**
 * author : feng
 * description ：
 * creation time : 18-7-13下午3:35
 */
public class MApplication extends Application {

  @Override
  public void onCreate() {
    super.onCreate();
    AppUtil.init(this);
  }
}
