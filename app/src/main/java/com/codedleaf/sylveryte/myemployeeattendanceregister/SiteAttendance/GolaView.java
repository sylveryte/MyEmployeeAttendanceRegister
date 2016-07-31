package com.codedleaf.sylveryte.myemployeeattendanceregister.SiteAttendance;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.codedleaf.sylveryte.myemployeeattendanceregister.Entry;
import com.codedleaf.sylveryte.myemployeeattendanceregister.R;

import org.joda.time.LocalDate;

/**
 * Created by sylveryte on 31/7/16.
 * <p>
 * Copyright (C) 2016 sylveryte@codedleaf <codedlaf@gmail.com>
 * <p>
 * This file is part of My Employee Attendance Register.
 */
public class GolaView
{
    private Entry mEntry;
    private CardView mGolaCard;
    private LinearLayout mLayoutView;
    private LocalDate mDate;

    private Context mContext;

    private TextView mTextView;

    public GolaView(LocalDate date,Context context)
    {
        mContext=context;
        mLayoutView=(LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.attendance_gola,null,true);
        mLayoutView.setGravity(Gravity.CENTER);
        mGolaCard =(CardView) mLayoutView.findViewById(R.id.attendance_gola_b);
        mTextView=(TextView)mLayoutView.findViewById(R.id.gola_text);
        mDate=date;
        String s=date.getDayOfMonth()+"";
        mTextView.setText(s);
    }

    public void setEntry(Entry entry)
    {
        if (entry.getDate().equals(mDate))
            mEntry = entry;
        CustomAttendanceCardListner.setColor(mEntry,mContext,mGolaCard);
        mLayoutView.setOnClickListener(new CustomAttendanceCardListner(mContext,mEntry,mGolaCard));
    }

    public void setClicability()
    {
        if (mEntry==null)
            mLayoutView.setClickable(false);
        else
            mLayoutView.setClickable(true);
    }

    public LinearLayout getLayoutView() {

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT,1.0f);
        lp.gravity=Gravity.CENTER;
        mLayoutView.setLayoutParams(lp);
        return mLayoutView;
    }
}