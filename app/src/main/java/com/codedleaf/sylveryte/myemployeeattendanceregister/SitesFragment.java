package com.codedleaf.sylveryte.myemployeeattendanceregister;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.DisplayMetrics;
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

        mSitesLab=SitesLab.getInstanceOf(getActivity());
        mSitesLab.addListener(this);

        getActivity().setTitle("Sites");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.recycler_fragment,container,false);
        mRecyclerView =(RecyclerView)view.findViewById(R.id.fragment_recycler_view);

        //for automatic
        //// TODO: 22/6/16  looks suspicious
        DisplayMetrics metrics=new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int cardWidth=(int)metrics.xdpi;
        int spans=(int)Math.floor(mRecyclerView.getContext().getResources().getDisplayMetrics().widthPixels/(float)cardWidth);

        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(spans,StaggeredGridLayoutManager.VERTICAL));
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
        private Site mSite;

        public SiteHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    CharSequence choices[] = new CharSequence[] {"Take Attendance", "Show Stats","Edit","Delete"};

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle(mSite.getTitle());
                    builder.setItems(choices, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            switch (which)
                            {
                                case 3:
                                {
                                    mSitesLab.deleteSite(mSite);
                                    update();
                                    break;
                                }

                                case 2: {

                                    Intent intent = AdditionActivity.fetchIntent(getActivity(), AdditionActivity.FRAGMENT_CODE_EDIT_SITE);
                                    intent.putExtra(AdditionActivity.FRAGMENT_STRING_EDIT_SITE, mSite.getId());
                                    startActivity(intent);

                                    break;
                                }
                                case 0: {

                                    Intent intent1 = AttendanceActivity.fetchIntent(getActivity(), AttendanceActivity.FRAGMENT_CODE_SITE);
                                    intent1.putExtra(AttendanceActivity.siteAttendance, mSite.getId());
                                    startActivity(intent1);

                                    break;
                                }
                                case 1: {

                                    Intent intent3=StatActivity.fetchIntent(getActivity(),StatActivity.FRAGMENT_CODE_STAT_SITE,mSite.getId());
                                    startActivity(intent3);
                                }
                            }
                        }
                    });
                    builder.show();





                }
            });

            title=(TextView)itemView.findViewById(R.id.site_card_title);
            description=(TextView)itemView.findViewById(R.id.site_card_description);

        }

        public void bind(Site site)
        {
            mSite=site;
            title.setText(site.getTitle());
            description.setText(site.getDescription());
        }

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
        mSiteAdapter.notifyDataSetChanged();
    }

    public static Fragment newInstance()
    {
        return new SitesFragment();
    }


}
