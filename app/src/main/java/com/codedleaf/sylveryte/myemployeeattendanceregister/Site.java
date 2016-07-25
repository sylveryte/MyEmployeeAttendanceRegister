package com.codedleaf.sylveryte.myemployeeattendanceregister;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

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
        return getEmployeesInvolved().size()+" Employees\n"+mDescription;
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
        List<Entry> entries=EntryLab.getInstanceOf(context)
                .getEntries(null,month.getMonthOfYear(),month.getYear(),remark,mSiteId,null);

        if (entries!=null)
        {
            int total=entries.size();
            int counted=0;

            for (Entry entry: entries)
            {
                if (entry.getRemark()==remark)
                    counted++;
            }
            return (float)counted/total;
        }
        return 0.0f;
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
        {
            mEmployeesInvolved.remove(empId);
            updateMyDB(context);
            return;
        }
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
        updateMyDB(context);
    }

    public void updateMyDB(Context context)
    {
        SitesLab.getInstanceOf(context).updateSite(this);
    }

    @Override
    public void doSomeUpdate(Context context) {
        for (UUID uuid:PickCache.getInstance().getPicked(getId().toString()))
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
