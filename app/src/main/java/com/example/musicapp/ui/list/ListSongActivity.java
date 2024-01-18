package com.example.musicapp.ui.list;

import static com.example.musicapp.common.AppConstants.MusicBundleKey.KEY_SEARCH;
import static com.example.musicapp.common.AppConstants.MusicBundleKey.POSITION;
import static com.example.musicapp.common.AppConstants.MusicBundleKey.TYPE;
import static com.example.musicapp.common.AppConstants.MusicPlayerType.FAVORITES_SONG;
import static com.example.musicapp.common.AppConstants.MusicPlayerType.SEARCH_SONG_OFFLINE;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.musicapp.R;
import com.example.musicapp.common.AppConstants;
import com.example.musicapp.common.InternetConnection;
import com.example.musicapp.databinding.ActivityListSongBinding;
import com.example.musicapp.ui.player.MusicPlayerActivity;

import java.io.IOException;

public class ListSongActivity extends AppCompatActivity implements ListSongAdapter.ListSongListener {

    ActivityListSongBinding binding;
    ListSongViewModel listSongViewModel;
    ListSongAdapter listSongAdapter;
    String typeData, keySearch;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getDataFromBundle();
        initBinding();
        initViewModel();
        initAdapter();
        observerDataInViewModel();
        setContentView(binding.getRoot());
    }

    private void getDataFromBundle() {
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            typeData = bundle.getString(TYPE, "");
            keySearch = bundle.getString(KEY_SEARCH, "");
        }
    }

    private void initViewModel() {
        listSongViewModel = new ViewModelProvider(this).get(ListSongViewModel.class);
        listSongViewModel.initData(typeData, keySearch);
    }

    private void initAdapter() {
        listSongAdapter = new ListSongAdapter(this);
        binding.rcvListSong.setAdapter(listSongAdapter);
    }

    private void initBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_list_song);
    }

    private void observerDataInViewModel(){
        listSongViewModel.getMutableLiveData().observe(this, songs -> {
            Log.e("TAG", "observerDataInViewModel: ");
            listSongAdapter.submitList(songs);
        });
    }

    @Override
    public void onClickItem(int position) {
        try {
            Bundle bundle = new Bundle();

            bundle.putString(TYPE, typeData);
            bundle.putInt(POSITION, position);
            bundle.putString(KEY_SEARCH, keySearch);

            Intent intent = new Intent(this, MusicPlayerActivity.class);
            intent.putExtras(bundle);
            if(InternetConnection.isConnected() || typeData.equals(FAVORITES_SONG) || typeData.equals(SEARCH_SONG_OFFLINE)){
                startActivity(intent);
            }else {
                Toast.makeText(this, "No Internet", Toast.LENGTH_SHORT).show();
            }
        } catch (InterruptedException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
