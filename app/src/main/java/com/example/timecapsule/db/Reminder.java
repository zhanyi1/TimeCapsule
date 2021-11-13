package com.example.timecapsule.db;

import cn.bmob.v3.BmobObject;

public class Reminder extends BmobObject {
    int ID;
    long time;

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getID() {
        return ID;
    }

    public long getTime() {
        return time;
    }
}
