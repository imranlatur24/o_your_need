package com.oyourneed.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.oyourneed.R;
import com.oyourneed.view.MyRadioButton;

import java.util.ArrayList;

/**
 * Created by sonu on 19/09/16.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolder> {

    private ArrayList<String> arrayList;
    private Context context;
    private CallbackInterface mCallback;
    private boolean mBoolean;
    private int selectedPosition = -1;

    static class RecyclerViewHolder extends RecyclerView.ViewHolder {

        private TextView label;
        private MyRadioButton radioButton;

        RecyclerViewHolder(View view) {
            super(view);
            label = (TextView) view.findViewById(R.id.label);
            radioButton = (MyRadioButton) view.findViewById(R.id.radio_button);
        }

    }

    public RecyclerViewAdapter(Context context, ArrayList<String> arrayList, boolean mBoolean) {
        this.arrayList = arrayList;
        this.context = context;
        this.mBoolean = mBoolean;
        try{
           mCallback = (RecyclerViewAdapter.CallbackInterface) context;
        }catch(ClassCastException ex){
            //.. should log the error or throw and exception
            Log.e("MyAdapter","Must implement the CallbackInterface in the Activity", ex);
        }
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_custom_row_layout, viewGroup, false);
        return new RecyclerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, final int i) {

        holder.label.setText(arrayList.get(i));

        //check the radio button if both position and selectedPosition matches
        holder.radioButton.setChecked(i == selectedPosition);

        //Set the position tag to both radio button and label
        holder.radioButton.setTag(i);
        holder.label.setTag(i);
        holder.radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                itemCheckChanged(v);
                mCallback.onHandleSelection(i,mBoolean);
            }
        });

        holder.label.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                itemCheckChanged(v);
                mCallback.onHandleSelection(i,mBoolean);
            }


        });
    }

    //On selecting any view set the current position to selectedPositon and notify adapter
    private void itemCheckChanged(View v) {
        selectedPosition = (Integer) v.getTag();
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return (null != arrayList ? arrayList.size() : 0);
    }

    public interface CallbackInterface{

        /**
         * Callback invoked when clicked
         * @param pos - the position
         * @param b - the boolean
         */
        void onHandleSelection(int pos, boolean b);
    }
}
