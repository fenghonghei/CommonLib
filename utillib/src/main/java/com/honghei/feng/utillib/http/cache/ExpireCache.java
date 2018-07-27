package com.honghei.feng.utillib.http.cache;

import com.google.gson.Gson;
import com.honghei.feng.utillib.http.api.ApiHandler;
import com.honghei.feng.utillib.http.api.HttpResponse;
import com.honghei.feng.utillib.util.FileIOUtil;
import com.honghei.feng.utillib.util.FileUtil;
import com.honghei.feng.utillib.util.Logger;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Consumer;
import java.io.File;

/**
 * author : feng
 * description ： 缓存过期策略
 * creation time : 18-7-26下午7:16
 */
public class ExpireCache implements CacheStrategy {

  private long expireTime;

  public ExpireCache(long expireTime) {
    this.expireTime = expireTime;
  }

  @Override
  public Observable<HttpResponse> fetchData(final String url) {
    Observable<HttpResponse> cacheObservable = Observable.create(new ObservableOnSubscribe<HttpResponse>() {
      @Override
      public void subscribe(ObservableEmitter<HttpResponse> e) throws Exception {
        File file = FileUtil.getCachedFile(url);
        if (FileUtil.isFileExists(file) && !FileUtil.isFileExpired(file, expireTime)) {
          Logger.e("fetch from cache");
          String json = FileIOUtil.readFile2String(file);
          HttpResponse httpResponse = new Gson().fromJson(json, HttpResponse.class);
          e.onNext(httpResponse);
        } else {
          e.onComplete();
        }
      }
    });
    Observable<HttpResponse> networkObservable = ApiHandler.getInstance().getCommonApi()
        .commonGet(url)
        .doOnNext(new Consumer<HttpResponse>() {
          @Override
          public void accept(HttpResponse httpResponse) throws Exception {
            Logger.e("fetch from network");
            File file = FileUtil.getCachedFile(url);
            String json = new Gson().toJson(httpResponse);
            FileIOUtil.writeFileFromString(file, json);
          }
        });
    return Observable.concat(cacheObservable, networkObservable);
  }
}
