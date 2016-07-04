package com.codedleaf.sylveryte.myemployeeattendanceregister;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by sylveryte on 12/6/16.
 */
public class Designation implements Pickable {

    private String mTitle;
    private String mDescription;

    private UUID mDesignationId;

    private List<UUID> mEmployees;

    public Designation()
    {
        mDesignationId = UUID.randomUUID();
        mEmployees=new ArrayList<>();
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







    public void delete()
    {
        EmployeeLab lab=EmployeeLab.getInstanceOf();
        for (UUID uuid:mEmployees)
        {
            Employee employee=lab.getEmployeeById(uuid);
            if (employee!=null)
            {
                employee.removeDesignationById(mDesignationId);
            }
        }
    }

    public void addEmployeeInvolvedById(UUID uuid)
    {
        if (mEmployees.contains(uuid))
            return;
        mEmployees.add(uuid);

        Employee employee=EmployeeLab.getInstanceOf().getEmployeeById(uuid);
        employee.removeDesignationById(mDesignationId);
    }

    public void removeEmployeeInvolvedById(UUID uuid)
    {
        if (!mEmployees.contains(uuid))
            return;
        mEmployees.remove(uuid);
    }

    @Override
    public UUID getId() {
        return mDesignationId;
    }
}
