/*
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class JoinActivity extends AppCompatActivity {

    private Button chkNickName, chkId;
    private EditText input_NickName, input_Id, input_Pw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);
        input_NickName = findViewById(R.id.edit_nickname);
        input_Id = findViewById(R.id.edit_id);
        input_Pw = findViewById(R.id.edit_pw);
        chkNickName = findViewById(R.id.btn_chk_nickName);
        chkId = findViewById(R.id.btn_chk_id);

        chkNickName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nickName = input_NickName.getText().toString().trim();
                String id = input_Id.getText().toString().trim();
                String pw = input_Pw.getText().toString().trim();

                saveDataToSharedPreferences(nickName, id, pw); // SharedPreferences에 값 저장

                // 닉네임 중복 확인 및 처리 로직 추가
                String savedNickName = getSavedNickNameFromSharedPreferences();
                if (nickName.equals(savedNickName)) {
                    // 닉네임 중복
                    Toast.makeText(JoinActivity.this, "중복된 닉네임입니다.", Toast.LENGTH_SHORT).show();
                } else {
                    // 사용 가능한 닉네임
                    Toast.makeText(JoinActivity.this, "사용 가능한 닉네임입니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        chkId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 아이디 중복 확인 및 처리 로직 추가
            }
        });
    }

    // SharedPreferences에 값 저장하는 메서드
    private void saveDataToSharedPreferences(String nickName, String id, String pw) {
        // SharedPreferences에 값을 저장하는 로직을 구현해야 합니다.
        // 예를 들어, SharedPreferences에 "nickName", "id", "pw" 키로 값을 저장하는 코드는 아래와 같습니다.
        */
/* SharedPreferences sharedPreferences = getSharedPreferences("your_preferences_name", Context.MODE_PRIVATE);
         SharedPreferences.Editor editor = sharedPreferences.edit();
         editor.putString("nickName", nickName);*//*

        // editor.putString("id", id);
        // editor.putString("pw", pw);
        // editor.apply();
    }

    // SharedPreferences에서 저장된 닉네임 값을 가져오는 메서드 (임의의 메서드로 대체하여 사용해야 합니다)
    private String getSavedNickNameFromSharedPreferences() {
        // SharedPreferences에서 닉네임 값을 가져와서 반환하는 로직을 구현해야 합니다.
        // 예를 들어, SharedPreferences에서 "nickName" 키로 저장된 값을 가져오는 코드는 아래와 같습니다.
        // SharedPreferences sharedPreferences = getSharedPreferences("your_preferences_name", Context.MODE_PRIVATE);
        // return sharedPreferences.getString("nickName", "");
        return "";
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // JoinActivity를 종료하고 LoginActivity로 돌아가는 코드
        finish();
    }
}
*/
