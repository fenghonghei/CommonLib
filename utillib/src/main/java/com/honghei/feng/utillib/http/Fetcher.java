package com.honghei.feng.utillib.http;

import com.google.gson.Gson;
import com.honghei.feng.utillib.http.api.HttpResponse;
import com.honghei.feng.utillib.http.cache.CacheStrategy;
import com.honghei.feng.utillib.http.cache.ExpireCache;
import com.honghei.feng.utillib.http.cache.NoneCache;
import com.honghei.feng.utillib.http.cache.UpdateCache;
import io.reactivex.Observable;
import io.reactivex.functions.Function;

/**
 * author : feng
 * description ： 获取数据
 * creation time : 18-7-26上午9:13
 */
public class Fetcher {

  public static <T> Observable<T> fetchData(String url, Class<T> tClass) {
    return fetchData(url, tClass, CacheStrategy.NONE_CACHE);
  }

  public static <T> Observable<T> fetchData(String url, Class<T> tClass, int cacheStrategy) {
    return fetchData(url, tClass, cacheStrategy, 0);

  }

  public static <T> Observable<T> fetchData(String url, final Class<T> tClass, int cacheStrategy, long expireTime) {
    Observable<HttpResponse> observable;
    if (cacheStrategy == CacheStrategy.NONE_CACHE) {
      observable = new NoneCache().fetchData(url);
    } else if (cacheStrategy == CacheStrategy.EXPIRE_CACHE) {
      if (expireTime <= 0) {
        throw new IllegalArgumentException(" expire time must > 0");
      }
      observable = new ExpireCache(expireTime).fetchData(url);
    } else if (cacheStrategy == CacheStrategy.UPDATE_CACHE) {
      observable = new UpdateCache().fetchData(url);
    } else {
      throw new IllegalArgumentException(" expire cacheStrategy must be none update or expire cache");
    }
    return observable.map(new Function<HttpResponse, T>() {
      @Override
      public T apply(HttpResponse httpResponse) throws Exception {
        Gson gson = new Gson();
        return gson.fromJson(httpResponse.getData(), tClass);
      }
    });
  }
}
