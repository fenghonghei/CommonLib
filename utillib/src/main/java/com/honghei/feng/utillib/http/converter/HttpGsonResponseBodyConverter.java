package com.honghei.feng.utillib.http.converter;

import static okhttp3.internal.Util.UTF_8;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.honghei.feng.utillib.http.api.ApiException;
import com.honghei.feng.utillib.http.api.HttpResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * author : feng
 * description ：ResponseConverter
 * creation time : 18-7-25下午5:13
 */
public class HttpGsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {

  private final Gson gson;
  private final TypeAdapter<T> adapter;

  HttpGsonResponseBodyConverter(Gson gson, TypeAdapter<T> adapter) {
    this.gson = gson;
    this.adapter = adapter;
  }

  @Override
  public T convert(ResponseBody value) throws IOException {
    String response = value.string();
    HttpResponse httpResponse = gson.fromJson(response, HttpResponse.class);
    if (!httpResponse.isValidResponse()) {
      value.close();
      throw new ApiException(httpResponse.getCode(), httpResponse.getMessage());
    }

    MediaType contentType = value.contentType();
    Charset charset = contentType != null ? contentType.charset(UTF_8) : UTF_8;
    InputStream inputStream = new ByteArrayInputStream(response.getBytes());
    Reader reader = new InputStreamReader(inputStream, charset);
    JsonReader jsonReader = gson.newJsonReader(reader);
    try {
      return adapter.read(jsonReader);
    } finally {
      value.close();
    }
  }
}
