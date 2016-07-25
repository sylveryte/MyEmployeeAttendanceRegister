package com.codedleaf.sylveryte.myemployeeattendanceregister;

/**
 * Created by sylveryte on 25/7/16.
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
