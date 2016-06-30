package com.codedleaf.sylveryte.myemployeeattendanceregister;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by sylveryte on 12/6/16.
 */
public class Site implements Pickable {


    String mTitle;
    String mDescription;
    List<UUID> mEmployeesInvolved;
    LocalDate mBeginDate;
    LocalDate mFinishedDate;
    UUID mSiteId;

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

    public void addEmployee(UUID uuid)
    {
        mEmployeesInvolved.add(uuid);
    }

    public List<UUID> getEmployeesInvolved() {
        return mEmployeesInvolved;
    }

    @Override
    public UUID getId() {
        return mSiteId;
    }
}
