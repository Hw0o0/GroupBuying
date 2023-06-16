package com.dnlab.groupbuying.Retrofit;

import android.util.Log;

import com.dnlab.groupbuying.Retrofit.chat.ChatLog;
import com.dnlab.groupbuying.Retrofit.chat.ChatLogs;
import com.dnlab.groupbuying.Retrofit.product.ProductImformation;
import com.dnlab.groupbuying.Retrofit.product.Products;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static final String TAG = "RetrofitClient";

    private Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://groupbuying.fly.dev/")
            .addConverterFactory(GsonConverterFactory.create()) // gson을 사용해 JSON을 변환할 것이다.
            .build();
    private RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);

    public RetrofitAPI getRetrofitAPI() {
        return retrofitAPI;
    }

    public void serverProductGet() { // Get 서버에서 상품 정보를 가져오는 메소드
        retrofitAPI.getProductData().enqueue(new Callback<Products>() {
            @Override
            public void onResponse(Call<Products> call, Response<Products> response) {
                List<ProductImformation> productImformations = response.body().getProductArray();

                if (productImformations != null && !productImformations.isEmpty()) {

                    for (ProductImformation productImformation : productImformations) {
                        Log.d(TAG, "성공");
                        Log.d(TAG, "상품 주소: " + productImformation.getProductAddress());
                        Log.d(TAG, "상품 이름: " + productImformation.getProductName());
                        Log.d(TAG, "행사 내용: " + productImformation.getEventContent());
                        Log.d(TAG, "" + productImformation.getEvent1());
                        Log.d(TAG, "" + productImformation.getEvent2());
                        Log.d(TAG, "인원 수: " + productImformation.getPersonCount());
                        Log.d(TAG, "상품 가격: " + productImformation.getProductPrice());
                    }
                } else {
                    Log.i(TAG, "상품 정보를 가져오는데 실패했습니다.");
                }
            }

            @Override
            public void onFailure(Call<Products> call, Throwable t) {
                Log.i(TAG, "네트워크 오류가 발생했습니다.");
            }
        });
    }


        public void serverProductSend(ProductImformation productImformation) { // Post 서버로 보내는것
        retrofitAPI.postData(productImformation).
                enqueue(new Callback<ProductImformation>() {
                    @Override
                    public void onResponse(Call<ProductImformation> call, Response<ProductImformation> response) {
                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                ProductImformation productImformation = response.body();
                                Log.i(TAG, "상품 주소:" + productImformation.getProductAddress());
                                Log.i(TAG, "상품 이름:" + productImformation.getProductName());
                                Log.i(TAG, "행사 내용:" + productImformation.getEventContent());
                                Log.i(TAG, "" + productImformation.getEvent1());
                                Log.i(TAG, "" + productImformation.getEvent2());
                                Log.i(TAG, "인원 수:" + productImformation.getPersonCount());
                                Log.i(TAG, "상품 가격:" + productImformation.getProductPrice());
                            } else {
                                Log.i(TAG, "상품 정보를 보내는데 실패했습니다.");
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ProductImformation> call, Throwable t) {
                        Log.i(TAG, "네트워크 오류가 발생했습니다.");
                    }
                });
    }
    public void serverChatGet() { //서버에서 채팅내용 가져오는 메소드
        retrofitAPI.getChatLogData().enqueue(new Callback<ChatLogs>() {
            @Override
            public void onResponse(Call<ChatLogs> call, Response<ChatLogs> response) {
                List<ChatLog>chatLogs = response.body().getChatArray();

                if (chatLogs != null && !chatLogs.isEmpty()) {

                    for (ChatLog chatLog : chatLogs) {
                        Log.d(TAG, "성공");
                        Log.d(TAG, "사용자: " + chatLog.getUser());
                        Log.d(TAG, "채팅 내용: " + chatLog.getChat());
                    }
                } else {
                    Log.i(TAG, "채팅 정보를 가져오는데 실패했습니다.");
                }
            }

            @Override
            public void onFailure(Call<ChatLogs> call, Throwable t) {
                Log.i(TAG, "네트워크 오류가 발생했습니다.");
            }
        });
    }
    public void serverChatSend(ChatLog chatLog) { // Post 서버로 보내는것

        retrofitAPI.postData(chatLog).
                enqueue(new Callback<ChatLog>() {
                    @Override
                    public void onResponse(Call<ChatLog> call, Response<ChatLog> response) {
                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                ChatLog chatLog = response.body();
                                Log.d(TAG, "성공");
                                Log.d(TAG, "사용자: " + chatLog.getUser());
                                Log.d(TAG, "채팅 내용: " + chatLog.getChat());
                            } else {
                                Log.i(TAG, "채팅 정보를 보내는데 실패했습니다.");
                            }
                        }
                    }
                    @Override
                    public void onFailure(Call<ChatLog> call, Throwable t) {
                        Log.i(TAG, "네트워크 오류가 발생했습니다.");
                    }
                });
    }
}