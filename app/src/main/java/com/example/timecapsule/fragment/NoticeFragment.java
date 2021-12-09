package com.example.timecapsule.fragment;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.timecapsule.R;
import com.example.timecapsule.db.Event;
import com.example.timecapsule.db.User;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;
import com.google.android.material.snackbar.Snackbar;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import android.app.DatePickerDialog;

public class NoticeFragment extends Fragment {

    private PieChart mChart;
    private List<Event> eventList = new ArrayList<>();
    private ImageButton calendar;
    private static Calendar cal = Calendar.getInstance();
    private int type = 2;
    private int[] Intdate = new int[3];
    private TextView textYear;
    private TextView textMD;
    private TextView textBoss;
    private TextView textEnergy;
    private TextView textSkill;
    private String[] months = {"Jan", "Feb", "Mar", "Apr","May", "Jun", "Jul", "Aug", "Sept","Oct","Nov","Dec"};
    private float[] capsules = new float[3];
    private TextView selYear;
    private TextView selMonth;
    private TextView selDay;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_notice, container, false);
        mChart = root.findViewById(R.id.chart);
        calendar = root.findViewById(R.id.cal);
        textYear = root.findViewById(R.id.data_year);
        textMD = root.findViewById(R.id.data_month_day);
        textBoss = root.findViewById(R.id.boss);
        textEnergy = root.findViewById(R.id.energy);
        textSkill = root.findViewById(R.id.skill);
        selYear = root.findViewById(R.id.year);
        selMonth = root.findViewById(R.id.month);
        selDay = root.findViewById(R.id.day);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(new Date());

        for(int i = 0; i < 3; i++){
            Intdate[i] = Integer.parseInt(date.split("-")[i]);
        }
        textYear.setText(Intdate[0] + "");
        textMD.setText(months[Intdate[1]-1] + " "+ Intdate[2]);
        eventList = CalendarFragment.eventList;
        capsules = getDayEventList(eventList, date, type);
        textBoss.setText(capsules[0]+"");
        textEnergy.setText(capsules[1]+"");
        textSkill.setText(capsules[2]+"");
        showPieChart(mChart, getPieChartData(capsules));

        calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragment newFragment = new DatePickerFragment(eventList,type);
                newFragment.show(getActivity().getSupportFragmentManager(), "Date Picker");

            }
        });

        selYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = 0;
                selYear.setBackground(getResources().getDrawable(R.drawable.year_selector2, null));
                selMonth.setBackground(getResources().getDrawable(R.drawable.month_selector, null));
                selDay.setBackground(getResources().getDrawable(R.drawable.day_selector, null));
                int month = getMonth(textMD.getText().toString().split(" ")[0])+1;
                String date = textYear.getText().toString()+"-"+month+"-"+ textMD.getText().toString().split(" ")[1];
                float[] capsules = getDayEventList(eventList,date, type);
                showPieChart(mChart, getPieChartData(capsules));
                textBoss.setText(capsules[0]+"");
                textEnergy.setText(capsules[1]+"");
                textSkill.setText(capsules[2]+"");


            }
        });

        selMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = 1;
                selYear.setBackground(getResources().getDrawable(R.drawable.year_selector, null));
                selMonth.setBackground(getResources().getDrawable(R.drawable.month_selector2, null));
                selDay.setBackground(getResources().getDrawable(R.drawable.day_selector, null));
                int month = getMonth(textMD.getText().toString().split(" ")[0])+1;
                String date = textYear.getText().toString()+"-"+month+"-"+ textMD.getText().toString().split(" ")[1];
                float[] capsules = getDayEventList(eventList,date, type);
                showPieChart(mChart, getPieChartData(capsules));
                textBoss.setText(capsules[0]+"");
                textEnergy.setText(capsules[1]+"");
                textSkill.setText(capsules[2]+"");

            }
        });

        selDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                type = 2;
                selYear.setBackground(getResources().getDrawable(R.drawable.year_selector, null));
                selMonth.setBackground(getResources().getDrawable(R.drawable.month_selector, null));
                selDay.setBackground(getResources().getDrawable(R.drawable.day_selector2, null));
                int month = getMonth(textMD.getText().toString().split(" ")[0])+1;
                String date = textYear.getText().toString()+"-"+month+"-"+ textMD.getText().toString().split(" ")[1];
                float[] capsules = getDayEventList(eventList,date, type);
                showPieChart(mChart, getPieChartData(capsules));
                textBoss.setText(capsules[0]+"");
                textEnergy.setText(capsules[1]+"");
                textSkill.setText(capsules[2]+"");

            }
        });






        return root;
    }



    public static float[] getDayEventList(List<Event> eventList,String date, int type){
        String[] dates = date.split("-");
        float[] capsules = new float[3];

        if(type == 0){
            for(Event event : eventList){
                if(event.getYear()==Integer.parseInt(dates[0])){
                    if(!event.isIs_complete()){
                        if(event.getType().equals("Boss Capsule")){
                            capsules[0] = capsules[0] + 1f;
                        }else{
                            capsules[2] = capsules[2] + 1f;
                        }
                    }else {
                        capsules[1] = capsules[1] + 1f;
                    }
                }
            }
        }else if (type == 1){
            for(Event event : eventList){
                if(event.getYear()==Integer.parseInt(dates[0]) && event.getMonth() == Integer.parseInt(dates[1])){
                    if(!event.isIs_complete()){
                        if(event.getType().equals("Boss Capsule")){
                            capsules[0] = capsules[0] + 1f;
                        }else{
                            capsules[2] = capsules[2] + 1f;
                        }
                    }else {
                        capsules[1] = capsules[1] + 1f;
                    }
                }
            }
        }else if (type == 2){
            for(Event event : eventList){
                if(event.getYear()==Integer.parseInt(dates[0]) && event.getMonth() == Integer.parseInt(dates[1]) && event.getDay() == Integer.parseInt(dates[2])){
                    if(!event.isIs_complete()){
                        if(event.getType().equals("Boss Capsule")){
                            capsules[0] = capsules[0] + 1f;
                        }else{
                            capsules[2] = capsules[2] + 1f;
                        }
                    }else {
                        capsules[1] = capsules[1] + 1f;
                    }
                }
            }
        }

        return capsules;


    }


    //添加数据
    private static List<PieEntry> getPieChartData(float[] capsules) {

        List<PieEntry> mPie = new ArrayList<>();
        float total = capsules[0] + capsules[1] + capsules[2];
        String[] names = {"Boss", "Energy", "Skill"};
        if(total==0){
            PieEntry pieNoneEntry = new PieEntry(1f, "None");
            mPie.add(pieNoneEntry);
        }else{

            for(int i = 0; i < 3; i++){
                if(capsules[i]==0){
                    mPie.add(new PieEntry(capsules[i]/total, ""));
                }else{
                    mPie.add(new PieEntry(capsules[i]/total, names[i]));
                }
            }


        }

        return mPie;
    }


    public static void showPieChart(PieChart pieChart, List<PieEntry> pieList) {
        PieDataSet dataSet = new PieDataSet(pieList,"Label");

        ArrayList<Integer> colors = new ArrayList<Integer>();
        final int[] ALLCAPSULE = {
                Color.rgb(255, 255, 140), Color.rgb(255, 208, 140),
                Color.rgb(140, 234, 255), Color.rgb(255, 140, 157)
        };

        final int[] NONE = {
                Color.rgb(255, 140, 157)
        };

        if(pieList.size() == 3){
            for (int c : ALLCAPSULE) {
                colors.add(c);
            }
        }else{
            for (int c : NONE) {
                colors.add(c);
            }
        }

        dataSet.setColors(colors);
        PieData pieData = new PieData(dataSet);

        // 设置描述，我设置了不显示，因为不好看，你也可以试试让它显示，真的不好看
        Description description = new Description();
        description.setEnabled(false);
        pieChart.setDescription(description);

        //设置半透明圆环的半径, 0为透明
        pieChart.setTransparentCircleRadius(0.5f);

        //设置初始旋转角度
        pieChart.setRotationAngle(-15);

        //数据连接线距图形片内部边界的距离，为百分数
        dataSet.setValueLinePart1OffsetPercentage(100f);

        //设置连接线的颜色
        dataSet.setValueLineColor(Color.BLACK);
        // 连接线在饼状图外面
        dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);

        // 设置饼块之间的间隔
        dataSet.setSliceSpace(5f);
        dataSet.setHighlightEnabled(true);
        // 不显示图例
        Legend legend = pieChart.getLegend();
        legend.setEnabled(false);

        // 和四周相隔一段距离,显示数据
        pieChart.setExtraOffsets(26, 5, 26, 5);

        // 设置pieChart图表是否可以手动旋转
        pieChart.setRotationEnabled(true);
        // 设置piecahrt图表点击Item高亮是否可用
        pieChart.setHighlightPerTapEnabled(true);
        // 设置pieChart图表展示动画效果，动画运行1.4秒结束
        pieChart.animateY(1000, Easing.EaseInOutQuad);
        //设置pieChart是否只显示饼图上百分比不显示文字
        pieChart.setDrawEntryLabels(true);
        pieChart.setEntryLabelColor(Color.rgb(100,100,100));
        pieChart.setEntryLabelTextSize(15f);
        //是否绘制PieChart内部中心文本
        pieChart.setDrawCenterText(true);
        // 绘制内容value，设置字体颜色大小
        pieData.setDrawValues(true);
        pieChart.setUsePercentValues(true);
        pieData.setValueFormatter(new PercentFormatter());
        pieData.setValueTextSize(18f);
        pieData.setValueTextColor(Color.BLACK);

        pieChart.setData(pieData);
        // 更新 piechart 视图
        pieChart.postInvalidate();
    }

    public int getMonth(String month){
        for(int i = 0; i < months.length; i++){
            if(months[i].equals(month)){
                return i;
            }
        }
        return 0;
    }



    public static class DatePickerFragment extends androidx.appcompat.app.AppCompatDialogFragment implements DatePickerDialog.OnDateSetListener {
        String[] months = {"Jan", "Feb", "Mar", "Apr","May", "Jun", "Jul", "Aug", "Sept","Oct","Nov","Dec"};
        int mYear, mMonth, mDay;

        List<Event> eventList;
        int type;

        DatePickerFragment(List<Event> eventList, int type){
            this.eventList = eventList;
            this.type = type;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            TextView textviewdatemd = (TextView) getActivity().findViewById(R.id.data_month_day);
            TextView textviewdatey = (TextView) getActivity().findViewById(R.id.data_year);
            mYear = Integer.parseInt(textviewdatey.getText().toString());
            mMonth = getMonth(textviewdatemd.getText().toString().split(" ")[0]);
            mDay = Integer.parseInt(textviewdatemd.getText().toString().split(" ")[1]);
            return new DatePickerDialog(getActivity(), this, mYear, mMonth, mDay);
        }

        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {
            TextView textviewdatemd = (TextView) getActivity().findViewById(R.id.data_month_day);
            TextView textviewdatey = (TextView) getActivity().findViewById(R.id.data_year);
            TextView textBoss = (TextView) getActivity().findViewById(R.id.boss);
            TextView textEnergy = (TextView) getActivity().findViewById(R.id.energy);
            TextView textSkill = (TextView) getActivity().findViewById(R.id.skill);

            PieChart mChart = (PieChart) getActivity().findViewById(R.id.chart);

            textviewdatemd.setText(months[month]+ " "+day);
            textviewdatey.setText(year+"");
            int mon = month+1;
            String date = year + "-" + mon + "-" + day;
            float[] capsules = getDayEventList(eventList,date, type);
            showPieChart(mChart, getPieChartData(capsules));

            textBoss.setText(capsules[0]+"");
            textEnergy.setText(capsules[1]+"");
            textSkill.setText(capsules[2]+"");


            cal.set(Calendar.DAY_OF_MONTH, day);
            cal.set(Calendar.MONTH, month);
            cal.set(Calendar.YEAR, year);

            mYear = year;
            mMonth = month + 1;
            mDay = day;

        }

        public int getMonth(String month){
            for(int i = 0; i < months.length; i++){
                if(months[i].equals(month)){
                    return i;
                }
            }
            return 0;
        }


    }






}