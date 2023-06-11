package com.dnlab.groupbuying.Retrofit;

import com.dnlab.groupbuying.Retrofit.chat.ChatLog;
import com.dnlab.groupbuying.Retrofit.chat.ChatLogs;
import com.dnlab.groupbuying.Retrofit.product.ProductImformation;
import com.dnlab.groupbuying.Retrofit.product.Products;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface RetrofitAPI {

    @GET("/productList")
    Call<Products> getProductData();

    @GET("/chatRoom")
    Call<ChatLogs> getChatLogData();

    @POST("/productList")
    Call<ProductImformation> postData(@Body ProductImformation productImformation);

    @POST("/chatRoom")
    Call<ChatLog> postData(@Body ChatLog chatLog);

}