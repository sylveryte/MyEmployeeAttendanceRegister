package com.codedleaf.sylveryte.myemployeeattendanceregister.GeneralFragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codedleaf.sylveryte.myemployeeattendanceregister.SiteAttendance.AttendanceActivity;
import com.codedleaf.sylveryte.myemployeeattendanceregister.CodedleafTools;
import com.codedleaf.sylveryte.myemployeeattendanceregister.Labs.LabObserver;
import com.codedleaf.sylveryte.myemployeeattendanceregister.Labs.EmployeeLab;
import com.codedleaf.sylveryte.myemployeeattendanceregister.Labs.SitesLab;
import com.codedleaf.sylveryte.myemployeeattendanceregister.Picknation.PickDialogFragment;
import com.codedleaf.sylveryte.myemployeeattendanceregister.R;
import com.codedleaf.sylveryte.myemployeeattendanceregister.RegisterConstants;
import com.codedleaf.sylveryte.myemployeeattendanceregister.Picknation.SimpleListDialogFragment;
import com.codedleaf.sylveryte.myemployeeattendanceregister.Models.Site;
import com.codedleaf.sylveryte.myemployeeattendanceregister.Editing.SiteAdditionDialogFragment;
import com.codedleaf.sylveryte.myemployeeattendanceregister.Stats.StatActivity;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sylveryte on 11/6/16.
 *
 * Copyright (C) 2016 sylveryte@codedleaf <codedleaf@gmail.com>
 *
 * This file is part of My Employee Attendance Register.
 *
 */
public class SitesFragment extends Fragment implements LabObserver,SearchView.OnQueryTextListener {


    private SitesLab mSitesLab;
    private SiteAdapter mSiteAdapter;

    private RecyclerView mRecyclerView;

    private List<Site> mSites;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        mSitesLab=SitesLab.getInstanceOf(getActivity());
        mSitesLab.addListener(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.recycler_fragment,container,false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.fragment_recycler_view);

        setHasOptionsMenu(true);


        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


      /*  //for automatic
        //// TODO: 22/6/16  looks suspicious
        DisplayMetrics metrics=new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int cardWidth=(int)metrics.xdpi;
        int spans=(int)Math.floor(mRecyclerView.getContext().getResources().getDisplayMetrics().widthPixels/(float)cardWidth);

        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(spans,StaggeredGridLayoutManager.VERTICAL));*/

        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));

        mSites=mSitesLab.getSites();

        mSiteAdapter=new SiteAdapter(mSites);
        mRecyclerView.setAdapter(mSiteAdapter);

    }

    private class SiteAdapter extends RecyclerView.Adapter<SiteHolder>
    {
        List<Site> mAdapterSites;

        public SiteAdapter(List<Site> sites)
        {
            mAdapterSites =new ArrayList<>(sites);
        }
        @Override
        public SiteHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater=getActivity().getLayoutInflater();
            View view=inflater.inflate(R.layout.site_card,parent,false);

            return new SiteHolder(view);
        }

        @Override
        public void onBindViewHolder(SiteHolder holder, int position) {
            holder.bind(mAdapterSites.get(position));
        }

        @Override
        public int getItemCount() {
            return mAdapterSites.size();
        }

        public Site removeItem(int position)
        {
            final Site site=mAdapterSites.remove(position);
            notifyItemRemoved(position);
            return site;
        }

        public void addItem(int position, Site site)
        {
            mAdapterSites.add(position,site);
            notifyItemInserted(position);
        }

        public void moveItem(int fromPosition,int toPosition)
        {
            final Site site=mAdapterSites.remove(fromPosition);
            mAdapterSites.add(toPosition,site);
            notifyItemMoved(fromPosition,toPosition);
        }

        public void animateTo(List<Site> sites)
        {
            applyAndAnimateRemovals(sites);
            applyAndAnimateAddition(sites);
            applyAndAnimateMovedItems(sites);
        }

        private void applyAndAnimateRemovals(List<Site> sites)
        {
            for (int i=mAdapterSites.size()-1;i>=0;i--)
            {
                final Site site=mAdapterSites.get(i);
                if (!sites.contains(site))
                {
                    removeItem(i);
                }
            }
        }

        private void applyAndAnimateAddition(List<Site> sites)
        {
            for (int i=0,count=sites.size();i<count;i++)
            {
                final Site site=sites.get(i);
                if (!mAdapterSites.contains(site))
                {
                    addItem(i,site);
                }
            }
        }

        private void applyAndAnimateMovedItems(List<Site> sites)
        {
            for (int toPosition=sites.size()-1;toPosition >= 0; toPosition--)
            {
                final Site site=sites.get(toPosition);
                final int fromPosition=mAdapterSites.indexOf(site);
                if (fromPosition>=0&&fromPosition!=toPosition)
                {
                    moveItem(fromPosition,toPosition);
                }
            }
        }
        }
    //end of adapter class


    //onquery
    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        final List<Site> filteredSites=filter(mSites,newText);
        mSiteAdapter.animateTo(filteredSites);
//        mRecyclerView.scrollTo(0,0);

        //this one is buggy glitchy
