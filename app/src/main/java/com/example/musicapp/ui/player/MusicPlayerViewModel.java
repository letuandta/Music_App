package com.example.musicapp.ui.player;

import static com.example.musicapp.utils.AppConstants.MusicBundleKey.ACTION_MUSIC;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.databinding.BindingAdapter;
import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.MutableLiveData;

import com.example.musicapp.data.AppDataManager;
import com.example.musicapp.data.model.local.Song;
import com.example.musicapp.services.MusicPlayerService;
import com.example.musicapp.ui.base.BaseViewModel;
import com.example.musicapp.utils.NetworkUtils;

import java.io.File;
import java.io.IOException;
import java.util.Objects;


public class MusicPlayerViewModel extends BaseViewModel {
    private final MutableLiveData<Song> mutableLiveData;

    public ObservableBoolean isFavorite = new ObservableBoolean(false);

    public MusicPlayerViewModel(AppDataManager appDataManager) {
        super(appDataManager);
        mutableLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<Song> getMutableLiveData() {
        return mutableLiveData;
    }

    public void checkFavorite(){
        isFavorite.set(mDataManager.mRealmRepository.existsSong(getMutableLiveData().getValue()));
    }

    @BindingAdapter("isFavorite")
    public static void setColorFavoriteIcon(ImageView view, boolean is){
        if(is){
            view.setColorFilter(Color.argb(255, 255, 0, 0));
        }
        else {
            view.setColorFilter(Color.argb(255, 255, 255, 255));
        }
    }

    public void addFavorite(View view){
        try {
            if(!isFavorite.get()) {
                if (NetworkUtils.isConnected()) {
                    try {
                        mDataManager.mRealmRepository.addSong(mutableLiveData.getValue());
                        String fileName = Objects.requireNonNull(mutableLiveData.getValue()).getId() + ".mp3";
                        File file = mDataManager.mUserDataRepository.getSong(fileName);
                        if (!file.exists())
                            mDataManager.mUserDataRepository.downloadSong(mutableLiveData.getValue());
                        isFavorite.set(true);
                    } catch (Exception e) {
                        Log.e("ERROR", "Error: " + e.getMessage());
                    }
                }else Toast.makeText(view.getContext(), "No internet", Toast.LENGTH_LONG).show();
            }else{
                try {
                    mDataManager.mRealmRepository.deleteSong(mutableLiveData.getValue());
                    isFavorite.set(false);
                } catch (Exception e) {
                    Log.e("ERROR", "Error: " + e.getMessage());
                }
            }
        } catch (InterruptedException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void handleOnClickIcon(View view, int action){
        Intent intentService = new Intent(view.getContext(), MusicPlayerService.class);
        Bundle bundle = new Bundle();
        bundle.putInt(ACTION_MUSIC, action);
        intentService.putExtras(bundle);
        view.getContext().startService(intentService);
    }
}
