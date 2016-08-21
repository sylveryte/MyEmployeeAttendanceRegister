package com.codedleaf.sylveryte.myemployeeattendanceregister.Stats;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.codedleaf.sylveryte.myemployeeattendanceregister.Labs.EmployeeLab;
import com.codedleaf.sylveryte.myemployeeattendanceregister.Models.Employee;
import com.codedleaf.sylveryte.myemployeeattendanceregister.R;
import com.codedleaf.sylveryte.myemployeeattendanceregister.Stats.EmpStat.MoneyContainer;

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

    private MoneyContainer mMoneyContainer;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.employee_stat_fragment,container,false);
        mMoneyContainer=(MoneyContainer) view.findViewById(R.id.moneyContainer);
        //since site is requi/*d now
        Bundle arguments=getArguments();
        if (arguments!=null) {
            mEmployee= EmployeeLab.getInstanceOf(getActivity())
                    .getEmployeeById((UUID) arguments.getSerializable(ITEM_CODE));
        }
                return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mMoneyContainer.initialize(mEmployee.getId(),getFragmentManager());
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

