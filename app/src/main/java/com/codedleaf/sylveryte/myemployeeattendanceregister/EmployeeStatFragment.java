package com.codedleaf.sylveryte.myemployeeattendanceregister;

import android.support.v4.app.Fragment;

/**
 * Created by sylveryte on 11/7/16.
 */
public class EmployeeStatFragment extends Fragment {

    private Employee mEmployee;

    private void setEmployee(Employee employee) {
        mEmployee = employee;
    }

    public static EmployeeStatFragment createInstance(Employee employee)
    {
        EmployeeStatFragment employeeStatFragment=new EmployeeStatFragment();
        employeeStatFragment.setEmployee(employee);
        return employeeStatFragment;
    }
}

