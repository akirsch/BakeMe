package com.example.android.bakeme.networking;

import android.content.Context;

import com.example.android.bakeme.Constants;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClientInstance {

    private static Retrofit mRetrofit = null;

    public static Retrofit getRetrofitInstance(Context context){
        // create a retrofit instance, only if one hasn't been created yet
        if (mRetrofit == null){

            //Create OkHttpClient.Builder object
            OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();

            HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);

            okHttpClientBuilder.addInterceptor(httpLoggingInterceptor);
            okHttpClientBuilder.connectTimeout(Constants.CONNECTION_TIMEOUT_IN_MILLIS, TimeUnit.MILLISECONDS);


            // create new Retrofit Object and attach to it the OkHttp client
            mRetrofit = new Retrofit.Builder()
                    .baseUrl(Constants.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClientBuilder.build())
                    .build();
        }

        return mRetrofit;
    }
}
