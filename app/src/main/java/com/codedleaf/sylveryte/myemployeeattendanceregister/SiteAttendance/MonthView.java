package com.codedleaf.sylveryte.myemployeeattendanceregister.SiteAttendance;

import android.content.Context;
import android.graphics.PointF;
import android.support.v7.widget.CardView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.codedleaf.sylveryte.myemployeeattendanceregister.CodedleafTools;
import com.codedleaf.sylveryte.myemployeeattendanceregister.Models.Employee;
import com.codedleaf.sylveryte.myemployeeattendanceregister.Labs.EmployeeLab;
import com.codedleaf.sylveryte.myemployeeattendanceregister.Models.Entry;
import com.codedleaf.sylveryte.myemployeeattendanceregister.Labs.EntryLab;
import com.codedleaf.sylveryte.myemployeeattendanceregister.R;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by sylveryte on 27/7/16.
 * <p>
 * Copyright (C) 2016 sylveryte@codedleaf <codedleaf@gmail.com>
 * <p>
 * This file is part of My Employee Attendance Register.
 */
public class MonthView{

    private final java.util.UUID mSiteId;
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

    private MonthView(LayoutInflater inflater, Context context,LocalDate date,Employee employee,UUID siteID)
    {
        mSiteId=siteID;
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

        final Button leftArrow=(Button)mView.findViewById(R.id.left_arrow);
        final Button rightArrow=(Button)mView.findViewById(R.id.right_arrow);

        rightArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rightAction();
            }
        });
        leftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                leftAction();
            }
        });

        mView.setOnTouchListener(new View.OnTouchListener() {
            float x=0;
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        x=event.getX();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        break;
                    case MotionEvent.ACTION_UP:
                        float dif=x-event.getX();
                        if (Math.abs(dif)>200) {
                            if (dif< 0)
                                leftAction();
                            else rightAction();
                        }
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        break;
                }

                return true;
            }
        });
    }

    private void leftAction() {
        mDate=mDate.minusMonths(1);
        refreshMonth(mContext);
    }

    private void rightAction() {
        mDate=mDate.plusMonths(1);
        refreshMonth(mContext);
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
        LinearLayout view=(LinearLayout)mInflater.inflate(R.layout.entry_unit_head,null,false);


        view.setGravity(Gravity.CENTER);

        TextView textView=(TextView) view.findViewById(R.id.entry_unit_text);

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
            mEntry= EntryLab.getInstanceOf(mContext).getEntry(mLocalDate,mSiteId,mEmployee.getId());
            mView=mInflater.inflate(R.layout.entry_unit,null,false);
            mGolaCard=(CardView)mView.findViewById(R.id.entry_card);
            mTextView=(TextView)mView.findViewById(R.id.entry_unit_text);
            mGolaCard.setOnClickListener(new EntryUnitCardListner(mContext,mEntry,mGolaCard));
            mTextView.setText(String.valueOf(mLocalDate.getDayOfMonth()));
            invisible=false;
            EntryUnitCardListner.setColor(mEntry,mContext,mGolaCard);
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


    public static MonthView getInstance(LayoutInflater inflater, Context context, LocalDate date, UUID empid,UUID siteId)
    {
        Employee employee= EmployeeLab.getInstanceOf(context).getEmployeeById(empid);

        MonthView monthView=new MonthView(inflater,context,date,employee,siteId);
        monthView.init();
        return monthView;
    }

}
