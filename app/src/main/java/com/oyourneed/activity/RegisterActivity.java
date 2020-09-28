package com.oyourneed.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.oyourneed.R;
import com.oyourneed.data.APIService;
import com.oyourneed.data.APIUrl;
import com.oyourneed.data.ResponseResult;
import com.oyourneed.model.ForgotModel;
import com.oyourneed.model.OtpModel;

import java.util.concurrent.TimeoutException;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends BaseActivity implements View.OnClickListener {
    private EditText registerMobile, registerPassword, registerCPassword;
    private String strMobile, strPassword, strConfirmPassword;
    private Button btnRegister;
    private ProgressDialog progressDialog;
    private APIService apiService;
    private TextView txt_Login;
    private static final String TAG = "RegisterActivity";
    private Dialog dialog;
    private String otpvalue;
    private EditText edtOTP;
    private Button btnOTP;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCurrentLanguage();
        setContentView(R.layout.register);
        init();
    }

    private void init() {
        txt_Login = findViewById(R.id.txt_Login);
        registerMobile = findViewById(R.id.registerMobile);
        registerPassword = findViewById(R.id.registerPassword);
        registerCPassword = findViewById(R.id.registerCPassword);
        btnRegister = findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(this);

        apiService = APIUrl.getClient().create(APIService.class);
        registerMobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(final CharSequence s, int start, int before, int count) {
                if (s.length() == 10) {
                   checkMobileNumber(String.valueOf(s));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        txt_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
            }
        });
    }

    private void checkMobileNumber(final String strMobile) {
        //defining a progress dialog to show while signing up
        progressDialog = new ProgressDialog(this, R.style.AppCompatAlertDialogStyle);
        progressDialog.setMessage(getResources().getString(R.string.please_wait));
        progressDialog.show();
        progressDialog.setCancelable(false);

        //calling the api
        callCheckMobileNumber(strMobile).enqueue(new Callback<ResponseResult>() {
            @Override
            public void onResponse(Call<ResponseResult> call, Response<ResponseResult> response) {
                //hiding progress dialog
                progressDialog.dismiss();

                try {
                    System.out.println("#response otp " + response.body().getResponse());
                    if (Integer.parseInt(response.body().getResponse()) == 101) {
                        progressDialog.dismiss();
                        System.out.println("#OTP Response : "+response.body().getUserMsgList());

                       // Toasty.success(getApplicationContext(), getResources().getString(R.string.you_will_receive_otp), Toasty.LENGTH_LONG).show();
                       // dialogBoxOTP();

                    } else if (Integer.parseInt(response.body().getResponse()) == 102) {
                        progressDialog.dismiss();
                        System.out.println("#OTP Response : "+response.body().getUserMsgList());
                        Toasty.success(getApplicationContext(), getResources().getString(R.string.useralreadyexist), Toasty.LENGTH_LONG).show();
                        Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                        intent.putExtra("mobno",strMobile);
                        startActivity(intent);
                        finish();

                        //registerMobile.setText("");
                        //Toasty.error(getApplicationContext(), getResources().getString(R.string.requiredparameter), Toasty.LENGTH_LONG).show();
                    } else if (Integer.parseInt(response.body().getResponse()) == 105) {
                        progressDialog.dismiss();
                        System.out.println(TAG + " Invalid Mobile no");
                        registerMobile.setText("");
                        Toasty.error(getApplicationContext(),"Server Getting Down, please try after sometime",Toasty.LENGTH_LONG).show();
                    } else {
                        progressDialog.dismiss();
                        System.out.println(TAG + " Else Close");
                        registerMobile.setText("");
                        Toasty.error(getApplicationContext(),"Server Getting Down, please try after sometime",Toasty.LENGTH_LONG).show();
                    }
                } catch (NullPointerException e) {
                    registerMobile.setText("");
                    Toasty.error(getApplicationContext(),"Server Getting Down, please try after sometime",Toasty.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseResult> call, Throwable throwable) {
                progressDialog.dismiss();
                registerMobile.setText("");
                if (!isNetworkAvailable()) {
                    Toasty.error(getApplicationContext(), getResources().getString(R.string.error_msg_no_internet), Toasty.LENGTH_LONG).show();
                } else if (throwable instanceof TimeoutException) {
                    Toasty.error(getApplicationContext(), getResources().getString(R.string.error_msg_timeout), Toasty.LENGTH_LONG).show();
                }
            }
        });
    }
    
    private Call<ResponseResult> callCheckMobileNumber(String strMobile) {
        System.out.println("KEY MOBILE VERIFICATION "+ APIUrl.KEY+" Mobile "+strMobile);
        return apiService.mobileVerification(
                APIUrl.KEY,
                strMobile
        );
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnRegister:
                strMobile = registerMobile.getText().toString().trim();
                strPassword = registerPassword.getText().toString().trim();
                strConfirmPassword = registerCPassword.getText().toString().trim();

                if (!isNetworkAvailable()) {
                    Toasty.error(getApplicationContext(), getResources().getString(R.string.error_msg_no_internet), Toasty.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(strMobile)) {
                    // registerMobile.setError("Enter Mobile no.");
                    Toasty.error(getApplicationContext(), getResources().getString(R.string.entermobile), Toasty.LENGTH_SHORT).show();
                    return;
                }
                if (strMobile.length() < 10) {
                    //  registerMobile.setError("Enter Valid Mobile no.");
                    Toasty.error(getApplicationContext(), getResources().getString(R.string.entervalidmobileno), Toasty.LENGTH_SHORT).show();
                    return;
                }
                if (String.valueOf(strMobile.charAt(0)).equals("0") ||
                        String.valueOf(strMobile.charAt(0)).equals("1") ||
                        String.valueOf(strMobile.charAt(0)).equals("2") ||
                        String.valueOf(strMobile.charAt(0)).equals("3") ||
                        String.valueOf(strMobile.charAt(0)).equals("4") ||
                        String.valueOf(strMobile.charAt(0)).equals("5") ||
                        String.valueOf(strMobile.charAt(0)).equals("6")) {
                    // registerMobile.setError("Enter Valid Mobile .");
                    Toasty.error(getApplicationContext(), getResources().getString(R.string.entervalidmobileno), Toasty.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(strPassword)) {
                    //registerPassword.setError("Enter Password");
                    Toasty.error(getApplicationContext(), getResources().getString(R.string.enterpassword), Toasty.LENGTH_SHORT).show();
                    return;
                }
                if (strPassword.length() < 6) {
                    // registerPassword.setError("Enter Password should not be less than 6 character");
                    Toasty.error(getApplicationContext(), getResources().getString(R.string.enterpasswordvalidation), Toasty.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(strConfirmPassword)) {
                    //registerCPassword.setError("Enter Confirm Password");
                    Toasty.error(getApplicationContext(), getResources().getString(R.string.enterconfirmpassword), Toasty.LENGTH_SHORT).show();
                    return;
                }
                if (!strPassword.equals(strConfirmPassword)) {
                    Toasty.error(getApplicationContext(), getResources().getString(R.string.oldnewpasswordnotmatch), Toasty.LENGTH_SHORT).show();
                    return;
                }
                userSignUp(strMobile, strPassword);

                break;
        }
    }

    private void userSignUp(final String mobile, final String password) {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.loading));
        progressDialog.show();

        callRegisterService(mobile, password).enqueue(new Callback<ResponseResult>() {
            @Override
            public void onResponse(Call<ResponseResult> call, Response<ResponseResult> response) {
                progressDialog.dismiss();
                if (Integer.parseInt(response.body().getResponse()) == 101) {
                    System.out.println("response reg :"+response.body().getResponse());

                    Toasty.success(getApplicationContext(), getResources().getString(R.string.registrationdone), Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();

                } else if (Integer.parseInt(response.body().getResponse()) == 102) {
                    progressDialog.dismiss();
                    Toasty.error(getApplicationContext(), getResources().getString(R.string.account_block), Toast.LENGTH_LONG).show();
                } else if (Integer.parseInt(response.body().getResponse()) == 103) {
                    progressDialog.dismiss();
                    System.out.println(TAG + " Required Parameter Missing");
                    Toasty.error(getApplicationContext(), getResources().getString(R.string.entervalidmobilenopassword), Toast.LENGTH_LONG).show();
                } else if (Integer.parseInt(response.body().getResponse()) == 105) {
                    progressDialog.dismiss();
                    System.out.println(TAG + " Login failed");
                    Toasty.error(getApplicationContext(), getResources().getString(R.string.entervalidmobilenopassword), Toast.LENGTH_LONG).show();
                } else {
                    progressDialog.dismiss();
                    System.out.println(TAG + " Else Close");
                    Toasty.error(getApplicationContext(), getResources().getString(R.string.entervalidmobilenopassword), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseResult> call, Throwable t) {
                progressDialog.dismiss();
                errorOut(t);
            }
        });
    }

    private Call<ResponseResult> callRegisterService(final String mobile, final String password) {
        return apiService.callRegister(
                APIUrl.KEY,
                mobile,
                password
        );
    }

    private void dialogBoxOTP() {

        dialog = new Dialog(RegisterActivity.this, R.style.AppCompatAlertDialogStyle);
        dialog.setContentView(R.layout.otp_dialog_box);
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
        edtOTP = dialog.findViewById(R.id.edtOTP);
        btnOTP = dialog.findViewById(R.id.btnOTP );

        btnOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                otpvalue = edtOTP.getText().toString().trim();
                if (TextUtils.isEmpty(otpvalue)) {
                    Toasty.error(getApplicationContext(), getResources().getString(R.string.enterotp), Toasty.LENGTH_SHORT).show();
                }

                if (!isNetworkAvailable()) {
                    Toasty.error(getApplicationContext(), getResources().getString(R.string.error_msg_no_internet), Toasty.LENGTH_SHORT).show();
                    return;
                }
                if (otpvalue.length() < 5) {
                    Toasty.error(getApplicationContext(),R.string.entervalidotp,Toasty.LENGTH_SHORT).show();
                    return;
                }
                otp(otpvalue);
            }
        });
        dialog.show();
    }
    private void otp(String otpvalue) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.please_wait));
        progressDialog.show();
        callForgot().enqueue(new Callback<OtpModel>() {
            @Override
            public void onResponse(Call<OtpModel> call, Response<OtpModel> response) {
                progressDialog.dismiss();
                try {
                    System.out.println("response confirm otp :"+response.body().getResponse());

                    if (Integer.parseInt(response.body().getResponse()) == 101) {
                        System.out.println("response confirm otp :"+response.body().getResponse());
                        progressDialog.dismiss();
                        dialog.dismiss();
                        Toasty.error(getApplicationContext(), getResources().getString(R.string.otpmatched), Toast.LENGTH_LONG).show();
                    } else if (Integer.parseInt(response.body().getResponse()) == 102) {
                        progressDialog.dismiss();
                        Toasty.error(getApplicationContext(), getResources().getString(R.string.otpwrong), Toast.LENGTH_LONG).show();
                    } else {
                        progressDialog.dismiss();
                        System.out.println(TAG + " Else Close");
                        Toasty.error(getApplicationContext(), getResources().getString(R.string.serverdown), Toast.LENGTH_LONG).show();
                    }
                } catch (NullPointerException | NumberFormatException e) {
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<OtpModel> call, Throwable t) {
                progressDialog.dismiss();
                errorOut(t);
            }
        });
    }

    private Call<OtpModel> callForgot() {
        System.out.println("API KEY : "+APIUrl.KEY+" OTP VALUE :"+otpvalue);
        return apiService.checkotp(
                APIUrl.KEY,
                otpvalue
        );
    }
}