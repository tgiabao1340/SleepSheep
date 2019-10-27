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
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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
        ArrayList<Sleep> list = new ArrayList<>(db.getAllSleeps());
        SleepListAdapter adapter = new SleepListAdapter(getActivity(), R.layout.sleep_list_view, list);
        listView.setAdapter(adapter);
        lineChart.setPinchZoom(false);
        LineDataSet lineDataSet = new LineDataSet(listDuration(), "Thời gian ngủ");
        LineData lineData = new LineData(lineDataSet);
        lineChart.setData(lineData);
        return view;
    }

    public ArrayList listDuration() {
        ArrayList<Entry> list = new ArrayList();
        List<Sleep> listSleep = db.getAllSleeps();
        for (Sleep sleep : listSleep) {
            double duration = sleep.getDuration() / 1000 / 60 / 60;
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(sleep.getStartTime());
            Entry entry = new Entry(cal.get(Calendar.DATE), (float) duration);
            list.add(entry);
        }
        return list;
    }
}
