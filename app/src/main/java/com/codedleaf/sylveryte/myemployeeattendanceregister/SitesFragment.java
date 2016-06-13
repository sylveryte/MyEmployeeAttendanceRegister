package com.codedleaf.sylveryte.myemployeeattendanceregister;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
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
    View mView;
    RecyclerView mSitesRecyclerView;
    SiteAdapter mSiteAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        mSitesLab=SitesLab.getInstanceOf();
        mSitesLab.addListener(this);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mView=inflater.inflate(R.layout.sites_fragment,container,false);

        mSitesRecyclerView=(RecyclerView)mView.findViewById(R.id.site_fragment_recycler_view);
        mSitesRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        mSiteAdapter=new SiteAdapter(mSitesLab.getSites());
        mSitesRecyclerView.setAdapter(mSiteAdapter);

        return mView;
    }


    private class SiteAdapter extends RecyclerView.Adapter<SiteHolder>
    {
        List<Site> mLocalSites;

        public SiteAdapter(List<Site> sites)
        {
            mLocalSites=sites;
        }
        @Override
        public SiteHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater=getActivity().getLayoutInflater();
            View view=inflater.inflate(R.layout.list_view_card,parent,false);

            return new SiteHolder(view);
        }

        @Override
        public void onBindViewHolder(SiteHolder holder, int position) {
            holder.bind(mLocalSites.get(position));
        }

        @Override
        public int getItemCount() {
            return mLocalSites.size();
        }
    }

    private class SiteHolder extends RecyclerView.ViewHolder
    {
        TextView title;
        TextView description;

        public SiteHolder(View itemView) {
            super(itemView);

            title=(TextView)itemView.findViewById(R.id.list_title);
            description=(TextView)itemView.findViewById(R.id.list_description);

        }

        public void bind(Site site)
        {
            title.setText(site.getTitle());
            description.setText(site.getDescription());
        }

    }

    private void updateView() {
        mSiteAdapter.notifyDataSetChanged();
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
