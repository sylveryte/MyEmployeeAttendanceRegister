package com.codedleaf.sylveryte.myemployeeattendanceregister.Picknation;

/**
 * Created by sylveryte on 25/7/16.
 *
 * Copyright (C) 2016 sylveryte@codedleaf <codedlaf@gmail.com>
 *
 * This file is part of My Employee Attendance Register.
 *
 */
public class PickingChavaClass {

    boolean mChecked;
    Pickable mPickable;

    public PickingChavaClass(boolean checked,Pickable pickable)
    {
        mPickable=pickable;
        mChecked=checked;
    }

    public boolean isChecked() {
        return mChecked;
    }

    public Pickable getPickable() {
        return mPickable;
    }

    public void setChecked(boolean checked) {
        mChecked = checked;
    }
}
