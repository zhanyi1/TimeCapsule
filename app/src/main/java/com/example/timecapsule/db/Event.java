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
    private boolean is_all_day;
    private User owner;
    public boolean is_complete;
    String location2;
    String comTime;
    String notes;

    public void setLocation2(String location2) {
        this.location2 = location2;
    }

    public void setComTime(String comTime) {
        this.comTime = comTime;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getLocation2() {
        return location2;
    }

    public String getComTime() {
        return comTime;
    }

    public String getNotes() {
        return notes;
    }

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

    public boolean isIs_complete(){
        return  is_complete;
    }

    public void setIs_complete(boolean is_complete){
        this.is_complete = is_complete;
    }

    public Event setOwner(User owner){
        this.owner = owner;
        return this;

    }

    public User getOwner() {
        return owner;
    }

    public int getYear(){
        String[] dates = date.split("-");
        return Integer.parseInt(dates[0]);
    }

    public int getMonth(){
        String[] dates = date.split("-");
        return Integer.parseInt(dates[1]);
    }

    public int getDay(){
        String[] dates = date.split("-");
        return Integer.parseInt(dates[2]);
    }
}
