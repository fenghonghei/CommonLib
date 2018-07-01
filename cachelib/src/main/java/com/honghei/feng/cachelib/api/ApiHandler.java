package com.honghei.feng.cachelib.api;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiHandler {

    private static volatile ApiService apiService;

    public static ApiService getApiService() {
        if (apiService == null) {
            synchronized (ApiHandler.class) {
                if (apiService == null) {
                    apiService = creteApiService();
                }
            }
        }
        return apiService;
    }

    private static ApiService creteApiService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        return retrofit.create(ApiService.class);
    }
}
