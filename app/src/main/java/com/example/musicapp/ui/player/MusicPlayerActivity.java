package com.example.musicapp.ui.player;


import static com.example.musicapp.utils.AppConstants.MusicBundleKey.ACTION_MUSIC;
import static com.example.musicapp.utils.AppConstants.MusicBundleKey.SKIP_DURATION;
import static com.example.musicapp.utils.AppConstants.MusicPlayerActions.ACTION_CHANGE_LOOPING;
import static com.example.musicapp.utils.AppConstants.MusicPlayerActions.ACTION_CHANGE_SHUFFLE;
import static com.example.musicapp.utils.AppConstants.MusicPlayerActions.ACTION_NEXT;
import static com.example.musicapp.utils.AppConstants.MusicPlayerActions.ACTION_PLAY_OR_PAUSE;
import static com.example.musicapp.utils.AppConstants.MusicPlayerActions.ACTION_PREVIOUS;
import static com.example.musicapp.utils.AppConstants.MusicPlayerActions.ACTION_SKIP;
import static com.example.musicapp.utils.AppConstants.MusicPlayerActions.ACTION_START;
import static com.example.musicapp.utils.AppConstants.MusicPlayerActions.ACTION_STOP;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.SeekBar;

import androidx.annotation.Nullable;
import androidx.databinding.library.baseAdapters.BR;

import com.example.musicapp.R;
import com.example.musicapp.di.component.ActivityComponent;
import com.example.musicapp.ui.base.BaseActivity;
import com.example.musicapp.utils.TimeFormat;
import com.example.musicapp.databinding.ActivityPlayMusicBinding;
import com.example.musicapp.data.model.local.Song;
import com.example.musicapp.services.MusicPlayerService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Objects;

public class MusicPlayerActivity extends BaseActivity<ActivityPlayMusicBinding, MusicPlayerViewModel> {
    Song song = new Song();
    int position = 0, total, action, currentDuration = 0, duration;
    boolean isPlaying;
    Handler handler = new Handler();
    Runnable seekBarRunnable;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void receiveEventFromService(MusicPlayerService.SendToActivityEvent event){
        song = event.song;
        mViewModel.getMutableLiveData().setValue(song);
        mViewModel.checkFavorite();
        position = event.position;
        isPlaying = event.isPlaying;
        total = event.total;
        action = event.action;
        duration = event.duration;

        handleLayoutMusic(action);
    }


    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_play_music;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sendInitDataToService();

        setEventChangeProgressSeekbar();

    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void performDependencyInjection(ActivityComponent buildComponent) {
        buildComponent.inject(this);
    }

    private void setEventChangeProgressSeekbar() {
        mViewDataBinding.seekBarSong.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
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
    private void sendInitDataToService() {
        Intent intent = new Intent(MusicPlayerActivity.this, MusicPlayerService.class);
        intent.putExtras(Objects.requireNonNull(getIntent().getExtras()));
        this.startService(intent);
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
        }
    }

    private void startMusic() {
        if(isPlaying){
            mViewDataBinding.iconPlayPause.setImageResource(R.drawable.pause_24);
        }else {
            mViewDataBinding.iconPlayPause.setImageResource(R.drawable.play_arrow_24);
        }
        mViewDataBinding.total.setText(String.valueOf(total));
        mViewDataBinding.position.setText(String.valueOf(position + 1));
        mViewDataBinding.seekBarSong.setMax(duration);
        mViewDataBinding.durationTotal.setText(TimeFormat.formattedTime(duration));
        currentDuration = 0;
        mViewDataBinding.seekBarSong.setProgress(currentDuration);

        seekBarRunnable = new Runnable() {
            @Override
            public void run() {
                mViewDataBinding.seekBarSong.setProgress(currentDuration++);
                mViewDataBinding.durationPlayed.setText(TimeFormat.formattedTime(currentDuration));

                if(currentDuration < duration){
                    handler.postDelayed(this, 1000);
                }else{
                    handler.removeCallbacks(this);
                }
            }
        };

        MusicPlayerActivity.this.runOnUiThread(seekBarRunnable);
    }

    private void previousMusic() {
        handler.removeCallbacksAndMessages(null);
        startMusic();
    }

    private void nextMusic() {
        Log.e("TAG", "nextMusic: " );
        handler.removeCallbacksAndMessages(null);
        startMusic();
    }

    private void playOrPause() {
        if(isPlaying){
            mViewDataBinding.iconPlayPause.setImageResource(R.drawable.pause_24);
            handler.removeCallbacks(seekBarRunnable);
            MusicPlayerActivity.this.runOnUiThread(seekBarRunnable);
        }else {
            mViewDataBinding.iconPlayPause.setImageResource(R.drawable.play_arrow_24);
            handler.removeCallbacks(seekBarRunnable);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(seekBarRunnable);
    }
}
