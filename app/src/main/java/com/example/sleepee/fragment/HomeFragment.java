package com.example.sleepee.fragment;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;

import com.example.sleepee.R;
import com.example.sleepee.SleepActivity;

import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private NumberPicker hours_picker;
    private NumberPicker minutes_picker;
    private String[] hour_data = new String[]{"00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11","12","13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23"};
    private String[] minute_data = new String[]{"00", "10", "15", "20", "25", "30", "35", "40", "45", "50", "55"};
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
        hours_picker.setMaxValue(hour_data.length-1);
        hours_picker.setDisplayedValues(hour_data);
        minutes_picker.setMinValue(0);
        minutes_picker.setMaxValue(minute_data.length-1);
        minutes_picker.setDisplayedValues(minute_data);
        button_sleep = view.findViewById(R.id.button_sleep);
        button_sleep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SleepActivity.class);
                int hour = Integer.parseInt(hour_data[hours_picker.getValue()]);
                int minute = Integer.parseInt(minute_data[ minutes_picker.getValue()]);
                Calendar calendar_start = Calendar.getInstance();
                Calendar calendar_end = Calendar.getInstance();
                calendar_end.set(Calendar.HOUR,hour);
                calendar_end.set(Calendar.MINUTE,minute);
                if(calendar_start.compareTo(calendar_end)>0){
                    calendar_end.add(Calendar.DATE,1);
                }
                Long timeSleep = calendar_end.getTime().getTime() - calendar_start.getTime().getTime();
                intent.putExtra("timeSleep",timeSleep);
                startActivity(intent);
            }
        });
        return view;
    }

}
