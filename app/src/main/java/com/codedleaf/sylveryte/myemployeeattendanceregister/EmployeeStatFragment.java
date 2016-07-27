package com.codedleaf.sylveryte.myemployeeattendanceregister;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import java.util.UUID;

/**
 * Created by sylveryte on 11/7/16.
 */
public class EmployeeStatFragment extends Fragment {

    private static final String ITEM_CODE = "retrievId";
    private Employee mEmployee;



    public static EmployeeStatFragment createInstance(UUID empId)
    {
        EmployeeStatFragment employeeStatFragment=new EmployeeStatFragment();
        Bundle args=new Bundle(1);
        args.putSerializable(ITEM_CODE,empId);

        employeeStatFragment.setArguments(args);
        return employeeStatFragment;
    }
}

