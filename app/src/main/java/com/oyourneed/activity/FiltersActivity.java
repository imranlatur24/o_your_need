package com.oyourneed.activity;

import android.annotation.SuppressLint;
import android.support.design.widget.TabLayout;
        import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.oyourneed.R;
        import com.oyourneed.adapter.ViewPagerBookingAdapter;

public class FiltersActivity extends BaseActivity {

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private ViewPagerBookingAdapter adapter;
    private Toolbar toolbar;
    private FiltersActivity context;
    public FiltersActivity() {

    }

    @SuppressLint("ValidFragment")
    public FiltersActivity(FiltersActivity context) {
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCurrentLanguage();
        setContentView(R.layout.activity_form);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.filters));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        inti();
        onClickListener();
    }

    private void inti() {

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setText("Refine By"));
        tabLayout.addTab(tabLayout.newTab().setText("Filters"));
        tabLayout.setTabMode(TabLayout.GRAVITY_CENTER);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        adapter = new ViewPagerBookingAdapter(getSupportFragmentManager(), tabLayout.getTabCount(),
                FiltersActivity.this);
        viewPager.setAdapter(adapter);

    }

    private void onClickListener() {
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
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
}