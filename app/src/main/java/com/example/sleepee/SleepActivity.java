package com.example.sleepee;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class SleepActivity extends AppCompatActivity {
    private TextView textView_time;
    private final int TIMEOFCYCLE = 90; // 90 min
    private int TIMETOFALLASLEEP = 14; // 14 min
    private long START_TIME,END_TIME,TOTAL_TIME;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleep);
        textView_time = findViewById(R.id.textView_time);
        Intent intent = getIntent();
        if(intent!=null){
            if(intent.hasExtra("START_TIME") && intent.hasExtra("END_TIME")){
                START_TIME = intent.getLongExtra("START_TIME",0);
                END_TIME = intent.getLongExtra("END_TIME",0);
                Calendar cal = Calendar.getInstance();
                if((new Date(START_TIME).compareTo(new Date(END_TIME)))>0){
                    cal.setTimeInMillis(END_TIME);
                    cal.add(Calendar.DATE,1);
                    END_TIME = cal.getTimeInMillis();
                }
                TOTAL_TIME = Math.abs(END_TIME - START_TIME - TIMETOFALLASLEEP*60*1000);
                Calendar calEndTime = Calendar.getInstance();
                Calendar calTimeMin = calEndTime.getInstance();
                Calendar calWakeTime = Calendar.getInstance();
                calEndTime.setTimeInMillis(END_TIME);
                calTimeMin.setTimeInMillis(END_TIME);
                calWakeTime.setTimeInMillis(timeWake(START_TIME,calculateCycle(TOTAL_TIME)));
                calTimeMin.add(Calendar.MINUTE,-30);
                DateFormat dateFormat = new SimpleDateFormat("HH:mm");
                textView_time.setText("Alarm : " + dateFormat.format(calTimeMin.getTime())+ " - "+ dateFormat.format(calEndTime.getTime()));
                //textView_time.setText((TOTAL_TIME/1000)/60 + "min| " + calculateCycle(TOTAL_TIME) +" | " + new Date(timeWake(START_TIME,calculateCycle(TOTAL_TIME))));
            }
        }
    }
    public long timeWake(long startTime,int cycle){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(startTime);
        calendar.add(Calendar.MINUTE,TIMEOFCYCLE*cycle);
        return calendar.getTime().getTime();
    }
    public int calculateCycle(long timeSleep){
        Long cycle = (timeSleep/1000/60)/TIMEOFCYCLE;
        return  cycle.intValue();
    }
}
