package com.codedleaf.sylveryte.myemployeeattendanceregister.Stats;


import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.codedleaf.sylveryte.myemployeeattendanceregister.Models.Designation;

import java.util.UUID;

/**
 * Created by sylveryte on 11/7/16.
 *
 * Copyright (C) 2016 sylveryte@codedleaf <codedleaf@gmail.com>
 *
 * This file is part of My Employee Attendance Register.
 *
 */
public class DesignationStatFragment extends Fragment {

    private static final String ITEM_CODE = "retrieveItem";
    private Designation mDesignation;



    public static DesignationStatFragment createInstance(UUID desgId)
    {
        DesignationStatFragment designationStatFragment=new DesignationStatFragment();

        Bundle args=new Bundle(1);
        args.putSerializable(ITEM_CODE,desgId);

        designationStatFragment.setArguments(args);

        return designationStatFragment;
    }
}
