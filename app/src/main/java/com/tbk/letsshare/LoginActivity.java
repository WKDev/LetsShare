package com.tbk.letsshare;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.tbk.letsshare.Login_Data.LoginData;
import com.tbk.letsshare.Login_Data.LoginResponse;
import com.tbk.letsshare.MainFragment.FragmentAccount;
import com.tbk.letsshare.network.RetrofitClient;
import com.tbk.letsshare.network.ServiceApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    public static final String KEY_VALUE = "Logged in";

     EditText usernameField;
     EditText passwordField;
    Button signIn;
    Button signUp;
    ProgressBar loginProgress;
    private ServiceApi service;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameField = (EditText)findViewById(R.id.username);
        passwordField = (EditText)findViewById(R.id.password);
        signIn = (Button)findViewById(R.id.login);
        signUp = (Button)findViewById(R.id.register);
        loginProgress = (ProgressBar)findViewById(R.id.login_progress);

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
            startLogin(new LoginData(email,password)); // Login_Data/LoginData.class
            showProgress(true);
        }
    }

    private void startLogin(LoginData data){
        service.userLogin(data).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                Log.e("로그인 시도 : ", usernameField.getText().toString());
                LoginResponse result = response.body();
                Toast.makeText(getApplicationContext(), result.getMessage(),Toast.LENGTH_SHORT).show();
                showProgress(false); // getMessage() : LoginResponse에 존재하는 메서드

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("mydata", "1028");
                startActivity(intent);

                finish();
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
