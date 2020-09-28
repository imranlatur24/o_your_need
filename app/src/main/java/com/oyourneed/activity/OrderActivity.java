package com.oyourneed.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.oyourneed.R;
import com.oyourneed.adapter.OrderAdapter;
import com.oyourneed.data.APIService;
import com.oyourneed.data.APIUrl;
import com.oyourneed.data.ResponseResult;
import com.oyourneed.model.OrderList;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderActivity extends BaseActivity implements View.OnClickListener {

    private Toolbar toolbar;
    private RecyclerView recyclerView_orders;
    private OrderActivity context;
    private OrderAdapter OrderAdapter;
    public static ArrayList<OrderList> templistview;
    private static final String TAG = "OrderActivity";
    int selection = -1;
    MenuItem searchItem;
    LinearLayoutManager linearLayoutManager;
    private Menu menu;
    private Animation rotation;
    private TextView txt_Filters, txt_items;
    private String cat_id, cat_name, user_id;
    private LinearLayout linearFilter;
    private APIService apiService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCurrentLanguage();
        setContentView(R.layout.order);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.my_orders));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        init();
        orderList();
    }

    private void init() {
        apiService = APIUrl.getClient().create(APIService.class);

        prefManager.connectDB();
        user_id = prefManager.getString("userId");
        prefManager.closeDB();

        //for recyclerview
        //initilize recyclerview
        txt_Filters = findViewById(R.id.txt_Filtersmyorder);
        txt_items = findViewById(R.id.txt_items);

        txt_Filters.setOnClickListener(this);
        //for recyclerview
        //initilize recyclerview
        linearFilter = findViewById(R.id.myorderslinearFilter);
        linearFilter.setOnClickListener(this);
        recyclerView_orders = findViewById(R.id.recyclerView_myorders);
        recyclerView_orders.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView_orders.setLayoutManager(linearLayoutManager);
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


    //main categories data
    private void orderList() {
//        swipeContainer.setRefreshing(false);
        final ProgressDialog progressDialog = new ProgressDialog(this, R.style.AppCompatAlertDialogStyle);
        progressDialog.setMessage(getResources().getString(R.string.loading));
        progressDialog.show();
        progressDialog.setCancelable(false);
        templistview = new ArrayList<>();


        callCategory().enqueue(new Callback<ResponseResult>() {
            @Override
            public void onResponse(Call<ResponseResult> call, Response<ResponseResult> response) {
                try {
                    if (Integer.parseInt(response.body().getResponse()) == 101) {
                        progressDialog.dismiss();
                        System.out.println("response Ordr Activity Response :"+response.body().getResponse());

                        OrderAdapter = new OrderAdapter(OrderActivity.this);
                        recyclerView_orders.setAdapter(OrderAdapter);
                        OrderAdapter.setListArray(response.body().getOrder_list());

                        templistview = response.body().getOrder_list();
                        System.out.println("response Arraylist  order :"+templistview);

                        txt_items.setText("Order " + String.valueOf(templistview.size()));
                        Log.d("order list: ", String.valueOf(templistview.size()));

                    } else if (Integer.parseInt(response.body().getResponse()) == 102) {
                        progressDialog.dismiss();
                        linearFilter.setVisibility(View.GONE);
                        recyclerView_orders.setVisibility(View.GONE);
                        Toasty.error(getApplicationContext(), getResources().getString(R.string.account_block), Toast.LENGTH_LONG).show();
                    } else if (Integer.parseInt(response.body().getResponse()) == 103) {
                        progressDialog.dismiss();
                        System.out.println(TAG + " Required Parameter Missing");
                        Toasty.error(getApplicationContext(), getResources().getString(R.string.requiredparameter), Toast.LENGTH_LONG).show();
                    } else if (Integer.parseInt(response.body().getResponse()) == 104) {
                        progressDialog.dismiss();
                        System.out.println(TAG + " Invalid Key");
                        Toasty.error(getApplicationContext(), getResources().getString(R.string.invalid_key), Toast.LENGTH_LONG).show();
                    } else if (Integer.parseInt(response.body().getResponse()) == 105) {
                        progressDialog.dismiss();
                        System.out.println(TAG + " Login failed");
                        Toasty.error(getApplicationContext(), getResources().getString(R.string.login_failed), Toast.LENGTH_LONG).show();
                    } else if (Integer.parseInt(response.body().getResponse()) == 110) {
                        progressDialog.dismiss();
                        linearFilter.setVisibility(View.GONE);
                        recyclerView_orders.setVisibility(View.GONE);
                        System.out.println(TAG + " Page Not Found");
                        Toasty.error(getApplicationContext(), getResources().getString(R.string.productnotfound), Toast.LENGTH_SHORT).show();
                    } else {
                        progressDialog.dismiss();
                        linearFilter.setVisibility(View.GONE);
                        recyclerView_orders.setVisibility(View.GONE);
                        System.out.println(TAG + " Else Close");
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
            }
        });
    }

    //categories
    private Call<ResponseResult> callCategory() {
        System.out.println("API KEY " + APIUrl.KEY + " : CAT ID " + cat_id);
        return apiService.getOrderList(
                APIUrl.KEY,
                user_id
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.products_menu, menu);
        this.menu = menu;
        searchItem = menu.findItem(R.id.action_search);
        menu.findItem(R.id.action_cart).setVisible(false);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
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
                    OrderAdapter.getFilter(searchQuery);
                    recyclerView_orders.invalidate();
                    return true;
                } catch (NullPointerException e) {

                    return true;
                }
            }
        });

        ImageView action_cart = (ImageView) menu.findItem(R.id.action_cart).getActionView();

        action_cart.setImageResource(R.drawable.ic_shopping_basket);
        action_cart.setPadding(8, 8, 8, 8);
        action_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isNetworkAvailable()) {
                    Toasty.error(OrderActivity.this, "No internet connection", Toast.LENGTH_SHORT).show();
                } else {
                    startActivity(new Intent(OrderActivity.this, CartListActivity.class));
                }
            }
        });
        return true;

    }

    @Override

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txt_Filtersmyorder:
                sort();
                break;

            case R.id.linearFilter:
                sort();
                break;
        }
    }

    private void sort() {

        //popup with radiobuttons
        String[] artSort = new String[]
                {
                        "Lower Price",
                        "Higher Price",
                };

        AlertDialog.Builder builder = new AlertDialog.Builder(OrderActivity.this, R.style.AppCompatAlertDialogStyle);
        builder.setTitle("Filters By Pricing..");
        builder.setSingleChoiceItems(artSort, selection, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichlist) {
                dialog.dismiss();
                switch (whichlist) {
                    case 0:
                        sortByDistance();
                        selection = 0;
                        break;
                    case 1:
                        sortByHiger();
                        selection = 1;
                        break;
                }
            }
        });
        builder.show();
    }

    //filter by lower
    private void sortByDistance() {
        if (!templistview.isEmpty()) {

            if (templistview.size() == 1) {
                // list not emplty more than 1 record..
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.operationcanno), Toast.LENGTH_LONG).show();
            } else {

                new Thread() {
                    public void run() {
                        //operation perform for more than 1 record
                        try {
                            for (int i = 0; i < templistview.size(); i++) {
                                for (int j = i + 1; j < templistview.size(); j++) {
                                    if (Float.parseFloat(templistview.get(i).getFinalTotalAmount()) >
                                            Float.parseFloat(templistview.get(j).getFinalTotalAmount())) {
                                        OrderList temp = templistview.get(i);
                                        templistview.set(i, templistview.get(j));
                                        templistview.set(j, temp);
                                    }
                                }
                            }
                        } catch (ArrayIndexOutOfBoundsException e) {
                            System.out.println("No Data Found " + e.getMessage());

                        } catch (NullPointerException e) {
                            System.out.println("No Data Found " + e.getMessage());

                        }


                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    System.out.println("### " + templistview.get(0).getFinalTotalAmount());
                                    System.out.println("### " + templistview.get(1).getFinalTotalAmount());
                                    OrderAdapter.setListArray(templistview);
                                } catch (ArrayIndexOutOfBoundsException e) {
                                    System.out.println(getResources().getString(R.string.norecordfound) + e.getMessage());

                                } catch (NullPointerException e) {
                                    System.out.println("No Data Found " + e.getMessage());
                                }
                            }
                        });
                    }
                }.start();
            }

        } else {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.norecordfound), Toast.LENGTH_LONG).show();
        }

    }

    //filter by higher
    private void sortByHiger() {
        if (!templistview.isEmpty()) {

            if (templistview.size() == 1) {
                // list not emplty more than 1 record..
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.operationcanno), Toast.LENGTH_LONG).show();
            } else {

                new Thread() {
                    public void run() {
                        //operation perform for more than 1 record
                        try {
                            for (int i = 0; i < templistview.size(); i++) {
                                for (int j = i + 1; j < templistview.size(); j++) {
                                    if (Float.parseFloat(templistview.get(i).getFinalTotalAmount()) <
                                            Float.parseFloat(templistview.get(j).getFinalTotalAmount())) {
                                        OrderList temp = templistview.get(i);
                                        templistview.set(i, templistview.get(j));
                                        templistview.set(j, temp);
                                    }
                                }
                            }
                        } catch (ArrayIndexOutOfBoundsException e) {
                            System.out.println(getResources().getString(R.string.norecordfound) + e.getMessage());

                        } catch (NullPointerException e) {
                            System.out.println("No Data Found " + e.getMessage());

                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    System.out.println("### " + templistview.get(0).getFinalTotalAmount());
                                    System.out.println("### " + templistview.get(1).getFinalTotalAmount());
                                    OrderAdapter.setListArray(templistview);
                                } catch (ArrayIndexOutOfBoundsException e) {
                                    System.out.println(getResources().getString(R.string.norecordfound) + e.getMessage());

                                } catch (NullPointerException e) {
                                    System.out.println(getResources().getString(R.string.norecordfound)+ e.getMessage());
                                }
                            }
                        });
                    }
                }.start();
            }

        } else {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.norecordfound), Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onBackPressed() {

        if (!getIntent().getBooleanExtra("order", false)) {
            super.onBackPressed();
        } else {
            Intent intent = new Intent(OrderActivity.this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }
}
