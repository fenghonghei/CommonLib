package com.honghei.feng.utillib.http;

import com.google.gson.JsonObject;

/**
 * author : feng
 * description ： http响应实体类
 * creation time : 18-7-25上午10:18
 */
public class HttpResponse {

  private static final int VALID_RESPONSE = 200;

  private int code;
  private String message;
  private JsonObject jsonObject;

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

  public JsonObject getJsonObject() {
    return jsonObject;
  }

  public void setJsonObject(JsonObject jsonObject) {
    this.jsonObject = jsonObject;
  }

  public boolean isValidResponse() {
    return code == VALID_RESPONSE;
  }
}
