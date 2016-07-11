package com.codedleaf.sylveryte.myemployeeattendanceregister;


import android.support.v4.app.Fragment;

/**
 * Created by sylveryte on 11/7/16.
 */
public class DesignationStatFragment extends Fragment {

    private Designation mDesignation;


    private void setDesignation(Designation designation) {
        mDesignation = designation;
    }

    public static DesignationStatFragment createInstance(Designation designation)
    {
        DesignationStatFragment designationStatFragment=new DesignationStatFragment();
        designationStatFragment.setDesignation(designation);
        return designationStatFragment;
    }
}
