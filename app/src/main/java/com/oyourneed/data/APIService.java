package com.oyourneed.data;


import com.oyourneed.model.CityListModel;
import com.oyourneed.model.CountryListModel;
import com.oyourneed.model.DistrictModel;
import com.oyourneed.model.ForgotModel;
import com.oyourneed.model.OtpModel;
import com.oyourneed.model.UpdateProfile;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface APIService {

    //The login call
    @FormUrlEncoded
    @POST("login.php")
    Call<ResponseResult> callSignIn(
            @Field("key") String key,
            @Field("mobile_no") String mobile_no,
            @Field("password") String password,
            @Field("gcm_code") String gcm_code);


    //check version code
    @FormUrlEncoded
    @POST("version_update.php")
    Call<ResponseResult> card_Versioncode(
            @Field("key") String key,
            @Field("version_code") String version_code);


  //check version code
    @FormUrlEncoded
    @POST("order_place.php")
    Call<ResponseResult> ordercod(
            @Field("key") String key,
            @Field("user_id") String user_id,
            @Field("place_name") String place_name,
            @Field("order_no") String order_no,
            @Field("place_address") String place_address,
            @Field("place_mobileno") String place_mobileno
            );

 @FormUrlEncoded
    @POST("support.php")
    Call<ResponseResult> support(
            @Field("key") String key);



    @FormUrlEncoded
    @POST("mobile_verify.php")
    Call<ResponseResult> mobileVerification(
            @Field("key") String key,
            @Field("mobile_no") String mobile_no);

    @FormUrlEncoded
    @POST("check_opt.php")
    Call<OtpModel> checkotp(
            @Field("key") String key,
            @Field("mobile_no") String mobile_no);

    @FormUrlEncoded
    @POST("forget.php")
    Call<ForgotModel> forgotMain(
            @Field("key") String key,
            @Field("mobile_no") String mobile_no);


    @FormUrlEncoded
    @POST("order_wise_product.php")
    Call<ResponseResult> orderWProductList(
            @Field("key") String key,
            @Field("user_id") String user_id,
            @Field("order_no") String order_no
    );

    @FormUrlEncoded
    @POST("show_slider.php")
    Call<ResponseResult> callSlider(
            @Field("key") String key);

    @FormUrlEncoded
    @POST("get_work_city_list")
    Call<ResponseResult> callCityList(
            @Field("key") String key,
            @Field("language_id") String language_id
    );

    @FormUrlEncoded
    @POST("product_details.php")
    Call<ResponseResult> callProductWList(
            @Field("key") String key,
            @Field("category_id") String category_id
           // @Field("language_id") String language_id
    );

    @FormUrlEncoded
    @POST("forgot_otp_varification.php")
    Call<ResponseResult> callForgotPassword(
            @Field("key") String key,
            @Field("OTP") String OTP,
            @Field("user_id") String user_id,
            @Field("new_password") String new_password,
            @Field("mobile_no") String mobile_no
    );

    @FormUrlEncoded
    @POST("country.php")
    Call<CountryListModel> get_country(
            @Field("key") String key
    );

    @FormUrlEncoded
    @POST("get_language_list")
    Call<ResponseResult> getLanguageList(
            @Field("key") String key
    );

 @FormUrlEncoded
    @POST("view_order.php")
    Call<ResponseResult> getOrderList(
            @Field("key") String key,
            @Field("user_id") String user_id
    );


    @FormUrlEncoded
    @POST("user_update.php")
    Call<UpdateProfile> updateProfile(
            @Field("key") String key,
            @Field("mobile_no") String mobile_no,
            @Field("gcm_code") String gcm_code,
            @Field("first_name") String first_name,
            @Field("last_name") String last_name,
            @Field("country") String country,
            @Field("state") String state,
            @Field("city") String city,
            @Field("address") String address,
            @Field("pincode") String pincode,
            @Field("email_id") String email_id,
            @Field("dob") String dob,
            @Field("reg_id") String reg_id
            );



    //category list
 @FormUrlEncoded
    @POST("category_details.php")
    Call<ResponseResult> callCategories(
            @Field("key") String key
           // @Field("language_id") String language_id
 );



 //sub category list
 @FormUrlEncoded
    @POST("get_product_categorywise")
    Call<ResponseResult> callProductList(
            @Field("key") String key,
            @Field("catergory_id") String catergory_id
            );

   @FormUrlEncoded
   @POST("user_registration.php")
   Call<ResponseResult> callRegister(
           @Field("key") String key,
           @Field("mobile_no") String mobile_no,
           @Field("password") String password
   );

   //The filters_fragment call


    //The OTP verification for new filters_fragment call
    @FormUrlEncoded
    @POST("otp_varification")
    Call<ResponseResult> callOtpVerificationRegister(
            @Field("key") String key,
            @Field("mobile_no") String mobile_no,
            @Field("password") String password,
            @Field("otp") String otp);

    //The OTP verification for new password call
    @FormUrlEncoded
    @POST("forgot_otp_varification")
    Call<ResponseResult> callOtpVerification(
            @Field("key") String key,
            @Field("mobile_no") String mobile_no,
            @Field("password") String password,
            @Field("otp") String otp);

    //The OTP resend call
    @FormUrlEncoded
    @POST("resend_otp")
    Call<ResponseResult> callResendOTP(
            @Field("key") String key,
            @Field("mobile_no") String mobile_no);

    //The forgot password call
    @FormUrlEncoded
    @POST("forgot_change_password")
    Call<ResponseResult> callForgotPassword(
            @Field("key") String key,
            @Field("reg_id") String reg_id,
            @Field("new_password") String new_password
            );

    //The change password call
    @FormUrlEncoded
    @POST("ChangePass.php")
    Call<ResponseResult> callChangePassword(
            @Field("key") String key,
            @Field("reg_id") String id,
           // @Field("mobile_no") String mobile_no,
           // @Field("gcm_code") String gcm_code,
           // @Field("old_password") String strOldPassword,
            @Field("new_password") String strNewPassword);

    //
    //working city list
    @FormUrlEncoded
    @POST("get_work_city_list")
    Call<ResponseResult> callWorkingCityList(
            @Field("key") String key,
            @Field("language_id") String language_id);



    //The city locality call
    @FormUrlEncoded
    @POST("get_locality")
    Call<ResponseResult> callLocalityList(
            @Field("key") String key,
            @Field("work_city_id") String workspaceCityId,
            @Field("language_id") String language_id
            );

    //The category call
    @FormUrlEncoded
    @POST("get_category")
    Call<ResponseResult> callCategoryTab(
            @Field("key") String key);

    //The feedback call
    @FormUrlEncoded
    @POST("customer_feedback")
    Call<ResponseResult> callSendFeedback(
            @Field("key") String key,
            @Field("rating") String strRating,
            @Field("suggesstion") String strFeedBack,
            @Field("login_id") String id);


    //The workspace call
    @FormUrlEncoded
    @POST("get_workspace_list")
    Call<ResponseResult> callWorkspaceList(
            @Field("key") String key,
            @Field("work_city_id") String intCityId,
            @Field("locality_id") String intLocalityId,
            @Field("category_id") String intCategoryId,
            @Field("page") String pages);

    //The user booking history call
    @FormUrlEncoded
    @POST("get_booking_history_by_id")
    Call<ResponseResult> callBookingHistoryList(
            @Field("key") String key,
            @Field("login_id") String userId);

    //The user book now  call
    @FormUrlEncoded
    @POST("add_booking")
    Call<ResponseResult> callSubmitBooking(
            @Field("key") String key,
            @Field("work_space_id") String work_space_id,
            @Field("work_space_name") String work_space_name,
            @Field("customer_id") String customer_id,
            @Field("customer_name") String customer_name,
            @Field("customer_email_id") String customer_email_id,
            @Field("customer_mobile_no") String customer_mobile_no,
            @Field("desk_id") String desk_id,
            @Field("desk_qty") String desk_qty,
            @Field("book_start_time") String book_start_time,
            @Field("book_end_time") String book_end_time,
            @Field("book_hours") String book_hours,
            @Field("book_day") String book_day,
            @Field("book_month") String book_month,
            @Field("book_three_month") String book_three_month,
            @Field("book_six_month") String book_six_month,
            @Field("book_year") String book_year,
            @Field("booking_type") String booking_type,
            @Field("booking_date") String booking_date,
            @Field("total_amount") String total_amount,
            @Field("gst_percentage") String gst_percentage,
            @Field("gst_amount") String gst_amount,
            @Field("owner_email") String owner_email,
            @Field("owner_mobile_no") String owner_mobile_no,
            @Field("owner_name") String owner_name,
            @Field("final_total_amount") String final_total_amount,
            @Field("booking_status") String booking_status);

    //The all State call
    @FormUrlEncoded
    @POST("state.php")
    Call<DistrictModel> getState(@Field("key") String key,
                                  @Field("country_id") String country_id);

    //The city call
    @FormUrlEncoded
    @POST("city.php")
    Call<CityListModel> getCity(@Field("key") String key,
                                @Field("state_id") String state_id);


    //The get booking by id call
    @FormUrlEncoded
    @POST("get_booking_by_id")
    Call<ResponseResult> callLastBookingHistory(
            @Field("key") String key,
            @Field("work_space_id") String work_space_id,
            @Field("current_date") String current_date);

    @FormUrlEncoded
    @POST("add_order.php")
    Call<ResponseResult>callOrderPlace(
            @Field("key") String key,
            @Field("user_id")String user_id,
            @Field("total_qty")String total_qty,
            @Field("final_total_amount")String final_total_amount,
            @Field("delivery_address")String delivery_address,
            @Field("product_list")String product_list
    );

}
