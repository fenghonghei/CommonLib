package com.honghei.feng.utillib.http;

/**
 * author : feng
 * description ： api异常
 * creation time : 18-7-25下午4:48
 */
public class ApiException extends RuntimeException {

  private int errorCode;

  public ApiException(int errorCode, String message) {
    super(message);
    this.errorCode = errorCode;
  }

  public int getErrorCode() {
    return errorCode;
  }
}
