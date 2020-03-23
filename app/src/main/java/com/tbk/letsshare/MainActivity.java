package com.tbk.letsshare;

import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.tbk.letsshare.MainFragment.FragmentAccount;
import com.tbk.letsshare.MainFragment.FragmentCategory;
import com.tbk.letsshare.MainFragment.FragmentChat;
import com.tbk.letsshare.MainFragment.FragmentHome;


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

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 맨 처음에 나타날 Fragment 등록함
        final FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.main_frame, fragmentHome).commitAllowingStateLoss();

        BottomNavigationView bottomNavigationView = findViewById(R.id.navigationView);


        //메뉴 클릭 이벤트에 따른 리스너 등록
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
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
                        transaction1.replace(R.id.main_frame, fragmentChat).commit();
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
//        editor = sf.edit();
//        editor.putString("code","logged-out").commit();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // 액션바 Inflate
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

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
    }


}
