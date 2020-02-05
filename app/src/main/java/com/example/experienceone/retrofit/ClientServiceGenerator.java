package com.example.experienceone.retrofit;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;



public class ClientServiceGenerator {

/*    private static Retrofit retrofit = null;*/

    private static final String ROOT_URL = "https://dev.mobisprint.com/api/v1/";

    private static HttpLoggingInterceptor logging = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);

    private static OkHttpClient httpClient = new OkHttpClient.Builder()
            .addInterceptor(logging).connectTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS).build();;


    private static Retrofit builder =
            new Retrofit.Builder()
                    .baseUrl(ROOT_URL)
                    .addConverterFactory(new NullOnEmptyConverterFactory())
                    .addConverterFactory(GsonConverterFactory.create())
                    .addConverterFactory(new ToStringConverterFactory())
            .client(httpClient).build();

 //   private static Retrofit retrofit = builder.build();

    public static Retrofit getUrlClient() {
        return builder;
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
