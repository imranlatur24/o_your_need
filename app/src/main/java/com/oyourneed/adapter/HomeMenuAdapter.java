package com.oyourneed.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.oyourneed.R;
import com.oyourneed.activity.HomeActivity;
import com.oyourneed.activity.ProductListActivity;
import com.oyourneed.model.CategoryList;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class HomeMenuAdapter extends RecyclerView.Adapter<HomeMenuAdapter.MyViewHolder> {

    private Context context;

    private ArrayList<CategoryList> arrayLists;
    private ArrayList<CategoryList> tempArrayLists;

    public HomeMenuAdapter(HomeActivity context) {
        this.context = context;
    }

    public List<CategoryList> getListArray() {
        return arrayLists;
    }

    public void setListArray(ArrayList<CategoryList> arrayLists) {
        this.arrayLists = arrayLists;
        tempArrayLists = new ArrayList<>();
        tempArrayLists.addAll(arrayLists);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.menu_adapter, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        final CategoryList categoryList = arrayLists.get(position);

        holder.title.setText(categoryList.getCategoryName());

        Picasso.with(context)
                .load(categoryList.getCategoryImage())
                .placeholder(R.drawable.loading_image)
                .into(holder.images);
        System.out.println("IMAGE CATEGORY = "+ categoryList.getCategoryImage());
        holder.title.setText(categoryList.getCategoryName());
        holder.title.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        holder.title.setSelected(true);

        holder.cvCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tempArrayLists.get(position);
                Intent intent = new Intent(context, ProductListActivity.class);
                intent.putExtra("cat_id", arrayLists.get(position).getCategoryId());
                intent.putExtra("cat_name", arrayLists.get(position).getCategoryName());
                context.startActivity(intent);
                System.out.println("CATEGORY ID "+arrayLists.get(position).getCategoryId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayLists.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView title;
        public ImageView images;
        public LinearLayout ltyCategory;
        public CardView cvCategory;

        public MyViewHolder(View view) {
            super(view);
            images = (ImageView) view.findViewById(R.id.imageView_image);
            title = (TextView) view.findViewById(R.id.textView_title);
            ltyCategory = (LinearLayout) view.findViewById(R.id.ltyCategory);
            cvCategory = (CardView) view.findViewById(R.id.cvCategoryProduct);
        }
    }
}
