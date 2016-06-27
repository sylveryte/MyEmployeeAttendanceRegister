package com.codedleaf.sylveryte.myemployeeattendanceregister;

import java.util.Date;
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


    Date mDate;
    int mRemark;
    UUID mSiteId;
    UUID mEmployeeId;
    String mNote;


    public Entry(UUID employeeId)
    {
        mEmployeeId=employeeId;

        mDate=new Date();
        mRemark=PRESENT;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
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

    public UUID getEmployeeId() {
        return mEmployeeId;
    }

    public String getEmployeeInfo()
    {
        return EmployeeLab.getInstanceOf().getEmployeeById(mEmployeeId).getTitle();
    }

    public String getNote() {
        return mNote;
    }

    public void setNote(String note) {
        mNote = note;
    }
}
