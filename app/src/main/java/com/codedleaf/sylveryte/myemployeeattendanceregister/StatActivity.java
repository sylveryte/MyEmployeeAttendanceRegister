package com.codedleaf.sylveryte.myemployeeattendanceregister;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.UUID;

public class StatActivity extends AppCompatActivity {


    private static final String FRAGMENT_CODE = "stat_vagaira_ka";
    private static final String UUID_CODE = "uuid_vagaira_ka";

    public static final int FRAGMENT_CODE_STAT_SITE = 30;
    public static final int FRAGMENT_CODE_STAT_EMPLOYEE = 31;
    public static final int FRAGMENT_CODE_STAT_DESIGNATION = 32;

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stat);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if (fab!=null)
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.stat_fragment_container);

        if (fragment == null) {
            fragment = createFragment();
            fm.beginTransaction()
                    .add(R.id.stat_fragment_container, fragment)
                    .commit();
        }


        mContext=getApplicationContext();
    }

    protected Fragment createFragment() {


        if (mContext==null)
            mContext=getApplicationContext();

        DesignationLab designationLab = DesignationLab.getInstanceOf(mContext);
        SitesLab sitesLab = SitesLab.getInstanceOf(mContext);
        EmployeeLab employeeLab = EmployeeLab.getInstanceOf(mContext);

        Intent intent=getIntent();
        int i=intent.getIntExtra(FRAGMENT_CODE,FRAGMENT_CODE_STAT_SITE);
        UUID uuid=(UUID)intent.getSerializableExtra(UUID_CODE);

        switch (i)
        {
            case FRAGMENT_CODE_STAT_SITE:
            {
                Site site= sitesLab.getSiteById(uuid);
                return SiteStatFragment.createInstance(site);
            }
            case FRAGMENT_CODE_STAT_EMPLOYEE:
            {
                Employee employee= employeeLab.getEmployeeById(uuid);
                return EmployeeStatFragment.createInstance(employee);
            }
            case FRAGMENT_CODE_STAT_DESIGNATION:
            {
                Designation designation= designationLab.getDesigantionById(uuid);
                return DesignationStatFragment.createInstance(designation);
            }
        }

        Site site= sitesLab.getSites().get(0);
        return SiteStatFragment.createInstance(site);
    }

    public static Intent fetchIntent(Context context, int fragCode,UUID uuid)
    {
        Intent i=new Intent(context,StatActivity.class);
        i.putExtra(FRAGMENT_CODE,fragCode);
        i.putExtra(UUID_CODE,uuid);
        return i;
    }


}
