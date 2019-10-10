package com.example.sleepee;

import android.app.Service;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;

public class RingtoneService extends Service {
    Ringtone r;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        r = RingtoneManager.getRingtone(getApplication(), notification);
        r.play();
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        if (r != null) {
            r.stop();
        }
    }
}
