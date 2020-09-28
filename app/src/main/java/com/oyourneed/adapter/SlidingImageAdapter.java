package com.oyourneed.adapter;
import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.oyourneed.R;
import com.oyourneed.model.SliderList;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class SlidingImageAdapter extends PagerAdapter {


    private  int IMAGES [] = {R.drawable.demo2,R.drawable.demo2,R.drawable.demo2,
    R.drawable.demo2};

    private ArrayList<SliderList> sliderLists;
    private LayoutInflater inflater;
    private Context context;

    public SlidingImageAdapter(Context context,ArrayList<SliderList> sliderLists) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.sliderLists = sliderLists;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return sliderLists.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View imageLayout = inflater.inflate(R.layout.slidingimages_layout, view, false);

        assert imageLayout != null;
        final ImageView imageView = (ImageView) imageLayout.findViewById(R.id.image);
        final TextView txtTitle = (TextView) imageLayout.findViewById(R.id.txtTitle);
        final TextView txtMobile = (TextView) imageLayout.findViewById(R.id.txtMobile);

        Picasso.with(context).load(
                sliderLists.get(position).getsliderimg_image()).error(R.drawable.loading_image).into(imageView);
        System.out.println("Image Slider : "+sliderLists.get(position).getsliderimg_image());
        txtTitle.setVisibility(View.GONE);
        txtMobile.setVisibility(View.GONE);
        view.addView(imageLayout, 0);

        return imageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }


}