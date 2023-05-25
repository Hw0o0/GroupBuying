package com.dnlab.groupbuying;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashSet;
import java.util.Set;

public class ProductListActivity extends AppCompatActivity {

    private LinearLayout productListLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        productListLayout = findViewById(R.id.product_list_layout);
        productListLayout.setOrientation(LinearLayout.VERTICAL);

        // SharedPreferences에서 상품 정보 가져오기
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        Set<String> productSet = preferences.getStringSet("products", new HashSet<>());

        for (String productInfo : productSet) {
            // 동적으로 LinearLayout 생성 및 설정
            LinearLayout productLinearLayout = createLinearLayout();

            // 동적으로 TextView 생성 및 설정
            TextView productTextView = createTextView(productInfo);

            // TextView를 LinearLayout에 추가
            productLinearLayout.addView(productTextView);


            // LinearLayout을 productListLayout에 추가
            productListLayout.addView(productLinearLayout);
        }
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
    // TextView 생성 및 설정을 처리하는 메서드
    private TextView createTextView(String text) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(dpToPx(5), dpToPx(5), dpToPx(5), dpToPx(5));

        TextView textView = new TextView(this);
        textView.setLayoutParams(layoutParams);

        textView.setText(text);
        // 텍스트 굵게 설정
        textView.setTypeface(null, Typeface.BOLD);
        // 배경색 지정
        textView.setBackgroundColor(Color.parseColor("#DAE5E3"));
        // 테두리 추가
        textView.setBackgroundResource(R.drawable.textview_border);
        // 가로 방향으로 정렬
        textView.setGravity(Gravity.HORIZONTAL_GRAVITY_MASK);
        // 필요한 속성 설정 가능

        // TextView 클릭 이벤트 처리
        textView.setOnClickListener(view -> showNotificationDialog());

        return textView;
    }

    // 알림 대화상자 표시
    private void showNotificationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
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
