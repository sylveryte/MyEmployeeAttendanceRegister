package com.codedleaf.sylveryte.myemployeeattendanceregister;

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



    private SitesLab()
    {
        mSites=new ArrayList<>();
        mLabObservers=new ArrayList<>();


        //// TODO: 12/6/16 get rid of this fake db
        for (int i=1;i<25;i++)
        {
            Site site=new Site();
            site.setTitle("Site "+i*763);
            site.setDescription("codedleaf bc!");
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

    public String getSiteNameById(UUID uuid)
    {
        for (Site site: mSites)
        {
            if (site.getSiteId().equals(uuid))
            {
                return site.getTitle();
            }
        }
        return "No sites Assigned";
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
