package com.codedleaf.sylveryte.myemployeeattendanceregister.Models;

import android.content.Context;

import com.codedleaf.sylveryte.myemployeeattendanceregister.CodedleafTools;
import com.codedleaf.sylveryte.myemployeeattendanceregister.Labs.MoneyLab;
import com.codedleaf.sylveryte.myemployeeattendanceregister.Labs.SitesLab;

import org.joda.time.DateTime;

import java.util.Currency;
import java.util.Locale;
import java.util.UUID;

/**
 * Created by sylveryte on 5/8/16.
 * <p>
 * Copyright (C) 2016 sylveryte@codedleaf <codedleaf@gmail.com>
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

    public void setSiteId(UUID siteId) {
        mSiteId = siteId;
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

    public String getSiteString(Context context)
    {
        if (getSiteId()!=null)
            return " ";

        Site site= SitesLab.getInstanceOf(context).getSiteById(getSiteId());
        if (site==null)
        {
            return " ";
        }else return site.getTitle();
    }

    public int getAmount() {
        return mAmount;
    }

    public String getAmountString()
    {
         return String.valueOf(getAmount())+ CodedleafTools.getCurrencySymbol(Currency.getInstance(Locale.getDefault()).getCurrencyCode());
    }

    public String getNote() {
        return mNote;
    }

    public DateTime getDate() {
        return mDate;
    }

    public String getDateTimeString()
    {
        return CodedleafTools.getPrettyDateString(mDate);
    }

    public void updateMe(Context context)
    {
        MoneyLab.getInstanceOf(context).updateMoney(this);
    }
}
