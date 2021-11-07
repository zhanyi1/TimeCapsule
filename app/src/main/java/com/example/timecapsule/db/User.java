package com.example.timecapsule.db;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobUser;

public class User extends BmobUser {

    private String name;
    private String phone;
    private String location;
    private String description;



    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setLocation(String location) {
        this.location = location;
    }


    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getLocation() {
        return location;
    }


    public String getDescription() {
        return description;
    }

}
