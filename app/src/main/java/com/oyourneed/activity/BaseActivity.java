package com.oyourneed.activity;


import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.content.res.Configuration;

import com.oyourneed.R;
import com.oyourneed.data.SharedPreferenceManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import es.dmoral.toasty.Toasty;


public class BaseActivity extends AppCompatActivity {
	
	public static SharedPreferenceManager prefManager;
	public DisplayMetrics metrices;
	
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		
		prefManager = new SharedPreferenceManager(this);
		metrices = getResources().getDisplayMetrics();
	}

	public  boolean isNetworkAvailable()
	{
		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}

	public boolean isValidEmail(String email) {
		String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
		return email.matches(regex);
	}

	public boolean isSpecialCharAvailable(String s) {
		//int counter =0;
		if (s == null || s.trim().isEmpty()) {
			return false;
		}
		Pattern p = Pattern.compile("[^A-Za-z0-9]");//replace this with your needs
		Matcher m = p.matcher(s);
		// boolean b = m.matches();

		boolean b = m.find();
		if (b == true)
			return true;
		else
			return false;
	}

	public static String getDateText(String timestamp)
	{
		SimpleDateFormat format = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
		String date = format.format(new Date(Long.parseLong(timestamp)));
		String str = date;
		return str;
	}
	public static String sendDateText(String timestamp)
	{
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
		String date = format.format(new Date(Long.parseLong(timestamp)));
		String str = date;
		return str;
	}

	public static String getTimeText(String timestamp)
	{
		SimpleDateFormat format = new SimpleDateFormat("hh:mm a", Locale.getDefault());
		String date = format.format(new Date(Long.parseLong(timestamp)));
		String str = date;
		return str;
	}

	public static String sendTimeTextToSever(String timestamp)
	{
		String inputPattern = "hh:mm a";
		String outputPattern = "HH:mm:ss";
		SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
		SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

		Date date = null;
		String str = null;

		try {
			date = inputFormat.parse(timestamp);
			str = outputFormat.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return str;
	}


	public static String sendTimeText(String timestamp)
	{
		String inputPattern = "hh a";
		String outputPattern = "HH:mm:ss";
		SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
		SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

		Date date = null;
		String str = null;

		try {
			date = inputFormat.parse(timestamp);
			str = outputFormat.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return str;
	}


	public String parseDate(String time) {
		String inputPattern = "yyyy-MM-dd HH:mm:ss";
		String outputPattern = "MMM dd,yyyy";
		SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
		SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

		Date date = null;
		String str = null;

		try {
			date = inputFormat.parse(time);
			str = outputFormat.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return str;
	}

	public String parseTime(String time) {
		String inputPattern = "HH:mm:ss";
		String outputPattern = "hh:mm a";
		SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
		SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

		Date date = null;
		String str = null;

		try {
			date = inputFormat.parse(time);
			str = outputFormat.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return str;
	}


	public String capFirstLetter(String value){
		String s1 = value.substring(0, 1).toUpperCase();
		String nameCapitalized = s1 + value.substring(1);
		return nameCapitalized;
	}

	public void showProgressDialog(ProgressDialog pDialog,String message){
		pDialog.setMessage(message);
		pDialog.setIndeterminate(false);
		pDialog.setCancelable(false);
		pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
	}

	/*public void dismissProgressDialog(ProgressDialog pDialog){
		pDialog.dismiss();
	}*/

	public void errorOut(Throwable t){
		if(!isNetworkAvailable()){
			Toasty.error(getApplicationContext(), getResources().getString(R.string.error_msg_no_internet), Toasty.LENGTH_LONG).show();
		}else {
			Toasty.error(getApplicationContext(), getResources().getString(R.string.serverdown) + t, Toasty.LENGTH_LONG).show();
		}
	}

	public void setLocale(String lang) {
		Locale locale = new Locale(lang);
		Locale.setDefault(locale);
		Configuration configuration = new Configuration();
		configuration.locale = locale;
		getBaseContext().getResources().updateConfiguration(configuration,getBaseContext().getResources().getDisplayMetrics());
	}

	public void setCurrentLanguage() {
		Configuration configuration = new Configuration(getResources().getConfiguration());
		prefManager.connectDB();
		String restoredLanguage = prefManager.getLanguage("language");
		prefManager.closeDB();
		configuration.locale = new Locale(restoredLanguage);
		getResources().updateConfiguration(configuration, null);
	}
}
