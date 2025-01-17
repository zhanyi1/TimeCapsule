package com.example.timecapsule.fragment;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.Projection;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnDismissListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.bumptech.glide.Glide;
import com.example.timecapsule.R;
import com.example.timecapsule.adapter.EventAdapter;
import com.example.timecapsule.adapter.PoiItemAdapter;
import com.example.timecapsule.db.Event;
import com.example.timecapsule.db.Reminder;
import com.example.timecapsule.db.User;
import com.example.timecapsule.utils.AlarmBroadcastReceiver;
import com.example.timecapsule.view.MyMonthView;
import com.example.timecapsule.view.MyWeekView;
import com.example.timecapsule.view.ProgressMonthView;
import com.example.timecapsule.view.ProgressWeekView;
import com.google.android.material.snackbar.Snackbar;
import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarLayout;
import com.haibin.calendarview.CalendarView;
import com.suke.widget.SwitchButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

@SuppressWarnings("all")
public class CalendarFragment extends Fragment implements
        CalendarView.OnCalendarSelectListener,
        CalendarView.OnYearChangeListener, BaiduMap.OnMapStatusChangeListener, PoiItemAdapter.MyOnItemClickListener
        , OnGetGeoCoderResultListener,
        EventAdapter.OnItemLongClickListener,
        EventAdapter.OnClickListener {


    private TextView mTextMonthDay;
    private TextView mTextYear;
    private TextView mTextCurrentDay;
    private TextView mEvent;
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
    private Boolean is_poevent1 = false;
    private Boolean is_initRe = false;
    public Boolean is_start = false;
    public Boolean is_end = false;
    public Boolean is_alert = false;
    public Boolean is_location = false;
    private BaiduMap mBaiduMap = null;
    private boolean isFirstLocate = true;
    private String myLocation = "";
    private LocationClient mLocationClient = null;
    private PoiSearch mPoiSearch = null;
    private SuggestionSearch mSuggestionSearch = null;
    private Marker mCurrentMarker;
    private BitmapDescriptor currentMarker = null;
    private List<PoiInfo> dataList;
    private ListAdapter adapter;
    private MyLocationConfiguration.LocationMode mCurrentMode;
    private LatLng locationLatLng;
    private String city;
    private GeoCoder geoCoder;

    private EditText location_t;
    private EditText location_t2;
    private EditText title_t;
    private EditText detail_t;
    private EditText repeat_t;

    long start_s;
    long end_s;
    long alert_s;
    public static List<Event> eventList = new ArrayList<>();
    private List<Event> day_eventList = new ArrayList<>();
    private EventAdapter event_adapter;
    private RecyclerView recyclerView;
    private ImageView image;
    private View root;
    private View view;
    private MapView mMapView;
    // Default inverse geocoding radius range
    private static final int sDefaultRGCRadius = 500;
    private LatLng mCenter;
    private Handler mHandler;
    private RecyclerView locRecyclerView;
    private PoiItemAdapter mPoiItemAdapter;
    private GeoCoder mGeoCoder = null;
    private boolean mStatusChangeByItemClick = false;
    private PopupWindow window;
    private TextView help;
    private Map<String, Calendar> mark_map = new HashMap<>();
    private Map<String, ArrayList<Event>> day_map = new HashMap<>();



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_calendar, container, false);

        mTextMonthDay = root.findViewById(R.id.tv_month_day);
        mTextYear = root.findViewById(R.id.tv_year);
        mRelativeTool = root.findViewById(R.id.rl_tool);
        mCalendarView = root.findViewById(R.id.calendarView);
        mTextCurrentDay = root.findViewById(R.id.tv_current_day);
        mCalendarLayout = root.findViewById(R.id.calendarLayout);
        change = root.findViewById(R.id.change);
        menu = root.findViewById(R.id.menu);
        context = getContext();
        mEvent = root.findViewById(R.id.new_event);


        recyclerView = (RecyclerView) root.findViewById(R.id.recycler_view);
        image = root.findViewById(R.id.image);
        help = root.findViewById(R.id.help);


        mCalendarView.setOnCalendarSelectListener(this);
        mCalendarView.setOnYearChangeListener(this);
        mTextYear.setText(String.valueOf(mCalendarView.getCurYear()));
        mYear = mCalendarView.getCurYear();
        mMonth = mCalendarView.getCurMonth();
        mDay = mCalendarView.getCurDay();


        getData(root);

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
                showPopwindow(root, null);
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

        return root;
    }


    private void setDayMap(){
        day_map.clear();
        mark_map.clear();
        for(Event event : eventList){
            if(!day_map.containsKey(event.getDate())){
                day_map.put(event.getDate(), new ArrayList<Event>());
                day_map.get(event.getDate()).add(event);
            }else{
                day_map.get(event.getDate()).add(event);
            }
        }
    }

    private void setCalendar(){
        setDayMap();
        for(Map.Entry<String, ArrayList<Event>> entry : day_map.entrySet()){
            Calendar calendar = new Calendar();
            ArrayList<Event> mapValue = entry.getValue();
            calendar.setYear(mapValue.get(0).getYear());
            calendar.setMonth(mapValue.get(0).getMonth());
            calendar.setDay(mapValue.get(0).getDay());
            for(Event event : mapValue){
                if(event.isIs_complete()){
                    calendar.addScheme( 0xFFaacc44, "E");
                }else{
                    if(event.getType().equals("Boss Capsule")){
                        calendar.addScheme(0xffed5353, "B");
                    }else{
                        calendar.addScheme(0xFF13acf0, "S");
                    }
                }
            }
            mark_map.put(calendar.toString(),calendar);

        }
        mCalendarView.setSchemeDate(mark_map);
    }


    @Override
    public void onCalendarSelect(Calendar calendar, boolean isClick) {

        mTextMonthDay.setVisibility(View.VISIBLE);
        if (is_english == false) {
            mTextMonthDay.setText(calendar.getMonth() + "月" + calendar.getDay() + "日");
            mEvent.setText("新事件");
            help.setText(getString(R.string.help));
        } else {
            mTextMonthDay.setText(calendar.getMonth() + "-" + calendar.getDay());
            mEvent.setText("New Event");
            help.setText(getString(R.string.help2));
        }
        mTextYear.setText(String.valueOf(calendar.getYear()));
        mYear = calendar.getYear();
        mMonth = calendar.getMonth();
        mDay = calendar.getDay();

        showDay();
        displayList(day_eventList);


    }


    @Override
    public void onYearChange(int year) {
        mTextYear.setText(String.valueOf(year));
    }


    private void showPopupMenu(Context context, View ancher) {
        PopupMenu popupMenu = new PopupMenu(context, ancher);
        //Introduce menu resources
        popupMenu.inflate(R.menu.header_menu_pop);
        //Monitor of menu items
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

        //Display PopupMenu
        popupMenu.show();
    }

    @Override
    public void onCalendarOutOfRange(Calendar calendar) {

    }

    //Show popupWindow
    private void showPopwindow(View root, Event event) {

        is_pop = true;
        is_initRe = false;
        try {

            LayoutInflater inflater1 = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater1.inflate(R.layout.popwindowlayout3, null);
            location_t = (EditText) view.findViewById(R.id.location_t);
            title_t = (EditText) view.findViewById(R.id.title_t);
            detail_t = (EditText) view.findViewById(R.id.details);
            repeat_t = (EditText) view.findViewById(R.id.repeat_t);
            window = new PopupWindow(view,
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.WRAP_CONTENT);
            mMapView = view.findViewById(R.id.bmapView);
            setPopwindow(view, event);
            initRecyclerView(view);
            initMap(view, mMapView);
            // Set popWindow pop-up form to be clickable
            window.setFocusable(true);
            // Set popWindow display and disappear animation
            window.setAnimationStyle(R.style.mypopwindow_anim_style);
            window.showAtLocation(root, Gravity.BOTTOM, 0, 0);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setPopwindow(View view, Event event1) {


        ImageButton type = view.findViewById(R.id.type);
        ImageButton location = view.findViewById(R.id.location);
        ImageButton start = view.findViewById(R.id.start);
        ImageButton end = view.findViewById(R.id.end);
        ImageButton alert =  view.findViewById(R.id.alert);
        SwitchButton switch_button =  view.findViewById(R.id.switch_button);

        LinearLayout capsule = view.findViewById(R.id.capsule);
        CardView start_1 = view.findViewById(R.id.start_1);
        CardView end_1 =  view.findViewById(R.id.end_1);
        ScrollView parentScrollView = view.findViewById(R.id.parentScroll);
        LinearLayout maplayout =  view.findViewById(R.id.maplayout);

        TextView capsule_t = view.findViewById(R.id.type_t);
        TextView alert_t = view.findViewById(R.id.alert_t);
        TextView date_t =  view.findViewById(R.id.date_t);
        TextView start_t = view.findViewById(R.id.start_t);
        TextView end_t = view.findViewById(R.id.end_t);
        RadioGroup nRg1 =  view.findViewById(R.id.rg_1);
        RadioButton rb1 = view.findViewById(R.id.rb_1);
        RadioButton rb2 = view.findViewById(R.id.rb_2);
        ImageButton ok =  view.findViewById(R.id.ok);

        ImageButton clear_s = view.findViewById(R.id.clear_s);
        ImageButton clear_e = view.findViewById(R.id.clear_e);
        ImageButton clear_a =  view.findViewById(R.id.clear_a);


        SimpleDateFormat simpleDF = new SimpleDateFormat("HH:mm:ss");
        if (event1 != null) {
            capsule_t.setText(event1.getType());
            if (event1.getType().equals("Boss Capsule")) {
                rb1.setChecked(true);
            } else {
                rb2.setChecked(true);
            }

            detail_t.setText(event1.getDetails());
            title_t.setText(event1.getTitle());
            location_t.setText(event1.getLocation());
            repeat_t.setText(event1.getRepeat() + "");

            if (event1.isIs_all_day()) {
                switch_button.setChecked(true);
                start_1.setVisibility(View.GONE);
                end_1.setVisibility(View.GONE);
            } else {
                if (event1.getStart() != 0) {
                    start_t.setText(simpleDF.format(event1.getStart()));
                }
                if (event1.getEnd() != 0) {
                    end_t.setText(simpleDF.format(event1.getEnd()));
                }
            }

            if (event1.getAlert() != 0) {
                alert_t.setText(simpleDF.format(event1.getAlert()));
            }

        } else {

            title_t.setText("");
            detail_t.setText("");
            repeat_t.setText("None");
            location_t.setText("");

            start_t.setText("None");
            end_t.setText("None");
            alert_t.setText("None");

        }

        date_t.setText(mYear + "-" + mMonth + "-" + mDay);


        ImageButton cancel = (ImageButton) view.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
            }
        });

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
                if (switch_button.isChecked()) {
                    start_1.setVisibility(View.GONE);
                    end_1.setVisibility(View.GONE);
                } else {
                    start_1.setVisibility(View.VISIBLE);
                    end_1.setVisibility(View.VISIBLE);
                }
            }
        });


        mMapView.getChildAt(0).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    //Allow ScrollView to cut off the click event, ScrollView can slide
                    parentScrollView.requestDisallowInterceptTouchEvent(false);
                } else {
                    // ScrollView is not allowed to cut off the click event, the click event is handled by the child View
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
                    type.setImageDrawable(getResources().getDrawable(R.drawable.to, null));
                } else {
                    capsule.setVisibility(View.GONE);
                    type.setImageDrawable(getResources().getDrawable(R.drawable.go,null));
                }
            }
        });


        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
                if (maplayout.getVisibility() == View.GONE) {
                    maplayout.setVisibility(View.VISIBLE);
                    is_location = true;
                    mMapView.onResume();
                    location_t.setText(myLocation);
                    location.setImageDrawable(getResources().getDrawable(R.drawable.to));
                } else {
                    maplayout.setVisibility(View.GONE);
                    is_location = false;
                    mMapView.onPause();
                    location.setImageDrawable(getResources().getDrawable(R.drawable.go));
                }
            }
        });


        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (is_start == false) {
                    TimePickerView pvTime1 = new TimePickerBuilder(getActivity(), (date, view) -> {
                        SimpleDateFormat simpleDF = new SimpleDateFormat("HH:mm:ss");
                        start_s = date.getTime();
                        String result = simpleDF.format(date);
                        start_t.setText(result);
                    }).setRangDate(null, java.util.Calendar.getInstance())
                            .setType(new boolean[]{false, false, false, true, true, true})
                            .isDialog(true)
                            .setLabel("", "", "", " h", " min", " s")
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

                if (is_alert == false) {
                    TimePickerView pvTime1 = new TimePickerBuilder(getActivity(), (date, view) -> {
                        SimpleDateFormat simpleDF = new SimpleDateFormat("yy/MM/dd/ HH:mm:ss");
                        String result = simpleDF.format(date);
                        alert_s = date.getTime();
                        alert_t.setText(result);
                    }).setRangDate(java.util.Calendar.getInstance(), null)
                            .setType(new boolean[]{true, true, true, true, true, true})
                            .isDialog(true)
                            .setLabel("", "", "", " h", " m", " s")
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

                if (is_end == false) {
                    TimePickerView pvTime1 = new TimePickerBuilder(getActivity(), (date, view) -> {
                        SimpleDateFormat simpleDF = new SimpleDateFormat("HH:mm:ss");
                        String result = simpleDF.format(date);
                        end_s = date.getTime();
                        end_t.setText(result);
                    }).setRangDate(java.util.Calendar.getInstance(), null)
                            .setType(new boolean[]{false, false, false, true, true, true})
                            .isDialog(true)
                            .isCyclic(true)
                            .setLabel("/", "/", "/", " h", " min", " s")
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

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (capsule_t.getText().toString().replace(" ", "").equals("None")) {
                    capsule_t.setError("Please Choose One");
                } else {

                    if (event1 != null) {
                        long alert1 = event1.getAlert();
                        int repeat1 = event1.getRepeat();

                        Event event2 = new Event();
                        event2.setObjectId(event1.getObjectId());
                        event2.delete(new UpdateListener() {

                            @Override
                            public void done(BmobException e) {
                                if (e == null) {
                                    for (int i = 0; i < repeat1; i++) {
                                        stopRemind((int) alert1 + i);
                                    }
                                } else {
                                    Snackbar.make(root, e.getMessage(), Snackbar.LENGTH_LONG).show();
                                }
                            }

                        });
                    }

                    String type_s;
                    String title_s;
                    String details_s;
                    String location_s;
                    String date_s;
                    int repeat_s = 0;
                    boolean is_all_day;
                    type_s = capsule_t.getText().toString();

                    title_s = title_t.getText().toString();
                    details_s = detail_t.getText().toString();
                    location_s = location_t.getText().toString();
                    date_s = date_t.getText().toString();


                    Event event = new Event();
                    event.setDetails(details_s);
                    event.setTitle(title_s);
                    event.setDate(date_s);
                    event.setIs_complete(false);

                    if (switch_button.isChecked()) {
                        is_all_day = true;
                        event.setEnd(0);
                        event.setStart(0);
                    } else {
                        is_all_day = false;

                        if (!end_t.getText().toString().equals("None")) {
                            event.setEnd(end_s);
                        } else {
                            event.setEnd(0);
                        }

                        if (!start_t.getText().toString().equals("None")) {
                            event.setStart(start_s);
                        } else {
                            event.setStart(0);
                        }
                    }

                    if (!alert_t.getText().toString().equals("None")) {
                        event.setAlert(alert_s);
                        if (repeat_t.getText().toString().equals("") || repeat_t.getText().toString().equals("0")) {
                            repeat_s = 1;
                        } else {
                            repeat_s = Integer.parseInt(repeat_t.getText().toString());
                        }
                    } else {
                        event.setAlert(0);
                        repeat_s = 0;
                    }


                    event.setRepeat(repeat_s);
                    event.setIs_all_day(is_all_day);
                    event.setType(type_s);
                    event.setLocation(location_s);


                    event.setOwner(BmobUser.getCurrentUser(User.class));
                    event.save(new SaveListener<String>() {
                        @Override
                        public void done(String objectId, BmobException e) {
                            if (e == null) {
                                day_eventList.remove(event1);
                                getData(root);
                                showDay();
                                displayList(day_eventList);
                                for (int i = 0; i < event.getRepeat(); i++) {
                                    if (event.getAlert() != 0) {
                                        Reminder reminder = new Reminder();
                                        reminder.setTime(alert_s);
                                        int id = (int) alert_s + i;
                                        reminder.setID(id);
                                        int finalI = i;
                                        reminder.save(new SaveListener<String>() {
                                            @Override
                                            public void done(String objectId, BmobException e) {
                                                if (e == null) {
                                                    setAlarm(alert_s + 1000 * 60 * finalI, title_s, (int) alert_s + finalI);
                                                } else {
                                                    Snackbar.make(root, e.getMessage(), Snackbar.LENGTH_SHORT).show();
                                                }
                                            }
                                        });

                                    }
                                }
                            } else {
                                Snackbar.make(root, e.getMessage(), Snackbar.LENGTH_LONG).show();
                                Log.e(">>>>", e.getMessage());
                            }
                        }
                    });

                    window.dismiss();
                }
            }
        });

        //popupWindow disappearance monitoring method
        window.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                is_pop = false;
            }
        });

    }


    //REFERENCE: https://lbsyun.baidu.com/index.php?title=首页 BAIDU SDK
    private void initMap(View root, MapView mMapView) {

        mHandler = new Handler();
        mBaiduMap = mMapView.getMap();
        if (null == mBaiduMap) {
            return;
        }

        // Set the initial center point to Beijing
        mCenter = new LatLng(39.963175, 116.400244);
        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newLatLngZoom(mCenter, 16);
        mBaiduMap.setMapStatus(mapStatusUpdate);
        mBaiduMap.setOnMapStatusChangeListener(this);
        mBaiduMap.setOnMapLoadedCallback(new BaiduMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                createCenterMarker();
                reverseRequest(mCenter);
            }
        });
    }

    /**
     * Create a map center point marker
     */
    private void createCenterMarker() {
        Projection projection = mBaiduMap.getProjection();
        if (null == projection) {
            return;
        }

        Point point = projection.toScreenLocation(mCenter);
        BitmapDescriptor bitmapDescriptor =
                BitmapDescriptorFactory.fromResource(R.drawable.icon_binding_point);
        if (null == bitmapDescriptor) {
            return;
        }

        MarkerOptions markerOptions = new MarkerOptions()
                .position(mCenter)
                .icon(bitmapDescriptor)
                .flat(false)
                .fixedScreenPosition(point);
        mBaiduMap.addOverlay(markerOptions);
        bitmapDescriptor.recycle();
    }

    /**
     * Initialize recyclerView
     */
    private void initRecyclerView(View view) {
        locRecyclerView = view.findViewById(R.id.recycler_view);
        if (null == locRecyclerView) {
            return;
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        locRecyclerView.setLayoutManager(layoutManager);
    }


    /**
     * Reverse geocoding request
     *
     * @param latLng
     */
    private void reverseRequest(LatLng latLng) {
        if (null == latLng) {
            return;
        }

        ReverseGeoCodeOption reverseGeoCodeOption = new ReverseGeoCodeOption().location(latLng)
                .newVersion(1) // 建议请求新版数据
                .radius(sDefaultRGCRadius);

        if (null == mGeoCoder) {
            mGeoCoder = GeoCoder.newInstance();
        }

        mGeoCoder.setOnGetGeoCodeResultListener(this);
        mGeoCoder.reverseGeoCode(reverseGeoCodeOption);
    }

    @Override
    public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {

    }

    @Override
    public void onGetReverseGeoCodeResult(final ReverseGeoCodeResult reverseGeoCodeResult) {
        if (null == reverseGeoCodeResult) {
            return;
        }

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (is_pop) {
                    updateUI(reverseGeoCodeResult, location_t);
                } else if (is_poevent1) {
                    updateUI(reverseGeoCodeResult, location_t2);
                }

            }
        });
    }

    /**
     * Update UI
     *
     * @param reverseGeoCodeResult
     */
    private void updateUI(ReverseGeoCodeResult reverseGeoCodeResult, EditText location) {
        List<PoiInfo> poiInfos = reverseGeoCodeResult.getPoiList();

        PoiInfo curAddressPoiInfo = new PoiInfo();
        curAddressPoiInfo.address = reverseGeoCodeResult.getAddress();
        curAddressPoiInfo.location = reverseGeoCodeResult.getLocation();

        if (null == poiInfos) {
            poiInfos = new ArrayList<>(2);
        }

        poiInfos.add(0, curAddressPoiInfo);

        if (curAddressPoiInfo.address != null) {
            myLocation = curAddressPoiInfo.address.toString();
            location.setText(myLocation);
        }


        if (!is_initRe) {
            Log.e("dsd", "=============================1");
            mPoiItemAdapter = new PoiItemAdapter(poiInfos, location);
            locRecyclerView.setAdapter(mPoiItemAdapter);
            mPoiItemAdapter.setOnItemClickListener(this);
            is_initRe = true;
        } else {
            Log.e("dsd", "=============================2");
            mPoiItemAdapter.updateData(poiInfos, location);
        }
    }

    @Override
    public void onMapStatusChangeStart(MapStatus mapStatus) {
    }

    @Override
    public void onMapStatusChangeStart(MapStatus mapStatus, int i) {
    }

    @Override
    public void onMapStatusChange(MapStatus mapStatus) {

    }

    public static boolean isLatlngEqual(LatLng latLng0, LatLng latLng1) {
        if (latLng0.latitude == latLng1.latitude
                && latLng0.longitude == latLng1.longitude) {
            return true;
        }

        return false;
    }

    @Override
    public void onMapStatusChangeFinish(MapStatus mapStatus) {
        LatLng newCenter = mapStatus.target;

        // If the map status is updated by clicking the poi item, you don’t need to do the reverse geography request later.
        if (mStatusChangeByItemClick) {
            if (!isLatlngEqual(mCenter, newCenter)) {
                mCenter = newCenter;
            }
            mStatusChangeByItemClick = false;
            return;
        }

        if (!isLatlngEqual(mCenter, newCenter)) {
            mCenter = newCenter;
            reverseRequest(mCenter);
        }
    }

    @Override
    public void onItemClick(int position, PoiInfo poiInfo) {
        if (null == poiInfo || null == poiInfo.getLocation()) {
            return;
        }

        mStatusChangeByItemClick = true;
        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newLatLng(poiInfo.getLocation());
        mBaiduMap.setMapStatus(mapStatusUpdate);
    }


    // Inherit the abstract class BDAbstractListener and rewrite its onReceieveLocation method to obtain positioning data and pass it to MapView
    public class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {

            // If it is the first time positioning
            LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
            if (isFirstLocate) {
                isFirstLocate = false;
                //Set the state of the map
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newLatLng(ll));
                MapStatusUpdate msu = MapStatusUpdateFactory.newLatLngZoom(ll, 18);
                mBaiduMap.animateMapStatus(msu);
            }

            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // Set the direction information obtained by the developer here, clockwise 0-360
                    .direction(location.getDirection()).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);


            // Display current information
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(location.getAddrStr());
            myLocation = stringBuilder.toString();

            //Get the coordinates, which will be used for the distance between the POI information point and the positioning
            locationLatLng = new LatLng(location.getLatitude(), location.getLongitude());
            //Get the city and use it in POISearch later
            city = location.getCity();
            //Create GeoCoder instance object
            geoCoder = GeoCoder.newInstance();
            //Initiate an anti-geocoding request (latitude and longitude -> address information)
            ReverseGeoCodeOption reverseGeoCodeOption = new ReverseGeoCodeOption();
            //Set the coordinates of the anti-geocoding location
            reverseGeoCodeOption.location(new LatLng(location.getLatitude(), location.getLongitude()));
            geoCoder.reverseGeoCode(reverseGeoCodeOption);

            //Set the query result listener
            geoCoder.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
                @Override
                public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {

                }

                @Override
                public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
                    List<PoiInfo> poiInfos = reverseGeoCodeResult.getPoiList();
