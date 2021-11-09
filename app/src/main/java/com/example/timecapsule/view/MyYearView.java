package com.example.timecapsule.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.util.Log;

import com.example.timecapsule.R;
import com.example.timecapsule.fragment.CalendarFragment;
import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.YearView;


public class MyYearView extends YearView{

    Typeface font;
    Bitmap bitmap;

    public MyYearView(Context context) {
        super(context);
        bitmap = CalendarFragment.bitmap;
        font = CalendarFragment.font;
        mMonthTextPaint.setTypeface(font);

    }

    @Override
    protected void onDrawMonth(Canvas canvas, int year, int month, int x, int y, int width, int height) {

        String months = getContext()
                .getResources()
                .getStringArray(com.haibin.calendarview.R.array.month_string_array)[month - 1];

        java.util.Calendar calendar = java.util.Calendar.getInstance();
        if(year==calendar.get(java.util.Calendar.YEAR)){
            mMonthTextPaint.setColor(0xffed5353);
        }
        canvas.drawText(months, x + mItemWidth / 2, y + mMonthTextBaseLine, mMonthTextPaint);
        canvas.drawBitmap(bitmap,x+mItemWidth*4 , y ,mMonthTextPaint);
    }

    @Override
    protected void onDrawWeek(Canvas canvas, int week, int x, int y, int width, int height) {
        String text = getContext().getResources().getStringArray(com.haibin.calendarview.R.array.year_view_week_string_array)[week];
        canvas.drawText(text, x + width / 2, y + mWeekTextBaseLine, mWeekTextPaint);
    }

    @Override
    protected boolean onDrawSelected(Canvas canvas, Calendar calendar, int x, int y, boolean hasScheme) {
        int cx = x + mItemWidth / 2;
        int cy = y + mItemHeight / 2;
        int radius = Math.min(mItemWidth, mItemHeight) / 8 * 5;
        mSelectedPaint.setColor(0xffed5353);
        canvas.drawCircle(cx, cy, radius, mSelectedPaint);
        return true;
    }

    @Override
    protected void onDrawScheme(Canvas canvas, Calendar calendar, int x, int y) {

    }

    @Override
    protected void onDrawText(Canvas canvas, Calendar calendar, int x, int y, boolean hasScheme, boolean isSelected) {
        float baselineY = mTextBaseLine + y;
        int cx = x + mItemWidth / 2;

        if (isSelected) {
            canvas.drawText(String.valueOf(calendar.getDay()),
                    cx,
                    baselineY,
                    hasScheme ? mSchemeTextPaint : mSelectTextPaint);
        } else if (hasScheme) {
            canvas.drawText(String.valueOf(calendar.getDay()),
                    cx,
                    baselineY,
                    calendar.isCurrentDay() ? mCurDayTextPaint : mSchemeTextPaint);

        } else {
            canvas.drawText(String.valueOf(calendar.getDay()), cx, baselineY,
                    calendar.isCurrentDay() ? mCurDayTextPaint : mCurMonthTextPaint);
        }
    }

}
