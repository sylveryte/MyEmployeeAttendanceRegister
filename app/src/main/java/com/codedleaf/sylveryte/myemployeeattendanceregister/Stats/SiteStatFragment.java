package com.codedleaf.sylveryte.myemployeeattendanceregister.Stats;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.codedleaf.sylveryte.myemployeeattendanceregister.CodedleafTools;
import com.codedleaf.sylveryte.myemployeeattendanceregister.Models.Designation;
import com.codedleaf.sylveryte.myemployeeattendanceregister.Models.Entry;
import com.codedleaf.sylveryte.myemployeeattendanceregister.Labs.DesignationLab;
import com.codedleaf.sylveryte.myemployeeattendanceregister.Labs.EmployeeLab;
import com.codedleaf.sylveryte.myemployeeattendanceregister.Labs.SitesLab;
import com.codedleaf.sylveryte.myemployeeattendanceregister.R;
import com.codedleaf.sylveryte.myemployeeattendanceregister.Picknation.SimpleListDialogFragment;
import com.codedleaf.sylveryte.myemployeeattendanceregister.Models.Site;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by sylveryte on 11/7/16.
 *
 * Copyright (C) 2016 sylveryte@codedleaf <codedlaf@gmail.com>
 *
 * This file is part of My Employee Attendance Register.
 *
 */
public class SiteStatFragment extends Fragment {

    private static final String ITEM_CODE="site_code";
    private static final String DIALOG_CODE="simple_list_dialog_code";


    private Site mSite;

    private LocalDate mDate;
    private LocalDate mDateForPie;

    private PieChart pieDesgChart;
    private PieChart pieChart;

    private TextView title;
    private TextView desc;
    private TextView begindate;
    private TextView finishdate;
    private TextView empcounter;
    private TextView pieMonth;

    private Button pieMonthLeft;
    private Button pieMonthRight;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.site_stat,container,false);
        pieChart = (PieChart) view.findViewById(R.id.piechartsite);
        pieDesgChart = (PieChart) view.findViewById(R.id.piechartsite_desgs);
        title = (TextView)view.findViewById(R.id.site_stat_title);
        desc = (TextView)view.findViewById(R.id.site_stat_description);
        begindate = (TextView)view.findViewById(R.id.site_stat_begin_date);
        finishdate = (TextView)view.findViewById(R.id.site_stat_finished_date);
        pieMonth = (TextView)view.findViewById(R.id.pieMonthText);
        pieMonthLeft=(Button)view.findViewById(R.id.pie_left);
        pieMonthRight=(Button)view.findViewById(R.id.pie_right);
        empcounter = (TextView)view.findViewById(R.id.site_stat_emp_counter_info);

        pieMonthLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDateForPie=mDateForPie.minusMonths(1);
                updatePieMonthText();
                initPie(pieChart,getAttendancePieData(mDateForPie));
                pieChart.invalidate();
            }
        });
        pieMonthRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDateForPie=mDateForPie.plusMonths(1);
                updatePieMonthText();
                initPie(pieChart,getAttendancePieData(mDateForPie));
                pieChart.invalidate();
            }
        });

        empcounter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleListDialogFragment.getInstance(mSite.getId().toString(), EmployeeLab.getInstanceOf(getActivity()).getPickables(mSite.getEmployeesInvolved()))
                        .show(getFragmentManager(),DIALOG_CODE);
            }
        });
        Bundle args=getArguments();
        if (args!=null)
        {
            UUID uuid=(UUID)args.getSerializable(ITEM_CODE);
            mSite= SitesLab.getInstanceOf(getActivity())
                    .getSiteById(uuid);
        }
        //// TODO: 22/7/16  have eye on this one
        mDate=new LocalDate();
        mDateForPie=new LocalDate();


        update();
        initPie(pieChart,getAttendancePieData(mDate));
        pieChart.setCenterText("Attendances");
        initPie(pieDesgChart,getDesgPieData());
        pieDesgChart.setCenterText("Designations");



        getActivity().setTitle(mSite.getTitle());
        return view;
    }

    private void updatePieMonthText()
    {
        pieMonth.setText(CodedleafTools.getMonthYearString(mDateForPie));
    }

    private void update()
    {
        title.setText(mSite.getTitle());
        desc.setText(mSite.getDescriptionPure());
        begindate.setText(CodedleafTools.getPrettyStringFromLocalDate(mSite.getBeginDate()));
        finishdate.setText(CodedleafTools.getPrettyStringFromLocalDate(mSite.getFinishedDate()));
        empcounter.setText(mSite.getEmployeeCountString());
    }

    private void initPie(PieChart pieChart,PieData pieData) {
        pieChart.setHoleRadius(30f);
        pieChart.setTransparentCircleRadius(50f);
        Legend l = pieChart.getLegend();
        l.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);
        pieChart.setData(pieData);
        pieChart.setDescription("");

        pieChart.animateXY(800,800);
        //pieChart.spin( 500,0,-360f, Easing.EasingOption.EaseInOutQuad);
    }

    private PieData getAttendancePieData(LocalDate localDate)
    {
        //this is gonna be big method hopefully
        ArrayList<PieEntry> pieEntries=new ArrayList<>();

        pieEntries.add(new PieEntry(mSite.getFloat(localDate, Entry.LATE,getActivity()),"Late"));
        pieEntries.add(new PieEntry(mSite.getFloat(localDate,Entry.ABSENT,getActivity()),"Absent"));
        pieEntries.add(new PieEntry(mSite.getFloat(localDate,Entry.PRESENT,getActivity()),"Present"));
        pieEntries.add(new PieEntry(mSite.getFloat(localDate,Entry.HALF_TIME,getActivity()),"HalfTime"));
        pieEntries.add(new PieEntry(mSite.getFloat(localDate,Entry.OVER_TIME,getActivity()),"Overtime"));

        PieDataSet dataSet=new PieDataSet(pieEntries,localDate.monthOfYear().toString());
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);

        //color
        ArrayList<Integer> colors=new ArrayList<>();

        colors.add(ContextCompat.getColor(getActivity(),R.color.attendanceLate));
        colors.add(ContextCompat.getColor(getActivity(),R.color.attendanceAbsent));
        colors.add(ContextCompat.getColor(getActivity(),R.color.attendancePresent));
        colors.add(ContextCompat.getColor(getActivity(),R.color.attendanceHalfTime));
        colors.add(ContextCompat.getColor(getActivity(),R.color.attendanceOvertime));

        dataSet.setColors(colors);

        PieData pieData=new PieData(dataSet);
        pieData.setValueFormatter(new PercentFormatter());
        pieData.setValueTextSize(11f);
        pieData.setValueTextColor(Color.WHITE);

