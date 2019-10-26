package com.example.sleepee;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;

;

public class AlarmReceiver extends BroadcastReceiver {
    private static Ringtone ringtone = null;

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent intentSleep = new Intent(context, SleepActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intentSleep.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        ringtone = RingtoneManager.getRingtone(context, notification);
        ringtone.play();
        context.startActivity(intentSleep);
    }

    public static void stopRingtone() {
        if (ringtone != null) {
            ringtone.stop();
        }
    }
}
