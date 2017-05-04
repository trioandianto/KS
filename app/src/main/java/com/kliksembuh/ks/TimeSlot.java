package com.kliksembuh.ks;

/**
 * Created by Ucu Nurul Ulum on 17/02/2017.
 */

public class TimeSlot {
    private int time_id;
    private String time_slot;

    // Constructor

    public TimeSlot(int time_id, String time_slot) {
        this.time_id = time_id;
        this.time_slot = time_slot;
    }

    // Getter & Setter

    public int getTime_id() {
        return time_id;
    }

    public void setTime_id(int time_id) {
        this.time_id = time_id;
    }

    public String getTime_slot() {
        return time_slot;
    }

    public void setTime_slot(String time_slot) {
        this.time_slot = time_slot;
    }

}
