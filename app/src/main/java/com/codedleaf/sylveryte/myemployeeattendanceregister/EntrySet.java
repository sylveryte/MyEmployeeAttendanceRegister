package com.codedleaf.sylveryte.myemployeeattendanceregister;

import android.content.Context;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by sylveryte on 27/6/16.
 */
public class EntrySet {

    private UUID mSiteId;
    private LocalDate mDate;
    private List<Entry> mEntries;


    public EntrySet(UUID siteId,LocalDate date)
    {
        mSiteId=siteId;
        mDate=date;
    }

    public void startEntriesProcess(Context context)
    {
        //must be called once :/

        mEntries=EntryLab.getInstanceOf(context).getEntries(mDate,mSiteId);

        if (mEntries==null)
            initializeEntries(context);
    }

    private void initializeEntries(Context context)
    {
        mEntries=new ArrayList<>();
        List<Employee> employees=SitesLab.getInstanceOf(context).getCurrentEmployeesInSiteBySiteId(mSiteId,context);
        for (Employee employee: employees)
        {
            if (!employee.isActive())
                continue;
            Entry entry=new Entry(employee.getId(),mSiteId);
            mEntries.add(entry);
        }

        EntryLab.getInstanceOf(context).adddEntrySetToDatabase(this);
    }

    public LocalDate getDate() {
        return mDate;
    }

    public UUID getSiteId() {
        return mSiteId;
    }

    public void cleanseEntriesOfEmployee(UUID empId)
    {
        for (Entry entry: mEntries)
        {
            if (entry.getEmployeeId().equals(empId))
            {
                mEntries.remove(entry);
            }
        }
    }


    public List<Entry> getEntries() {

        if (mEntries==null)
            mEntries=new ArrayList<>();

        return mEntries;

    }
}

