package com.tbk.letsshare;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.tbk.letsshare.Comm_Data.ItemDataResponse;
import com.tbk.letsshare.Comm_Data.LoginData;
import com.tbk.letsshare.Comm_Data.LoginResponse;
import com.tbk.letsshare.ListManager.ItemListAdapter;
import com.tbk.letsshare.ListManager.ItemListContainer;
import com.tbk.letsshare.network.ServiceApi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity {
    private EditText searchField;
    private Button searchBtn;

    private String searchText;

    private ServiceApi service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ActionBar ab = getSupportActionBar();
        ab.hide();

        searchField = findViewById(R.id.search_field);
        searchBtn = findViewById(R.id.search_button);

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchText = searchField.getText().toString();
                Intent intent = new Intent(getApplicationContext(), searchResultActivity.class);
                intent.putExtra("searched", searchText);
                startActivity(intent);


            }
        });

    }
}
