package com.dnlab.groupbuying;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class HomeActivity extends AppCompatActivity {

    private Button btn1, btn2;  // 로그아웃 버튼 추가
    private ImageButton btnLogout;
    private TextView txtWelcomeNickname;

    private static final String TAG = "HomeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        txtWelcomeNickname = findViewById(R.id.txt_welcome_nickname);
        btn1 = findViewById(R.id.signUp_btn);
        btn2 = findViewById(R.id.listView_btn);
        btnLogout = findViewById(R.id.logout_btn);  // 로그아웃 버튼

        btn1.setOnClickListener(btnOnclick("등록하시겠습니까?", RegisterActivity.class));

        btn2.setOnClickListener(btnOnclick("상품 목록으로 이동하시겠습니까?", ProductListActivity.class));

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                builder.setIcon(R.drawable.logo);
                builder.setMessage("로그아웃하시겠습니까?")
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 로그인 화면으로 전환하는 코드
                                SharedPreferences preferences = getSharedPreferences("UserInfo", MODE_PRIVATE);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putBoolean("isLoggedIn", false);
                                editor.apply();

                                Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
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

        // SharedPreferences에서 유저 정보 가져오기
        SharedPreferences preferences = getSharedPreferences("UserInfo", MODE_PRIVATE);
        String nickname = preferences.getString("nickname", "");
        Log.d(TAG, "nickname in home: " + nickname);

        // 환영 메시지 설정
        txtWelcomeNickname.setText(nickname+"님 환영합니다.");
    }

    private View.OnClickListener btnOnclick(String dialogMessage, Class<?> activity) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                builder.setIcon(R.drawable.logo);
                builder.setMessage(dialogMessage)
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 다른 화면으로 전환하는 코드
                                Intent intent = new Intent(HomeActivity.this, activity);
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
        };
    }
}
