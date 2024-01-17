package com.example.musicapp.ui.player;


import static com.example.musicapp.common.AppConstants.MusicBundleKey.ACTION_MUSIC;
import static com.example.musicapp.common.AppConstants.MusicBundleKey.ACTION_SEND_DATA_TO_ACTIVITY;
import static com.example.musicapp.common.AppConstants.MusicBundleKey.DURATION;
import static com.example.musicapp.common.AppConstants.MusicBundleKey.IS_PLAYING;
import static com.example.musicapp.common.AppConstants.MusicBundleKey.POSITION;
import static com.example.musicapp.common.AppConstants.MusicBundleKey.SKIP_DURATION;
import static com.example.musicapp.common.AppConstants.MusicBundleKey.SONG_JSON;
import static com.example.musicapp.common.AppConstants.MusicBundleKey.TOTAL;
import static com.example.musicapp.common.AppConstants.MusicPlayerActions.ACTION_CHANGE_LOOPING;
import static com.example.musicapp.common.AppConstants.MusicPlayerActions.ACTION_CHANGE_SHUFFLE;
import static com.example.musicapp.common.AppConstants.MusicPlayerActions.ACTION_NEXT;
import static com.example.musicapp.common.AppConstants.MusicPlayerActions.ACTION_PLAY_OR_PAUSE;
import static com.example.musicapp.common.AppConstants.MusicPlayerActions.ACTION_PREVIOUS;
import static com.example.musicapp.common.AppConstants.MusicPlayerActions.ACTION_SKIP;
import static com.example.musicapp.common.AppConstants.MusicPlayerActions.ACTION_START;
import static com.example.musicapp.common.AppConstants.MusicPlayerActions.ACTION_STOP;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.widget.SeekBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.bumptech.glide.Glide;
import com.example.musicapp.R;
import com.example.musicapp.databinding.ActivityPlayMusicBinding;
import com.example.musicapp.models.Song;
import com.example.musicapp.services.MusicPlayerService;
import com.google.gson.Gson;

public class MusicPlayerActivity extends AppCompatActivity {

    ActivityPlayMusicBinding binding;
    Bundle songsBundle;
    Song song;
    int position = 0, total, action, currentDuration = 0, duration;
    boolean isPlaying;
    Handler handler = new Handler();
    Runnable seekBarRunnable;
    Gson gson = new Gson();

    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            if(bundle != null) {
                song = gson.fromJson(bundle.getString(SONG_JSON), Song.class);
                position = bundle.getInt(POSITION, -1);
                isPlaying = bundle.getBoolean(IS_PLAYING);
                total = bundle.getInt(TOTAL);
                action = bundle.getInt(ACTION_MUSIC);
                duration = bundle.getInt(DURATION);

                handleLayoutMusic(action);
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        inflateBinding();
        registerReceiverAction();
        sendInitDataToService();

        setEventClickNextMusic();
        setEventClickPreviousMusic();
        setEventClickPlayOrPauseMusic();

        setEventChangeProgressSeekbar();

    }

    private void inflateBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_play_music);
    }
    private void setEventChangeProgressSeekbar() {
        binding.seekBarSong.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser){
                    Intent intentService = new Intent(MusicPlayerActivity.this, MusicPlayerService.class);

                    currentDuration = progress;
                    Bundle bundle = new Bundle();
                    bundle.putInt(ACTION_MUSIC, ACTION_SKIP);
                    bundle.putInt(SKIP_DURATION, progress);
                    intentService.putExtras(bundle);
                    startService(intentService);
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }
    private void setEventClickNextMusic() {
        binding.iconNext.setOnClickListener(view -> {
            handleOnClickIcon(ACTION_NEXT);
        });
    }
    private void setEventClickPreviousMusic() {
        binding.iconPrevious.setOnClickListener(view -> {
            handleOnClickIcon(ACTION_PREVIOUS);
        });
    }
    private void setEventClickPlayOrPauseMusic() {
        binding.iconPlayPause.setOnClickListener(view -> {
            handleOnClickIcon(ACTION_PLAY_OR_PAUSE);
        });
    }
    private void sendInitDataToService() {
        Intent intent = new Intent(MusicPlayerActivity.this, MusicPlayerService.class);
        intent.putExtras(getIntent().getExtras());
        this.startService(intent);
    }
    private void registerReceiverAction() {
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, new IntentFilter(ACTION_SEND_DATA_TO_ACTIVITY));
    }

    private void handleOnClickIcon(int action){

        Intent intentService = new Intent(MusicPlayerActivity.this, MusicPlayerService.class);
        Bundle bundle = new Bundle();
        bundle.putInt(ACTION_MUSIC, action);
        intentService.putExtras(bundle);
        this.startService(intentService);
    }

    private void handleLayoutMusic(int action){
        switch (action){
            case ACTION_START:
                startMusic();
                break;
            case ACTION_PLAY_OR_PAUSE:
                playOrPause();
                break;
            case ACTION_NEXT:
                nextMusic();
                break;
            case ACTION_PREVIOUS:
                previousMusic();
                break;
            case ACTION_STOP:
                finish();
                break;
            case ACTION_CHANGE_SHUFFLE:
                break;
            case ACTION_CHANGE_LOOPING:
                break;
        }
    }

    private void startMusic() {
        if(isPlaying){
            binding.iconPlayPause.setImageResource(R.drawable.pause_24);
        }else {
            binding.iconPlayPause.setImageResource(R.drawable.play_arrow_24);
        }
        binding.total.setText(String.valueOf(total));
        binding.position.setText(String.valueOf(position + 1));
        binding.titleSong.setText(String.valueOf(song.getTitle()));
        binding.artistSong.setText(String.valueOf(song.getArtist().getName()));
        Glide.with(this)
                .asBitmap()
                .load(song.getArtist().getPicture())
                .into(binding.pictureSong);
        binding.seekBarSong.setMax(duration);
        binding.durationTotal.setText(formattedTime(duration));
        currentDuration = 0;
        binding.seekBarSong.setProgress(currentDuration);

        seekBarRunnable = new Runnable() {
            @Override
            public void run() {
                binding.seekBarSong.setProgress(currentDuration++);
                binding.durationPlayed.setText(formattedTime(currentDuration));

                if(currentDuration < duration){
                    handler.postDelayed(this, 1000);
                }else{
                    handler.removeCallbacks(this);
                }
            }
        };

        MusicPlayerActivity.this.runOnUiThread(seekBarRunnable);
    }

    private String formattedTime(int currentDuration) {
        String time = "";
        String seconds = String.valueOf(currentDuration % 60);
        String minutes = String.valueOf(currentDuration / 60);
        time = minutes + ":" + seconds;
        return time;
    }

    private void previousMusic() {
        handler.removeCallbacks(seekBarRunnable);
        startMusic();
    }

    private void nextMusic() {
        handler.removeCallbacks(seekBarRunnable);
        startMusic();
    }

    private void playOrPause() {
        if(isPlaying){
            binding.iconPlayPause.setImageResource(R.drawable.pause_24);
            handler.removeCallbacks(seekBarRunnable);
            MusicPlayerActivity.this.runOnUiThread(seekBarRunnable);
        }else {
            binding.iconPlayPause.setImageResource(R.drawable.play_arrow_24);
            handler.removeCallbacks(seekBarRunnable);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
        handler.removeCallbacks(seekBarRunnable);
    }
}
