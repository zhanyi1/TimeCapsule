package com.example.timecapsule.activity;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;

import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.example.timecapsule.R;
import com.example.timecapsule.db.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_search, R.id.navigation_calendar, R.id.navigation_notice)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(navView, navController);
        navView.setItemRippleColor(ColorStateList.valueOf(android.graphics.Color.parseColor("#FFFFFF")));
        navView.setBackgroundColor(android.graphics.Color.parseColor("#D0EDFA"));

        //Since 4.3.0, all interfaces of Baidu Map SDK support Baidu coordinates and National Bureau of Surveying and Measurement coordinates.
        //Include BD09LL and GCJ02 coordinates, the default is BD09LL coordinates.
        SDKInitializer.initialize(getApplicationContext());
        SDKInitializer.setCoordType(CoordType.BD09LL);

    }

}