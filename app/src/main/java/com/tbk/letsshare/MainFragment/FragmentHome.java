package com.tbk.letsshare.MainFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.tbk.letsshare.ListManager.ItemListAdapter;
import com.tbk.letsshare.ListManager.ItemListContainer;
import com.tbk.letsshare.MainActivity;
import com.tbk.letsshare.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

// Fragment를 상속받는 클래스, onCreateView를 재정의하고, inflater를 통해 레이아웃 리소스 id로 생성된 View 반환
public class FragmentHome extends Fragment {

    private ArrayList<ItemListContainer> mArrayList;
    private ItemListAdapter mAdapter;
    private int count = -1;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_home, container, false);

        RecyclerView mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_itemlist);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);


        mArrayList = new ArrayList<>();

        mAdapter = new ItemListAdapter(mArrayList);
        mRecyclerView.setAdapter(mAdapter);

      //  DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
      //          mLinearLayoutManager.getOrientation());
      //  mRecyclerView.addItemDecoration(dividerItemDecoration);

        Button buttonInsert = (Button) rootView.findViewById(R.id.button_main_insert);
        buttonInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count++;

                ItemListContainer data = new ItemListContainer(R.drawable.ic_launcher_background, "TestItem", "10000원", "Yesterday");

                //mArrayList.add(0, dict); //RecyclerView의 첫 줄에 삽입
                mArrayList.add(data); // RecyclerView의 마지막 줄에 삽입
                mAdapter.notifyDataSetChanged();
            }
        });
        return rootView;
    }

}