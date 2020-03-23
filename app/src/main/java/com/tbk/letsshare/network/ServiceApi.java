package com.tbk.letsshare.network;

import com.tbk.letsshare.Comm_Data.ChatRoomReq;
import com.tbk.letsshare.Comm_Data.ChatRoomRes;
import com.tbk.letsshare.Comm_Data.ItemAddData;
import com.tbk.letsshare.Comm_Data.ItemAddResponse;
import com.tbk.letsshare.Comm_Data.ItemDataResponse;
import com.tbk.letsshare.Comm_Data.JoinData;
import com.tbk.letsshare.Comm_Data.JoinResponse;
import com.tbk.letsshare.Comm_Data.LoginData;
import com.tbk.letsshare.Comm_Data.LoginResponse;
import com.tbk.letsshare.Comm_Data.LogoutReq;
import com.tbk.letsshare.Comm_Data.LogoutRes;
import com.tbk.letsshare.Comm_Data.SearchRequest;
import com.tbk.letsshare.Comm_Data.SearchResult;
import com.tbk.letsshare.Comm_Data.verifyStateReq;
import com.tbk.letsshare.Comm_Data.verifyStateRes;


import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ServiceApi {

    @POST("/user/join")
    Call<JoinResponse> userJoin(@Body JoinData data);

    @POST("/user/board")
    Call<ItemAddResponse> itemAdd(@Body ItemAddData data);

    @GET("/user/inquiredata")
    Call<List<ItemDataResponse>> importItem();

    @POST("/user/login")
    Call<LoginResponse> userLogin(@Body LoginData data);

    @POST("/user/logout")
    Call<LogoutRes> userLogout(@Body LogoutReq data);

    @POST("/user/search")
    Call<List<SearchResult>> searchItem(@Body SearchRequest data);

    @POST("/user/state")
    Call<verifyStateRes> changeState(@Body verifyStateReq data);

//https://androidclarified.com/android-image-upload-example/
    // Retrofit2에서 인터페이스 메서드는 애플리케이션와 상호작용하는 api를 정의하는 데 쓰인다. 여기에서는 ServiceApi 가 해당된다/
    //이미지를 업로드하기 위해 필요한 Multipart와 part api를 정의한다.
//    @Multipart //requestbody(요청하는 형태)가 multi-part임을 denote한다. 파라미터로서 @Part가 선언 및 어노테이션 되어야 ㅎ나다.
//    @POST("user/item/image/")
//    Call<Response> uploadImage(@Part MultipartBody.Part image,  @Part("name") RequestBody requestBody);
//    multipart 형태의 요청에 개별 부분을 denote한다/ part의 파라미터 형태는 아래처럼 세 가지 형태 로 처리된다/
//    파라미터 타입이 MultipartBody.Part인 경우 컨텐츠는 직접적으로 쓰이며, 이름은 어노테이션과 같이 쓰일 필요 없다.@Part MultipartBody.Part part
//    타입이 RequestBody인 경우 값은 컨텐츠 형과 함께 직접적으로 같이 쓰인다. 어노테이션 안에 part를 추가해야한다.@Part("foo") RequestBody foo).
//    다른 오브젝트는 converter를 사용함으로서 적절한 표현으로 바뀔 것이다. 마찬가지로 어노테이션 안에 part 이름을 추가해줘야 한다.@Part("foo") Image photo
//
//    step4. 이미지 업로드하기
//    이 메서드에서 입력 매개변수는 단순한 이미지 파일 경로다. 이미 알겠지만, 갤러리나 카메라에서 어떻게 파일의 경로를 알아내는지 배우고 싶다면 이 튜토리얼을 읽어라
//    파일 경로를 사용해서 우리는 파일 객체를 만들고 이미지파일을 가진 multipartbody를 만들 것이다. requestbody에서 우리는 아래 코드 스니펫에서 보듯
//    파일 이름과 파트 이름을 MultipartBody.Part를

    @POST("/chat/chatroom")
    Call<List<ChatRoomRes>> chatRoom(@Body ChatRoomReq data);


    @Multipart
    @POST("/upload")
    Call<ItemAddResponse> uploadImage(@Part MultipartBody.Part image, @Part("upload") RequestBody name);
}
