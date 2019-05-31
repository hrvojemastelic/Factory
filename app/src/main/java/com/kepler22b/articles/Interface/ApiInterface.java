package com.kepler22b.articles.Interface;

import com.kepler22b.articles.model.ResponseModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("articles")
    Call<ResponseModel> getLatestNews(@Query("source") String source, @Query("apikey") String apiKey);
}
