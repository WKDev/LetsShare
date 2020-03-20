package com.tbk.letsshare.MainFragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.tbk.letsshare.Comm_Data.ItemDataResponse;
import com.tbk.letsshare.ItemDetailedActivity;
import com.tbk.letsshare.ListManager.ItemListAdapter;
import com.tbk.letsshare.ListManager.ItemListContainer;
import com.tbk.letsshare.R;
import com.tbk.letsshare.network.RetrofitClient;
import com.tbk.letsshare.network.ServiceApi;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// Fragment를 상속받는 클래스, onCreateView를 재정의하고, inflater를 통해 레이아웃 리소스 id로 생성된 View 반환
public class FragmentHome extends Fragment {

    public static FragmentHome newInstance() {
        return new FragmentHome();
    }

    private ArrayList titleArray = new ArrayList();
    private ArrayList priceArray = new ArrayList();
    private ArrayList writerArray = new ArrayList();
    private ArrayList descriptionArray = new ArrayList();
    private ArrayList dateArray = new ArrayList();


    private ServiceApi client;

    private SwipeRefreshLayout mRefresh;
    private RecyclerView mRecyclerView;
    private ArrayList<ItemListContainer> mArrayList;
    private ItemListAdapter mAdapter;
    private ItemListContainer itemBowl;


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_home, container, false);
        mRefresh = rootView.findViewById(R.id.swiperefresh);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_itemlist);

        client = RetrofitClient.getClient().create(ServiceApi.class);
        parseItemData();
        mAdapter = new ItemListAdapter(mArrayList);

        //  mRefresh.setColorSchemeResources(R.color.yellow, R.color.red, R.color.black, R.color.blue);
        mRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mRefresh.setRefreshing(true);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        parseItemData();
                        mRefresh.setRefreshing(false);
                    }
                }, 1000);

                parseItemData();
            }
        });
        return rootView;
    }


    protected void parseItemData() {

        Call<List<ItemDataResponse>> call = client.importItem();
        call.enqueue(new Callback<List<ItemDataResponse>>() {
            @Override
            public void onResponse(Call<List<ItemDataResponse>> call, Response<List<ItemDataResponse>> response) {
                List<ItemDataResponse> resource = response.body();
                LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getActivity());
                mRecyclerView.setHasFixedSize(true);
                mRecyclerView.setLayoutManager(mLinearLayoutManager);
                mArrayList = new ArrayList<>();
                mAdapter = new ItemListAdapter(mArrayList);
                for (ItemDataResponse data : resource) {
                    itemBowl = new ItemListContainer(R.drawable.ic_launcher_background, data.parsedTitle, data.parsedPrice, data.parsedWriter);
                    mArrayList.add(itemBowl);
                    mRecyclerView.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();
                    titleArray.add(data.getParsedTitle());
                    priceArray.add(data.getParsedPrice());
                    writerArray.add(data.getParsedWriter());
                    descriptionArray.add(data.getParsedDescription());
                    dateArray.add(data.getParsedDate());

                    mRecyclerView.setAdapter(mAdapter);

                    mAdapter.setOnItemClickListener(new ItemListAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(View v, int pos) {
                            Intent intent = new Intent(getActivity(), ItemDetailedActivity.class);
                            intent.putExtra("title", "" + titleArray.get(pos));
                            intent.putExtra("price", "" + priceArray.get(pos));
                            intent.putExtra("writer", "" + writerArray.get(pos));
                            intent.putExtra("desc", "" + descriptionArray.get(pos));
                            intent.putExtra("date", "" + dateArray.get(pos));
                            startActivity(intent);
                        }
                    });
                }
            }
//                        Toast.makeText(getActivity(), "Succeeded to parsing itemData from DB", Toast.LENGTH_SHORT).show();

        @Override
        public void onFailure (Call < List < ItemDataResponse >> call, Throwable t){
            try {
                Toast.makeText(getActivity(), "DB와의 통신에 실패했습니다.", Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Toast.makeText(getActivity(), "에러 발생, 앱을 종료합니다.", Toast.LENGTH_LONG).show();
                Log.e("에러 발생", t.getMessage());
            }

        }
    });
}

}
