package com.example.lmantamqrcode;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface JsonPlaceHolderAPI {
    @GET("posts")
    Call<List<Post>> getPosts(
            @Query("userId") Integer[] userId,
            @Query("_sort") String sort,
            @Query("_order") String order
    );

    @GET("posts")
    Call<List<Post>> getPosts(@QueryMap Map<String, String> params);


    @POST("lm")
    Call<Post> getData(@Body Post post);



    @Headers({"api-key: lmantam"})
    @POST("api/get/lm")
    Call<Post> postMessage(@QueryMap Map<String, String> params);

    @Headers({"api-key: lmantam"})
    @GET("api/get/lm")
    Call<Post> getMessage(@QueryMap Map<String, String> params);
}
