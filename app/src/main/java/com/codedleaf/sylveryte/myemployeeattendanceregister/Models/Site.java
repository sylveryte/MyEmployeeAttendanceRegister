package com.codedleaf.sylveryte.myemployeeattendanceregister.Models;

import android.content.Context;

import com.codedleaf.sylveryte.myemployeeattendanceregister.Labs.EmployeeLab;
import com.codedleaf.sylveryte.myemployeeattendanceregister.Labs.EntryLab;
import com.codedleaf.sylveryte.myemployeeattendanceregister.Labs.SitesLab;
import com.codedleaf.sylveryte.myemployeeattendanceregister.Picknation.PickCache;
import com.codedleaf.sylveryte.myemployeeattendanceregister.Picknation.PickDialogObserver;
import com.codedleaf.sylveryte.myemployeeattendanceregister.Picknation.Pickable;
import com.codedleaf.sylveryte.myemployeeattendanceregister.RegisterConstants;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by sylveryte on 12/6/16.
 *
 * Copyright (C) 2016 sylveryte@codedleaf <codedlaf@gmail.com>
 *
 * This file is part of My Employee Attendance Register.
 *
 */
public class Site implements Pickable,PickDialogObserver {


    private String mTitle;
    private String mDescription;

    private LocalDate mBeginDate;
    private LocalDate mFinishedDate;

    private boolean mActive;

    private UUID mSiteId;

    private List<UUID> mEmployeesInvolved;

    @Override
    public int getType() {
        return RegisterConstants.SITE;
    }

    public Site()
    {
        mEmployeesInvolved=new ArrayList<>();
        mBeginDate = new LocalDate();
        mSiteId = UUID.randomUUID();
        mActive=true;
    }

    public Site(UUID uuid)
    {
        mSiteId=uuid;

        mEmployeesInvolved=new ArrayList<>();
        mBeginDate = new LocalDate();
    }

    public String getTitle() {
        return mTitle;
    }

    public boolean isActive() {
        return mActive;
    }

    public void setActive(boolean active) {
        mActive = active;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getDescription() {
        return getEmployeeCountString()+"\n"+mDescription;
    }
    public String getEmployeeCountString()
    {
        return getEmployeesInvolved().size()+" Employees";
    }
    public String getDescriptionPure() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public LocalDate getBeginDate() {
        return mBeginDate;
    }

    public void setBeginDate(LocalDate beginDate) {
        mBeginDate = beginDate;
    }

    public LocalDate getFinishedDate() {
        return mFinishedDate;
    }

    public void setFinishedDate(LocalDate finishedDate) {
        mFinishedDate = finishedDate;
    }

    public List<UUID> getEmployeesInvolved() {
        return mEmployeesInvolved;
    }

    //for chart
    public float getFloat(LocalDate month,int remark, Context context)
    {
        List<Entry> entriesTotal= EntryLab.getInstanceOf(context)
                .getEntries(null,month.getMonthOfYear(),month.getYear(),null,mSiteId,null);

        List<Entry> entries=EntryLab.getInstanceOf(context)
                .getEntries(null,month.getMonthOfYear(),month.getYear(),remark,mSiteId,null);

        if (entriesTotal==null||entries==null)
            return 0;
        return (float)100*entries.size()/entriesTotal.size();
    }

    public float getDesgPercent(UUID desgId,Context context)
    {
        int i=0;
        for (Pickable pickable: EmployeeLab.getInstanceOf(context).getPickables(getEmployeesInvolved()))
        {
            Employee employee=(Employee)pickable;
            if (employee.getDesignations().contains(desgId))
            {
                i++;
            }
        }
        if (i==0||getEmployeesInvolved().size()==0)
            return 0;
        return (float)100*i/getEmployeesInvolved().size();
    }

    @Override
    public UUID getId() {
        return mSiteId;
    }

    public void delete(Context context)
    {
        EmployeeLab lab=EmployeeLab.getInstanceOf(context);
        for (UUID uuid:mEmployeesInvolved)
        {
            Employee employee=lab.getEmployeeById(uuid);
            if (employee!=null)
            {
                employee.removeSiteByid(mSiteId,context);

            }
        }

        EntryLab.getInstanceOf(context).cleanseEntriesOfSiteId(mSiteId);
    }

    public void addEmployeeById(UUID empId,Context context)
    {
        if (mEmployeesInvolved.contains(empId))
            return;
        mEmployeesInvolved.add(empId);

        Employee employee=EmployeeLab.getInstanceOf(context).getEmployeeById(empId);

        if (employee!=null)
            employee.addSiteById(mSiteId,context);
    }

    public void removeEmployeeById(UUID uuid,Context context)
    {
        if (!mEmployeesInvolved.contains(uuid))
            return;
        mEmployeesInvolved.remove(uuid);
        Employee employee=EmployeeLab.getInstanceOf(context).getEmployeeById(uuid);
        //fucking cant believe it below was remove designation
        employee.removeSiteByid(getId(),context);
    }

    public void updateMyDB(Context context)
    {
        SitesLab.getInstanceOf(context).updateSite(this);
    }

    @Override
    public void doSomeUpdate(Context context) {
        for (UUID uuid: PickCache.getInstance().getPicked(getId().toString()))
        {
            addEmployeeById(uuid,context);
        }
        List<UUID> list=PickCache.getInstance().getRemoved(getId().toString());
        for (UUID uuid:list)
        {
            removeEmployeeById(uuid,context);
        }
        updateMyDB(context);
    }
}
