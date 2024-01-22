package com.example.musicapp.ui.player;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.databinding.Bindable;
import androidx.databinding.BindingAdapter;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.musicapp.MyApplication;
import com.example.musicapp.models.Song;

import java.io.File;
import java.util.Objects;


public class MusicPlayerViewModel extends ViewModel {
    private MutableLiveData<Song> mutableLiveData;
    private MusicPlayerCallBack callBack;
    private static boolean isFavorite;

    public MusicPlayerViewModel() {
        Song song = new Song();
        mutableLiveData = new MutableLiveData<>(song);
    }

    public MutableLiveData<Song> getMutableLiveData() {
        return mutableLiveData;
    }

    public void setCallBack(MusicPlayerCallBack callBack) { // TODO: Bad practice
        this.callBack = callBack;
    }

    public MusicPlayerCallBack getCallBack() {
        return callBack;
    }

    @BindingAdapter("myIconColor")
    public static void setBackgroundColor(ImageView view, String id){
        Log.e("ChangeColorIcon", "progress");
        if(id != null && !id.isEmpty()) {
            if (MyApplication.mFavoritesRepository.exists(id)) {
                view.setColorFilter(Color.argb(255, 255, 0, 0));
                isFavorite = true;
            }
            else {
                view.setColorFilter(Color.argb(255, 255, 255, 255));
                isFavorite = false;
            }
        }
    }

    public void addFavorite(View view){
        if(!isFavorite){
            try {
                MyApplication.mFavoritesRepository.addSong(mutableLiveData.getValue());
                String fileName = Objects.requireNonNull(mutableLiveData.getValue()).getId() + ".mp3";
                File file = MyApplication.mOfflineRepository.getSong(view.getContext(), fileName);
                if (!file.exists())
                    MyApplication.mOfflineRepository.downloadSong(view.getContext(), mutableLiveData.getValue());
                isFavorite = true;
                callBack.addFavorite(true);
            } catch (Exception e) {
                Log.e("FAVORITES LIST", "can't add song into favorites list");
            }
        }else{
            try{
                MyApplication.mFavoritesRepository.deleteSong(mutableLiveData.getValue());
                isFavorite = false;
                callBack.deleteFavorite(true);
            }catch (Exception e) {
                Log.e("FAVORITES LIST", "can't delete song into favorites list");
            }
        }
    }

    public interface MusicPlayerCallBack{
        void handleOnClickIcon(int action);
        void addFavorite(boolean isFavorite);
        void deleteFavorite(boolean isSuccess);
    }
}
