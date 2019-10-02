package com.example.sleepee;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class SleepActivity extends AppCompatActivity {
    private TextView textView_time;
    private final long cycleSleep = 5400000; // 90 minute

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleep);
        textView_time = findViewById(R.id.textView_time);
        Intent intent = getIntent();
        if(intent!=null){
            if(intent.hasExtra("timeSleep")){
                Long timeSleep = intent.getLongExtra("timeSleep",0);
                DateFormat dateFormat = new SimpleDateFormat("HH:mm");
                textView_time.setText(dateFormat.format(timeSleep));
            }
        }
    }
    public long timeWake(long currentTime, long timeSleep){
        Long time = 0l;
        return time;
    }
    public int calculateCycle(long timeSleep){
        Long cycle = (timeSleep/cycleSleep);
        return  cycle.intValue();
    }

}
