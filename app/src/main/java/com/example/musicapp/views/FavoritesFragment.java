package com.example.musicapp.views;

import static com.example.musicapp.common.MusicBundleKey.POSITION;
import static com.example.musicapp.common.MusicBundleKey.TYPE;
import static com.example.musicapp.common.MusicPlayerType.FAVORITES_SONG;
import static com.example.musicapp.common.MusicPlayerType.RECOMMEND_SONG;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.musicapp.R;
import com.example.musicapp.adapter.FavoriteSongAdapter;
import com.example.musicapp.common.InternetConnection;
import com.example.musicapp.databinding.FragmentFavoritesBinding;
import com.example.musicapp.models.Song;
import com.example.musicapp.viewmodels.FavoriteSongViewModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class FavoritesFragment extends Fragment implements FavoriteSongAdapter.FavoritesSongListener{

    FragmentFavoritesBinding binding;
    private FavoriteSongAdapter favoriteSongAdapter;
    private FavoriteSongViewModel favoriteSongViewModel;

    Gson gson = new Gson();
    Realm realm = Realm.getDefaultInstance();
    public static FavoritesFragment newInstance() {
        return new FavoritesFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        inflateBinding(inflater, container);
        initViewModel();
        initAdapter();
        observerDataInViewModel();
        setAdapterForRecycleView(); // Should be inside initAdapter

        return binding.getRoot();
    }

    private void inflateBinding(LayoutInflater inflater, ViewGroup container){
        binding = FragmentFavoritesBinding.inflate(inflater, container, false);
    }
    private void initViewModel() {
        favoriteSongViewModel = new ViewModelProvider(this).get(FavoriteSongViewModel.class);
    }
    private void initAdapter(){
        favoriteSongAdapter = new FavoriteSongAdapter(this);
    };
    private void observerDataInViewModel(){
        favoriteSongViewModel.getLiveRealmResults().observe(getViewLifecycleOwner(), songs -> {
            Log.e("TAG", "observerDataInViewModel: ");
            favoriteSongAdapter.submitList(songs);
        });
    }
    private void setAdapterForRecycleView(){
        binding.rcvFavoriteSong.setAdapter(favoriteSongAdapter);
    }
    @Override
    public void onClickItem(int position) {
        try {
            if(InternetConnection.isConnected()){
                Bundle bundle = new Bundle();

                bundle.putString(TYPE, FAVORITES_SONG);
                bundle.putInt(POSITION, position);

                Intent intent = new Intent(getContext(), MusicPlayerActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }else {
                Toast.makeText(getContext(), "No Internet", Toast.LENGTH_SHORT).show();
            }
        } catch (InterruptedException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}