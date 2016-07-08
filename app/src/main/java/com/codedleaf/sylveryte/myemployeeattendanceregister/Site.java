package com.codedleaf.sylveryte.myemployeeattendanceregister;

import android.content.Context;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by sylveryte on 12/6/16.
 */
public class Site implements Pickable {


    private String mTitle;
    private String mDescription;

    private LocalDate mBeginDate;
    private LocalDate mFinishedDate;

    private UUID mSiteId;

    private List<UUID> mEmployeesInvolved;

    private Context mContext;

    public Site(Context context)
    {
        mContext=context;

        mEmployeesInvolved=new ArrayList<>();
        mBeginDate = new LocalDate();
        mSiteId = UUID.randomUUID();

    }

    public Site(Context context,UUID uuid)
    {
        mContext=context;
        mSiteId=uuid;

        mEmployeesInvolved=new ArrayList<>();
        mBeginDate = new LocalDate();
    }

    public String getTitle() {
        return mTitle;
    }




    public void setTitle(String title) {
        mTitle = title;
    }

    public String getDescription() {
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


    @Override
    public UUID getId() {
        return mSiteId;
    }










    public void delete()
    {
        EmployeeLab lab=EmployeeLab.getInstanceOf(mContext);
        for (UUID uuid:mEmployeesInvolved)
        {
            Employee employee=lab.getEmployeeById(uuid);
            if (employee!=null)
            {
                employee.removeSiteByid(mSiteId);
            }
        }

        EntryLab.getInstanceOf(mContext).cleanseEntriesOfSiteId(mSiteId);

    }

    public void addEmployeeById(UUID empId)
    {
        if (mEmployeesInvolved.contains(empId))
            return;
        mEmployeesInvolved.add(empId);

        Employee employee=EmployeeLab.getInstanceOf(mContext).getEmployeeById(empId);

        if (employee==null)
            return;
        employee.addSiteById(mSiteId);
    }

    public void removeEmployeeById(UUID uuid)
    {
        if (!mEmployeesInvolved.contains(uuid))
            return;
        mEmployeesInvolved.remove(uuid);
    }
}
