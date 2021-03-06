package com.tbk.letsshare;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.tbk.letsshare.Comm_Data.ItemDataResponse;
import com.tbk.letsshare.Comm_Data.LoginResponse;
import com.tbk.letsshare.Comm_Data.SearchRequest;
import com.tbk.letsshare.Comm_Data.SearchResult;
import com.tbk.letsshare.ListManager.ItemListAdapter;
import com.tbk.letsshare.ListManager.ItemListContainer;
import com.tbk.letsshare.ListManager.SearchResultAdapter;
import com.tbk.letsshare.ListManager.SearchResultContainer;
import com.tbk.letsshare.network.RetrofitClient;
import com.tbk.letsshare.network.ServiceApi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class searchResultActivity extends AppCompatActivity {

    private RecyclerView SRrecyclerView;
    private ArrayList<SearchResultContainer> SRArrayList;
    private SearchResultAdapter SRAdapter;

    private ArrayList titleArray = new ArrayList();
    private ArrayList priceArray = new ArrayList();
    private ArrayList writerArray = new ArrayList();
    private ArrayList descriptionArray = new ArrayList();
    private ArrayList dateArray = new ArrayList();


    private ServiceApi service;
    private String inputData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        Intent aIntent = getIntent();
        inputData = aIntent.getStringExtra("searched");
        inputData = "%" + inputData + "%"; // 포함검색을 하기 위해 SQL 쿼리 %% 추가함;

        SRrecyclerView = (RecyclerView) findViewById(R.id.sr_itemlist);
        service = RetrofitClient.getClient().create(ServiceApi.class);

        SearchRequest request = new SearchRequest(inputData);

        Toast.makeText(getApplicationContext(), "입력 : " + request.getSearchTitle() + "에 대한 검색 실행", Toast.LENGTH_SHORT).show();

        //서버 통신해서 아이템 리스트에 추가함
        service.searchItem(request).enqueue(new Callback<List<SearchResult>>() {
            @Override
            public void onResponse(Call<List<SearchResult>> call, Response<List<SearchResult>> response) {
//                Toast.makeText(getApplicationContext(), "Server Responsed to Request.", Toast.LENGTH_SHORT).show();
                List<SearchResult> resource = response.body();
                LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getApplicationContext());
                SRrecyclerView.setHasFixedSize(true);
                SRrecyclerView.setLayoutManager(mLinearLayoutManager);

                SRArrayList = new ArrayList<>();
                SRAdapter = new SearchResultAdapter(SRArrayList);
                SRrecyclerView.setAdapter(SRAdapter);


                for (SearchResult data : resource) {
                    SearchResultContainer itemBowl = new SearchResultContainer(R.drawable.ic_launcher_background, data.getResultTitle(), data.getResultPrice(), data.getResultWriter());

                    titleArray.add(data.getResultTitle());
                    priceArray.add(data.getResultPrice());
                    writerArray.add(data.getResultWriter());
                    descriptionArray.add(data.getResultDescription());
                    dateArray.add(data.getResultDate());

                    SRArrayList.add(itemBowl);
                }
                SRAdapter.setOnItemClickListener(new SearchResultAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View v, int pos) {
                        Intent intent = new Intent(getApplicationContext(), ItemDetailedActivity.class);
                        intent.putExtra("title", "" + titleArray.get(pos));
                        intent.putExtra("price", "" + priceArray.get(pos));
                        intent.putExtra("writer", "" + writerArray.get(pos));
                        intent.putExtra("desc", "" + descriptionArray.get(pos));
                        intent.putExtra("date", "" + dateArray.get(pos));
                        startActivity(intent);
                    }
                });

                SRrecyclerView.setAdapter(SRAdapter);

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

        //아이템을 눌렀을 때 이벤트 처리

    }
}
