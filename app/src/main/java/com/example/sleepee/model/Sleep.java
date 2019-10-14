package com.example.sleepee.model;

import androidx.annotation.NonNull;

public class Sleep {
    private long startTime;
    private long endTime;
    private long duration;
    private int cycle;

    public Sleep(long startTime, int cycle) {
        this.startTime = startTime;
        this.cycle = cycle;
    }

    public Sleep(long startTime, long endTime, long duration, int cycle) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.duration = duration;
        this.cycle = cycle;
    }

    public int getCycle() {
        return cycle;
    }

    public long getDuration() {
        return duration;
    }

    public long getEndTime() {
        return endTime;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public void setCycle(int cycle) {
        this.cycle = cycle;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    @NonNull
    @Override
    public String toString() {
        return "startTime: " + startTime;
    }
}
