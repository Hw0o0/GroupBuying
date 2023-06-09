package com.dnlab.groupbuying.Retrofit;

import com.dnlab.groupbuying.Retrofit.chat.ChatLog;
import com.dnlab.groupbuying.Retrofit.chat.ChatLogs;
import com.dnlab.groupbuying.Retrofit.product.ProductImformation;
import com.dnlab.groupbuying.Retrofit.product.Products;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

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
