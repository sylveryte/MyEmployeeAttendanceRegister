package com.codedleaf.sylveryte.myemployeeattendanceregister;

import android.content.Context;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by sylveryte on 12/6/16.
 */
public class Site implements Pickable,DialogPickObserver {


    private String mTitle;
    private String mDescription;

    private LocalDate mBeginDate;
    private LocalDate mFinishedDate;

    private boolean mActive;

    private UUID mSiteId;

    private List<UUID> mEmployeesInvolved;


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
        updateMyDB(context);

        Employee employee=EmployeeLab.getInstanceOf(context).getEmployeeById(empId);

        if (employee==null)
            return;
        employee.addSiteById(mSiteId,context);
    }

    public void removeEmployeeById(UUID uuid,Context context)
    {
        if (!mEmployeesInvolved.contains(uuid))
            return;
        mEmployeesInvolved.remove(uuid);
        updateMyDB(context);
    }

    public void updateMyDB(Context context)
    {
        SitesLab.getInstanceOf(context).updateSite(this);
    }

    @Override
    public void doSomeUpdate(Context context) {
        for (UUID uuid:PickCache.getInstance().getPickables(getId().toString()))
        {
            addEmployeeById(uuid,context);
        }
        PickCache.getInstance().destroyMyCache(getId().toString());
        updateMyDB(context);
    }
}
