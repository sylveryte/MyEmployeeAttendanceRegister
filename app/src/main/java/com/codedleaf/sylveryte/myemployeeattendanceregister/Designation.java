package com.codedleaf.sylveryte.myemployeeattendanceregister;

import java.util.UUID;

/**
 * Created by sylveryte on 12/6/16.
 */
public class Designation {

    String mTitle;
    String mDescription;
    UUID mDesignationId;

    public Designation()
    {
        mDesignationId = UUID.randomUUID();
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


    public UUID getDesignationId() {
        return mDesignationId;
    }
}
