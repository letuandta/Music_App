package com.example.musicapp.ui.main;

import static com.example.musicapp.utils.AppConstants.MusicBundleKey.ACTION_MUSIC;
import static com.example.musicapp.utils.AppConstants.MusicBundleKey.KEY_SEARCH;
import static com.example.musicapp.utils.AppConstants.MusicBundleKey.TYPE;
import static com.example.musicapp.utils.AppConstants.MusicPlayerActions.ACTION_STOP;
import static com.example.musicapp.utils.AppConstants.MusicPlayerType.FAVORITES_SONG;
import static com.example.musicapp.utils.AppConstants.MusicPlayerType.RECOMMEND_SONG;
import static com.example.musicapp.utils.AppConstants.MusicPlayerType.SEARCH_SONG;
import static com.example.musicapp.utils.AppConstants.MusicPlayerType.SEARCH_SONG_OFFLINE;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.example.musicapp.BR;
import com.example.musicapp.R;
import com.example.musicapp.databinding.ActivityMainBinding;
import com.example.musicapp.di.component.ActivityComponent;
import com.example.musicapp.services.MusicPlayerService;
import com.example.musicapp.ui.base.BaseActivity;
import com.example.musicapp.ui.favorite.FavoritesFragment;
import com.example.musicapp.ui.list.ListSongActivity;
import com.example.musicapp.ui.recommend.RecommendsFragment;
import com.example.musicapp.utils.NetworkUtils;

import java.io.IOException;

public class MainActivity extends BaseActivity<ActivityMainBinding, MainViewModel> {

    private FragmentManager fragmentManager;
    //<!-- endregion -->

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(mViewDataBinding.getRoot());
        addFragment();
        requestPermissionsForApplication();
        setEventSeeAll();
        setEventSearch();

    }

    @Override
    public void performDependencyInjection(ActivityComponent buildComponent) {
        buildComponent.inject(this);
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
        mViewDataBinding.favoriteSeeAll.setOnClickListener(view -> {
            bundle.putString(TYPE, FAVORITES_SONG);
            intent.putExtras(bundle);
            startActivity(intent);
        });
        mViewDataBinding.recommendSeeAll.setOnClickListener(view -> {
            bundle.putString(TYPE, RECOMMEND_SONG);
            intent.putExtras(bundle);
            startActivity(intent);
        });
    }

    private void setEventSearch() {
        mViewDataBinding.searchIcon.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, ListSongActivity.class);
            Bundle bundle = new Bundle();
            try {
                if(NetworkUtils.isConnected())
                    bundle.putString(TYPE, SEARCH_SONG);
                else bundle.putString(TYPE, SEARCH_SONG_OFFLINE);
            } catch (InterruptedException | IOException e) {
                throw new RuntimeException(e);
            }
            bundle.putString(KEY_SEARCH, mViewDataBinding.edtSearch.getText().toString());
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