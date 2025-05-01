package com.example.fightball.API;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class RetroFitBuilder {
    private static RetroFitBuilder instance=null;
    public String URL="http://localhost:8080/api";
    private Retrofit.Builder builder;
    private ApiInterface apiInterface;

    private RetroFitBuilder(){
    }
    public  static RetroFitBuilder getInstance(){
        if (instance==null){
            instance=new RetroFitBuilder();
        }
        return instance;
    }
    public void build(String url){

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();


        builder = new Retrofit.Builder();
        builder.baseUrl(url);
        builder.addConverterFactory(GsonConverterFactory.create(gson));
        Retrofit retrofit = builder.build();
        apiInterface=retrofit.create(ApiInterface.class);

    }

    public ApiCalls callApi(){
        return new ApiCalls(apiInterface);
    }
}
