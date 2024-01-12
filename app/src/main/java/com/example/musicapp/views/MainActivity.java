package com.example.musicapp.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;

import com.example.musicapp.R;
import com.example.musicapp.adapter.FavoriteSongAdapter;
import com.example.musicapp.adapter.RecommendSongAdapter;
import com.example.musicapp.common.MusicPlayerActions;
import com.example.musicapp.databinding.ActivityMainBinding;
import com.example.musicapp.models.Song;
import com.example.musicapp.services.MusicPlayerService;
import com.example.musicapp.viewmodels.FavoriteSongViewModel;
import com.example.musicapp.viewmodels.RecommendSongViewModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Intent intentService = new Intent(MainActivity.this, MusicPlayerService.class);
        Bundle bundle = new Bundle();
        bundle.putInt("action_music", MusicPlayerActions.ACTION_STOP);
        intentService.putExtras(bundle);
        this.startService(intentService);
        Log.e("TAG", "onDestroy: " );
    }
}