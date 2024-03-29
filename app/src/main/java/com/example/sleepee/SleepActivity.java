package com.example.sleepee;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sleepee.model.Sleep;
import com.ncorti.slidetoact.SlideToActView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class SleepActivity extends AppCompatActivity {
    private final int TIME_OF_CYCLE = 90; // 90 min
    private final int MAX_CYCLE = 5;
    private final int MIN_CYCLE = 3;
    private final int TIME_TO_FALL_A_SLEEP = 14; // 14 min
    private long startTime, endTime;
    private TextView textView_time;
    private Sleep sleep;
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleep);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true);
            setTurnScreenOn(true);
        } else {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                    | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                    | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                    | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                    | WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON);
        }
        textView_time = findViewById(R.id.textView_time);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = getIntent();
        if (intent != null) if (intent.hasExtra("START_TIME") && intent.hasExtra("END_TIME")) {
            startTime = intent.getLongExtra("START_TIME", 0);
            endTime = intent.getLongExtra("END_TIME", 0);
            if ((new Date(startTime).compareTo(new Date(endTime))) > 0) {
                Calendar cal = convertCalendar(endTime);
                cal.add(Calendar.DATE, 1);
                endTime = cal.getTimeInMillis();
            }
            long totalTime = totalTime(startTime, endTime) - TIME_TO_FALL_A_SLEEP * 60 * 1000;
            int cycle = calculateCycle(totalTime);
            if (calculateCycle(totalTime) < MIN_CYCLE || cycle > MAX_CYCLE) {
                showDialog(listTimesAlarm(startTime), endTime);
            } else {
                Calendar timeforAlarm = convertCalendar(timeWake(startTime, cycle));
                setAlarm(timeforAlarm);
            }
        }
        SlideToActView slideToActStop = findViewById(R.id.slideActToStop);
        slideToActStop.setOnSlideCompleteListener(new SlideToActView.OnSlideCompleteListener() {
            @Override
            public void onSlideComplete(SlideToActView slideToActView) {
                SleepDatabaseHelper db = new SleepDatabaseHelper(SleepActivity.this);
                Calendar calendar = Calendar.getInstance();
                sleep = new Sleep(startTime, calendar.getTimeInMillis(), totalTime(startTime, calendar.getTimeInMillis()), calculateCycle(totalTime(startTime, calendar.getTimeInMillis())));
                db.addSleep(sleep);
                MainActivity.getInstance().selectTab(MainActivity.TAB_TODAY);
                cancelAlarm();
                finish();
            }
        });

    }

    public Calendar convertCalendar(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        return calendar;
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onBackPressed() {
        //Do nothing :)))

    }

    @SuppressLint("DefaultLocale")
    public void setAlarm(Calendar calendar) {
        //Calendar calTest = Calendar.getInstance();
        // calTest.add(Calendar.MINUTE, 1);
        Intent intentAlarm = new Intent(SleepActivity.this, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(SleepActivity.this, 0, intentAlarm, 0);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        textView_time.setText(String.format("Alarm : %02d:%02d %s", calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), (calendar.get(Calendar.AM_PM) == Calendar.AM) ? "AM" : "PM"));
    }

    private void cancelAlarm() {
        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }
        AlarmReceiver.stopRingtone();
    }

    public void showDialog(final List<Long> list, final Long endTime) {
        final Dialog dialog = new Dialog(SleepActivity.this, R.style.DialogCustomTheme);
        final List<String> listTime = new ArrayList<>();
        final Calendar calendar = Calendar.getInstance();
        for (int i = 0; i < list.size(); i++) {
            calendar.setTimeInMillis(list.get(i));
            listTime.add(String.format("%02d:%02d %s", calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), (calendar.get(Calendar.AM_PM) == Calendar.AM) ? "AM" : "PM"));
        }
        // dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.diaglog_sleep);
        Button btndialog = dialog.findViewById(R.id.btndialog);
        calendar.setTimeInMillis(endTime);
        btndialog.setText(String.format("%02d:%02d %s", calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), (calendar.get(Calendar.AM_PM) == Calendar.AM) ? "AM" : "PM"));
        btndialog.setOnClickListener(new View.OnClickListener() {
            //Chose ENDTIME
            @Override
            public void onClick(View v) {
                setAlarm(convertCalendar(endTime));
                dialog.dismiss();
            }
        });
        ListView listView = dialog.findViewById(R.id.listview);
        ArrayAdapter arrayAdapter = new ArrayAdapter(SleepActivity.this, R.layout.list_item, R.id.tv, listTime);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            //Chose SUGGESTTIME
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Calendar selectedTime = Calendar.getInstance();
                selectedTime.setTimeInMillis(list.get(position));
                setAlarm(selectedTime);
                dialog.dismiss();
            }
        });
        dialog.show();
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                finish();
            }
        });
    }

    public List<Long> listTimesAlarm(long startTime) {
        List<Long> list = new ArrayList<>();
        for (int i = MIN_CYCLE; i < MAX_CYCLE + 1; i++) {
            list.add(timeWake(startTime, i));
        }
        return list;
    }

    public long totalTime(long startTime, long endTime) {
        return endTime - startTime;
    }

    public long timeWake(long startTime, int cycle) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(startTime);
        calendar.add(Calendar.MINUTE, TIME_OF_CYCLE * cycle + TIME_TO_FALL_A_SLEEP);
        return calendar.getTime().getTime();
    }

    public int calculateCycle(long timeSleep) {
        long cycle = (timeSleep / 1000 / 60) / TIME_OF_CYCLE;
        return (int) cycle;
    }
}