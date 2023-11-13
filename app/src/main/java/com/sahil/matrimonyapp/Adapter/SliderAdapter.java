package com.sahil.matrimonyapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.appcompat.view.menu.MenuView;

import com.bumptech.glide.Glide;
import com.prathamesh.matrimonyapp.Model.SliderImages;
import com.prathamesh.matrimonyapp.R;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.List;

public class SliderAdapter extends SliderViewAdapter<SliderAdapter.ViewHolder>{

    private List<SliderImages> imageUrl;
    private Context mCOntext;
    private String url = null;

    public SliderAdapter(List<SliderImages> imageUrl, Context mCOntext) {
        this.imageUrl = imageUrl;
        this.mCOntext = mCOntext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.sliderlayoutitem, null);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        SliderImages sliderItem = imageUrl.get(position);

        Glide.with(viewHolder.itemView).load(sliderItem.getImgUrl()).fitCenter()
                .into(viewHolder.image);

    }

    @Override
    public int getCount() {
        return imageUrl.size();
    }

    public class ViewHolder extends SliderViewAdapter.ViewHolder{

        public View itemView;
        public ImageView image;
        public ViewHolder(View itemView) {
            super(itemView);

            this.itemView = itemView;
            image = itemView.findViewById(R.id.myimage);
        }
    }
}
