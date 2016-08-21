package com.codedleaf.sylveryte.myemployeeattendanceregister.Models;

import android.content.Context;

import com.codedleaf.sylveryte.myemployeeattendanceregister.Labs.DesignationLab;
import com.codedleaf.sylveryte.myemployeeattendanceregister.Labs.EmployeeLab;
import com.codedleaf.sylveryte.myemployeeattendanceregister.Labs.EntryLab;
import com.codedleaf.sylveryte.myemployeeattendanceregister.Labs.MoneyLab;
import com.codedleaf.sylveryte.myemployeeattendanceregister.Labs.SitesLab;
import com.codedleaf.sylveryte.myemployeeattendanceregister.Picknation.PickCache;
import com.codedleaf.sylveryte.myemployeeattendanceregister.GeneralObserver;
import com.codedleaf.sylveryte.myemployeeattendanceregister.Picknation.Pickable;
import com.codedleaf.sylveryte.myemployeeattendanceregister.RegisterConstants;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by sylveryte on 12/6/16.
 *
 * Copyright (C) 2016 sylveryte@codedleaf <codedleaf@gmail.com>
 *
 * This file is part of My Employee Attendance Register.
 *
 */
public class Employee implements Pickable,GeneralObserver {



    private int mAge;

    private String mName;

    private String mAddress;
    private String mNote;

    private Boolean mIsMale;
    private Boolean mIsActive;

    private UUID mEmployeeId;

    private List<UUID> mDesignations;
    private List<Designation> mDesignationsActual;
    private String mDesignationsString="";
    private List<UUID> mSites;

    @Override
    public int getType() {
        return RegisterConstants.EMPLOYEE;
    }

    public Employee()
    {

        mIsMale=true;
        mIsActive =true;
        mEmployeeId =UUID.randomUUID();
        mDesignations =new ArrayList<>();
        mSites=new ArrayList<>();
    }

    public Employee(UUID employeeId)
    {
        mEmployeeId=employeeId;
    }

    public List<UUID> getDesignations()
    {
        return mDesignations;
    }

    public void setSites(List<UUID> sites,Context context) {
        mSites=new ArrayList<>();
        for (UUID uuid:sites)
        {
            addSiteById(uuid,context);
        }
    }

    public void setDesignations(List<UUID> designations,Context context) {
        mDesignations=new ArrayList<>();
        for (UUID uuid:designations)
        {
            addDesignationById(uuid,context);
        }
    }

    public String getDesignationString(Context context)
    {

        //// TODO: 18/6/16 clean this shit we should not need these two (site String) methods

        if(!mDesignations.isEmpty())
        {
            DesignationLab designationLab=DesignationLab.getInstanceOf(context);
            Designation designation=designationLab.getDesigantionById(mDesignations.get(0));
            if (designation==null)
            {
                return "no Designation Assigned";
            }
            String desgnations=designation.getTitle();
            if (mDesignations.size()>1)
            {
                for (int i=1;i<mDesignations.size();i++)
                {
                    desgnations+=","+designationLab.getDesigantionById(mDesignations.get(i)).getTitle();
                }
            }
            return desgnations;
        }
        return "no Designation Assigned";
    }

    public List<UUID> getSites()
    {
        return mSites;
    }

    public String getSiteString(Context context)
    {
        if(!mSites.isEmpty())
        {
            SitesLab sitesLab=SitesLab.getInstanceOf(context);
            Site site=sitesLab.getSiteById(mSites.get(0));

            if (site==null)
            {
                return "No sites Assigned";
            }
            String sites=site.getTitle();
            if (mSites.size()>1)
            {
                for (int i=1;i<mSites.size();i++)
                {
                    sites+=","+sitesLab.getSiteById(mSites.get(i)).getTitle();
                }
            }
            return sites;
        }
        return "No sites Assigned";
    }

    @Override
    public String getTitle() {
        return getName();
    }

    @Override
    public String getDescription() {
        return getMaleFemaleString()+" "+getAge()+"\n"+mDesignationsString;
    }

    public  String getMaleFemaleString()
    {
        return isMale()?"Male":"Female";
    }



    public Boolean isMale() {
        return mIsMale;
    }

