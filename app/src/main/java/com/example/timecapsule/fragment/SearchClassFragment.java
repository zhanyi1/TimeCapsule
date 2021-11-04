package com.example.timecapsule.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapLanguage;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.example.timecapsule.R;
import com.example.timecapsule.adapter.ClassAdapter;
import com.example.timecapsule.db.Classroom;
import com.example.timecapsule.db.MyClassroom;
import com.example.timecapsule.db.User;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobBatch;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BatchResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.util.V;

import android.widget.TextView;


import java.util.Map;
import java.util.Random;


public class SearchClassFragment extends Fragment {

    private Button search;
    private Button clear1;
    private Button clear2;
    private Button clear3;
    private ImageButton locationB;

    //Whether the button was clicked
    private Boolean[] search_week_number = {false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false};
    private Boolean[] search_week = {false, false, false, false, false, false, false};
    private Boolean[] search_class = {false, false, false, false, false, false, false, false, false, false, false, false};
    //The id of the record button is used for initialization
    private int[] week_number = {R.id.week1, R.id.week2, R.id.week3, R.id.week4, R.id.week5, R.id.week6, R.id.week7, R.id.week8, R.id.week9, R.id.week10, R.id.week11, R.id.week12, R.id.week13, R.id.week14, R.id.week15, R.id.week16, R.id.week17, R.id.week18, R.id.week19, R.id.week20, R.id.week21, R.id.week22, R.id.week23, R.id.week24};
    private int[] week = {R.id.mon, R.id.tues, R.id.wen, R.id.thur, R.id.fri, R.id.sat, R.id.sun};
    private int[] class_number = {R.id.class1, R.id.class2, R.id.class3, R.id.class4, R.id.class5, R.id.class6, R.id.class7, R.id.class8, R.id.class9, R.id.class10, R.id.class11, R.id.class12};

    private Boolean weekNumber = false;
    private Boolean weekN = false;
    private Boolean classNumber = false;
    private MapView mMapView = null;
    private Boolean is_map = false;
    private BaiduMap mBaiduMap = null;
    private LocationClient mLocationClient = null;
    private boolean isFirstLocate = true;
    private MyLocationConfiguration.LocationMode locationMode;
    private EditText classroom;
    private String myLocation;
    private RecyclerView recyclerView;
    private Context mContext;
    private ClassAdapter adapter;
    private Map<Integer, Boolean> map;

