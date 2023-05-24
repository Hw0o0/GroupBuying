package com.dnlab.groupbuying;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashSet;
import java.util.Set;

public class RegisterActivity extends AppCompatActivity {

    private Button btn2;
    private EditText editProductName, inputPersonCount,productCount1,productCount2,eventContext1, inputProductCount2, editTextTextPersonName6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // SharedPreferences 객체 가져오기
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        btn2 = findViewById(R.id.start_btn);
        editProductName = findViewById(R.id.editPruductName);
        productCount1 = findViewById(R.id.productCount1);
        productCount2 = findViewById(R.id.inputPersonCount);
        eventContext1 = findViewById(R.id.inputProductCount2);
        inputPersonCount = findViewById(R.id.inputPersonCount);
        inputProductCount2 = findViewById(R.id.inputProductCount2);
        editTextTextPersonName6 = findViewById(R.id.editTextTextPersonName6);

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                builder.setMessage("공구 시작 하시겠습니까?")
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 기존 상품 정보 가져오기
                                Set<String> existingProducts = preferences.getStringSet("products", new HashSet<String>());

                                // 새로운 상품 정보 생성
                                String productName = editProductName.getText().toString();
                                String eventContent1 = productCount1.getText().toString();
                                String eventContent2 = productCount2.getText().toString();
                                String eventContext = eventContext1.getText().toString();
                                String personCount = inputPersonCount.getText().toString();
                                String productCount = inputProductCount2.getText().toString();
                                String price = editTextTextPersonName6.getText().toString();
                                String newProduct = "\n    상품 주소 : 반여동 \n"+ "    상품 이름 : " + productName + " " +"이벤트 내용 : "+eventContent1 +" + "+ eventContent2 + " " + eventContext +"\n    인원 수 : " + personCount + " " + "상품수량 : " + productCount + " " + "가격 : " + price +"\n";

                                // 기존 상품 정보에 새로운 상품 정보 추가
                                existingProducts.add(newProduct);

                                // SharedPreferences에 상품 정보 저장
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putStringSet("products", existingProducts);
                                editor.apply();

                                // 홈 화면으로 전환하는 코드
                                Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // 기존의 모든 액티비티를 제거
                                startActivity(intent);
                                finish(); // 현재 액티비티를 종료
                                Toast.makeText(RegisterActivity.this, "상품 등록이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 취소 버튼 클릭 시 수행할 코드
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }
}
