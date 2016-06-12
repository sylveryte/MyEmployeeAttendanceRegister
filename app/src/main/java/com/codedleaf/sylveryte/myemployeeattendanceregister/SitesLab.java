package com.codedleaf.sylveryte.myemployeeattendanceregister;

import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by sylveryte on 12/6/16.
 */
public class SitesLab {

    public static SitesLab sLab;

    List<Site> mSites;

    private SitesLab()
    {
            mSites=new ArrayList<>();
    }

    public static SitesLab getInstanceOf()
    {
        if(sLab==null)
        {
            sLab=new SitesLab();

        }

        if (sLab!=null)
        {
            Entry e=new Entry();
        }

        return sLab;
    }

    public void addSite(Site site)
    {
        mSites.add(site);
    }

    //// TODO: 12/6/16 replace this with global iterator
    public Site getASite()
    {
        Random random=new Random();
        return mSites.get(random.nextInt(mSites.size()));
    }

}
