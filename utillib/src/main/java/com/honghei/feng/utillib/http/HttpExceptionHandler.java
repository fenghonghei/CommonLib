package com.honghei.feng.utillib.http;

import java.io.IOException;
import retrofit2.HttpException;

/**
 * author : feng
 * description ： 异常处理器
 * creation time : 18-7-25下午7:01
 */
public class HttpExceptionHandler {

  private HttpHelper httpHelper;

  public HttpExceptionHandler(HttpHelper httpHelper) {
    this.httpHelper = httpHelper;
  }

  public void handler(Throwable throwable) {
    if (throwable instanceof IOException) {
      handleIOException();
    } else if (throwable instanceof HttpException) {
      // We had non-2XX http error
      handleHttpException((HttpException) throwable);
    } else if (throwable instanceof ApiException) {
      handleApiException((ApiException) throwable);
    }
  }

  private void handleIOException() {
    httpHelper.onIOException();
  }

  private void handleHttpException(HttpException e) {
    httpHelper.onHttpException(e);
  }

  private void handleApiException(ApiException e) {
    httpHelper.onApiException(e);
  }
}
