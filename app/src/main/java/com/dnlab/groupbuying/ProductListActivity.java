package com.dnlab.groupbuying;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dnlab.groupbuying.Retrofit.product.ProductImformation;
import com.dnlab.groupbuying.Retrofit.product.Products;
import com.dnlab.groupbuying.Retrofit.RetrofitAPI;
import com.dnlab.groupbuying.Retrofit.RetrofitClient;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductListActivity extends AppCompatActivity {

    private LinearLayout productListLayout;
    private RetrofitClient retrofitClient = new RetrofitClient();
    private static final String TAG = "ProductListActivity";
    private Set<String> displayedProducts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        productListLayout = findViewById(R.id.product_list_layout);
        productListLayout.setOrientation(LinearLayout.VERTICAL);
        displayedProducts = new HashSet<>();

        RetrofitAPI retrofitAPI = retrofitClient.getRetrofitAPI();
        retrofitAPI.getProductData().enqueue(new Callback<Products>() {

            // 서버에서 상품 정보 가져오기
            @Override
            public void onResponse(Call<Products> call, Response<Products> response) {
                List<ProductImformation> productImformations = response.body().getProductArray();

                if (productImformations != null && !productImformations.isEmpty()) {
                    for (ProductImformation productImformation : productImformations) {
                        // 이미 표시된 상품인지 확인
                        if (!displayedProducts.contains(productImformation.getProductName())) {
                            Log.d(TAG, "성공");
                            Log.d(TAG, "상품 주소: " + productImformation.getProductAddress());
                            Log.d(TAG, "상품 이름: " + productImformation.getProductName());
                            Log.d(TAG, "행사 내용: " + productImformation.getEventContent());
                            Log.d(TAG, "" + productImformation.getEvent1());
                            Log.d(TAG, "" + productImformation.getEvent2());
                            Log.d(TAG, "인원 수: " + productImformation.getPersonCount());
                            Log.d(TAG, "상품 가격: " + productImformation.getProductPrice());

                            // 동적으로 LinearLayout 생성 및 설정
                            LinearLayout productLinearLayout = createLinearLayout();

                            // 동적으로 TextView 생성 및 설정
                            TextView productTextView = createTextView(productImformation);

                            // TextView를 LinearLayout에 추가
                            productLinearLayout.addView(productTextView);

                            // LinearLayout을 productListLayout에 추가
                            productListLayout.addView(productLinearLayout);

                            // 표시된 상품 목록에 추가
                            displayedProducts.add(productImformation.getProductName());
                        }
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

    // LinearLayout 생성 및 설정을 처리하는 메서드
    private LinearLayout createLinearLayout() {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, dpToPx(5), 0, dpToPx(5));
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setLayoutParams(layoutParams);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        // 필요한 속성 설정 가능

        return linearLayout;
    }

    // TextView 생성 및 설정을 처리하는 메서드
    private TextView createTextView(ProductImformation productImformation) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, dpToPx(5), 0, dpToPx(5));

        TextView textView = new TextView(this);
        textView.setLayoutParams(layoutParams);
        textView.setText("상품 주소: " + productImformation.getProductAddress() + "\n" +
                "상품 이름: " + productImformation.getProductName() + " " +
                "행사 내용: " + productImformation.getEventContent() + " " +
                productImformation.getEvent1() +" + " + productImformation.getEvent2() +"\n" +
                "인원 수: " + productImformation.getPersonCount() + " " +
                "상품 가격: " + productImformation.getProductPrice());
        // 텍스트 굵게 설정
        textView.setTypeface(null, Typeface.BOLD);
        // 텍스트 크기 설정
        textView.setTextSize(0,dpToPx(15));
        // 배경색 지정
        textView.setBackgroundColor(Color.parseColor("#DAE5E3"));
        // 테두리 추가
        textView.setBackgroundResource(R.drawable.textview_border);
        // 가로 방향으로 정렬
        textView.setGravity(Gravity.HORIZONTAL_GRAVITY_MASK);
        // padding 설정
        int padding = dpToPx(20);
        textView.setPadding(padding, padding, padding, padding);
        // 필요한 속성 설정 가능

        // TextView 클릭 이벤트 처리
        textView.setOnClickListener(view -> showNotificationDialog());

        return textView;
    }
    // 알림 대화상자 표시
    private void showNotificationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.logo);
        builder.setMessage("참석하시겠습니까?")
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // HomeActivity로 이동하는 코드
                        Intent intent = new Intent(ProductListActivity.this, ChatRoomActivity.class);
                        startActivity(intent);
                        dialog.dismiss();
                        finish();
                    }
                }).setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 취소 버튼 클릭 시 수행할 코드
                dialog.cancel();
            }
        });
        // 대화상자 생성 및 표시
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    // dp 값을 px로 변환하는 메서드
    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }
}
