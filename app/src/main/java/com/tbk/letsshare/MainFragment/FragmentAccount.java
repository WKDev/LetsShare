package com.tbk.letsshare.MainFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.tbk.letsshare.MainActivity;
import com.tbk.letsshare.R;

import org.json.JSONException;
import org.json.JSONObject;

import static java.sql.DriverManager.println;

public class FragmentAccount extends Fragment {
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


            ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_home, container, false);

            RecyclerView mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_itemlist);
            LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getActivity());
            mRecyclerView.setHasFixedSize(true);
            mRecyclerView.setLayoutManager(mLinearLayoutManager);




            ViewGroup guestView = (ViewGroup)inflater.inflate(R.layout.fragment_login, container, false);
            ViewGroup memberView = (ViewGroup)inflater.inflate(R.layout.fragment_account, container, false);

            final TextView tinyConsole = (TextView)guestView.findViewById(R.id.signin_txt);
            final EditText id= (EditText)guestView.findViewById(R.id.username);
            final EditText pw= (EditText)guestView.findViewById(R.id.password);
            final String publicDNS = getResources().getString(R.string.aws_public_dns);
            Button signIn = (Button)guestView.findViewById(R.id.login);

            boolean LoginState = false;

            signIn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String url = publicDNS;
                    //JSON형식으로 데이터 통신을 진행합니다!
                    JSONObject testjson = new JSONObject();
                    try {
                        //입력해둔 edittext의 id와 pw값을 받아와 put해줍니다 : 데이터를 json형식으로 바꿔 넣어주었습니다.
                        testjson.put("id", id.getText().toString());
                        testjson.put("password", pw.getText().toString());
                        String jsonString = testjson.toString(); //완성된 json 포맷

                        //이제 전송해볼까요?
                        final RequestQueue requestQueue = Volley.newRequestQueue(getContext());
                        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url,testjson, new Response.Listener<JSONObject>() {

                            //데이터 전달을 끝내고 이제 그 응답을 받을 차례입니다.
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    println("데이터전송 성공");

                                    //받은 json형식의 응답을 받아
                                    JSONObject jsonObject = new JSONObject(response.toString());

                                    //key값에 따라 value값을 쪼개 받아옵니다.
                                    String resultId = jsonObject.getString("approve_id");
                                    String resultPassword = jsonObject.getString("approve_pw");

                                    //만약 그 값이 같다면 로그인에 성공한 것입니다.
                                    if(resultId.equals("OK") & resultPassword.equals("OK")){

                                        //이 곳에 성공 시 화면이동을 하는 등의 코드를 입력하시면 됩니다.
                                    }else{
                                        //로그인에 실패했을 경우 실행할 코드를 입력하시면 됩니다.
                                        println("조짐");
                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                    println("오류뜸");

                                }
                            }
                            //서버로 데이터 전달 및 응답 받기에 실패한 경우 아래 코드가 실행됩니다.
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                error.printStackTrace();
                                //Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                            }
                        });
                        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                        requestQueue.add(jsonObjectRequest);
                        //
                    } catch (
                            JSONException e) {
                        e.printStackTrace();
                    }



                }
            });

            if(!LoginState){
                return guestView;

            }
            else{
                return memberView;
            }
        }


}

