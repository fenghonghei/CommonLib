package com.honghei.feng.utillib.http;

import com.honghei.feng.utillib.http.api.ApiException;
import retrofit2.HttpException;

/**
 * author : feng
 * description ： 网络请求的页面实现，统一管理网络相关回调
 * creation time : 18-7-25下午7:59
 */
public interface IHttpHandle {

  void onStart();

  void onComplete();

  void onIOException();

  void onHttpException(HttpException e);

  void onApiException(ApiException e);
}
