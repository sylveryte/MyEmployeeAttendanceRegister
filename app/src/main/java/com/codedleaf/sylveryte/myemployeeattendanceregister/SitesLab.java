package com.codedleaf.sylveryte.myemployeeattendanceregister;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

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

    private Context mContext;
    private SQLiteDatabase mDatabase;



    private SitesLab(Context context)
    {
        mContext=context.getApplicationContext();
        mDatabase=AttendanceBaseHelper.getDatabaseWritable(mContext);

        mSites=new ArrayList<>();
        mLabObservers=new ArrayList<>();

    }

    public static SitesLab getInstanceOf(Context context)
    {
        if(sLab==null)
        {
            sLab=new SitesLab(context);

        }

        return sLab;
    }

    public void addSite(Site site)
    {
        mSites.add(site);
        alertAllObservers();
    }
    public void deleteSite(Site site)
    {
        site.delete();
        mSites.remove(site);
        alertAllObservers();
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

    public List<Employee> getCurrentEmployeesInSiteBySiteId(UUID siteId)
    {
        List<Employee> employees=new ArrayList<>();
        List<UUID> employeeIds=getSiteById(siteId).getEmployeesInvolved();
        EmployeeLab employeeLab=EmployeeLab.getInstanceOf(mContext);

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
}
