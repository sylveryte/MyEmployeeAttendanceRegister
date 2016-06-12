package com.codedleaf.sylveryte.myemployeeattendanceregister;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by sylveryte on 12/6/16.
 */
public class SitesLab {

    public static SitesLab sLab;

    List<Site> mSites;
    List<LabObservable> mObserverables;


    private SitesLab()
    {
        mSites=new ArrayList<>();
        mObserverables=new ArrayList<>();


        //// TODO: 12/6/16 get rid of this fake db
        for (int i=42;i<200;i++)
        {
            Site site=new Site();
            site.setTitle("Site "+i);
            mSites.add(site);
        }

    }

    public static SitesLab getInstanceOf()
    {
        if(sLab==null)
        {
            sLab=new SitesLab();

        }

        return sLab;
    }

    public void addSite(Site site)
    {
        mSites.add(site);
        alertAllObservers();
    }


    public void addListener(LabObservable labObservable)
    {
        mObserverables.add(labObservable);
    }

    private void alertAllObservers()
    {
        for (LabObservable labObservable :mObserverables)
            labObservable.update();
    }

    public List<Site> getSites() {
        return mSites;
    }
}
