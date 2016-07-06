package com.codedleaf.sylveryte.myemployeeattendanceregister;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by sylveryte on 14/6/16.
 */
public class EmployeeLab implements LabObeservable {

    private static EmployeeLab sEmployeeLab;

    private List<Employee> mEmployees;
    private List<LabObserver> mLabObservers;

    private Context mContext;
    private SQLiteDatabase mDatabase;

    private EmployeeLab(Context context)
    {

        mContext=context.getApplicationContext();
        mDatabase=AttendanceBaseHelper.getDatabaseWritable(mContext);

        mEmployees=new ArrayList<>();
        mLabObservers=new ArrayList<>();

    }

    public static EmployeeLab getInstanceOf(Context context)
    {
        if (sEmployeeLab==null)
        {
            sEmployeeLab=new EmployeeLab(context);
        }
        return sEmployeeLab;
    }

    public void addEmployee(Employee employee)
    {
        if (mEmployees.contains(employee))
            return;
        mEmployees.add(employee);
        alertAllObservers();
    }
    public void deleteEmployee(Employee employee)
    {
        employee.delete();
        mEmployees.remove(employee);
        alertAllObservers();
    }

    public List<Employee> getEmployees()
    {
        return mEmployees;
    }

    public Employee getEmployeeById(UUID uuid)
    {
        for (Employee employee:mEmployees)
        {
            if (employee.getId().equals(uuid))
                return employee;
        }
        return null;
    }

    public void addListener(LabObserver labObserver)
    {
        mLabObservers.add(labObserver);
    }

    public void alertAllObservers()
    {
        for (LabObserver labObserver :mLabObservers)
            labObserver.update();
    }

}
