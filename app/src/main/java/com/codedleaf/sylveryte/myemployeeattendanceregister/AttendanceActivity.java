package com.codedleaf.sylveryte.myemployeeattendanceregister;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import java.util.UUID;

/**
 * Created by sylveryte on 27/6/16.
 */
public class AttendanceActivity extends AppCompatActivity {

    public static final String siteAttendance = "IAMBATMAN";
    private static final String FRAGMENT_CODE = "codedleaf.attendance.fragment.code";

    public static final int FRAGMENT_CODE_DESIGNATION = 1288;
    public static final int FRAGMENT_CODE_SITE = 1287;


    public static Intent fetchIntent(Context context, int fragCode) {
        Intent i = new Intent(context, AttendanceActivity.class);
        i.putExtra(FRAGMENT_CODE, fragCode);
        return i;
    }


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.attendance_fragment_container);

        if (fragment == null) {
            fragment = createFragment();
            fm.beginTransaction()
                    .add(R.id.attendance_fragment_container, fragment)
                    .commit();
        }
    }

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
