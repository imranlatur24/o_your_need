package com.oyourneed.adapter;

import android.app.Dialog;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.oyourneed.R;
import com.oyourneed.activity.CartListActivity;
import com.oyourneed.data.AppDatabase;
import com.oyourneed.model.Cartdb;

import java.util.ArrayList;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.MyViewHolder> {

    private CartListActivity context;
    List<Cartdb> list;

    private int ID[] = {
            1,
            2,
            3,
    };
    private String TITLE[] = {
            "Strawberry",
            "Apple",
            "Orange",
    };
    private String DISCRIPTION[] = {
            "Good for Eyes",
            "Good for Digession",
            "Good for Skin",

    };
    private String QTY[] = {
            "1 kg",
            "1 kg",
            "1 kg",
    };
    private String PRIZE[] = {
            "55.49",
            "76.52",
            "40.00",
    };
    private int IMAGES[] = {
            R.drawable.strawberries,
            R.drawable.apple,
            R.drawable.orange,
    };

    private int COlOR[] = {
            R.color.white,
            R.color.white,
            R.color.white,
    };


    private Dialog dialog1;

    private float prize;

    public CartAdapter(CartListActivity context) {
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cart_adapter, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        int w = holder.cvCategory.getResources().getDisplayMetrics().widthPixels;
        final int h = holder.cvCategory.getResources().getDisplayMetrics().heightPixels;

        holder.title.setText(TITLE[position]);
        holder.textView_id.setText(String.valueOf(ID[position]));
        holder.textView_description.setText(DISCRIPTION[position]);
        holder.textView_qty.setText(QTY[position]);
        holder.textView_prize.setText("\u20B9 "+PRIZE[position]);
        holder.textView_description.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        holder.title.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        holder.title.setSelected(true);
        holder.images.setImageResource(IMAGES[position]);
        holder.ltyCategory.setBackgroundColor(context.getResources().getColor(COlOR[position]));

        holder.imageView_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int itemLabel = ID[position];

                // Show the removed item label
                Toast.makeText(context,"Removed : " + itemLabel,Toast.LENGTH_SHORT).show();
            }
        });

        try {
            Cartdb cartdb = getSingleProduct(AppDatabase.getAppDatabase(context), String.valueOf(ID[position]));
            holder.txt_Minus.setVisibility(View.VISIBLE);
            holder.txt_Add.setVisibility(View.GONE);
            holder.txt_One.setVisibility(View.VISIBLE);
            holder.txt_One.setText(cartdb.getPro_total());
            if(holder.txt_One.getText().toString().equals("0")){
                holder.txt_Minus.setVisibility(View.GONE);
                holder.txt_Add.setVisibility(View.VISIBLE);
                holder.txt_One.setVisibility(View.GONE);
            }
        }catch (NullPointerException e){

        }

        holder.txt_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.txt_Add.setVisibility(View.GONE);
                holder.txt_Minus.setVisibility(View.VISIBLE);
                holder.txt_One.setVisibility(View.VISIBLE);

                prize = Float.parseFloat((PRIZE[position]));

                int sum = 1 + Integer.parseInt(holder.txt_One.getText().toString());
                if(holder.txt_One.getText().toString().equals("0")){
                    Cartdb cart = new Cartdb();
                    cart.setPro_id(String.valueOf(ID[position]));
                    cart.setPro_desc(DISCRIPTION[position]);
                    cart.setPro_qty(QTY[position]);
                    cart.setPro_price(PRIZE[position]);
                    cart.setPro_total(String.valueOf(sum));
                    cart.setPro_image(String.valueOf(IMAGES[position]));
                    addProduct(AppDatabase.getAppDatabase(context),cart);
                }else {
                    Cartdb cartdb = getSingleProduct(AppDatabase.getAppDatabase(context), String.valueOf(ID[position]));
                    Cartdb cart = new Cartdb();
                    cart.setId(cartdb.getId());
                    cart.setPro_id(String.valueOf(ID[position]));
                    cart.setPro_desc(DISCRIPTION[position]);
                    cart.setPro_qty(QTY[position]);
                    cart.setPro_price(PRIZE[position]);
                    cart.setPro_total(String.valueOf(sum));
                    cart.setPro_image(String.valueOf(IMAGES[position]));
                    updateProduct(AppDatabase.getAppDatabase(context),cart);
                }
                holder.txt_One.setText(String.valueOf(sum));

                //double sum2 = prize + Float.parseFloat(holder.textView_prize.getText().toString());
                //double sum2 = prize + Float.parseFloat(holder.textView_prize.getText().toString());
                showData(v);

            }
        });
        holder.txt_Minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int min = Integer.parseInt(holder.txt_One.getText().toString()) - 1;
                holder.txt_One.setText(String.valueOf(min));
                if(holder.txt_One.getText().toString().equals("0")){
                  holder.txt_Minus.setVisibility(View.GONE);
                  holder.txt_Add.setVisibility(View.VISIBLE);
                  holder.txt_One.setVisibility(View.VISIBLE);
                  //holder.txt_One.setText("0");
                    deleteProduct(AppDatabase.getAppDatabase(context),String.valueOf(ID[position]));
                }else {
                    Cartdb cartdb = getSingleProduct(AppDatabase.getAppDatabase(context), String.valueOf(ID[position]));
                    Cartdb cart = new Cartdb();
                    cart.setId(cartdb.getId());
                    cart.setPro_id(String.valueOf(ID[position]));
                    cart.setPro_desc(DISCRIPTION[position]);
                    cart.setPro_qty(QTY[position]);
                    cart.setPro_price(PRIZE[position]);
                    cart.setPro_total(String.valueOf(min));
                    cart.setPro_image(String.valueOf(IMAGES[position]));
                    updateProduct(AppDatabase.getAppDatabase(context),cart);
                }
                showData(v);
            }
        });
        holder.ltyCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (position) {
                    case 0:

                        break;
                    case 1:
                        //context.startActivity(new Intent(context, Notification_MainActivity.class));
                        break;
                    case 2:
                        //context.startActivity(new Intent(context, Support.class));
                        break;
                    case 3:
                        //context.startActivity(new Intent(context, Launcher.class));
                        break;
                    case 4:
                        //context.startActivity(new Intent(context, ProfileActivity.class));
                        break;
                    case 5:
                        //context.startActivity(new Intent(context, ChangePasswordActivity.class));
                        break;
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return IMAGES.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView textView_id,title, textView_qty, textView_prize, textView_description, txt_plus, txt_Minus, txt_One,
                txt_Add;
        public ImageView images,imageView_delete;
        public LinearLayout ltyCategory;
        public CardView cvCategory;

        public MyViewHolder(View view) {
            super(view);
            images = (ImageView) view.findViewById(R.id.imageView_image);
            textView_id = (TextView) view.findViewById(R.id.textView_id);
            imageView_delete = (ImageView) view.findViewById(R.id.imageView_delete);
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

    private void showData(View v){
        list = new ArrayList<>();
        list = getProductList(AppDatabase.getAppDatabase(context));
        int total = 0;
        int price = 0;
        System.out.println("data"+list.size());
        for(Cartdb data : list){
            total = Integer.parseInt(data.getPro_total())+total;
        }

        Snackbar snackbar = Snackbar
                .make(v,"Total : "+total+" Qty"+price, Snackbar.LENGTH_LONG)
               /* .setAction("Go To Cart", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                    }
                })*/;

// Changing message text color
        snackbar.setActionTextColor(Color.RED);
// Changing action button text color
        snackbar.show();
    }


    /*public void getFilter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        arrayLists.clear();
        if (charText.length() == 0) {
            arrayLists.addAll(tempArrayLists);
        } else {
            for (OrderList data : tempArrayLists) {
                if (data.getOrderNo().toLowerCase(Locale.getDefault()).contains(charText)||
                        data.getOrderNo().toLowerCase(Locale.getDefault()).contains(charText)) {
                    arrayLists.add(data);
                }
            }
        }
        notifyDataSetChanged();
    }*/
}
