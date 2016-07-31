package com.codedleaf.sylveryte.myemployeeattendanceregister;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import org.joda.time.LocalDate;

import java.util.UUID;

/**
 * Created by sylveryte on 11/7/16.
 *
 * Copyright (C) 2016 sylveryte@codedleaf <codedlaf@gmail.com>
 *
 * This file is part of My Employee Attendance Register.
 *
 */
public class EmployeeStatFragment extends Fragment {

    private static final String ITEM_CODE = "retrievId";
    private Employee mEmployee;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.employee_stat_fragment,container,false);

        LinearLayout monthContainer=(LinearLayout)view.findViewById(R.id.month_container);

        //since site is required now
/*
        Bundle arguments=getArguments();
        if (arguments!=null) {
            mEmployee= EmployeeLab.getInstanceOf(getActivity())
                    .getEmployeeById((UUID) arguments.getSerializable(ITEM_CODE));


            MonthView monthView = MonthView.getInstance(inflater, getContext(), new LocalDate(), mEmployee);
            monthContainer.addView(monthView.getView());

        }*/
        return view;
    }

    public static EmployeeStatFragment createInstance(UUID empId)
    {
        EmployeeStatFragment employeeStatFragment=new EmployeeStatFragment();
        Bundle args=new Bundle(1);
        args.putSerializable(ITEM_CODE,empId);

        employeeStatFragment.setArguments(args);
        return employeeStatFragment;
    }
}

