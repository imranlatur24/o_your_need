package com.oyourneed.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.oyourneed.R;
import com.oyourneed.adapter.OrderListAdapter;
import com.oyourneed.data.APIService;
import com.oyourneed.data.APIUrl;
import com.oyourneed.data.ResponseResult;
import com.oyourneed.model.CountryListModel;
import com.oyourneed.model.OrderListModel;
import com.oyourneed.model.SupportList;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SupportActivity extends BaseActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Toolbar toolbar;
    private APIService apiService;
    private TextView txt_support_address,txt_support_mobileno,txt_support_email,txt_support_website,txt_support_whatsapp;
    private static ArrayList<SupportList> templistview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCurrentLanguage();
        setContentView(R.layout.activity_support);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.Support));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        apiService = APIUrl.getClient().create(APIService.class);
        txt_support_address = findViewById(R.id.txt_support_address);
        txt_support_mobileno = findViewById(R.id.txt_support_mobileno);
        txt_support_email = findViewById(R.id.txt_support_email);
        //txt_support_website = findViewById(R.id.txt_support_website);
        txt_support_whatsapp = findViewById(R.id.txt_support_whatsapp);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        support();
    }

    //main categories data
    private void support() {
//        swipeContainer.setRefreshing(false);
        final ProgressDialog progressDialog = new ProgressDialog(this, R.style.AppCompatAlertDialogStyle);
        progressDialog.setMessage(getResources().getString(R.string.please_wait));
        progressDialog.show();

        templistview = new ArrayList<>();

        orderlist().enqueue(new Callback<ResponseResult>() {
            @Override
            public void onResponse(Call<ResponseResult> call, Response<ResponseResult> response) {
                try {
                    if (Integer.parseInt(response.body().getResponse()) == 101) {
                        progressDialog.dismiss();
                        System.out.println("response support :"+response.body().getResponse());

                        templistview = response.body().getSupportList();
                        System.out.println("response Arraylist order list:"+templistview);

                        txt_support_mobileno.setText(response.body().getSupportList().get(0).getSub_mobile());
                        txt_support_whatsapp.setText(response.body().getSupportList().get(0).getSub_whtapp_no());
                        txt_support_email.setText(response.body().getSupportList().get(0).getSub_email());
                        txt_support_address.setText(response.body().getSupportList().get(0).getSub_address());
                        }
                    else if (Integer.parseInt(response.body().getResponse()) == 105) {
                        progressDialog.dismiss();
                         Toasty.error(getApplicationContext(), getResources().getString(R.string.account_block), Toast.LENGTH_LONG).show();
                    } else {
                        progressDialog.dismiss();
                         Toasty.error(getApplicationContext(), getResources().getString(R.string.serverdown), Toast.LENGTH_LONG).show();
                    }
                } catch (NullPointerException | NumberFormatException e) {
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ResponseResult> call, Throwable t) {
                progressDialog.dismiss();
                errorOut(t);
                Toasty.error(getApplicationContext(), getResources().getString(R.string.serverdown), Toast.LENGTH_LONG).show();

            }
        });
    }

    //categories
    private Call<ResponseResult> orderlist() {
        System.out.println("API KEY : " + APIUrl.KEY);
        return apiService.support(
                APIUrl.KEY
        );
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng location = new LatLng(18.4083728,76.5415475);
        mMap.addMarker(new MarkerOptions().position(location).title(getResources().getString(R.string.app_name)));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location,17));
        mMap.setBuildingsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setMapToolbarEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
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