    public void setMale(Boolean isMale) {
        mIsMale = isMale;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public int getAge() {
        return mAge;
    }
    public String getAgeString()
    {
        return mAge+"";
    }

    public void setAge(int age) {
        mAge = age;
    }

    public String getAddress() {
        return mAddress;
    }

    public void setAddress(String address) {
        mAddress = address;
    }

    public String getNote() {
        return mNote;
    }

    public void setNote(String note) {
        mNote = note;
    }

    public Boolean isActive() {
        return mIsActive;
    }

    public void setActive(Boolean isActive) {
        mIsActive = isActive;
    }

    @Override
    public UUID getId() {
        return mEmployeeId;
    }

    public void delete(Context context)
    {
        DesignationLab designationLab=DesignationLab.getInstanceOf(context);
        SitesLab sitesLab=SitesLab.getInstanceOf(context);

        //remove from desgs
        for (UUID uuid:mDesignations)
        {
            Designation designation=designationLab.getDesigantionById(uuid);
            if (designation!=null)
            {
                designation.removeEmployeeInvolvedById(mEmployeeId,context);
            }
        }

        //remove from sitee
        for (UUID uuid:mSites)
        {
            Site site=sitesLab.getSiteById(uuid);
            if(site!=null)
            {
                site.removeEmployeeById(mEmployeeId,context);
            }
        }

        MoneyLab.getInstanceOf(context).cleanseMoneyLogOfEmployeeId(mEmployeeId);
        EntryLab.getInstanceOf(context).cleanseEntriesOfEmployeeId(mEmployeeId);
    }

    public void addSiteById(UUID siteid,Context context)
    {
        if(mSites.contains(siteid))
            return;
        mSites.add(siteid);
        updateMyDB(context);

        Site site=SitesLab.getInstanceOf(context).getSiteById(siteid);
        if (site==null)
        {
            mSites.remove(siteid);
            updateMyDB(context);
            return;
        }
        site.addEmployeeById(mEmployeeId,context);
    }


    public void removeSiteByid(UUID uuid,Context context)
    {
        if (!mSites.contains(uuid))
            return;
        mSites.remove(uuid);
        Site site=SitesLab.getInstanceOf(context).getSiteById(uuid);
        if (site!=null)
            site.removeEmployeeById(getId(),context);
        updateMyDB(context);
    }

    public void addDesignationById(UUID desigantionid,Context context)
    {

        if (mDesignations.contains(desigantionid))
            return;
        mDesignations.add(desigantionid);
        mDesignationsString=getDesignationString(context);
        updateMyDB(context);

        Designation designation=DesignationLab.getInstanceOf(context).getDesigantionById(desigantionid);
        if (designation==null)
        {
            mDesignations.remove(desigantionid);
            updateMyDB(context);
            return;
        }
        designation.addEmployeeInvolvedById(mEmployeeId,context);
    }
    public void removeDesignationById(UUID uuid,Context context)
    {
        if (!mDesignations.contains(uuid))
            return;
        mDesignations.remove(uuid);
        mDesignationsString=getDesignationString(context);
        Designation designation=DesignationLab.getInstanceOf(context).getDesigantionById(uuid);
        if (designation!=null)
            designation.removeEmployeeInvolvedById(getId(),context);

        updateMyDB(context);
    }

    @Override
    public void doSomeUpdate(Context context) {


        String desgedId=getId().toString()+"d";
        for (UUID uuid: PickCache.getInstance().getPicked(desgedId))
        {
            addDesignationById(uuid,context);
        }
        for (UUID uuid:PickCache.getInstance().getRemoved(desgedId))
        {
            removeDesignationById(uuid,context);
        }

        String sitedId=getId().toString()+"s";
        for (UUID uuid:PickCache.getInstance().getPicked(sitedId))
        {
            addSiteById(uuid,context);
        }
        for (UUID uuid:PickCache.getInstance().getRemoved(sitedId))
        {
            removeSiteByid(uuid,context);
        }

        updateMyDB(context);
        EmployeeLab.getInstanceOf(context).alertAllObservers();
    }

    public void updateMyDB(Context context)
    {
        EmployeeLab.getInstanceOf(context).updateEmployee(this);
    }

}
