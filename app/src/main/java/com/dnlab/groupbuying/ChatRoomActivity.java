package com.dnlab.groupbuying;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dnlab.groupbuying.Retrofit.chat.ChatLog;
import com.dnlab.groupbuying.Retrofit.chat.ChatLogs;
import com.dnlab.groupbuying.Retrofit.RetrofitAPI;
import com.dnlab.groupbuying.Retrofit.RetrofitClient;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatRoomActivity extends AppCompatActivity {

    private LinearLayout chatLogLayout;
    private RetrofitClient retrofitClient = new RetrofitClient();
    private EditText inputChat;
    private static final String TAG = "ChatRoomActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        chatLogLayout = findViewById(R.id.chat_log_layout);
        chatLogLayout.setOrientation(LinearLayout.VERTICAL);
        inputChat = findViewById(R.id.input_chat);
        Button sendButton = findViewById(R.id.button);

        RetrofitAPI retrofitAPI = retrofitClient.getRetrofitAPI();
        retrofitAPI.getChatLogData().enqueue(new Callback<ChatLogs>() {

            @Override
            public void onResponse(Call<ChatLogs> call, Response<ChatLogs> response) {
                ChatLogs chatLogs = response.body();
                SharedPreferences preferences = getSharedPreferences("UserInfo", MODE_PRIVATE);
                String nickname = preferences.getString("nickname", "");
                if (chatLogs != null) {
                    List<ChatLog> logs = chatLogs.getChatArray();
                    if (logs != null && !logs.isEmpty()) {
                        for (ChatLog chatLog : logs) {
                            addChatLogToLayout(chatLog);
                        }
                    } else {
                        Log.i(TAG, "채팅 로그가 비어 있습니다.");
                    }
                } else {
                    Log.i(TAG, "채팅 로그를 가져오는데 실패했습니다.");
                }
            }

            @Override
            public void onFailure(Call<ChatLogs> call, Throwable t) {
                Log.i(TAG, "네트워크 오류가 발생했습니다.");
            }
        });

        // 전송 버튼 클릭 시 서버에 채팅 데이터 전송
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChatLog chatLog = new ChatLog();
                String chat = inputChat.getText().toString().trim();
                if (!TextUtils.isEmpty(chat)) {
                    SharedPreferences preferences = getSharedPreferences("UserInfo", MODE_PRIVATE);
                    String nickname = preferences.getString("nickname", "");
                    chatLog.setUser(nickname);
                    chatLog.setChat(chat);
                    serverChatSend(chatLog);
                }
            }
        });
    }

    // 서버에 채팅 데이터 전송
    public void serverChatSend(ChatLog chatLog) { // Post 서버로 보내는것
        RetrofitAPI retrofitAPI = retrofitClient.getRetrofitAPI();
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
                                addChatLogToLayout(chatLog); // 새로운 채팅 로그를 화면에 추가
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

    // LinearLayout에 채팅 로그 추가
    private void addChatLogToLayout(ChatLog chatLog) {
        LinearLayout chatLinearLayout = createLinearLayout();
        TextView chatTextView = createTextView(chatLog);
        chatLinearLayout.addView(chatTextView);
        chatLogLayout.addView(chatLinearLayout);
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
    private TextView createTextView(ChatLog chatLog) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(dpToPx(5), dpToPx(5), dpToPx(5), dpToPx(5));

        TextView textView = new TextView(this);
        textView.setLayoutParams(layoutParams);

        textView.setText(
                "사용자: " + chatLog.getUser() + "\n" +
                "채팅 시간: " + getCurrentTime() + "\n" +
                "채팅 내용: " + chatLog.getChat());
        // 텍스트 굵게 설정
        textView.setTypeface(null, Typeface.BOLD);
        // 텍스트 크기 설정
        textView.setTextSize(0,dpToPx(12));
        // 배경색 지정
        textView.setBackgroundColor(Color.parseColor("#DAE5E3"));
        // 테두리 추가
        textView.setBackgroundResource(R.drawable.textview_border);
        // 가로 방향으로 정렬
        textView.setGravity(Gravity.HORIZONTAL_GRAVITY_MASK);
        //padding 설정
        int padding = dpToPx(10);
        textView.setPadding(padding, padding, padding, padding);

        return textView;
    }

    // dp 값을 px로 변환하는 메서드
    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }
    // 현재 시간을 가져오는 메소드
    private String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("MM월 dd일 HH시 mm분", Locale.getDefault());
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));//대한민국 시간대 설정
        String currentTime = sdf.format(new Date());
        return currentTime;
    }
}
