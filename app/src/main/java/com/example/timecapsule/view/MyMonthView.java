package com.example.timecapsule.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextUtils;

import com.example.timecapsule.db.Event;
import com.example.timecapsule.fragment.CalendarFragment;
import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.MonthView;

import java.util.ArrayList;
import java.util.List;


public class MyMonthView extends MonthView {


    private Paint mTextPaint = new Paint();
    private Paint mCurrentDayPaint = new Paint();
    private Paint mPaint = new Paint();
    private Paint mSchemeBasicPaint = new Paint();
    private float mRadio;
    private int mPadding;
    private float mSchemeBaseLine;


    public MyMonthView(Context context) {
        super(context);

        mTextPaint.setTextSize(dipToPx(context, 8));
        mTextPaint.setColor(0xffffffff);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setFakeBoldText(true);

        mCurrentDayPaint.setAntiAlias(true);
        mCurrentDayPaint.setStyle(Paint.Style.FILL);
        mCurrentDayPaint.setColor(Color.parseColor("#FF9800"));

        mPaint.setTextSize(dipToPx(context, 8));
        mPaint.setColor(Color.parseColor("#ed5353"));
        mPaint.setAntiAlias(true);
        mPaint.setFakeBoldText(true);

        mSchemeBasicPaint.setAntiAlias(true);
        mSchemeBasicPaint.setStyle(Paint.Style.FILL);
        mSchemeBasicPaint.setTextAlign(Paint.Align.CENTER);
        mSchemeBasicPaint.setFakeBoldText(true);


        mRadio = dipToPx(getContext(), 7);
        mPadding = dipToPx(getContext(), 4);

        Paint.FontMetrics metrics = mSchemeBasicPaint.getFontMetrics();
        mSchemeBaseLine = mRadio - metrics.descent + (metrics.bottom - metrics.top) / 2 + dipToPx(getContext(), 1);

    }


    @Override
    protected boolean onDrawSelected(Canvas canvas, Calendar calendar, int x, int y, boolean hasScheme) {
        mSelectedPaint.setStyle(Paint.Style.FILL);
        canvas.drawRect(x + mPadding+7, y + mPadding+7, x + mItemWidth - mPadding/2-7, y + mItemHeight - mPadding-7, mSelectedPaint);
        return true;
    }


    @Override
    protected void onDrawScheme(Canvas canvas, Calendar calendar, int x, int y) {

        int boss_number = 0;
        int skill_number = 0;
        int energy_number = 0;
        List<Calendar.Scheme> schemes = calendar.getSchemes();
        for(Calendar.Scheme scheme : schemes){
            if(scheme.getScheme().equals("B")){
                if(boss_number==0){
                    mSchemeBasicPaint.setColor(scheme.getShcemeColor());
                    canvas.drawCircle(x + mItemWidth - mPadding - mRadio / 2, y + mPadding + mRadio, mRadio, mSchemeBasicPaint);
                    canvas.drawText("B",
                            x + mItemWidth - mPadding - mRadio / 2 - getTextWidth("B") / 2,
                            y + mPadding + mSchemeBaseLine, mTextPaint);
                }
                boss_number+=1;

            }else if (scheme.getScheme().equals("S")){
                if(skill_number==0){
                    mSchemeBasicPaint.setColor(scheme.getShcemeColor());
                    canvas.drawCircle(x + mItemWidth - mPadding - mRadio / 2, y + mPadding + mRadio*3 , mRadio, mSchemeBasicPaint);
                    canvas.drawText("S",
                            x + mItemWidth - mPadding - mRadio / 2 - getTextWidth("S") / 2,
                            y + mPadding + mSchemeBaseLine+mRadio*2 , mTextPaint);
                }
                skill_number+=1;
            }else{
                if(energy_number==0){
                    mSchemeBasicPaint.setColor(scheme.getShcemeColor());
                    canvas.drawCircle(x + mItemWidth - mPadding - mRadio / 2, y + mPadding + mRadio*5 , mRadio, mSchemeBasicPaint);
                    canvas.drawText("E",
                            x + mItemWidth - mPadding - mRadio / 2 - getTextWidth("S") / 2,
                            y + mPadding + mSchemeBaseLine+mRadio*4, mTextPaint);
                }
                energy_number+=1;
            }
        }
        if(boss_number != 0){
            canvas.drawText("x"+boss_number, x + mItemWidth-5,y + mPadding + mRadio*1 + 5, mPaint);

        }

        if(skill_number!= 0){
            canvas.drawText("x"+skill_number, x + mItemWidth-5,y + mPadding + mRadio*3 +5, mPaint);
        }

        if(energy_number!= 0){
            canvas.drawText("x"+energy_number, x + mItemWidth-5,y + mPadding + mRadio*5 +6, mPaint);
        }

    }

    private float getTextWidth(String text) {
        return mTextPaint.measureText(text);
    }


    @Override
    protected void onDrawText(Canvas canvas, Calendar calendar, int x, int y, boolean hasScheme, boolean isSelected) {
        int cx = x + mItemWidth / 2;
        int top;
        if(CalendarFragment.is_english == true){
            top = y;
        }else{
            top = y - mItemHeight / 6;
        }

        boolean isInRange = isInRange(calendar);


        if (calendar.isWeekend() && calendar.isCurrentMonth()) {
            mCurMonthTextPaint.setColor(Color.parseColor("#FAA9A9"));
            mCurMonthLunarTextPaint.setColor(Color.parseColor("#FAA9A9"));
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
            if(CalendarFragment.is_english == false){
                canvas.drawText(calendar.getLunar(), cx, mTextBaseLine + y + mItemHeight / 10, mSelectedLunarTextPaint);
            }
        } else if (hasScheme) {
            canvas.drawText(String.valueOf(calendar.getDay()), cx, mTextBaseLine + top,
                    calendar.isCurrentDay() ? mCurDayTextPaint :
                        calendar.isCurrentMonth() && isInRange ? mSchemeTextPaint : mOtherMonthTextPaint);
            if(CalendarFragment.is_english == false){
                canvas.drawText(calendar.getLunar(), cx, mTextBaseLine + y + mItemHeight / 10,
                        calendar.isCurrentDay() && isInRange ? mCurDayLunarTextPaint :
                                calendar.isCurrentMonth() ? mCurMonthLunarTextPaint : mOtherMonthLunarTextPaint);

            }

        }
        else {
            canvas.drawText(String.valueOf(calendar.getDay()), cx, mTextBaseLine + top,
                    calendar.isCurrentDay() ? mCurDayTextPaint :
                            calendar.isCurrentMonth() && isInRange ? mCurMonthTextPaint : mOtherMonthTextPaint);
            if(CalendarFragment.is_english == false){
                canvas.drawText(calendar.getLunar(), cx, mTextBaseLine + y + mItemHeight / 10,
                        calendar.isCurrentDay() && isInRange ? mCurDayLunarTextPaint :
                                calendar.isCurrentMonth() ? mCurMonthLunarTextPaint : mOtherMonthLunarTextPaint);
            }
        }

    }


    private static int dipToPx(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
