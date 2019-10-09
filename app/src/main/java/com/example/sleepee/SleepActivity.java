package com.example.sleepee;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sleepee.model.Sleep;
import com.ncorti.slidetoact.SlideToActView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class SleepActivity extends AppCompatActivity {
    private TextView textView_time;
    private SlideToActView slideToActStop;
    private final int TIMEOFCYCLE = 90; // 90 min
    private final int MAXCYCLE = 5;
    private final int MINCYCLE = 3;
    private int TIMETOFALLASLEEP = 14; // 14 min
    private long START_TIME,END_TIME,TOTAL_TIME;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleep);
        textView_time = findViewById(R.id.textView_time);
        Intent intent = getIntent();
        if(intent!=null) if (intent.hasExtra("START_TIME") && intent.hasExtra("END_TIME")) {
            START_TIME = intent.getLongExtra("START_TIME", 0);
            END_TIME = intent.getLongExtra("END_TIME", 0);
            Calendar cal = Calendar.getInstance();
            if ((new Date(START_TIME).compareTo(new Date(END_TIME))) > 0) {
                cal.setTimeInMillis(END_TIME);
                cal.add(Calendar.DATE, 1);
                END_TIME = cal.getTimeInMillis();
            }
            TOTAL_TIME = Math.abs(END_TIME - START_TIME - TIMETOFALLASLEEP * 60 * 1000);
            Calendar calStartTime = Calendar.getInstance();
            Calendar calEndTime = Calendar.getInstance();
            Calendar calTimeMin = Calendar.getInstance();
            Calendar calWakeTime = Calendar.getInstance();
            calStartTime.setTimeInMillis(START_TIME);
            calEndTime.setTimeInMillis(END_TIME);
            calTimeMin.setTimeInMillis(END_TIME);
            calWakeTime.setTimeInMillis(timeWake(START_TIME, calculateCycle(TOTAL_TIME)));
            calTimeMin.add(Calendar.MINUTE, -30);
            DateFormat dateFormat = new SimpleDateFormat("HH:mm");
            if (calculateCycle(TOTAL_TIME) < MINCYCLE || calculateCycle(TOTAL_TIME) > MAXCYCLE) {
                List<Long> list = new ArrayList<>();
                for (int i = MINCYCLE; i < MAXCYCLE + 1; i++) {
                    list.add(timeWake(START_TIME, i));
                }
                showDialog(list, END_TIME);
            } else {
                Calendar timeforAlarm = Calendar.getInstance();
                timeforAlarm.setTimeInMillis(timeWake(START_TIME, calculateCycle(TOTAL_TIME)));
                textView_time.setText(dateFormat.format(timeforAlarm.getTime()));
            }
        }
        slideToActStop = findViewById(R.id.slideActToStop);
        slideToActStop.setOnSlideCompleteListener(new SlideToActView.OnSlideCompleteListener() {
            @Override
            public void onSlideComplete(SlideToActView slideToActView) {
                Intent intent = new Intent(SleepActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    public void setAlarm(Calendar calendar){
        textView_time.setText(String.format("Alarm : %02d:%02d %s",calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),(calendar.get(Calendar.AM_PM)==0)?"AM":"PM"));
    }
    public void showDialog(final List<Long> list, final Long endTime){
        final Dialog dialog = new Dialog(SleepActivity.this,R.style.DialogCustomTheme);
        final List<String> listTime = new ArrayList<>();
        final Calendar calendar = Calendar.getInstance();
        for (int i = 0; i < list.size(); i ++){
            calendar.setTimeInMillis(list.get(i));
            listTime.add(String.format("%02d:%02d %s",calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),(calendar.get(Calendar.AM_PM)==0)?"AM":"PM"));
        }
        // dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.diaglog_sleep);
        Button btndialog = dialog.findViewById(R.id.btndialog);
        calendar.setTimeInMillis(endTime);
        btndialog.setText(String.format("ALarm : %02d:%02d %s",calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),(calendar.get(Calendar.AM_PM)==0)?"AM":"PM"));
        btndialog.setOnClickListener(new View.OnClickListener() {
            //Chose ENDTIME
            @Override
            public void onClick(View v) {
                Calendar selectedTime = Calendar.getInstance();
                selectedTime.setTimeInMillis(endTime);
                setAlarm(selectedTime);
                dialog.dismiss();
            }
        });
        ListView listView = dialog.findViewById(R.id.listview);
        ArrayAdapter arrayAdapter = new ArrayAdapter(this,R.layout.list_item, R.id.tv, listTime);
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
    }
    public long timeWake(long startTime,int cycle){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(startTime);
        calendar.add(Calendar.MINUTE,TIMEOFCYCLE*cycle + TIMETOFALLASLEEP);
        return calendar.getTime().getTime();
    }
    public int calculateCycle(long timeSleep){
        Long cycle = (timeSleep/1000/60)/TIMEOFCYCLE;
        return  cycle.intValue();
    }
}
