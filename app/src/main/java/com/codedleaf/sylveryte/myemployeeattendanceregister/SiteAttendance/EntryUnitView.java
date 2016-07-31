package com.codedleaf.sylveryte.myemployeeattendanceregister.SiteAttendance;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.codedleaf.sylveryte.myemployeeattendanceregister.Models.Entry;
import com.codedleaf.sylveryte.myemployeeattendanceregister.R;

/**
 * Created by sylveryte on 31/7/16.
 * <p/>
 * Copyright (C) 2016 sylveryte@codedleaf <codedlaf@gmail.com>
 * <p/>
 * This file is part of My Employee Attendance Register.
 */
public class EntryUnitView extends LinearLayout{


    public EntryUnitView(Context context, Entry entry) {
        super(context);
        LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout view =(LinearLayout)inflater.inflate(R.layout.entry_unit,EntryUnitView.this,false);
        CardView unitCard=(CardView)view.findViewById(R.id.entry_card);
        addView(view);
        unitCard.setOnClickListener(new EntryUnitCardListner(context,entry,unitCard));
        EntryUnitCardListner.setColor(entry,context,unitCard);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT,1.0f);
        lp.gravity= Gravity.CENTER;
        EntryUnitView.this.setLayoutParams(lp);
    }
}
