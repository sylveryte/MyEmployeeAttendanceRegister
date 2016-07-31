package com.codedleaf.sylveryte.myemployeeattendanceregister.SiteAttendance;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.codedleaf.sylveryte.myemployeeattendanceregister.RegisterConstants;
import com.codedleaf.sylveryte.myemployeeattendanceregister.SingleFragmentActivity;

import java.util.UUID;

/**
 * Created by sylveryte on 27/6/16.
 *
 * Copyright (C) 2016 sylveryte@codedleaf <codedlaf@gmail.com>
 *
 * This file is part of My Employee Attendance Register.
 *
 */
public class AttendanceActivity extends SingleFragmentActivity {

    public static final String siteAttendance = "IAMBATMAN";
    private static final String FRAGMENT_CODE = "codedleaf.attendance.fragment.code";


    public static Intent fetchIntent(Context context, int registerConstant) {
        Intent i = new Intent(context, AttendanceActivity.class);
        i.putExtra(FRAGMENT_CODE, registerConstant);
        return i;
    }


    @Override
    protected Fragment createFragment() {

        Intent intent = getIntent();

        switch (intent.getIntExtra(FRAGMENT_CODE, RegisterConstants.SITE)) {
            case RegisterConstants.SITE: {
                return getSiteFragment(intent);
            }
        }

        return getSiteFragment(intent);
    }

    private Fragment getSiteFragment(Intent intent) {

        UUID siteId = (UUID) intent.getSerializableExtra(AttendanceActivity.siteAttendance);

        return SiteAttendanceTaking.createInstance(siteId);
    }

}
