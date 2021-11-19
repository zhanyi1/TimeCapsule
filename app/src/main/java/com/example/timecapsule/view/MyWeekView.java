package com.example.timecapsule.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextUtils;

import com.example.timecapsule.fragment.CalendarFragment;
import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.WeekView;

import java.util.List;

/**
 * 演示一个变态需求的周视图
 * Created by huanghaibin on 2018/2/9.
 */

public class MyWeekView extends WeekView {


    private int mRadius;

    /**
     * 自定义魅族标记的文本画笔
     */
    private Paint mTextPaint = new Paint();


    /**
     * 24节气画笔
     */
    private Paint mSolarTermTextPaint = new Paint();

    /**
     * 背景圆点
     */
    private Paint mPointPaint = new Paint();

    /**
     * 今天的背景色
     */
    private Paint mCurrentDayPaint = new Paint();


    /**
     * 圆点半径
     */
    private float mPointRadius;

    private int mPadding;

    private float mCircleRadius;
    /**
     * 自定义魅族标记的圆形背景
     */
    private Paint mSchemeBasicPaint = new Paint();
    private Paint mPaint = new Paint();

    private float mSchemeBaseLine;

    public MyWeekView(Context context) {
        super(context);
        mTextPaint.setTextSize(dipToPx(context, 8));
        mTextPaint.setColor(0xffffffff);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setFakeBoldText(true);

        mPaint.setTextSize(dipToPx(context, 8));
        mPaint.setColor(Color.parseColor("#ed5353"));
        mPaint.setAntiAlias(true);
        mPaint.setFakeBoldText(true);


        mSolarTermTextPaint.setColor(0xff489dff);
        mSolarTermTextPaint.setAntiAlias(true);
        mSolarTermTextPaint.setTextAlign(Paint.Align.CENTER);

        mSchemeBasicPaint.setAntiAlias(true);
        mSchemeBasicPaint.setStyle(Paint.Style.FILL);
        mSchemeBasicPaint.setTextAlign(Paint.Align.CENTER);
        mSchemeBasicPaint.setFakeBoldText(true);
        mSchemeBasicPaint.setColor(Color.WHITE);

        mPointPaint.setAntiAlias(true);
        mPointPaint.setStyle(Paint.Style.FILL);
        mPointPaint.setTextAlign(Paint.Align.CENTER);
        mPointPaint.setColor(Color.RED);


        mCurrentDayPaint.setAntiAlias(true);
        mCurrentDayPaint.setStyle(Paint.Style.FILL);
        mCurrentDayPaint.setColor(0xFFeaeaea);


        mCircleRadius = dipToPx(getContext(), 7);

        mPadding = dipToPx(getContext(), 3);

        mPointRadius = dipToPx(context, 2);

        Paint.FontMetrics metrics = mSchemeBasicPaint.getFontMetrics();
        mSchemeBaseLine = mCircleRadius - metrics.descent + (metrics.bottom - metrics.top) / 2 + dipToPx(getContext(), 1);

    }


    @Override
    protected void onPreviewHook() {
        mSolarTermTextPaint.setTextSize(mCurMonthLunarTextPaint.getTextSize());
        mRadius = Math.min(mItemWidth, mItemHeight) / 11 * 5-7;
    }


    @Override
    protected boolean onDrawSelected(Canvas canvas, Calendar calendar, int x, boolean hasScheme) {
        int cx = x + mItemWidth / 2;
        int cy = mItemHeight / 2;
//        canvas.drawRect(cx-mItemHeight/2 +17 , cy-mItemHeight/2 +17, cx + mItemWidth/2 -17, cy + mItemHeight/2 -17, mSelectedPaint);
        canvas.drawCircle(cx, cy, mRadius, mSelectedPaint);
        return true;
    }


    @Override
    protected void onDrawScheme(Canvas canvas, Calendar calendar, int x) {

        boolean isSelected = isSelected(calendar);
        if (isSelected) {
            mPointPaint.setColor(Color.WHITE);
        } else {
            mPointPaint.setColor(Color.GRAY);
        }

        canvas.drawCircle(x + mItemWidth / 2, mItemHeight - 3 * mPadding, mPointRadius, mPointPaint);
    }

