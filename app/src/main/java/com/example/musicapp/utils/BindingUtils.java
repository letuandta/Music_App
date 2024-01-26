package com.example.musicapp.utils;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;

public class BindingUtils {

    @BindingAdapter("setImageUrl")
    public static void setImageUrl(ImageView imageView, String url){
        Context context = imageView.getContext();
        Glide.with(context).load(url).into(imageView);
    }

    @BindingAdapter("progressing")
    public static void progressing(View view, boolean isLoading){
        if(isLoading) view.setVisibility(View.VISIBLE);
        else view.setVisibility(View.GONE);
    }
}
