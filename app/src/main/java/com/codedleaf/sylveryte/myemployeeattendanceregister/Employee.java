package com.codedleaf.sylveryte.myemployeeattendanceregister;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.jar.Pack200;

/**
 * Created by sylveryte on 12/6/16.
 */
public class Employee {



    Boolean mIsMale;
    String mName;
    int mAge;
    double mSalary;
    String mAdress;
    String mNote;
    List<UUID> mDesignations;
    List<UUID> mSites;
    Boolean mIsActive;
    UUID mEployeeId;

    public Employee()
    {
        mIsMale=true;
        mIsActive =true;
        mEployeeId=UUID.randomUUID();
        mDesignations =new ArrayList<>();
        mSites=new ArrayList<>();
    }

    public void addDesignation(Designation designation)
    {
        mDesignations.add(designation.getDesignationId());
        //Add into the table code her
        // TODO: 12/6/16 table insert code here
        //use update for remove and this too
    }
    public String getDesignationString()
    {
        return "no Desg lab :p";
    }

    public void addSite(Site site)
    {
        mSites.add(site.getSiteId());
        //Add into the table code her
        // TODO: 14/6/16 table insert code here
        //use update for remove and this too
    }
    public String getSiteString()
    {
        if(!mSites.isEmpty())
        {
            SitesLab sitesLab=SitesLab.getInstanceOf();
            String sites=sitesLab.getSiteNameById(mSites.get(0));
            for (int i=1;i<mSites.size();i++)
            {
                sites=","+SitesLab.getInstanceOf().getSiteNameById(mSites.get(i));
            }
            return sites;
        }
        return "No sites Assigned";
    }

    public void removeDesignation(Designation designation )
    {
        mDesignations.remove(designation.getDesignationId());

        // TODO: 12/6/16 table deletion code here

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

    public void setAge(int age) {
        mAge = age;
    }

    public double getSalary() {
        return mSalary;
    }

    public void setSalary(double salary) {
        mSalary = salary;
    }

    public String getAdress() {
        return mAdress;
    }

    public void setAdress(String adress) {
        mAdress = adress;
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

    public UUID getEployeeId() {
        return mEployeeId;
    }

}
