package com.oyourneed.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.RatingBar;

import com.oyourneed.R;
import com.oyourneed.data.APIService;
import com.oyourneed.data.APIUrl;
import com.oyourneed.data.ResponseResult;
import com.oyourneed.view.MyButton;
import com.oyourneed.view.MyEditText;

import es.dmoral.toasty.Toasty;
import hyogeun.github.com.colorratingbarlib.ColorRatingBar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeedbackActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "FeedbackActivity";
    private Toolbar toolbar;
    private ColorRatingBar seek_bar;
    private MyEditText edtFeedBack;
    private MyButton btnFeedback;
    private String id, strRating, strFeedBack;
    private ProgressDialog progressDialog;
    private APIService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCurrentLanguage();
        setContentView(R.layout.activity_feedback);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.feedback));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        inti();
        onClickListener();

    }

    private void inti() {

        prefManager.connectDB();
        id = prefManager.getString("userId");
        prefManager.closeDB();

        seek_bar = (ColorRatingBar) findViewById(R.id.seek_bar);
        edtFeedBack = (MyEditText) findViewById(R.id.edtFeedBack);
        btnFeedback = (MyButton) findViewById(R.id.btnFeedback);

        apiService = APIUrl.getClient().create(APIService.class);
    }

    private void onClickListener() {
        btnFeedback.setOnClickListener(this);
        seek_bar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float i, boolean fromUser) {
                strRating = String.valueOf((float) i);
            }
        });
    }

    private void sendFeedback(String id, String rating, String feedback) {
        progressDialog = ProgressDialog.show(this, null, null, true);
        showProgressDialog(progressDialog, getResources().getString(R.string.submitting_feedback));
        callSendFeedback(id, rating, feedback).enqueue(new Callback<ResponseResult>() {
            @Override
            public void onResponse(Call<ResponseResult> call, Response<ResponseResult> response) {
                progressDialog.dismiss();
                try {
                    System.out.println("Response " + response.body().getResponse());
                    if (Integer.parseInt(response.body().getResponse()) == 101) {
                        Toasty.success(getApplicationContext(), getResources().getString(R.string.feedback_send_successfully), Toasty.LENGTH_SHORT).show();
                        finish();
                    } else if (Integer.parseInt(response.body().getResponse()) == 102) {
                        Toasty.error(getApplicationContext(), getResources().getString(R.string.account_block), Toasty.LENGTH_LONG).show();
                    } else if (Integer.parseInt(response.body().getResponse()) == 103) {
                        System.out.println(TAG + " Required Parameter Missing");
                        Toasty.error(getApplicationContext(), getResources().getString(R.string.feedbackfailed), Toasty.LENGTH_LONG).show();
                    } else if (Integer.parseInt(response.body().getResponse()) == 104) {
                        System.out.println(TAG + " Invalid Key");
                        Toasty.error(getApplicationContext(), getResources().getString(R.string.feedbackfailed), Toasty.LENGTH_LONG).show();
                    } else if (Integer.parseInt(response.body().getResponse()) == 105) {
                        System.out.println(TAG + " Login failed");
                        Toasty.error(getApplicationContext(), getResources().getString(R.string.feedbackfailed), Toasty.LENGTH_LONG).show();
                    } else if (Integer.parseInt(response.body().getResponse()) == 106) {
                        System.out.println(TAG + " Page Not Found");
                        Toasty.error(getApplicationContext(), getResources().getString(R.string.serverdown), Toasty.LENGTH_LONG).show();
                    } else {
                        System.out.println(TAG + " Else Close");
                        Toasty.error(getApplicationContext(), getResources().getString(R.string.serverdown), Toasty.LENGTH_LONG).show();
                    }
                } catch (NullPointerException | NumberFormatException e) {
                    Toasty.error(getApplicationContext(), getResources().getString(R.string.serverdown), Toasty.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseResult> call, Throwable t) {
                progressDialog.dismiss();
                errorOut(t);
            }
        });

    }

    private Call<ResponseResult> callSendFeedback(String id, String strRating, String strFeedBack) {
        return apiService.callSendFeedback(
                APIUrl.KEY,
                strRating,
                strFeedBack,
                id);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnFeedback) {
            strFeedBack = edtFeedBack.getText().toString();
            if (!isNetworkAvailable()) {
                Toasty.error(getApplicationContext(), getResources().getString(R.string.error_msg_no_internet), Toasty.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(strRating)) {
                Toasty.error(getApplicationContext(), getResources().getString(R.string.rateus), Toasty.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(strFeedBack)) {
                edtFeedBack.setError(getResources().getString(R.string.enterfeedback));
                return;
            }
            sendFeedback(id, strRating, strFeedBack);
        }
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
