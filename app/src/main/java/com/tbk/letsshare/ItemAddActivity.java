package com.tbk.letsshare;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.tbk.letsshare.Comm_Data.ItemAddData;
import com.tbk.letsshare.Comm_Data.ItemAddResponse;
import com.tbk.letsshare.network.RetrofitClient;
import com.tbk.letsshare.network.ServiceApi;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class ItemAddActivity extends AppCompatActivity {

    private ServiceApi service;

    private EditText titleText;
    private EditText priceText;
    private EditText descText;
    private Button submitButton;
    private ImageView itemImage;

    private String mImageUrl = "";

    private final int PICK_IMAGE = 200;


    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList<>();
    private ArrayList<String> permissions = new ArrayList<>();
    Uri picUri;


    private final static int GALLERY_REQUEST_CODE = 107;
    private final static int MY_PERMISSIONS_REQUEST_READ_EX_STORAGE = 200;
    private final static int ALL_PERMISSIONS_RESULT = 107;
    private final static int IMAGE_RESULT = 200;

    FloatingActionButton fabCamera, fabUpload;
    Bitmap mBitmap;
    TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_add);

        itemImage = findViewById(R.id.itemadd_img);
        titleText = (EditText) findViewById(R.id.itemadd_title);
        priceText = (EditText) findViewById(R.id.itemadd_price);
        descText = (EditText) findViewById(R.id.itemadd_description);
        submitButton = (Button) findViewById(R.id.itemadd_submit);

        reqPermission();


        service = RetrofitClient.getClient().create(ServiceApi.class);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String title = titleText.getText().toString();
                String price = priceText.getText().toString();
                String description = descText.getText().toString();

                SharedPreferences sf = getSharedPreferences("sFile", MODE_PRIVATE);
                String writer = sf.getString("writer", "nullError:checkoutItemAddActivity");

                addItemToDatabase(new ItemAddData(title, price, description, writer));

            }
        });


        itemImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, PICK_IMAGE);
            }
        });

        askPermissions();
        initRetrofitClient();


    }

    private void askPermissions() {
        permissions.add(WRITE_EXTERNAL_STORAGE);
        permissions.add(READ_EXTERNAL_STORAGE);

        permissionsToRequest = findUnAskedPsermissions(permissions);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (permissionsToRequest.size() > 0) {
                requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
            }
        }
    }

    public Intent getPickImageChooserIntent() {
        Uri outputFileUri = getCaptureImageOutputUri();
        List<Intent> allIntents = new ArrayList<>();
        PackageManager packageManager = getPackageManager();

        Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
        for (ResolveInfo res : listCam) {
            Intent intent = new Intent(captureIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            allIntents.add(intent);
        }
        Intent mainIntent = allIntents.get(allIntents.size() - 1);
        for (Intent intent : allIntents) {
            if (intent.getComponent().getClassName().equals("com.android.documentsui.DocumentsActivity")) {
                mainIntent = intent;
                break;
            }
        }
        allIntents.remove(mainIntent);

        Intent chooserIntent = Intent.createChooser(mainIntent, "select source");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, allIntents.toArray(new Parcelable[allIntents.size()]));

        return chooserIntent;
    }

    private Uri getCaptureImageOutputUri() {
        Uri outputFileUri = null;
        File getImage = getExternalFilesDir("");
        if (getImage != null) {
            outputFileUri = Uri.fromFile(new File(getImage.getPath(), "profile.png"));
        }
        return outputFileUri;
    }

    private void addItemToDatabase(ItemAddData data) {

        service.itemAdd(data).enqueue(new Callback<ItemAddResponse>() {
            @Override
            public void onResponse(Call<ItemAddResponse> call, Response<ItemAddResponse> response) {
                ItemAddResponse result = response.body();
                Toast.makeText(getApplicationContext(), result.getAddMessage(), Toast.LENGTH_LONG).show();
                if (result.getCode() == 200) {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<ItemAddResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "게시 오류 발생", Toast.LENGTH_LONG).show();

            }
        });
    }

    @Override
    // 이미지 선택 결과 핸들
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

//        if (requestCode == PICK_IMAGE) {
//            if (resultCode == RESULT_OK) {
//                try {
//                    InputStream in = getContentResolver().openInputStream(data.getData());
//                    Bitmap img = BitmapFactory.decodeStream(in);
//                    in.close();
//                    itemImage.setImageBitmap(img);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }
        //https://youngest-programming.tistory.com/54 참조함
        // 유저가 이미지를 선택한 경우에만 요청 코드 RESULT_OK를 반환한다.
