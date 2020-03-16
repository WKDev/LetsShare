package com.tbk.letsshare;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.tbk.letsshare.Comm_Data.ItemDataResponse;
import com.tbk.letsshare.Comm_Data.LoginData;
import com.tbk.letsshare.Comm_Data.LoginResponse;
import com.tbk.letsshare.Comm_Data.SearchRequest;
import com.tbk.letsshare.Comm_Data.SearchResult;
import com.tbk.letsshare.ListManager.ItemListAdapter;
import com.tbk.letsshare.ListManager.ItemListContainer;
import com.tbk.letsshare.network.ServiceApi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class searchResultActivity extends AppCompatActivity {
    private ServiceApi service;
    private String inputData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        Intent aIntent = getIntent();
        inputData = aIntent.getStringExtra("searched");

        parseSearchedItem(new SearchRequest(inputData));

    }

    protected void parseItemData(SearchRequest data) {
        Call<List<SearchResult>> call = service.searchItem(data);
        call.enqueue(new Callback<List<SearchResult>>() {
            @Override
            public void onResponse(Call<List<SearchResult>> call, Response<List<SearchResult>> response) {
                List<SearchResult> resource = response.body();
                LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getApplicationContext());
                mRecyclerView.setHasFixedSize(true);
                mRecyclerView.setLayoutManager(mLinearLayoutManager);

                mArrayList = new ArrayList<>();
                mAdapter = new ItemListAdapter(mArrayList);
                mRecyclerView.setAdapter(mAdapter);
                for (ItemDataResponse data : resource) {
                    ItemListContainer itemBowl = new ItemListContainer(R.drawable.ic_launcher_background, data.parsedTitle, data.parsedPrice, data.parsedWriter);
                    mArrayList.add(itemBowl);
                    mAdapter.notifyDataSetChanged();
                }
                Toast.makeText(getApplicationContext(), "Succeeded to parsing itemData from DB", Toast.LENGTH_LONG).show();



            }

            @Override
            public void onFailure(Call<List<SearchResult>> call, Throwable t) {
                try {
                    Toast.makeText(getApplicationContext(), "DB와의 통신에 실패했습니다.", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "에러 발생, 앱을 종료합니다.", Toast.LENGTH_LONG).show();
                    Log.e("에러 발생", t.getMessage());
                }

            }
        });
    }
}
