package com.example.timecapsule.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import com.example.timecapsule.fragment.CalendarFragment;
import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.WeekView;

import java.util.List;

/**
 * 精美进度风格
 * Created by huanghaibin on 2018/2/8.
 */

public class ProgressWeekView extends WeekView {


    private int mRadius;
    private Paint mEnergyProgressPaint = new Paint();
    private Paint mBossProgressPaint = new Paint();
    private Paint mSkillProgressPaint = new Paint();

    public ProgressWeekView(Context context) {
        super(context);
        mEnergyProgressPaint.setAntiAlias(true);
        mEnergyProgressPaint.setStyle(Paint.Style.STROKE);
        mEnergyProgressPaint.setStrokeWidth(dipToPx(context, 2.2f));
        mEnergyProgressPaint.setColor(0xFFaacc44);

        mBossProgressPaint.setAntiAlias(true);
        mBossProgressPaint.setStyle(Paint.Style.STROKE);
        mBossProgressPaint.setStrokeWidth(dipToPx(context, 2.2f));
        mBossProgressPaint.setColor(0xffed5353);

        mSkillProgressPaint.setAntiAlias(true);
        mSkillProgressPaint.setStyle(Paint.Style.STROKE);
        mSkillProgressPaint.setStrokeWidth(dipToPx(context, 2.2f));
        mSkillProgressPaint.setColor(0xFF13acf0);
    }

    @Override
    protected void onPreviewHook() {
        mRadius = Math.min(mItemWidth, mItemHeight) / 11 * 5;
    }

    @Override
    protected boolean onDrawSelected(Canvas canvas, Calendar calendar, int x, boolean hasScheme) {
        int cx = x + mItemWidth / 2;
        int cy = mItemHeight / 2;

        if(hasScheme){
            canvas.drawCircle(cx, cy, Math.min(mItemWidth, mItemHeight) / 11 * 4, mSelectedPaint);
            DrawScheme(canvas,calendar,x);
        }else{
            canvas.drawCircle(cx, cy, mRadius, mSelectedPaint);
        }
        return false;

    }

    @Override
    protected void onDrawScheme(Canvas canvas, Calendar calendar, int x) {
        DrawScheme(canvas,calendar,x);

    }

    private void DrawScheme(Canvas canvas, Calendar calendar, int x) {

        int cx = x + mItemWidth / 2;
        int cy = mItemHeight / 2;


        int boss_number = 0;
        int skill_number = 0;
        int energy_number = 0;
        List<Calendar.Scheme> schemes = calendar.getSchemes();
        for(Calendar.Scheme scheme : schemes){
            if(scheme.getScheme().equals("B")){
                boss_number++;
            }else if(scheme.getScheme().equals("S")){
                skill_number++;
            }else {
                energy_number++;
            }
        }
        int progress = energy_number * 100 / (energy_number+skill_number+boss_number);
        int boss_pro = boss_number * 100 / (energy_number+skill_number+boss_number);
        int skill_pro = skill_number * 100 / (energy_number+skill_number+boss_number);
        int angle = getAngle(progress);
        int boss_angle = getAngle(boss_pro);
        int skill_angle = getAngle(skill_pro);

        RectF progressRectF = new RectF(cx - mRadius, cy - mRadius, cx + mRadius, cy + mRadius);
        canvas.drawArc(progressRectF, -90, angle, false, mEnergyProgressPaint);

        RectF bossRectF = new RectF(cx - mRadius, cy - mRadius, cx + mRadius, cy + mRadius);
        canvas.drawArc(bossRectF, angle - 90, boss_angle, false, mBossProgressPaint);

        RectF skillRectF = new RectF(cx - mRadius, cy - mRadius, cx + mRadius, cy + mRadius);
        canvas.drawArc(skillRectF, angle+boss_angle - 90, skill_angle, false, mSkillProgressPaint);

    }


    @Override
    protected void onDrawText(Canvas canvas, Calendar calendar, int x, boolean hasScheme, boolean isSelected) {
        float baselineY;
        if(CalendarFragment.is_english == false){
            baselineY= (float) (mTextBaseLine - 20.5);
        }else {
            baselineY = mTextBaseLine;
        }
        int cx = x + mItemWidth / 2;

        if (isSelected) {
            canvas.drawText(String.valueOf(calendar.getDay()),
                    cx,
                    baselineY,
                    mSelectTextPaint);
            if(CalendarFragment.is_english==false){
                canvas.drawText(calendar.getLunar(), cx, mTextBaseLine + mItemHeight / 10, mSelectedLunarTextPaint);
            }
        } else if (hasScheme) {
            canvas.drawText(String.valueOf(calendar.getDay()),
                    cx,
                    baselineY,
                    calendar.isCurrentDay() ? mCurDayTextPaint :
                            calendar.isCurrentMonth() ? mSchemeTextPaint : mOtherMonthTextPaint);
            if(CalendarFragment.is_english==false){
                canvas.drawText(calendar.getLunar(), cx, mTextBaseLine + mItemHeight / 10, mCurMonthLunarTextPaint);
            }
        } else {
            canvas.drawText(String.valueOf(calendar.getDay()), cx, baselineY,
                    calendar.isCurrentDay() ? mCurDayTextPaint :
                            calendar.isCurrentMonth() ? mCurMonthTextPaint : mOtherMonthTextPaint);
            if(CalendarFragment.is_english==false){
                canvas.drawText(calendar.getLunar(), cx, mTextBaseLine + mItemHeight / 10, mCurMonthLunarTextPaint);
            }
        }

    }

    /**
     * 获取角度
     *
     * @param progress 进度
     * @return 获取角度
     */
    private static int getAngle(int progress) {
        return (int) (progress * 3.6);
    }


    /**
     * dp转px
     *
     * @param context context
     * @param dpValue dp
     * @return px
     */
    @SuppressWarnings("all")
    private static int dipToPx(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
