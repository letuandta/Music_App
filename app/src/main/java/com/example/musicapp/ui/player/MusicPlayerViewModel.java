package com.example.musicapp.ui.player;

import static com.example.musicapp.utils.AppConstants.MusicBundleKey.ACTION_MUSIC;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;
import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.MutableLiveData;

import com.example.musicapp.data.AppDataManager;
import com.example.musicapp.data.model.local.Song;
import com.example.musicapp.services.MusicPlayerService;
import com.example.musicapp.ui.base.BaseViewModel;

import java.io.File;
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
        if(!isFavorite.get()){
            try {
                mDataManager.mRealmRepository.addSong(mutableLiveData.getValue());
                String fileName = Objects.requireNonNull(mutableLiveData.getValue()).getId() + ".mp3";
                File file = mDataManager.mUserDataRepository.getSong(fileName);
                if (!file.exists())
                    mDataManager.mUserDataRepository.downloadSong(mutableLiveData.getValue());
                isFavorite.set(true);
            } catch (Exception e) {
                Log.e("FAVORITES LIST", "can't add song into favorites list");
            }
        }else{
            try{
                mDataManager.mRealmRepository.deleteSong(mutableLiveData.getValue());
                isFavorite.set(false);
            }catch (Exception e) {
                Log.e("FAVORITES LIST", "can't delete song into favorites list");
            }
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
