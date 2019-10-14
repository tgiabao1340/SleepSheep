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
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ProfileFragment extends Fragment {


    private BarChart barChart;
    private final List<Sleep> listSleep = new ArrayList<>();
    private final int MAXCHART = 7;
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
        ArrayList<Sleep> list = new ArrayList<>(db.getAllSleeps());
        //list.add(new Sleep(10000000, 20000000, 10000000, 100));
        SleepListAdapter adapter = new SleepListAdapter(getActivity(), R.layout.sleep_list_view, list);
        listView.setAdapter(adapter);
//        BarChart chart = view.findViewById(R.id.barchart);
//        ArrayList NoOfEmp = new ArrayList();
//        NoOfEmp.add(new BarEntry(945f, 0));
//        ArrayList year = new ArrayList();
//        year.add("2008");
//        year.add("2009");
//        year.add("2010");
//        year.add("2011");
//        year.add("2012");
//        year.add("2013");
//        year.add("2014");
//        year.add("2015");
//        year.add("2016");
//        year.add("2017");
//
//        BarDataSet bardataset = new BarDataSet(listDuration(), "No Of Employee");
//        chart.animateY(5000);
//        BarData data = new BarData(year, bardataset);
//        chart.setData(data);

        return view;
    }

    public ArrayList listDuration() {
        ArrayList list = new ArrayList();
        int no = 0;
        List<Sleep> listSleep = db.getAllSleeps();
        for (Sleep sleep : listSleep) {
            double duration = sleep.getDuration() / 1000 / 60 / 60;
            BarEntry barEntry = new BarEntry((float) duration, no++);
            list.add(barEntry);
        }
        return list;
    }

    public ArrayList listStartTime(int number) {
        ArrayList list = new ArrayList<>();
        List<Sleep> listSleep = db.getSleepNearBy(number);
        for (Sleep sleep : listSleep) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(sleep.getStartTime());
            list.add(calendar.get(Calendar.DAY_OF_MONTH) + "/" + calendar.get(Calendar.MONTH) + "T:" + calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE));
        }
        return list;
    }
}
