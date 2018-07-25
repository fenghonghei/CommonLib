package com.honghei.feng.utillib.http;

import com.honghei.feng.utillib.http.converter.HttpGsonConverterFactory;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * author : feng
 * description ： 统一管理api
 * creation time : 18-7-25上午9:42
 */
public class ApiHandler {

  private static volatile ApiHandler instance;
  private Retrofit retrofit;
  private CommonApi commonApi;

  private ApiHandler() {
    initDefaultRetrofit();
  }

  public static ApiHandler getInstance() {
    if (instance == null) {
      synchronized (ApiHandler.class) {
        if (instance == null) {
          instance = new ApiHandler();
        }
      }
    }
    return instance;
  }

  private void initDefaultRetrofit() {
    retrofit = new Retrofit.Builder()
        .baseUrl("http://fenghonghei.com/")
        .addConverterFactory(HttpGsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build();
  }

  public CommonApi getCommonApi() {
    if (commonApi == null) {
      commonApi = createApi(retrofit, CommonApi.class);
    }
    return commonApi;
  }

  private <T> T createApi(Retrofit retrofit, Class<T> tClass) {
    return retrofit.create(tClass);
  }
}
