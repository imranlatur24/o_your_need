package com.oyourneed.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.oyourneed.R;
import com.oyourneed.data.APIService;
import com.oyourneed.data.APIUrl;
import com.oyourneed.data.ResponseResult;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "ChangePasswordActivity";
    private Toolbar toolbar;
    private EditText oldPassword,newPassword, confirmPassword;
    private Button btnChangePassword;
    private String strOldPassword, strNewPassword, strConfirmPassword,id,mobileNo;
    private ProgressDialog progressDialog;
    private APIService apiService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCurrentLanguage();
        setContentView(R.layout.activity_change_password);
        inti();
    }

    private void inti() {

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.change_passord));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        prefManager.connectDB();
        id = prefManager.getString("userId");
        mobileNo = prefManager.getString("userMobile");
        prefManager.closeDB();

        oldPassword = (EditText) findViewById(R.id.txt_OldPassword);
        newPassword = (EditText) findViewById(R.id.txt_NewPassword);
        confirmPassword = (EditText) findViewById(R.id.txt_ConfirmPassword);
        btnChangePassword = (Button) findViewById(R.id.btnChangePassword);
        btnChangePassword.setOnClickListener(this);
        apiService = APIUrl.getClient().create(APIService.class);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnChangePassword:
                strOldPassword = oldPassword.getText().toString();
                strNewPassword = newPassword.getText().toString();
                strConfirmPassword = confirmPassword.getText().toString();
               if(!isNetworkAvailable()){
                        Toasty.error(getApplicationContext(),getResources().getString(R.string.error_msg_no_internet),Toasty.LENGTH_SHORT).show();
                        return;
                }
                if(TextUtils.isEmpty(strOldPassword)){
                   // oldPassword.setError("Enter Old Password");
                    Toasty.error(getApplicationContext(),getResources().getString(R.string.enterpassword),Toasty.LENGTH_SHORT).show();
                    return;
                }
                if(strOldPassword.length() < 6){
                    //oldPassword.setError("Enter Old Password should not be less than 6 character");
                    Toasty.error(getApplicationContext(),getResources().getString(R.string.enterpasswordvalidation),Toasty.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(strNewPassword)){
                    //newPassword.setError("Enter New Password");
                    Toasty.error(getApplicationContext(),getResources().getString(R.string.enternewpassword),Toasty.LENGTH_SHORT).show();
                    return;
                }
                if(strNewPassword.length() < 6){
                    //newPassword.setError("Enter New Password should not be less than 6 character");
                    Toasty.error(getApplicationContext(),getResources().getString(R.string.enterpasswordvalidation),Toasty.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(strConfirmPassword)){
                    //confirmPassword.setError("Enter Confirm Password");
                    Toasty.error(getApplicationContext(),"Enter Confirm Password",Toasty.LENGTH_SHORT).show();
                    return;
                }
                if(strConfirmPassword.length() < 6){
                   // confirmPassword.setError("Enter Confirm Password should not be less than 6 character");
                    Toasty.error(getApplicationContext(),"Enter New Password should not be less than 6 characters",Toasty.LENGTH_SHORT).show();
                    return;
                }
                if(!strNewPassword.equals(strConfirmPassword)){
                    Toasty.error(getApplicationContext(),"New password and confirm password doesn't match",Toasty.LENGTH_SHORT).show();
                    return;
                }
                changePassword(strOldPassword,strNewPassword);
                break;
        }
    }

    private void changePassword(String strOldPassword, String strNewPassword) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.please_wait));
        progressDialog.show();

        callChangePassword(strOldPassword,strNewPassword).enqueue(new Callback<ResponseResult>() {
            @Override
            public void onResponse(Call<ResponseResult> call, Response<ResponseResult> response) {
                progressDialog.dismiss();
                progressDialog.dismiss();
                try {
                    System.out.println("Response " + response.body().getResponse());
                    if (Integer.parseInt(response.body().getResponse()) == 101) {
                        Toasty.success(getApplicationContext(), getResources().getString(R.string.password_chaged_successfully), Toasty.LENGTH_LONG).show();
                        Intent intent = new Intent(ChangePasswordActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                        prefManager.connectDB();
                        prefManager.setBoolean("isLogin",false);
                        prefManager.closeDB();
                    } else if (Integer.parseInt(response.body().getResponse()) == 102) {
                        Toasty.error(getApplicationContext(), getResources().getString(R.string.account_block), Toasty.LENGTH_LONG).show();
                    } else if (Integer.parseInt(response.body().getResponse()) == 103) {
                        System.out.println(TAG + " Required Parameter Missing");
                        Toasty.error(getApplicationContext(), getResources().getString(R.string.requiredparameter), Toasty.LENGTH_LONG).show();
                    } else if (Integer.parseInt(response.body().getResponse()) == 104) {
                        System.out.println(TAG + " Invalid Key");
                        Toasty.error(getApplicationContext(), getResources().getString(R.string.passwordchangedfailed), Toasty.LENGTH_LONG).show();
                    } else if (Integer.parseInt(response.body().getResponse()) == 105) {
                        System.out.println(TAG + " Login failed");
                        Toasty.error(getApplicationContext(), getResources().getString(R.string.oldnewpasswordnotmatch), Toasty.LENGTH_LONG).show();
                    } else if (Integer.parseInt(response.body().getResponse()) == 106) {
                        System.out.println(TAG + " Page Not Found");
                        Toasty.error(ChangePasswordActivity.this, getResources().getString(R.string.serverdown), Toasty.LENGTH_SHORT).show();
                    } else if (Integer.parseInt(response.body().getResponse()) == 109) {
                        System.out.println(TAG + " Invalid Password");
                        Toasty.error(ChangePasswordActivity.this, getResources().getString(R.string.invalid_old_password), Toasty.LENGTH_SHORT).show();
                    }  else if (Integer.parseInt(response.body().getResponse()) == 110) {
                        System.out.println(TAG + "Invalide User");
                        Toasty.error(ChangePasswordActivity.this, getResources().getString(R.string.invalid_user), Toasty.LENGTH_SHORT).show();
                    } else {
                        System.out.println(TAG + " Else Close");
                        Toasty.error(ChangePasswordActivity.this, getResources().getString(R.string.serverdown), Toasty.LENGTH_SHORT).show();
                    }
                }catch (NumberFormatException|NullPointerException e){
                   // Toasty.error(ChangePasswordActivity.this, "Sever break down, please try after sometime ", Toasty.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseResult> call, Throwable t) {
                progressDialog.dismiss();
                errorOut(t);
            }
        });

    }

    private Call<ResponseResult> callChangePassword(String strOldPassword, String strNewPassword) {
        return apiService.callChangePassword(
                APIUrl.KEY,
                //mobileNo,
                //"1234567890",
                id,
               // strOldPassword,
                strNewPassword
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
}