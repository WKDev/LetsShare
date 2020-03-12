package com.tbk.letsshare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

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

                switch(menuItem.getItemId()){
                    case R.id.home_item:
                        transaction1.replace(R.id.main_frame, fragmentHome).commitAllowingStateLoss();
                        break;

                    case R.id.category_item:
                        transaction1.replace(R.id.main_frame, fragmentCategory).commitAllowingStateLoss();
                        break;
                    case R.id.chat_item:
                        transaction1.replace(R.id.main_frame, fragmentChat).commitAllowingStateLoss();
                        break;
                    case R.id.account_item:
                        transaction1.replace(R.id.main_frame, fragmentAccount).commitAllowingStateLoss();
                        break;

                }
                return false;
            }
        });
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
        if(item.getItemId() == R.id.action_search){
            Intent myIntent = new Intent(getApplicationContext(), SearchActivity.class);
            startActivity(myIntent);
        }
        return super.onOptionsItemSelected(item);
    }


}
