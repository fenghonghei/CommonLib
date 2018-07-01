package com.honghei.feng.cachelib.api;

import com.google.gson.JsonObject;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface ApiService {

    @GET
    Observable<JsonObject> commonGet(@Url String url);
}
