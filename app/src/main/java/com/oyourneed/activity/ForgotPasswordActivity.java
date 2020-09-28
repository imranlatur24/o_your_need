package com.oyourneed.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.oyourneed.R;
import com.oyourneed.data.APIService;
import com.oyourneed.data.APIUrl;
import com.oyourneed.data.ResponseResult;

import java.util.concurrent.TimeoutException;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.CALL_PHONE;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_SMS;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;


public class ForgotPasswordActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "ForgotPasswordActivity";
    private EditText forgot_password_mobile,editText_otp, editText_ChangePassword, forgot_password_otp, editText_NewPassword, editText_comfirmPassword;;
    private Button forgot_password_submit,txt_Login;
    private String strMobile, strOTP, mVerificationId, mobile_no, email_id, customer_id, strPassword, strConfirmPassword;
    private APIService apiService;
    private ProgressDialog progressDialog;
    //private FirebaseAuth mAuth;
    private Dialog dialogNewPassword;
    private Toolbar mToolbar;
    private String reg_id,regi_id,regi_mobile,otp;
    private String old_password, new_password, comfrim_password;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCurrentLanguage();
        setContentView(R.layout.forgotpassword);
        init();
        onClickListener();
    }

    private void init() {
       /* prefManager.connectDB();
        reg_id = prefManager.getString("userId");
        prefManager.closeDB();*/
       // Log.d(TAG,"#RED ID " +reg_id);

        Intent intent = getIntent();
        regi_id = intent.getStringExtra("user_id");
        regi_mobile = intent.getStringExtra("mobile_no");
        otp = intent.getStringExtra("OTP");

        Log.d(TAG,"#REGI ID " +regi_id);
        Log.d(TAG,"#REGI Mobile " +regi_mobile);


        mToolbar = (Toolbar) findViewById(R.id.forgot_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.forgertpassword));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txt_Login = findViewById(R.id.txt_Login);
        forgot_password_mobile = (EditText) findViewById(R.id.forgot_password_mobile);
        editText_otp = findViewById(R.id.editText_otp);
        editText_NewPassword = findViewById(R.id.editText_NewPassword);
        editText_comfirmPassword = findViewById(R.id.editText_comfirmPassword);
        forgot_password_submit = (Button) findViewById(R.id.forgot_password_submit);

        apiService = APIUrl.getClient().create(APIService.class);
        txt_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ForgotPasswordActivity.this,LoginActivity.class));
                finish();
            }
        });
    }

    private void onClickListener() {
        forgot_password_mobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                strMobile = forgot_password_mobile.getText().toString();
                strOTP = editText_otp.getText().toString();
             /*   old_password = editText_ChangePassword.getText().toString();
                new_password = editText_NewPassword.getText().toString();
                comfrim_password = editText_comfirmPassword.getText().toString();*/


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        forgot_password_submit.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.forgot_password_submit) {
            otp = editText_otp.getText().toString();
            new_password = editText_NewPassword.getText().toString();
            comfrim_password = editText_comfirmPassword.getText().toString();

            if (!isNetworkAvailable()) {
                Toasty.error(this, getResources().getString(R.string.error_msg_no_internet), Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(otp)) {
                //editText_NewPassword.setError("Enter new password");
                Toasty.error(this, getResources().getString(R.string.enterotp), Toast.LENGTH_SHORT).show();
                return;
            }
            if (otp.length() < 5) {
                //editText_NewPassword.setError("Enter new password should be greater than 6 letters");
                Toasty.error(this, getResources().getString(R.string.enterotpvalidation), Toast.LENGTH_SHORT).show();
                return;
            } if (TextUtils.isEmpty(new_password)) {
                //editText_NewPassword.setError("Enter new password");
                Toasty.error(this, getResources().getString(R.string.enternewpassword), Toast.LENGTH_SHORT).show();
                return;
            }
            if (new_password.length() < 6) {
                //editText_NewPassword.setError("Enter new password should be greater than 6 letters");
                Toasty.error(this, getResources().getString(R.string.enterotpvalidation), Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(comfrim_password)) {
                //editText_comfirmPassword.setError("Enter confirm password");
                Toasty.error(this, getResources().getString(R.string.confirm_password), Toast.LENGTH_SHORT).show();
                return;
            }
            if (comfrim_password.length() < 6) {
                // editText_comfirmPassword.setError("Enter confirm password should be greater than 6 letters");
                Toasty.error(this,  getResources().getString(R.string.enterpasswordvalidation), Toast.LENGTH_SHORT).show();
                return;
            }
            if (!comfrim_password.equals(new_password)) {
                Toasty.error(this,  getResources().getString(R.string.oldnewpasswordnotmatch),Toast.LENGTH_SHORT).show();
                return;
            }
            forgotPassword(otp,comfrim_password);
        }
    }




    private void forgotPassword(String otp,String comfrim_password) {
        //defining a progress dialog to show while signing up
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.loading));
        progressDialog.show();
        progressDialog.setCancelable(false);

        //calling the api
        callForgotPassword(otp,comfrim_password).enqueue(new Callback<ResponseResult>() {
            @Override
            public void onResponse(Call<ResponseResult> call, Response<ResponseResult> response) {
                //hiding progress dialog
                progressDialog.dismiss();
                try {
                    System.out.println("#response forgot ="+response.body().getResponse());

                    if (Integer.parseInt(response.body().getResponse()) == 101) {
                        progressDialog.dismiss();
                        System.out.println("#response forgot ="+response.body().getResponse());
                        System.out.println("#response forgot array ="+response.body().getChangePassList());
                        Toasty.success(getApplicationContext(), getResources().getString(R.string.password_chaged_successfully), Toasty.LENGTH_SHORT).show();
                        startActivity(new Intent(ForgotPasswordActivity.this, LoginActivity.class));
                        finish();
                    } else if (Integer.parseInt(response.body().getResponse()) == 102) {
                        progressDialog.dismiss();
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ForgotPasswordActivity.this);
                        alertDialogBuilder.setTitle("Your Need...!!!");
                        alertDialogBuilder.setIcon(R.drawable.logo);
                        alertDialogBuilder.setMessage("Verification under process please contact support team");
                        alertDialogBuilder.setCancelable(false);

                        alertDialogBuilder.setPositiveButton("Call", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                                callIntent.setData(Uri.parse("tel:7710881086"));//change the number
                                if (ActivityCompat.checkSelfPermission(ForgotPasswordActivity.this, CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                    Toast.makeText(ForgotPasswordActivity.this, "Permissions required", Toast.LENGTH_SHORT).show();
                                    if (!checkPermission()) {
                                        requestPermission();
                                    }
                                    return;
                                }
                                startActivity(callIntent);
                            }
                        });

                        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
                        Toasty.error(getApplicationContext(), "Verification under process please contact support team", Toast.LENGTH_LONG).show();
                    } else if (Integer.parseInt(response.body().getResponse()) == 105) {
                        Toasty.error(getApplicationContext(), getResources().getString(R.string.serverdown), Toast.LENGTH_LONG).show();
                    }else {
                        System.out.println(TAG + " Else Close");
                        forgot_password_mobile.setText("");
                        Toasty.error(getApplicationContext(), getResources().getString(R.string.serverdown), Toast.LENGTH_LONG).show();
                    }
                } catch (NullPointerException | NumberFormatException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseResult> call, Throwable throwable) {
                progressDialog.dismiss();
                Log.d("#trowable", throwable.getMessage());
                forgot_password_mobile.setText("");
                if (!isNetworkAvailable()) {
                    Toasty.error(getApplicationContext(), getResources().getString(R.string.serverdown), Toasty.LENGTH_LONG).show();
                } else if (throwable instanceof TimeoutException) {
                    Toasty.error(getApplicationContext(), getResources().getString(R.string.error_msg_timeout), Toasty.LENGTH_LONG).show();
                }
            }
        });
    }

    private Call<ResponseResult> callForgotPassword(String otp,String comfrim_password) {
        System.out.println("API KEY : "+APIUrl.KEY+" otp "+otp+" user id "+regi_id+" password "+comfrim_password+" mobile no "+regi_mobile);
        return apiService.callForgotPassword(
                APIUrl.KEY,
                otp,
                regi_id,
                comfrim_password,
                regi_mobile
        );
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(ForgotPasswordActivity.this, new
                String[]{CALL_PHONE, READ_SMS,
                WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, 1);
    }

    public boolean checkPermission() {

        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), CALL_PHONE);
        int result5 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_SMS);
        int result10 = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int result11 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);

        return result1 == PackageManager.PERMISSION_GRANTED
                && result5 == PackageManager.PERMISSION_GRANTED
                && result10 == PackageManager.PERMISSION_GRANTED
                && result11 == PackageManager.PERMISSION_GRANTED;
    }

}
