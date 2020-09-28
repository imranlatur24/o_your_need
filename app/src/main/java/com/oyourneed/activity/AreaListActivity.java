package com.oyourneed.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.oyourneed.R;
import com.oyourneed.adapter.RecyclerViewAdapter;
import com.oyourneed.model.WorkSpaceLocality;

import java.util.ArrayList;

public class AreaListActivity extends BaseActivity implements RecyclerViewAdapter.CallbackInterface {

    private Toolbar toolbar;
    private RecyclerView areaListView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerViewAdapter mAdapter;
    private LinearLayout error_layout;
    private TextView error_txt_cause;
    private Button error_btn_retry;
    private FrameLayout ltyAreaList;
    private ArrayList<WorkSpaceLocality> workSpaceCityArrayList;
    private ArrayList<String> stringArrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCurrentLanguage();
        setContentView(R.layout.activity_area_list);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getIntent().getStringExtra("city"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        init();
    }

    private void init() {
        error_layout = (LinearLayout) findViewById(R.id.error_layout);
        error_btn_retry = (Button) findViewById(R.id.error_btn_retry);
        error_txt_cause = (TextView) findViewById(R.id.error_txt_cause);
        areaListView = (RecyclerView) findViewById(R.id.areaListView);
        ltyAreaList = (FrameLayout) findViewById(R.id.ltyAreaList);

        workSpaceCityArrayList = new ArrayList<>();
        workSpaceCityArrayList = (ArrayList<WorkSpaceLocality>) getIntent().getSerializableExtra("arrayList");
        stringArrayList = new ArrayList<>();

        for(WorkSpaceLocality data : workSpaceCityArrayList){
            stringArrayList.add(data.getLocality_name());
        }

        mAdapter =  new RecyclerViewAdapter(AreaListActivity.this,stringArrayList,false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        areaListView.setLayoutManager(linearLayoutManager);
        areaListView.setAdapter(mAdapter);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onHandleSelection(int pos, boolean b)
    {
        setResult(Activity.RESULT_OK,getIntent().putExtra(getResources().getString(R.string.locality),stringArrayList.get(pos).toString()));
        finish();
    }
}
