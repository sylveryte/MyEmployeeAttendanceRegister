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

public class StatActivity extends SingleFragmentActivity {


    private static final String FRAGMENT_CODE = "stat_vagaira_ka";
    private static final String UUID_CODE = "uuid_vagaira_ka";


    @Override
    protected Fragment createFragment() {

        DesignationLab designationLab = DesignationLab.getInstanceOf(this);
        SitesLab sitesLab = SitesLab.getInstanceOf(this);
        EmployeeLab employeeLab = EmployeeLab.getInstanceOf(this);

        Intent intent=getIntent();
        int i=intent.getIntExtra(FRAGMENT_CODE,RegisterConstants.SITE);
        UUID uuid=(UUID)intent.getSerializableExtra(UUID_CODE);

        switch (i)
        {
            case RegisterConstants.SITE:
            {
                Site site= sitesLab.getSiteById(uuid);
                return SiteStatFragment.createInstance(site);
            }
            case RegisterConstants.EMPLOYEE:
            {
                Employee employee= employeeLab.getEmployeeById(uuid);
                return EmployeeStatFragment.createInstance(employee);
            }
            case RegisterConstants.DESIGNATION:
            {
                Designation designation= designationLab.getDesigantionById(uuid);
                return DesignationStatFragment.createInstance(designation);
            }
        }

        Site site= sitesLab.getSites().get(0);
        return SiteStatFragment.createInstance(site);
    }

    public static Intent fetchIntent(Context context, int registerConstant,UUID uuid)
    {
        Intent i=new Intent(context,StatActivity.class);
        i.putExtra(FRAGMENT_CODE,registerConstant);
        i.putExtra(UUID_CODE,uuid);
        return i;
    }


}
