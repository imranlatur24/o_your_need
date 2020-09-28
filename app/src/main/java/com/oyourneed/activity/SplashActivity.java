package com.oyourneed.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.oyourneed.R;
import com.oyourneed.data.APIService;
import com.oyourneed.data.APIUrl;
import com.oyourneed.data.ResponseResult;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends BaseActivity {


    private static final long SPLASH_TIME_OUT = 3000;
    private boolean isLogin;
    private APIService apiService;
    private static final String TAG = "SplashActivity";
    private Intent intent;
    static String version_code = "1.0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCurrentLanguage();
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();
        apiService = APIUrl.getClient().create(APIService.class);

        prefManager.connectDB();
        isLogin = prefManager.getBoolean("isLogin");
        prefManager.closeDB();

       /* ImageView img_Logo = findViewById(R.id.img_Logo);
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade);
        img_Logo.startAnimation(animation);*/

        if(isNetworkAvailable()) {
            card_VersioncodeM();
        }else {
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    if(isLogin){
                        Toasty.error(getApplicationContext(),getResources().getString(R.string.error_msg_no_internet), Toasty.LENGTH_LONG).show();
                        intent = new Intent(getApplicationContext(), HomeActivity.class);
                        startActivity(intent);
                    }else {
                        Toasty.error(getApplicationContext(),getResources().getString(R.string.error_msg_no_internet), Toasty.LENGTH_LONG).show();
                        intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                    }
                    finish();
                }
            }, SPLASH_TIME_OUT);
        }
    }

    //check version code
    private void card_VersioncodeM() {
        card_Versioncode().enqueue(new Callback<ResponseResult>() {
            @Override
            public void onResponse(Call<ResponseResult> call, Response<ResponseResult> response) {
                try{
                    System.out.println(TAG+ " Response "+response.body().getResponse());
                    if(Integer.parseInt(response.body().getResponse()) == 101)
                    {
                        Thread thread = new Thread(){
                            @Override
                            public void run() {
                                try {
                                    sleep(2000);
                                    if(isLogin){
                                        intent = new Intent(getApplicationContext(), HomeActivity.class);
                                        startActivity(intent);
                                    }else {
                                        intent = new Intent(getApplicationContext(), LoginActivity.class);
                                        startActivity(intent);
                                    }
                                    finish();
                                }catch (Exception e)
                                {
                                    e.printStackTrace();
                                }
                            }
                        };
                        thread.start();

                    }
                    else if (Integer.parseInt(response.body().getResponse()) == 102){
                        final Dialog dialog = new Dialog(SplashActivity.this);
                        dialog.setCancelable(false);
                        dialog.setContentView(R.layout.force_update_dialog);

                        Button btnNow=(Button) dialog.findViewById(R.id.btnNow);
                        Button btnCancel2=(Button) dialog.findViewById(R.id.btnCancel2);
                        ImageView image_cancel_dialog=(ImageView) dialog.findViewById(R.id.image_cancel_dialog);
                        btnCancel2.setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View v)
                            {
                                dialog.dismiss();
                                Thread thread = new Thread(){
                                    @Override
                                    public void run() {
                                        try {
                                            sleep(2000);
                                            if(isLogin){
                                                intent = new Intent(getApplicationContext(), HomeActivity.class);
                                                startActivity(intent);
                                            }else {
                                                intent = new Intent(getApplicationContext(), LoginActivity.class);
                                                startActivity(intent);
                                            }
                                            finish();
                                        }catch (Exception e)
                                        {
                                            e.printStackTrace();
                                        }
                                    }
                                };
                                thread.start();
                            }
                        });

                        image_cancel_dialog.setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View v)
                            {
                                dialog.dismiss();
                                Thread thread = new Thread(){
                                    @Override
                                    public void run() {
                                        try {
                                            sleep(2000);
                                            if(isLogin){
                                                intent = new Intent(getApplicationContext(), HomeActivity.class);
                                                startActivity(intent);
                                            }else {
                                                intent = new Intent(getApplicationContext(), LoginActivity.class);
                                                startActivity(intent);
                                            }
                                            finish();
                                        }catch (Exception e)
                                        {
                                            e.printStackTrace();
                                        }
                                    }
                                };
                                thread.start();
                            }
                        });


                        btnNow.setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View v)
                            {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName())));
                                finish();
                            }
                        });
                        dialog.show();
                    }
                }catch (NullPointerException | NumberFormatException e)
                {
//                    System.out.println(TAG+ " Response "+response.body().getResponse());
                    Thread thread = new Thread(){
                        @Override
                        public void run() {
                            try {
                                sleep(4000);
                                if(isLogin){
                                    intent = new Intent(getApplicationContext(), HomeActivity.class);
                                    startActivity(intent);
                                }else {
                                    intent = new Intent(getApplicationContext(), LoginActivity.class);
                                    startActivity(intent);
                                }
                                finish();
                            }catch (Exception e)
                            {
                                e.printStackTrace();
                            }
                        }
                    };
                    thread.start();
                }
            }

            @Override
            public void onFailure(Call<ResponseResult> call, Throwable t) {
                Log.d(TAG, "onFailure: "+t.getMessage());
                Thread thread = new Thread(){
                    @Override
                    public void run() {
                        try {
                            sleep(4000);
                            if(isLogin){
                                intent = new Intent(getApplicationContext(), HomeActivity.class);
                                startActivity(intent);
                            }else {
                                intent = new Intent(getApplicationContext(), LoginActivity.class);
                                startActivity(intent);
                            }
                            finish();
                        }catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                };
                thread.start();
                //Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
    private Call<ResponseResult> card_Versioncode() {
        System.out.println(TAG+ " Response "+version_code);
        return apiService.card_Versioncode(
                APIUrl.KEY,
                version_code
        );
    }

    public static boolean isTimeAutomatic(Context c) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return Settings.Global.getInt(c.getContentResolver(), Settings.Global.AUTO_TIME, 0) == 1;
        } else {
            return Settings.System.getInt(c.getContentResolver(), Settings.System.AUTO_TIME, 0) == 1;
        }
    }
}
