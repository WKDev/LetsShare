package com.tbk.letsshare;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.tbk.letsshare.Comm_Data.ItemAddData;
import com.tbk.letsshare.Comm_Data.ItemAddResponse;
import com.tbk.letsshare.Comm_Data.LoginResponse;
import com.tbk.letsshare.network.RetrofitClient;
import com.tbk.letsshare.network.ServiceApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ItemAddActivity extends AppCompatActivity {

    private ServiceApi service;

    private EditText titleText;
    private EditText priceText;
    private EditText descText;
    private Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_add);

        titleText = (EditText) findViewById(R.id.itemadd_title);
        priceText = (EditText) findViewById(R.id.itemadd_price);
        descText = (EditText) findViewById(R.id.itemadd_description);
        submitButton = (Button) findViewById(R.id.itemadd_submit);

        service = RetrofitClient.getClient().create(ServiceApi.class);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String title = titleText.getText().toString();
                String price = priceText.getText().toString();
                String description = descText.getText().toString();
                String writer = "tbk_test";

                addItemToDatabase(new ItemAddData(title, price, description, writer));
            }
        });
    }

    private void addItemToDatabase(ItemAddData data) {

        service.itemAdd(data).enqueue(new Callback<ItemAddResponse>() {
            @Override
            public void onResponse(Call<ItemAddResponse> call, Response<ItemAddResponse> response) {
                ItemAddResponse result = response.body();
                Toast.makeText(getApplicationContext(), result.getAddMessage(), Toast.LENGTH_LONG).show();
                if (result.getCode() == 200) {
                    finish();
                }
            }

            @Override
            public void onFailure(Call<ItemAddResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "게시 오류 발생", Toast.LENGTH_LONG).show();

            }
        });
    }
}
