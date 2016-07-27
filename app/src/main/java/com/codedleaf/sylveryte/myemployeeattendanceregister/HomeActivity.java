package com.codedleaf.sylveryte.myemployeeattendanceregister;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

/**
 * Created by sylveryte on 27/6/16.
 *
 * Copyright (C) 2016 sylveryte@codedleaf <codedlaf@gmail.com>
 *
 * This file is part of My Employee Attendance Register.
 *
 */

public class HomeActivity extends AppCompatActivity {


    private static final String DIALOG_FRAGMENT_CODE = "codedleafbrodialog";


    private com.getbase.floatingactionbutton.FloatingActionsMenu mMenu;


    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        final com.getbase.floatingactionbutton.FloatingActionButton addSite=(com.getbase.floatingactionbutton.FloatingActionButton)findViewById(R.id.floating_add_site);
        com.getbase.floatingactionbutton.FloatingActionButton addDesignation=(com.getbase.floatingactionbutton.FloatingActionButton)findViewById(R.id.floating_add_designation);
        com.getbase.floatingactionbutton.FloatingActionButton addEmployee=(com.getbase.floatingactionbutton.FloatingActionButton)findViewById(R.id.floating_add_employee);


        mMenu=(com.getbase.floatingactionbutton.FloatingActionsMenu)findViewById(R.id.floating_add_menu);

        if (addSite != null) {
            addSite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startAddSite();
                }
            });
        }

        //floating button
        if (addDesignation != null) {
            addDesignation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startAddDesignation();
                }
            });
        }

        //floating button
        if (addEmployee != null) {
            addEmployee.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startAddEmployee();
                }
            });
        }


        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        mViewPager.setCurrentItem(1);
    }



    private void startAddEmployee() {
        EmployeeAdditionFragmentDialog.getDialogFrag(null)
                .show(getSupportFragmentManager(),DIALOG_FRAGMENT_CODE);


        closeFloatingMenu();
    }

    private void startAddDesignation() {
        DesignationAdditionDialogFragment.getDialogFrag(null)
                .show(getSupportFragmentManager(),DIALOG_FRAGMENT_CODE);

        closeFloatingMenu();
    }

    private void startAddSite() {
        SiteAdditionDialogFragment.getSiteFrag(null)
                .show(getSupportFragmentManager(),DIALOG_FRAGMENT_CODE);

        closeFloatingMenu();
    }

    private void closeFloatingMenu() {
        if (mMenu != null) {
            mMenu.collapse();
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the mMenu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id)
        {
            case R.id.action_settings :
            {
                break;
            }
            case R.id.addSite:
            {
                startAddSite();
                break;
            }
            case R.id.addDesignation:
            {
                startAddDesignation();
                break;
            }
            case R.id.addEmployee:
            {
                startAddEmployee();
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch (position) {
                case 0:
                    return EmployeeFragment.newInstance();
                case 1:
                    return SitesFragment.newInstance();
                case 2:
                    return DesignationFragment.newInstance();
            }
            return null;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Employee";
                case 1:
                    return "Site";
                case 2:
                    return "Designation";
            }
            return null;
        }
    }

}
