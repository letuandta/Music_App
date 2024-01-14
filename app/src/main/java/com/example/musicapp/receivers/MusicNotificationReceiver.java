package com.example.musicapp.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.musicapp.common.InternetConnection;
import com.example.musicapp.services.MusicPlayerService;

public class MusicNotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        if(bundle != null){
            int action = bundle.getInt("action_music", -1);
            if(action > 0){
                Intent intentService = new Intent(context, MusicPlayerService.class);
                intentService.putExtras(bundle);
                context.startService(intentService);
            }
        }
    }
}
