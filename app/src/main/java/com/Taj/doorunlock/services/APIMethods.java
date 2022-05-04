package com.taj.doorunlock.services;


import com.taj.doorunlock.pojo.GeneralPojo;
import com.taj.doorunlock.pojo.doorunlock.DoorUnlock;
import com.taj.doorunlock.pojo.doorunlock.KeyGeneration;
import com.taj.doorunlock.pojo.doorunlock.LegicToken;


import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.HeaderMap;
import retrofit2.http.Headers;
import retrofit2.http.POST;


public interface APIMethods {

    @Headers({"x-api-key:23894-73894-79382-47293-42384"})
    @POST("signin/signup")
    Call<GeneralPojo> userRegistration(@Body Map map);

    @Headers({"x-api-key:23894-73894-79382-47293-42384"})
    @POST("signin/")
    Call<GeneralPojo> userAuthentication(@Body Map map);

    @Headers({"x-api-key:23894-73894-79382-47293-42384"})
    @POST("signin/verifyotp/")
    Call<GeneralPojo> verifyOTP(@Body Map map);

    @Headers({"x-api-key:23894-73894-79382-47293-42384"})
    @POST("signin/setmpin/")
    Call<GeneralPojo> createMpin(@Body Map map);

    @Headers({"x-api-key:23894-73894-79382-47293-42384"})
    @POST("signin/loginwithmpin/")
    Call<GeneralPojo> loginMpin(@Body Map map);

    @Headers({"x-api-key:23894-73894-79382-47293-42384"})
    @POST("signin/resendotp/")
    Call<GeneralPojo> resendOTP(@Body Map map);

    @Headers({"x-api-key:23894-73894-79382-47293-42384"})
    @POST("mobilekey/reservation")
    Call<GeneralPojo> getBookingDetails(@Body Map map);


    @Headers({"x-api-key:23894-73894-79382-47293-42384"})
    @POST("signin/logout/")
    Call<GeneralPojo> logout(@Body Map map);

/*    @Headers({"Content-Type:application/json", "organization-key:f0a32d93e9852ebb870ba86dc21480c3ff84e11d", "location:taj-exotica", "login-type:guest"})
    @POST("guest/")
    Call<AuthenticateMobile> regUser(@Body Map<String, String> map);*/

    //Door Unlock assabloy
    @Headers({"x-api-key:23894-73894-79382-47293-42384"})
    @POST("mobilekey/invitationcode")
    Call<DoorUnlock> getInvitationCode(@Body Map<String, String> map);

    @Headers({"x-api-key:23894-73894-79382-47293-42384"})
    @POST("mobilekey/mobilekey")
    Call<DoorUnlock> mobilekeyapi(@Body Map<String, String> map);



    //doormakaba door unlock

    @Headers({"x-api-key:23894-73894-79382-47293-42384"})
    @POST("mobilekey/dormakaba_device_regisration")
    Call<LegicToken> getToken(@Body HashMap map);

    @Headers({"x-api-key:23894-73894-79382-47293-42384"})
    @POST("mobilekey/dormakaba_mobilekey")
    Call<GeneralPojo> getkeyfiles(@Body HashMap map);

}
