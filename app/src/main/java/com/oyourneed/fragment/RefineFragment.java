package com.oyourneed.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.oyourneed.R;
import com.oyourneed.activity.FiltersActivity;

public class RefineFragment extends Fragment implements View.OnClickListener {

    private FiltersActivity context;

    public RefineFragment() {
    }

    @SuppressLint("ValidFragment")
    public RefineFragment(FiltersActivity context) {
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.filter_refine, container, false);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {

        }
    }
}
