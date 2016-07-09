package com.codedleaf.sylveryte.myemployeeattendanceregister;


import android.content.Context;

import org.joda.time.LocalDate;

import java.util.UUID;

/**
 * Created by sylveryte on 12/6/16.
 */
public class Entry {


    public static final int PRESENT=70;
    public static final int LATE=71;
    public static final int HALF_TIME=72;
    public static final int OVER_TIME=73;
    public static final int ABSENT=74;


    private LocalDate mDate;

    private int mRemark;

    private UUID mSiteId;
    private UUID mEmployeeId;


    private String mNote;


    public Entry(UUID employeeId,UUID siteId)
    {
        mEmployeeId=employeeId;

        mSiteId=siteId;

        mDate=new LocalDate();
        mRemark=PRESENT;
    }

    public Entry(UUID employeeId,UUID siteId,LocalDate date)
    {
        mEmployeeId=employeeId;

        mSiteId=siteId;

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

    public void setSiteId(UUID siteId) {
        mSiteId = siteId;
    }

    public void setEmployeeId(UUID employeeId) {
        mEmployeeId = employeeId;
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
