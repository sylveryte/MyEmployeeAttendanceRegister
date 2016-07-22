package com.codedleaf.sylveryte.myemployeeattendanceregister;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by sylveryte on 11/7/16.
 */
public class SiteStatFragment extends Fragment {

    private static final String ARGS_CODE="site_stat_budle_code";
    private static final String ITEM_CODE="site_code";


    private Site mSite;

    private LocalDate mDate;

    private PieChart mPieChart;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.site_stat,container,false);

        Bundle args=getArguments();
        if (args!=null)
        {
            UUID uuid=(UUID)args.getSerializable(ITEM_CODE);
            mSite=SitesLab.getInstanceOf(getActivity())
                    .getSiteById(uuid);
        }

        //// TODO: 22/7/16  have eye on this one
        mDate=new LocalDate();


        mPieChart =(PieChart)view.findViewById(R.id.piechartsite);

        mPieChart.setUsePercentValues(true);
        mPieChart.setDescription("");
        mPieChart.setExtraOffsets(5, 10, 5, 5);

        mPieChart.setDragDecelerationFrictionCoef(0.95f);

//        mPieChart.setCenterTextTypeface(mTfLight);
        mPieChart.setCenterText("codedleaf");

        mPieChart.setDrawHoleEnabled(true);
        mPieChart.setHoleColor(Color.WHITE);

        mPieChart.setTransparentCircleColor(Color.WHITE);
        mPieChart.setTransparentCircleAlpha(110);

        mPieChart.setHoleRadius(58f);
        mPieChart.setTransparentCircleRadius(61f);

        mPieChart.setDrawCenterText(true);

        mPieChart.setRotationAngle(0);
        // enable rotation of the chart by touch
        mPieChart.setRotationEnabled(true);
        mPieChart.setHighlightPerTapEnabled(true);

        // mPieChart.setUnit(" €");
        // mPieChart.setDrawUnitsInChart(true);

        // add a selection listener
        mPieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(com.github.mikephil.charting.data.Entry e, Highlight h) {

            }

            @Override
            public void onNothingSelected() {

            }
        });

        setPieData();

        mPieChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
        // mPieChart.spin(2000, 0, 360);

        Legend l = mPieChart.getLegend();
        l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);

        // entry label styling
        mPieChart.setEntryLabelColor(Color.WHITE);
//        mPieChart.setEntryLabelTypeface(mTfRegular);
        mPieChart.setEntryLabelTextSize(12f);






        //doesnt work why?
        getActivity().setTitle(mSite.getTitle());

        return view;
    }

    private void setPieData()
    {
        //this is gonna be big method hopefully
        ArrayList<PieEntry> pieEntries=new ArrayList<>();

        pieEntries.add(new PieEntry(mSite.getFloat(mDate,Entry.LATE,getActivity()),"Late"));
        pieEntries.add(new PieEntry(mSite.getFloat(mDate,Entry.ABSENT,getActivity()),"Absent"));
        pieEntries.add(new PieEntry(mSite.getFloat(mDate,Entry.PRESENT,getActivity()),"Present"));
        pieEntries.add(new PieEntry(mSite.getFloat(mDate,Entry.HALF_TIME,getActivity()),"HalfTime"));
        pieEntries.add(new PieEntry(mSite.getFloat(mDate,Entry.OVER_TIME,getActivity()),"Overtime"));

        PieDataSet dataSet=new PieDataSet(pieEntries,"Remarks");
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

        mPieChart.setData(pieData);

        // undo all highlights
        mPieChart.highlightValues(null);

        mPieChart.invalidate();
    }


    public static SiteStatFragment createInstance(Site site)
    {
        SiteStatFragment siteStatFragment=new SiteStatFragment();

        Bundle args=new Bundle(1);
        args.putSerializable(ITEM_CODE,site.getId());

        siteStatFragment.setArguments(args);

        return siteStatFragment;
    }
}
