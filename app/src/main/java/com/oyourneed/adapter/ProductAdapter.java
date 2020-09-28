package com.oyourneed.adapter;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.oyourneed.R;
import com.oyourneed.activity.CartListActivity;
import com.oyourneed.activity.ProductListActivity;
import com.oyourneed.data.AppDatabase;
import com.oyourneed.model.Cartdb;
import com.oyourneed.model.LocalityList;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyViewHolder> {

    private ProductListActivity context;
    private CartListActivity mContext;
    private ArrayList<LocalityList> arrayLists;
    private ArrayList<Cartdb> list;
    private ArrayList<LocalityList> tempArrayLists;
    public Snackbar snackbar;
    private boolean value;
    private CallbackInterface mCallback;

    public ProductAdapter(ProductListActivity context,boolean value) {
        this.context = context;
        this.value = value;
        try{
            mCallback = (CallbackInterface) mContext;
        }catch(ClassCastException ex){
            //.. should log the error or throw and exception
            Log.e("MyAdapter","Must implement the CallbackInterface in the Activity", ex);
        }
    }
    public ProductAdapter( CartListActivity mContext,boolean value) {
        this.mContext = mContext;
        this.value = value;

        try{
            mCallback = (CallbackInterface) mContext;
        }catch(ClassCastException ex){
            //.. should log the error or throw and exception
            Log.e("MyAdapter","Must implement the CallbackInterface in the Activity", ex);
        }
    }

    public List<LocalityList> getListArray() {
        return arrayLists;
    }

    public void setListArray(ArrayList<LocalityList> arrayLists) {
        this.arrayLists = arrayLists;
        tempArrayLists = new ArrayList<>();
        tempArrayLists.addAll(arrayLists);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_adapter, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        final LocalityList localityList = arrayLists.get(position);
        tempArrayLists.get(position);

        //Toast.makeText(context, "Array size "+arrayLists.size(), Toast.LENGTH_SHORT).show();

        holder.title.setText(localityList.getProductName());
        holder.textView_temp_price.setText("\u20B9 "+localityList.getProduct_temp_price());
        holder.textView_temp_price.setPaintFlags(holder.textView_temp_price.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);

        holder.textView_product_discount.setText(localityList.getProduct_discount()+"% OFF");
        holder.textView_id.setText(String.valueOf(localityList.getProductId()));
        holder.textView_description.setText(localityList.getProductQty());
        holder.textView_qty.setText(localityList.getProductQty()+" /"+localityList.getProductUnit());
        holder.textView_prize.setText("\u20B9 "+localityList.getProductPrice());
        holder.textView_description.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        holder.title.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        holder.title.setSelected(true);

        //locality list is a server list pass to Card roomer db
        try {
            Cartdb cartdb = getSingleProduct(AppDatabase.getAppDatabase(context), String.valueOf(localityList.getProductId()));            holder.txt_Minus.setVisibility(View.VISIBLE);
            holder.txt_Add.setVisibility(View.GONE);
            holder.txt_One.setVisibility(View.GONE);
            holder.txt_One.setText(cartdb.getPro_total());
            System.out.println("Pro Total "+cartdb.getPro_total());
        }catch (NullPointerException e){
            holder.txt_Minus.setVisibility(View.GONE);
            holder.txt_Add.setVisibility(View.VISIBLE);
            holder.txt_One.setVisibility(View.GONE);
            holder.txt_One.setText("0");
        }

        if(value) {
            Picasso.with(context)
                    .load(localityList.getProductImage())
                    .placeholder(R.drawable.loading_image)
                    .into(holder.images);
        }else {
            Picasso.with(mContext)
                    .load(localityList.getProductImage())
                    .placeholder(R.drawable.loading_image)
                    .into(holder.images);
            double mul = Double.parseDouble(localityList.getProductPrice())* Double.parseDouble(holder.txt_One.getText().toString());
            holder.textView_prize.setText("\u20B9 "+mul);
        }

        holder.txt_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.txt_Add.setVisibility(View.GONE);
                holder.txt_Minus.setVisibility(View.VISIBLE);
                holder.txt_One.setVisibility(View.GONE);

                //increament by 1 and add to roomer db
                int sum = 1 + Integer.parseInt(holder.txt_One.getText().toString());
                try{
                    Cartdb cartdb = getSingleProduct(AppDatabase.getAppDatabase(context), String.valueOf(localityList.getProductId()));
                    Cartdb cart = new Cartdb();
                    cart.setId(cartdb.getId());
                    cart.setPro_id(localityList.getProductId());
                    cart.setTitle(localityList.getProductName());
                    cart.setPro_desc(localityList.getProductUnit());
                    cart.setPro_qty(localityList.getProductQty());
                    cart.setPro_price(localityList.getProductPrice());
                    cart.setProduct_temp_price(localityList.getProduct_temp_price());
                    cart.setProduct_discount(localityList.getProduct_discount());
                    cart.setProduct_unit(localityList.getProductUnit());
                    cart.setPro_total(String.valueOf(sum));
                    cart.setPro_image(String.valueOf(localityList.getProductImage()));
                    updateProduct(AppDatabase.getAppDatabase(context),cart);
                }catch (NullPointerException e) {
                    Cartdb cart = new Cartdb();
                    cart.setPro_id(localityList.getProductId());
                    cart.setTitle(localityList.getProductName());
                    cart.setProduct_unit(localityList.getProductUnit());
                    cart.setPro_desc(localityList.getProductUnit());
                    cart.setPro_qty(localityList.getProductQty());
                    cart.setPro_price(localityList.getProductPrice());
                    cart.setProduct_temp_price(localityList.getProduct_temp_price());
                    cart.setProduct_discount(localityList.getProduct_discount());
                    cart.setPro_total(String.valueOf(sum));
                    cart.setPro_image(String.valueOf(localityList.getProductImage()));
                    addProduct(AppDatabase.getAppDatabase(context), cart);
                }
                holder.txt_One.setText(String.valueOf(sum));
                if(!value){
                    int mul = Integer.parseInt(localityList.getProductPrice())* Integer.parseInt(holder.txt_One.getText().toString());
                    holder.textView_prize.setText("\u20B9 "+mul);
                }
                showData(v);

            }
        });
      /*  holder.img_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteProduct(AppDatabase.getAppDatabase(context),String.valueOf(localityList.getProductId()));
                if(!value){
                    arrayLists.remove(position);
                    notifyDataSetChanged();
                }
            }
        });*/
        holder.txt_Minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int min = Integer.parseInt(holder.txt_One.getText().toString()) - 1;
                holder.txt_One.setText(String.valueOf(min));
                if(holder.txt_One.getText().toString().equals("0")){
                  holder.txt_Minus.setVisibility(View.GONE);
                  holder.txt_Add.setVisibility(View.GONE);
                  holder.txt_One.setVisibility(View.GONE);
                  holder.txt_One.setText("0");
                    deleteProduct(AppDatabase.getAppDatabase(context),String.valueOf(localityList.getProductId()));
                    if(!value){
                        arrayLists.remove(position);
                        notifyDataSetChanged();
                    }
                }else
                    {
                    Cartdb cartdb = getSingleProduct(AppDatabase.getAppDatabase(context), String.valueOf(localityList.getProductId()));
                    Cartdb cart = new Cartdb();
                    cart.setId(cartdb.getId());
                    cart.setPro_id(localityList.getProductId());
                    cart.setTitle(localityList.getProductName());
                    cart.setPro_desc(localityList.getProductUnit());
                    cart.setProduct_unit(localityList.getProductUnit());
                    cart.setPro_qty(localityList.getProductQty());
                    cart.setPro_price(localityList.getProductPrice());
                    cart.setProduct_temp_price(localityList.getProduct_temp_price());
                    cart.setProduct_discount(localityList.getProduct_discount());
                    cart.setPro_total(String.valueOf(min));
                    cart.setPro_image(String.valueOf(localityList.getProductImage()));
                    updateProduct(AppDatabase.getAppDatabase(context),cart);
                    if(!value){
                        double mul = Integer.parseInt(localityList.getProductPrice())* Double.parseDouble(holder.txt_One.getText().toString());
                        holder.textView_prize.setText("\u20B9 "+mul);
                    }
                }
                showData(v);
            }
        });

    }


    @Override
    public int getItemCount() {
        return arrayLists.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView textView_id,title, textView_qty, textView_prize, textView_description, txt_plus, txt_Minus, txt_One, txt_Add,textView_temp_price,textView_product_discount;
        public ImageView images,img_delete;
        public LinearLayout ltyCategory;
        public CardView cvCategory;

        public MyViewHolder(View view) {
            super(view);
            images = (ImageView) view.findViewById(R.id.imageView_image);
            //img_delete = (ImageView) view.findViewById(R.id.img_delete);
            textView_id = (TextView) view.findViewById(R.id.textView_id);
            textView_temp_price = (TextView) view.findViewById(R.id.textView_temp_price);
            textView_product_discount = (TextView) view.findViewById(R.id.textView_product_discount);
            title = (TextView) view.findViewById(R.id.textView_title);
            textView_description = (TextView) view.findViewById(R.id.textView_description);
            textView_qty = (TextView) view.findViewById(R.id.textView_qty);
            textView_prize = (TextView) view.findViewById(R.id.textView_prize);
            txt_plus = (TextView) view.findViewById(R.id.txt_plus);
            txt_Minus = (TextView) view.findViewById(R.id.txt_Minus);
            txt_One = (TextView) view.findViewById(R.id.txt_One);
            txt_Add = (TextView) view.findViewById(R.id.txt_Add);
            ltyCategory = (LinearLayout) view.findViewById(R.id.ltyCategory);
            cvCategory = (CardView) view.findViewById(R.id.cvCategory);
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

    public void showData(View v){
        list = new ArrayList<>();
        list = (ArrayList<Cartdb>) getProductList(AppDatabase.getAppDatabase(context));
        double total = 0.0;
        double price = 0.0;
        double item = 0.0;

        for(Cartdb data : list){
            total = total + 1;
            item = item + Double.parseDouble(data.getPro_total());
            price = Double.parseDouble(data.getPro_price())*Double.parseDouble(data.getPro_total())+price;
        }
        if(value) {
            snackbar = Snackbar
                    .make(v, " Total : " + context.getResources().getString(R.string.Rs) + price, Snackbar.LENGTH_INDEFINITE)
                    .setAction("Go To Cart", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            context.startActivity(new Intent(context, CartListActivity.class));
                        }
                    });

// Changing message text color
            snackbar.setActionTextColor(Color.RED);
// Changing action button text color
            snackbar.show();
        }else {
            mCallback.onHandleSelection(" Total : " + mContext.getResources().getString(R.string.Rs) + price,list);
        }
    }

    public void getFilter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        arrayLists.clear();
        if (charText.length() == 0) {
            arrayLists.addAll(tempArrayLists);
        } else {
            for (LocalityList data : tempArrayLists) {
                if (data.getProductName().toLowerCase(Locale.getDefault()).contains(charText)) {
                    arrayLists.add(data);
                }
            }
        }
        notifyDataSetChanged();
    }

    public interface CallbackInterface{

        /**
         * Callback invoked when clicked
         * @param text - the text to pass back
         * @param list
         */
        void onHandleSelection(String text, ArrayList<Cartdb> list);
    }
}
