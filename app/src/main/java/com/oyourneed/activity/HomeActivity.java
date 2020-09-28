package com.oyourneed.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.oyourneed.R;
import com.oyourneed.adapter.HomeMenuAdapter;
import com.oyourneed.adapter.RecyclerViewAdapter;
import com.oyourneed.adapter.SlidingImageAdapter;
import com.oyourneed.adapter.ViewPagerAdapter;
import com.oyourneed.data.APIService;
import com.oyourneed.data.APIUrl;
import com.oyourneed.data.ResponseResult;
import com.oyourneed.model.SliderList;
import com.oyourneed.model.WorkSpaceCity;
import com.oyourneed.model.WorkSpaceData;
import com.oyourneed.model.WorkSpaceLocality;
import com.oyourneed.view.CountDrawable;
import com.oyourneed.view.MyTextView;
import com.viewpagerindicator.CirclePageIndicator;
import com.wajahatkarim3.easyflipviewpager.CardFlipPageTransformer;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeoutException;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener,
        BottomNavigationView.OnNavigationItemSelectedListener,
        RecyclerViewAdapter.CallbackInterface {

    private boolean isLogin;
    private Toolbar toolbar;
    private FloatingActionButton fab;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    private TextView txtLoginName, txtLoginMobile;
    private View headerView;
    private Menu nav_Menu;
    private APIService apiService;
    private BottomNavigationView bottomNavigation;
    private String intCityId, strLoginName, strLoginMobile, strCity;
    private Menu menu;
    private ProgressDialog progressDialog1;
    private RecyclerView recyclerView_gridview, homeRecyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private GridLayoutManager gridLayoutManager;
    private HomeMenuAdapter homeMenuAdapter;
    private static final String TAG = "HomeActivity";
    //static slider
    ViewPager viewPager;
    private LinearLayout sliderDotsPanel;
    private int dotsCount;
    private ImageView[] dots;
    HomeActivity context;
    private static SwipeRefreshLayout swipeContainer;
    private CirclePageIndicator indicator;
    private ViewPagerAdapter viewPagerAdapter;
    private CardFlipPageTransformer pageTransformer;
    private static int currentPage = 0, NUM_PAGES = 4, w, position;
    private MyTextView edtCity;
    private TextView dialogTitle, dialogCancel, dialogOk;
    private ArrayList<String> cityArrayList, localityArrayList;
    private ArrayList<WorkSpaceCity> cityModelArrayList;
    private ArrayList<WorkSpaceLocality> localityModelArrayList;
    private ProgressDialog progressDialog, progressDialog3;
    private Dialog dialog;
    private RecyclerView dialogListView;
    private RecyclerViewAdapter adapter;
    private com.oyourneed.view.MyAutoCompleteTextView autoCompleteHomeCity;
    private String intLocalityId = "", intCategoryId = "", language_id = "";
    private LinearLayout error_layout;
    private TextView error_txt_cause;
    private int page = 1;
    private ArrayList<WorkSpaceData> workspaceModelArrayList = new ArrayList<>();
    private android.view.animation.Animation rotation;
    //private HomeAdapter mAdapter;
    private ArrayList<SliderList> sliderLists = new ArrayList<>();
    private Button btnRetry;
    private LinearLayout errorLayout;
    private TextView txtError;
    private Intent intent;
    int selection = -1;

    public HomeActivity() {

    }

    @SuppressLint("ValidFragment")
    public HomeActivity(HomeActivity context) {
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCurrentLanguage();
        setContentView(R.layout.activity_home);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        inti();
        onClickListener();
        sliderImages();
        categoryList();
    }



    private void inti() {

        apiService = APIUrl.getClient().create(APIService.class);

        prefManager.connectDB();
        isLogin = prefManager.getBoolean("isLogin");
        strCity = prefManager.getString("userCity");
        language_id = prefManager.getString("language_id");
        strLoginName = prefManager.getString("userFirstName") + " " + prefManager.getString("userLastName");
        strLoginMobile = prefManager.getString("userMobile");
        prefManager.closeDB();

        //for recyclerview
        recyclerView_gridview = (RecyclerView) findViewById(R.id.recyclerView_gridview);
        gridLayoutManager = new GridLayoutManager(getApplicationContext(), 3);
        recyclerView_gridview.setLayoutManager(gridLayoutManager);
        //Toasty.success(getApplicationContext(),"language id " +language_id,Toasty.LENGTH_SHORT).show();
        fab = (FloatingActionButton) findViewById(R.id.fab);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        headerView = navigationView.getHeaderView(0);
        txtLoginName = (TextView) headerView.findViewById(R.id.txtLoginName);
        txtLoginMobile = (TextView) headerView.findViewById(R.id.txtLoginMobile);
        nav_Menu = navigationView.getMenu();
        autoCompleteHomeCity = findViewById(R.id.autoCompleteHomeCity);
/*
        edtCity = (MyTextView) findViewById(R.id.edtCity);
        edtCity.setText(getResources().getString(R.string.aaroli_mumbai));
        edtCity.setOnClickListener(this);*/

        if (isLogin) {
            txtLoginName.setText(strLoginName);
            txtLoginMobile.setText(strLoginMobile);
            nav_Menu.findItem(R.id.nav_logout).setVisible(true);
            nav_Menu.findItem(R.id.nav_login).setVisible(false);
        } else {
            txtLoginName.setText(strLoginName);
            txtLoginMobile.setText(strLoginMobile);
            nav_Menu.findItem(R.id.nav_logout).setVisible(false);
            nav_Menu.findItem(R.id.nav_login).setVisible(true);
        }

        //for slider
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        //for slider
        indicator = (CirclePageIndicator) findViewById(R.id.indicator);

        errorLayout = (LinearLayout) findViewById(R.id.error_layout);
        btnRetry = (Button) findViewById(R.id.error_btn_retry);
        txtError = (TextView) findViewById(R.id.error_txt_cause);


        bottomNavigation = findViewById(R.id.bottomNavigation);
        bottomNavigation.setOnNavigationItemSelectedListener(this);

    }

    //all cities here
    private void cityList() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Getting City...");
        progressDialog.show();
        progressDialog.setCancelable(false);

        callCityList().enqueue(new Callback<ResponseResult>() {
            @Override
            public void onResponse(Call<ResponseResult> call, Response<ResponseResult> response) {
                try {
                    progressDialog.dismiss();
                    System.out.println("Response " + response.body().getResponse());
                    if (Integer.parseInt(response.body().getResponse()) == 101) {
                        cityModelArrayList = new ArrayList<>();
                        cityArrayList = new ArrayList<>();

                        cityModelArrayList = response.body().getWork_cities_list();

                        for (WorkSpaceCity data : cityModelArrayList) {
                            cityArrayList.add(data.getWork_city_name());
                        }

                        cityDialog(cityArrayList);

                        for (WorkSpaceCity data : cityModelArrayList) {
                            if (edtCity.getText().toString().equals(data.getWork_city_name())) {
                                intCityId = data.getWork_city_id();
                                localityList(intCityId);
                                return;
                            }
                        }
                    } else {
                        Toasty.error(getApplicationContext(), getResources().getString(R.string.serverdown), Toasty.LENGTH_LONG).show();
                    }


                } catch (NullPointerException | NumberFormatException e) {
                    progressDialog.dismiss();
                    Toasty.error(getApplicationContext(), getResources().getString(R.string.serverdown), Toasty.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<ResponseResult> call, Throwable t) {
                progressDialog.dismiss();
                errorOut(t);
            }
        });
    }

    private Call<ResponseResult> callCityList() {
        return apiService.callCityList(APIUrl.KEY, "1");
    }

    //all city show in dialog box
    private void cityDialog(ArrayList<String> stringArrayList) {
        dialog = new Dialog(HomeActivity.this);
        dialog.setContentView(R.layout.interested_city_box);
        dialog.setCancelable(false);
        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        int height = ViewGroup.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setLayout(width, height);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        //for choose city dialog
        dialogTitle = dialog.findViewById(R.id.dialogTitle);
        dialogTitle.setText(getResources().getString(R.string.choose_the_city));
        dialogListView = dialog.findViewById(R.id.dialogListView);
        dialogCancel = dialog.findViewById(R.id.dialogCancel);
        dialogOk = dialog.findViewById(R.id.dialogOk);

        adapter = new RecyclerViewAdapter(HomeActivity.this, stringArrayList, true);
        dialogListView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        dialogListView.setLayoutManager(linearLayoutManager);
        dialogListView.setAdapter(adapter);

        dialogCancel.setOnClickListener(this);
        dialogOk.setOnClickListener(this);

        dialog.show();
    }

    //city relevant data here
    private void localityList(String workspaceCityId) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.loading));
        progressDialog.show();
        progressDialog.setCancelable(false);
        callLocalityList(workspaceCityId).enqueue(new Callback<ResponseResult>() {
            @Override
            public void onResponse(Call<ResponseResult> call, Response<ResponseResult> response) {
                //dismissProgressDialog(progressDialog1);
                try {
                    System.out.println("Response " + response.body().getResponse());
                    if (Integer.parseInt(response.body().getResponse()) == 101) {
                        progressDialog.dismiss();
                        localityModelArrayList = new ArrayList<>();
                        localityArrayList = new ArrayList<>();

                        localityModelArrayList = response.body().getLocality_list();

                        for (WorkSpaceLocality data : localityModelArrayList) {
                            localityArrayList.add(data.getLocality_name());
                        }

                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(HomeActivity.this, R.layout.custom_tab, localityArrayList);
                        // Drop down layout style - list view with radio button
                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        autoCompleteHomeCity.setThreshold(0);
                        // attaching data adapter to spinner
                        autoCompleteHomeCity.setAdapter(dataAdapter);


                        //################
                        autoCompleteHomeCity.setVisibility(View.GONE);
                        autoCompleteHomeCity.setEnabled(true);
                        autoCompleteHomeCity.setHint(getResources().getString(R.string.search));
                    } else {
                        //################
                        autoCompleteHomeCity.setVisibility(View.GONE);
                        autoCompleteHomeCity.setEnabled(false);
                        autoCompleteHomeCity.setHint(getResources().getString(R.string.locality));
                    }

                } catch (NullPointerException | NumberFormatException e) {
                    progressDialog.dismiss();
                    autoCompleteHomeCity.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<ResponseResult> call, Throwable t) {
                //dismissProgressDialog(progressDialog1);
                progressDialog.dismiss();
                errorOut(t);
            }
        });

    }

    private Call<ResponseResult> callLocalityList(String workspaceCityId) {
        return apiService.callLocalityList(APIUrl.KEY, workspaceCityId, "1");
    }

    private void onClickListener() {
        fab.setOnClickListener(this);
        navigationView.setNavigationItemSelectedListener(this);

        autoCompleteHomeCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeActivity.this, AreaListActivity.class);
                i.putExtra("arrayList", localityModelArrayList);
                i.putExtra("city", edtCity.getText().toString());
                startActivityForResult(i, 200);
            }
        });


    }

    private void loginNow() {
     /*   new DroidDialog.Builder(HomeActivity.this)
                .animation(Animation.RELATIVE_TO_PARENT)
                .icon(R.drawable.ic_check)
                .title(getString(R.string.app_name))
                .content(getString(R.string.login_now))
                .cancelable(true, true)
                .positiveButton(getResources().getString(R.string.login_now2), new DroidDialog.onPositiveListener() {
                    @Override
                    public void onPositive(Dialog droidDialog) {
                        droidDialog.dismiss();
                        startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                    }
                }).negativeButton(getString(R.string.cancel), new DroidDialog.onNegativeListener() {
            @Override
            public void onNegative(Dialog droidDialog) {
                droidDialog.dismiss();
            }
        }).show();*/

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);
        alertDialogBuilder.setTitle(getString(R.string.app_name));
        alertDialogBuilder.setIcon(R.drawable.logo);
        alertDialogBuilder.setMessage(R.string.login_now);
        alertDialogBuilder.setCancelable(false);

        alertDialogBuilder.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                startActivity(new Intent(HomeActivity.this, LoginActivity.class));

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

    }


    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
       /*     new DroidDialog.Builder(HomeActivity.this)
                    .animation(Animation.RELATIVE_TO_PARENT)
                    .icon(R.drawable.ic_check)
                    .title(getString(R.string.app_name))
                    .content(getString(R.string.exit_now))
                    .cancelable(true, true)
                    .positiveButton(getResources().getString(R.string.exit), new DroidDialog.onPositiveListener() {
                        @Override
                        public void onPositive(Dialog droidDialog) {
                            droidDialog.dismiss();
                            HomeActivity.super.onBackPressed();
                        }
                    })
                    .negativeButton(getString(R.string.cancel), new DroidDialog.onNegativeListener() {
                        @Override
                        public void onNegative(Dialog droidDialog) {
                            droidDialog.dismiss();
                        }
                    })
                    .show();*/


            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);
            alertDialogBuilder.setTitle(getString(R.string.app_name));
            alertDialogBuilder.setIcon(R.drawable.logo);
            alertDialogBuilder.setMessage(getString(R.string.exit_now));
            alertDialogBuilder.setCancelable(false);

            alertDialogBuilder.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    HomeActivity.super.onBackPressed();

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

        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_homepage) {
            // Handle the camera action
        } else if (id == R.id.nav_profile) {

            if (isNetworkAvailable()) {
                startActivity(new Intent(HomeActivity.this, ProfileActivity.class));
            } else {
                Toasty.error(HomeActivity.this, getResources().getString(R.string.error_msg_no_internet), Toasty.LENGTH_SHORT).show();
            }
        } else if (id == R.id.nav_order) {
            if (isNetworkAvailable()) {
                startActivity(new Intent(HomeActivity.this, OrderActivity.class));
            } else {
                Toasty.error(HomeActivity.this, getResources().getString(R.string.error_msg_no_internet), Toasty.LENGTH_SHORT).show();
            }
        } else if (id == R.id.nav_aboutus) {
            if (isNetworkAvailable()) {
                startActivity(new Intent(HomeActivity.this, AboutUsActivity.class));
            } else {
                Toasty.error(HomeActivity.this, getResources().getString(R.string.error_msg_no_internet), Toasty.LENGTH_SHORT).show();
            }
        } else if (id == R.id.nav_cart) {
            startActivity(new Intent(HomeActivity.this, CartListActivity.class));
        } else if (id == R.id.nav_support) {
            startActivity(new Intent(HomeActivity.this, SupportActivity.class));
        } else if (id == R.id.nav_change_password) {
            if (isNetworkAvailable()) {
                startActivity(new Intent(HomeActivity.this, ChangePasswordActivity.class));
            } else {
                Toasty.error(HomeActivity.this, getResources().getString(R.string.error_msg_no_internet), Toasty.LENGTH_SHORT).show();
            }
        }/* else if (id == R.id.nav_change_language) {
            if (isNetworkAvailable()) {
                dialogLanguage();
            } else {
                Toasty.error(HomeActivity.this, getResources().getString(R.string.error_msg_no_internet), Toasty.LENGTH_SHORT).show();
            }
        } */else if (id == R.id.nav_share) {
            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name));
            sharingIntent.putExtra(Intent.EXTRA_TEXT, "Your Need App\nClick here https://play.google.com/store/apps/details?id=com.jg_wholesale to download the app");
            startActivity(Intent.createChooser(sharingIntent, getResources().getString(R.string.app_name)));
        } else if (id == R.id.nav_logout) {
            logoutNow();
        } else if (id == R.id.nav_login) {
            loginNow();
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
           /* case R.id.edtCity:
                if (isNetworkAvailable()) {
                    cityList();
                } else {
                    Toasty.error(HomeActivity.this, getResources().getString(R.string.error_msg_no_internet), Toasty.LENGTH_SHORT).show();
                }
                break;*/
            case R.id.dialogCancel:
                dialog.dismiss();
                break;
            case R.id.dialogOk:
              //  edtCity.setText(strCity);
                dialog.dismiss();
                break;
        }
    }

    private void setViewPager(final int num_pages) {
        final float density = getResources().getDisplayMetrics().density;

//Set circle indicator radius
        indicator.setRadius(3 * density);


        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == num_pages) {
                    currentPage = 0;
                }
                viewPager.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 3000, 3000);

        // Pager listener over indicator
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                currentPage = position;

            }

            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int pos) {

            }
        });
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
        getMenuInflater().inflate(R.menu.home, menu);
        this.menu = menu;

        MenuItem searchItem = menu.findItem(R.id.action_notification);
        ImageView locButton = (ImageView) menu.findItem(R.id.action_refresh_menu).getActionView();

        if (locButton != null) {
            locButton.setImageResource(R.drawable.ic_refresh);
            locButton.setPadding(8, 8, 8, 8);

            locButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!isNetworkAvailable()) {
                        Toasty.error(HomeActivity.this, getResources().getString(R.string.error_msg_no_internet), Toast.LENGTH_SHORT).show();
                    } else {
                        rotation = AnimationUtils.loadAnimation(HomeActivity.this, R.anim.rotate);
                        v.startAnimation(rotation);
                        categoryList();
                        sliderImages();
                    }
                }
            });
        }
        return true;
    }

    //sliders for homepage
    private void sliderImages() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.loading));
        progressDialog.show();
        progressDialog.setCancelable(false);
        callSignIn().enqueue(new Callback<ResponseResult>() {
            @Override
            public void onResponse(Call<ResponseResult> call, Response<ResponseResult> response) {
                progressDialog.dismiss();
                try {
                    if (Integer.parseInt(response.body().getResponse()) == 101) {
                        System.out.println("response slider : " + response.body().getResponse());
                        viewPager.setAdapter(new SlidingImageAdapter(HomeActivity.this, response.body().getSlider_list()));
                        indicator.setViewPager(viewPager);
                        pageTransformer = new CardFlipPageTransformer();
                        pageTransformer.setFlipOrientation(CardFlipPageTransformer.VERTICAL);
                        pageTransformer.setScalable(true);
                        viewPager.setPageTransformer(true, pageTransformer);
                        setViewPager(response.body().getSlider_list().size());
                        viewPager.setSystemUiVisibility(View.VISIBLE);

                    } else {
                        progressDialog.dismiss();
                        viewPager.setSystemUiVisibility(View.GONE);
                        System.out.println(TAG + " Else Close");
                        Toasty.error(getApplicationContext(), getResources().getString(R.string.invalidmobilenopassword), Toast.LENGTH_LONG).show();
                    }

                } catch (NullPointerException | NumberFormatException e) {
                    viewPager.setSystemUiVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<ResponseResult> call, Throwable t) {
                // dismissProgressDialog(progressDialog1);
                progressDialog.dismiss();
                viewPager.setSystemUiVisibility(View.GONE);
            }
        });
    }

    private Call<ResponseResult> callSignIn() {
        return apiService.callSlider(
                APIUrl.KEY
        );
    }

    //main categories data
    private void categoryList() {
//        swipeContainer.setRefreshing(false);
        final ProgressDialog progressDialog = new ProgressDialog(this, R.style.AppCompatAlertDialogStyle);
        progressDialog.setMessage(getResources().getString(R.string.loading));
        progressDialog.show();
        progressDialog.setCancelable(false);
        callCategory().enqueue(new Callback<ResponseResult>() {
            @Override
            public void onResponse(Call<ResponseResult> call, Response<ResponseResult> response) {
                try {

                    if (Integer.parseInt(response.body().getResponse()) == 101) {
                        System.out.println("response category : " + response.body().getResponse());
                        progressDialog.dismiss();
                        homeMenuAdapter = new HomeMenuAdapter(HomeActivity.this);
                        recyclerView_gridview.setAdapter(homeMenuAdapter);
                        errorLayout.setVisibility(View.GONE);
                        homeMenuAdapter.setListArray(response.body().getCategory_list());
                    } else {
                        progressDialog.dismiss();
                        recyclerView_gridview.setVisibility(View.GONE);
                        showErrorView(getResources().getString(R.string.norecordfound));
                        System.out.println(TAG + " Else Close");
                        Toasty.error(getApplicationContext(), getResources().getString(R.string.serverdown), Toast.LENGTH_SHORT).show();
                    }
                } catch (NullPointerException | NumberFormatException e) {
                    progressDialog.dismiss();
                    recyclerView_gridview.setVisibility(View.GONE);
                    showErrorView(getResources().getString(R.string.norecordfound));
                    Toasty.error(getApplicationContext(), getResources().getString(R.string.serverdown), Toast.LENGTH_SHORT).show();
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
        return apiService.callCategories(
                APIUrl.KEY
        );
    }

    //logout popus
    private void logoutNow() {
       /* new DroidDialog.Builder(HomeActivity.this)
                .animation(Animation.RELATIVE_TO_PARENT)
                .icon(R.drawable.ic_check)
                .title(getString(R.string.app_name))
                .content(getString(R.string.login_now))
                .cancelable(true, true)
                .positiveButton(getResources().getString(R.string.logout_now2), new DroidDialog.onPositiveListener() {
                    @Override
                    public void onPositive(Dialog droidDialog) {
                        droidDialog.dismiss();
                        prefManager.connectDB();
                        prefManager.setBoolean("isLogin", false);
                        prefManager.closeDB();
                        Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                })
                .negativeButton(getResources().getString(R.string.cancel), new DroidDialog.onNegativeListener() {
                    @Override
                    public void onNegative(Dialog droidDialog) {
                        droidDialog.dismiss();
                    }
                })
                .show();*/

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);
        alertDialogBuilder.setTitle(getString(R.string.app_name));
        alertDialogBuilder.setIcon(R.drawable.logo);
        alertDialogBuilder.setMessage(getString(R.string.login_now));
        alertDialogBuilder.setCancelable(false);

        alertDialogBuilder.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                prefManager.connectDB();
                prefManager.setBoolean("isLogin", false);
                prefManager.closeDB();
                Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
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


    }

    @Override
    public void onHandleSelection(int pos, boolean b) {
        if (b) {
            strCity = cityModelArrayList.get(pos).getWork_city_name();
            prefManager.connectDB();
            prefManager.setString("selectedCity", strCity);
            prefManager.closeDB();
            for (WorkSpaceCity data : cityModelArrayList) {
                if (strCity.equals(data.getWork_city_name())) {

                    intCityId = data.getWork_city_id();
                    intLocalityId = "";
                    autoCompleteHomeCity.setText(null);
                    autoCompleteHomeCity.setHint("Search...");
                    prefManager.connectDB();
                    prefManager.setString("selectedCityId", intCityId);
                    prefManager.closeDB();
                    localityList(intCityId);
                    return;
                }
            }
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

    //dialog change language
    private void dialogLanguage() {
        //popup with radiobuttons
        String[] artSort = new String[]
                {"English",
                        "हिंदी",
                        "मराठी"
                };

        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this, R.style.AppCompatAlertDialogStyle);
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
                        marathi();
                        selection = 1;
                        break;

                    case 2:
                        setLocale("mr");
                        recreate();
                        prefManager.connectDB();
                        prefManager.setString("language_id", "3");
                        prefManager.setString("language", "mr");
                        prefManager.closeDB();
                        hindi();
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

                        Intent intent = new Intent(HomeActivity.this, HomeActivity.class);
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
}