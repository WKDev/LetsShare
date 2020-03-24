package com.tbk.letsshare;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.tbk.letsshare.ListManager.ItemListAdapter;
import com.tbk.letsshare.ListManager.ItemListContainer;
import com.tbk.letsshare.ListManager.MessageAdapter;
import com.tbk.letsshare.ListManager.MessageContainer;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

import static android.content.ContentValues.TAG;

public class ChatRoomActivity extends AppCompatActivity {


    private EditText msgField;
    private Button sendButton;

    private Socket mSocket;

    private RecyclerView mRecyclerView;
    private ArrayList<MessageContainer> mArrayList;
    private MessageAdapter mAdapter;
    private JSONObject receivedData;

    private String roomId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        mRecyclerView = findViewById(R.id.recycler_msg);
        msgField = findViewById(R.id.msg_field);
        sendButton = findViewById(R.id.msg_button);

        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);

        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mArrayList = new ArrayList<>();

        mAdapter = new MessageAdapter(mArrayList);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();


        initChatSocket();

        msgField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (msgField.length() == 0) {
                    sendButton.setEnabled(false);
                } else {
                    sendButton.setEnabled(true);

                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (msgField.length() == 0) {
                    sendButton.setEnabled(false);
                } else {
                    sendButton.setEnabled(true);

                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (msgField.length() == 0) {
                    sendButton.setEnabled(false);
                } else {
                    sendButton.setEnabled(true);

                }
            }
        });
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });

        Intent intent = getIntent();
        roomId = intent.getStringExtra("chat_room_id");
        ActionBar ab = getSupportActionBar();
        ab.setTitle(roomId);
    }


    private void initChatSocket() {
        try {
            mSocket = IO.socket("http://ec2-13-209-22-0.ap-northeast-2.compute.amazonaws.com:3000");
            mSocket.connect();
            mSocket.on(Socket.EVENT_CONNECT, onConnect);
            mSocket.on(Socket.EVENT_DISCONNECT, onConnectError);
            mSocket.on("msg_to_room", onMessageReceived);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }


    }

    private void sendMessage() {
        String message = msgField.getText().toString();
        if (message != "") {
            // 리사이클러뷰에 내가 보낸 내용 추가
            mArrayList.add(new MessageContainer(message, "coming soon", "me"));
            mAdapter.notifyDataSetChanged();

            msgField.setText("");
            mRecyclerView.scrollToPosition(mArrayList.size() - 1);

            //데이터 전송 처리
            JSONObject data = new JSONObject();
            try {
                data.put("room_id", roomId);
                data.put("message", message);
                mSocket.emit("msg", data);
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }

    // 내가 보낸 메시지 이벤트
    // 소켓 서버에 커넥트 된 후  서버로부터 전달받은 socket.event_connect 이벤트 처리
    private Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            // 전달받은 데이터는 아래와 같이 추출 가능함
            SharedPreferences sf;
            sf = getSharedPreferences("sFile", MODE_PRIVATE);
            String user_id = sf.getString("user_id", "Anonymous");
            // 소켓에 연결되자마자 방에 join 시키기. 이로서 초기에 메시지가 받아지지 않던 문제 해결
            mSocket.emit("room_id_to_join", roomId);
            mSocket.emit("client_id", user_id);

        }
    };

    private Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            // 전달받은 데이터는 아래와 같이 추출 가능함
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "ChatRoomActivity : 연결 실패", Toast.LENGTH_SHORT).show();
                }
            });
        }
    };


    //내가 받는 메시지 이벤트
    //서버로 이벤트를 전송하는 부분에 추가할 수 있는 기능
    private Emitter.Listener onMessageReceived = new Emitter.Listener() {
        String message;
        String date = "";

        @Override
        public void call(Object... args) {
            try {
                receivedData = (JSONObject) args[0];
                message = receivedData.getString("msg");
                date = receivedData.getString("date");

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                        mArrayList.add(new MessageContainer(message, date, "opponent"));
                        mAdapter.notifyDataSetChanged();
                        mRecyclerView.scrollToPosition(mArrayList.size() - 1);
                    }
                });

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSocket.disconnect();
        mSocket.off(Socket.EVENT_CONNECT, onConnect);
        mSocket.off(Socket.EVENT_DISCONNECT, onConnectError);
        mSocket.off("msg_from_server", onMessageReceived);
    }

}
