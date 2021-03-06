package com.thanhlh.cinema.repository.remote;

import com.thanhlh.cinema.commom.Constants;
import com.thanhlh.cinema.utils.GsonUtils;
import com.google.gson.Gson;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by thanhle on 4/24/17.
 */

public class ServiceFactory {
    public static <T> T createServiceFrom(final Class<T> serviceClass, String endpoint) {
        return createRetrofit(endpoint).create(serviceClass);
    }

    /**
     * Create OkHttpClient for setting of Interceptor for logging and connect timeout
     * @return OkHttpClient
     */
    private static OkHttpClient createOkHttp() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        if(Constants.DEBUG_MODE)
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .retryOnConnectionFailure(true)
                .connectTimeout(60, TimeUnit.SECONDS)
                .build();
    }

    public static Retrofit createRetrofit(String endpoint) {
        Gson gson = GsonUtils.createGson();
        return new Retrofit.Builder()
                .baseUrl(endpoint)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(createOkHttp())
                .build();
    }
}
