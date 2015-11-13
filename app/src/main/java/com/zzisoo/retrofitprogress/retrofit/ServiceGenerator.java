package com.zzisoo.retrofitprogress.retrofit;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 * Created by yangjisoo on 15. 11. 13..
 */
public class ServiceGenerator {

    public static final String API_BASE_URL = "http://10.100.0.134:8000";



    private static OkHttpClient httpClient = new OkHttpClient();
    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(API_BASE_URL);

    public static <S> S createService(Class<S> serviceClass) {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.HEADERS);
        httpClient.interceptors().add(logging);


        Retrofit retrofit = builder.client(httpClient).build();
        return retrofit.create(serviceClass);
    }
}