package com.codedleaf.sylveryte.myemployeeattendanceregister;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import java.util.UUID;

/**
 * Created by sylveryte on 27/6/16.
 */
public class AttendanceActivity extends SingleFragmentActivity {

    public static final String siteAttendance = "IAMBATMAN";
    private static final String FRAGMENT_CODE = "codedleaf.attendance.fragment.code";

    public static final int FRAGMENT_CODE_DESIGNATION = 1288;
    public static final int FRAGMENT_CODE_SITE = 1287;


    public static Intent fetchIntent(Context context, int fragCode) {
        Intent i = new Intent(context, AttendanceActivity.class);
        i.putExtra(FRAGMENT_CODE, fragCode);
        return i;
    }


    @Override
    protected Fragment createFragment() {

        Intent intent = getIntent();

        switch (intent.getIntExtra(FRAGMENT_CODE, FRAGMENT_CODE_SITE)) {
            case FRAGMENT_CODE_SITE: {
                return getSiteFragment(intent);
            }
        }

        return getSiteFragment(intent);
    }

    private Fragment getSiteFragment(Intent intent) {

        UUID siteId = (UUID) intent.getSerializableExtra(AttendanceActivity.siteAttendance);
        Site site = SitesLab.getInstanceOf(getApplicationContext()).getSiteById(siteId);

        return SiteAttendanceFragment.createInstance(site);
    }

}
