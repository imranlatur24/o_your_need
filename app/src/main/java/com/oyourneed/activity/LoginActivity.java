package com.oyourneed.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.oyourneed.R;
import com.oyourneed.data.APIService;
import com.oyourneed.data.APIUrl;
import com.oyourneed.data.ResponseResult;
import com.oyourneed.model.ForgotModel;
import com.oyourneed.view.MyEditText;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "LoginActivity";

    private MyEditText confirmPassword, edtOTP, newPassword;
    private Button btnLogin, btnLoginForget, btnOTP, loginGues;
    private TextView txt_Register, confirmPasswor, loginForgotPassword, loginSignUp, txtLoginGoBack, txtResendOTP, otpTitle;
    private EditText edtLoginMobile, edtConfirmPassword, LoginMobileNo, LoginPassword;
    private Dialog dialog;
    private LinearLayout lytNewPassword;
    private APIService apiService;
    private ProgressDialog progressDialog1, progressDialog2, progressDialog3, progressDialog4;
    private String strFCMCode, strMobile, strPassword, reg_id;
    private String language_id;
    int selection = -1;
    EditText edtNewPassword;
    Button btnLoginForgetok;
    String forgot_mobile, registration_id, reg_mobileno,otp;
    private Intent intent;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        init();
    }

    private void init() {


        strFCMCode = "IMRAN GCM";

        if (TextUtils.isEmpty(strFCMCode)) {
            strFCMCode = "null";
        }

        System.out.println("Refreshed " + strFCMCode);

        btnLogin = findViewById(R.id.btnLogin);
        LoginMobileNo = findViewById(R.id.LoginMobileNo);
        LoginPassword = findViewById(R.id.LoginPassword);
        txt_Register = findViewById(R.id.txt_Register);
        loginForgotPassword = findViewById(R.id.loginForgotPassword);
        loginForgotPassword.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        txt_Register.setOnClickListener(this);

        apiService = APIUrl.getClient().create(APIService.class);
//        strMobile = intent.getExtras().getString("mobno");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLogin:
                strMobile = LoginMobileNo.getText().toString().trim();
                strPassword = LoginPassword.getText().toString().trim();

                if (!isNetworkAvailable()) {

                    Toasty.error(getApplicationContext(), getResources().getString(R.string.error_msg_no_internet), Toasty.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(strMobile)) {
                    LoginMobileNo.setError(getResources().getString(R.string.entermobile));
                    return;
                }
                if (strMobile.length() < 10) {
                    LoginMobileNo.setError(getResources().getString(R.string.entervalidmobileno));
                    return;
                }
                if (String.valueOf(strMobile.charAt(0)).equals("0") ||
                        String.valueOf(strMobile.charAt(0)).equals("1") ||
                        String.valueOf(strMobile.charAt(0)).equals("2") ||
                        String.valueOf(strMobile.charAt(0)).equals("3") ||
                        String.valueOf(strMobile.charAt(0)).equals("4") ||
                        String.valueOf(strMobile.charAt(0)).equals("5") ||
                        String.valueOf(strMobile.charAt(0)).equals("6")) {
                    LoginMobileNo.setError(getResources().getString(R.string.entervalidmobileno));
                    return;
                }
                if (TextUtils.isEmpty(strPassword)) {
                    LoginPassword.setError(getResources().getString(R.string.enterpassword));
                    return;
                }
                if (strPassword.length() < 6) {
                    LoginPassword.setError(getResources().getString(R.string.enter_valid_password));
                    return;
                }
                userSignIn(strMobile, strPassword, strFCMCode);
                break;

            case R.id.txt_Register:
                Intent registerActivity = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(registerActivity);
                break;

            case R.id.loginForgotPassword:
               forgotDialog();
                break;
        }
    }


    private void forgotDialog() {

        dialog = new Dialog(LoginActivity.this, R.style.AppCompatAlertDialogStyle);
        dialog.setContentView(R.layout.forgot_password_dialog_box);
        dialog.setCancelable(true);
        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        int height = ViewGroup.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setLayout(width, height);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        ImageView image_cancel_dialog = dialog.findViewById(R.id.image_cancel_dialog);
        image_cancel_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        edtNewPassword = dialog.findViewById(R.id.edtNewPassword);
        btnLoginForgetok = dialog.findViewById(R.id.btnLoginForget);

        btnLoginForgetok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgot_mobile = edtNewPassword.getText().toString().trim();
                if (!isNetworkAvailable()) {
                    Toasty.error(getApplicationContext(), getResources().getString(R.string.error_msg_no_internet), Toasty.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(forgot_mobile)) {
                    edtNewPassword.setError(getResources().getString(R.string.entermobile));
                    return;
                }
                if (forgot_mobile.length() < 10) {
                    edtNewPassword.setError(getResources().getString(R.string.entervalidmobileno));
                    return;
                }
                if (String.valueOf(forgot_mobile.charAt(0)).equals("0") ||
                        String.valueOf(forgot_mobile.charAt(0)).equals("1") ||
                        String.valueOf(forgot_mobile.charAt(0)).equals("2") ||
                        String.valueOf(forgot_mobile.charAt(0)).equals("3") ||
                        String.valueOf(forgot_mobile.charAt(0)).equals("4") ||
                        String.valueOf(forgot_mobile.charAt(0)).equals("5") ||
                        String.valueOf(forgot_mobile.charAt(0)).equals("6")) {
                    edtNewPassword.setError(getResources().getString(R.string.entervalidmobileno));
                    return;
                }
                forgotPassword(forgot_mobile);
                Toast.makeText(getApplicationContext(),"mobile no : "+forgot_mobile,Toast.LENGTH_SHORT).show();
            }
        });
        dialog.show();
    }


    private void forgotPassword(final String forgot_mobile) {
        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage(getResources().getString(R.string.please_wait));
        progressDialog.show();
        callForgot(forgot_mobile).enqueue(new Callback<ForgotModel>() {

            @Override
            public void onResponse(Call<ForgotModel> call, Response<ForgotModel> response) {
                progressDialog.dismiss();
                try {
                    if (Integer.parseInt(response.body().getResponse()) == 101) {
                        progressDialog.dismiss();
                        dialog.dismiss();
                        System.out.println("response forgot : "+response.body().getResponse());
                        System.out.println("#reg id =" + response.body().getLogin().get(0).getUser_id());
                        registration_id = response.body().getLogin().get(0).getUser_id();
                        reg_mobileno = response.body().getLogin().get(0).getMobile_no();
                        otp = response.body().getLogin().get(0).getOTP();
                        System.out.println("#reg Mobile No =" + response.body().getLogin().get(0).getMobile_no());
                        Toasty.success(getApplicationContext(),R.string.you_will_receive_otp,Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                        intent.putExtra("user_id", registration_id);
                        intent.putExtra("mobile_no", reg_mobileno);
                        intent.putExtra("OTP", otp);
                        startActivity(intent);

                    } else if (Integer.parseInt(response.body().getResponse()) == 102) {
                        progressDialog.dismiss();
                        Toasty.error(getApplicationContext(), getResources().getString(R.string.mobilenonotexist), Toast.LENGTH_LONG).show();
                    } else if (Integer.parseInt(response.body().getResponse()) == 103) {
                        progressDialog.dismiss();
                        System.out.println(TAG + " Required Parameter Missing");
                        Toasty.error(getApplicationContext(), getResources().getString(R.string.passwordwrong), Toast.LENGTH_LONG).show();
                    } else if (Integer.parseInt(response.body().getResponse()) == 105) {
                        progressDialog.dismiss();
                        System.out.println(TAG + " Login failed");
                        Toasty.error(getApplicationContext(), getResources().getString(R.string.serverdown), Toast.LENGTH_LONG).show();
                    } else {
                        progressDialog.dismiss();
                        System.out.println(TAG + " Else Close");
                        Toasty.error(getApplicationContext(), getResources().getString(R.string.mobilenonotfound), Toast.LENGTH_LONG).show();
                    }
                } catch (NullPointerException | NumberFormatException e) {
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ForgotModel> call, Throwable t) {
                progressDialog.dismiss();
                errorOut(t);
            }
        });
    }

    private Call<ForgotModel> callForgot(final String forgot_mobile) {
        System.out.println("API KEY : "+APIUrl.KEY+" MOBILE NO : "+forgot_mobile);
        return apiService.forgotMain(
                APIUrl.KEY,
                forgot_mobile
        );
    }

    private void userSignIn(final String mobile, final String password, final String gcm_code) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.signin));
        progressDialog.show();
        callSignIn(mobile, password, gcm_code).enqueue(new Callback<ResponseResult>() {
            @Override
            public void onResponse(Call<ResponseResult> call, Response<ResponseResult> response) {
                progressDialog.dismiss();
                try {
                    if (Integer.parseInt(response.body().getResponse()) == 101) {
                        progressDialog.dismiss();
                        System.out.println("response login : " + response.body().getResponse());
                        Toasty.success(getApplicationContext(), getResources().getString(R.string.welcome), Toast.LENGTH_LONG).show();

                        prefManager.connectDB();
                        prefManager.setBoolean("isLogin", true);
                        prefManager.setString("userId", response.body().getLogin().get(0).getReg_id());
                        prefManager.setString("userFirstName", response.body().getLogin().get(0).getFirst_name());
                        prefManager.setString("userLastName", response.body().getLogin().get(0).getLast_name());
                        prefManager.setString("userMobile", response.body().getLogin().get(0).getMobile_no());
                        prefManager.setString("userEmail", response.body().getLogin().get(0).getEmail_id());
                        prefManager.setString("userDOB", response.body().getLogin().get(0).getDob());
                        prefManager.setString("userCountry", response.body().getLogin().get(0).getCountry());
                        prefManager.setString("userState", response.body().getLogin().get(0).getState());
                        prefManager.setString("userCity", response.body().getLogin().get(0).getCity());
                        prefManager.setString("userAddress", response.body().getLogin().get(0).getAddress());
                        prefManager.setString("userStatus", response.body().getLogin().get(0).getUser_status());
                        prefManager.setString("userPincode", response.body().getLogin().get(0).getPincode());
                        prefManager.setString("profile_pic", response.body().getLogin().get(0).getProfile_pic());
                        prefManager.setString("is_created_date", response.body().getLogin().get(0).getIs_created_date());
                        prefManager.closeDB();

                        Log.d(TAG, "#userId" + response.body().getLogin().get(0).getReg_id());
                        Log.d(TAG, "#userFirstName" + response.body().getLogin().get(0).getFirst_name());
                        Log.d(TAG, "#userLastName" + response.body().getLogin().get(0).getLast_name());
                        Log.d(TAG, "#userEmail" + response.body().getLogin().get(0).getEmail_id());
                        Log.d(TAG, "#userMobile" + response.body().getLogin().get(0).getMobile_no());
                        Log.d(TAG, "#userDOB" + response.body().getLogin().get(0).getDob());
                        Log.d(TAG, "#userCountry" + response.body().getLogin().get(0).getCountry());
                        Log.d(TAG, "#userState" + response.body().getLogin().get(0).getState());
                        Log.d(TAG, "#userCity" + response.body().getLogin().get(0).getCity());
                        Log.d(TAG, "#userAddress" + response.body().getLogin().get(0).getAddress());
                        Log.d(TAG, "#userStatus" + response.body().getLogin().get(0).getUser_status());
                        Log.d(TAG, "#profile_pic" + response.body().getLogin().get(0).getProfile_pic());
                        Log.d(TAG, "#is_created_date" + response.body().getLogin().get(0).getIs_created_date());

                        //dialogLanguage();
                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                        //  intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();


                    } else if (Integer.parseInt(response.body().getResponse()) == 102) {
                        progressDialog.dismiss();
                        Toasty.error(getApplicationContext(), getResources().getString(R.string.mobilenonotexist), Toast.LENGTH_LONG).show();
                    } else if (Integer.parseInt(response.body().getResponse()) == 103) {
                        progressDialog.dismiss();
                        System.out.println(TAG + " Required Parameter Missing");
                        Toasty.error(getApplicationContext(), getResources().getString(R.string.passwordwrong), Toast.LENGTH_LONG).show();
                    } else if (Integer.parseInt(response.body().getResponse()) == 105) {
                        progressDialog.dismiss();
                        System.out.println(TAG + " Login failed");
                        Toasty.error(getApplicationContext(), getResources().getString(R.string.serverdown), Toast.LENGTH_LONG).show();
                    } else {
                        progressDialog.dismiss();
                        System.out.println(TAG + " Else Close");
                        Toasty.error(getApplicationContext(), getResources().getString(R.string.serverdown), Toast.LENGTH_LONG).show();
                    }
                } catch (NullPointerException | NumberFormatException e) {
                    progressDialog.dismiss();
                    Toasty.error(getApplicationContext(), getResources().getString(R.string.serverdown), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseResult> call, Throwable t) {
                progressDialog.dismiss();
                errorOut(t);
            }
        });
    }

    private Call<ResponseResult> callSignIn(final String mobile, final String password, final String gcm_code) {
        return apiService.callSignIn(
                APIUrl.KEY,
                mobile,
                password,
                gcm_code
        );
    }


 /*   //dialog change language
    private void dialogLanguage() {
        //popup with radiobuttons
        String[] artSort = new String[]
                {"English",
                        "हिंदी",
                        "मराठी",


                };

        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this, R.style.AppCompatAlertDialogStyle);
        builder.setTitle(getResources().getString(R.string.sortby_language));
        builder.setSingleChoiceItems(artSort, selection, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichlist) {
                dialog.dismiss();
                switch (whichlist) {
                    case 0:

                        prefManager.connectDB();
                        prefManager.setString("language_id", "1");
                        prefManager.setString("language", "en");
                        prefManager.closeDB();
                        english();
                        selection = 0;
                        break;
                    case 1:
                        setLocale("hi");
                        recreate();
                        prefManager.connectDB();
                        prefManager.setString("language_id", "2");
                        prefManager.setString("language", "hi");
                        prefManager.closeDB();
                        hindi();
                        selection = 1;
                        break;

                    case 2:
                        setLocale("mr");
                        recreate();
                        prefManager.connectDB();
                        prefManager.setString("language_id", "3");
                        prefManager.setString("language", "mr");
                        prefManager.closeDB();
                        marathi();
                        selection = 2;
                        break;
                }
            }
        });
        builder.show();
    }

    private void english() {
        setLocale("en");
        recreate();
        languageSelection();
    }

    private void marathi() {
        setLocale("hi");
        recreate();
        languageSelection();
    }

    private void hindi() {
        setLocale("mr");
        recreate();
        languageSelection();
    }

    private void languageSelection() {
        final ProgressDialog progressDialog = new ProgressDialog(this, R.style.AppCompatAlertDialogStyle);
        progressDialog.setMessage(getResources().getString(R.string.loading));
        progressDialog.show();

        callLanguage().enqueue(new Callback<ResponseResult>() {
            @Override
            public void onResponse(Call<ResponseResult> call, Response<ResponseResult> response) {
                try {
                    if (Integer.parseInt(response.body().getResponse()) == 101) {
                        progressDialog.dismiss();
                        //Toasty.error(getApplicationContext(), "success language", Toast.LENGTH_LONG).show();


                        System.out.println("#Language id : " + response.body().getLanguage_list().get(0).getLanguage_id());
                        System.out.println("#Language name : " + response.body().getLanguage_list().get(0).getLanguage_name());
                        System.out.println("#Language status : " + response.body().getLanguage_list().get(0).getLanguage_status());
                        System.out.println("#Language date : " + response.body().getLanguage_list().get(0).getIs_created_date());

                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                        //  intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();

                    } else if (Integer.parseInt(response.body().getResponse()) == 102) {
                        progressDialog.dismiss();
                        Toasty.error(getApplicationContext(), getResources().getString(R.string.account_block), Toast.LENGTH_LONG).show();
                    } else {
                        progressDialog.dismiss();
                        Toasty.error(getApplicationContext(), getResources().getString(R.string.serverdown), Toast.LENGTH_LONG).show();
                    }
                } catch (NullPointerException | NumberFormatException e) {
                    progressDialog.dismiss();
                    Toasty.error(getApplicationContext(), getResources().getString(R.string.serverdown), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseResult> call, Throwable t) {
                progressDialog.dismiss();
                errorOut(t);
            }
        });
    }

    //categories
    private Call<ResponseResult> callLanguage() {
        System.out.println("API KEY " + APIUrl.KEY);
        return apiService.getLanguageList(
                APIUrl.KEY
        );
    }
*/

    //m permission

    @Override
    public void onRequestPermissionsResult(int RC, String per[], int[] Result) {

        switch (RC) {

            case 1:

                if (Result.length > 0 && Result[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(LoginActivity.this, getResources().getString(R.string.permission_granted), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(LoginActivity.this, getString(R.string.permission_denied), Toast.LENGTH_LONG).show();
                }
                break;
        }
    }
}

