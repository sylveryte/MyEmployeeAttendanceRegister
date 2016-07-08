package com.codedleaf.sylveryte.myemployeeattendanceregister;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;

import com.codedleaf.sylveryte.myemployeeattendanceregister.AttendanceDbSchema.EmployeesTable;

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

    private void initializeDatabase()
    {
        EmployeeCursorWrapper cursorWrapper=querySites(null,null);
        try
        {
            cursorWrapper.moveToFirst();
            while (!cursorWrapper.isAfterLast())
            {
                mEmployees.add(cursorWrapper.getEmployee());
                cursorWrapper.moveToNext();
            }
        }
        finally
        {
            cursorWrapper.close();
        }
    }


    public static EmployeeLab getInstanceOf(Context context)
    {
        if (sEmployeeLab==null)
        {
            sEmployeeLab=new EmployeeLab(context);
            sEmployeeLab.initializeDatabase();
        }
        return sEmployeeLab;
    }

    public void addEmployee(Employee employee)
    {
        if (mEmployees.contains(employee))
        {
            return;
        }
        mEmployees.add(employee);
        alertAllObservers();

        ContentValues values= AttendanceDbToolsProvider.getContentValues(employee);
        mDatabase.insert(AttendanceDbSchema.EmployeesTable.NAME,null,values);
    }

    public void updateEmployee(Employee employee)
    {

        String empIdString=employee.getId().toString();
        ContentValues values= AttendanceDbToolsProvider.getContentValues(employee);
        mDatabase.update(AttendanceDbSchema.EmployeesTable.NAME,values, AttendanceDbSchema.EmployeesTable.Cols.UID+"=?",new String[]{empIdString});
    }

    public void deleteEmployee(Employee employee)
    {
        employee.delete();
        mEmployees.remove(employee);
        alertAllObservers();

        String empIdString=employee.getId().toString();
        mDatabase.delete(AttendanceDbSchema.EmployeesTable.NAME, AttendanceDbSchema.EmployeesTable.Cols.UID+"=?",new String[]{empIdString});
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


    //database codes


    private EmployeeCursorWrapper querySites(String whereClause, String[] wherwArgs)
    {
        Cursor cursor=mDatabase.query(
                AttendanceDbSchema.EmployeesTable.NAME,
                null, //columns null coz select all columns :)
                whereClause,
                wherwArgs,
                null, //group by
                null, //having
                null //orderby
        );

        return new EmployeeCursorWrapper(cursor);

    }

    private  class EmployeeCursorWrapper extends CursorWrapper
    {
        public EmployeeCursorWrapper(Cursor cursor)
        {
            super(cursor);
        }

        public Employee getEmployee()
        {
            String uuidString=getString(getColumnIndex(EmployeesTable.Cols.UID));
            String name=getString(getColumnIndex(EmployeesTable.Cols.NAME));
            String address=getString(getColumnIndex(EmployeesTable.Cols.ADDRESS));
            int age=getInt(getColumnIndex(EmployeesTable.Cols.AGE));
            String noteString=getString(getColumnIndex(EmployeesTable.Cols.NOTE));


            String isMale=getString(getColumnIndex(EmployeesTable.Cols.GENDER));
            String isActive=getString(getColumnIndex(EmployeesTable.Cols.ACTIVE));

            String designations=getString(getColumnIndex(EmployeesTable.Cols.DESIGNATIONIDS));
            String sites=getString(getColumnIndex(EmployeesTable.Cols.SITEIDS));

            Employee employee=new Employee(mContext,UUID.fromString(uuidString));

            employee.setName(name);
            employee.setActive(CodedleafTools.getBooleanFromString(isActive));
            employee.setMale(CodedleafTools.getBooleanFromString(isMale));
            employee.setAddress(address);
            employee.setAge(age);
            employee.setNote(noteString);
            employee.setDesignations(CodedleafTools.getUUIDListFromString(designations));
            employee.setSites(CodedleafTools.getUUIDListFromString(sites));

            return employee;
        }
    }

}
