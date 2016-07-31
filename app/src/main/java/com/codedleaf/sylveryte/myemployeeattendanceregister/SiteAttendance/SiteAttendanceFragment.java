package com.codedleaf.sylveryte.myemployeeattendanceregister.SiteAttendance;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.codedleaf.sylveryte.myemployeeattendanceregister.Employee;
import com.codedleaf.sylveryte.myemployeeattendanceregister.EmployeeLab;
import com.codedleaf.sylveryte.myemployeeattendanceregister.Entry;
import com.codedleaf.sylveryte.myemployeeattendanceregister.EntryLab;
import com.codedleaf.sylveryte.myemployeeattendanceregister.R;
import com.codedleaf.sylveryte.myemployeeattendanceregister.Site;
import com.codedleaf.sylveryte.myemployeeattendanceregister.SitesLab;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * Created by sylveryte on 27/6/16.
 *
 * Copyright (C) 2016 sylveryte@codedleaf <codedlaf@gmail.com>
 *
 * This file is part of My Employee Attendance Register.
 *
 */
public class SiteAttendanceFragment extends Fragment {

    private static final String ARGS_CODE="siteattendanceargs";

    HashMap<UUID,List<Entry>> mEntriesMap;
    List<UUID> mEmployees;
    List<LocalDate> mLocalDates;

    private int NoOfDays;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle("Taking Attendance");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.attendance_fragment, container, false);
        LinearLayout dateContainer = (LinearLayout) view.findViewById(R.id.date_container_ll);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.attendance_fragment_recycler_view);

        NoOfDays=calculateNoOfDatesShould();
        mLocalDates=new ArrayList<>();
        LocalDate date=new LocalDate();


        inflateSquareDatesViews(inflater, dateContainer,date);


        //// TODO: 23/7/16 do something about this one

        if (mEmployees!=null)
        {
            EmployeeAttendanceAdapter employeeAttendanceAdpater = new EmployeeAttendanceAdapter(mEmployees);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
            recyclerView.setAdapter(employeeAttendanceAdpater);
        }
        return view;
    }


    private int calculateNoOfDatesShould()
    {
        int count;
        DisplayMetrics matrices=getActivity().getResources().getDisplayMetrics();
        count=(int)(matrices.heightPixels/matrices.density)/(2*85);

        if (count<1)
        {
            count=2;
        }

        return count;
    }

    private void inflateSquareDatesViews(LayoutInflater inflater, LinearLayout dateContainer, LocalDate date)
    {

        DateTimeFormatter fmt = DateTimeFormat.forPattern("d\nE");

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT,1.0f);

        for (int i=0;i< NoOfDays;i++)
        {
            LinearLayout view=(LinearLayout)inflater.inflate(R.layout.attendance_gola_square,null,false);

            view.setGravity(Gravity.CENTER);


            view.setLayoutParams(lp);

            TextView textView=(TextView) view.findViewById(R.id.button_square_date);

            LocalDate localDate=date.minusDays(i);
            mLocalDates.add(localDate);

            textView.setText(fmt.print(localDate));

            dateContainer.addView(view);
        }

        inflateEntries();

        dateContainer.invalidate();
    }

    public void inflateEntries()
    {
        UUID siteId=(UUID)getArguments().getSerializable(ARGS_CODE);
        Site site = SitesLab.getInstanceOf(getActivity()).getSiteById(siteId);

        mEntriesMap=new HashMap<>();
        mEmployees= site.getEmployeesInvolved();

        if (mEmployees!=null)
        {
            for (UUID empID:mEmployees)
            {
                mEntriesMap.put(empID,getEntries(empID,siteId));
            }
        }
    }

    public List<Entry> getEntries(UUID empId,UUID siteId)
    {
        List<Entry> toReturnEntries=new ArrayList<>(NoOfDays);
        for (int i=0;i<NoOfDays;i++)
        {
            Entry entry;
            entry= EntryLab.getInstanceOf(getActivity()).getEntry(mLocalDates.get(i),siteId,empId);
            toReturnEntries.add(entry);
        }
        return toReturnEntries;
    }

    private class EmployeeAttendanceHolder extends RecyclerView.ViewHolder
    {
        private Employee mEmployee;
        private List<Entry> mEntryList;
        private List<GolaView> mGolaViews;

        private LinearLayout mGolaContainer;

        private TextView mTextViewName;

        public EmployeeAttendanceHolder(View itemView) {
            super(itemView);

            mTextViewName=(TextView)itemView.findViewById(R.id.employee_attendance_name);
            mGolaContainer=(LinearLayout)itemView.findViewById(R.id.gola_container);

            mGolaViews=new ArrayList<>();
            for (int i=0;i<NoOfDays;i++)
            {
                GolaView golaView=new GolaView(mLocalDates.get(i),getActivity());

                mGolaContainer.addView(golaView.getLayoutView());
                mGolaViews.add(golaView);
            }
        }
        private void bind(UUID empId)
        {
            mEntryList=mEntriesMap.get(empId);
            for (int i=0;i<NoOfDays;i++)
            {
                mGolaViews.get(i).setEntry(mEntryList.get(i));
            }

            mEmployee= EmployeeLab.getInstanceOf(getActivity()).getEmployeeById(empId);
            mTextViewName.setText(mEmployee.getTitle());
            int t=mEntriesMap.size();
            String s=mEntriesMap.toString();


            for (GolaView golaView: mGolaViews)
            {
                golaView.setClicability();
            }
        }


    }


    private class EmployeeAttendanceAdapter extends RecyclerView.Adapter<EmployeeAttendanceHolder>
    {
        List<UUID> mEntries;

        public EmployeeAttendanceAdapter(List<UUID> entries)
        {
            mEntries=entries;
        }
        @Override
        public EmployeeAttendanceHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater=getActivity().getLayoutInflater();
            View view=layoutInflater.inflate(R.layout.attendance_layout,parent,false);

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

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }


    public static SiteAttendanceFragment createInstance(UUID siteId)
    {
        SiteAttendanceFragment siteAttendanceFragment=new SiteAttendanceFragment();

        Bundle args=new Bundle();
        args.putSerializable(ARGS_CODE,siteId);
        siteAttendanceFragment.setArguments(args);

        return siteAttendanceFragment;
    }

}
