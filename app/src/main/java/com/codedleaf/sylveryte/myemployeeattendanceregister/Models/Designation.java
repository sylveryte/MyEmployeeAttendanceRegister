package com.codedleaf.sylveryte.myemployeeattendanceregister.Models;

import android.content.Context;

import com.codedleaf.sylveryte.myemployeeattendanceregister.Labs.DesignationLab;
import com.codedleaf.sylveryte.myemployeeattendanceregister.Labs.EmployeeLab;
import com.codedleaf.sylveryte.myemployeeattendanceregister.Picknation.PickCache;
import com.codedleaf.sylveryte.myemployeeattendanceregister.GeneralObserver;
import com.codedleaf.sylveryte.myemployeeattendanceregister.Picknation.Pickable;
import com.codedleaf.sylveryte.myemployeeattendanceregister.RegisterConstants;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by sylveryte on 12/6/16.
 *
 * Copyright (C) 2016 sylveryte@codedleaf <codedleaf@gmail.com>
 *
 * This file is part of My Employee Attendance Register.
 *
 */
public class Designation implements Pickable,GeneralObserver {
    @Override
    public int getType() {
        return RegisterConstants.DESIGNATION;
    }

    private String mTitle;
    private String mDescription;

    private UUID mDesignationId;

    private List<UUID> mEmployees;

    public Designation()
    {
        mDesignationId = UUID.randomUUID();
        mEmployees=new ArrayList<>();
    }

    public Designation(UUID designationId)
    {
        mEmployees=new ArrayList<>();
        mDesignationId=designationId;
    }

    public List<UUID> getEmployees() {
        return mEmployees;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getDescription() {
        return getEmployees().size()+" Employees\n"+mDescription;
    }

    public String getPureDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public void delete(Context context)
    {
        EmployeeLab lab=EmployeeLab.getInstanceOf(context);
        for (UUID uuid:mEmployees)
        {
            Employee employee=lab.getEmployeeById(uuid);
            if (employee!=null)
            {
                employee.removeDesignationById(mDesignationId,context);
            }
        }
    }

    public void addEmployeeInvolvedById(UUID uuid,Context context)
    {
        if (mEmployees.contains(uuid))
            return;
        mEmployees.add(uuid);

        Employee employee=EmployeeLab.getInstanceOf(context).getEmployeeById(uuid);
        if (employee!=null)
            employee.addDesignationById(mDesignationId,context);
    }

    public void removeEmployeeInvolvedById(UUID uuid,Context context)
    {
        if (!mEmployees.contains(uuid))
            return;
        mEmployees.remove(uuid);

        Employee employee=EmployeeLab.getInstanceOf(context).getEmployeeById(uuid);
        if (employee!=null)
            employee.removeDesignationById(getId(),context);
    }

    @Override
    public UUID getId() {
        return mDesignationId;
    }

    public void updateMyDB(Context context)
    {
        DesignationLab.getInstanceOf(context).updateDesignation(this);
    }

    @Override
    public void doSomeUpdate(Context context) {
        for (UUID uuid: PickCache.getInstance().getPicked(getId().toString()))
        {
            addEmployeeInvolvedById(uuid,context);
        }
        for (UUID uuid:PickCache.getInstance().getRemoved(getId().toString()))
        {
            removeEmployeeInvolvedById(uuid,context);
        }
        updateMyDB(context);
    }
}
