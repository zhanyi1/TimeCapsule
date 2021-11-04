package com.example.timecapsule.db;

import cn.bmob.v3.BmobObject;

public class MyClassroom extends BmobObject {

    private User owner;
    private Classroom classroom;

    public void setClassroom(Classroom classroom) {
        this.classroom = classroom;
    }

    public Classroom getClassroom() {
        return classroom;
    }

    public MyClassroom setOwner(User owner){
        this.owner = owner;
        return this;

    }

    public User getOwner() {
        return owner;
    }

}
