package com.honghei.feng.utillib.http.cache;

import com.honghei.feng.utillib.http.api.ApiHandler;
import com.honghei.feng.utillib.http.api.HttpResponse;
import io.reactivex.Observable;

/**
 * author : feng
 * description ： 不使用缓存策略
 * creation time : 18-7-26下午7:14
 */
public class NoneCache implements CacheStrategy {

  @Override
  public Observable<HttpResponse> fetchData(String url) {
    return ApiHandler.getInstance().getCommonApi().commonGet(url);
  }
}
