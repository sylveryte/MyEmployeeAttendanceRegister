package com.codedleaf.sylveryte.myemployeeattendanceregister;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by sylveryte on 11/6/16.
 */
public class SitesFragment extends Fragment implements LabObservable {


    SitesLab mSitesLab;
    List<Site> mSites;
    View mView;
    RecyclerView mSitesRecyclerView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        mSitesLab=SitesLab.getInstanceOf();
        mSitesLab.addListener(this);
        mSites=mSitesLab.getSites();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mView=inflater.inflate(R.layout.sites_fragment,container,false);

        mSitesRecyclerView=(RecyclerView)mView.findViewById(R.id.site_fragment_recycler_view);
        mSitesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        //// TODO: 12/6/16 clean all this too :)
        updateView();

        return mView;
    }

    private class SiteHolder extends RecyclerView.ViewHolder
    {
        public

        public SiteHolder(View itemView) {
            super(itemView);

        }
    }

    private void updateView() {

        //// TODO: 12/6/16 we probably dont need this get rid of it
        TextView siteTitle=(TextView)mView.findViewById(R.id.textView_site_title);

        Site site=mSites.get(mSites.size()-1);

        siteTitle.setText(site.getTitle());
    }

    @Override
    public void update() {
        updateView();
    }

    public static Fragment newInstance()
    {
        return new SitesFragment();
    }


}
