package com.oyourneed.adapter;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.oyourneed.R;
import com.oyourneed.activity.CartListActivity;
import com.oyourneed.activity.OrderActivity;
import com.oyourneed.activity.OrdersListActivity;
import com.oyourneed.data.APIUrl;
import com.oyourneed.data.AppDatabase;
import com.oyourneed.model.Cartdb;
import com.oyourneed.model.OrderList;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.MyViewHolder> {

    private OrderActivity context;

    private ArrayList<OrderList> arrayLists;
    private ArrayList<Cartdb> list;
    private ArrayList<OrderList> tempArrayLists;
    int sum;
    private float prize;
    private String total_amt;
    public OrderAdapter(OrderActivity context) {
        this.context = context;
    }

    public List<OrderList> getListArray() {
        return arrayLists;
    }

    public void setListArray(ArrayList<OrderList> arrayLists) {
        this.arrayLists = arrayLists;
        tempArrayLists = new ArrayList<>();
        tempArrayLists.addAll(arrayLists);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_design, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        final OrderList OrderList = arrayLists.get(position);
        //Toast.makeText(context, "Array size "+arrayLists.size(), Toast.LENGTH_SHORT).show();

       /* if (holder.textView_TotalQty.equals("userid")) {
            holder.textView_TotalQty.setText("");
        }
        if (holder.textView_Orderid.equals("orderid")) {
            holder.textView_Orderid.setText("");
        }
        if (holder.textView_userid.equals("uid")) {
            holder.textView_userid.setText("");
        }
        if (holder.textView_prize.equals("prize")) {
            holder.textView_prize.setText("");
        }
        if (holder.textView_qty.equals("qty")) {
            holder.textView_qty.setText("");
        }
        if (holder.textView_date.equals("date")) {
            holder.textView_date.setText("");
        }
*/
        //holder.title.setText(OrderList.getProductName());
//        holder.textView_TotalQty.setText("Qty : " + String.valueOf(OrderList.getTotalQty()));
        if (holder.textView_date.equals("date")) {
            holder.textView_date.setText("");
        }
        holder.textView_Orderid.setText("Ord id: " + String.valueOf(OrderList.getOrderNo()));
        //marquee for order id
        holder.textView_Orderid.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        holder.textView_Orderid.setSelected(true);
//        holder.textView_userid.setText("User id - " + String.valueOf(OrderList.getUserId()));



        //holder.textView_address.setText("Address - " + OrderList.getDeliveryAddress());
//        holder.textView_qty.setText(OrderList.getTotalQty() + " kg");
        total_amt = OrderList.getFinalTotalAmount();
        holder.textView_prize.setText("\u20B9 " + OrderList.getFinalTotalAmount());
        holder.textView_prize2.setText("Total Payable Amount : - "+"\u20B9 " + OrderList.getFinalTotalAmount());
        holder.textView_date.setText(context.parseDate(OrderList.getIsCreatedDate()));


        holder.btn_viewdeatils.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tempArrayLists.get(position);

        Intent intent = new Intent(context, OrdersListActivity.class);
        intent.putExtra("order_no", arrayLists.get(position).getOrderNo());
        intent.putExtra("total_amt", total_amt);
        intent.putExtra("date", context.parseDate(OrderList.getIsCreatedDate()));
        intent.putExtra("user_id", arrayLists.get(position).getUserId());
        context.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return arrayLists.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView textView_TotalQty, textView_Orderid, textView_userid, textView_address, textView_prize2,textView_prize, textView_qty, textView_date;
        //public ImageView images;
        public LinearLayout ltyCategory;
        public Button btn_viewdeatils;

        public MyViewHolder(View view) {
            super(view);
            textView_TotalQty = view.findViewById(R.id.textView_TotalQty);
            textView_Orderid = view.findViewById(R.id.textView_Orderid);
            textView_userid = view.findViewById(R.id.textView_userid);
           // textView_address = view.findViewById(R.id.textView_address);
            textView_prize = view.findViewById(R.id.textView_prize);
            textView_prize2 = view.findViewById(R.id.textView_prize2);
            textView_qty = view.findViewById(R.id.textView_qty);
            textView_date = view.findViewById(R.id.textView_date);
            btn_viewdeatils = view.findViewById(R.id.btn_viewdeatils);
        }
    }

    private static void addProduct(final AppDatabase db, Cartdb cartdb) {
        db.adminDao().insertProduct(cartdb);
    }

    private static void updateProduct(final AppDatabase db, Cartdb cartdb) {
        db.adminDao().updateProduct(cartdb);
    }

    private static Cartdb getSingleProduct(final AppDatabase db, String cartdb) {
        return db.adminDao().getSingleProduct(cartdb);
    }

    private static void deleteProduct(final AppDatabase db, String cartdb) {
        db.adminDao().getDeleteProduct(cartdb);
    }

    private static List<Cartdb> getProductList(final AppDatabase db) {
        return db.adminDao().getAllProduct();
    }

    private void showData(View v) {
        list = new ArrayList<>();
        list = (ArrayList<Cartdb>) getProductList(AppDatabase.getAppDatabase(context));
        int total = 0;
        int price = 0;

        for (Cartdb data : list) {
            total = Integer.parseInt(data.getPro_total()) + total;
            //price = Integer.parseInt(data.getPro_qty()+price)*Integer.parseInt(data.getTotal_total())+price;
        }
        Snackbar snackbar = Snackbar
                .make(v, "Total : " + total +", "+ " Qty : " + price+", ", Snackbar.LENGTH_LONG)
                .setAction("CHECKOUT", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        context.startActivity(new Intent(context, CartListActivity.class));
                    }
                });

// Changing message text color
        snackbar.setActionTextColor(Color.RED);
// Changing action button text color
        snackbar.show();
    }

    public void getFilter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        System.out.println("getFilter "+charText);
        arrayLists.clear();
        if (charText.length() == 0) {
            arrayLists.addAll(tempArrayLists);
        } else {
            for (OrderList data : tempArrayLists) {
                if (data.getOrderNo().toLowerCase(Locale.getDefault()).contains(charText)) {
                    arrayLists.add(data);
                }
            }
        }
        notifyDataSetChanged();
    }
}
