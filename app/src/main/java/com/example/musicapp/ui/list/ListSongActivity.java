package com.example.musicapp.ui.list;

import static com.example.musicapp.utils.AppConstants.MusicBundleKey.KEY_SEARCH;
import static com.example.musicapp.utils.AppConstants.MusicBundleKey.TYPE;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.example.musicapp.BR;
import com.example.musicapp.R;
import com.example.musicapp.databinding.ActivityListSongBinding;
import com.example.musicapp.di.component.ActivityComponent;
import com.example.musicapp.ui.base.BaseActivity;

public class ListSongActivity extends BaseActivity<ActivityListSongBinding, ListSongViewModel> {

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
        listSongAdapter = new ListSongAdapter(typeData, keySearch);
        mViewDataBinding.rcvListSong.setAdapter(listSongAdapter);
    }

    private void observerDataInViewModel(){
        mViewModel.getMutableLiveData().observe(this, songs -> {
            Log.e("TAG", "observerDataInViewModel: ");
            listSongAdapter.submitList(songs);
        });
    }
}
