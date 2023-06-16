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
import android.widget.EditText;
import android.widget.Toast;

import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {
    private EditText edit_id, edit_pw;
    private Button btn_login, btn_signup;
    private SharedPreferences preferences;
    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edit_id = findViewById(R.id.edt_id);
        edit_pw = findViewById(R.id.edt_pw);
        btn_login = findViewById(R.id.btn_login);
        btn_signup = findViewById(R.id.btn_signup);

        preferences = getSharedPreferences("UserInfo", MODE_PRIVATE);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputId = edit_id.getText().toString();
                String inputPw = edit_pw.getText().toString();

                // SharedPreferences에서 저장된 사용자 정보 가져오기
                String savedUsers = preferences.getString("users", "");

                if (!savedUsers.isEmpty()) {
                    // 사용자 정보가 존재하는 경우
                    String[] users = savedUsers.split(",");
                    Log.d(TAG, "users in sharedPreference: " + Arrays.toString(users));

                    for (String user : users) {
                        // 사용자 정보 파싱
                        String[] userInfo;
                        String savedId;
                        String savedPw;
                        try {
                            userInfo = user.split(":");
                            savedId = userInfo[0];
                            savedPw = userInfo[1];
                        } catch (ArrayIndexOutOfBoundsException e) {
                            continue;
                        }

                        if (inputId.equals(savedId) && inputPw.equals(savedPw)) {
                            // 아이디와 비밀번호가 일치하는 사용자를 찾았을 때

                            // 닉네임 가져오기
                            String nickname = userInfo[2];

                            // 로그인 성공 처리
                            showLoginSuccessDialog();

                            // 로그인 상태와 닉네임 저장
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putBoolean("isLoggedIn", true);
                            editor.putString("nickname", nickname);
                            editor.apply();

                            return; // 로그인 성공 후 반복문 종료
                        }
                    }
                }

                // 사용자 정보가 없거나 아이디와 비밀번호가 일치하는 사용자를 찾지 못한 경우
                showLoginFailDialog();
            }
        });

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                builder.setIcon(R.drawable.logo);
                builder.setMessage("회원가입 하시겠습니까?")
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                AlertDialog.Builder signupBuilder = new AlertDialog.Builder(LoginActivity.this);
                                signupBuilder.setTitle("회원가입")
                                        .setView(R.layout.dialog_signup)
                                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                AlertDialog alertDialog = (AlertDialog) dialog;
                                                EditText editId = alertDialog.findViewById(R.id.edit_id);
                                                EditText editPw = alertDialog.findViewById(R.id.edit_pw);
                                                EditText editNickname = alertDialog.findViewById(R.id.edit_nickname);

                                                if (editId == null || editPw == null || editNickname == null) {
                                                    // The EditTexts do not exist, show an error message
                                                    showErrorMessage();
                                                    return;
                                                }

                                                String userId = editId.getText().toString();
                                                String userPw = editPw.getText().toString();
                                                String nickname = editNickname.getText().toString();

                                                // 기존 사용자 정보 가져오기
                                                String savedUsers = preferences.getString("users", "");

                                                // 새로운 사용자 정보 추가
                                                String newUser = userId + ":" + userPw + ":" + nickname;

                                                if (!savedUsers.isEmpty()) {
                                                    // 기존 사용자 정보가 있을 경우에는 쉼표로 구분하여 추가
                                                    savedUsers += ",";
                                                }
                                                savedUsers += newUser;

                                                // 사용자 정보 저장
                                                SharedPreferences.Editor editor = preferences.edit();
                                                editor.putString("users", savedUsers);
                                                editor.putString("userid", userId);
                                                editor.putString("userpw", userPw);
                                                editor.putString("nickname", nickname);
                                                editor.apply();

                                                // 회원가입 성공 알림을 표시
                                                showRegistrationSuccessDialog();

                                                // 이전 화면으로 돌아가기
                                                dialog.dismiss();
                                            }
                                        })
                                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.cancel();
                                            }
                                        });

                                AlertDialog signupDialog = signupBuilder.create();
                                signupDialog.show();
                            }
                        })
                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }

    private void showLoginSuccessDialog() {
        String nickname = preferences.getString("nickname", ""); // 저장된 닉네임 가져오기

        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setIcon(R.drawable.logo);
        builder.setTitle("로그인 성공")
                .setMessage("로그인에 성공하였습니다.")
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // HomeActivity로 이동하는 코드
                        dialog.dismiss();
                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                        startActivity(intent);
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showLoginFailDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setIcon(R.drawable.logo);
        builder.setTitle("로그인 실패")
                .setMessage("아이디 또는 비밀번호가 잘못되었습니다.")
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showRegistrationSuccessDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setIcon(R.drawable.logo);
        builder.setTitle("회원가입 성공")
                .setMessage("회원가입이 성공적으로 완료되었습니다. 로그인을 해주세요.")
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showErrorMessage() {
        Toast.makeText(this, "입력되지 않은 값이 있습니다.", Toast.LENGTH_SHORT).show();
    }
}