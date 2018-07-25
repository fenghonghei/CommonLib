package com.honghei.feng.utillib.http;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;

/**
 * author : feng
 * description ： 通用的api请求service
 * creation time : 18-7-25上午10:00
 */
public interface CommonApi {

  @GET
  Observable<HttpResponse> commonGet(@Url String url);

  @POST
  Observable<HttpResponse> commonPost(@Url String url, @Body RequestBody requestBody);
}
