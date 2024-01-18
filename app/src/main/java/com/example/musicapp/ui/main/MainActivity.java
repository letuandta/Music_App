package com.example.musicapp.ui.main;

import static com.example.musicapp.common.AppConstants.MusicBundleKey.ACTION_MUSIC;
import static com.example.musicapp.common.AppConstants.MusicBundleKey.KEY_SEARCH;
import static com.example.musicapp.common.AppConstants.MusicBundleKey.TYPE;
import static com.example.musicapp.common.AppConstants.MusicPlayerActions.ACTION_STOP;
import static com.example.musicapp.common.AppConstants.MusicPlayerType.FAVORITES_SONG;
import static com.example.musicapp.common.AppConstants.MusicPlayerType.RECOMMEND_SONG;
import static com.example.musicapp.common.AppConstants.MusicPlayerType.SEARCH_SONG;
import static com.example.musicapp.common.AppConstants.MusicPlayerType.SEARCH_SONG_OFFLINE;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.example.musicapp.R;
import com.example.musicapp.common.AppConstants;
import com.example.musicapp.common.InternetConnection;
import com.example.musicapp.databinding.ActivityMainBinding;
import com.example.musicapp.services.MusicPlayerService;
import com.example.musicapp.ui.favorite.FavoritesFragment;
import com.example.musicapp.ui.list.ListSongActivity;
import com.example.musicapp.ui.recommend.RecommendsFragment;

import java.io.IOException;

public class MainActivity extends AppCompatActivity  {
    //<!-- region declare -->
    ActivityMainBinding binding;
    private FragmentManager fragmentManager;
    //<!-- endregion -->

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflateBinding();
        setContentView(binding.getRoot());
        addFragment();
        requestPermissionsForApplication();
        setEventSeeAll();
        setEventSearch();

    }



    private void inflateBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
    }
    private void addFragment() {
        fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction()
                .add(R.id.fragment_favorites_song, FavoritesFragment.newInstance(), "fragment_favorites")
                .add(R.id.fragment_recommend_song, RecommendsFragment.newInstance(), "fragment_recommend")
                .commit();
    }
    private void requestPermissionsForApplication() {
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 111);
        }
    }

    private void setEventSeeAll() {
        Intent intent = new Intent(MainActivity.this, ListSongActivity.class);
        Bundle bundle = new Bundle();
        binding.favoriteSeeAll.setOnClickListener(view -> {
            bundle.putString(TYPE, FAVORITES_SONG);
            intent.putExtras(bundle);
            startActivity(intent);
        });
        binding.recommendSeeAll.setOnClickListener(view -> {
            bundle.putString(TYPE, RECOMMEND_SONG);
            intent.putExtras(bundle);
            startActivity(intent);
        });
    }

    private void setEventSearch() {
        binding.searchIcon.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, ListSongActivity.class);
            Bundle bundle = new Bundle();
            try {
                if(InternetConnection.isConnected())
                    bundle.putString(TYPE, SEARCH_SONG);
                else bundle.putString(TYPE, SEARCH_SONG_OFFLINE);
            } catch (InterruptedException | IOException e) {
                throw new RuntimeException(e);
            }
            bundle.putString(KEY_SEARCH, binding.edtSearch.getText().toString());
            intent.putExtras(bundle);
            startActivity(intent);
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(MusicPlayerService.isServiceRunning){
            Intent intentService = new Intent(MainActivity.this, MusicPlayerService.class);
            Bundle bundle = new Bundle();
            bundle.putInt(ACTION_MUSIC, ACTION_STOP);
            intentService.putExtras(bundle);
            this.startService(intentService);
        }
    }
}