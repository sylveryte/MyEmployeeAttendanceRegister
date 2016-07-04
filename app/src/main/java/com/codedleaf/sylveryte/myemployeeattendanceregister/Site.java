package com.codedleaf.sylveryte.myemployeeattendanceregister;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by sylveryte on 12/6/16.
 */
public class Site implements Pickable {


    private String mTitle;
    private String mDescription;

    private LocalDate mBeginDate;
    private LocalDate mFinishedDate;

    private UUID mSiteId;

    private List<UUID> mEmployeesInvolved;

    public Site()
    {
        mEmployeesInvolved=new ArrayList<>();
        mBeginDate = new LocalDate();
        mSiteId = UUID.randomUUID();


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

    public LocalDate getBeginDate() {
        return mBeginDate;
    }

    public void setBeginDate(LocalDate beginDate) {
        mBeginDate = beginDate;
    }

    public LocalDate getFinishedDate() {
        return mFinishedDate;
    }

    public void setFinishedDate(LocalDate finishedDate) {
        mFinishedDate = finishedDate;
    }

    public List<UUID> getEmployeesInvolved() {
        return mEmployeesInvolved;
    }


    @Override
    public UUID getId() {
        return mSiteId;
    }










    public void delete()
    {
        EmployeeLab lab=EmployeeLab.getInstanceOf();
        for (UUID uuid:mEmployeesInvolved)
        {
            Employee employee=lab.getEmployeeById(uuid);
            if (employee!=null)
            {
                employee.removeSiteByid(mSiteId);
            }
        }

        EntryLab.getInstanceOf().cleanseEntriesOfSiteId(mSiteId);

    }

    public void addEmployeeById(UUID empId)
    {
        if (mEmployeesInvolved.contains(empId))
            return;
        mEmployeesInvolved.add(empId);

        Employee employee=EmployeeLab.getInstanceOf().getEmployeeById(empId);
        employee.addSiteById(mSiteId);
    }

    public void removeEmployeeById(UUID uuid)
    {
        if (!mEmployeesInvolved.contains(uuid))
            return;
        mEmployeesInvolved.remove(uuid);
    }
}
