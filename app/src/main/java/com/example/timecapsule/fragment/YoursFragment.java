package com.example.timecapsule.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.airbnb.lottie.L;
import com.bigkoo.pickerview.adapter.ArrayWheelAdapter;
import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.contrarywind.listener.OnItemSelectedListener;
import com.contrarywind.view.WheelView;
import com.example.timecapsule.R;
import com.example.timecapsule.adapter.ClassAdapter;
import com.example.timecapsule.db.Classroom;
import com.example.timecapsule.db.MyClassroom;
import com.example.timecapsule.db.User;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.BmobBatch;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BatchResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.util.V;

public class YoursFragment extends Fragment {


    private Button week;
    private String weekNumber = "Week 1";
    private RecyclerView recyclerView;
    private Context mContext;
    private ClassAdapter adapter;
    private Map<Integer, Boolean> map;
    private Boolean is_pop = false;
    private List<MyClassroom> classList = new ArrayList<>();
    private List<Classroom> textList = new ArrayList<>();
    //Used to maintain all timetables
    private int[][] ClassRoom =
            {{R.id.mon_1,R.id.mon_2,R.id.mon_3,R.id.mon_4,R.id.mon_5,R.id.mon_6,R.id.mon_7,R.id.mon_8,R.id.mon_9,R.id.mon_10,R.id.mon_11,R.id.mon_12},
                    {R.id.tue_1,R.id.tue_2,R.id.tue_3,R.id.tue_4,R.id.tue_5,R.id.tue_6,R.id.tue_7,R.id.tue_8,R.id.tue_9,R.id.tue_10,R.id.tue_11,R.id.tue_12},
                    {R.id.wed_1,R.id.wed_2,R.id.wed_3,R.id.wed_4,R.id.wed_5,R.id.wed_6,R.id.wed_7,R.id.wed_8,R.id.wed_9,R.id.wed_10,R.id.wed_11,R.id.wed_12},
                            {R.id.thur_1,R.id.thur_2,R.id.thur_3,R.id.thur_4,R.id.thur_5,R.id.thur_6,R.id.thur_7,R.id.thur_8,R.id.thur_9,R.id.thur_10,R.id.thur_11,R.id.thur_12},
                                    {R.id.fri_1,R.id.fri_2,R.id.fri_3,R.id.fri_4,R.id.fri_5,R.id.fri_6,R.id.fri_7,R.id.fri_8,R.id.fri_9,R.id.fri_10,R.id.fri_11,R.id.fri_12},
                                            {R.id.sat_1,R.id.sat_2,R.id.sat_3,R.id.sat_4,R.id.sat_5,R.id.sat_6,R.id.sat_7,R.id.sat_8,R.id.sat_9,R.id.sat_10,R.id.sat_11,R.id.sat_12},
                    {R.id.sun_1,R.id.sun_2,R.id.sun_3,R.id.sun_4,R.id.sun_5,R.id.sun_6,R.id.sun_7,R.id.sun_8,R.id.sun_9,R.id.sun_10,R.id.sun_11,R.id.sun_12}};

    private List<List<List<MyClassroom>>>  classroomData = new ArrayList<List<List<MyClassroom>>>();

