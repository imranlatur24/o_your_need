package com.oyourneed.activity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.oyourneed.R;
import com.oyourneed.data.APIService;
import com.oyourneed.data.APIUrl;
import com.oyourneed.model.CityListModel;
import com.oyourneed.model.CountryListModel;
import com.oyourneed.model.DistrictModel;
import com.oyourneed.model.StateList;
import com.oyourneed.model.UpdateProfile;
import com.oyourneed.view.TextInputAutoCompleteTextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProfileActivity extends BaseActivity implements View.OnClickListener {
    private Toolbar toolbar;
    private Menu menu;
    private static final String TAG = "ProfileActivity";
    private ArrayList<StateList> stateLists;
    private ArrayList<CountryListModel.CountryList> countryLists;
    private ArrayList<CityListModel.CityList> cityLists;
    private ArrayList<String> stateName, cityName, countryName;
    private TextInputAutoCompleteTextView textinput_country, textinput_state, textinput_city;
    private EditText txt_FirstName, txt_LastName, txt_Email, txt_Pincode, txt_Address, txt_Mobile, txt_DOB;
    private Button btn_update;
    private String id, city = "", state = "", country = "";
    private String firstname, lastname, email, pincode, address, mobile, dob, countryname, statename, cityname;
    private String gcm_code = "1230";
    private int mYear, mMonth, mDay;
    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    private static final String MOBILE_PATTERN = "^(\\+91[\\-\\s]?)?[0]?(91)?[789]\\d{9}$";
    private Pattern pattern;
    private Matcher matcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCurrentLanguage();
        setContentView(R.layout.activity_profile);

        init();
        getCountry();
    }

    private void init() {

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.myprofile));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        pattern = Pattern.compile(EMAIL_PATTERN);

        prefManager.connectDB();
        id = prefManager.getString("userId");
        prefManager.closeDB();
        txt_FirstName = findViewById(R.id.txt_FirstName);
        txt_LastName = findViewById(R.id.txt_LastName);
        txt_DOB = findViewById(R.id.txt_DOB);
        txt_Mobile = findViewById(R.id.txt_Mobile);
        txt_Email = findViewById(R.id.txt_Email);
        txt_Pincode = findViewById(R.id.txt_Pincode);
        txt_Address = findViewById(R.id.txt_Address);

        textinput_country = findViewById(R.id.textinput_country);

        textinput_state = findViewById(R.id.textinput_state);
        textinput_city = findViewById(R.id.textinput_city);
        btn_update = findViewById(R.id.btnSave);
        textinput_state.setOnClickListener(this);
        textinput_city.setOnClickListener(this);
        btn_update.setOnClickListener(this);

        prefManager.connectDB();
        txt_FirstName.setText(prefManager.getString("userFirstName"));
        txt_LastName.setText(prefManager.getString("userLastName"));
        txt_Mobile.setText(prefManager.getString("userMobile"));
        txt_Email.setText(prefManager.getString("userEmail"));
        txt_DOB.setText(prefManager.getString("userDOB"));
        txt_Address.setText(prefManager.getString("userAddress"));
        txt_Pincode.setText(prefManager.getString("userPincode"));
        city = prefManager.getString("userCity");
        state = prefManager.getString("userState");
        country = prefManager.getString("userCountry");
        prefManager.closeDB();


        textinput_country.setText(country);
        textinput_state.setText(state);
        textinput_city.setText(city);
        Log.d(TAG, "#COUNTRY : " + country);
        Log.d(TAG, "#STATE : " + state);
        Log.d(TAG, "#CITY : " + city);
        System.out.println("#PINCODE "+prefManager.getString("userPincode"));

        txt_FirstName.setEnabled(false);
        txt_LastName.setEnabled(false);
        txt_Mobile.setEnabled(false);
        txt_Email.setEnabled(false);
        txt_DOB.setEnabled(false);
        txt_Address.setEnabled(false);
        txt_Pincode.setEnabled(false);
        textinput_country.setEnabled(false);
        textinput_state.setEnabled(false);
        textinput_city.setEnabled(false);
        btn_update.setVisibility(View.GONE);


        System.out.println("First Name: " + txt_FirstName + " Lastname: " + txt_LastName + " MobileNo: " + txt_Mobile + " DOB: " + txt_DOB + " Address: "
                + txt_Address + " pincode: " + txt_Pincode + " Country: " + country + " state: " + state + " city: " + city);



             txt_DOB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Calendar c = Calendar.getInstance();
                    mYear = c.get(Calendar.YEAR);
                    mMonth = c.get(Calendar.MONTH);
                    mDay = c.get(Calendar.DAY_OF_MONTH);
                    DatePickerDialog datePickerDialog = new DatePickerDialog(ProfileActivity.this,
                            new DatePickerDialog.OnDateSetListener() {

                                @Override
                                public void onDateSet(DatePicker view, int year,
                                                      int monthOfYear, int dayOfMonth) {

                                    txt_DOB.setText(year+ "-"+ (monthOfYear + 1)+ "-" + dayOfMonth);

                                }
                            }, mYear, mMonth, mDay);
                    datePickerDialog.show();
                }
            });


        textinput_country.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for (int i = 0; i < countryLists.size(); i++) {
                    if (textinput_country.getText().toString().equals(countryLists.get(i).getName())) {
                        country = countryLists.get(i).getId();
                        //edit_state.setText("");
                            getState(country);
                            textinput_state.setText("");
                            state = "";
                        //taluka = ""
                        System.out.println("State " + state);
                        return;
                    }
                }
            }
        });

        textinput_state.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for (int i = 0; i < stateLists.size(); i++) {
                    if (textinput_state.getText().toString().equals(stateLists.get(i).getState_name())) {
                        state = stateLists.get(i).getState_id();
                        //edit_state.setText("");
                        getCity(state);
                        textinput_city.setText("");
                        city = "";
                        //taluka = ""
                        System.out.println("City " + city);
                        return;
                    }
                }
            }
        });

        textinput_city.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String str = textinput_city.getText().toString();
                System.out.println("Check district " + str);
                for (int j = 0; j < cityLists.size(); j++) {
                    if (str.equals(cityLists.get(j).getCity_name())) {
                        city = cityLists.get(j).getCity_id();
                        System.out.println("State " + state);
                        System.out.println("city " + city);
                        System.out.println("Check boolean " + str.equals(cityLists.get(j).getCity_name()));
                        System.out.println("Check city " + city);
                        System.out.println("Check city " + cityLists.get(j).getCity_name());
                        System.out.println("City " + textinput_city.getText().toString());
                        return;
                    }
                }
            }
        });
    }


    //get country
    private void getCountry() {
        //defining a progress dialog to show while signing up
        final ProgressDialog progressDialog = new ProgressDialog(this, R.style.AppCompatAlertDialogStyle);
        progressDialog.setMessage(getResources().getString(R.string.loading));
        progressDialog.show();
        progressDialog.setCancelable(false);

        countryLists = new ArrayList<>();
        //building retrofit object
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIUrl.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //Defining retrofit api service
        APIService service = retrofit.create(APIService.class);


        //defining the call
        Call<CountryListModel> call = service.get_country(APIUrl.KEY);

        //calling the api
        call.enqueue(new Callback<CountryListModel>() {
            @Override
            public void onResponse(Call<CountryListModel> call, Response<CountryListModel> response) {
                //hiding progress dialog
                progressDialog.dismiss();

                if (Integer.parseInt(response.body().getResponse()) == 101) {
                    for (int i = 0; i < response.body().getCity_list().size(); i++) {
                        countryLists.add(new CountryListModel.CountryList(
                                response.body().getCity_list().get(i).getId(),
                                response.body().getCity_list().get(i).getSortname(),
                                response.body().getCity_list().get(i).getName(),
                                response.body().getCity_list().get(i).getPhonecode()));
                    }
                    countySpinner(countryLists);
                } else if (Integer.parseInt(response.body().getResponse()) == 102) {
                    countySpinner(countryLists);
                } else if (Integer.parseInt(response.body().getResponse()) == 103) {
                    countySpinner(countryLists);
                    System.out.println(TAG + " Required Parameter Missing");
                } else if (Integer.parseInt(response.body().getResponse()) == 104) {
                    countySpinner(countryLists);
                    System.out.println(TAG + " Invalid Key");
                } else if (Integer.parseInt(response.body().getResponse()) == 105) {
                    countySpinner(countryLists);
                    System.out.println(TAG + " Login failed");
                } else {
                    countySpinner(countryLists);
                    System.out.println(TAG + " Else Close");
                }
            }

            @Override
            public void onFailure(Call<CountryListModel> call, Throwable t) {
                progressDialog.dismiss();
                countySpinner(countryLists);
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getState(String coutntry_id) {
        //defining a progress dialog to show while signing up
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.loading));
        progressDialog.show();

        stateLists = new ArrayList<>();

        //building retrofit object
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIUrl.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //Defining retrofit api service
        APIService service = retrofit.create(APIService.class);


        //defining the call
        Call<DistrictModel> call = service.getState(APIUrl.KEY, coutntry_id);

        //calling the api
        call.enqueue(new Callback<DistrictModel>() {
            @Override
            public void onResponse(Call<DistrictModel> call, Response<DistrictModel> response) {
                //hiding progress dialog
                progressDialog.dismiss();

                if (Integer.parseInt(response.body().getResponse()) == 101) {
                    //Toast.makeText(ProfileActivity.this, "state" + response.body().getResponse(), Toast.LENGTH_SHORT).show();
                    for (int i = 0; i < response.body().getDistrict_list().size(); i++) {
                        stateLists.add(new StateList(response.body().getDistrict_list().get(i).getState_id(),
                                response.body().getDistrict_list().get(i).getState_name(),
                                response.body().getDistrict_list().get(i).getCountry_id()));
                    }
                    stateSpinner(stateLists);
                } else if (Integer.parseInt(response.body().getResponse()) == 102) {
                    stateSpinner(stateLists);
                } else if (Integer.parseInt(response.body().getResponse()) == 103) {
                    stateSpinner(stateLists);
                    System.out.println(TAG + " Required Parameter Missing");
                } else if (Integer.parseInt(response.body().getResponse()) == 104) {
                    stateSpinner(stateLists);
                    System.out.println(TAG + " Invalid Key");
                } else if (Integer.parseInt(response.body().getResponse()) == 105) {
                    stateSpinner(stateLists);
                    System.out.println(TAG + " Login failed");
                } else if (Integer.parseInt(response.body().getResponse()) == 106) {
                    stateSpinner(stateLists);
                    System.out.println(TAG + " Page Not Found");
                } else {
                    stateSpinner(stateLists);
                    System.out.println(TAG + " Else Close");
                }
            }

            @Override
            public void onFailure(Call<DistrictModel> call, Throwable t) {
                progressDialog.dismiss();
                stateSpinner(stateLists);
                errorOut(t);
            }
        });
    }


    private void getCity(String state_id) {
        //defining a progress dialog to show while signing up
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.loading));
        progressDialog.show();

        cityLists = new ArrayList<>();

        //building retrofit object
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIUrl.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //Defining retrofit api service
        APIService service = retrofit.create(APIService.class);


        //defining the call
        Call<CityListModel> call = service.getCity(APIUrl.KEY, state_id);

        //calling the api
        call.enqueue(new Callback<CityListModel>() {
            @Override
            public void onResponse(Call<CityListModel> call, Response<CityListModel> response) {
                //hiding progress dialog
                progressDialog.dismiss();

                System.out.println("Response " + response.body().getResponse());
                if (Integer.parseInt(response.body().getResponse()) == 101) {
                    //Toast.makeText(ProfileActivity.this, "city" + response.body().getResponse(), Toast.LENGTH_SHORT).show();
                    for (int i = 0; i < response.body().getCities_list().size(); i++) {

                            cityLists.add(new CityListModel.CityList(response.body().getCities_list().get(i).getCity_id(),
                                response.body().getCities_list().get(i).getCity_name(),
                                response.body().getCities_list().get(i).getState_id()));
                    }
                    citySpinner(cityLists);
                } else if (Integer.parseInt(response.body().getResponse()) == 102) {
                    citySpinner(cityLists);
                } else if (Integer.parseInt(response.body().getResponse()) == 103) {
                    citySpinner(cityLists);
                    System.out.println(TAG + " Required Parameter Missing");
                } else if (Integer.parseInt(response.body().getResponse()) == 104) {
                    citySpinner(cityLists);
                    System.out.println(TAG + " Invalid Key");
                } else if (Integer.parseInt(response.body().getResponse()) == 105) {
                    citySpinner(cityLists);
                    System.out.println(TAG + " Login failed");
                } else if (Integer.parseInt(response.body().getResponse()) == 106) {
                    citySpinner(cityLists);
                    System.out.println(TAG + " Page Not Found");
                } else {
                    citySpinner(cityLists);
                    System.out.println(TAG + " Else Close");
                }
            }

            @Override
            public void onFailure(Call<CityListModel> call, Throwable t) {
                progressDialog.dismiss();
                citySpinner(cityLists);
                errorOut(t);
            }
        });
    }

        //spinner for country
        private void countySpinner(ArrayList<CountryListModel.CountryList> countryLists) {
            countryName = new ArrayList<>();
            for (CountryListModel.CountryList data : countryLists) {
                countryName.add(data.getName());
                if (data.getId().equals(country)) {
                    textinput_country.setText(data.getName());
                    getState(country);
                }
            }
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, countryName);
            // Drop down layout style - list view with radio button
            dataAdapter.setDropDownViewResource(android.R.layout.simple_list_item_2);
            textinput_country.setThreshold(1);
            // attaching data adapter to spinner
            textinput_country.setAdapter(dataAdapter);
        }

    private void stateSpinner(ArrayList<StateList> stateLists) {
        stateName = new ArrayList<>();
        for (StateList data : stateLists) {
            stateName.add(data.getState_name());
            if (data.getState_id().equals(state)) {
                textinput_state.setText(data.getState_name());
                getCity(state);
                Toast.makeText(this, "" + data.getState_name(), Toast.LENGTH_SHORT).show();
            }
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.custom_tab, stateName);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_list_item_2);
        textinput_state.setThreshold(1);
        // attaching data adapter to spinner
        textinput_state.setAdapter(dataAdapter);
    }

    private void citySpinner(ArrayList<CityListModel.CityList> districtLists) {
        cityName = new ArrayList<>();
        for (CityListModel.CityList data : districtLists) {
            cityName.add(data.getCity_name());
            if (data.getCity_id().equals(city)) {
                textinput_city.setText(data.getCity_name());

                //getTaluka(data.getDistrict_id());
            }
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, cityName);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_list_item_2);
        textinput_city.setThreshold(1);
        // attaching data adapter to spinner
        textinput_city.setAdapter(dataAdapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.profile_menu, menu);
        this.menu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_edit) {
            txt_FirstName.setEnabled(true);
            txt_LastName.setEnabled(true);
            txt_Mobile.setEnabled(false);
            txt_Email.setEnabled(true);
            txt_DOB.setEnabled(true);
            txt_Address.setEnabled(true);
            txt_Pincode.setEnabled(true);
            textinput_country.setEnabled(true);
            textinput_state.setEnabled(true);
            textinput_city.setEnabled(true);
            btn_update.setEnabled(true);
            btn_update.setVisibility(View.VISIBLE);

            menu.findItem(R.id.action_edit).setVisible(false);
            return true;
        }
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btnSave:

                firstname = txt_FirstName.getText().toString().trim();
                lastname = txt_LastName.getText().toString().trim();
                email = txt_Email.getText().toString().trim();
                mobile = txt_Mobile.getText().toString().trim();
                address = txt_Address.getText().toString().trim();
                pincode = txt_Pincode.getText().toString().trim();
                countryname = textinput_country.getText().toString();
                statename = textinput_state.getText().toString();
                cityname = textinput_city.getText().toString();
                dob = txt_DOB.getText().toString().trim();


                if (!isNetworkAvailable()) {
                    Toasty.error(this, getResources().getString(R.string.error_msg_no_internet), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(firstname)) {
                    txt_FirstName.setError(getResources().getString(R.string.enter_first_name));
                    return;
                }
                if (TextUtils.isEmpty(lastname)) {
                    txt_LastName.setError(getResources().getString(R.string.enter_last_name));
                    return;
                }
                if (TextUtils.isEmpty(email)) {
                    txt_Email.setError(getResources().getString(R.string.enter_email));
                    return;
                }
                if (email.length() > 1) {
                    matcher = pattern.matcher(email);
                    if (!matcher.matches()) {
                        Toasty.error(this, getResources().getString(R.string.enter_valid_email), Toasty.LENGTH_SHORT).show();
                        return;
                    }
                }


                if (TextUtils.isEmpty(mobile)) {
                    txt_Mobile.setError( getResources().getString(R.string.entermobile));
                    return;
                }
                if (String.valueOf(mobile.charAt(0)).equals("0") ||
                        String.valueOf(mobile.charAt(0)).equals("1") ||
                        String.valueOf(mobile.charAt(0)).equals("2") ||
                        String.valueOf(mobile.charAt(0)).equals("3") ||
                        String.valueOf(mobile.charAt(0)).equals("4") ||
                        String.valueOf(mobile.charAt(0)).equals("5") ||
                        String.valueOf(mobile.charAt(0)).equals("6")) {
                    txt_Mobile.setError(getResources().getString(R.string.entervalidmobileno));
                    return;
                }
                if (TextUtils.isEmpty(address)) {
                    txt_Address.setError(getResources().getString(R.string.enter_address));
                    return;
                }
                if (TextUtils.isEmpty(pincode)) {
                    txt_Pincode.setError(getResources().getString(R.string.enter_pincode));
                    return;
                }
                if (TextUtils.isEmpty(countryname)) {
                    textinput_country.setError(getResources().getString(R.string.select_country));
                    return;
                }
                if (TextUtils.isEmpty(statename)) {
                    textinput_state.setError(getResources().getString(R.string.select_state));
                    return;
                }
                if (TextUtils.isEmpty(cityname)) {
                    textinput_city.setError(getResources().getString(R.string.select_city));
                    return;
                }
                if (TextUtils.isEmpty(dob)) {
                    txt_DOB.setError(getResources().getString(R.string.enter_dob));
                    return;
                }
                updateProfileHere(firstname, lastname, email, mobile, dob, countryname, statename, cityname, address, pincode);
                break;
        }

    }

    private void updateProfileHere(String firstname, String lastname, String email, String mobile, String dob, String Countryname,
                                   String Statename, String Cityname, String address, String pincode) {

        //defining a progress dialog to show while signing up
        final ProgressDialog progressDialog = new ProgressDialog(this, R.style.AppCompatAlertDialogStyle);
        progressDialog.setMessage(getResources().getString(R.string.updating));
        progressDialog.show();
        progressDialog.setCancelable(false);

        //building retrofit object
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIUrl.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        //Defining retrofit api service
        APIService service = retrofit.create(APIService.class);


        Call<UpdateProfile> call = service.updateProfile(APIUrl.KEY,
                mobile,
                gcm_code,
                firstname,
                lastname,
                Countryname,
                Statename,
                Cityname,
                address,
                pincode,
                email,
                dob,
                id);

        call.enqueue(new Callback<UpdateProfile>() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onResponse(Call<UpdateProfile> call, Response<UpdateProfile> response) {
                //hiding progress dialog
                progressDialog.dismiss();
                System.out.println(TAG + " Response profile  " + response.body().getResponse());

                try {
                    if (Integer.parseInt(response.body().getResponse()) == 101) {
                        Toasty.success(getApplicationContext(), getResources().getString(R.string.profile_updated_successfully), Toasty.LENGTH_LONG).show();

                        prefManager.connectDB();
                        prefManager.connectDB();
                        prefManager.setBoolean("isLogin", true);
                        prefManager.setString("userId", response.body().getResult().get(0).getReg_id());
                        prefManager.setString("userFirstName", response.body().getResult().get(0).getFirst_name());
                        prefManager.setString("userLastName", response.body().getResult().get(0).getLast_name());
                        prefManager.setString("userMobile", response.body().getResult().get(0).getMobile_no());
                        prefManager.setString("userEmail", response.body().getResult().get(0).getEmail_id());
                        prefManager.setString("userDOB", response.body().getResult().get(0).getDob());
                        prefManager.setString("userCountry", response.body().getResult().get(0).getCountry());
                        prefManager.setString("userState", response.body().getResult().get(0).getState());
                        prefManager.setString("userCity", response.body().getResult().get(0).getCity());
                        prefManager.setString("userAddress", response.body().getResult().get(0).getAddress());
                        prefManager.setString("userStatus", response.body().getResult().get(0).getUser_status());
                        prefManager.setString("userPincode",response.body().getResult().get(0).getPincode());
                        prefManager.setString("is_created_date", response.body().getResult().get(0).getIs_created_date());
                        prefManager.closeDB();
                        btn_update.setVisibility(View.GONE);
                        txt_FirstName.setEnabled(false);
                        txt_LastName.setEnabled(false);
                        txt_Mobile.setEnabled(false);
                        txt_Email.setEnabled(false);
                        txt_DOB.setEnabled(false);
                        txt_Address.setEnabled(false);
                        txt_Pincode.setEnabled(false);
                        textinput_country.setEnabled(false);
                        textinput_state.setEnabled(false);
                        textinput_city.setEnabled(false);
                        menu.findItem(R.id.action_edit).setVisible(true);


                    } else if (Integer.parseInt(response.body().getResponse()) == 102) {
                        Toasty.error(getApplicationContext(), getResources().getString(R.string.account_block), Toasty.LENGTH_LONG).show();
                    } else if (Integer.parseInt(response.body().getResponse()) == 103) {
                        System.out.println(TAG + " Required Parameter Missing");
                        Toasty.error(getApplicationContext(), getResources().getString(R.string.requiredparameter), Toasty.LENGTH_LONG).show();
                    } else if (Integer.parseInt(response.body().getResponse()) == 104) {
                        System.out.println(TAG + " Invalid Key");
                        Toasty.error(getApplicationContext(), getResources().getString(R.string.invalid_key), Toasty.LENGTH_LONG).show();
                    } else if (Integer.parseInt(response.body().getResponse()) == 105) {
                        System.out.println(TAG + " Invalid Key");
                        Toasty.success(getApplicationContext(),getResources().getString(R.string.profile_updated_successfully), Toasty.LENGTH_LONG).show();
                        startActivity(new Intent(ProfileActivity.this, HomeActivity.class));
                        finish();
                    } else if (Integer.parseInt(response.body().getResponse()) == 110) {
                        System.out.println(TAG + " Invalid Key");
                        Toasty.error(getApplicationContext(), getResources().getString(R.string.profile_updated_successfully), Toasty.LENGTH_LONG).show();
                        startActivity(new Intent(ProfileActivity.this, HomeActivity.class));
                        finish();
                    } else {
                        System.out.println(TAG + " Else Close");
                        Toast.makeText(getApplicationContext(),getResources().getString(R.string.login_failed), Toast.LENGTH_LONG).show();
                    }
                } catch (NullPointerException e) {
                    System.out.println("Response " + e.getMessage());
                    Toasty.error(getApplicationContext(), getResources().getString(R.string.image_not_getting), Toasty.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<UpdateProfile> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
