package com.codedleaf.sylveryte.myemployeeattendanceregister;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;

import com.codedleaf.sylveryte.myemployeeattendanceregister.AttendanceDbSchema.SitesTable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


/**
 * Created by sylveryte on 12/6/16.
 */
public class SitesLab implements LabObeservable {

    public static SitesLab sLab;

    private List<Site> mSites;
    private List<LabObserver> mLabObservers;

    private SQLiteDatabase mDatabase;



    private SitesLab(Context context)
    {

        mDatabase=AttendanceBaseHelper.getDatabaseWritable(context);

        mSites=new ArrayList<>();
        mLabObservers=new ArrayList<>();

    }

    private void initializeDatabase()
    {
        SiteCursorWrapper cursorWrapper=querySites(null,null);
        try
        {
            cursorWrapper.moveToFirst();
            while (!cursorWrapper.isAfterLast())
            {
                mSites.add(cursorWrapper.getSite());
                cursorWrapper.moveToNext();
            }
        }
        finally
        {
            cursorWrapper.close();
        }
    }

    public static SitesLab getInstanceOf(Context context)
    {
        if(sLab==null)
        {
            sLab=new SitesLab(context);
            sLab.initializeDatabase();

        }

        return sLab;
    }

    public void addSite(Site site)
    {
        if (mSites.contains(site))
            return;

        mSites.add(site);
        alertAllObservers();

        ContentValues values= AttendanceDbToolsProvider.getContentValues(site);
        mDatabase.insert(SitesTable.NAME,null,values);
    }
    public void deleteSite(Site site,Context context)
    {
        site.delete(context);
        mSites.remove(site);
        alertAllObservers();

        String siteIdString=site.getId().toString();
        mDatabase.delete(SitesTable.NAME, SitesTable.Cols.UID+"=?",new String[]{siteIdString});
    }

    public void updateSite(Site site)
    {
        alertAllObservers();
        String siteIdString=site.getId().toString();
        ContentValues values= AttendanceDbToolsProvider.getContentValues(site);
        mDatabase.update(SitesTable.NAME,values, SitesTable.Cols.UID+"=?",new String[]{siteIdString});
    }

    public Site getSiteById(UUID uuid)
    {
        for(Site site:mSites)
        {
            if (site.getId().equals(uuid))
                return site;
        }
        return null;
    }

    public String getSiteStringById(UUID uuid)
    {
        return getSiteById(uuid).getTitle();
    }

    public List<Employee> getCurrentEmployeesInSiteBySiteId(UUID siteId,Context context)
    {
        List<Employee> employees=new ArrayList<>();
        List<UUID> employeeIds=getSiteById(siteId).getEmployeesInvolved();
        EmployeeLab employeeLab=EmployeeLab.getInstanceOf(context);

        for (UUID uuid:employeeIds)
        {
            Employee employee=employeeLab.getEmployeeById(uuid);
            employees.add(employee);
        }
        return employees;
    }

    public List<Site> getSites() {
        return mSites;
    }

    public void addListener(LabObserver labObserver)
    {
        mLabObservers.add(labObserver);
    }

    public void alertAllObservers()
    {
        for (LabObserver labObserver :mLabObservers)
            labObserver.update();
    }


    //db

    private SiteCursorWrapper querySites(String whereClause, String[] wherwArgs)
    {
        Cursor cursor=mDatabase.query(
                SitesTable.NAME,
                null, //columns null coz select all columns :)
                whereClause,
                wherwArgs,
                null, //group by
                null, //having
                null //orderby
        );

        return new SiteCursorWrapper(cursor);

    }

    private  class SiteCursorWrapper extends CursorWrapper
    {
        public SiteCursorWrapper(Cursor cursor)
        {
            super(cursor);
        }

        public Site getSite()
        {
            String uuidString=getString(getColumnIndex(SitesTable.Cols.UID));
            String description=getString(getColumnIndex(SitesTable.Cols.DESC));
            String title=getString(getColumnIndex(SitesTable.Cols.TITLE));
            String active=getString(getColumnIndex(SitesTable.Cols.ACTIVE));

            String beginDate=getString(getColumnIndex(SitesTable.Cols.BEGINDATE));
            String endDate=getString(getColumnIndex(SitesTable.Cols.ENDDATE));

            Site site=new Site(UUID.fromString(uuidString));
            site.setTitle(title);
            site.setActive(CodedleafTools.getBooleanFromString(active));
            site.setDescription(description);
            site.setBeginDate(CodedleafTools.getLocalDateFromString(beginDate));
            site.setFinishedDate(CodedleafTools.getLocalDateFromString(endDate));

            return site;
        }
    }
}
