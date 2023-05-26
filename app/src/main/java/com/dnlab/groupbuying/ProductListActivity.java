package com.dnlab.groupbuying;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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

            // 동적으로 ImageView 생성 및 설정
            ImageView productImageView = createImageView(productInfo);

            // TextView와 ImageView를 LinearLayout에 추가
            productLinearLayout.addView(productTextView);
            productLinearLayout.addView(productImageView);

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

    // ImageView 생성 및 설정을 처리하는 메서드
    private ImageView createImageView(String productInfo) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                dpToPx(80), dpToPx(80));
        layoutParams.setMargins(dpToPx(5), dpToPx(5), dpToPx(5), dpToPx(5));

        ImageView imageView = new ImageView(this);
        imageView.setLayoutParams(layoutParams);

        // 이미지 파일 경로 가져오기
        String imagePath = getProductImagePath(productInfo);

        if (imagePath != null) {
            // 이미지 파일 로드 및 설정
            Bitmap bitmap = loadImageFromFile(imagePath);
            if (bitmap != null) {
                imageView.setImageBitmap(bitmap);
            }
        }

        return imageView;
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

    // 상품 이미지 파일 경로 가져오기
    private String getProductImagePath(String productInfo) {
        String[] lines = productInfo.split("\n");
        for (String line : lines) {
            if (line.startsWith("    사진 경로 :")) {
                return line.substring(14); // "    사진 경로 :"를 제외한 경로 부분 반환
            }
        }
        return null;
    }

    // 파일로부터 이미지 로드
    private Bitmap loadImageFromFile(String imagePath) {
        try {
            File imageFile = new File(imagePath);
            FileInputStream inputStream = new FileInputStream(imageFile);
            return BitmapFactory.decodeStream(inputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
