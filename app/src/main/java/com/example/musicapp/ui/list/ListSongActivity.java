package com.example.musicapp.ui.list;

import static com.example.musicapp.utils.AppConstants.MusicBundleKey.KEY_SEARCH;
import static com.example.musicapp.utils.AppConstants.MusicBundleKey.POSITION;
import static com.example.musicapp.utils.AppConstants.MusicBundleKey.TYPE;
import static com.example.musicapp.utils.AppConstants.MusicPlayerType.FAVORITES_SONG;
import static com.example.musicapp.utils.AppConstants.MusicPlayerType.SEARCH_SONG_OFFLINE;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.musicapp.BR;
import com.example.musicapp.R;
import com.example.musicapp.databinding.ActivityListSongBinding;
import com.example.musicapp.di.component.ActivityComponent;
import com.example.musicapp.ui.base.BaseActivity;
import com.example.musicapp.ui.player.MusicPlayerActivity;
import com.example.musicapp.utils.NetworkUtils;

import java.io.IOException;

public class ListSongActivity extends BaseActivity<ActivityListSongBinding, ListSongViewModel> implements ListSongAdapter.ListSongListener {

    ListSongAdapter listSongAdapter;
    String typeData, keySearch;

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_list_song;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getDataFromBundle();
        initBinding();
        initViewModel();
        initAdapter();
        observerDataInViewModel();
        setContentView(mViewDataBinding.getRoot());
    }

    @Override
    public void performDependencyInjection(ActivityComponent buildComponent) {
        buildComponent.inject(this);
    }

    private void getDataFromBundle() {
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            typeData = bundle.getString(TYPE, "");
            keySearch = bundle.getString(KEY_SEARCH, "");
        }
    }

    private void initViewModel() {
        mViewModel.initData(typeData, keySearch);
    }

    private void initAdapter() {
        listSongAdapter = new ListSongAdapter(this);
        mViewDataBinding.rcvListSong.setAdapter(listSongAdapter);
    }

    private void initBinding() {
        mViewDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_list_song);
    }

    private void observerDataInViewModel(){
        mViewModel.getMutableLiveData().observe(this, songs -> {
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
            if(NetworkUtils.isConnected() || typeData.equals(FAVORITES_SONG) || typeData.equals(SEARCH_SONG_OFFLINE)){
                startActivity(intent);
            }else {
                Toast.makeText(this, "No Internet", Toast.LENGTH_SHORT).show();
            }
        } catch (InterruptedException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
