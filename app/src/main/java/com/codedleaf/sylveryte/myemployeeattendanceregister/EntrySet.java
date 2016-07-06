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

    public EntrySet(UUID siteId, Context context)
    {
        mSiteId=siteId;
        mDate=new LocalDate();
        mEntries=new ArrayList<>();
        initializeEntries(context);
    }

    private void initializeEntries(Context context)
    {
        List<Employee> employees=SitesLab.getInstanceOf(context).getCurrentEmployeesInSiteBySiteId(mSiteId);
        for (Employee employee: employees)
        {
            Entry entry=new Entry(employee.getId(),mSiteId);
            mEntries.add(entry);
        }
    }

    public LocalDate getDate() {
        return mDate;
    }

    public void setDate(LocalDate date) {
        mDate = date;
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

    public void update()
    {
        //// TODO: 6/7/16 update db from here
    }

    public List<Entry> getEntries() {
        return mEntries;
    }
}

