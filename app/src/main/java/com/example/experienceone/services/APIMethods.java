package com.example.experienceone.services;

import com.example.experienceone.fragment.general.TicketDetails;
import com.example.experienceone.model.TravelModel;
import com.example.experienceone.model.dinningmodel.DinningSegmentModel;
import com.example.experienceone.model.foreignexchange.ForeignExchangemodel;
import com.example.experienceone.model.raisingticketmodel.ModuleSegmentModel;
import com.example.experienceone.pojo.GeneralPojo;
import com.example.experienceone.pojo.HouseKeeping.HouseKeepingPojo;
import com.example.experienceone.pojo.currencyexchnage.CurrencyExchange;

import com.example.experienceone.pojo.dinning.InRoomDinning;
import com.example.experienceone.pojo.doorunlock.Mobilekeys;
import com.example.experienceone.pojo.emergency.Emergency;
import com.example.experienceone.pojo.foreignexchange.ForeignExchangePojo;
import com.example.experienceone.pojo.GuestDeatils;
import com.example.experienceone.pojo.doorunlock.InvitationCode;
import com.example.experienceone.pojo.loginmpin.ActiveBooking;
import com.example.experienceone.pojo.multiplerooms.MultipleRoomNumber;
import com.example.experienceone.pojo.mystay.MyStayPojo;
import com.example.experienceone.pojo.posttickets.TicketID;
import com.example.experienceone.pojo.preference.CreateUpdatePrefrencesPojo;
import com.example.experienceone.pojo.preference.PreferencePojo;
import com.example.experienceone.pojo.sos.SoSDetails;
import com.example.experienceone.pojo.support.Support;
import com.example.experienceone.pojo.ticketdetails.TicketDetailsPojo;
import com.example.experienceone.pojo.dashboardelements.DashbordElements;
import com.example.experienceone.pojo.hoteldirectory.HotelDirectoryPojo;
import com.example.experienceone.pojo.loginmpin.LoginMpin;
import com.example.experienceone.pojo.navmenuitems.NavMenuItems;
import com.example.experienceone.pojo.ticketlist.TicketList;
import com.example.experienceone.pojo.tourguide.TourGuidePojo;
import com.example.experienceone.pojo.travel.TravelPojo;
import com.example.experienceone.pojo.userauthentication.AuthenticateMobile;
import com.example.experienceone.pojo.creatempin.CreateMpin;
import com.example.experienceone.pojo.wifi.Wifi;
import java.util.Map;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface APIMethods {

    @Headers({"Content-Type:application/json", "organization-key:86168e09b29b8d1b1bf4b5ea209fe2fcdaa21c90", "location:demo-location","login-type:guest"})
    @POST("exp-authenticate-mobile/")
    Call<AuthenticateMobile> userAuthentication(@Body Map map);

    @Headers({"Content-Type:application/json", "organization-key:86168e09b29b8d1b1bf4b5ea209fe2fcdaa21c90", "location:demo-location","login-type:guest"})
    @POST("exp-guest-authenticate-otp/")
    Call<AuthenticateMobile> verifyOTP(@Body Map map);

    @Headers({"Content-Type:application/json", "organization-key:86168e09b29b8d1b1bf4b5ea209fe2fcdaa21c90", "location:demo-location","login-type:guest"})
    @POST("create-mpin/")
    Call<CreateMpin> createMpin(@HeaderMap Map<String,String> headers,@Body Map map);

    @Headers({"Content-Type:application/json", "organization-key:86168e09b29b8d1b1bf4b5ea209fe2fcdaa21c90", "location:demo-location","login-type:guest"})
    @POST("login-guest/")
    Call<LoginMpin> loginMpin(@Body Map map);

    @Headers({"Content-Type:application/json", "organization-key:86168e09b29b8d1b1bf4b5ea209fe2fcdaa21c90", "location:demo-location","login-type:guest"})
    @POST("guest-resend-otp/")
    Call<AuthenticateMobile> resendOTP(@Body Map map);


    @Headers({"Content-Type:application/json", "organization-key:86168e09b29b8d1b1bf4b5ea209fe2fcdaa21c90", "location:demo-location","login-type:guest"})
    @GET("guest-dashbord-module-view/")
    Call<DashbordElements> getDashBoardElemnts(@HeaderMap Map<String,String> headers);

    @Headers({"Content-Type:application/json", "organization-key:86168e09b29b8d1b1bf4b5ea209fe2fcdaa21c90", "location:demo-location","login-type:guest"})
    @GET("guest-mobile-routes-category/")
    Call<NavMenuItems> getNavItems(@HeaderMap Map<String,String> headers);


    @Headers({"Content-Type:application/json", "organization-key:86168e09b29b8d1b1bf4b5ea209fe2fcdaa21c90", "location:demo-location","login-type:guest"})
    @GET("guest-wifi/")
    Call<Wifi> getWifiCred(@HeaderMap Map<String,String> headers);


    @Headers({"Content-Type:application/json", "organization-key:86168e09b29b8d1b1bf4b5ea209fe2fcdaa21c90", "location:demo-location","login-type:guest"})
    @GET("guest-directory-service/")
    Call<HotelDirectoryPojo> getHotelDirectory(@HeaderMap Map<String,String> headers);


    @Headers({"Content-Type:application/json", "organization-key:86168e09b29b8d1b1bf4b5ea209fe2fcdaa21c90", "location:demo-location","login-type:guest"})
    @GET
    Call<HouseKeepingPojo> getHouseKeeping(@Url String url, @HeaderMap Map<String, String> headers);


    @Headers({"Content-Type:application/json", "organization-key:86168e09b29b8d1b1bf4b5ea209fe2fcdaa21c90", "location:demo-location","login-type:guest"})
    @POST
    Call<TicketID> postSegmentModule(@Url String url, @HeaderMap Map<String,String> headers, @Body ModuleSegmentModel houseKeepingPojo);

    @Headers({"Content-Type:application/json", "organization-key:86168e09b29b8d1b1bf4b5ea209fe2fcdaa21c90", "location:demo-location","login-type:guest"})
    @GET("display-ticket/{id}/")
    Call<TicketDetailsPojo>  getticketDetails(@HeaderMap Map<String,String> headers , @Path("id") String id);

    @Headers({"Content-Type:application/json", "organization-key:86168e09b29b8d1b1bf4b5ea209fe2fcdaa21c90", "location:demo-location","login-type:guest"})
    @GET("guest-laundry-category/")
    Call<HouseKeepingPojo> getLaundry(@HeaderMap Map<String,String> headers);

    @Headers({"Content-Type:application/json", "organization-key:86168e09b29b8d1b1bf4b5ea209fe2fcdaa21c90", "location:demo-location","login-type:guest"})
    @GET("guest-emergency-service/")
    Call<Emergency> getEmergencyDashBoardElemnts(@HeaderMap Map<String,String> headers);


    @Headers({"Content-Type:application/json", "organization-key:86168e09b29b8d1b1bf4b5ea209fe2fcdaa21c90", "location:demo-location","login-type:guest"})
    @POST("emergency-service-book-ticket/")
    Call<TicketID> postEmergencyRequst(@HeaderMap Map<String,String> headers, @Body ModuleSegmentModel houseKeepingPojo);

   /* @GET("guest-dining/")
    Call<Dinning> getDinningElemnts(@HeaderMap Map<String,String> headers);*/

    @Headers({"Content-Type:application/json", "organization-key:86168e09b29b8d1b1bf4b5ea209fe2fcdaa21c90", "location:demo-location","login-type:guest"})
    @GET("guest-dining-category/")
    Call<InRoomDinning> getDinningElemnts(@HeaderMap Map<String,String> headers);


    @Headers({"Content-Type:application/json", "organization-key:86168e09b29b8d1b1bf4b5ea209fe2fcdaa21c90", "location:demo-location","login-type:guest"})
    @POST("dining-book-ticket/")
    Call<TicketID> postDinningSegment(@HeaderMap Map<String,String> headers, @Body DinningSegmentModel dinningSegmentModel);

    @Headers({"Content-Type:application/json", "organization-key:86168e09b29b8d1b1bf4b5ea209fe2fcdaa21c90", "location:demo-location","login-type:guest"})
    @GET("guest-car-category/")
    Call<TravelPojo> getTravelList(@HeaderMap Map<String,String> headers);

    @Headers({"Content-Type:application/json", "organization-key:86168e09b29b8d1b1bf4b5ea209fe2fcdaa21c90", "location:demo-location","login-type:guest"})
    @GET("guest-list-ticket/")
    Call<TicketList>  getticketList(@HeaderMap Map<String,String> headers);


    @Headers({"Content-Type:application/json", "organization-key:86168e09b29b8d1b1bf4b5ea209fe2fcdaa21c90", "location:demo-location","login-type:guest"})
    @GET("guest-master-currency/currencylist/")
    Call<ForeignExchangePojo>  getexchange(@HeaderMap Map<String,String> headers);


    @Headers({"Content-Type:application/json", "organization-key:86168e09b29b8d1b1bf4b5ea209fe2fcdaa21c90", "location:demo-location","login-type:guest"})
    @GET("guest-master-currency/currencylistbyId/")
    Call<ForeignExchangePojo>  getexchangeById(@HeaderMap Map<String,String> headers, @Query("id") Integer id);

    @Headers({"Content-Type:application/json", "organization-key:86168e09b29b8d1b1bf4b5ea209fe2fcdaa21c90", "location:demo-location","login-type:guest"})
    @POST("guest-currency-exchange/")
    Call<CurrencyExchange> postForeignExchnage(@HeaderMap Map<String,String> headers,@Body Map map);

    @Headers({"Content-Type:application/json", "organization-key:86168e09b29b8d1b1bf4b5ea209fe2fcdaa21c90", "location:demo-location","login-type:guest"})
    @POST
    Call<TicketID> postforeignExchange(@Url String url, @HeaderMap Map<String,String> headers, @Body ForeignExchangemodel houseKeepingPojo);


    @Headers({"Content-Type:application/json", "organization-key:86168e09b29b8d1b1bf4b5ea209fe2fcdaa21c90", "location:demo-location","login-type:guest"})
    @POST("car-book-ticket/")
    Call<TicketID> postTravelRequst(@HeaderMap Map<String, String> headers,@Body TravelModel model);

    @Headers({"Content-Type:application/json", "organization-key:86168e09b29b8d1b1bf4b5ea209fe2fcdaa21c90", "location:demo-location","login-type:guest"})
    @GET("guest-preference/")
    Call<PreferencePojo> getPrefrenceList(@HeaderMap Map<String,String> headers);

    @Headers({"Content-Type:application/json", "organization-key:86168e09b29b8d1b1bf4b5ea209fe2fcdaa21c90", "location:demo-location","login-type:guest"})
    @POST("guest-preference/")
    Call<CreateUpdatePrefrencesPojo> postCreatePrefrence(@HeaderMap Map<String,String> headers, @Body Map<String, String> map);

    @Headers({"Content-Type:application/json", "organization-key:86168e09b29b8d1b1bf4b5ea209fe2fcdaa21c90", "location:demo-location","login-type:guest"})
    @PUT("guest-preference/{id}/")
    Call<CreateUpdatePrefrencesPojo> putStatusPrefrence(@Path("id") String id, @HeaderMap Map<String, String> headers, @Body Map<String, String> map);


    @Headers({"Content-Type:application/json", "organization-key:86168e09b29b8d1b1bf4b5ea209fe2fcdaa21c90", "location:demo-location","login-type:guest"})
    @DELETE("guest-preference/{id}/")
    Call<GeneralPojo> deletePrefrence(@Path("id") String id, @HeaderMap Map<String, String> headers);

    @Headers({"Content-Type:application/json", "organization-key:86168e09b29b8d1b1bf4b5ea209fe2fcdaa21c90", "location:demo-location","login-type:guest"})
    @GET("guest-booking/")
    Call<MyStayPojo>  getBookingDetails(@HeaderMap Map<String,String> headers);

    @Headers({"Content-Type:application/json", "organization-key:86168e09b29b8d1b1bf4b5ea209fe2fcdaa21c90", "location:demo-location","login-type:guest"})
    @GET("guest-localtour-guide-place/")
    Call<TourGuidePojo>  getTourGuideList(@HeaderMap Map<String,String> headers);

    @Headers({"Content-Type:application/json", "organization-key:86168e09b29b8d1b1bf4b5ea209fe2fcdaa21c90", "location:demo-location","login-type:guest"})
    @PUT("guest/{id}/")
    Call<GuestDeatils> putProfileDeatils(@Path("id") String id, @HeaderMap Map<String, String> headers, @Body Map<String, String> map);

    @Headers({"Content-Type:application/json", "organization-key:86168e09b29b8d1b1bf4b5ea209fe2fcdaa21c90", "location:demo-location","login-type:guest"})
    @GET
    Call<TicketList>  getticketNextPage(@Url String url,@HeaderMap Map<String,String> headers);

    @Headers({"Content-Type:application/json", "organization-key:86168e09b29b8d1b1bf4b5ea209fe2fcdaa21c90", "location:demo-location","login-type:guest"})
    @POST("guest-sos/")
    Call<GeneralPojo>  triggerSOS(@HeaderMap Map<String,String> headers,@Body Map<String, String> map);

    @Headers({"Content-Type:application/json", "organization-key:86168e09b29b8d1b1bf4b5ea209fe2fcdaa21c90", "location:demo-location","login-type:guest"})
    @GET("guest-sos/")
    Call<SoSDetails> getGuestSoS(@HeaderMap Map<String, String> headerMap);


    @Headers({"Content-Type:application/json", "organization-key:86168e09b29b8d1b1bf4b5ea209fe2fcdaa21c90", "location:demo-location","login-type:guest"})
    @POST("guest/")
    Call<AuthenticateMobile> regUser(@Body Map<String, String> map);



    //Door Unlock

    @Headers({"Content-Type:application/json", "organization-key:86168e09b29b8d1b1bf4b5ea209fe2fcdaa21c90", "location:demo-location"})
    @POST("exp-guest-door-unlock")
    Call<InvitationCode> getInvitationCode(@Body Map map);

    @Headers({"Content-Type:application/json", "organization-key:86168e09b29b8d1b1bf4b5ea209fe2fcdaa21c90", "location:demo-location"})
    @POST("mobile-key-generation/")
    Call<Mobilekeys> mobilekeyapi(@Body Map map);

    @Headers({"Content-Type:application/json", "organization-key:86168e09b29b8d1b1bf4b5ea209fe2fcdaa21c90", "location:demo-location","login-type:guest"})
    @GET("active-booking/")
    Call<MultipleRoomNumber>  getRoomNumbers(@HeaderMap Map<String,String> headers);

    @Headers({"Content-Type:application/json", "organization-key:86168e09b29b8d1b1bf4b5ea209fe2fcdaa21c90", "location:demo-location","login-type:guest"})
    @GET("guest-support/")
    Call<Support>  getSupport(@HeaderMap Map<String,String> headers);

    @Headers({"Content-Type:application/json", "organization-key:86168e09b29b8d1b1bf4b5ea209fe2fcdaa21c90", "location:demo-location","login-type:guest"})
    @POST("support-book-ticket/")
    Call<TicketID> postSupport(@HeaderMap Map<String, String> headers, @Body ModuleSegmentModel houseKeepingModel);


}
