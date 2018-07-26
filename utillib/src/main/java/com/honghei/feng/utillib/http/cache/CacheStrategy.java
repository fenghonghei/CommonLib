package com.honghei.feng.utillib.http.cache;

import com.honghei.feng.utillib.http.api.HttpResponse;
import io.reactivex.Observable;

/**
 * author : feng
 * description ： 缓存策略
 * creation time : 18-7-26上午9:28
 */
public interface CacheStrategy {

  int NONE_CACHE = 0x00;
  int EXPIRE_CACHE = 0x01;
  int UPDATE_CACHE = 0x02;

  Observable<HttpResponse> fetchData(String url);
}
