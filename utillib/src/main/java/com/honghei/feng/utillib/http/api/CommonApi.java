package com.honghei.feng.utillib.http.api;

import io.reactivex.Observable;
import java.util.Map;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
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
  <T> Observable<HttpResponse> commonPost(@Url String url, @Body T t);

  @FormUrlEncoded
  @POST
  Observable<HttpResponse> commonPost(@Url String url, @FieldMap Map<String, String> field);
}
