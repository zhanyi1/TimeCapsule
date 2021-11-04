package com.example.timecapsule.db;

import cn.bmob.v3.BmobObject;

public class Classroom extends BmobObject {

    String name;
    String week_number;
    String week;
    String class_number;

    public void setWeek_number(String week_number) {
        this.week_number = week_number;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public void setClass_number(String class_number) {
        this.class_number = class_number;
    }

    public String getWeek_number() {
        return week_number;
    }

    public String getWeek() {
        return week;
    }

    public String getClass_number() {
        return class_number;
    }


    public void setName(String name) {
        this.name = name;
    }


    public String getName() {
        return name;
    }
}
