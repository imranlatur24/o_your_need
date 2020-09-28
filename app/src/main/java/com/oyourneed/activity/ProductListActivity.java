package com.oyourneed.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.oyourneed.R;
import com.oyourneed.adapter.ProductAdapter;
import com.oyourneed.data.APIService;
import com.oyourneed.data.APIUrl;
import com.oyourneed.data.ResponseResult;
import com.oyourneed.model.Cartdb;
import com.oyourneed.model.LocalityList;
import com.oyourneed.view.CountDrawable;

import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductListActivity extends BaseActivity implements View.OnClickListener, ProductAdapter.CallbackInterface {
    private Toolbar toolbar;
    private RecyclerView recyclerView_products;
    private ProductAdapter productAdapter;
    public ProductListActivity context;
    private Menu menu;
    private Animation rotation;
    private TextView txt_Filters,txt_items;
    private String cat_id, cat_name;
    private APIService apiService;
    private static final String TAG = "ProductListActivity";
    private LinearLayoutManager linearLayoutManager;
    private LinearLayout linearFilter;
    private static ArrayList<LocalityList> templistview;
    private int selection = -1;
    private MenuItem searchItem;
    private Button btnRetry;
    private LinearLayout errorLayout;
    private TextView txtError;
    private String language_id="";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_list);
        setCurrentLanguage();
        init();

        productList();
    }

    private void init() {

        Intent intent = getIntent();
        cat_id = intent.getExtras().getString("cat_id");
        cat_name = intent.getExtras().getString("cat_name");

        prefManager.connectDB();
        language_id = prefManager.getString("language_id");
        prefManager.closeDB();

        Log.d(TAG, "#CATEGORY ID : " + cat_id);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(cat_name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        apiService = APIUrl.getClient().create(APIService.class);

        txt_Filters = findViewById(R.id.txt_Filters);
        txt_items = findViewById(R.id.txt_items);
        errorLayout = (LinearLayout) findViewById(R.id.error_layout);
        btnRetry = (Button) findViewById(R.id.error_btn_retry);
        txtError = (TextView) findViewById(R.id.error_txt_cause);
        btnRetry.setOnClickListener(this);
        
        txt_Filters.setOnClickListener(this);
        //for recyclerview
        //initilize recyclerview
        linearFilter = findViewById(R.id.linearFilter);
        linearFilter.setOnClickListener(this);
        recyclerView_products = findViewById(R.id.recyclerView_products);
        recyclerView_products.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView_products.setLayoutManager(linearLayoutManager);
    }

    //main categories data
    private void productList() {
//        swipeContainer.setRefreshing(false);
        final ProgressDialog progressDialog = new ProgressDialog(this, R.style.AppCompatAlertDialogStyle);
        progressDialog.setMessage("loading");
        progressDialog.show();
        progressDialog.setCancelable(false);

        templistview = new ArrayList<>();
        linearFilter.setVisibility(View.GONE);
        recyclerView_products.setVisibility(View.GONE);
        callCategory().enqueue(new Callback<ResponseResult>() {
            @Override
            public void onResponse(Call<ResponseResult> call, Response<ResponseResult> response) {
                try {
                    if (Integer.parseInt(response.body().getResponse()) == 101) {
                        System.out.println("response Product wise : "+response.body().getResponse());
                        progressDialog.dismiss();
                        productAdapter = new ProductAdapter(ProductListActivity.this,true);
                        recyclerView_products.setAdapter(productAdapter);
                        linearFilter.setVisibility(View.VISIBLE);
                        recyclerView_products.setVisibility(View.VISIBLE);
                        errorLayout.setVisibility(View.GONE);
                        productAdapter.setListArray(response.body().getProduct_list());
                        templistview = response.body().getProduct_list();
                        txt_items.setText("Item "+String.valueOf(templistview.size()));
                    } else {
                        progressDialog.dismiss();
                        linearFilter.setVisibility(View.GONE);
                        recyclerView_products.setVisibility(View.GONE);
                        showErrorView(getResources().getString(R.string.norecordfound));
                        System.out.println(TAG + " Else Close"); }
                } catch (NullPointerException | NumberFormatException e) {
                    progressDialog.dismiss();
                    progressDialog.dismiss();
                    linearFilter.setVisibility(View.GONE);
                    recyclerView_products.setVisibility(View.GONE);
                    showErrorView(getResources().getString(R.string.norecordfound));
                    System.out.println(TAG + " Else Close");
                }
            }

            @Override
            public void onFailure(Call<ResponseResult> call, Throwable t) {
                progressDialog.dismiss();
                showErrorView(t);
            }
        });
    }

    //categories
    private Call<ResponseResult> callCategory() {
        System.out.println("API KEY " + APIUrl.KEY + " : CAT ID " + cat_id + " : Land id " + "1");
        return apiService.callProductWList(
                APIUrl.KEY,
                cat_id
                //language_id
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.products_menu, menu);
        this.menu = menu;
        searchItem = menu.findItem(R.id.action_search);
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
                    productAdapter.getFilter(searchQuery.toString().trim());
                    recyclerView_products.invalidate();
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
                startActivity(new Intent(ProductListActivity.this, CartListActivity.class));
            }
        });
        return true;

    }

    @Override

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txt_Filters:
                sort();
                //startActivity(new Intent(ProductListActivity.this, FiltersActivity.class));
                break;

            case R.id.linearFilter:
                //startActivity(new Intent(ProductListActivity.this, FiltersActivity.class));
                sort();
                break;

            case R.id.error_btn_retry:
                productList();
                break;
        }
    }

    private void sort() {

        //popup with radiobuttons
        String[] artSort = new String[]
                {"Relevance",
                        "Lower Prize",
                        "Higher Prize",
                };

        AlertDialog.Builder builder = new AlertDialog.Builder(ProductListActivity.this, R.style.AppCompatAlertDialogStyle);
        builder.setTitle("Sort By");
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
                    case 2:
                        sortByHighToLow();
                        selection = 2;
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
                                    if (Float.parseFloat(templistview.get(i).getProductId()) >
                                            Float.parseFloat(templistview.get(j).getProductId())) {
                                        LocalityList temp = templistview.get(i);
                                        templistview.set(i, templistview.get(j));
                                        templistview.set(j, temp);
                                    }
                                }
                            }
                        } catch (WindowManager.BadTokenException b) {
                            System.out.println(getResources().getString(R.string.norecordfound) + b.getMessage());
                        } catch (Exception e) {
                            System.out.println(getResources().getString(R.string.norecordfound) + e.getMessage());
                        }
                        try {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    System.out.println("### " + templistview.get(0).getProductId());
                                    System.out.println("### " + templistview.get(1).getProductId());
                                    productAdapter.setListArray(templistview);
                                }
                            });
                        } catch (ArrayIndexOutOfBoundsException e) {
                            System.out.println(getResources().getString(R.string.norecordfound)+ e.getMessage());

                        } catch (NullPointerException e) {
                            System.out.println(getResources().getString(R.string.norecordfound) + e.getMessage());

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
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.operationcanno), Toast.LENGTH_LONG).show();
            } else {

                new Thread() {
                    public void run() {

                        //operation perform for more than 1 record
                        try {
                            for (int i = 0; i < templistview.size(); i++) {
                                for (int j = i + 1; j < templistview.size(); j++) {
                                    if (Float.parseFloat(templistview.get(i).getProductPrice())
                                            > Float.parseFloat(templistview.get(j).getProductPrice())) {
                                        LocalityList temp = templistview.get(i);
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
                                    System.out.println("### " + templistview.get(0).getProductPrice());
                                    System.out.println("### " + templistview.get(1).getProductPrice());
                                    productAdapter.setListArray(templistview);
                                } catch (ArrayIndexOutOfBoundsException e) {
                                    System.out.println(getResources().getString(R.string.norecordfound) + e.getMessage());

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

    //filter by distance High To Low
    private void sortByHighToLow() {
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
                                    if (Float.parseFloat(templistview.get(i).getProductPrice())
                                            < Float.parseFloat(templistview.get(j).getProductPrice())) {
                                        LocalityList temp = templistview.get(i);
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
                                    System.out.println("### " + templistview.get(0).getProductPrice());
                                    System.out.println("### " + templistview.get(1).getProductPrice());
                                    productAdapter.setListArray(templistview);
                                } catch (ArrayIndexOutOfBoundsException e) {
                                    System.out.println(getResources().getString(R.string.norecordfound) + e.getMessage());

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

    @Override
    public void onHandleSelection(String text, ArrayList<Cartdb> list) {

    }

    @Override
    public void onResume(){
        super.onResume();
        // put your code here...
        try {
            productAdapter.setListArray(templistview);
            productAdapter.snackbar.dismiss();
        }catch (NullPointerException e){

        }
    }

    private void showErrorView(String error) {

        if (errorLayout.getVisibility() == View.GONE) {
            errorLayout.setVisibility(View.VISIBLE);
            txtError.setText(error);
        }
    }

    private void showErrorView(Throwable throwable) {

        if (errorLayout.getVisibility() == View.GONE) {
            errorLayout.setVisibility(View.VISIBLE);
            txtError.setText(fetchErrorMessage(throwable));
        }
    }

    private String fetchErrorMessage(Throwable throwable) {
        String errorMsg = getResources().getString(R.string.error_msg_unknown);

        if (!isNetworkAvailable()) {
            errorMsg = getResources().getString(R.string.error_msg_no_internet);
        } else if (throwable instanceof TimeoutException) {
            errorMsg = getResources().getString(R.string.error_msg_timeout);
        }

        return errorMsg;
    }

}
