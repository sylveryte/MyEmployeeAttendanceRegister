package com.codedleaf.sylveryte.myemployeeattendanceregister.SiteAttendance;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.codedleaf.sylveryte.myemployeeattendanceregister.Labs.EntryLab;
import com.codedleaf.sylveryte.myemployeeattendanceregister.Labs.SitesLab;
import com.codedleaf.sylveryte.myemployeeattendanceregister.Models.Entry;
import com.codedleaf.sylveryte.myemployeeattendanceregister.R;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * Created by sylveryte on 31/7/16.
 * <p/>
 * Copyright (C) 2016 sylveryte@codedleaf <codedleaf@gmail.com>
 * <p/>
 * This file is part of My Employee Attendance Register.
 */
public class SiteAttendanceTaking extends Fragment {

    private static final String ARGS_CODE="siteattendanceargs";

    private LocalDate mDate;

    private UUID mSiteId;
    private int NoOfDays;

    private HashMap<UUID,List<EntryUnitView>> mViewMap;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle("Taking Attendance");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.attendance_fragment, null, false);
        LinearLayout dateContainer = (LinearLayout) view.findViewById(R.id.date_container_ll);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.attendance_fragment_recycler_view);

        mSiteId=(UUID)getArguments().getSerializable(ARGS_CODE);

        NoOfDays=calculateNoOfDatesShould();
        mDate=new LocalDate();


        inflateSquareDatesViews(inflater, dateContainer,mDate);


        mViewMap=getMap();

        AttendanceAdapter attendanceAdapter = new AttendanceAdapter(
                SitesLab.getInstanceOf(getActivity()).getSiteById(mSiteId).getEmployeesInvolved()
        );
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(attendanceAdapter);

        return view;
    }

    private int calculateNoOfDatesShould()
    {
        int count;
        DisplayMetrics matrices=getActivity().getResources().getDisplayMetrics();

        count=(int)(matrices.widthPixels/matrices.density)/(2*60);

        if (count<1)
        {
            count=2;
        }
        return count;
    }

    private HashMap<UUID,List<EntryUnitView>> getMap()
    {
        List<UUID> emps=SitesLab.getInstanceOf(getActivity()).getSiteById(mSiteId).getEmployeesInvolved();

        HashMap<UUID,List<EntryUnitView>> map=new HashMap<>(emps.size());

        for (UUID uuid:emps)
        {
            map.put(uuid,fetchViews(uuid));
        }

        return map;
    }

    private List<EntryUnitView> fetchViews(UUID empId)
    {
        List<EntryUnitView> entryUnitViews=new ArrayList<>(NoOfDays);

        for (int i=0;i<NoOfDays;i++)
        {
            LocalDate date=mDate.minusDays(i);

            Entry entry= EntryLab.getInstanceOf(getActivity()).getEntry(date,mSiteId,empId);

            EntryUnitView view=new EntryUnitView(getActivity(),entry);

            entryUnitViews.add(view);
        }
        return entryUnitViews;
    }

    private void inflateSquareDatesViews(LayoutInflater inflater, LinearLayout dateContainer, LocalDate date)
    {
        String pattern="d\nE";
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT,1.0f);
        for (int i=0;i< NoOfDays;i++)
        {
            LinearLayout view=(LinearLayout)inflater.inflate(R.layout.entry_unit_head ,null,false);
            view.setGravity(Gravity.CENTER);
            view.setLayoutParams(lp);

            TextView textView=(TextView) view.findViewById(R.id.entry_unit_text);

            textView.setText(mDate.minusDays(i).toString(pattern));
            dateContainer.addView(view);
        }
        dateContainer.invalidate();
    }


    //adapter and holder herer
    private class AttendanceAdapter extends RecyclerView.Adapter<AttendanceHolder>
    {
        private List<UUID> mEmployees;

        public AttendanceAdapter(List<UUID> employees)
        {
            mEmployees=employees;
        }

        @Override
        public AttendanceHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            AttendanceView attendanceView=new AttendanceView(getActivity());
            return new AttendanceHolder(attendanceView);
        }

        @Override
        public void onBindViewHolder(AttendanceHolder holder, int position) {
            holder.bind(mEmployees.get(position));
        }

        @Override
        public int getItemCount() {
            return mEmployees.size();
        }
    }



    private class AttendanceHolder extends RecyclerView.ViewHolder
    {
        AttendanceView mAttendanceView;

        public AttendanceHolder(AttendanceView itemView) {
            super(itemView);
            mAttendanceView=itemView;
        }

        public void bind(UUID empId)
        {
            mAttendanceView.bind(mViewMap.get(empId),empId,mSiteId,getFragmentManager());
        }
    }

    public static SiteAttendanceTaking createInstance(UUID siteId)
    {
        SiteAttendanceTaking siteAttendanceTaking=new SiteAttendanceTaking();

        Bundle args=new Bundle();
        args.putSerializable(ARGS_CODE,siteId);
        siteAttendanceTaking.setArguments(args);

        return siteAttendanceTaking;
    }

}
