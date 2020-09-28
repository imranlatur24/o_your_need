package com.oyourneed.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.oyourneed.R;
import com.oyourneed.adapter.OrderListAdapter;
import com.oyourneed.data.APIService;
import com.oyourneed.data.APIUrl;
import com.oyourneed.data.ResponseResult;
import com.oyourneed.model.OrderListModel;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrdersListActivity extends BaseActivity implements View.OnClickListener {

    private Toolbar toolbar;
    private RecyclerView recyclerView_orders;
    private OrderListAdapter OrderAdapter;
    private TextView text_total;
    private OrdersListActivity context;
    public static ArrayList<OrderListModel> templistview;
    private static final String TAG = "OrdersListActivity";
    int selection = -1;
    MenuItem searchItem;
    LinearLayoutManager linearLayoutManager;
    private Menu menu;
    private Animation rotation;
    private TextView txt_Filters,txt_items,txt_order,txt_date;
    private String cat_id, cat_name;
    private LinearLayout linearFilter,linear_cart_bottom;
    private APIService apiService;
    private String order_no,user_id,date,total_amt;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCurrentLanguage();
        setContentView(R.layout.activity_order);
        Intent intent = getIntent();
        date = intent.getExtras().getString("date");
        order_no = intent.getExtras().getString("order_no");
        user_id = intent.getExtras().getString("user_id");
        total_amt = intent.getExtras().getString("total_amt");

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(order_no);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        init();
        orderList();
    }

    private void init()
    {
        apiService = APIUrl.getClient().create(APIService.class);

        //for recyclerview
        //initilize recyclerview
        txt_order = findViewById(R.id.txt_order);
        txt_items = findViewById(R.id.txt_items);
        txt_date = findViewById(R.id.txt_date);
        txt_Filters = findViewById(R.id.txt_Filters);
        txt_Filters.setVisibility(View.GONE);
        txt_items.setVisibility(View.GONE);
        txt_date.setVisibility(View.VISIBLE);
        txt_order.setVisibility(View.VISIBLE);
        txt_date.setText("Date: "+date);

        txt_Filters.setOnClickListener(this);
        //for recyclerview
        //initilize recyclerview
        linearFilter = findViewById(R.id.linearFilter);
        linear_cart_bottom = findViewById(R.id.linear_cart_bottom);
        linear_cart_bottom.setVisibility(View.VISIBLE);
        linearFilter.setOnClickListener(this);
        text_total = findViewById(R.id.text_total);
        text_total.setVisibility(View.VISIBLE);
        txt_order.setText("Order Id: "+order_no);
        recyclerView_orders = findViewById(R.id.recyclerView_orders);
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
            case android.R.id.home :
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
        progressDialog.setMessage(getResources().getString(R.string.please_wait));
        progressDialog.show();
        progressDialog.setCancelable(false);

        templistview = new ArrayList<>();

        orderlist().enqueue(new Callback<ResponseResult>() {
            @Override
            public void onResponse(Call<ResponseResult> call, Response<ResponseResult> response) {
                try {
                    if (Integer.parseInt(response.body().getResponse()) == 101) {
                        progressDialog.dismiss();
                        System.out.println("response OrderListActivity :"+response.body().getResponse());

                        double total = 0.0, price = 0.0, item = 0.0;
                        OrderAdapter = new OrderListAdapter(OrdersListActivity.this);
                        recyclerView_orders.setAdapter(OrderAdapter);
                        OrderAdapter.setListArray(response.body().getOrder_product_list());

                        templistview = response.body().getOrder_product_list();
                        System.out.println("response Arraylist order list:"+templistview);

                        Log.d("order id: ", response.body().getOrder_product_list().get(0).getOrderNo());
                        for(OrderListModel cartdb :response.body().getOrder_product_list()){
                            total = total + 1;
                            item = item + Double.parseDouble(cartdb.getProductQty());
                            price = price + Double.parseDouble(cartdb.getProductTotalPrice());
                        }

                        text_total.setText(" Item: "+total+","+" Qty : "+item+","+" Total : "+getResources().getString(R.string.Rs)+total_amt);
                        text_total.setText("Total : "+getResources().getString(R.string.Rs)+total_amt);
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
                Toasty.error(getApplicationContext(), getResources().getString(R.string.serverdown), Toast.LENGTH_LONG).show();

            }
        });
    }

    //categories
    private Call<ResponseResult> orderlist() {
        System.out.println("API KEY : " + APIUrl.KEY + " USER ID " + user_id +" : ORDER NO : "+order_no );
        return apiService.orderWProductList(
                APIUrl.KEY,
                user_id,
                order_no
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
                    OrderAdapter.getFilter(searchQuery.toString().trim());
                    recyclerView_orders.invalidate();
                    return true;
                }catch (NullPointerException e){

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
                    Toasty.error(OrdersListActivity.this, getResources().getString(R.string.error_msg_no_internet), Toast.LENGTH_SHORT).show();
                } else {
                    startActivity(new Intent(OrdersListActivity.this, CartListActivity.class));}
            }
        });
        return true;
    }

    @Override

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txt_Filters:
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
                {getResources().getString(R.string.prize),
                        getResources().getString(R.string.Qty)
                };

        AlertDialog.Builder builder = new AlertDialog.Builder(OrdersListActivity.this, R.style.AppCompatAlertDialogStyle);
        builder.setTitle(getResources().getString(R.string.sortby));
        builder.setSingleChoiceItems(artSort, selection, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichlist) {
                dialog.dismiss();
                switch (whichlist) {
                    case 0:
                        sortByRelevance();
                        selection = 0;
                        break;
                    case 1:
                        sortByDistance();
                        selection = 1;
                        break;
                }
            }
        });
        builder.show();
    }

    //filter by relevance/normal
    private void sortByRelevance() {

        if (!templistview.isEmpty()) {
            //if only single record found
            if (templistview.size() == 1) {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.operationcanno), Toast.LENGTH_LONG).show();
            } else {

                // list not emplty more than 1 record..
                new Thread() {
                    public void run() {
                        try {
                            for (int i = 0; i < templistview.size(); i++) {
                                for (int j = i + 1; j < templistview.size(); j++) {
                                    if (Float.parseFloat(templistview.get(i).getProductTotalPrice()) >
                                            Float.parseFloat(templistview.get(j).getProductTotalPrice())) {
                                        OrderListModel temp = templistview.get(i);
                                        templistview.set(i, templistview.get(j));
                                        templistview.set(j, temp);
                                    }
                                }
                            }
                        } catch (WindowManager.BadTokenException b) {
                            System.out.println(getResources().getString(R.string.norecordfound)+ b.getMessage());
                        } catch (Exception e) {
                            System.out.println(getResources().getString(R.string.norecordfound) + e.getMessage());
                        }
                        try {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    System.out.println("### " + templistview.get(0).getProductTotalPrice());
                                    System.out.println("### " + templistview.get(1).getProductTotalPrice());
                                   try{
                                       OrderAdapter.setListArray(templistview);
                                   }catch (NullPointerException e)
                                   {
                                       Toasty.error(getApplicationContext(), getResources().getString(R.string.samerecoredfound), Toast.LENGTH_LONG).show();
                                   }
                                }
                            });
                        } catch (NullPointerException | ArrayIndexOutOfBoundsException e) {
                            System.out.println(getResources().getString(R.string.nodatafound) + e.getMessage());
                        }
                    }
                }.start();
            }
        } else {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.norecordfound), Toast.LENGTH_LONG).show();
        }
    }

    //filter by distance LOw To Hight
    private void sortByDistance() {
        if (!templistview.isEmpty()) {

            if (templistview.size() == 1) {
                // list not emplty more than 1 record..
                Toast.makeText(getApplicationContext(),getResources().getString(R.string. operationcanno), Toast.LENGTH_LONG).show();
            } else {

                new Thread() {
                    public void run() {

                        //operation perform for more than 1 record
                        try {
                            for (int i = 0; i < templistview.size(); i++) {
                                for (int j = i + 1; j < templistview.size(); j++) {
                                    if (Float.parseFloat(templistview.get(i).getProductMinimumQty())
                                            > Float.parseFloat(templistview.get(j).getProductMinimumQty())) {
                                        OrderListModel temp = templistview.get(i);
                                        templistview.set(i, templistview.get(j));
                                        templistview.set(j, temp);
                                    }
                                }
                            }
                        } catch (ArrayIndexOutOfBoundsException e) {
                            System.out.println(getResources().getString(R.string.norecordfound) + e.getMessage());

                        } catch (NullPointerException e) {
                            System.out.println(getResources().getString(R.string.norecordfound) + e.getMessage());

                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    System.out.println("### " + templistview.get(0).getProductMinimumQty());
                                    System.out.println("### " + templistview.get(1).getProductMinimumQty());
                                    try {
                                        OrderAdapter.setListArray(templistview);
                                    }catch (NullPointerException e)
                                    {
                                        Toasty.error(getApplicationContext(),getResources().getString(R.string. samerecoredfound), Toast.LENGTH_LONG).show();
                                    }
                                } catch (ArrayIndexOutOfBoundsException e) {
                                    System.out.println(getResources().getString(R.string.norecordfound)+ e.getMessage());

                                } catch (NullPointerException e) {
                                    System.out.println(getResources().getString(R.string.norecordfound) + e.getMessage());
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

}
