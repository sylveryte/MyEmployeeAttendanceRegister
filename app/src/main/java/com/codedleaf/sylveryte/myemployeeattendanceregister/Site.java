package com.codedleaf.sylveryte.myemployeeattendanceregister;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by sylveryte on 12/6/16.
 */
public class Site implements Pickable {


    String mTitle;
    String mDescription;
    List<UUID> mEmployeesInvolved;
    Date mBeginDate;
    Date mFinishedDate;
    UUID mSiteId;

    public Site()
    {
        mEmployeesInvolved=new ArrayList<>();
        mBeginDate = new Date();
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

    public Date getBeginDate() {
        return mBeginDate;
    }

    public void setBeginDate(Date beginDate) {
        mBeginDate = beginDate;
    }

    public Date getFinishedDate() {
        return mFinishedDate;
    }

    public void setFinishedDate(Date finishedDate) {
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
