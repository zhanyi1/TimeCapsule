package com.example.timecapsule.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.baidu.mapapi.map.MapView;

public class MyViewPager extends ViewPager {


    public MyViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyViewPager(Context context) {
        super(context);
    }

    //Do not slide when setting the map
    @Override
    protected boolean canScroll(View v, boolean checkV, int dx, int x, int y) {
        if (v != this && (v instanceof MapView )) {
            requestDisallowInterceptTouchEvent(true);
            return true;
        }
        return super.canScroll(v, checkV, dx, x, y);
    }



}
