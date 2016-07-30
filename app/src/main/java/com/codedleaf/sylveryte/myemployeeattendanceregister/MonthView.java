package com.codedleaf.sylveryte.myemployeeattendanceregister;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sylveryte on 27/7/16.
 * <p>
 * Copyright (C) 2016 sylveryte@codedleaf <codedlaf@gmail.com>
 * <p>
 * This file is part of My Employee Attendance Register.
 */
public class MonthView{

    LinearLayout mView;
    LayoutInflater mInflater;

    LocalDate mDate;

    Employee mEmployee;

    Context mContext;

    TextView mMonthText;

    LinearLayout monthContainer;

    GridView weekNameGrid;
    GridView weekDateGrid;

    String[] weekDaysNames;

    private MonthView(LayoutInflater inflater, Context context,LocalDate date,Employee employee)
    {
        mDate=new LocalDate(date.getYear(),date.getMonthOfYear(),1);
        mEmployee=employee;
        mContext=context;
        mInflater=inflater;
        mView=(LinearLayout)inflater.inflate(R.layout.month_view,null,false);
        monthContainer=(LinearLayout)mView.findViewById(R.id.month_container);
        weekDaysNames=context.getResources().getStringArray(R.array.weekdays_names);
        weekNameGrid=(GridView)mView.findViewById(R.id.gridview_names);
        weekDateGrid=(GridView)mView.findViewById(R.id.gridview_dates);
        mMonthText=(TextView)mView.findViewById(R.id.calendar_month_year);

        Button leftArrow=(Button)mView.findViewById(R.id.left_arrow);
        Button rightArrow=(Button)mView.findViewById(R.id.right_arrow);

        rightArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDate=mDate.plusMonths(1);
                refreshMonth(mContext);
            }
        });
        leftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDate=mDate.minusMonths(1);
                refreshMonth(mContext);
            }
        });
    }

    public View getView()
    {
        return mView;
    }

    private void inflateWeekNames()
    {

        List<View> weekdaysNameList=new ArrayList<>();
        for (int i=0;i<7;i++)
        {
           weekdaysNameList.add(getDayName(i));
        }

        weekNameGrid.setAdapter(new GridAdapter(weekdaysNameList));
    }

    private class GridAdapter extends BaseAdapter
    {
        List<View> mViewList;

        public GridAdapter(List<View> views)
        {
            mViewList=views;
        }

        @Override
        public int getCount() {
            return mViewList.size();
        }

        @Override
        public Object getItem(int position) {
            return mViewList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return mViewList.get(position);
        }
    }

    private View getDayName(int day)
    {
        LinearLayout view=(LinearLayout)mInflater.inflate(R.layout.calendar_gola_weekdays_name,null,false);


        view.setGravity(Gravity.CENTER);

        TextView textView=(TextView) view.findViewById(R.id.gola_text);

        textView.setText(weekDaysNames[day]);

        return view;
    }


    public void refreshMonth(Context context)
    {
        mContext=context;
        List<MonthDayView> monthDays=new ArrayList<>();

        mMonthText.setText(CodedleafTools.getMonthYearString(mDate));

        //here continue

        LocalDate tempdate=new LocalDate(mDate);

        int firstWeek=mDate.getDayOfWeek();
        if (firstWeek<7)
        for (int i=firstWeek;i>0;i--)
        {
            MonthDayView dayView=new MonthDayView(mDate.minusDays(i));
            dayView.setInvisible(true);
            monthDays.add(dayView);
        }

        int month=tempdate.getMonthOfYear();
        while (tempdate.getMonthOfYear()==month)
        {
            monthDays.add(new MonthDayView(tempdate));
            tempdate=tempdate.plusDays(1);
        }



        weekDateGrid.setAdapter(new GridDateAdapter(monthDays));
    }

    private class GridDateAdapter extends BaseAdapter
    {
        List<MonthDayView> mViewList;

        public GridDateAdapter(List<MonthDayView> views)
        {
            mViewList=views;
        }

        @Override
        public int getCount() {
            return mViewList.size();
        }

        @Override
        public Object getItem(int position) {
            return mViewList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return mViewList.get(position).getView();
        }
    }

    private class MonthDayView
    {
        private Entry mEntry;
        private LocalDate mLocalDate;
        private CardView mGolaCard;
        private View mView;
        private TextView mTextView;

        boolean invisible;

        public MonthDayView(LocalDate date)
        {
            mLocalDate=date;
            mEntry=EntryLab.getInstanceOf(mContext).getEntry(mLocalDate,mEmployee.getId());
            mView=mInflater.inflate(R.layout.calendar_gola,null,false);
            mGolaCard=(CardView)mView.findViewById(R.id.calendar_gola);
            mTextView=(TextView)mView.findViewById(R.id.gola_text);
            mGolaCard.setClickable(false);
            mTextView.setText(String.valueOf(mLocalDate.getDayOfMonth()));
            invisible=false;
            CustomAttendanceCardListner.setColor(mEntry,mContext,mGolaCard);
        }

        public void setInvisible(boolean invisible) {
            this.invisible = invisible;
        }

        public View getView() {
            if (invisible)
                mView.setVisibility(View.INVISIBLE);
            return mView;
        }
    }

    private void init()
    {
        inflateWeekNames();
        refreshMonth(mContext);
    }


    public static MonthView getInstance(LayoutInflater inflater, Context context, LocalDate date,Employee employee)
    {
        MonthView monthView=new MonthView(inflater,context,date,employee);
        monthView.init();
        return monthView;
    }

}
