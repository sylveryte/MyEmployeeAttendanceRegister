package com.codedleaf.sylveryte.myemployeeattendanceregister.Labs;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.widget.ImageView;

import com.codedleaf.sylveryte.myemployeeattendanceregister.CircleTransform;
import com.codedleaf.sylveryte.myemployeeattendanceregister.R;
import com.codedleaf.sylveryte.myemployeeattendanceregister.SQLite.AttendanceBaseHelper;
import com.codedleaf.sylveryte.myemployeeattendanceregister.SQLite.AttendanceDbSchema;
import com.codedleaf.sylveryte.myemployeeattendanceregister.SQLite.AttendanceDbSchema.EmployeesTable;
import com.codedleaf.sylveryte.myemployeeattendanceregister.SQLite.AttendanceDbToolsProvider;
import com.codedleaf.sylveryte.myemployeeattendanceregister.CodedleafTools;
import com.codedleaf.sylveryte.myemployeeattendanceregister.Models.Employee;
import com.codedleaf.sylveryte.myemployeeattendanceregister.Picknation.Pickable;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by sylveryte on 14/6/16.
 *
 * Copyright (C) 2016 sylveryte@codedleaf <codedleaf@gmail.com>
 *
 * This file is part of My Employee Attendance Register.
 *
 */
public class EmployeeLab implements LabObeservable {

    private static EmployeeLab sEmployeeLab;

    private static Bitmap sBitmapMale;
    private static Bitmap sBitmapFemale;

    private static final String MALE_KEY="derp";
    private static final String FEMALE_KEY="derpina";

    private List<Employee> mEmployees;
    private List<LabObserver> mLabObservers;


    private SQLiteDatabase mDatabase;

    private Context mContext;

    private EmployeeLab(Context context)
    {
        mContext=context;
        mDatabase= AttendanceBaseHelper.getDatabaseWritable(context);

        mEmployees=new ArrayList<>();
        mLabObservers=new ArrayList<>();

    }

    private void initializeDatabase(Context context)
    {
        EmployeeCursorWrapper cursorWrapper=querySites(null,null);
        try
        {
            cursorWrapper.moveToFirst();
            while (!cursorWrapper.isAfterLast())
            {
                mEmployees.add(cursorWrapper.getEmployee(context));
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
            SitesLab.getInstanceOf(context);
            sEmployeeLab=new EmployeeLab(context);
            sEmployeeLab.initializeDatabase(context);


            //// TODO: 24/7/16 get rid of this
//            for (int i=0;i<20;i++)
//            {
//                Employee employee=new Employee();
//                employee.setName("Emp "+i*3);
//                employee.setMale(i%2==0);
//                employee.setAge(20+i);
//                sEmployeeLab.addEmployee(employee);
//            }
        }
        return sEmployeeLab;
    }

    public boolean doesExist(Employee employee)
    {
        if (mEmployees.contains(employee))
            return true;
        return false;
    }

    public File getEmpPhotoFile(Employee employee)
    {
        File externalFilesDir=mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (externalFilesDir==null)
            return null;
        return new File(externalFilesDir,employee.getFileName());
    }

    public Bitmap getEmpPlaceHolder(Employee employee)
    {
        if (employee.isMale())
        {
//                sBitmapMale=BitmapFactory.decodeResource(mContext.getResources(), R.drawable.derp);
            if (sBitmapMale==null)
                try {
                    sBitmapMale= Picasso.with(mContext)
                            .load(R.drawable.derp)
                            .transform(new CircleTransform())
                            .get();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            return sBitmapMale;
        }
        else
        {
//                sBitmapFemale=BitmapFactory.decodeResource(mContext.getResources(), R.drawable.derpina);
            if (sBitmapFemale==null)
                try {
                    sBitmapFemale= Picasso.with(mContext)
                            .load(R.drawable.derpina)
                            .transform(new CircleTransform())
                            .get();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            return sBitmapFemale;
        }
    }

    public void setEmpPlaceHolder(Employee employee, ImageView imageView)
    {
        if (employee.isMale())
            Picasso.with(mContext)
            .load(R.drawable.derp)
            .stableKey(MALE_KEY)
            .transform(new CircleTransform())
            .into(imageView);
        else
            Picasso.with(mContext)
                    .load(R.drawable.derpina)
                    .stableKey(FEMALE_KEY)
                    .transform(new CircleTransform())
                    .into(imageView);
    }

    public List<Pickable> getPickables(List<UUID> empIds)
    {
        List<Pickable> pickables=new ArrayList<>();
        for (UUID uuid:empIds)
        {
            Pickable pickable=getEmployeeById(uuid);
            if (pickable == null) {
                continue;
            }
            pickables.add(pickable);
        }
        return pickables;
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
        alertAllObservers();
        String empIdString=employee.getId().toString();
        ContentValues values= AttendanceDbToolsProvider.getContentValues(employee);
        mDatabase.update(AttendanceDbSchema.EmployeesTable.NAME,values, AttendanceDbSchema.EmployeesTable.Cols.UID+"=?",new String[]{empIdString});
    }

    public void deleteEmployee(Employee employee,Context context)
    {
        mEmployees.remove(employee);
        alertAllObservers();

        employee.delete(context);
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

    public void invalidatePhotoCache(String path)
    {
        for (LabObserver labObserver:mLabObservers)
            labObserver.picassoAlert(path);
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

        public Employee getEmployee(Context context)
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

            Employee employee=new Employee(UUID.fromString(uuidString));

            employee.setName(name);
            employee.setActive(CodedleafTools.getBooleanFromString(isActive));
            employee.setMale(CodedleafTools.getBooleanFromString(isMale));
            employee.setAddress(address);
            employee.setAge(age);
            employee.setNote(noteString);
            employee.setDesignations(CodedleafTools.getUUIDListFromString(designations),context);
            employee.setSites(CodedleafTools.getUUIDListFromString(sites),context);

            return employee;
        }
    }

}