//        if (requestCode == RESULT_OK) {
//            switch (requestCode) {
//                case GALLERY_REQUEST_CODE:
//                    //data.getData는 선택한 이미지의 uri를 반환한다.
//                    Uri selectedImage = data.getData();
//                    itemImage.setImageURI(selectedImage);
//
//                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
//                    //cursor 받기
//                    Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null,null,null);
//                    //첫번째 행으로 이동
//                    cursor.moveToFirst();
//                    //MediaStore.Images.Media.DATA 가 존재하는 컬럼의 인덱스 구하기
//                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//                    //컬럼에 있는 문자열 구하기
//                    String imgDecodableString= cursor.getString(columnIndex);
//
//                    cursor.close();
//
//                    itemImage.setImageBitmap(BitmapFactory.decodeFile(imgDecodableString));
//
//                    break;
//            }

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == IMAGE_RESULT) {
                String filePath = getImageFilePath(data);
                if (FilePath != null) {
                    mBitmap = BitmapFactory.decodeFile(filePath);
                    imageView.setImageBitmap(mBitmap);
                }
            }
        }
    } // 이전 아이템

    private String getImageFromFilePath(Intent data) {
        Boolean data.getData() == null;
        return getPathFromURI(data.getData());
    }

    public String getImageFilePath(Intent data) {
        return getImageFromFilePath(data);
    }

    private String getPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Audio.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("pic_uri", picUri);

    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        picUri = savedInstanceState.getParcelable("pic_uri");
    }

    private ArrayList<String> findUnAskedPsermissions(ArrayList<String> wanted) {
        ArrayList<String> result = new ArrayList<String>();

        for (String perm : wanted) {
            if (!hasPermission(perm)) {
                result.add(perm);
            }
        }
        return result;
    }

    public boolean hasPermission(String permission) {
        if (canMakeSmores()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true;
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    private boolean canMakeSmores() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    @TargetApi(Build.VERSION_CODES.M)

    @Override //권한 요청 결과 핸들
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //MY_PERMISSIONS_REQUEST_READ_EX_STORAGE 값이 여기 전달된다/
        switch (requestCode) {
            case ALL_PERMISSIONS_RESULT: {
                // 권한이 수락되었으니 여기서 할 일을 하면 된다.
                for (String perms : permissionsToRequest) {
                    if (!hasPermission(perms)) {
                        permissionsRejected.add(perms);
                    }
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (shouldShowRequestPermissionRationale(permissionsRejected.get(0))) {
                        showMessageOKCancel("These permissions are mandatory for th application. please allow access",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        requestPermissions(permissionsRejected.toArray(new String[permissionsRejected.size()]), ALL_PERMISSIONS_RESULT);
                                    }
                                });
                        return;

                    }
                }
                //나머지 케이스 라인에서 이 앱이 요청하는 다른 권한 관련 동작을 수행하면 된다/
                break;
            }
        }
    }// 이전 아이템

    private void multipartImageUpload() {
        try {
            File filesDir = getApplicationContext().getFilesDir();
            File file = new File(filesDir, "image" + ".png");

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            mBitmap.compress(Bitmap.CompressFormat.PNG, 0, bos);
            byte[] bitmapdata = bos.toByteArray();

            FileOutputStream fos = new FileOutputStream();
            fos.write(bitmapdata);
            fos.flush();
            fos.close();

            RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
            MultipartBody.Part body = MultipartBody.Part.createFormData("upload", file.getName(), reqFile);
            RequestBody name = RequestBody.create(MediaType.parse("text/plain"), "upload");

            Call<ResponseBody> req = service.uploadImage(body, name);

            req.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if(response.code() == 200){
                        Snackbar.make(getWindow().getDecorView().getRootView(), "upload successfully", Snackbar.LENGTH_SHORT);
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Snackbar.make(getWindow().getDecorView().getRootView(), "request failed", Snackbar.LENGTH_SHORT).show();
                }
            });

        }
    }

    //-----------------------------------------------------------------------------------------------------
    private void uploadItemImage(String filePath) {
        // https://androidclarified.com/android-image-upload-example/ 참조
        //https://androidclarified.com/pick-image-gallery-camera-android/ 이미지 삽입하는법
        Retrofit retrofit = RetrofitClient.getImageClient(this);
        ServiceApi service = retrofit.create(ServiceApi.class);
        //파일 경로를 사용하는 파일 객체 생성
        File file = new File(filePath);

        // 파일과 이미지 미디어 타입을 사용하는 requestbody 생성
        RequestBody fileReqBody = RequestBody.create(MediaType.parse("image/*"), file);

        // file request-boby, 파일 이름, part 이름을 사용하는 MultipartBody.Part 생성
        MultipartBody.Part part = MultipartBody.Part.createFormData("upload", file.getName(), fileReqBody);

        //텍스트 미디어 타입과 텍스트 설명을 사용하는 requestpart 생성
        RequestBody description = RequestBody.create(MediaType.parse("text/plain"), "image-type");

        Call call = service.uploadImage(part, description);

        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {

            }

            @Override
            public void onFailure(Call call, Throwable t) {

            }
        });

    } // 이전 아이템

