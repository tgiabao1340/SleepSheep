package com.example.sleepee.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.example.sleepee.R;
import com.example.sleepee.SleepDatabaseHelper;
import com.example.sleepee.SleepListAdapter;
import com.example.sleepee.model.Sleep;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ProfileFragment extends Fragment {
    private SleepDatabaseHelper db;

    public ProfileFragment() {
        // Required empty public constructor


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        db = new SleepDatabaseHelper(getContext());
        db.createDefault();
        ListView listView = view.findViewById(R.id.lvSleep);
        LineChart lineChart = view.findViewById(R.id.lineChartSleep);
        ArrayList<Sleep> list = new ArrayList<>(db.getAllSleepsDesc());
        SleepListAdapter adapter = new SleepListAdapter(getActivity(), R.layout.sleep_list_view, list);
        listView.setAdapter(adapter);
        lineChart.setPinchZoom(false);
        lineChart.setDragEnabled(false);
        LineDataSet lineDataSet = new LineDataSet(listDuration(), "Thời gian ngủ");
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setLabelCount(4, true);
        xAxis.setGranularity(2f);
        ValueFormatter valueFormatter = new ValueFormatter() {
            @Override
            public String getAxisLabel(float value, AxisBase axis) {
                Date date = new Date((long) value);
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM", Locale.ENGLISH);
                return sdf.format(date);
            }
        };
        xAxis.setValueFormatter(valueFormatter);
        LineData lineData = new LineData(lineDataSet);
        lineChart.setData(lineData);
        lineChart.notifyDataSetChanged();
        lineChart.invalidate();
        return view;
    }

    public ArrayList listDuration() {
        ArrayList<Entry> list = new ArrayList();
        List<Sleep> listSleep = db.getAllSleeps();
        for (Sleep sleep : listSleep) {
            float duration = ((float) (sleep.getDuration())) / 1000 / 60 / 60;
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(sleep.getStartTime());
            Entry entry = new Entry(cal.getTimeInMillis(), duration);
            list.add(entry);
        }
        return list;
    }
}
