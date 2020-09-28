package com.oyourneed.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.oyourneed.R;
import com.oyourneed.activity.OrdersListActivity;
import com.oyourneed.model.Cartdb;
import com.oyourneed.model.OrderListModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.MyViewHolder> {

    private OrdersListActivity context;

    private ArrayList<OrderListModel> arrayLists;
    private ArrayList<Cartdb> list;
    private ArrayList<OrderListModel> tempArrayLists;
    int sum;
    private float prize;

    public OrderListAdapter(OrdersListActivity context) {
        this.context = context;
    }

    public List<OrderListModel> getListArray() {
        return arrayLists;
    }

    public void setListArray(ArrayList<OrderListModel> arrayLists) {
        this.arrayLists = arrayLists;
        tempArrayLists = new ArrayList<>();
        tempArrayLists.addAll(arrayLists);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.orderlist_adapter, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        final OrderListModel orderListModel = arrayLists.get(position);

/*
       if (orderListModel.getOrderStatus().equals("0"))
       {
           holder.textView_date.setVisibility(View.GONE);
           holder.textView_date3.setVisibility(View.GONE);
       }
       else{
           holder.textView_date.setVisibility(View.GONE);
           holder.textView_date3.setVisibility(View.GONE);
       }
*/
        if (holder.textView_Orderid.equals("orderid"))
        {
            holder.textView_Orderid.setText("");
        }

        if (holder.textView_Unit.equals("unit"))
        {
            holder.textView_Unit.setText("");
        }
        if (holder.textView_ProductName.equals("proname"))
        {
            holder.textView_ProductName.setText("");
        }
        if (holder.textView_prize.equals("prize"))
        {
            holder.textView_prize.setText("");
        }
        if (holder.textView_qty.equals("qty"))
        {
            holder.textView_qty.setText("");
        }
        if (holder.textView_date.equals("date"))
        {
            holder.textView_date.setText("");
        }

        //holder.title.setText(OrderList.getProductName());
        holder.textView_Orderid.setText("Order id - "+String.valueOf(orderListModel.getOrderNo()));
        holder.textView_Unit.setText("1 kg x "+orderListModel.getProductQty()+" Qty");
        holder.textView_qty.setText(orderListModel.getProductQty()+" Qty");
        holder.textView_prize.setText("\u20B9 "+orderListModel.getProductTotalPrice());
        holder.textView_date.setText(orderListModel.getIsCreatedDate());
        holder.textView_date3.setText(orderListModel.getIsCreatedDate());
        holder.textView_ProductName.setText(orderListModel.getProductName());

        Picasso.with(context)
                .load(orderListModel.getProductImage())
                .placeholder(R.drawable.foodgrains)
                .into(holder.images);

        System.out.println("Image URL "+ orderListModel.getProductImage());

    }


    @Override
    public int getItemCount() {
        return arrayLists.size();
        //return (arrayLists == null) ? 0 : arrayLists.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView textView_date3,textView_TotalQty,textView_ProductName,textView_Orderid,textView_userid,textView_address,textView_prize,
                textView_qty,textView_date,textView_Unit;
        public ImageView images;
        public LinearLayout ltyCategory;
        public CardView cvCategory;

        public MyViewHolder(View view) {
            super(view);
            textView_TotalQty = view.findViewById(R.id.textView_TotalQty);
            textView_date3 = view.findViewById(R.id.textView_date3);
            textView_Unit = view.findViewById(R.id.textView_Unit);
            textView_ProductName = view.findViewById(R.id.textView_ProductName);
            textView_Orderid = view.findViewById(R.id.textView_Orderid);
            textView_userid = view.findViewById(R.id.textView_userid);
           // textView_address = view.findViewById(R.id.textView_address);
            textView_prize = view.findViewById(R.id.textView_prize);
            textView_qty = view.findViewById(R.id.textView_qty);
            textView_date = view.findViewById(R.id.textView_date2);
            images = view.findViewById(R.id.imageView_image);
        }
    }
    public void getFilter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        arrayLists.clear();
        if (charText.length() == 0) {
            arrayLists.addAll(tempArrayLists);
        } else {
            for (OrderListModel data : tempArrayLists) {
                if (data.getProductName().toLowerCase(Locale.getDefault()).contains(charText)) {
                    arrayLists.add(data);
                }
            }
        }
        notifyDataSetChanged();
    }
}