//    @Override //권한 요청 결과 핸들
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        //MY_PERMISSIONS_REQUEST_READ_EX_STORAGE 값이 여기 전달된다/
//        switch (requestCode) {
//            case MY_PERMISSIONS_REQUEST_READ_EX_STORAGE: {
//                // 권한이 수락되었으니 여기서 할 일을 하면 된다.
//            }
//            //나머지 케이스 라인에서 이 앱이 요청하는 다른 권한 관련 동작을 수행하면 된다/
//        }
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//    } // 이전 아이템


    // 권한 요청
    private void reqPermission() {
        //https://developer.android.com/training/permissions/requesting#java 권한설정에 대한 참조
        if (ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            //권한 수락되지 않은 상태일 경우
            // 설명을 보여줘야 합니까?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, READ_EXTERNAL_STORAGE)) {
                //유저에게 연동되어 설명을 보여줌 --- 유저의 응답을 기다리는 이 스레드를 차단하지 마라, 유저가 설명을 본 뒤, 그 권한을 다시 요청하라
            } else {
                // 설명이 필요 없다 권한을 요청하라
                ActivityCompat.requestPermissions(this, new String[]{READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EX_STORAGE);
                // 콜백 메서드에서 MY_PERMISSIONS_REQUEST_READ_EX_STORAGE를 받아 요청의 결과를 처리한다.
            }
        } else {
            // 권한이 이미 수락되었다.
        }
    } // 이전 아이템

    // 갤러리에서 이미지 선택
    private void pickFromGallery() {
        //ACTION_PICK을 수행하는 인텐트를 만든다
        Intent intent = new Intent(Intent.ACTION_PICK);
        //선택하는 타입을 이미지로 설정한다. 이렇게 하면 이미지만 선택된다.
        intent.setType("image/*");
        // 추가적인 배열로 승인된mime 형태를 전달받는다. 이는 목표했던 mime 형태만 받도록 보장한다/
        String[] mimeTypes = {"image/jpeg", "image/png"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        //인텐트를 실행한다
        startActivityForResult(intent, GALLERY_REQUEST_CODE);

    } // 이전 아이템

    //https://k9327.tistory.com/5 참조

//    public byte[] getBytes(InputStream is) throws IOException {
//        ByteArrayOutputStream byteBuff = new ByteArrayOutputStream();
//
//        int buffSize = 1024;
//        byte[] buff = new byte[buffSize];
//
//        int len = 0;
//        while ((len = is.read(buff)) != -1) {
//            byteBuff.write(buff, 0, len);
//        }
//
//        return byteBuff.toByteArray();
//    }
//
//    private void uploadImage(byte[] imageBytes) {
//
//        //https://www.learn2crack.com/2017/08/upload-image-using-retrofit.html 참조
//
////        Retrofit retrofit = new Retrofit.Builder()
////                .baseUrl(URL)
////                .addConverterFactory(GsonConverterFactory.create())
////                .build();
////
////        RetrofitInterface retrofitInterface = retrofit.create(RetrofitInterface.class);
////
//
//        RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), imageBytes);
//
//        MultipartBody.Part body = MultipartBody.Part.createFormData("image", "image.jpg", requestFile);
//
//        service.uploadImage(body).enqueue(new Callback<ItemAddResponse>() {
//            @Override
//            public void onResponse(Call<ItemAddResponse> call, Response<ItemAddResponse> response) {
//
//                if (response.isSuccessful()) {
//                    ItemAddResponse responseBody = response.body();
//                    itemImage.setVisibility(View.VISIBLE);
//                    mImageUrl = "http://ec2-13-209-22-0.ap-northeast-2.compute.amazonaws.com:3000" + responseBody.getPath();
//                    Snackbar.make(findViewById(R.id.content), responseBody.getMessage(), Snackbar.LENGTH_SHORT).show();
//
//                } else {
//                    ItemAddResponse errorBody = response.errorBody();
//                    Gson gson = new Gson();
//
//                    try {
//
//                        Response errorResponse = gson.fromJson(errorBody.string(), Response.class);
//                        Snackbar.make(findViewById(R.id.content), errorResponse.getMessage(), Snackbar.LENGTH_SHORT).show();
//
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ItemAddResponse> call, Throwable t) {
//                    mProgressBar.setVisibility(View.GONE);
//                    Log.d(TAG, "onFailure: " + t.getLocalizedMessage());
//
//            }
//        });
//    }
}
