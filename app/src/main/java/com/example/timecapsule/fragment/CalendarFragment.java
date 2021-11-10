package com.example.timecapsule.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.MapLanguage;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnDismissListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.contrarywind.view.WheelView;
import com.example.timecapsule.R;
import com.example.timecapsule.view.MyMonthView;
import com.example.timecapsule.view.MyWeekView;
import com.example.timecapsule.view.ProgressMonthView;
import com.example.timecapsule.view.ProgressWeekView;
import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarLayout;
import com.haibin.calendarview.CalendarView;
import com.suke.widget.SwitchButton;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
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
    private Boolean is_pop = false;
    public Boolean is_start = false;
    public Boolean is_end = false;
    public Boolean is_alert = false;
    private BaiduMap mBaiduMap = null;
    private boolean isFirstLocate = true;
    private String myLocation;
    private LocationClient mLocationClient = null;
    private PoiSearch mPoiSearch = null;
    private SuggestionSearch mSuggestionSearch = null;
    private Marker mCurrentMarker;
    private BitmapDescriptor currentMarker = null;
    private List<PoiInfo> dataList;
    private ListAdapter adapter;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_calendar, container, false);

        mTextMonthDay = root.findViewById(R.id.tv_month_day);
        mTextYear = root.findViewById(R.id.tv_year);
        mRelativeTool = root.findViewById(R.id.rl_tool);
        mCalendarView = root.findViewById(R.id.calendarView);
        mTextCurrentDay = root.findViewById(R.id.tv_current_day);
        mCalendarLayout = root.findViewById(R.id.calendarLayout);
        change = root.findViewById(R.id.change);
        menu = root.findViewById(R.id.menu);
        context = getContext();
        View view = inflater.inflate(R.layout.popwindowlayout3, null);


        mCalendarView.setOnCalendarSelectListener(this);
        mCalendarView.setOnYearChangeListener(this);
        mTextYear.setText(String.valueOf(mCalendarView.getCurYear()));
        mYear = mCalendarView.getCurYear();
        mMonth = mCalendarView.getCurMonth();
        mDay = mCalendarView.getCurDay();
        mTextMonthDay.setText(mCalendarView.getCurMonth() + "月" + mCalendarView.getCurDay() + "日");

        mTextCurrentDay.setText(String.valueOf(mCalendarView.getCurDay()));
        font = Typeface.createFromAsset(getContext().getAssets(), "font.TTF");
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.capsule4);


        mCalendarView.setOnCalendarLongClickListener(new CalendarView.OnCalendarLongClickListener() {
            @Override
            public void onCalendarLongClickOutOfRange(Calendar calendar) {

            }

            @Override
            public void onCalendarLongClick(Calendar calendar) {
                showPopwindow(root);
            }

        });


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
                if (mCalendarLayout.isExpand()) {
                    mCalendarLayout.shrink();
                } else {
                    mCalendarLayout.expand();
                }

            }
        });

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(context, v);
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
        if (is_english == false) {
            mTextMonthDay.setText(calendar.getMonth() + "月" + calendar.getDay() + "日");
        } else {
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
                        if (is_english == false) {
                            is_english = true;
                            mTextMonthDay.setText(mMonth + "-" + mDay);
                            mCalendarView.scrollToCalendar(mYear, mMonth, mDay);
                        } else {
                            is_english = false;
                            mTextMonthDay.setText(mMonth + "月" + mDay + "日");
                            mCalendarView.scrollToCalendar(mYear, mMonth, mDay);
                        }
                        break;
                    case R.id.progress:
                        if (is_progress == false) {
                            mCalendarView.setMonthView(ProgressMonthView.class);
                            mCalendarView.setWeekView(ProgressWeekView.class);
                            is_progress = true;
                        } else {
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

    @Override
    public void onCalendarOutOfRange(Calendar calendar) {

    }

    //Show popupWindow
    private void showPopwindow(View root) {

        is_pop = true;
        // Use layoutInflater to get View
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.popwindowlayout3, null);

        //get the width and height getWindow().getDecorView().getWidth()
        PopupWindow window = new PopupWindow(view,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);

        // Set popWindow pop-up form to be clickable
        window.setFocusable(true);
        // Set popWindow display and disappear animation
        window.setAnimationStyle(R.style.mypopwindow_anim_style);
        // Show at the bottom
        window.showAtLocation(root.findViewById(R.id.change),
                Gravity.BOTTOM, 0, 0);


        ImageButton cancel = (ImageButton) view.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
            }
        });


        setPopwindow(view, window, root);

    }

    private void setPopwindow(View view, PopupWindow window, View root ) {

        ImageButton type = (ImageButton) view.findViewById(R.id.type);
        ImageButton location = (ImageButton) view.findViewById(R.id.location);
        ImageButton start = (ImageButton) view.findViewById(R.id.start);
        ImageButton end = (ImageButton) view.findViewById(R.id.end);
        ImageButton alert = (ImageButton) view.findViewById(R.id.alert);
        SwitchButton switch_button = (SwitchButton) view.findViewById(R.id.switch_button);

        LinearLayout capsule = (LinearLayout) view.findViewById(R.id.capsule);
        CardView start_1 = (CardView) view.findViewById(R.id.start_1);
        CardView end_1 = (CardView) view.findViewById(R.id.end_1);
        MapView mMapView = view.findViewById(R.id.bmapView);
        ScrollView parentScrollView = (ScrollView) view.findViewById(R.id.parentScroll);

        TextView capsule_t = (TextView) view.findViewById(R.id.type_t);
        TextView alert_t = (TextView) view.findViewById(R.id.alert_t);
        EditText title_t = (EditText) view.findViewById(R.id.title_t);
        EditText detail_t = (EditText) view.findViewById(R.id.details);
        EditText location_t = (EditText) view.findViewById(R.id.location_t);
        TextView date_t = (TextView) view.findViewById(R.id.date_t);
        TextView start_t = (TextView) view.findViewById(R.id.start_t);
        TextView end_t = (TextView) view.findViewById(R.id.end_t);
        RadioGroup nRg1 = (RadioGroup) view.findViewById(R.id.rg_1);
        ImageButton ok = (ImageButton) view.findViewById(R.id.ok);

        ImageButton clear_s = (ImageButton) view.findViewById(R.id.clear_s);
        ImageButton clear_e = (ImageButton) view.findViewById(R.id.clear_e);
        ImageButton clear_a = (ImageButton) view.findViewById(R.id.clear_a);

        initMap(root,mMapView);

        date_t.setText(mYear+"-"+mMonth+"-"+mDay);

        clear_a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert_t.setText("None");
            }
        });

        clear_s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start_t.setText("None");
            }
        });

        clear_e.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                end_t.setText("None");
            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(capsule_t.getText().toString().replace(" ","").equals("None")){
                    capsule_t.setError("Please Choose One");
                }else{
                    window.dismiss();
                }
            }
        });


        nRg1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton radioButton = radioGroup.findViewById(i);
                capsule_t.setText(radioButton.getText().toString());
            }
        });


        switch_button.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if(switch_button.isChecked()){
                    start_1.setVisibility(View.GONE);
                    end_1.setVisibility(View.GONE);
                }else{
                    start_1.setVisibility(View.VISIBLE);
                    end_1.setVisibility(View.VISIBLE);
                }
            }
        });



        mMapView.getChildAt(0).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP){
                    //允许ScrollView截断点击事件，ScrollView可滑动
                    parentScrollView.requestDisallowInterceptTouchEvent(false);
                }else{
                    //不允许ScrollView截断点击事件，点击事件由子View处理
                    parentScrollView.requestDisallowInterceptTouchEvent(true);
                }
                return false;
            }
        });

        type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (capsule.getVisibility() == View.GONE) {
                    capsule.setVisibility(View.VISIBLE);
                    type.setImageDrawable(getResources().getDrawable(R.drawable.to));
                } else {
                    capsule.setVisibility(View.GONE);
                    type.setImageDrawable(getResources().getDrawable(R.drawable.go));
                }
            }
        });

        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMapView.getVisibility() == View.GONE) {
                    mMapView.setVisibility(View.VISIBLE);
                    mMapView.onResume();
                    location_t.setText(myLocation);
                    location.setImageDrawable(getResources().getDrawable(R.drawable.to));
                } else {
                    mMapView.setVisibility(View.GONE);
                    mMapView.onPause();
                    location.setImageDrawable(getResources().getDrawable(R.drawable.go));
                }
            }
        });

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(is_start == false){
                    TimePickerView pvTime1 = new TimePickerBuilder(getActivity(), (date, view) -> {
                        SimpleDateFormat simpleDF = new SimpleDateFormat("HH:mm:ss");
                        String result = simpleDF.format(date);
                        start_t.setText(result);
                    }).setRangDate(null, java.util.Calendar.getInstance())
                            .setType(new boolean[]{false, false, false, true, true, true})
                            .isDialog(true)
                            .setLabel("","",""," h"," min"," s")
                            .isCyclic(true)
                            .build();
                    pvTime1.show();
                    is_start = true;
                    start.setImageDrawable(getResources().getDrawable(R.drawable.to));

                    pvTime1.setOnDismissListener(new OnDismissListener() {
                        @Override
                        public void onDismiss(Object o) {
                            is_start = false;
                            start.setImageDrawable(getResources().getDrawable(R.drawable.go));
                        }
                    });
                }


            }
        });

        alert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(is_alert == false){
                    TimePickerView pvTime1 = new TimePickerBuilder(getActivity(), (date, view) -> {
                        SimpleDateFormat simpleDF = new SimpleDateFormat("yy/MM/dd/ HH:mm:ss");
                        String result = simpleDF.format(date);
                        alert_t.setText(result);
                    }).setRangDate(java.util.Calendar.getInstance(), null)
                            .setType(new boolean[]{true, true, true, true, true, true})
                            .isDialog(true)
                            .setLabel("","",""," h"," m"," s")
                            .isCyclic(true)
                            .setSubmitColor(Color.parseColor("#FF9800"))
                            .setCancelColor(Color.parseColor("#FF9800"))
                            .build();
                    pvTime1.show();
                    alert.setImageDrawable(getResources().getDrawable(R.drawable.to));
                    is_alert = true;

                    pvTime1.setOnDismissListener(new OnDismissListener() {
                        @Override
                        public void onDismiss(Object o) {
                            is_alert = false;
                            alert.setImageDrawable(getResources().getDrawable(R.drawable.go));
                        }
                    });
                }

            }
        });




        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(is_end == false){
                    TimePickerView pvTime1 = new TimePickerBuilder(getActivity(), (date, view) -> {
                        SimpleDateFormat simpleDF = new SimpleDateFormat("HH:mm:ss");
                        String result = simpleDF.format(date);
                        end_t.setText(result);
                    }).setRangDate(java.util.Calendar.getInstance(), null)
                            .setType(new boolean[]{false, false, false, true, true, true})
                            .isDialog(true)
                            .isCyclic(true)
                            .setLabel("/","/","/"," h"," min"," s")
                            .build();
                    pvTime1.show();
                    end.setImageDrawable(getResources().getDrawable(R.drawable.to));
                    is_end = true;

                    pvTime1.setOnDismissListener(new OnDismissListener() {
                        @Override
                        public void onDismiss(Object o) {
                            is_end = false;
                            end.setImageDrawable(getResources().getDrawable(R.drawable.go));
                        }
                    });
                }

            }
        });

        //popupWindow disappearance monitoring method
        window.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                is_pop = false;
                mMapView.onDestroy();
            }
        });

    }

    //REFERENCE: https://lbsyun.baidu.com/index.php?title=首页 BAIDU SDK
    private void initMap(View root, MapView mMapView) {


        // get Map
        mBaiduMap = mMapView.getMap();
        // Turn on the positioning layer
        mBaiduMap.setMyLocationEnabled(true);
        //Baidu Map has not yet implemented the language switching function
        mBaiduMap.setMapLanguage(MapLanguage.valueOf("ENGLISH"));
        mLocationClient = new LocationClient(getActivity());
        //Set LocationClient related parameters through LocationClientOption
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // open gps
        option.setCoorType("bd09ll"); // Set the coordinate type
        option.setScanSpan(1000);
        // Optional, set address information
        option.setIsNeedAddress(true);
        //Optional, set whether address description is required
        option.setIsNeedLocationDescribe(true);
        //Set locationClientOption
        mLocationClient.setLocOption(option);
        //Register LocationListener listener
        MyLocationListener myLocationListener = new MyLocationListener();
        mLocationClient.registerLocationListener(myLocationListener);
        //Turn on the map positioning layer
        mLocationClient.start();


    }


    // Inherit the abstract class BDAbstractListener and rewrite its onReceieveLocation method to obtain positioning data and pass it to MapView
    public class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            //mapView is not processing the newly received position after being destroyed
//            if (location == null || mMapView == null) {
//                return;
//            }

            // If it is the first time positioning
            LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
            if (isFirstLocate) {
                isFirstLocate = false;
                //Set the state of the map
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newLatLng(ll));
            }

            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // Set the direction information obtained by the developer here, clockwise 0-360
                    .direction(location.getDirection()).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);


            // Display current information
            StringBuilder stringBuilder = new StringBuilder();
//            stringBuilder.append(location.getLatitude());
//            stringBuilder.append(location.getLongitude());
//            stringBuilder.append(location.getLocType());
//            stringBuilder.append(location.getCountry());
//            stringBuilder.append(location.getCity());
//            stringBuilder.append(location.getDistrict());
//            stringBuilder.append(location.getStreet());
            stringBuilder.append(location.getAddrStr());
            myLocation = stringBuilder.toString();
        }

    }



}