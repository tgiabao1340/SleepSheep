package com.example.sleepee.model;

import java.util.Date;

public class Sleep {
    private Date time_gotoBed;
    private Date time_wakeup;
    private int cycle;
    public Sleep(Date time_gotoBed){
        this.time_gotoBed = time_gotoBed;
    }

    public Date getTime_gotoBed() {
        return time_gotoBed;
    }

    public Date getTime_wakeup() {
        return time_wakeup;
    }

    public void setTime_gotoBed(Date time_gotoBed) {
        this.time_gotoBed = time_gotoBed;
    }

    public void setTime_wakeup(Date time_wakeup) {
        this.time_wakeup = time_wakeup;
    }

    public int getCycle() {
        return cycle;
    }

    public void setCycle(int cycle) {
        this.cycle = cycle;
    }
}
