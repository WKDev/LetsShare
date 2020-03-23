package com.tbk.letsshare;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.tbk.letsshare.Comm_Data.ItemAddData;
import com.tbk.letsshare.Comm_Data.ItemAddResponse;
import com.tbk.letsshare.network.RetrofitClient;
import com.tbk.letsshare.network.ServiceApi;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ItemAddActivity extends AppCompatActivity {

    private final static int REQ_ALBUM = 107;
    private final static int PERMISSIONS_REQUEST_CODE = 200;
    private static final int CODE_REFRESH_LIST = 101;

    String[] REQUIRED_PERMISSIONS = {Manifest.permission.READ_EXTERNAL_STORAGE};

    private ServiceApi service;

    private EditText titleText;
    private EditText priceText;
    private EditText descText;
    private ImageView imageView;
    private Button submitButton;
    private Button selectImgBtn;
    private String imageUrl = "";
    private File tempFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_add);

        titleText = findViewById(R.id.itemadd_title);
        priceText = findViewById(R.id.itemadd_price);
        descText = findViewById(R.id.itemadd_description);
        submitButton = findViewById(R.id.itemadd_submit);
        selectImgBtn = findViewById(R.id.select_button);

        service = RetrofitClient.getClient().create(ServiceApi.class);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String title = titleText.getText().toString();
                String price = priceText.getText().toString();
                String description = descText.getText().toString();

                SharedPreferences sf = getSharedPreferences("sFile", MODE_PRIVATE);
                String writer = sf.getString("nickname", "nullError:checkoutItemAddActivity");
                addItemToDatabase(new ItemAddData(title, price, description, writer));
            }
        });

        selectImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyingPermission();
            }
        });


    }
    private void verifyingPermission() { //https://webnautes.tistory.com/1225 권한 설정 참조
//        int writeEXStoragePermission = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
//
//        if (writeEXStoragePermission == PackageManager.PERMISSION_GRANTED) { // 권한이 이미 수락되었다면 이미지 선택으로 넘어감
//            getImageFromStorage();
//        } else { // 권한이 수락되지 않았을 경우
//            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
//                //퍼미션 거부한 적이 있다면
//                Snackbar.make(getWindow().getDecorView().getRootView(), "이 앱을 실행하려면 카메라와 외부 저장소 접근 권한이 필요합니다.",
//                        Snackbar.LENGTH_INDEFINITE).setAction("확인", new View.OnClickListener() {
//
//                    @Override
//                    public void onClick(View view) {
//
//                        // 3-3. 사용자게에 퍼미션 요청을 합니다. 요청 결과는 onRequestPermissionResult에서 수신됩니다.
//                        ActivityCompat.requestPermissions(ItemAddActivity.this, REQUIRED_PERMISSIONS, PERMISSIONS_REQUEST_CODE);
//                    }
//                }).show();
//            }
//
//        }
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(ItemAddActivity.this,
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(ItemAddActivity.this, Manifest.permission.READ_CONTACTS)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(ItemAddActivity.this, REQUIRED_PERMISSIONS, PERMISSIONS_REQUEST_CODE);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
            getImageFromStorage();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        if (requestCode == PERMISSIONS_REQUEST_CODE && grantResults.length == REQUIRED_PERMISSIONS.length) {
//            // 요청한 퍼미션 개수만큼 수신되었다면
//            boolean check_result = true;
//            for (int result : grantResults) { // 모든 퍼미션 허용했는지 체크
//                if (result != PackageManager.PERMISSION_GRANTED) {
//                    check_result = false;
//                    break;
//                }
//            }
//
//            if (check_result) {
//                getImageFromStorage();
//            }
//        } else {
//            if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])) {
//                //거부한 퍼미션이 있다면
//
//                // 사용자가 거부만 선택한 경우에는 앱을 다시 실행하여 허용을 선택하면 앱을 사용할 수 있습니다.
//
//                Snackbar.make(getWindow().getDecorView().getRootView(), "퍼미션이 거부되었습니다. 앱을 다시 실행하여 퍼미션을 허용해주세요. ",
//                        Snackbar.LENGTH_INDEFINITE).setAction("확인", new View.OnClickListener() {
//
//                    @Override
//                    public void onClick(View view) {
//                        finish();
//                    }
//                }).show();
//
//            } else {
//
//
//                // “다시 묻지 않음”을 사용자가 체크하고 거부를 선택한 경우에는 설정(앱 정보)에서 퍼미션을 허용해야 앱을 사용할 수 있습니다.
//                Snackbar.make(getWindow().getDecorView().getRootView(), "퍼미션이 거부되었습니다. 설정(앱 정보)에서 퍼미션을 허용해야 합니다. ",
//                        Snackbar.LENGTH_INDEFINITE).setAction("확인", new View.OnClickListener() {
//
//                    @Override
//                    public void onClick(View view) {
//                        finish();
//                    }
//                }).show();
//            }
//
//        }

        switch (requestCode) {
            case PERMISSIONS_REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    getImageFromStorage();
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(getApplicationContext(), "permission denied, app terminated.", Toast.LENGTH_SHORT).show();
                    finish();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }


    private void getImageFromStorage() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, REQ_ALBUM);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQ_ALBUM) {
                Uri photoUri = data.getData();
                Cursor cursor = null;
                try {
                    //uri 스키마를 content:///에서 file:///로 변경하는 과정
                    String[] proj = {MediaStore.Images.Media.DATA};
                    assert photoUri != null;
                    cursor = getContentResolver().query(photoUri, proj, null, null, null);

                    assert cursor != null;
                    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

                    cursor.moveToFirst();

                    tempFile = new File(cursor.getString(column_index)); //받아온 이미지 저장
                } finally {
                    if (cursor != null) {
                        cursor.close();
                    }
                }
                setImage();
            }
        }
    }

    private void setImage() {
        imageView = findViewById(R.id.itemadd_img);
        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap originalBm = BitmapFactory.decodeFile(tempFile.getAbsolutePath(), options);
        imageUrl = tempFile.getAbsolutePath();
        imageView.setImageBitmap(originalBm);
    }

    private void uploadImg() {
        File file = new File(imageUrl);
        RequestBody requestfile = RequestBody.create(MediaType.parse("multipart/form-data"), file);

        MultipartBody.Part body = MultipartBody.Part.createFormData("upload_file", file.getName(), requestfile);

        RequestBody name = RequestBody.create(MediaType.parse("text/plain"), "test_text");

        Call<ItemAddResponse> resultCall = service.uploadImage(body, name);

        resultCall.enqueue(new Callback<ItemAddResponse>() {
            @Override
            public void onResponse(Call<ItemAddResponse> call, Response<ItemAddResponse> response) {
              //  Toast.makeText(getApplicationContext(), "서버로부터 응답받음", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ItemAddResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "서버 응답 없음", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void addItemToDatabase(ItemAddData data) {

        service.itemAdd(data).enqueue(new Callback<ItemAddResponse>() {
            @Override
            public void onResponse(Call<ItemAddResponse> call, Response<ItemAddResponse> response) {
                uploadImg();
                ItemAddResponse result = response.body();
                Toast.makeText(getApplicationContext(), result.getAddMessage(), Toast.LENGTH_SHORT).show();
                if (result.getCode() == 200) {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<ItemAddResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "게시 오류 발생", Toast.LENGTH_SHORT).show();

            }
        });
    }


}