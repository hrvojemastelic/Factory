package com.kepler22b.articles.Adapter;


import android.databinding.BindingAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;



public class MyBindingAdapter {

    //glide image loader
    @BindingAdapter("glide:showImage")
    public static void setImageByGlide(ImageView view, String urlToImage) {

        Glide.with(view.getContext())
                .load(urlToImage)
                .into(view);

    }


}
