package com.honghei.feng.cachelib;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.honghei.feng.cachelib.api.ApiHandler;

import java.io.File;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * 支持且仅支持get请求的缓存策略
 */
public class Fetcher {

    private Context context;

    public Fetcher(Context context) {
        this.context = context;
    }

    public <T> Observable<T> fetchData(String url, final Class<T> tClass, int cacheStrategy) {
        Observable<JsonObject> observable;
        if (cacheStrategy == CacheStrategy.NONE_CACHE) {
            observable = new NoneCache().fetchData(url);
        } else if (cacheStrategy == CacheStrategy.EXPIRE_CACHE) {
            throw new IllegalArgumentException(" expire cacheStrategy must have expire time");
        } else if (cacheStrategy == CacheStrategy.UPDATE_CACHE) {
            observable = new UpdateCache(context).fetchData(url);
        } else {
            throw new IllegalArgumentException(" expire cacheStrategy must be none update or expire cache");
        }
        return observable.map(new Function<JsonObject, T>() {
            @Override
            public T apply(JsonObject jsonObject) throws Exception {
                Gson gson = new Gson();
                return gson.fromJson(jsonObject, tClass);
            }
        });

    }

    public <T> Observable<T> fetchData(String url, final Class<T> tClass, long expireTime) {
        return new ExpireCache(context, expireTime).fetchData(url)
                .map(new Function<JsonObject, T>() {
                    @Override
                    public T apply(JsonObject jsonObject) throws Exception {
                        Gson gson = new Gson();
                        return gson.fromJson(jsonObject, tClass);
                    }
                });

    }


    /**
     * 缓存策略
     */
    public interface CacheStrategy {

        int NONE_CACHE = 0x00;
        int EXPIRE_CACHE = 0x01;
        int UPDATE_CACHE = 0x02;

        Observable<JsonObject> fetchData(String url);
    }

    private class NoneCache implements CacheStrategy {

        @Override
        public Observable<JsonObject> fetchData(String url) {
            return ApiHandler.getApiService().commonGet(url);
        }
    }

    private class ExpireCache implements CacheStrategy {

        private Context context;
        private long expireTime;

        ExpireCache(Context context, long expireTime) {
            this.context = context;
            this.expireTime = expireTime;
        }

        @Override
        public Observable<JsonObject> fetchData(final String url) {
            Observable<JsonObject> cacheObservable = Observable.create(new ObservableOnSubscribe<JsonObject>() {
                @Override
                public void subscribe(ObservableEmitter<JsonObject> e) throws Exception {
                    File file = FileUtil.getCachedFile(context, url);
                    if (file.exists() && !FileUtil.isFileExpired(file, expireTime)) {
                        String json = FileUtil.FileToString(file, "utf-8");
                        JsonObject jsonObject = new JsonParser().parse(json).getAsJsonObject();
                        e.onNext(jsonObject);
                    } else {
                        e.onComplete();
                    }
                }
            });
            Observable<JsonObject> networkObservable = ApiHandler.getApiService().commonGet(url)
                    .doOnNext(new Consumer<JsonObject>() {
                        @Override
                        public void accept(JsonObject jsonObject) throws Exception {
                            File file = FileUtil.getCachedFile(context, url);
                            String json = jsonObject.toString();
                            FileUtil.StringToFile(json, file, "utf-8");
                        }
                    });
            return Observable.concat(cacheObservable, networkObservable);
        }
    }

    private class UpdateCache implements CacheStrategy {

        private Context context;

        UpdateCache(Context context) {
            this.context = context;
        }

        @Override
        public Observable<JsonObject> fetchData(final String url) {
            Observable<JsonObject> cacheObservable = Observable.create(new ObservableOnSubscribe<JsonObject>() {
                @Override
                public void subscribe(ObservableEmitter<JsonObject> e) throws Exception {
                    File file = FileUtil.getCachedFile(context, url);
                    if (file.exists()) {
                        String json = FileUtil.FileToString(file, "utf-8");
                        JsonObject jsonObject = new JsonParser().parse(json).getAsJsonObject();
                        e.onNext(jsonObject);
                    }
                    e.onComplete();
                }
            });
            Observable<JsonObject> networkObservable = ApiHandler.getApiService().commonGet(url)
                    .doOnNext(new Consumer<JsonObject>() {
                        @Override
                        public void accept(JsonObject jsonObject) throws Exception {
                            File file = FileUtil.getCachedFile(context, url);
                            String json = jsonObject.toString();
                            FileUtil.StringToFile(json, file, "utf-8");
                        }
                    });
            return Observable.concat(cacheObservable, networkObservable);
        }
    }
}
