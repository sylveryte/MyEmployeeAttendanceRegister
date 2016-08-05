package com.codedleaf.sylveryte.myemployeeattendanceregister.Models;

import android.content.Context;

import com.codedleaf.sylveryte.myemployeeattendanceregister.Labs.MoneyLab;

import org.joda.time.DateTime;

import java.util.UUID;

/**
 * Created by sylveryte on 5/8/16.
 * <p>
 * Copyright (C) 2016 sylveryte@codedleaf <codedlaf@gmail.com>
 * <p>
 * This file is part of My Employee Attendance Register.
 */
public class Money{

    private UUID mEmployeeId;
    private UUID mSiteId;
    private int mAmount;
    private String mNote;
    private DateTime mDate;

    public Money(UUID employeeId,UUID siteId,DateTime date)
    {
        mEmployeeId=employeeId;
        mSiteId=siteId;
        mDate=date;
    }

    public void setAmount(int amount) {
        mAmount = amount;
    }

    public void setNote(String note) {
        mNote = note;
    }

    public void setDate(DateTime date) {
        mDate = date;
    }

    public UUID getEmployeeId() {
        return mEmployeeId;
    }

    public UUID getSiteId() {
        return mSiteId;
    }

    public int getAmount() {
        return mAmount;
    }

    public String getNote() {
        return mNote;
    }

    public DateTime getDate() {
        return mDate;
    }

    public void updateMe(Context context)
    {
        MoneyLab.getInstanceOf(context).updateMoney(this);
    }
}
