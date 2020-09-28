package com.oyourneed.activity;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.droidbyme.dialoglib.DroidDialog;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.oyourneed.R;
import com.oyourneed.adapter.ProductAdapter;
import com.oyourneed.data.APIService;
import com.oyourneed.data.APIUrl;
import com.oyourneed.data.AppDatabase;
import com.oyourneed.data.ResponseResult;
import com.oyourneed.model.Cartdb;
import com.oyourneed.model.ForgotModel;
import com.oyourneed.model.LocalityList;
import com.oyourneed.model.ProductModel;
import com.oyourneed.paytmex.checksum;
import com.oyourneed.view.CountDrawable;

import java.util.ArrayList;
import java.util.UUID;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartListActivity extends BaseActivity implements View.OnClickListener, ProductAdapter.CallbackInterface {
    private static final String TAG = "CartListActivity";

    private Toolbar toolbar;
    private RecyclerView recyclerView_carts;
    private ProductAdapter productAdapter;
    private TextView text_total;
    private Menu menu;
    private ArrayList<LocalityList> arrayList = new ArrayList<>();
    private ArrayList<ProductModel> arrayProduct = new ArrayList<>();
    private ArrayList<Cartdb> cartArrayList = new ArrayList<>();
    private double total = 0;
    private double price = 0;
    private double item = 0;
    private MenuItem searchItem;
    private Button btnRetry,submit_order,btnProcess,btnCancel;
    private LinearLayout errorLayout, linear_cart_bottom;
    private TextView txtError;
    private String user_id;
    private Dialog dialog,coddialog;
    private RadioButton cod,online;
    private RadioGroup radio_group;
    private EditText edtName,edtMobile,edtAddress;
    View v;
    private APIService apiService;
    private String name,mobileno,address;
    private Intent intent;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCurrentLanguage();
        setContentView(R.layout.cart_list);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.mycart));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        init();
        if (ContextCompat.checkSelfPermission(CartListActivity.this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(CartListActivity.this, new String[]{Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS}, 101);
        }
    }

    private void init() {

        prefManager.connectDB();
        user_id = prefManager.getString("userId");
        prefManager.closeDB();
        apiService = APIUrl.getClient().create(APIService.class);

        //for recyclerview
        //initilize recyclerview
        text_total = findViewById(R.id.text_total);
        errorLayout = (LinearLayout) findViewById(R.id.error_layout);
        linear_cart_bottom = (LinearLayout) findViewById(R.id.linear_cart_bottom);
        btnRetry = (Button) findViewById(R.id.error_btn_retry);
        submit_order = (Button) findViewById(R.id.submit_order);
        btnRetry.setText("Go Back");
        txtError = (TextView) findViewById(R.id.error_txt_cause);

        cartArrayList = (ArrayList<Cartdb>) AppDatabase.getAppDatabase(CartListActivity.this).adminDao().getAllProduct();
        //get data from roomer db and add  SET to LocalityList model call
        for(Cartdb cartdb :cartArrayList)
        {
            LocalityList data = new LocalityList();
            data.setProductId(cartdb.getPro_id());
            data.setProductName(cartdb.getTitle());
            data.setProductPrice(cartdb.getPro_price());
            data.setProduct_temp_price(cartdb.getProduct_temp_price());
            data.setProduct_discount(cartdb.getProduct_discount());
            data.setProductUnit(cartdb.getProduct_unit());
            data.setProductQty("1");
            data.setProductImage(cartdb.getPro_image());
            arrayList.add(data);
            total = total + 1;
            item = item + Double.parseDouble(cartdb.getPro_total());
            price = Double.parseDouble(cartdb.getPro_price())*Double.parseDouble(cartdb.getPro_total())+price+50;
        }

        //print total value of product ex 100 rupees
        text_total.setText(getResources().getString(R.string.order)+getResources().getString(R.string.Rs)+price+"\n Delivery Charges : "+getString(R.string.Rs)+50);

        //initilization of recyclerview in vertical format
        recyclerView_carts = findViewById(R.id.recyclerView_carts);
        recyclerView_carts.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL); // set Horizontal Orientation
        recyclerView_carts.setLayoutManager(linearLayoutManager); // set LayoutManager to RecyclerView

        //set productAdapter to recyclerview_cards
        productAdapter = new ProductAdapter(CartListActivity.this,false);
        productAdapter.setListArray(arrayList);
        recyclerView_carts.setAdapter(productAdapter);

        //if cardview CardDB is empty
        if(cartArrayList.isEmpty()){
            recyclerView_carts.setVisibility(View.GONE);
            linear_cart_bottom.setVisibility(View.GONE);
            errorLayout.setVisibility(View.VISIBLE);
        }else {//if not cardview CardDB is an empty
            recyclerView_carts.setVisibility(View.VISIBLE);
            linear_cart_bottom.setVisibility(View.VISIBLE);
            errorLayout.setVisibility(View.GONE);
        }
        btnRetry.setOnClickListener(this);
        submit_order.setOnClickListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home :
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void setCount(Context context, String count) {
        MenuItem menuItem = menu.findItem(R.id.action_cart);
        LayerDrawable icon = (LayerDrawable) menuItem.getIcon();

        CountDrawable badge;

        // Reuse drawable if possible
        Drawable reuse = icon.findDrawableByLayerId(R.id.ic_group_count);
        if (reuse != null && reuse instanceof CountDrawable) {
            badge = (CountDrawable) reuse;
        } else {
            badge = new CountDrawable(context);
        }

        badge.setCount(count);
        icon.mutate();
        icon.setDrawableByLayerId(R.id.ic_group_count, badge);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        prefManager.connectDB();
        setCount(this, "3");
        prefManager.closeDB();
        return true;
    }



    private void checkoutNow() {
        new DroidDialog.Builder(CartListActivity.this)
                .animation(Animation.RELATIVE_TO_PARENT)
                .icon(R.drawable.ic_check)
                .title(getString(R.string.app_name))
                .content(getString(R.string.Checkout_now))
                .cancelable(true, true)
                .positiveButton(getResources().getString(R.string.check_out_now), new DroidDialog.onPositiveListener() {
                    @Override
                    public void onPositive(Dialog droidDialog) {
                        droidDialog.dismiss();
                       // placeOrder();
                        /*System.out.println("prize "+price);
                        Intent intent = new Intent(CartListActivity.this, checksum.class);
                        intent.putExtra("orderid","ORD"+ UUID.randomUUID().toString());
                        intent.putExtra("custid","CUS"+ UUID.randomUUID().toString());
                        intent.putExtra("prize",String.valueOf(price));
                        startActivity(intent);*/
                        paymentDialog();


                    }
                })
                .negativeButton(getResources().getString(R.string.cancel), new DroidDialog.onNegativeListener() {
                    @Override
                    public void onNegative(Dialog droidDialog) {
                        droidDialog.dismiss();
                    }
                })
                .show();
    }
   /*     AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);
        alertDialogBuilder.setTitle(getString(R.string.app_name));
        alertDialogBuilder.setIcon(R.drawable.logo);
        alertDialogBuilder.setMessage(R.string.Checkout_now);
        alertDialogBuilder.setCancelable(false);

        alertDialogBuilder.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
               // droidDialog.dismiss();
                // placeOrder();
                       *//* System.out.println("prize "+price);
                        Intent intent = new Intent(CartListActivity.this, checksum.class);
                        intent.putExtra("orderid","ORD"+ UUID.randomUUID().toString());
                        intent.putExtra("custid","CUS"+ UUID.randomUUID().toString());
                        intent.putExtra("prize",String.valueOf(price));
                        startActivity(intent);*//*
                paymentDialog();
                //placeOrder();
            }
        });

        alertDialogBuilder.setNegativeButton(getResources().getString(R.string.No), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }*/
    //online payment
    private void paymentDialog() {

        dialog = new Dialog(CartListActivity.this, R.style.AppCompatAlertDialogStyle);
        dialog.setContentView(R.layout.choosepayment);
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
        Button btnCanecl = dialog.findViewById(R.id.btnCanecl);
        btnCanecl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        cod = dialog.findViewById(R.id.cod);
        online = dialog.findViewById(R.id.online);
        radio_group = dialog.findViewById(R.id.radio_group);
        // Is the current Radio Button checked?
        cod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                codAddress();
            }
        });
        online.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                System.out.println("prize "+price);
                Intent intent = new Intent(CartListActivity.this, checksum.class);
                intent.putExtra("orderid","ORD"+ UUID.randomUUID().toString());
                intent.putExtra("custid","CUS"+ UUID.randomUUID().toString());
                intent.putExtra("prize",String.valueOf(price));
                startActivity(intent);
            }
        });
        dialog.show();
    }
    //cash on delivery
    private void codAddress() {

        coddialog = new Dialog(CartListActivity.this, R.style.AppCompatAlertDialogStyle);
        coddialog.setContentView(R.layout.order_address_popup);
        coddialog.setCancelable(true);
        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        int height = ViewGroup.LayoutParams.WRAP_CONTENT;
        coddialog.getWindow().setLayout(width, height);
        coddialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        edtName = coddialog.findViewById(R.id.edtName);
        edtMobile = coddialog.findViewById(R.id.edtMobile);
        edtAddress = coddialog.findViewById(R.id.edtAddress);

        name = prefManager.getString("userFirstName")+(prefManager.getString("userLastName"));
        mobileno = prefManager.getString("userMobile");
        address = prefManager.getString("userAddress");
        prefManager.connectDB();

        edtName.setText(name);
        edtMobile.setText(mobileno);
        edtAddress.setText(address);
        prefManager.closeDB();

        btnProcess = coddialog.findViewById(R.id.btnProcess);
        btnCancel = coddialog.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                coddialog.dismiss();
            }
        });
        btnProcess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = edtName.getText().toString().trim();
                address = edtAddress.getText().toString().trim();
                mobileno = edtMobile.getText().toString().trim();

                if (!isNetworkAvailable()) {
                    Toasty.error(CartListActivity.this, getResources().getString(R.string.error_msg_no_internet), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(name)) {
                    Toasty.error(CartListActivity.this, getResources().getString(R.string.entername), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(mobileno)) {
                    Toasty.error(CartListActivity.this, getResources().getString(R.string.entermobile), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (String.valueOf(mobileno.charAt(0)).equals("0") ||
                        String.valueOf(mobileno.charAt(0)).equals("1") ||
                        String.valueOf(mobileno.charAt(0)).equals("2") ||
                        String.valueOf(mobileno.charAt(0)).equals("3") ||
                        String.valueOf(mobileno.charAt(0)).equals("4") ||
                        String.valueOf(mobileno.charAt(0)).equals("5") ||
                        String.valueOf(mobileno.charAt(0)).equals("6")) {
                    Toasty.error(CartListActivity.this, getResources().getString(R.string.entervalidmobileno), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(address)) {
                    Toasty.error(CartListActivity.this, getResources().getString(R.string.enter_address), Toast.LENGTH_SHORT).show();
                    return;
                }
                codapi(name,mobileno,address);
            }
        });

        coddialog.show();
    }
    private void codapi(String name,String mobileno,String address) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.please_wait));
        progressDialog.show();
        progressDialog.setCancelable(true);
        callForgot(name,mobileno,address).enqueue(new Callback<ResponseResult>() {
            @Override
            public void onResponse(Call<ResponseResult> call, Response<ResponseResult> response) {
                progressDialog.dismiss();
                try {
                    if (Integer.parseInt(response.body().getResponse()) == 101) {
                        progressDialog.dismiss();
                        coddialog.dismiss();

                        System.out.println("#response place order output : " + response.body().getResult_order_place().get(0).getPlace_order());
                        placeOrder();

                    } else if (Integer.parseInt(response.body().getResponse()) == 102) {
                        progressDialog.dismiss();
                        coddialog.dismiss();
                        Toasty.error(getApplicationContext(), getResources().getString(R.string.order_no_does_not_exist), Toast.LENGTH_LONG).show();
                    }else if (Integer.parseInt(response.body().getResponse()) == 103) {
                        progressDialog.dismiss();
                        coddialog.dismiss();
                        Toasty.error(getApplicationContext(), getResources().getString(R.string.serverdown), Toast.LENGTH_LONG).show();
                    } else {
                        progressDialog.dismiss();
                        coddialog.dismiss();
                        System.out.println(TAG + " Else Close");
                        Toasty.error(getApplicationContext(), getResources().getString(R.string.serverdown), Toast.LENGTH_LONG).show();
                    }
                } catch (NullPointerException | NumberFormatException e) {
                    progressDialog.dismiss();
                    coddialog.dismiss();
                    Toast.makeText(CartListActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseResult> call, Throwable t) {
                progressDialog.dismiss();
                coddialog.dismiss();
                 Toast.makeText(CartListActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private Call<ResponseResult> callForgot(String name,String mobileno,String address) {
        System.out.println("API KEY : "+APIUrl.KEY+" userid : "+user_id+" name "+name+" EYQV6JFC "+" address "+address+" mobile no "+mobileno);
        return apiService.ordercod(
                APIUrl.KEY,
                user_id,
                name,
                "EYQV6JFC",
                address,
                mobileno

        );
    }


    private void placeOrder() {
        cartArrayList = (ArrayList<Cartdb>) AppDatabase.getAppDatabase(CartListActivity.this).adminDao().getAllProduct();
        if (cartArrayList.isEmpty()) {
            Toasty.error(CartListActivity.this,getResources().getString(R.string.cartempty),Toasty.LENGTH_SHORT).show();
            return;
        }
        if(!isNetworkAvailable()){
            Toasty.error(CartListActivity.this,getResources().getString(R.string.error_msg_no_internet),Toasty.LENGTH_SHORT).show();
            return;
        }
        total = 0;
        item = 0;
        price = 0;
        for(Cartdb cartdb :cartArrayList){
            double sum = Double.parseDouble(cartdb.getPro_total())*Double.parseDouble(cartdb.getPro_price());
            ProductModel data = new ProductModel();
            data.setProduct_name(cartdb.getTitle());
            data.setProduct_image(cartdb.getPro_image());
            data.setProduct_price(cartdb.getPro_price());
            data.setProduct_minimum_qty("1");
            data.setProduct_unit("kg");
            data.setProduct_temp_price(cartdb.getProduct_temp_price());
            data.setProduct_discount(cartdb.getProduct_discount());
            data.setProduct_unit(cartdb.getProduct_unit());
            data.setProduct_qty(cartdb.getPro_total());
            data.setProduct_total_price(String.valueOf(sum));
            arrayProduct.add(data);
            total = total + 1;
            item = item + Double.parseDouble(cartdb.getPro_total());
            price = Double.parseDouble(cartdb.getPro_price())*Double.parseDouble(cartdb.getPro_total())+price+50;
        }

        // Use this builder to construct a Gson instance when you need to set configuration options other than the default.
        GsonBuilder gsonBuilder = new GsonBuilder();

        // This is the main class for using Gson. Gson is typically used by first constructing a Gson instance and then invoking toJson(Object) or fromJson(String, Class) methods on it.
        // Gson instances are Thread-safe so you can reuse them freely across multiple threads.
        Gson gson = gsonBuilder.create();

        String JSONObject = gson.toJson(arrayProduct);
        System.out.println("\nConverted JSONObject ==> " + JSONObject);

        final ProgressDialog progressDialog = new ProgressDialog(this, R.style.AppCompatAlertDialogStyle);
        progressDialog.setMessage(getResources().getString(R.string.loading));
        progressDialog.show();

        System.out.println("Order user_id "+user_id+" item "+item+" price"+price+" JSONObject"+JSONObject);


        APIUrl.getClient().create(APIService.class).callOrderPlace(APIUrl.KEY,
                user_id,
                String.valueOf(item),
                String.valueOf(price),
                address,
                JSONObject).enqueue(new Callback<ResponseResult>() {
            @Override
            public void onResponse(Call<ResponseResult> call, Response<ResponseResult> response) {
                progressDialog.dismiss();
                if(Integer.parseInt(response.body().getResponse()) == 101){
                    System.out.println("response CartListResponse :"+response.body().getResponse());
                    Toasty.success(CartListActivity.this,getResources().getString(R.string.orderplacedsuccefully),Toasty.LENGTH_SHORT).show();
                    AppDatabase.getAppDatabase(CartListActivity.this).adminDao().getAllDeleteProduct();
                    Intent intent = new Intent(CartListActivity.this, OrderActivity.class);
                    intent.putExtra("order",true);
                    System.out.println("Order Number by placeorder : "+true);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }else {
                    Toasty.error(CartListActivity.this,getResources().getString(R.string.error_order),Toasty.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseResult> call, Throwable t) {
                progressDialog.dismiss();
                //errorOut(t);
                Toast.makeText(getApplicationContext(), "error"+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case  R.id.error_btn_retry:
                finish();
                break;
            case R.id.submit_order:
                checkoutNow();
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.products_menu, menu);
        this.menu = menu;
        searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
       // ImageView locButton = (ImageView) menu.findItem(R.id.action_refresh_sort).getActionView();
        ImageView action_EditSearch = (ImageView) menu.findItem(R.id.action_search).getActionView();

        ((EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text)).setHintTextColor(getResources().getColor(R.color.white));
        ((EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text)).setTextColor(getResources().getColor(R.color.white));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String searchQuery) {
                try {
                    productAdapter.getFilter(searchQuery.toString().trim());
                    recyclerView_carts.invalidate();
                    return true;
                }catch (NullPointerException e){

                    return true;
                }
            }
        });
        return true;
    }


    @Override
    public void onHandleSelection(String text, ArrayList<Cartdb> list) {
        text_total.setText(text+"\n Delivery Charges : "+getString(R.string.Rs)+50);
        if(list.isEmpty()){
            recyclerView_carts.setVisibility(View.GONE);
            linear_cart_bottom.setVisibility(View.GONE);
            errorLayout.setVisibility(View.VISIBLE);
        }else {
            recyclerView_carts.setVisibility(View.VISIBLE);
            linear_cart_bottom.setVisibility(View.VISIBLE);
            errorLayout.setVisibility(View.GONE);
        }
    }
}
