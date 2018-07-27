package com.honghei.feng.utillib.http.api;

import com.google.gson.JsonElement;
import com.honghei.feng.utillib.constant.HttpConstant;

/**
 * author : feng
 * description ： http响应实体类
 * creation time : 18-7-25上午10:18
 */
public class HttpResponse {

  private int errorCode;
  private String errorMsg;
  private JsonElement data;

  public int getCode() {
    return errorCode;
  }

  public void setCode(int code) {
    this.errorCode = code;
  }

  public String getMessage() {
    return errorMsg;
  }

  public void setMessage(String message) {
    this.errorMsg = message;
  }

  public JsonElement getData() {
    return data;
  }

  public void setData(JsonElement data) {
    this.data = data;
  }

  public boolean isValidResponse() {
    return errorCode == HttpConstant.VALID_RESPONSE;
  }
}
