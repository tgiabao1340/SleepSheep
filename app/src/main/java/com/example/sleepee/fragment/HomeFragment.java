package com.example.sleepee.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;

import androidx.fragment.app.Fragment;

import com.example.sleepee.R;
import com.example.sleepee.SleepActivity;

import java.util.Calendar;

public class HomeFragment extends Fragment {
    private final String[] HOUR_DATA = new String[]{"00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23"};
    private final String[] MINUTE_DATA = new String[]{"00", "05", "10", "15", "20", "25", "30", "35", "40", "45", "50", "55"};
    private NumberPicker hours_picker;
    private NumberPicker minutes_picker;
    private Button button_sleep;

    public HomeFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        hours_picker = view.findViewById(R.id.hours_picker);
        minutes_picker = view.findViewById(R.id.minutes_picker);
        hours_picker.setMinValue(0);
        hours_picker.setMaxValue(HOUR_DATA.length - 1);
        hours_picker.setDisplayedValues(HOUR_DATA);
        minutes_picker.setMinValue(0);
        minutes_picker.setMaxValue(MINUTE_DATA.length - 1);
        minutes_picker.setDisplayedValues(MINUTE_DATA);
        button_sleep = view.findViewById(R.id.button_sleep);
        button_sleep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SleepActivity.class);
                int hour = Integer.parseInt(HOUR_DATA[hours_picker.getValue()]);
                int minute = Integer.parseInt(MINUTE_DATA[minutes_picker.getValue()]);
                Calendar startTime = Calendar.getInstance();
                Calendar endTime = Calendar.getInstance();
                endTime.set(Calendar.HOUR_OF_DAY, hour);
                endTime.set(Calendar.MINUTE, minute);
                endTime.set(Calendar.SECOND, 0);
                intent.putExtra("START_TIME", startTime.getTime().getTime());
                intent.putExtra("END_TIME", endTime.getTime().getTime());
                startActivity(intent);
            }
        });
        return view;
    }

}
