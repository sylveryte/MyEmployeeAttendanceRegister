package com.codedleaf.sylveryte.myemployeeattendanceregister.SiteAttendance;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.codedleaf.sylveryte.myemployeeattendanceregister.Labs.EmployeeLab;
import com.codedleaf.sylveryte.myemployeeattendanceregister.R;

import java.util.List;
import java.util.UUID;


/**
 * Created by sylveryte on 31/7/16.
 * <p>
 * Copyright (C) 2016 sylveryte@codedleaf <codedlaf@gmail.com>
 * <p>
 * This file is part of My Employee Attendance Register.
 */
public class AttendanceView extends LinearLayout {

    private static final String DIALOG_CODE = "socodeman";
    private CardView mRootView;
    private LinearLayout mEntryViewContainer;
    private LinearLayout mEmployeeNameContainer;
    private TextView mTextViewName;

    private UUID mEmpID;
    private UUID mSiteID;
    private FragmentManager mFragmentManager;

    public AttendanceView(Context context) {
        super(context);

        LinearLayout.LayoutParams lpd = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lpd.gravity= Gravity.CENTER;
        setLayoutParams(lpd);


        LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mRootView=(CardView)(inflater.inflate(R.layout.attendance_layout,AttendanceView.this,false));
        addView(mRootView);

        mEmployeeNameContainer=(LinearLayout)mRootView.findViewById(R.id.name_container_site_attendance);
        mEntryViewContainer =(LinearLayout)mRootView.findViewById(R.id.gola_container);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT,1.0f);
        lp.gravity= Gravity.CENTER;

        mEmployeeNameContainer.setLayoutParams(lp);
        mEntryViewContainer.setLayoutParams(lp);

        mEmployeeNameContainer.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                MonthViewDialogFragment.getInstance(mEmpID, mSiteID)
                        .show(mFragmentManager,DIALOG_CODE);
            }
        });

        mTextViewName=(TextView)mRootView.findViewById(R.id.employee_attendance_name);
    }

    public void bind(List<EntryUnitView> unitViews, @NonNull UUID empId, UUID siteId, FragmentManager fragmentManager)
    {
        mSiteID =siteId;
        mEmpID =empId;
        mFragmentManager=fragmentManager;
        mTextViewName.setText(
                EmployeeLab.getInstanceOf(getContext()).getEmployeeById(empId).getTitle()
        );

        setViews(unitViews);
    }

    private void setViews(List<EntryUnitView> views)
    {
        if (views.isEmpty())
            return;

        mEntryViewContainer.removeAllViews();

        for(EntryUnitView view:views)
        {
            mEntryViewContainer.addView(view);
        }
    }
}
