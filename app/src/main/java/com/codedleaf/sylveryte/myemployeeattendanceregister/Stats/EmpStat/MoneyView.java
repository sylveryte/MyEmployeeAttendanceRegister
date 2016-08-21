package com.codedleaf.sylveryte.myemployeeattendanceregister.Stats.EmpStat;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.codedleaf.sylveryte.myemployeeattendanceregister.Models.Money;
import com.codedleaf.sylveryte.myemployeeattendanceregister.R;

/**
 * Created by sylveryte on 21/8/16.
 * <p/>
 * Copyright (C) 2016 sylveryte@codedleaf <codedleaf@gmail.com>
 * <p/>
 * This file is part of My Employee Attendance Register.
 */
public class MoneyView extends FrameLayout {

    private CardView mCardView;
    private TextView mAmount;
    private TextView mDateTime;
    private TextView mSite;
    private TextView mNote;
    private Context mContext;

    public MoneyView(Context context) {
        super(context);
        FrameLayout.LayoutParams layoutParams=new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        setLayoutParams(layoutParams);
        mContext=context;
        LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mCardView=(CardView)inflater.inflate(R.layout.money_view_card,this,false);
        addView(mCardView);
        mAmount=(TextView)mCardView.findViewById(R.id.amount);
        mNote=(TextView)mCardView.findViewById(R.id.note_text);
        mDateTime=(TextView)mCardView.findViewById(R.id.date_time);
        mSite=(TextView)mCardView.findViewById(R.id.site_name);
    }

    private void update(Money money)
    {
        mAmount.setText(money.getAmountString());
        mDateTime.setText(money.getDateTimeString());
        mSite.setText(money.getSiteString(mContext));
        mNote.setText(money.getNote());
    }

    public void setMoney(Money money)
    {
        update(money);
    }
}
