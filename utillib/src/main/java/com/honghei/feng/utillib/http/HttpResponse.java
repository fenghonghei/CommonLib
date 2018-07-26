package com.honghei.feng.utillib.http;

import com.google.gson.JsonObject;
import com.honghei.feng.utillib.constant.HttpConstant;

/**
 * author : feng
 * description ： http响应实体类
 * creation time : 18-7-25上午10:18
 */
public class HttpResponse {

  private int code;
  private String message;
  private JsonObject data;

  public int getCode() {
    return code;
  }

  public void setCode(int code) {
    this.code = code;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public JsonObject getData() {
    return data;
  }

  public void setData(JsonObject data) {
    this.data = data;
  }

  public boolean isValidResponse() {
    return code == HttpConstant.VALID_RESPONSE;
  }
}
