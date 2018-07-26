package com.honghei.feng.utillib.http.cache;

import com.google.gson.Gson;
import com.honghei.feng.utillib.http.ApiHandler;
import com.honghei.feng.utillib.http.HttpResponse;
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
 * description ： 缓存策略
 * creation time : 18-7-26上午9:28
 */
public interface CacheStrategy {

  int NONE_CACHE = 0x00;
  int EXPIRE_CACHE = 0x01;
  int UPDATE_CACHE = 0x02;

  Observable<HttpResponse> fetchData(String url);

  class NoneCache implements CacheStrategy {

    @Override
    public Observable<HttpResponse> fetchData(String url) {
      return ApiHandler.getInstance().getCommonApi().commonGet(url);
    }
  }

  class ExpireCache implements CacheStrategy {

    private long expireTime;

    ExpireCache(long expireTime) {
      this.expireTime = expireTime;
    }

    @Override
    public Observable<HttpResponse> fetchData(final String url) {
      Observable<HttpResponse> cacheObservable = Observable.create(new ObservableOnSubscribe<HttpResponse>() {
        @Override
        public void subscribe(ObservableEmitter<HttpResponse> e) throws Exception {
          File file = FileUtil.getCachedFile(url);
          if (FileUtil.isFileExists(file) && !FileUtil.isFileExpired(file, expireTime)) {
            Logger.i("fetch from cache");
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
              Logger.i("fetch from network");
              File file = FileUtil.getCachedFile(url);
              String json = new Gson().toJson(httpResponse);
              FileIOUtil.writeFileFromString(file, json);
            }
          });
      return Observable.concat(cacheObservable, networkObservable);
    }
  }

  class UpdateCache implements CacheStrategy {

    @Override
    public Observable<HttpResponse> fetchData(final String url) {
      Observable<HttpResponse> cacheObservable = Observable.create(new ObservableOnSubscribe<HttpResponse>() {
        @Override
        public void subscribe(ObservableEmitter<HttpResponse> e) throws Exception {
          File file = FileUtil.getCachedFile(url);
          if (FileUtil.isFileExists(file)) {
            Logger.i("fetch from cache");
            String json = FileIOUtil.readFile2String(file);
            HttpResponse httpResponse = new Gson().fromJson(json, HttpResponse.class);
            e.onNext(httpResponse);
          }
          e.onComplete();
        }
      });
      Observable<HttpResponse> networkObservable = ApiHandler.getInstance().getCommonApi()
          .commonGet(url)
          .doOnNext(new Consumer<HttpResponse>() {
            @Override
            public void accept(HttpResponse httpResponse) throws Exception {
              Logger.i("fetch from network");
              File file = FileUtil.getCachedFile(url);
              String json = new Gson().toJson(httpResponse);
              FileIOUtil.writeFileFromString(file, json);
            }
          });
      return Observable.concat(cacheObservable, networkObservable);
    }
  }

}
