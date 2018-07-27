package com.honghei.feng.commonlib.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import com.honghei.feng.commonlib.R;
import com.honghei.feng.utillib.http.Fetcher;
import com.honghei.feng.utillib.http.HttpHandleProxy;
import com.honghei.feng.utillib.http.IHttpHandle;
import com.honghei.feng.utillib.http.api.ApiException;
import com.honghei.feng.utillib.http.api.HttpResponse;
import com.honghei.feng.utillib.http.cache.CacheStrategy;
import com.honghei.feng.utillib.util.Logger;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;

/**
 * author : feng
 * description ： http测试
 * creation time : 18-7-27下午6:10
 */
public class HttpActivity extends AppCompatActivity implements IHttpHandle {

  private HttpHandleProxy httpHandleProxy;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_http);
    httpHandleProxy = new HttpHandleProxy(this);
  }

  public void getWithNoneCache(View view) {
    String url = "http://www.wanandroid.com/friend/json";
    Disposable noneCacheDisposable = Fetcher.fetchData(url)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnSubscribe(new Consumer<Disposable>() {
          @Override
          public void accept(Disposable disposable) throws Exception {
            onStartRequest();
          }
        })
        .subscribe(new Consumer<HttpResponse>() {
          @Override
          public void accept(HttpResponse httpResponse) throws Exception {
            Logger.e(httpResponse.getData().toString());
            onCompleteRequest();
          }
        }, new Consumer<Throwable>() {
          @Override
          public void accept(Throwable throwable) throws Exception {
            httpHandleProxy.handler(throwable);
          }
        });
  }

  public void getWithCacheExpire(View view) {
    String url = "http://www.wanandroid.com/banner/json";
    Disposable cacheExpireDisposable = Fetcher.fetchData(url, CacheStrategy.EXPIRE_CACHE, 1000 * 60)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnSubscribe(new Consumer<Disposable>() {
          @Override
          public void accept(Disposable disposable) throws Exception {
            onStartRequest();
          }
        })
        .subscribe(new Consumer<HttpResponse>() {
          @Override
          public void accept(HttpResponse httpResponse) throws Exception {
            onCompleteRequest();
            Logger.e(httpResponse.getData().toString());
          }
        }, new Consumer<Throwable>() {
          @Override
          public void accept(Throwable throwable) throws Exception {
            httpHandleProxy.handler(throwable);
          }
        });
  }

  public void getWithCacheUpdate(View view) {
    String url = "http://www.wanandroid.com//hotkey/json";
    Disposable updateCacheDisposable = Fetcher.fetchData(url, CacheStrategy.UPDATE_CACHE)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnSubscribe(new Consumer<Disposable>() {
          @Override
          public void accept(Disposable disposable) throws Exception {
            onStartRequest();
          }
        })
        .subscribe(new Consumer<HttpResponse>() {
          @Override
          public void accept(HttpResponse httpResponse) throws Exception {
            onCompleteRequest();
            Logger.e(httpResponse.getData().toString());
          }
        }, new Consumer<Throwable>() {
          @Override
          public void accept(Throwable throwable) throws Exception {
            httpHandleProxy.handler(throwable);
          }
        });
  }


  @Override
  public void onStartRequest() {
    Logger.e("onStartRequest");
  }

  @Override
  public void onCompleteRequest() {
    Logger.e("onCompleteRequest");
  }

  @Override
  public void onIOException() {
    Logger.e("onIOException");
  }

  @Override
  public void onHttpException(HttpException e) {
    Logger.e("onHttpException: " + e.code() + "--" + e.message());
  }

  @Override
  public void onApiException(ApiException e) {
    Logger.e("onApiException: " + e.getErrorCode() + "--" + e.getMessage());
  }
}
