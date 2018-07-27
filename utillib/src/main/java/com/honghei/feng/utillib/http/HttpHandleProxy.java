package com.honghei.feng.utillib.http;

import com.honghei.feng.utillib.http.api.ApiException;
import com.honghei.feng.utillib.util.Logger;
import java.io.IOException;
import retrofit2.HttpException;

/**
 * author : feng
 * description ： 异常处理器代理
 * creation time : 18-7-25下午7:01
 */
public class HttpHandleProxy {

  private IHttpHandle httpHandle;

  public HttpHandleProxy(IHttpHandle httpHandle) {
    this.httpHandle = httpHandle;
  }

  public void handler(Throwable throwable) {
    if (throwable instanceof IOException) {
      handleIOException();
    } else if (throwable instanceof HttpException) {
      // We had non-2XX http error
      handleHttpException((HttpException) throwable);
    } else if (throwable instanceof ApiException) {
      handleApiException((ApiException) throwable);
    } else {
      Logger.e(throwable.toString());
    }
  }

  private void handleIOException() {
    httpHandle.onIOException();
  }

  private void handleHttpException(HttpException e) {
    httpHandle.onHttpException(e);
  }

  private void handleApiException(ApiException e) {
    httpHandle.onApiException(e);
  }
}
