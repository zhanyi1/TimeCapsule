package com.example.timecapsule.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuPopupHelper;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;

import com.example.timecapsule.R;
import com.example.timecapsule.view.MyMonthView;
import com.example.timecapsule.view.MyWeekView;
import com.example.timecapsule.view.ProgressMonthView;
import com.example.timecapsule.view.ProgressWeekView;
import com.google.android.material.snackbar.Snackbar;
import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarLayout;
import com.haibin.calendarview.CalendarView;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;


public class CalendarFragment extends Fragment implements
        CalendarView.OnCalendarSelectListener,
        CalendarView.OnYearChangeListener {


    private TextView mTextMonthDay;
    private TextView mTextYear;
    private TextView mTextCurrentDay;
    private CalendarView mCalendarView;
    private RelativeLayout mRelativeTool;
    private CalendarLayout mCalendarLayout;
    private ImageButton change;
    private ImageButton menu;
    private int mYear;
    private int mMonth;
    private int mDay;
    private Context context;
    private boolean is_progress = false;
    public static boolean is_english = false;
    public static Typeface font;
    public static Bitmap bitmap;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_calendar, container, false);

        mTextMonthDay = root.findViewById(R.id.tv_month_day);
        mTextYear = root.findViewById(R.id.tv_year);
        mRelativeTool = root.findViewById(R.id.rl_tool);
        mCalendarView =  root.findViewById(R.id.calendarView);
        mTextCurrentDay =  root.findViewById(R.id.tv_current_day);
        mCalendarLayout = root.findViewById(R.id.calendarLayout);
        change = root.findViewById(R.id.change);
        menu = root.findViewById(R.id.menu);
        context = getContext();

        mCalendarView.setOnCalendarSelectListener(this);
        mCalendarView.setOnYearChangeListener(this);
        mTextYear.setText(String.valueOf(mCalendarView.getCurYear()));
        mYear = mCalendarView.getCurYear();
        mMonth = mCalendarView.getCurMonth();
        mDay = mCalendarView.getCurDay();
        mTextMonthDay.setText(mCalendarView.getCurMonth() + "月" + mCalendarView.getCurDay() + "日");

        mTextCurrentDay.setText(String.valueOf(mCalendarView.getCurDay()));
        font =  Typeface.createFromAsset(getContext().getAssets(), "font.TTF");
        bitmap  = BitmapFactory.decodeResource(getResources(),R.drawable.capsule4);


//        mCalendarLayout.setModeBothMonthWeekView();
        mTextYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCalendarView.showYearSelectLayout(mYear);
            }
        });

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mCalendarLayout.isExpand()){
                    mCalendarLayout.shrink();
                }else{
                    mCalendarLayout.expand();
                }

            }
        });

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(context,v);
            }
        });

        root.findViewById(R.id.fl_current).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCalendarView.scrollToCurrent();
            }
        });

        int year = mCalendarView.getCurYear();
        int month = mCalendarView.getCurMonth();

        Map<String, Calendar> map = new HashMap<>();
        map.put(getSchemeCalendar(year, month, 3, 0xFF40db25, "假").toString(),
                getSchemeCalendar(year, month, 3, 0xFF40db25, "假"));
        map.put(getSchemeCalendar(year, month, 6, 0xFFe69138, "事").toString(),
                getSchemeCalendar(year, month, 6, 0xFFe69138, "事"));
        map.put(getSchemeCalendar(year, month, 9, 0xFFdf1356, "议").toString(),
                getSchemeCalendar(year, month, 9, 0xFFdf1356, "议"));
        map.put(getSchemeCalendar(year, month, 13, 0xFFedc56d, "记").toString(),
                getSchemeCalendar(year, month, 13, 0xFFedc56d, "记"));
        map.put(getSchemeCalendar(year, month, 14, 0xFFedc56d, "记").toString(),
                getSchemeCalendar(year, month, 14, 0xFFedc56d, "记"));
        map.put(getSchemeCalendar(year, month, 15, 0xFFaacc44, "假").toString(),
                getSchemeCalendar(year, month, 15, 0xFFaacc44, "假"));
        map.put(getSchemeCalendar(year, month, 18, 0xFFbc13f0, "记").toString(),
                getSchemeCalendar(year, month, 18, 0xFFbc13f0, "记"));
        map.put(getSchemeCalendar(year, month, 25, 0xFF13acf0, "假").toString(),
                getSchemeCalendar(year, month, 25, 0xFF13acf0, "假"));
        map.put(getSchemeCalendar(year, month, 27, 0xFF13acf0, "多").toString(),
                getSchemeCalendar(year, month, 27, 0xFF13acf0, "多"));
        //此方法在巨大的数据量上不影响遍历性能，推荐使用
        mCalendarView.setSchemeDate(map);

        return root;
    }


    private Calendar getSchemeCalendar(int year, int month, int day, int color, String text) {
        Calendar calendar = new Calendar();
        calendar.setYear(year);
        calendar.setMonth(month);
        calendar.setDay(day);
        calendar.setSchemeColor(color);//如果单独标记颜色、则会使用这个颜色
        calendar.setScheme(text);
        calendar.addScheme(new Calendar.Scheme());
        calendar.addScheme(0xFF008800, "假");
        calendar.addScheme(0xFF008800, "节");
        return calendar;
    }



    @Override
    public void onCalendarSelect(Calendar calendar, boolean isClick) {

        mTextMonthDay.setVisibility(View.VISIBLE);
        if(is_english==false){
            mTextMonthDay.setText(calendar.getMonth() + "月" + calendar.getDay() + "日");
        }else{
            mTextMonthDay.setText(calendar.getMonth() + "-" + calendar.getDay());
        }
        mTextYear.setText(String.valueOf(calendar.getYear()));
        mYear = calendar.getYear();
        mMonth = calendar.getMonth();
        mDay = calendar.getDay();

    }

    @Override
    public void onYearChange(int year) {
        mTextYear.setText(String.valueOf(year));
    }

    @Override
    public void onCalendarOutOfRange(Calendar calendar) {

    }


    private void showPopupMenu(Context context, View ancher) {
        PopupMenu popupMenu = new PopupMenu(context, ancher);
        //引入菜单资源
        popupMenu.inflate(R.menu.header_menu_pop);
        //菜单项的监听
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.language:
                        if(is_english==false){
                            is_english = true;
                            mTextMonthDay.setText(mMonth + "-" + mDay);
                            mCalendarView.scrollToCalendar(mYear,mMonth,mDay);
                        }else{
                            is_english = false;
                            mTextMonthDay.setText(mMonth + "月" + mDay + "日");
                            mCalendarView.scrollToCalendar(mYear,mMonth,mDay);
                        }
                        break;
                    case R.id.progress:
                        if(is_progress==false){
                            mCalendarView.setMonthView(ProgressMonthView.class);
                            mCalendarView.setWeekView(ProgressWeekView.class);
                            is_progress = true;
                        }else {
                            mCalendarView.setMonthView(MyMonthView.class);
                            mCalendarView.setWeekView(MyWeekView.class);
                            is_progress = false;
                        }
                        break;
                }
                return true;
            }
        });

        //显示PopupMenu
        popupMenu.show();
    }

}