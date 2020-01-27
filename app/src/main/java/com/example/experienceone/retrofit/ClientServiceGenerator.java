package com.example.experienceone.retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ClientServiceGenerator {

    private static Retrofit retrofit = null;

    private static final String ROOT_URL = "https://dev.mobisprint.com/api/v1/";

    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    public static Retrofit getUrlClient() {

        httpClient.connectTimeout(120, TimeUnit.SECONDS).writeTimeout(120, TimeUnit.SECONDS).readTimeout(120, TimeUnit.SECONDS);
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        httpClient.addInterceptor(chain -> {
                Request request = chain.request().newBuilder()
                       /* .addHeader("Content-Type", "application/json")
                        .addHeader("organization-key", "86168e09b29b8d1b1bf4b5ea209fe2fcdaa21c90")
                        .addHeader("location", "demo-location")*/
                        .build();

            return chain.proceed(request);
        });
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        // add logging as last interceptor
        httpClient.addInterceptor(logging);  // <-- this is the important line!
        Gson gson = new GsonBuilder().create();
        //.addConverterFactory(new NullOnEmptyConverterFactory())
        retrofit = new Retrofit.Builder()
                .baseUrl(ROOT_URL)
                .addConverterFactory(new NullOnEmptyConverterFactory())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addConverterFactory(new ToStringConverterFactory())
                .client(httpClient.build())
                .build();
        return retrofit;
    }

    public static class NullOnEmptyConverterFactory extends Converter.Factory {

        @Override
        public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
            final Converter<ResponseBody, ?> delegate = retrofit.nextResponseBodyConverter(this, type, annotations);
            return (Converter<ResponseBody, Object>) body -> {
                // Utility.Log("VMA","Response  Body "+body.contentLength());
                if (body.contentLength() == 0) return null;
                return delegate.convert(body);
            };
        }
    }


}
