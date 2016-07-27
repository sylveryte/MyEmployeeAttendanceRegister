package com.codedleaf.sylveryte.myemployeeattendanceregister;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by sylveryte on 12/6/16.
 */
public class Designation implements Pickable,DialogPickObserver {

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
        for (UUID uuid:PickCache.getInstance().getPicked(getId().toString()))
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
