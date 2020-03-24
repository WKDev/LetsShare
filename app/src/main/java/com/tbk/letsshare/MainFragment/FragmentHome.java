package com.tbk.letsshare.MainFragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
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

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// Fragment를 상속받는 클래스, onCreateView를 재정의하고, inflater를 통해 레이아웃 리소스 id로 생성된 View 반환
public class FragmentHome extends Fragment {

    private ServiceApi client;

    private SwipeRefreshLayout mRefresh;
    private RecyclerView mRecyclerView;
    private ArrayList<ItemListContainer> mArrayList;
    private ItemListAdapter mAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_home, container, false);
        mRefresh = rootView.findViewById(R.id.swiperefresh);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_itemlist);


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
            }
        });
        parseItemData();
        return rootView;
    }


    public void parseItemData() {
        client = RetrofitClient.getClient().create(ServiceApi.class);
        Call<List<ItemDataResponse>> call = client.importItem();
        call.enqueue(new Callback<List<ItemDataResponse>>() {
            @Override
            public void onResponse(Call<List<ItemDataResponse>> call, Response<List<ItemDataResponse>> response) {
                List<ItemDataResponse> resource = response.body();
              //  Toast.makeText(getActivity(), "DB와의 통신에 성공했습니다", Toast.LENGTH_LONG).show();
                mArrayList = new ArrayList<>();
                String BASE_URL = "http://ec2-13-209-22-0.ap-northeast-2.compute.amazonaws.com:3000/";
                for (ItemDataResponse data : resource) {
                    ItemListContainer container = new ItemListContainer();
                    // 리스트 한 개에 대한 값 설정
                    container.setName(data.parsedTitle);
                    container.setDate(data.parsedDate);
                    container.setPrice(data.parsedPrice);
                    container.setThumbnail(BASE_URL+data.parsedImg);
                    container.setDescription(data.parsedDescription);
                    container.setWriter(data.parsedWriter);
                    mArrayList.add(container); // 이걸 전체 리스트에 차곡차곡 쌓아줌

                } // data를 리스트에 쌓는 과정
                //Toast.makeText(getActivity(), mArrayList.get(2).getImageURL(),Toast.LENGTH_SHORT).show();

                mRecyclerView.setHasFixedSize(true);
                LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getActivity());
                mRecyclerView.setLayoutManager(mLinearLayoutManager);
                mAdapter = new ItemListAdapter(mArrayList);
                mRecyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();

                //세부사항 표현하는 리스너 | 아이템 정보를 넘겨주며 ItemDetailedActivity로 이동
                mAdapter.setOnItemClickListener(new ItemListAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View v, int pos) {
                        Intent intent = new Intent(getActivity(), ItemDetailedActivity.class);
                        intent.putExtra("title", "" + mArrayList.get(pos).getName());
                        intent.putExtra("price", "" + mArrayList.get(pos).getPrice());
                        intent.putExtra("writer", "" + mArrayList.get(pos).getWriter());
                        intent.putExtra("desc", "" + mArrayList.get(pos).getDescription());
                        intent.putExtra("date", "" + mArrayList.get(pos).getDate());
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onFailure(Call<List<ItemDataResponse>> call, Throwable t) {
                try {
                    Toast.makeText(getActivity(), "FragmentHome: DB와의 통신에 실패했습니다.", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Toast.makeText(getActivity(), "에러 발생, 앱을 종료합니다.", Toast.LENGTH_LONG).show();
                    Log.e("에러 발생", t.getMessage());
                }

            }
        });
    }

}
