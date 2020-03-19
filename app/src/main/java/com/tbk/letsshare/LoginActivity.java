package com.tbk.letsshare;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.tbk.letsshare.Comm_Data.LoginData;
import com.tbk.letsshare.Comm_Data.LoginResponse;
import com.tbk.letsshare.network.RetrofitClient;
import com.tbk.letsshare.network.ServiceApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    public static final String KEY_VALUE = "Logged in";

    private EditText usernameField;
    private EditText passwordField;
    private Button signIn;
    private Button signUp;
    private ProgressBar loginProgress;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private String isAutoLoginChecked = "false";
    private ServiceApi service;
    private CheckBox autoLoginCheckBox;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameField = (EditText) findViewById(R.id.username);
        passwordField = (EditText) findViewById(R.id.password);
        autoLoginCheckBox = findViewById(R.id.autologin);
        signIn = (Button) findViewById(R.id.login);
        signUp = (Button) findViewById(R.id.register);
        loginProgress = (ProgressBar) findViewById(R.id.login_progress);

        service = RetrofitClient.getClient().create(ServiceApi.class);

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptLogin();
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), JoinActivity.class);
                startActivity(intent);
            }
        });
        autoLoginCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());

                    builder.setMessage("개인 휴대폰이 아닐 시 위험할 수 있습니다. 계속하시겠습니까?")
                            .setPositiveButton("네", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    isAutoLoginChecked = "true";
                                }
                            })
                            .setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    isAutoLoginChecked = "false";
                                    autoLoginCheckBox.setChecked(false);
                                }
                            });
                    AlertDialog dialog = builder.create();
                    dialog.show();

            }

        });

    }

    private void attemptLogin() {
        usernameField.setError(null);
        passwordField.setError(null);

        String email = usernameField.getText().toString();
        String password = passwordField.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (password.isEmpty()) {
            passwordField.setError("비밀번호를 입력해주세요");
            focusView = usernameField;
            cancel = true;
        } else if (!isPasswordValid(password)) {
            passwordField.setError("4자 이상의 비밀번호를 입력해주세요");
            focusView = passwordField;
            cancel = true;
        }

        if(email.isEmpty()){
            usernameField.setError("이메일을 입력해주세요");
            focusView = usernameField;
            cancel = true;
        } else if(!isEmailValid(email)){
            usernameField.setError("@를 포함한 이메일을 입력해주세요");
            focusView = usernameField;
            cancel = true;
        }

        if(cancel){
            focusView.requestFocus(); //포커스를 줌
        } else{
            startLogin(new LoginData(email,password, isAutoLoginChecked)); // Login_Data/LoginData.class
            showProgress(true);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
//https://m.blog.naver.com/PostView.nhn?blogId=eyeballss&logNo=221043096972&proxyReferer=https%3A%2F%2Fwww.google.com%2F retrofit 통신 기초
    private void startLogin(LoginData data){
        service.userLogin(data).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                Log.e("로그인 시도 : ", usernameField.getText().toString());
                LoginResponse result = response.body();


                Toast.makeText(getApplicationContext(), result.getMessage(),Toast.LENGTH_SHORT).show();
                //res값으로 로그인 성공 여부 확인
                if(result.getCode() == 200) {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);


                    // 로그인한 정보 저장
                    SharedPreferences sf = getSharedPreferences("sFile", MODE_PRIVATE);
                    SharedPreferences.Editor editor;
                    editor = sf.edit();
                    editor.putInt("login_state", 200).apply();
                    editor.putString("user_id", result.getUserId()).apply();
                    editor.putString("code", "" + result.getCode()).apply();
                    editor.putString("nickname", result.getNickname()).apply();
                    editor.putString("email", result.getEmail()).apply();
                    editor.putString("auto_login", isAutoLoginChecked).apply();



//                    intent.putExtra("user_id", result.getUserId());
//                    intent.putExtra("nickname", result.getNickname());
//                    intent.putExtra("email", result.getEmail());

//                    intent.putExtra("login_state", 200);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"로그인 에러 발생", Toast.LENGTH_SHORT).show();
                Log.e("로그인 에러 발생", t.getMessage());
                showProgress(false);
            }
        }); //ServiceApi에서 나옴
    }

        private boolean isEmailValid(String email) {
            return email.contains("@");
        }

        private boolean isPasswordValid(String password) {
            return password.length() >= 4;
        }

        private void showProgress(boolean show) {
            loginProgress.setVisibility(show ? View.VISIBLE : View.GONE);
        }
}
