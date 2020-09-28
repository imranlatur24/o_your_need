package com.oyourneed.data;


import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIUrl {
    public static final String BASE_URL = "http://operandtechnologies.com/groceryApp/";
    public static final String BASE_IMAGE = "http://operandtechnologies.com/groceryApp/UploadDocs/SliderImages/";
    public static final String BASE_CATEGORY = "http://operandtechnologies.com/groceryApp/UploadDocs/CategoryImages/";
    public static final String BASE_ORDERLISTIMAGE = "http://clintico.co.in/grocary_portal/";
    public static final String KEY = "123456789";
    public static Retrofit retrofit = null;

    public static Retrofit getClient() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(3, TimeUnit.MINUTES)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .build();
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

}