    @SuppressWarnings("IntegerDivisionInFloatingPointContext")
    @Override
    protected void onDrawText(Canvas canvas, Calendar calendar, int x, boolean hasScheme, boolean isSelected) {
        int cx = x + mItemWidth / 2;
        int cy = mItemHeight / 2;
        int top;
        if(CalendarFragment.is_english==false){
            top = -mItemHeight / 6;
        }else{
            top = 0;
        }

        if (calendar.isCurrentDay() && !isSelected) {
            canvas.drawCircle(cx, cy, mRadius, mCurrentDayPaint);
        }

        if(hasScheme) {

            int boss_number = 0;
            int skill_number = 0;
            int energy_number = 0;
            List<Calendar.Scheme> schemes = calendar.getSchemes();
            for(Calendar.Scheme scheme : schemes){
                if(scheme.getScheme().equals("B")){
                    if(boss_number==0){
                        mSchemeBasicPaint.setColor(scheme.getShcemeColor());
                        canvas.drawCircle(x + mItemWidth - mPadding - mCircleRadius / 2, mPadding + mCircleRadius, mCircleRadius, mSchemeBasicPaint);
                        canvas.drawText("B",
                                x + mItemWidth - mPadding - mCircleRadius / 2 - 10 / 2,
                                mPadding + mSchemeBaseLine, mTextPaint);
                    }
                    boss_number+=1;

                }else if (scheme.getScheme().equals("S")){
                    if(skill_number==0){
                        mSchemeBasicPaint.setColor(scheme.getShcemeColor());
                        canvas.drawCircle(x + mItemWidth - mPadding - mCircleRadius / 2,  mPadding + mCircleRadius*3 , mCircleRadius, mSchemeBasicPaint);
                        canvas.drawText("S",
                                x + mItemWidth - mPadding - mCircleRadius / 2 - 10 / 2,
                                mPadding + mSchemeBaseLine+mCircleRadius*2 , mTextPaint);
                    }
                    skill_number+=1;
                }else{
                    if(energy_number==0){
                        mSchemeBasicPaint.setColor(scheme.getShcemeColor());
                        canvas.drawCircle(x + mItemWidth - mPadding - mCircleRadius / 2,  mPadding + mCircleRadius*5 , mCircleRadius, mSchemeBasicPaint);
                        canvas.drawText("E",
                                x + mItemWidth - mPadding - mCircleRadius / 2 - 10 / 2,
                                mPadding + mSchemeBaseLine+mCircleRadius*4, mTextPaint);
                    }
                    energy_number+=1;
                }
            }
            if(boss_number != 0){
                canvas.drawText("x"+boss_number, x + mItemWidth-5, mPadding + mCircleRadius*1 + 5, mPaint);

            }

            if(skill_number!= 0){
                canvas.drawText("x"+skill_number, x + mItemWidth-5, mPadding + mCircleRadius*3 +5, mPaint);
            }

            if(energy_number!= 0){
                canvas.drawText("x"+energy_number, x + mItemWidth-5, mPadding + mCircleRadius*5 +6, mPaint);
            }
        }

        if (calendar.isWeekend() && calendar.isCurrentMonth()) {
            mCurMonthTextPaint.setColor(0xFF489dff);
            mCurMonthLunarTextPaint.setColor(0xFF489dff);
            mSchemeTextPaint.setColor(0xFF489dff);
            mSchemeLunarTextPaint.setColor(0xFF489dff);
            mOtherMonthLunarTextPaint.setColor(0xFF489dff);
            mOtherMonthTextPaint.setColor(0xFF489dff);
        } else {
            mCurMonthTextPaint.setColor(0xff333333);
            mCurMonthLunarTextPaint.setColor(0xffCFCFCF);
            mSchemeTextPaint.setColor(0xff333333);
            mSchemeLunarTextPaint.setColor(0xffCFCFCF);
            mOtherMonthTextPaint.setColor(0xFFe1e1e1);
            mOtherMonthLunarTextPaint.setColor(0xFFe1e1e1);
        }

        if (isSelected) {
            canvas.drawText(String.valueOf(calendar.getDay()), cx, mTextBaseLine + top,
                    mSelectTextPaint);
            if(CalendarFragment.is_english==false){
                canvas.drawText(calendar.getLunar(), cx, mTextBaseLine + mItemHeight / 10, mSelectedLunarTextPaint);
            }
        } else if (hasScheme) {

            canvas.drawText(String.valueOf(calendar.getDay()), cx, mTextBaseLine + top,
                    calendar.isCurrentMonth() ? mSchemeTextPaint : mOtherMonthTextPaint);
            if(CalendarFragment.is_english==false){
                canvas.drawText(calendar.getLunar(), cx, mTextBaseLine + mItemHeight / 10,
                        !TextUtils.isEmpty(calendar.getSolarTerm()) ? mSolarTermTextPaint : mSchemeLunarTextPaint);
            }

        } else {
            canvas.drawText(String.valueOf(calendar.getDay()), cx, mTextBaseLine + top,
                    calendar.isCurrentDay() ? mCurDayTextPaint :
                            calendar.isCurrentMonth() ? mCurMonthTextPaint : mOtherMonthTextPaint);
            if(CalendarFragment.is_english==false){
                canvas.drawText(calendar.getLunar(), cx, mTextBaseLine + mItemHeight / 10,
                        calendar.isCurrentDay() ? mCurDayLunarTextPaint :
                                !TextUtils.isEmpty(calendar.getSolarTerm()) ? mSolarTermTextPaint :
                                        calendar.isCurrentMonth() ?
                                                mCurMonthLunarTextPaint : mOtherMonthLunarTextPaint);
            }
        }
    }

    /**
     * dp转px
     *
     * @param context context
     * @param dpValue dp
     * @return px
     */
    private static int dipToPx(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