//                    PoiAdapter poiAdapter = new PoiAdapter(MainActivity.this, poiInfos);
//                    poisLL.setAdapter(poiAdapter);
                }
            });
        }

    }

    //Reference: learn from https://blog.csdn.net/androidforwell/article/details/53696665?spm=1001.2101.3001.6650.13&utm_medium=distribute.pc_relevant.none-task-blog-2%7Edefault%7EOPENSEARCH%7Edefault-13.no_search_link&depth_1-utm_source=distribute.pc_relevant.none-task-blog-2%7Edefault%7EOPENSEARCH%7Edefault-13.no_search_link
    private void setAlarm(long time, String name, int id) {
        java.util.Calendar ctmp = java.util.Calendar.getInstance();
        ctmp.setTimeInMillis(time);

        Intent intent = new Intent(getContext(), AlarmBroadcastReceiver.class);
        intent.putExtra("name", name);
        intent.putExtra("id", id);
        intent.putExtra("time", new SimpleDateFormat("h:mm a").format(ctmp.getTime()));
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), id, intent, 0);

        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(android.content.Context.ALARM_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, time, pendingIntent);
        } else {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, time, pendingIntent);
        }
    }

    /**
     * Close reminder
     */
    private void stopRemind(int id) {
        Intent intent = new Intent(getContext(), AlarmBroadcastReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(getActivity(), id, intent, 0);
        AlarmManager am = (AlarmManager) getActivity().getSystemService(android.content.Context.ALARM_SERVICE);
        am.cancel(pi);
    }


    private void getData(View root) {

        Log.e(">>>>", "begin to get data");
        BmobQuery<Event> query = new BmobQuery<>();
        query.addWhereEqualTo("owner", User.getCurrentUser());
        query.order("-comTime");
        query.order("-start");
        query.findObjects(new FindListener<Event>() {
            @Override
            public void done(List<Event> object, BmobException e) {
                if (e == null) {
                    eventList.clear();
                    eventList = object;
                    setCalendar();
                    showDay();
                    displayList(day_eventList);


                } else {
                    Log.e("BMOB", e.toString());
                    Snackbar.make(root, e.getMessage(), Snackbar.LENGTH_LONG).show();
                }
            }

        });

    }

    //Used to display the data in the recycleview in popWindow
    private void displayList(List<Event> List) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        event_adapter = new EventAdapter(getContext(), List, eventList);
        event_adapter.setOnItemLongClickListener(this);
        event_adapter.setComClickListener(this);
        event_adapter.setDeleteClickListener(this);
        event_adapter.setEditClickListener(this);
        recyclerView.setAdapter(event_adapter);
    }


    private void showDay() {

        day_eventList.clear();
        for (Event event : eventList) {
            String[] dates = event.getDate().split("-");
            if (dates[0].equals(mYear + "") && dates[1].equals(mMonth + "") && dates[2].equals(mDay + "")) {
                day_eventList.add(event);
            }
        }
        if (day_eventList.size() == 0) {
            image.setVisibility(View.VISIBLE);
            help.setVisibility(View.VISIBLE);

        } else {
            image.setVisibility(View.GONE);
            help.setVisibility(View.GONE);
        }

        displayList(day_eventList);

    }


    private void showPopWindowCom(Event event) {
        String ObjectID = event.getObjectId();
        is_poevent1 = true;
        is_initRe = false;
        // Use layoutInflater to get View
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.popwindowlayout4, null);

        //get the width and height getWindow().getDecorView().getWidth()
        PopupWindow window = new PopupWindow(view,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        // Set popWindow pop-up form to be clickable
        window.setFocusable(true);
        // Set popWindow display and disappear animation
        window.setAnimationStyle(R.style.mypopwindow_anim_style);
        // Show at the bottom
        window.showAtLocation(root, Gravity.BOTTOM, 0, 0);

        location_t2 = (EditText) view.findViewById(R.id.location_t2);
        ImageButton location2 = (ImageButton) view.findViewById(R.id.location2);
        TextView time_t = (TextView) view.findViewById(R.id.time_t);
        ImageButton time = (ImageButton) view.findViewById(R.id.time);
        EditText note_t = (EditText) view.findViewById(R.id.notes);
        ImageButton clear = (ImageButton) view.findViewById(R.id.clear_a);
        ImageButton ok = (ImageButton) view.findViewById(R.id.ok);
        ImageButton cancel = (ImageButton) view.findViewById(R.id.cancel);
        ScrollView parentScrollView = (ScrollView) view.findViewById(R.id.parentScroll);
        LinearLayout maplayout = (LinearLayout) view.findViewById(R.id.maplayout);

        mMapView = view.findViewById(R.id.bmapView);
        initRecyclerView(view);
        initMap(view, mMapView);

        //popupWindow disappearance monitoring method
        window.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                is_poevent1 = false;
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String notes_s = note_t.getText().toString();
                String com_time = time_t.getText().toString();
                String location_s = location_t2.getText().toString();

                Event event1 = new Event();
                event1.setNotes(notes_s);
                event1.setComTime(com_time);
                event1.setLocation2(location_s);
                event1.setIs_complete(true);
                event1.update(ObjectID, new UpdateListener() {

                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            getData(root);
                            showDay();
                            displayList(day_eventList);
                            window.dismiss();
                        } else {
                        }
                    }

                });


            }
        });

        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (is_alert == false) {
                    TimePickerView pvTime1 = new TimePickerBuilder(getActivity(), (date, view) -> {
                        SimpleDateFormat simpleDF = new SimpleDateFormat("yy/MM/dd/ HH:mm:ss");
                        String result = simpleDF.format(date);
                        alert_s = date.getTime();
                        time_t.setText(result);
                    }).setRangDate(java.util.Calendar.getInstance(), null)
                            .setType(new boolean[]{true, true, true, true, true, true})
                            .isDialog(true)
                            .setLabel("", "", "", " h", " m", " s")
                            .isCyclic(true)
                            .setSubmitColor(Color.parseColor("#cddc39"))
                            .setCancelColor(Color.parseColor("#cddc39"))
                            .build();
                    pvTime1.show();
//                    Glide.with(context).load(R.drawable.to).into(time);
                    time.setImageDrawable(getResources().getDrawable(R.drawable.to));
                    is_alert = true;

                    pvTime1.setOnDismissListener(new OnDismissListener() {
                        @Override
                        public void onDismiss(Object o) {
                            is_alert = false;
//                            Glide.with(context).load(R.drawable.go).into(time);
                            time.setImageDrawable(getResources().getDrawable(R.drawable.go));
                        }
                    });
                }

            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                time_t.setText("None");
            }
        });

        mMapView.getChildAt(0).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    //允许ScrollView截断点击事件，ScrollView可滑动
                    parentScrollView.requestDisallowInterceptTouchEvent(false);
                } else {
                    //不允许ScrollView截断点击事件，点击事件由子View处理
                    parentScrollView.requestDisallowInterceptTouchEvent(true);
                }
                return false;
            }
        });

        location2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (maplayout.getVisibility() == View.GONE) {
                    maplayout.setVisibility(View.VISIBLE);
                    is_location = true;
                    mMapView.onResume();
                    location_t2.setText(myLocation);
                    location2.setImageDrawable(getResources().getDrawable(R.drawable.to));
                } else {
                    maplayout.setVisibility(View.GONE);
                    mMapView.onPause();
                    is_location = false;
                    location2.setImageDrawable(getResources().getDrawable(R.drawable.go));
                }
            }
        });


    }

    private void showPopWindowAll(Event event) {
        // Use layoutInflater to get View
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.popwindowlayout5, null);

        //get the width and height getWindow().getDecorView().getWidth()
        PopupWindow window = new PopupWindow(view,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        // Set popWindow pop-up form to be clickable
        window.setFocusable(true);
        // Set popWindow display and disappear animation
        window.setAnimationStyle(R.style.mypopwindow_anim_style);
        // Show at the bottom
        window.showAtLocation(root, Gravity.BOTTOM, 0, 0);

        TextView type_t = view.findViewById(R.id.type_t);
        TextView detail_t = view.findViewById(R.id.details);
        TextView title_t = view.findViewById(R.id.title_t);
        TextView location_t = view.findViewById(R.id.location_t);
        TextView date_t = view.findViewById(R.id.date_t);
        TextView start_t = view.findViewById(R.id.start_t);
        TextView end_t = view.findViewById(R.id.end_t);
        TextView alert_t = view.findViewById(R.id.alert_t);
        TextView repeat_t = view.findViewById(R.id.repeat_t);
        TextView location2_t = view.findViewById(R.id.location_t2);
        TextView time_t = view.findViewById(R.id.time_t);
        TextView note_t = view.findViewById(R.id.notes);
        ImageButton cancel = view.findViewById(R.id.cancel);

        SimpleDateFormat simpleDF = new SimpleDateFormat("yy/MM/dd/ HH:mm:ss");
        SimpleDateFormat simpleDF2 = new SimpleDateFormat("HH:mm:ss");

        type_t.setText(event.getType());
        detail_t.setText(event.getDetails());
        title_t.setText(event.getTitle());
        location_t.setText(event.getLocation());
        date_t.setText(event.getDate());

        repeat_t.setText(event.getRepeat() + "");
        location2_t.setText(event.getLocation2());
        time_t.setText(event.getComTime());
        note_t.setText(event.getNotes());

        if(event.getStart()!=0){
            start_t.setText(simpleDF2.format(event.getStart()));
        }else{
            start_t.setText("None");
        }

        if(event.getEnd()!=0){
            end_t.setText(simpleDF2.format(event.getEnd()));
        }else{
            end_t.setText("None");
        }

        if(event.getAlert()!=0){
            alert_t.setText(simpleDF.format(event.getAlert()));
        }else{
            alert_t.setText("None");
        }

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
            }
        });

    }


    @Override
    public void OnItemLongClick(View v, int position) {
        Event event = day_eventList.get(position);
        if (event.isIs_complete()) {
            showPopWindowAll(event);
        } else {
            new AlertDialog.Builder(getContext()).setTitle(" You have not completed it! \n Click Edit to see the details")
                    .setNegativeButton("ok", null)
                    .show();
        }
    }

    @Override
    public void OnClickEdit(View v, int position) {
        Event event = day_eventList.get(position);
        showPopwindow(root, event);


    }

    @Override
    public void OnClickCom(View v, int position) {
        Event event = day_eventList.get(position);
        if (event.isIs_complete()) {
            new AlertDialog.Builder(getContext()).setTitle(" You have complete it!\n Edit anything to change its state")
                    .setNegativeButton("ok", null)
                    .show();
        } else {
            showPopWindowCom(event);
        }


    }

    @Override
    public void OnClickDelete(View v, int position) {
        Event event = day_eventList.get(position);
        long alert_time = event.getAlert();
        int repeat = event.getRepeat();
        new AlertDialog.Builder(getContext()).setTitle("Are you sure to delete it?")
                .setNegativeButton("No", null)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        event.delete(new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if(e==null){
                                    getData(root);
                                    showDay();
                                    displayList(day_eventList);
                                    //cancel reminder
                                    for(int i = 0; i < event.getRepeat(); i++){
                                        int id = (int) alert_time+i;
                                        stopRemind(id);
                                    }
                                }
                            }

                        });
                    }
                })
                .show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mLocationClient != null) {
            mLocationClient.stop();
        }
        if (mMapView != null) {
            mMapView.onDestroy();
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        if (mMapView != null) {
            mMapView.onResume();
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        if (mMapView != null) {
            mMapView.onPause();
        }
    }
}