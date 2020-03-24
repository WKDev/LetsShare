package com.tbk.letsshare.MainFragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.tbk.letsshare.Comm_Data.LogoutReq;
import com.tbk.letsshare.Comm_Data.LogoutRes;
import com.tbk.letsshare.Comm_Data.verifyStateReq;
import com.tbk.letsshare.Comm_Data.verifyStateRes;
import com.tbk.letsshare.MainActivity;
import com.tbk.letsshare.R;
import com.tbk.letsshare.network.RetrofitClient;
import com.tbk.letsshare.network.ServiceApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;


public class FragmentAccount extends Fragment {


    private String userID;
    private String isAutoLoginChecked;
    private String aLoginState;

    private SharedPreferences sf;
    private SharedPreferences.Editor editor;
    private Intent intent;

    private ServiceApi service;
    private ServiceApi service2;

    private TextView accId;
    private TextView accNickname;
    private TextView accEmail;
    private CheckBox AutoLoginBox;
    private Button logoutBtn;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final ViewGroup memberView = (ViewGroup) inflater.inflate(R.layout.fragment_account, container, false);

        accId = memberView.findViewById(R.id.acc_id);
        accNickname = memberView.findViewById(R.id.acc_nickname);
        accEmail = memberView.findViewById(R.id.acc_email);
        logoutBtn = memberView.findViewById(R.id.logout);
        AutoLoginBox = memberView.findViewById(R.id.acc_autologin);

        service = RetrofitClient.getClient().create(ServiceApi.class);
        service2 = RetrofitClient.getClient().create(ServiceApi.class);

        sf = getActivity().getSharedPreferences("sFile", MODE_PRIVATE);

        userID = sf.getString("user_id", "account:null");

        accId.setText(userID);
        accNickname.setText(sf.getString("nickname", "account:null"));
        accEmail.setText(sf.getString("email", "account:null"));

        aLoginState = sf.getString("auto_login", "false");

        if(aLoginState == "true") {
            AutoLoginBox.setChecked(true);
        } else{
            AutoLoginBox.setChecked(false);
        }

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout(new LogoutReq(userID));


            }
        });

        AutoLoginBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(AutoLoginBox.isChecked()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(memberView.getContext());

                    builder.setMessage("개인 휴대폰이 아닐 시 위험할 수 있습니다. 계속하시겠습니까?")
                            .setPositiveButton("네", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    isAutoLoginChecked = "true";
                                    changeState(new verifyStateReq(userID, isAutoLoginChecked, "in_use"));
                                    ;
                                }
                            })
                            .setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    isAutoLoginChecked = "false";
                                    changeState(new verifyStateReq(userID, isAutoLoginChecked, "in_use"));
                                }
                            });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                } else {
                    isAutoLoginChecked = "false";
                    changeState(new verifyStateReq(userID, isAutoLoginChecked, "in_use"));
                }
                }
        });

        return memberView;
    }

    private void changeState(final verifyStateReq data) {
        service2.changeState(data).enqueue(new Callback<verifyStateRes>() {
            String use = data.getUse();

            @Override
            public void onResponse(Call<verifyStateRes> call, Response<verifyStateRes> response) {
                verifyStateRes result = response.body();
                Log.e("autologin 정보 불러오기 : ", "");

                if (use == "initial_checkout") {
                    if (result.getValue() == "true") {
                        AutoLoginBox.setChecked(true);
                        saveData("auto_login", "true");
                    }
                    if (result.getValue() == "false") {
                        AutoLoginBox.setChecked(false);
                        saveData("auto_login", "false");
                        Toast.makeText(getContext(), "autologin" + sf.getString("auto_login", ""), Toast.LENGTH_SHORT).show();

                    }
                }
            }

            @Override
            public void onFailure(Call<verifyStateRes> call, Throwable t) {
                Toast.makeText(getContext(), "통신 에러 발생", Toast.LENGTH_SHORT).show();
                Log.e("통신 에러 발생", t.getMessage());
            }
        }); //ServiceApi에서 나옴
    }

    private void logout(LogoutReq data) {
        service.userLogout(data).enqueue(new Callback<LogoutRes>() {
            @Override
            public void onResponse(Call<LogoutRes> call, Response<LogoutRes> response) {
                Context context = getActivity();
                LogoutRes result = response.body();
                Log.e("로그아웃 시도 : ", "");
                if (result.getCode() == 200) {

                    sf = context.getSharedPreferences("sFile", MODE_PRIVATE);
                    editor = sf.edit();
                    editor.putInt("login_state", 404).apply();

                    Toast.makeText(context, "로그아웃 성공" + sf.getInt("login_state", 1000), Toast.LENGTH_SHORT).show();

                    editor.putInt("login_state", 404);
                    editor.remove("user_id");
                    editor.remove("code");
                    editor.remove("nickname");
                    editor.remove("email");
                    editor.remove("auto_login").apply();

                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction().remove(FragmentAccount.this).commit();
                    //fragmentManager.popBackStack();

                    FragmentHome fragmentHome = new FragmentHome();
                    FragmentTransaction tr = fragmentManager.beginTransaction();
                    tr.replace(R.id.main_frame, fragmentHome).commit();
                }

            }

            @Override
            public void onFailure(Call<LogoutRes> call, Throwable t) {
                Toast.makeText(getContext(), "로그아웃 에러 발생", Toast.LENGTH_SHORT).show();
                Log.e("로그아웃 에러 발생", t.getMessage());
            }
        }); //ServiceApi에서 나옴
    }

    public void saveData(String key, String state) {
        editor = sf.edit();
        editor.putString(key, state).apply();
    }
}

