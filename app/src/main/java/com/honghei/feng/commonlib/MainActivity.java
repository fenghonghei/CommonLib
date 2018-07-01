package com.honghei.feng.commonlib;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.honghei.feng.cachelib.Fetcher;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Fetcher fetcher = new Fetcher(getApplicationContext());
        String url = "http://wthrcdn.etouch.cn/weather_mini?citykey=101010100";
        Disposable disposable = fetcher.fetchData(url, Weather.class, Fetcher.CacheStrategy.UPDATE_CACHE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        Log.e(TAG, "doOnSubscribe");
                    }
                })
                .subscribe(new Consumer<Weather>() {
                    @Override
                    public void accept(Weather weather) throws Exception {
                        Log.e(TAG, "data: " + weather.toString());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e(TAG, "throwable: " + throwable.getMessage());
                    }
                });

    }
}
