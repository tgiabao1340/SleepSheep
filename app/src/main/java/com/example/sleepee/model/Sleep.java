package com.example.sleepee.model;

public class Sleep {
    private long startTime;
    private long maxTime;
    private long actualTime;
    private int cycle;

    public Sleep(long startTime, long maxTime, int cycle) {
        this.startTime = startTime;
        this.maxTime = maxTime;
        this.cycle = cycle;
    }

    public int getCycle() {
        return cycle;
    }

    public long getActualTime() {
        return actualTime;
    }

    public long getMaxTime() {
        return maxTime;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setActualTime(long actualTime) {
        this.actualTime = actualTime;
    }

    public void setCycle(int cycle) {
        this.cycle = cycle;
    }

    public void setMaxTime(long maxTime) {
        this.maxTime = maxTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }
}
