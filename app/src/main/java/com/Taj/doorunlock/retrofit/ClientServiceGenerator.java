package com.taj.doorunlock.retrofit;

import com.taj.doorunlock.helper.TLSSocketFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;

import okhttp3.CertificatePinner;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ClientServiceGenerator {

    /*    private static Retrofit retrofit = null;*/

      private static final String ROOT_URL = "https://izestkey.ihcltata.com/api/v1/";
    //private static final String ROOT_URL = "https://demo.mobisprint.com/ihcl/izestkey/api/v1/";

    private static CertificatePinner pinner=new CertificatePinner.Builder().add(
           "izestkey.ihcltata.com",
            "sha256/x4QzPSC810K5/cMjb05Qm4k3Bw5zBn4lTdO/nEW/Td4="
    ).build();

    private static HttpLoggingInterceptor logging = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);


    private static  TLSSocketFactory tlsSocketFactory;

    static {
        try {
            tlsSocketFactory = new TLSSocketFactory();
        } catch (KeyManagementException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }


    private static OkHttpClient httpClient = new OkHttpClient.Builder()
            .cache(null)
            .certificatePinner(pinner)
            .sslSocketFactory(tlsSocketFactory, tlsSocketFactory.systemDefaultTrustManager())
            .addInterceptor(logging)
            .connectTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS).build();


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
