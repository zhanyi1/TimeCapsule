package com.example.timecapsule.db;

import cn.bmob.v3.BmobObject;

public class Event extends BmobObject {
    String type;
    String title;
    String details;
    String location;
    String date;
    long start;
    long end;
    long alert;
    int repeat;
    boolean is_all_day;
    private User owner;

    public void setType(String type) {
        this.type = type;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public void setEnd(long end) {
        this.end = end;
    }

    public void setAlert(long alert) {
        this.alert = alert;
    }

    public void setRepeat(int repeat) {
        this.repeat = repeat;
    }

    public void setIs_all_day(boolean is_all_day) {
        this.is_all_day = is_all_day;
    }

    public String getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public String getDetails() {
        return details;
    }

    public String getLocation() {
        return location;
    }

    public String getDate() {
        return date;
    }

    public long getStart() {
        return start;
    }

    public long getEnd() {
        return end;
    }

    public long getAlert() {
        return alert;
    }

    public int getRepeat() {
        return repeat;
    }

    public boolean isIs_all_day() {
        return is_all_day;
    }

    public Event setOwner(User owner){
        this.owner = owner;
        return this;

    }

    public User getOwner() {
        return owner;
    }
}
