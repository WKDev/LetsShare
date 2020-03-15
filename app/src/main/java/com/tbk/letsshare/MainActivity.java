package com.tbk.letsshare;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.tbk.letsshare.MainFragment.FragmentAccount;
import com.tbk.letsshare.MainFragment.FragmentCategory;
import com.tbk.letsshare.MainFragment.FragmentChat;
import com.tbk.letsshare.MainFragment.FragmentHome;

import org.json.JSONException;
import org.json.JSONObject;

import static java.sql.DriverManager.println;

public class MainActivity extends AppCompatActivity {


    private FragmentManager fragmentManager = getSupportFragmentManager();

    //fragment_____.class에서 정의한 Fragment 선언, 아래 클래스들은 Fragment View를 반환함
    private FragmentHome fragmentHome = new FragmentHome();
    private FragmentCategory fragmentCategory = new FragmentCategory();
    private FragmentChat fragmentChat = new FragmentChat();
    private FragmentAccount fragmentAccount = new FragmentAccount();
    private String publicDNS = "ec2-13-209-22-0.ap-northeast-2.compute.amazonaws.com:8080";
    private String parsedData = "0";


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
                        transaction1.replace(R.id.main_frame, fragmentHome).commit();
                        break;

                    case R.id.category_item:
                        transaction1.replace(R.id.main_frame, fragmentCategory).commit();
                        break;
                    case R.id.chat_item:
                        transaction1.replace(R.id.main_frame, fragmentChat).commit();
                        break;
                    case R.id.account_item:

                        Intent intent = getIntent();
                        parsedData = intent.getStringExtra("mydata");

                        if(parsedData == null){
                            Toast.makeText(getApplicationContext(), "null handed-over", Toast.LENGTH_LONG).show();
                            Intent myIntent = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(myIntent);


                        } else{
                            transaction1.replace(R.id.main_frame, fragmentAccount).commit();
                            Toast.makeText(getApplicationContext(), "not-null handed-over", Toast.LENGTH_LONG).show();
                        }


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
