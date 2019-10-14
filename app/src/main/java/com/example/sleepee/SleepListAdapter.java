package com.example.sleepee;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.sleepee.model.Sleep;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SleepListAdapter extends ArrayAdapter<Sleep> {
    private Context context;
    private int resource;
    private List<Sleep> arrSleep;

    public SleepListAdapter(Context context, int resource, ArrayList<Sleep> arrSleep) {
        super(context, resource, arrSleep);
        this.context = context;
        this.resource = resource;
        this.arrSleep = arrSleep;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.sleep_list_view, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.tv_StartTime = convertView.findViewById(R.id.tv_timeStart);
            viewHolder.tv_Duration = convertView.findViewById(R.id.tv_duration);
            viewHolder.tv_Cycle = convertView.findViewById(R.id.tv_cycle);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Sleep sleep = arrSleep.get(position);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(sleep.getStartTime());
        viewHolder.tv_StartTime.setText(String.format("%02d/%02d\nT: %02d:%02d", calendar.get(Calendar.DAY_OF_MONTH) + 1, calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE)));
        Calendar dur = Calendar.getInstance();
        dur.setTimeInMillis(sleep.getDuration());
        viewHolder.tv_Duration.setText(String.format("%02dh%02dm", dur.get(Calendar.HOUR_OF_DAY), dur.get(Calendar.MINUTE)));
        viewHolder.tv_Cycle.setText("" + sleep.getCycle());
        return convertView;
    }

    public class ViewHolder {
        TextView tv_StartTime, tv_Duration, tv_Cycle;
    }
}
