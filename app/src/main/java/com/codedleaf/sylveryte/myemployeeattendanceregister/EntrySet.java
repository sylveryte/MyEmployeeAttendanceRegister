package com.codedleaf.sylveryte.myemployeeattendanceregister;

import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by sylveryte on 27/6/16.
 */
public class EntrySet {

    private UUID mSiteId;
    private Date mDate;
    private List<Entry> mEntries;

    public EntrySet(UUID siteId)
    {
        mSiteId=siteId;
        mDate=new Date();
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

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public UUID getSiteId() {
        return mSiteId;
    }

    public List<Entry> getEntries() {
        initializeEntries();

        return mEntries;
    }
}

