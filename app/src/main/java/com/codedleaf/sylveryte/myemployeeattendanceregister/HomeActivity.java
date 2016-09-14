package com.codedleaf.sylveryte.myemployeeattendanceregister;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.codedleaf.sylveryte.myemployeeattendanceregister.Editing.DesignationAdditionDialogFragment;
import com.codedleaf.sylveryte.myemployeeattendanceregister.Editing.EmployeeAdditionFragmentDialog;
import com.codedleaf.sylveryte.myemployeeattendanceregister.Editing.SiteAdditionDialogFragment;
import com.codedleaf.sylveryte.myemployeeattendanceregister.GeneralFragments.DesignationFragment;
import com.codedleaf.sylveryte.myemployeeattendanceregister.GeneralFragments.EmployeeFragment;
import com.codedleaf.sylveryte.myemployeeattendanceregister.GeneralFragments.SitesFragment;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabClickListener;

/**
 * Created by sylveryte on 27/6/16.
 *
 * Copyright (C) 2016 sylveryte@codedleaf <codedleaf@gmail.com>
 *
 * This file is part of My Employee Attendance Register.
 *
 */

public class HomeActivity extends AppCompatActivity {


    private static final String DIALOG_FRAGMENT_CODE = "codedleafbrodialog";


    private com.getbase.floatingactionbutton.FloatingActionsMenu mMenu;

    private BottomBar mBottomBar;

    private FragmentManager mFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Register");

        final com.getbase.floatingactionbutton.FloatingActionButton addSite=(com.getbase.floatingactionbutton.FloatingActionButton)findViewById(R.id.floating_add_site);
        final com.getbase.floatingactionbutton.FloatingActionButton addDesignation=(com.getbase.floatingactionbutton.FloatingActionButton)findViewById(R.id.floating_add_designation);
        final com.getbase.floatingactionbutton.FloatingActionButton addEmployee=(com.getbase.floatingactionbutton.FloatingActionButton)findViewById(R.id.floating_add_employee);


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

        mFragmentManager = getSupportFragmentManager();

        mBottomBar = BottomBar.attachShy((CoordinatorLayout) findViewById(R.id.main_content),
                findViewById(R.id.fragment_container_home), savedInstanceState);

        mBottomBar.setMaxFixedTabs(2);
        mBottomBar.noTabletGoodness();

        mBottomBar.setItems(R.menu.menu_bottom);
        mBottomBar.setDefaultTabPosition(1);
        mBottomBar.setOnMenuTabClickListener(new OnMenuTabClickListener() {
                                                 @Override
                                                 public void onMenuTabSelected(@IdRes int menuItemId) {
                                                     switch (menuItemId)
                                                     {
                                                         case R.id.employee_bottomo_menu :
                                                         {
                                                              changeFragment(RegisterConstants.EMPLOYEE);
                                                             break;
                                                         }
                                                         case R.id.site_bottom_menu :
                                                         {
                                                             changeFragment(RegisterConstants.SITE);
                                                             break;
                                                         }
                                                         case R.id.designation_bottom_menu :
                                                         {
                                                             changeFragment(RegisterConstants.DESIGNATION);
                                                             break;
                                                         }
                                                     }
                                                 }

                                                 @Override
                                                 public void onMenuTabReSelected(@IdRes int menuItemId) {

                                                     // TODO: 2/8/16 code to go to top of the fragment
                                                 }
                                             });



        //for some reason this aint working
        mBottomBar.mapColorForTab(0, ContextCompat.getColor(this,R.color.colorEmployeeCardDeactivated));

        mBottomBar.mapColorForTab(1, ContextCompat.getColor(this,R.color.colorSiteCardDeactivated));

        mBottomBar.mapColorForTab(2, ContextCompat.getColor(this,R.color.colorDesignationCardDeactivated));
    }


    private void changeFragment(int code) {
        Fragment fragment=mFragmentManager.findFragmentByTag(String.valueOf(code));
        detachFrag();

        ActionBar bar=getSupportActionBar();
        //for color
        if (bar!=null)
        {
            switch (code) {
                case RegisterConstants.EMPLOYEE: {
                    bar.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this,R.color.colorEmployeeCardDeactivated)));
                    break;
                }
                case RegisterConstants.SITE: {
                    bar.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this,R.color.colorSiteCardDeactivated)));
                    break;
                }
                case RegisterConstants.DESIGNATION: {
                    bar.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this,R.color.colorDesignationCardDeactivated)));
                    break;
                }
            }
        }

        if (fragment==null)
        {
            switch (code)
            {
                case RegisterConstants.EMPLOYEE:
                {
                    fragment=EmployeeFragment.newInstance();

                    mFragmentManager.beginTransaction()
                            .add(R.id.fragment_container_home,fragment,String.valueOf(RegisterConstants.EMPLOYEE))
                            .commitAllowingStateLoss();
                    return;
                }
                case RegisterConstants.SITE:
                {
                    fragment=SitesFragment.newInstance();
                    mFragmentManager.beginTransaction()
                            .add(R.id.fragment_container_home,fragment,String.valueOf(RegisterConstants.SITE))
                            .commitAllowingStateLoss();

                    //to load employee fragment
                    new LoadFragment().execute();
//                    //// TODO: 12/9/16 use async
//                    new Thread(new Runnable() {
//                        @Override
//                        public void run() {
//                            Fragment fragment1=EmployeeFragment.newInstance();
//                            mFragmentManager.beginTransaction()
//                                    .add(R.id.fragment_container_home,fragment1,String.valueOf(RegisterConstants.EMPLOYEE))
//                                    .detach(fragment1)
//                                    .commitAllowingStateLoss();
//                        }
//                    }).start();
                    return;
                }
                case RegisterConstants.DESIGNATION:
                {
                    fragment= DesignationFragment.newInstance();
                    mFragmentManager.beginTransaction()
                            .add(R.id.fragment_container_home,fragment,String.valueOf(RegisterConstants.DESIGNATION))
                            .commitAllowingStateLoss();
                    return;
                }
            }
        }
            mFragmentManager.beginTransaction()
                    .attach(fragment)
                    .commit();
    }

    class LoadFragment extends AsyncTask<Void,Void,Fragment>
    {
        @Override
        protected Fragment doInBackground(Void... params) {

            return EmployeeFragment.newInstance();
        }

        @Override
        protected void onPostExecute(Fragment fragment) {
            super.onPostExecute(fragment);
            mFragmentManager.beginTransaction()
                    .add(R.id.fragment_container_home,fragment,String.valueOf(RegisterConstants.EMPLOYEE))
                    .detach(fragment)
                    .commitAllowingStateLoss();
        }
    }

    private void detachFrag()
    {
        Fragment fragment=mFragmentManager.findFragmentById(R.id.fragment_container_home);
        if (fragment==null)
            return;
        mFragmentManager.beginTransaction()
                .detach(mFragmentManager.findFragmentById(R.id.fragment_container_home))
                .commit();
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
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            //Do some stuff
            getMenuInflater().inflate(R.menu.menu_home_land, menu);
        }
        else
            getMenuInflater().inflate(R.menu.menu_home,menu);
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mBottomBar.onSaveInstanceState(outState);
    }
}