//        mRecyclerView.scrollToPosition(0);
        return true;
    }

    private List<Site> filter(List<Site> siteList, String newText)
    {
        newText=newText.toLowerCase();

        final List<Site> filteredSites=new ArrayList<>();

        for (Site site:siteList)
        {
            final String text=site.getTitle().toLowerCase();
            if (text.contains(newText))
            {
                filteredSites.add(site);
            }
        }
        return filteredSites;
    }


    //start of holder class
    private class SiteHolder extends RecyclerView.ViewHolder
    {
        private static final String DIALOG_FRAGMENT_CODE = "editdialogsite";
        private static final String DIALOG_PICK_FRAGMENT_CODE = "pickempdialogsite";

        private TextView title;
        private TextView description;
        private TextView begindate;
        private TextView finisheddate;
        private Site mSite;
        private CardView mCardView;


        public SiteHolder(View itemView) {
            super(itemView);

            title=(TextView)itemView.findViewById(R.id.site_card_title);
            begindate=(TextView)itemView.findViewById(R.id.site_card_beginDate);
            finisheddate=(TextView)itemView.findViewById(R.id.site_card_finishedDate);
            description=(TextView)itemView.findViewById(R.id.site_card_description);
            mCardView=(CardView)itemView.findViewById(R.id.site_card_card);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    CharSequence choices[] = new CharSequence[] {"Take Attendance", "Show Stats","Show Assignments","Assign Employees","Active??","Edit","Delete"};

                    if (mSite.isActive())
                    {
                        choices[4]="Deactivate";
                    }
                    else
                    {
                        choices[4]="Activate";
                    }

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle(mSite.getTitle());
                    builder.setItems(choices, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            switch (which)
                            {
                                case 6:
                                {
                                    new AlertDialog.Builder(getActivity())
                                            .setTitle("Delete "+mSite.getTitle())
                                            .setMessage("Are you sure you want to delete this site?")
                                            .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    // continue with delete
                                                    mSitesLab.deleteSite(mSite,getActivity());
                                                    update();
                                                }
                                            })
                                            .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    // do nothing
                                                }
                                            })
                                            .setIcon(android.R.drawable.ic_dialog_alert)
                                            .show();

                                    break;
                                }

                                case 2:
                                {
                                    SimpleListDialogFragment.getInstance(mSite.getId().toString(),
                                            EmployeeLab.getInstanceOf(getActivity()).getPickables(mSite.getEmployeesInvolved()),SimpleListDialogFragment.NORMAL_MODE)
                                            .show(getActivity().getSupportFragmentManager(),DIALOG_PICK_FRAGMENT_CODE);
                                    break;
                                }

                                case 3:
                                {
                                    PickDialogFragment.getInstance(mSite.getId().toString(),
                                            mSite,
                                            mSite.getEmployeesInvolved(),
                                            EmployeeLab.getInstanceOf(getActivity()).getEmployees())
                                            .show(getActivity().getSupportFragmentManager(),DIALOG_PICK_FRAGMENT_CODE);
                                    break;
                                }

                                case 4:
                                {
                                    if (mSite.isActive())
                                    {
                                        mSite.setFinishedDate(new LocalDate());
                                    }
                                    else
                                    {
                                        mSite.setFinishedDate(null);
                                    }
                                    mSite.setActive(!mSite.isActive());
                                    mSite.updateMyDB(getActivity());
                                    update();
                                    break;
                                }

                                case 5: {

                                    SiteAdditionDialogFragment.getSiteFrag(mSite.getId()).show(getActivity().getSupportFragmentManager(),DIALOG_FRAGMENT_CODE);
                                    break;
                                }
                                case 0: {

                                    Intent intent1 = AttendanceActivity.fetchIntent(getActivity(), RegisterConstants.SITE);
                                    intent1.putExtra(AttendanceActivity.siteAttendance, mSite.getId());
                                    startActivity(intent1);

                                    break;
                                }
                                case 1: {

                                    Intent intent3= StatActivity.fetchIntent(getActivity(),RegisterConstants.SITE,mSite.getId());
                                    startActivity(intent3);
                                }
                            }
                        }
                    });
                    builder.show();
                }
            });




        }

        public void bind(Site site)
        {
            mSite=site;
            title.setText(site.getTitle());
            begindate.setText(CodedleafTools.getPrettyDateString(mSite.getBeginDate()));
            finisheddate.setText(CodedleafTools.getPrettyDateString(mSite.getFinishedDate()));
            description.setText(site.getDescription());
                if (mSite.isActive())
                    mCardView.setCardBackgroundColor(ContextCompat.getColor(getActivity(),R.color.colorSiteCard));
                else
                    mCardView.setCardBackgroundColor(ContextCompat.getColor(getActivity(),R.color.colorSiteCardDeactivated));
        }

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        MenuItem item=menu.findItem(R.id.action_search);
        SearchView searchView=(SearchView) item.getActionView();
        searchView.setOnQueryTextListener(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        update();
    }


    @Override
    public void onResume() {
        super.onResume();
        update();
    }

    @Override
    public void update() {
        if (mSiteAdapter!=null)
        {
            List<Site> sites=new ArrayList<>(SitesLab.getInstanceOf(getActivity()).getSites());
            mSites=sites;
            mSiteAdapter.animateTo(sites);

            mSiteAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void picassoAlert(String path) {

    }

    public static Fragment newInstance()
    {
        return new SitesFragment();
    }


}