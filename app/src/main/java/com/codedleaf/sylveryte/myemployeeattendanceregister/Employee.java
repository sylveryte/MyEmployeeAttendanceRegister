package com.codedleaf.sylveryte.myemployeeattendanceregister;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.jar.Pack200;

/**
 * Created by sylveryte on 12/6/16.
 */
public class Employee implements Pickable{



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

    public void addDesignation(UUID desigantionid)
    {
        mDesignations.add(desigantionid);

    }
    public String getDesignationString()
    {

        //// TODO: 18/6/16 clean this shit we should not need these two (site String) methods

        if(!mDesignations.isEmpty())
        {
            DesignationLab designationLab=DesignationLab.getInstanceOf();
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

    public void addSite(UUID siteid)
    {
        mSites.add(siteid);
        //Add into the table code her
        // TODO: 14/6/16 table insert code here
        //use update for remove and this too
    }
    public String getSiteString()
    {
        if(!mSites.isEmpty())
        {
            SitesLab sitesLab=SitesLab.getInstanceOf();
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
        return getMaleFemaleString()+" "+getAge()+ " "+getDesignationString();
    }

    public  String getMaleFemaleString()
    {
        return isMale()?"Male":"Female";
    }

    public void removeDesignation(Designation designation )
    {
        mDesignations.remove(designation.getId());

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
    public String getAgeString()
    {
        return mAge+"";
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

    @Override
    public UUID getId() {
        return mEployeeId;
    }
}
