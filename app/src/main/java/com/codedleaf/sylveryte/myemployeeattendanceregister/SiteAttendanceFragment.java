package com.codedleaf.sylveryte.myemployeeattendanceregister;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telecom.TelecomManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by sylveryte on 27/6/16.
 */
public class SiteAttendanceFragment extends Fragment {

    private Site mSite;
    private EntrySet mEntrySet;
    private RecyclerView mRecyclerView;
    private EmployeeAttendanceAdapter mEmployeeAttendanceAdpater;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.recycler_fragment_attendance, container, false);

        UUID siteId=mSite.getId();
        Date date=new Date();

        mEntrySet=EntryLab.getInstanceOf().getEntrySetBySiteIdAndDate(date,siteId);
        mSite=SitesLab.getInstanceOf().getSiteById(siteId);

        int ids=2;

        List<Entry> entries=mEntrySet.getEntries();
        int di=entries.size();

        mEmployeeAttendanceAdpater=new EmployeeAttendanceAdapter(entries);

        int i=entries.size();
        mRecyclerView = (RecyclerView) view.findViewById(R.id.fragment_recycler_view_attendance);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL,true));
        mRecyclerView.setAdapter(mEmployeeAttendanceAdpater);

        return view;
    }

    private class EmployeeAttendanceHolder extends RecyclerView.ViewHolder
    {
        private Entry mEntry;
        private TextView mTextViewName;

        public EmployeeAttendanceHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CharSequence choices[] = new CharSequence[] {"Present", "Late", "Halftime", "Overtime","Absent"};

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Set status");
                    builder.setItems(choices, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which)
                            {
                                case 0: mEntry.setRemark(Entry.PRESENT); break;
                                case 1: mEntry.setRemark(Entry.LATE); break;
                                case 2: mEntry.setRemark(Entry.HALF_TIME); break;
                                case 3: mEntry.setRemark(Entry.OVER_TIME); break;
                                case 4: mEntry.setRemark(Entry.ABSENT); break;
                            }
                        }
                    });
                    builder.show();
                }
            });

            mTextViewName=(TextView)itemView.findViewById(R.id.employee_attendance_name);
        }

        private void bind(Entry entry)
        {
            mEntry=entry;
            mTextViewName.setText(entry.getEmployeeInfo());
        }
    }


    private class EmployeeAttendanceAdapter extends RecyclerView.Adapter<EmployeeAttendanceHolder>
    {
        List<Entry> mEntries;

        public EmployeeAttendanceAdapter(List<Entry> entries)
        {
            mEntries=entries;
        }

        @Override
        public EmployeeAttendanceHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater=getActivity().getLayoutInflater();
            View view=layoutInflater.inflate(R.layout.employee_attendance_layout,parent);

            return new EmployeeAttendanceHolder(view);
        }

        @Override
        public void onBindViewHolder(EmployeeAttendanceHolder holder, int position) {
            holder.bind(mEntries.get(position));
        }

        @Override
        public int getItemCount() {
            return mEntries.size();
        }
    }



    public void setSite(Site site) {
        mSite = site;
    }


    public static SiteAttendanceFragment createInstance(Site site)
    {
        SiteAttendanceFragment siteAttendanceFragment=new SiteAttendanceFragment();
        siteAttendanceFragment.setSite(site);
        return siteAttendanceFragment;
    }
}
