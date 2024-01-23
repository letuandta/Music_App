package com.example.musicapp.utils;

import android.content.Context;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;

public class BindingUtils {

    @BindingAdapter("setImageUrl")
    public static void setImageUrl(ImageView imageView, String url){
        Context context = imageView.getContext();
        Glide.with(context).load(url).into(imageView);
    }
}
