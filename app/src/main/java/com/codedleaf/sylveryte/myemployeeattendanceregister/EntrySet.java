package com.codedleaf.sylveryte.myemployeeattendanceregister;

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

    public EntrySet(UUID siteId)
    {
        mSiteId=siteId;
        mDate=new LocalDate();
        initializeEntries();
    }

    private void initializeEntries()
    {
        mEntries=new ArrayList<>();

        List<Employee> employees=SitesLab.getInstanceOf().getCurrentEmployeesInSiteBySiteId(mSiteId);
        for (Employee employee: employees)
        {
            Entry entry=new Entry(employee.getId());
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


    public List<Entry> getEntries() {
        return mEntries;
    }
}

