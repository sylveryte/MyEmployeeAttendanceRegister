package com.codedleaf.sylveryte.myemployeeattendanceregister;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by sylveryte on 11/7/16.
 */
public class SiteStatFragment extends Fragment {

    private Site mSite;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.site_stat,container,false);




        //doesnt work why?
        getActivity().setTitle(mSite.getTitle());

        return view;
    }

    private void setSite(Site site) {
        mSite = site;
    }

    public static SiteStatFragment createInstance(Site site)
    {
        SiteStatFragment siteStatFragment=new SiteStatFragment();
        siteStatFragment.setSite(site);
        return siteStatFragment;
    }
}