//        pieData.setValueTypeface(mTfLight);

        return pieData;
    }

    private PieData getDesgPieData()
    {
        //this is gonna be big method hopefully
        ArrayList<PieEntry> pieEntries=new ArrayList<>();

        for (Designation designation: DesignationLab.getInstanceOf(getContext()).getDesignations())
        {
            float per=mSite.getDesgPercent(designation.getId(),getContext());
            if (per>0)
                pieEntries.add(new PieEntry(per,(int)per*mSite.getEmployeesInvolved().size()/100+"\n"+designation.getTitle()));
        }




        PieDataSet dataSet=new PieDataSet(pieEntries," ");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);

        //color
        ArrayList<Integer> colors=new ArrayList<>();

        colors.add(ContextCompat.getColor(getActivity(),R.color.green_700));
        colors.add(ContextCompat.getColor(getActivity(),R.color.lime_700));
        colors.add(ContextCompat.getColor(getActivity(),R.color.light_blue_700));
        colors.add(ContextCompat.getColor(getActivity(),R.color.blue_700));
        colors.add(ContextCompat.getColor(getActivity(),R.color.purple_700));
        colors.add(ContextCompat.getColor(getActivity(),R.color.pink_700));
        colors.add(ContextCompat.getColor(getActivity(),R.color.red_700));
        colors.add(ContextCompat.getColor(getActivity(),R.color.orange_700));
        colors.add(ContextCompat.getColor(getActivity(),R.color.yellow_700));

        dataSet.setColors(colors);

        PieData pieData=new PieData(dataSet);
        pieData.setValueFormatter(new PercentFormatter());
        pieData.setValueTextSize(11f);
        pieData.setValueTextColor(Color.WHITE);

//        pieData.setValueTypeface(mTfLight);

        return pieData;
    }




    public static SiteStatFragment createInstance(UUID siteId)
    {
        SiteStatFragment siteStatFragment=new SiteStatFragment();

        Bundle args=new Bundle(1);
        args.putSerializable(ITEM_CODE,siteId);

        siteStatFragment.setArguments(args);

        return siteStatFragment;
    }
}
