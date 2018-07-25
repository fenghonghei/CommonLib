package com.honghei.feng.utillib.http.converter;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * author : feng
 * description ： ConverterFactory
 * creation time : 18-7-25下午4:58
 */
public class HttpGsonConverterFactory extends Converter.Factory {

  public static HttpGsonConverterFactory create() {
    return create(new Gson());
  }


  public static HttpGsonConverterFactory create(Gson gson) {
    if (gson == null) {
      throw new NullPointerException("gson == null");
    }
    return new HttpGsonConverterFactory(gson);
  }

  private final Gson gson;

  private HttpGsonConverterFactory(Gson gson) {
    this.gson = gson;
  }

  @Override
  public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations,
      Retrofit retrofit) {
    TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(type));
    return new HttpGsonResponseBodyConverter<>(gson, adapter);
  }

  @Override
  public Converter<?, RequestBody> requestBodyConverter(Type type,
      Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
    TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(type));
    return new HttpGsonRequestBodyConverter<>(gson, adapter);
  }
}
