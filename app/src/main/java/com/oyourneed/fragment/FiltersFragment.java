package com.oyourneed.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.oyourneed.R;
import com.oyourneed.activity.FiltersActivity;

public class FiltersFragment extends Fragment {

    private FiltersActivity context;
    private RadioButton genderradioButton;
    private RadioGroup radioGroup;
    View view;


    public FiltersFragment() {
    }

    @SuppressLint("ValidFragment")
    public FiltersFragment(FiltersActivity context) {
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       view = inflater.inflate(R.layout.filters_fragment, container, false);
        radioGroup= view.findViewById(R.id.radioGroup);
        onclickbuttonMethod();
        return view;
    }
    public void onclickbuttonMethod(){
        int selectedId = radioGroup.getCheckedRadioButtonId();
        genderradioButton = view.findViewById(selectedId);
        if(selectedId==-1){
            //Toast.makeText(context,"Nothing selected", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(context,genderradioButton.getText(), Toast.LENGTH_SHORT).show();
        }
    }



}