    //REFERENCE: https://developer.android.google.cn/jetpack/androidx/releases/swiperefreshlayout
    private SwipeRefreshLayout swipe;
    private User currentUser;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_yours, container, false);
        currentUser = User.getCurrentUser(User.class);
        mContext = getActivity();


        //Init each ArrayList for each time item
        for (int i = 0; i < ClassRoom.length; i++){
            classroomData.add(new ArrayList<List<MyClassroom>>());
            for(int j = 0; j < ClassRoom[i].length; j++){
                classroomData.get(i).add(new ArrayList<MyClassroom>());
            }
        }

        BmobQuery<MyClassroom> query = new BmobQuery<>();
        query.addWhereEqualTo("owner", currentUser);
        query.order("-updatedAt");
        query.include("classroom.class_number, classroom.week_number, classroom.week, classroom.name");
        query.findObjects(new FindListener<MyClassroom>() {
            @Override
            public void done(List<MyClassroom> object, BmobException e) {
                if (e == null) {
                    classList.clear();
                    classList = object;
                    setData(root, classList);

                }
            }

        });


        //Change the color of the loading display
        swipe = (SwipeRefreshLayout) root.findViewById(R.id.swipe);
        swipe.setColorSchemeColors(ContextCompat.getColor(this.getContext(),R.color.orange),ContextCompat.getColor(this.getContext(),R.color.orange));
        //Set how many refreshes will appear when you pull down
        swipe.setDistanceToTriggerSync(200);
        //Set the position where the refresh appears
        swipe.setProgressViewEndTarget(false, 200);
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipe.setRefreshing(false);

                if(User.getCurrentUser(User.class)!=null){
                    getData(root);
                }else{
                    new AlertDialog.Builder(getContext()).setTitle("You should log in firstly")
                            .setNegativeButton("Yes", null)
                            .show();
                }

            }
        });

        setAllTextClick(root);
        //Initialize the contents of the week selector
        final List<String> week_number = new ArrayList<>();
        for (int i = 1; i < 25; i++){
            week_number.add("Week " + i);
        }

        week = root.findViewById(R.id.change);
        week.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                OptionsPickerView<String> build = new OptionsPickerBuilder(getContext(), new OnOptionsSelectListener() {
                    //Week number selector
                    @Override
                    public void onOptionsSelect(int options1, int options2, int options3, View v) {
                        String s=week_number.get(options1);
                        weekNumber = s;
                        week.setText("W" + weekNumber.substring(4, weekNumber.length()).toString());

                        setData(root, classList);
                        Snackbar.make(week,weekNumber , Snackbar.LENGTH_SHORT).show();

                    }
                }).build();
                build.setPicker(week_number);
                build.show();
            }

        });

        return root;
    }
    //Clear data
    private void clearData(View root){
        for (int i = 0; i < ClassRoom.length; i++){
            for (int j = 0; j < ClassRoom[i].length; j++){
                TextView textView = root.findViewById(ClassRoom[i][j]);
                textView.setText(" ");
                classroomData.get(i).get(j).clear();
            }
        }
    }
    //Set data
    private  void setData(View root, List<MyClassroom> textClassList){
        clearData(root);
        for (MyClassroom c : textClassList){
            String week_number = c.getClassroom().getWeek_number();
            int week = Integer.parseInt(c.getClassroom().getWeek()) - 1;
            int class_number = Integer.parseInt(c.getClassroom().getClass_number()) - 1;

            if (weekNumber.substring(5, weekNumber.length()).equals(week_number)) {
                classroomData.get(week).get(class_number).add(c);
                TextView textView = root.findViewById(ClassRoom[week][class_number]);
                String alreadyClass = textView.getText().toString();

                if (alreadyClass.replace(" ", "").equals("")) {
                    textView.setText(c.getClassroom().getName());
                } else {
                    textView.setText(alreadyClass + "\n" + c.getClassroom().getName());
                }
            }

        }
    }


    private void getData(View root) {

        Log.e(">>>>","begin to get data");
        BmobQuery<MyClassroom> query = new BmobQuery<>();
        query.addWhereEqualTo("owner", currentUser);
        query.order("-updatedAt");
        query.include("classroom.class_number, classroom.week_number, classroom.week, classroom.name");
        query.findObjects(new FindListener<MyClassroom>() {
            @Override
            public void done(List<MyClassroom> object, BmobException e) {
                if (e == null) {
                    classList.clear();
                    classList = object;
                    Snackbar.make(root, "Your have "+classList.size() +" data in total ", Snackbar.LENGTH_SHORT).show();

                    //There is a very, very big bug here! ! ! ! ! ! ! !
                    //Since getData is sending a request to the server, the data reading is very slow,
                    //at this time our setData has no effect. So setData only after the success of gateData
                    setData(root, classList);

                } else {
                    Log.e("BMOB", e.toString());
                    Snackbar.make(root, e.getMessage(), Snackbar.LENGTH_LONG).show();
                }
            }

        });

    }



    private void popText(View root, int id){
        TextView textView = root.findViewById(id);
        textView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(is_pop == false){
                    showPopwindow(root,id);
                }

            }
        });

    }

    private void setAllTextClick(View root){
        for (int i = 0; i < ClassRoom.length; i++){
            for(int j = 0; j <ClassRoom[i].length; j++){
                popText(root, ClassRoom[i][j]);
            }
        }
    }

    private void deleteAll(View root, List<MyClassroom> deleteAll){

            for(MyClassroom myClassroom : deleteAll){
                myClassroom.delete(new UpdateListener() {

                    @Override
                    public void done(BmobException e) {
                        if(e==null){

                        }else{
                            Snackbar.make(root, e.getMessage(), Snackbar.LENGTH_LONG).show();
                        }
                    }

                });
            }

    }

    //Show popupWindow
    private void showPopwindow(View root, int id) {
        //VERY IMPORTANT
        textList.clear();
        is_pop = true;
        // Use layoutInflater to get View
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.popwindowlayout2, null);

        //get the width and height getWindow().getDecorView().getWidth()
        PopupWindow window = new PopupWindow(view,
                600,
                600);
        // Set popWindow pop-up form to be clickable
        window.setFocusable(true);
        // Set popWindow display and disappear animation
        window.setAnimationStyle(R.style.mypopwindow_anim_style);
        // Show at the bottom
        window.showAtLocation(root.findViewById(id),
                Gravity.CENTER, 45, 200);



        int week = getIndex(id)[0];
        int class_number = getIndex(id)[1];
        int class_index = class_number +1;
        TextView classroomTag = (TextView) view.findViewById(R.id.classroomTag);
        classroomTag.setText(getWeekName(week)+"    Class "+class_index);

        for (MyClassroom myclassroom : classroomData.get(week).get(class_number)){
            Classroom classroom = new Classroom();
            classroom.setClass_number(myclassroom.getClassroom().getClass_number());
            classroom.setWeek_number(myclassroom.getClassroom().getWeek_number());
            classroom.setWeek(myclassroom.getClassroom().getWeek());
            classroom.setName(myclassroom.getClassroom().getName());
            textList.add(classroom);
        }
        recyclerView = (RecyclerView) view.findViewById(R.id.contentslist);

        displayList(textList);
        ImageButton delete = (ImageButton) view.findViewById(R.id.delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(User.getCurrentUser(User.class)!=null){
                    List<MyClassroom> deleteClassList = getClassroom(classroomData.get(week).get(class_number));
                    if(deleteClassList.size()!=0){
                        deleteAll(root,deleteClassList);
                        for(MyClassroom classroom: deleteClassList){
//                            Log.e("!@#!@#!@#!@#",classroom.getClassroom().getName());
                            classroomData.get(week).get(class_number).remove(classroom);
//                        root.findViewById(ClassRoom[week][class_number]);
                            classList.remove(classroom);
                            setData(root, classList);
                        }

//                    getData(root);
                        setData(root, classList);
                        window.dismiss();
                    }else {
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
        adapter = new ClassAdapter(mContext, List, R.layout.delete_item);
        recyclerView.setAdapter(adapter);
        adapter.setItemClickListener(new ClassAdapter.RecyclerViewOnItemClickListener(){
            @Override
            public void onItemClickListener(View view, int position) {
                adapter.setSelectItem(position);
            }
        });

    }

    private int[] getIndex(int id){
        int[] index = new int[2];
        for (int i = 0; i < ClassRoom.length; i++){
            for(int j =0; j < ClassRoom[i].length ;j++){
                if (ClassRoom[i][j] == id){
                    index[0] = i;
                    index[1] = j;
                    break;
                }
            }
        }
        return index;
    }

    private String getWeekName(int i){
        String WEEK = "";
        switch (i){
            case 0:
                WEEK = "MON";
                break;
            case 1:
                WEEK = "TUE";
                break;
            case 2:
                WEEK = "WED";
                break;
            case 3:
                WEEK = "THU";
                break;
            case 4:
                WEEK = "FRI";
                break;
            case 5:
                WEEK = "SAT";
                break;
            case 6:
                WEEK = "SUN";
                break;
        }
        return WEEK;
    }

    //Obtain user-selected data
    private  List<MyClassroom>  getClassroom(List<MyClassroom> List){
        List<MyClassroom> deleteClassList = new ArrayList<>();
        map = adapter.getMap();
        for (Map.Entry<Integer, Boolean> entry : map.entrySet()) {
            if(entry.getValue() == true){
                deleteClassList.add(List.get(entry.getKey()));
            }
        }
        return deleteClassList;
    }


}
