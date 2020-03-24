package com.tbk.letsshare;

import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.tbk.letsshare.MainFragment.FragmentAccount;
import com.tbk.letsshare.MainFragment.FragmentCategory;
import com.tbk.letsshare.MainFragment.FragmentChat;
import com.tbk.letsshare.MainFragment.FragmentHome;

import static android.widget.Toast.LENGTH_SHORT;


public class MainActivity extends AppCompatActivity {

    private SharedPreferences sf;
    private SharedPreferences.Editor editor;

    public int inAppLoginState = 404;
    public int loginRes;


    private FragmentManager fragmentManager = getSupportFragmentManager();

    //fragment_____.class에서 정의한 Fragment 선언, 아래 클래스들은 Fragment View를 반환함
    private FragmentHome fragmentHome = new FragmentHome();
    private FragmentCategory fragmentCategory = new FragmentCategory();
    private FragmentChat fragmentChat = new FragmentChat();
    private FragmentAccount fragmentAccount = new FragmentAccount();

    private BackPressCloseHandler backPressCloseHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 맨 처음에 나타날 Fragment 등록함
        final FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.main_frame, fragmentHome).commitAllowingStateLoss();

        BottomNavigationView bottomNavigationView = findViewById(R.id.navigationView);


        //메뉴 클릭 이벤트에 따른 리스너 등록
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        final FragmentTransaction transaction1 = fragmentManager.beginTransaction();

                        switch (menuItem.getItemId()) {
                            case R.id.home_item: {
                                transaction1.replace(R.id.main_frame, fragmentHome).commit();
                                break;
                            }
                            case R.id.category_item: {
                                transaction1.replace(R.id.main_frame, fragmentCategory).commit();
                                break;
                            }
                            case R.id.chat_item: {
                                SharedPreferences sf = getSharedPreferences("sFile", MODE_PRIVATE);
                                String userID = sf.getString("user_id", "null");
                                Toast.makeText(getApplicationContext(), userID, LENGTH_SHORT).show();
                                if (userID == "null") {
                                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                    startActivity(intent);
                                } else {
                                    transaction1.replace(R.id.main_frame, fragmentChat).commit();
                                }
                                break;
                            }
                            case R.id.account_item: {
                                sf = getSharedPreferences("sFile", MODE_PRIVATE);
                            }
                            inAppLoginState = sf.getInt("login_state", 404);

                            if (inAppLoginState == 200) {
                                //Toast.makeText(getApplicationContext(), "로그인 성공, fragmentAccount로 이동", Toast.LENGTH_SHORT).show();
                                transaction1.replace(R.id.main_frame, fragmentAccount).commit();
                            } else if (inAppLoginState == 404) {
                                Intent myIntent = new Intent(getApplicationContext(), LoginActivity.class);
                                startActivity(myIntent);
                            }
                            break;
                        }
                        return false;
                    }
                }

        );

        backPressCloseHandler = new BackPressCloseHandler(this);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //출처: https://jhshjs.tistory.com/entry/안드로이드-Activity-Fragment-각각에서-각각의-함수변수-접근 [독학하는 1인 개발자]
    }

    @Override
    protected void onStop() {

        super.onStop();
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // 액션바 Inflate
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    } // 액션바 inflate

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //액션바 아이템 눌림 이벤트 handle
        if (item.getItemId() == R.id.action_search) {
            Intent myIntent = new Intent(getApplicationContext(), SearchActivity.class);
            startActivity(myIntent);
        }
        if (item.getItemId() == R.id.action_add) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.remove(fragmentHome);
            Intent myIntent = new Intent(getApplicationContext(), ItemAddActivity.class);
            startActivity(myIntent);
        }
        return super.onOptionsItemSelected(item);
    } // 액션바 메뉴 누름 이벤트


    // 두번 눌러 종료 활성화
    public class BackPressCloseHandler {
        private long backKeyPressedTime = 0;
        private Toast toast;
        private Activity activity;

        public BackPressCloseHandler(Activity context) {
            this.activity = context;
        }

        public void onBackPressed() {
            if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
                backKeyPressedTime = System.currentTimeMillis();
                showGuide();
                return;
            }
            if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
                sf = getSharedPreferences("sFile", MODE_PRIVATE);
                String isAutologin = sf.getString("auto_login", "false");
                if (isAutologin == "false") {
                    editor = sf.edit();
                    editor.putInt("login_state", 404).apply();
                    editor.remove("user_id");
                    editor.remove("code");
                    editor.remove("nickname");
                    editor.remove("email");
                    editor.remove("auto_login").apply();
                    Toast.makeText(activity, "로그아웃합니다.", Toast.LENGTH_SHORT).show();
                }
                activity.finish();
                toast.cancel();
            }
        }

        public void showGuide() {
            toast = Toast.makeText(activity, "\'뒤로\'버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    @Override
    public void onBackPressed() {
        backPressCloseHandler.onBackPressed();
    }
}
