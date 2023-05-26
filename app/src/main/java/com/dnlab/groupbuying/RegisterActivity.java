package com.dnlab.groupbuying;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class RegisterActivity extends AppCompatActivity {

    private Button gps_btn, btn2;
    private EditText editProductName, inputPersonCount, productCount1, productCount2, eventContext1, inputProductCount2, inputPrice;

    /*카메라 관련 코드 추가*/
        final private static String TAG = "ProductListActivity";
    private Button btn_photo;
    private ImageView iv_photo;
    final static int TAKE_PICTURE = 1;

    String mCurrentPhotoPath;
    static final int REQUEST_TAKE_PHOTO = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // SharedPreferences 객체 가져오기
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        // 촬영한 사진을 파일로 저장
        String photoPath = mCurrentPhotoPath;
        // 새로운 상품 정보 생성
        btn2 = findViewById(R.id.start_btn);
        gps_btn = findViewById(R.id.setgps_btn);
        editProductName = findViewById(R.id.editPruductName);
        productCount1 = findViewById(R.id.productCount1);
        productCount2 = findViewById(R.id.inputPersonCount);
        eventContext1 = findViewById(R.id.inputProductCount2);
        inputPersonCount = findViewById(R.id.inputPersonCount);
        inputProductCount2 = findViewById(R.id.inputProductCount2);
        inputPrice = findViewById(R.id.editTextTextPersonName6);

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
                                         /*    @Override
                                         public void onClick(View v) {
                                             switch (v.getId()) {
                                                 case R.id.picture:
                                                     Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                                                     startActivityForResult(cameraIntent, TAKE_PICTURE);
                                                     break;
                                             }
                                         }
                                     });*/
                                         @Override
                                         public void onClick(View v) {
                                             switch (v.getId()) {
                                                 case R.id.picture:
                                                     // 카메라 앱을 여는 소스
                                                     dispatchTakePictureIntent();
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
                                // 기존 상품 정보 가져오기
                                Set<String> existingProducts = preferences.getStringSet("products", new HashSet<String>());

                                // 새로운 상품 정보 생성
                                String productName = editProductName.getText().toString();
                                String eventContent1 = productCount1.getText().toString();
                                String eventContent2 = productCount2.getText().toString();
                                String eventContext = eventContext1.getText().toString();
                                String personCount = inputPersonCount.getText().toString();
                                String productCount = inputProductCount2.getText().toString();
                                String price = inputPrice.getText().toString();

                                String newProduct = "\n    상품 주소 : 반여동 \n" + "    상품 이름 : " + productName + " " + "이벤트 내용 : " + eventContent1 + " + " + eventContent2 + " " + eventContext + "\n    인원 수 : " + personCount + " " + "상품수량 : " + productCount + " " + "가격 : " + price + "\n";

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
        gps_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                builder.setMessage("GPS를 설정 하시겠습니까?")
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                // 다른 화면으로 전환하는 코드
                                Intent intent = new Intent(RegisterActivity.this, MapLocationSettingActivity.class);
                                startActivity(intent);
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
    //카메라로 촬형한 영상을 가져오는 부분
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        try {
            switch (requestCode) {
                case REQUEST_TAKE_PHOTO: {
                    if (resultCode == RESULT_OK) {
                        File file = new File(mCurrentPhotoPath);
                        Bitmap bitmap = MediaStore.Images.Media
                                .getBitmap(getContentResolver(), Uri.fromFile(file));
                        if (bitmap != null) {
                            iv_photo.setImageBitmap(bitmap);
                        }
                    }
                    break;
                }
            }

        } catch (Exception error) {
            error.printStackTrace();
        }
    }

    // ImageFile의 경로를 가져올 메서드 선언
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.dnlab.groupbuying.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }
}
