package com.codedleaf.sylveryte.myemployeeattendanceregister;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by sylveryte on 12/6/16.
 */
public class Employee implements Pickable{



    private int mAge;
    private double mSalary;

    private String mName;

    private String mAdress;
    private String mNote;

    private Boolean mIsMale;
    private Boolean mIsActive;

    private UUID mEployeeId;

    private List<UUID> mDesignations;
    private List<UUID> mSites;

    public Employee()
    {
        mIsMale=true;
        mIsActive =true;
        mEployeeId=UUID.randomUUID();
        mDesignations =new ArrayList<>();
        mSites=new ArrayList<>();
    }


    public List<UUID> getDesignations()
    {
        return mDesignations;
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




    public List<UUID> getSites()
    {
        return mSites;
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
        return getMaleFemaleString()+" "+getAge();
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






    public void delete()
    {
        DesignationLab designationLab=DesignationLab.getInstanceOf();
        SitesLab sitesLab=SitesLab.getInstanceOf();

        //remove from desgs
        for (UUID uuid:mDesignations)
        {
            Designation designation=designationLab.getDesigantionById(uuid);
            if (designation!=null)
            {
                designation.removeEmployeeInvolvedById(uuid);
            }
        }

        //remove from sitee
        for (UUID uuid:mSites)
        {
            Site site=sitesLab.getSiteById(uuid);
            if(site!=null)
            {
                site.removeEmployeeById(uuid);
            }
        }

        EntryLab.getInstanceOf().cleanseEntriesOfEmployeeId(mEployeeId);
    }

    public void addSiteById(UUID siteid)
    {
        if(mSites.contains(siteid))
            return;
        mSites.add(siteid);

        Site site=SitesLab.getInstanceOf().getSiteById(siteid);
        site.addEmployeeById(mEployeeId);
    }


    public void removeSiteByid(UUID uuid)
    {
        if (!mSites.contains(uuid))
            return;
        mSites.remove(uuid);
    }

    public void addDesignationById(UUID desigantionid)
    {
        if (mDesignations.contains(desigantionid))
            return;
        mDesignations.add(desigantionid);

        Designation designation=DesignationLab.getInstanceOf().getDesigantionById(desigantionid);
        designation.addEmployeeInvolvedById(mEployeeId);
    }
    public void removeDesignationById(UUID uuid)
    {
        if (!mDesignations.contains(uuid))
            return;
        mDesignations.remove(uuid);
    }

}
