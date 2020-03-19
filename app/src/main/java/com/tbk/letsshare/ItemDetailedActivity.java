package com.tbk.letsshare;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.tbk.letsshare.Comm_Data.ItemDataResponse;
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

public class ItemDetailedActivity extends AppCompatActivity {

    private TextView detailedTitle;
    private TextView detailedPrice;
    private TextView detailedDescription;
    private TextView detailedWriter;
    private TextView detailedDate;

    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detailed);

        detailedTitle = findViewById(R.id.detailed_title);
        detailedPrice = findViewById(R.id.detailed_price);
        detailedDescription = findViewById(R.id.detailed_description);
        detailedWriter = findViewById(R.id.detailed_writer);
        detailedDate = findViewById(R.id.detailed_date);

        Intent intent = getIntent();

        detailedTitle.setText(intent.getStringExtra("title"));
        detailedPrice.setText(intent.getStringExtra("price"));
        detailedDescription.setText(intent.getStringExtra("desc"));
        detailedWriter.setText(intent.getStringExtra("writer"));
        detailedDate.setText(intent.getStringExtra("date"));
    }
}
