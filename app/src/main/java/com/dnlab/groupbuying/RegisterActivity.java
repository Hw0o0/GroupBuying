package com.dnlab.groupbuying;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.dnlab.groupbuying.Retrofit.product.ProductImformation;
import com.dnlab.groupbuying.Retrofit.RetrofitClient;

public class RegisterActivity extends AppCompatActivity {

    private RetrofitClient retrofitClient = new RetrofitClient();
    private Button gps_btn, btn2;
    private EditText productAddress, productName, eventContent, event1, event2, personCount, productPrice,userLocate;
    final static int REQUEST_CODE = 1234;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        btn2 = findViewById(R.id.start_btn);
        gps_btn = findViewById(R.id.setgps_btn);
        userLocate = findViewById(R.id.user_locate);
        productAddress = findViewById(R.id.user_locate);
        productName = findViewById(R.id.productName);
        eventContent = findViewById(R.id.eventContent);
        event1 = findViewById(R.id.event1);
        event2 = findViewById(R.id.event2);
        personCount = findViewById(R.id.personCount);
        productPrice = findViewById(R.id.productPrice);



        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                builder.setIcon(R.drawable.logo);
                builder.setMessage("공구 시작 하시겠습니까?")
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //상품 정보
                                ProductImformation productImformation = new ProductImformation();

                                // 새로운 상품 정보 생성
                                productImformation.setProductAddress(productAddress.getText().toString());
                                productImformation.setProductName(productName.getText().toString());
                                productImformation.setEventContent(eventContent.getText().toString());
                                productImformation.setEvent1(event1.getText().toString());
                                productImformation.setEvent2(event2.getText().toString());
                                productImformation.setPersonCount(personCount.getText().toString());
                                productImformation.setProductPrice(productPrice.getText().toString());

                                //서버에 상품 정보 넘겨주는 코드
                                retrofitClient.serverProductSend(productImformation);


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
        gps_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                builder.setIcon(R.drawable.logo);
                builder.setMessage("GPS를 설정 하시겠습니까?")
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                String request_code = "MY_REQUEST_CODE";

                                // 다른 화면으로 전환하는 코드
                                Intent intent = new Intent(RegisterActivity.this, MapLocationSettingActivity.class);
                                startActivityForResult(intent, REQUEST_CODE);
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
    // 주소 edit에 intent해줌
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if(requestCode == REQUEST_CODE && resultCode == RESULT_OK){
            String roadAddress = intent.getStringExtra("road_data");
            userLocate.setText(roadAddress);
        }else{
            userLocate.setText("No Data");
        }

    }


}