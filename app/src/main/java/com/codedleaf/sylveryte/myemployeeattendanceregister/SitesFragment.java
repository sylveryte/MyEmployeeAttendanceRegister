package com.codedleaf.sylveryte.myemployeeattendanceregister;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
public class SitesFragment extends Fragment implements LabObserver {


    private SitesLab mSitesLab;
    private RecyclerView mRecyclerView;
    private SiteAdapter mSiteAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        mSitesLab=SitesLab.getInstanceOf();
        mSitesLab.addListener(this);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.recycler_fragment,container,false);

        mRecyclerView =(RecyclerView)view.findViewById(R.id.fragment_recycler_view);
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        mSiteAdapter=new SiteAdapter(mSitesLab.getSites());
        mRecyclerView.setAdapter(mSiteAdapter);

        return view;
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
            View view=inflater.inflate(R.layout.site_card,parent,false);

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
        private TextView title;
        private TextView description;

        public SiteHolder(View itemView) {
            super(itemView);

            title=(TextView)itemView.findViewById(R.id.site_card_title);
            description=(TextView)itemView.findViewById(R.id.site_card_description);

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
