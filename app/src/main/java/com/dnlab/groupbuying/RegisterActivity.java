package com.dnlab.groupbuying;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.dnlab.groupbuying.Retrofit.product.ProductImformation;
import com.dnlab.groupbuying.Retrofit.RetrofitClient;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RegisterActivity extends AppCompatActivity {

    private RetrofitClient retrofitClient = new RetrofitClient();
    private Button gps_btn, btn2;
    private EditText productAddress, productName, eventContent, event1, event2, personCount, productPrice,userLocate;
    /*카메라 관련 코드 추가*/
    final private static String TAG = "태그명";
    private Button btn_photo;
    private ImageView iv_photo;
    final static int TAKE_PICTURE = 1;



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

        /*카메라 관련 코드*/
        btn_photo = findViewById(R.id.picture);
        iv_photo = findViewById(R.id.inputPicture);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "권한 설정 완료");
            }
            else {
                Log.d(TAG, "권한 설정 요청");
                ActivityCompat.requestPermissions(RegisterActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }
        btn_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.picture:
                        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(cameraIntent, TAKE_PICTURE);
                        break;
                }
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
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
                builder.setMessage("GPS를 설정 하시겠습니까?")
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                String request_code = "MY_REQUEST_CODE";

                                // 다른 화면으로 전환하는 코드
                                Intent intent = new Intent(RegisterActivity.this, MapLocationSettingActivity.class);
                                startActivityForResult(intent, 1234);
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
    /*카메라 실행을 위한 코드*/
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d(TAG, "onRequestPermissionsResult");
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED ) {
            Log.d(TAG, "Permission: " + permissions[0] + "was " + grantResults[0]);
        }
    }


    // 카메라로 촬영한 사진의 썸네일을 가져와 이미지뷰에 띄워줌
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        switch (requestCode) {
            case TAKE_PICTURE:
                if (resultCode == RESULT_OK && intent.hasExtra("data")) {
                    Bitmap bitmap = (Bitmap) intent.getExtras().get("data");
                    if (bitmap != null) {
                        iv_photo.setImageBitmap(bitmap);
                    }

                }
                break;
        }
        if(requestCode == 1234 && resultCode == RESULT_OK){
            String roadAddress = intent.getStringExtra("road_data");
            userLocate.setText(roadAddress);
        }else{
            userLocate.setText("No Data");
        }

    }
    // ImageFile의 경로를 가져올 메서드 선언
    private File createImageFile() throws IOException {
        // 파일이름을 세팅 및 저장경로 세팅
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_"; File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File StorageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
        return image;
    }
}