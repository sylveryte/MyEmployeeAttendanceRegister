package com.codedleaf.sylveryte.myemployeeattendanceregister.Stats;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.codedleaf.sylveryte.myemployeeattendanceregister.RegisterConstants;
import com.codedleaf.sylveryte.myemployeeattendanceregister.SingleFragmentActivity;

import java.util.UUID;

/**
 * Created by sylveryte on 11/7/16.
 *
 * Copyright (C) 2016 sylveryte@codedleaf <codedlaf@gmail.com>
 *
 * This file is part of My Employee Attendance Register.
 *
 */
public class StatActivity extends SingleFragmentActivity {


    private static final String FRAGMENT_CODE = "stat_vagaira_ka";
    private static final String UUID_CODE = "uuid_vagaira_ka";


    @Override
    protected Fragment createFragment() {

        Intent intent=getIntent();
        int i=intent.getIntExtra(FRAGMENT_CODE, RegisterConstants.SITE);
        UUID uuid=(UUID)intent.getSerializableExtra(UUID_CODE);

        switch (i)
        {
            case RegisterConstants.SITE:
            {
                return SiteStatFragment.createInstance(uuid);
            }
            case RegisterConstants.EMPLOYEE:
            {
                return EmployeeStatFragment.createInstance(uuid);
            }
            case RegisterConstants.DESIGNATION:
            {
                return DesignationStatFragment.createInstance(uuid);
            }
        }

        return SiteStatFragment.createInstance(uuid);
    }

    public static Intent fetchIntent(Context context, int registerConstant,UUID uuid)
    {
        Intent i=new Intent(context,StatActivity.class);
        i.putExtra(FRAGMENT_CODE,registerConstant);
        i.putExtra(UUID_CODE,uuid);
        return i;
    }


}
