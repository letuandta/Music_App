package com.example.musicapp.views;


import static com.example.musicapp.services.MusicPlayerService.ACTION_CHANGE_LOOPING;
import static com.example.musicapp.services.MusicPlayerService.ACTION_CHANGE_SHUFFLE;
import static com.example.musicapp.services.MusicPlayerService.ACTION_NEXT;
import static com.example.musicapp.services.MusicPlayerService.ACTION_PLAY_OR_PAUSE;
import static com.example.musicapp.services.MusicPlayerService.ACTION_PREVIOUS;
import static com.example.musicapp.services.MusicPlayerService.ACTION_SKIP;
import static com.example.musicapp.services.MusicPlayerService.ACTION_START;
import static com.example.musicapp.services.MusicPlayerService.ACTION_STOP;

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
                song = gson.fromJson(bundle.getString("song_json"), Song.class);
                position = bundle.getInt("position", -1);
                isPlaying = bundle.getBoolean("is_playing");
                total = bundle.getInt("total");
                action = bundle.getInt("action_music");
                duration = bundle.getInt("duration");

                handleLayoutMusic(action);
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_play_music);

        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, new IntentFilter("action_send_data_to_activity"));

        Intent intent = new Intent(MusicPlayerActivity.this, MusicPlayerService.class);
        intent.putExtras(getIntent().getExtras());
        this.startService(intent);

        binding.iconNext.setOnClickListener(view -> {
            Intent intentService = new Intent(MusicPlayerActivity.this, MusicPlayerService.class);
            Bundle bundle = new Bundle();
            bundle.putInt("action_music", ACTION_NEXT);
            intentService.putExtras(bundle);
            this.startService(intentService);
        });

        binding.iconPrevious.setOnClickListener(view -> {
            Intent intentService = new Intent(MusicPlayerActivity.this, MusicPlayerService.class);
            Bundle bundle = new Bundle();
            bundle.putInt("action_music", ACTION_PREVIOUS);
            intentService.putExtras(bundle);
            this.startService(intentService);
        });

        binding.iconPlayPause.setOnClickListener(view -> {
            Intent intentService = new Intent(MusicPlayerActivity.this, MusicPlayerService.class);
            Bundle bundle = new Bundle();
            bundle.putInt("action_music", ACTION_PLAY_OR_PAUSE);
            intentService.putExtras(bundle);
            this.startService(intentService);
        });

        binding.seekBarSong.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                Intent intentService = new Intent(MusicPlayerActivity.this, MusicPlayerService.class);

                currentDuration = progress;
                Bundle bundle = new Bundle();
                bundle.putInt("action_music", ACTION_SKIP);
                bundle.putInt("skip_duration", progress);
                intentService.putExtras(bundle);
                startService(intentService);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

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
