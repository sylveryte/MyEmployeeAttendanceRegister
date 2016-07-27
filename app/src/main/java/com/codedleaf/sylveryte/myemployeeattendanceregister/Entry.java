package com.codedleaf.sylveryte.myemployeeattendanceregister;


import android.content.Context;

import org.joda.time.LocalDate;

import java.util.UUID;

/**
 * Created by sylveryte on 12/6/16.
 *
 * Copyright (C) 2016 sylveryte@codedleaf <codedlaf@gmail.com>
 *
 * This file is part of My Employee Attendance Register.
 *
 */
public class Entry {


    public static final int PRESENT=70;
    public static final int LATE=71;
    public static final int HALF_TIME=72;
    public static final int OVER_TIME=73;
    public static final int ABSENT=74;
    public static final int NOTSPECIFIED=75;


    private LocalDate mDate;

    private int mRemark;

    private UUID mSiteId;
    private UUID mEmployeeId;

    private Boolean mNew;

    private String mNote;


    public Entry(UUID employeeId,UUID siteId)
    {
        mEmployeeId=employeeId;

        mSiteId=siteId;

        mDate=new LocalDate();

        mNew=false;

        mRemark=NOTSPECIFIED;
    }

    public Entry(UUID employeeId,UUID siteId,LocalDate date)
    {
        mEmployeeId=employeeId;

        mSiteId=siteId;

        mNew=false;

        mDate=date;
    }

    public LocalDate getDate() {
        return mDate;
    }

    public int getRemark() {
        return mRemark;
    }

    public void setRemark(int remark) {
        mRemark = remark;
    }

    public UUID getSiteId() {
        return mSiteId;
    }

    public Boolean isNew() {
        return mNew;
    }

    public void setNew(Boolean aNew) {
        mNew = aNew;
    }

    public UUID getEmployeeId() {

        return mEmployeeId;
    }

    public String getEmployeeInfo(Context context)
    {
        return EmployeeLab.getInstanceOf(context).getEmployeeById(mEmployeeId).getTitle();
    }

    public String  getRemarkString()
    {
        switch (getRemark())
        {
            case PRESENT: return "Present";
            case LATE: return "Late";
            case HALF_TIME: return "Half-Time";
            case OVER_TIME: return "Over-Time";
            case ABSENT: return "Absent";
            case NOTSPECIFIED: return "Not-Specified";
        }
        return "Don't know";
    }

    public String getNote() {
        return mNote;
    }

    public void setNote(String note) {
        mNote = note;
    }
}
