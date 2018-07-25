package com.honghei.feng.utillib.http;

import java.io.IOException;
import retrofit2.HttpException;

/**
 * author : feng
 * description ： 异常处理器
 * creation time : 18-7-25下午7:01
 */
public interface IHttpExceptionHandler {

  void handler(Throwable throwable);

  abstract class HttpExceptionHandleAdapter implements IHttpExceptionHandler {

    @Override
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

    protected abstract void handleIOException();

    protected void handleHttpException(HttpException e) {

    }

    protected void handleApiException(ApiException e) {

    }

  }
}
