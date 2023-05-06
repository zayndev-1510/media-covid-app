package com.it015.mediacovidapp.apiconfig.admin;

import android.content.Context;
import android.content.res.Resources;
import android.widget.Toast;

import com.it015.mediacovidapp.R;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
public class NetworkClient {
    public static Retrofit retrofit;
    public static Resources resources;


    public static Retrofit getRetrofit(String url){

        OkHttpClient okHttpClient=new OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60,TimeUnit.SECONDS)
                .build();
        if(retrofit==null){

            retrofit=new Retrofit.Builder().baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient).build();
        }

        return retrofit;
    }
}