    private List<Classroom> classList = new ArrayList<>();
    private List<Classroom> MyclassList = new ArrayList<>();
    private List<BmobObject> SaveClassrooms = new ArrayList<>();
    private User currentUser;
    private List<String> IDs = new ArrayList<>();
    private Boolean is_pop = false;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_search_class, container, false);

        search = (Button) root.findViewById(R.id.search);
        clear1 = (Button) root.findViewById(R.id.clear1);
        clear2 = (Button) root.findViewById(R.id.clear2);
        clear3 = (Button) root.findViewById(R.id.clear3);
        locationB = (ImageButton) root.findViewById(R.id.location);
        classroom = (EditText) root.findViewById(R.id.classroom);
        mMapView = (MapView) root.findViewById(R.id.bmapView);

        //Initialize the map
        initMap(root);
        mMapView.setVisibility(View.GONE);
        mContext = getActivity();
        currentUser = User.getCurrentUser(User.class);

        //Set click events for all buttons
        //Because there are too many operations, it is placed in a sub-thread
        new Thread(new Runnable() {
            @Override
            public void run() {
                clickAll(root);
            }
        }).start();

        //Only when the user selects all the buttons can the data be displayed
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(User.getCurrentUser(User.class)!=null){
                    CheckClick();
                    if (weekNumber == false) {
                        Snackbar.make(search, "You have not selected Week Number", Snackbar.LENGTH_LONG).show();
                    } else if (weekN == false) {
                        Snackbar.make(search, "You have not selected Week", Snackbar.LENGTH_LONG).show();
                    } else if (classNumber == false) {
                        Snackbar.make(search, "You have not selected Class Number", Snackbar.LENGTH_LONG).show();
                    } else {
                        if(is_pop == false){
                            showPopwindow(root);
                        }
                    }
                }else{
                    new AlertDialog.Builder(getContext()).setTitle("You should log in firstly")
                            .setNegativeButton("Yes", null)
                            .show();
                }





            }
        });

        clear1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for (int i = 0; i < search_week_number.length; i++) {
                    if (search_week_number[i] == true) {
                        search_week_number[i] = false;
                        root.findViewById(week_number[i]).setBackground(getResources().getDrawable(R.drawable.button2, null));
                    }
                }
            }
        });

        clear2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < search_week.length; i++) {
                    if (search_week[i] == true) {
                        search_week[i] = false;
                        root.findViewById(week[i]).setBackground(getResources().getDrawable(R.drawable.button2, null));
                    }
                }
            }
        });

        clear3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < search_class.length; i++) {
                    if (search_class[i] == true) {
                        search_class[i] = false;
                        root.findViewById(class_number[i]).setBackground(getResources().getDrawable(R.drawable.button2, null));
                    }
                }
            }
        });

        //Open and close the map
        locationB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (is_map == false) {
                    mMapView.setVisibility(View.VISIBLE);
                    mMapView.onResume();
                    is_map = true;
                    classroom.setText(myLocation);

                } else {
                    mMapView.setVisibility(View.GONE);
                    mMapView.onPause();
                    is_map = false;
                }

            }
        });


        return root;
    }

    //Set the click event for the Week button and record the click
    private void clickWeekButton(View root, int i) {
        Button button = root.findViewById(i);
        int index = Integer.parseInt(button.getTag().toString()) - 1;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (search_week[index] == false) {
                    button.setBackground(getResources().getDrawable(R.drawable.button3, null));
                    search_week[index] = true;


                } else {
                    button.setBackground(getResources().getDrawable(R.drawable.button2, null));
                    search_week[index] = false;

                }

            }
        });

    }

    //Set the click event for the Week Number button and record the click
    private void clickWeekNumberButton(View root, int i) {
        Button button = root.findViewById(i);
        int index = Integer.parseInt(button.getText().toString()) - 1;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (search_week_number[index] == false) {
                    button.setBackground(getResources().getDrawable(R.drawable.button6, null));
                    search_week_number[index] = true;


                } else {
                    button.setBackground(getResources().getDrawable(R.drawable.button2, null));
                    search_week_number[index] = false;

                }

            }
        });

    }

    //Set the click event for the Class Number button and record the click
    private void clickClassButton(View root, int i) {
        Button button = root.findViewById(i);
        int index = Integer.parseInt(button.getText().toString()) - 1;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (search_class[index] == false) {
                    button.setBackground(getResources().getDrawable(R.drawable.button5, null));
                    search_class[index] = true;


                } else {
                    button.setBackground(getResources().getDrawable(R.drawable.button2, null));
                    search_class[index] = false;

                }

            }
        });

    }

    private void clickAll(View root) {

        for (int i : week_number) {
            clickWeekNumberButton(root, i);
        }

        for (int i : week) {
            clickWeekButton(root, i);
        }
        for (int i : class_number) {
            clickClassButton(root, i);
        }


    }

    private List<List<Integer>> getCheckData(){
        List<List<Integer>> checkdata = new ArrayList<>();
        for(int i = 0; i < 3; i++){
            checkdata.add(new ArrayList<>());
        }
        for (int i = 1; i < search_week_number.length+1; i++) {
            if (search_week_number[i-1] == true) {
                checkdata.get(0).add(i);
            }
        }

        for (int i = 1; i < search_week.length +1; i++) {
            if (search_week[i-1] == true) {
                checkdata.get(1).add(i);
            }
        }

        for (int i = 1; i < search_class.length +1; i++) {
            if (search_class[i-1] == true) {
                checkdata.get(2).add(i);
            }
        }

        return  checkdata;
    }

    //Check whether all the buttons in the three parts of the button block are clicked
    private void CheckClick() {

        weekNumber = false;
        weekN = false;
        classNumber = false;

        for (boolean b : search_week_number) {

            if (b == true) {
                weekNumber = true;
                break;
            }

        }

        for (boolean b : search_week) {
            if (b == true) {
                weekN = true;
                break;
            }
        }

        for (boolean b : search_class) {
            if (b == true) {
                classNumber = true;
                break;
            }
        }

    }


    //REFERENCE: https://lbsyun.baidu.com/index.php?title=首页 BAIDU SDK
    private void initMap(View root) {

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
        SearchClassFragment.MyLocationListener myLocationListener = new SearchClassFragment.MyLocationListener();
        mLocationClient.registerLocationListener(myLocationListener);
        //Turn on the map positioning layer
        mLocationClient.start();


    }


    // Inherit the abstract class BDAbstractListener and rewrite its onReceieveLocation method to obtain positioning data and pass it to MapView
    public class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            //mapView is not processing the newly received position after being destroyed
            if (location == null || mMapView == null) {
                return;
            }

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


    //Show popupWindow
    private void showPopwindow(View root) {
        is_pop = true;
        IDs = ALLDownloadData(root);
        // Use layoutInflater to get View
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.popwindowlayout, null);

        //get the width and height getWindow().getDecorView().getWidth()
        PopupWindow window = new PopupWindow(view,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);

        // Set popWindow pop-up form to be clickable
        window.setFocusable(true);
        // Set popWindow display and disappear animation
        window.setAnimationStyle(R.style.mypopwindow_anim_style);
        // Show at the bottom
        window.showAtLocation(root.findViewById(R.id.search),
                Gravity.BOTTOM, 0, 0);
        TextView classroomTag = (TextView) view.findViewById(R.id.classroomTag);
        classroomTag.setText("Free Classrooms");

        Random random = new Random();
        int NUMBER = random.nextInt(3)+1;
        List<List<Integer>> clickdata = new ArrayList<>();
        clickdata = getCheckData();
        classList.clear();
        for (int i = 0; i < NUMBER; i++){
            for(int week_number : clickdata.get(0) ){
                for (int week : clickdata.get(1)){
                    for(int class_number : clickdata.get(2)){
                        Classroom c = new Classroom();
                        c.setWeek_number(""+week_number);
                        c.setWeek(""+week);
                        c.setClass_number(""+class_number);
                        classList.add(c);
                        int building = random.nextInt(4)+1;
                        int layer = random.nextInt(6)+1;
                        StringBuilder str = new StringBuilder();
                        for (int j = 0; j < 2; j++) {
                            str.append(random.nextInt(10));
                        }
                        String classroom = str.toString();
                        c.setName("T" + building +" "+ layer+""+classroom);
                        c.save(new SaveListener<String>() {
                            @Override
                            public void done(String objectId,BmobException e) {
                                if(e==null){
                                }else{
                                }
                            }
                        });

                    }
                }
            }


        }


        recyclerView = (RecyclerView) view.findViewById(R.id.contentslist);
        displayList(classList);
        ImageButton download = (ImageButton) view.findViewById(R.id.download);
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(currentUser != null){
                    getClassroom(classList);
                    if(MyclassList.size() != 0){
                        for (Classroom classroom : MyclassList) {
                            saveClassroom(classroom,root);
                        }
                        window.dismiss();

                    }else if(MyclassList.size() == 0){
                        new AlertDialog.Builder(getContext()).setTitle("Please choose the classroom")
                                .setNegativeButton("Yes", null)
                                .show();
                    }

                }else{
                    new AlertDialog.Builder(getContext()).setTitle("You should log in firstly")
                            .setNegativeButton("Yes", null)
                            .show();
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

    //Used to display the data in the recycleview in popWindow
    private void displayList(List<Classroom> List) {
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        adapter = new ClassAdapter(mContext, List, R.layout.class_item);
        recyclerView.setAdapter(adapter);
        adapter.setItemClickListener(new ClassAdapter.RecyclerViewOnItemClickListener(){
            @Override
            public void onItemClickListener(View view, int position) {
                adapter.setSelectItem(position);
            }
        });

    }
    //Obtain user-selected data
    private void  getClassroom(List<Classroom> List){
        MyclassList.clear();
        map = adapter.getMap();
        for (Map.Entry<Integer, Boolean> entry : map.entrySet()) {
            if(entry.getValue() == true){
                MyclassList.add(List.get(entry.getKey()));
            }
        }
    }

    private List<String> ALLDownloadData(View root){
        List<String> IDs = new ArrayList<>();
        if (BmobUser.isLogin()) {
            BmobQuery<MyClassroom> query = new BmobQuery<>();
            query.addWhereEqualTo("owner", BmobUser.getCurrentUser(User.class));
            query.findObjects(new FindListener<MyClassroom>() {
                @Override
                public void done(List<MyClassroom> object, BmobException e) {
                    if (e == null) {
                        for(MyClassroom classroom : object){
                            IDs.add(classroom.getClassroom().getObjectId());
                        }
                    } else {
                        Snackbar.make(root, e.getMessage(), Snackbar.LENGTH_LONG).show();
                    }
                }

            });
        }
//        else {
//            Snackbar.make(root, "Log in Firstly", Snackbar.LENGTH_LONG).show();
//        }


        return IDs;

    }



    private void saveClassroom(Classroom classroom, View root) {

        if (currentUser.isLogin()){
            String ClassroomID = classroom.getObjectId();
            if(!IDs.contains(ClassroomID)){
                MyClassroom myClassroom = new MyClassroom();
                myClassroom.setClassroom(classroom);
                myClassroom.setOwner(BmobUser.getCurrentUser(User.class));
                myClassroom.save(new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                        if (e == null) {
                            IDs.add(ClassroomID);
                        } else {
                            Log.e("BMOB", e.toString());
                            Snackbar.make(root, e.getMessage(), Snackbar.LENGTH_LONG).show();
                        }
                    }
                });
            }else{
                Snackbar.make(root, "You have downloaded it already", Snackbar.LENGTH_LONG).show();
            }

        }else {
            Snackbar.make(root, "Log in firstly", Snackbar.LENGTH_LONG).show();
        }
    }



}
