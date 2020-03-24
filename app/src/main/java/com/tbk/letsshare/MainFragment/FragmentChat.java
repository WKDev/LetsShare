package com.tbk.letsshare.MainFragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.tbk.letsshare.ChatRoomActivity;
import com.tbk.letsshare.Comm_Data.ChatRoomReq;
import com.tbk.letsshare.Comm_Data.ChatRoomRes;

import com.tbk.letsshare.ListManager.ChatListAdapter;
import com.tbk.letsshare.ListManager.ChatListContainer;

import com.tbk.letsshare.R;
import com.tbk.letsshare.network.RetrofitClient;
import com.tbk.letsshare.network.ServiceApi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;

public class FragmentChat extends Fragment {

    private ServiceApi client;
    private RecyclerView RecyclerChat;
    private ArrayList<ChatListContainer> mArrayList;
    private ChatListAdapter mAdapter;

    private SharedPreferences sf;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_chat, container, false);

        ImageView imageView = (ImageView) rootView.findViewById(R.id.imageView3);
        Glide.with(getContext()).load("http://wwwns.akamai.com/media_resources/globe_emea.png").into(imageView);
        RecyclerChat = (RecyclerView) rootView.findViewById(R.id.recycler_chatlist);
        RecyclerChat.setHasFixedSize(true);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getActivity());
        RecyclerChat.setLayoutManager(mLinearLayoutManager);
        mAdapter = new ChatListAdapter(mArrayList);

        sf = getActivity().getSharedPreferences("sFile", MODE_PRIVATE);
        String userID = sf.getString("user_id", "null");

        //@mode : refer or add
        chatRoom(new ChatRoomReq(userID, "refer"));
        return rootView;
    }

    public void chatRoom(ChatRoomReq req) {
        client = RetrofitClient.getClient().create(ServiceApi.class);
        Call<List<ChatRoomRes>> call = client.chatRoom(req);

        //req : buyer_id, mode
        //res : chat_room_id, seller_id,last_statement
        call.enqueue(new Callback<List<ChatRoomRes>>() {
            @Override
            public void onResponse(Call<List<ChatRoomRes>> call, Response<List<ChatRoomRes>> response) {
                List<ChatRoomRes> resource = response.body();
                //  Toast.makeText(getActivity(), "DB와의 통신에 성공했습니다", Toast.LENGTH_LONG).show();
                mArrayList = new ArrayList<>();
                mAdapter = new ChatListAdapter(mArrayList);
                for (ChatRoomRes data : resource) {
                    ChatListContainer container = new ChatListContainer();
                    // 리스트 한 개에 대한 값 설정
                    container.setRoomId(data.getChatRoomId());
                    container.setRoomTitle(data.getSeller_id());
                    container.setLast_statement(data.getLastStatement());
                    mArrayList.add(container); // 이걸 전체 리스트에 차곡차곡 쌓아줌

                    //[{이름 : name, 날짜 : date, 가격 : price }, {이름 : name, 날짜 : date, 가격 : price } ]
                    // 중괄호 안의 개별 데이터를 container를 통해 담고 그렇게 생성된
                    // container 데이터 한 덩어리를 통째로 mArrayList에 삽입.
                } // data를 리스트에 쌓는 과정
                RecyclerChat.setAdapter(mAdapter);
                //Toast.makeText(getActivity(), mArrayList.get(2).getImageURL(),Toast.LENGTH_SHORT).show();mAdapter.notifyDataSetChanged();
                RecyclerChat.setHasFixedSize(true);
                LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getActivity());
                RecyclerChat.setLayoutManager(mLinearLayoutManager);
                mAdapter.notifyDataSetChanged();

                //채팅창 이동 | 아이템 정보를 넘겨주며 ChatRoomActivity 이동
                mAdapter.setOnItemClickListener(new ChatListAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View v, int pos) {
                        Intent intent = new Intent(getActivity(), ChatRoomActivity.class);
                        intent.putExtra("chat_room_id", mArrayList.get(pos).getRoomId());
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onFailure(Call<List<ChatRoomRes>> call, Throwable t) {
                try {
                    Toast.makeText(getActivity(), "FragmentChat: DB와의 통신에 실패했습니다.", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Toast.makeText(getActivity(), "에러 발생, 앱을 종료합니다.", Toast.LENGTH_LONG).show();
                    Log.e("에러 발생", t.getMessage());
                }

            }
        });
    }

}