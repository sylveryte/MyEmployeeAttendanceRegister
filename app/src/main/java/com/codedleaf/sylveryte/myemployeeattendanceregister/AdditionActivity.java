package com.codedleaf.sylveryte.myemployeeattendanceregister;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import java.util.UUID;


public class AdditionActivity extends AppCompatActivity {


    public static final String FRAGMENT_CODE="codedleaf.addition.fragment.code";
    public static final int FRAGMENT_CODE_ADD_SITE=722;
    public static final int FRAGMENT_CODE_EDIT_SITE=122;
    public static final int FRAGMENT_CODE_ADD_EMPLOYEE=721;
    public static final int FRAGMENT_CODE_EDIT_EMPLOYEE=121;
    public static final int FRAGMENT_CODE_ADD_DESIGNATION=723;
    public static final int FRAGMENT_CODE_EDIT_DESIGNATION=123;
    public static final String FRAGMENT_STRING_EDIT_EMPLOYEE="codedleaf.edit.employee.fragment.code";
    public static final String FRAGMENT_STRING_EDIT_DESIGNATION="codedleaf.edit.designation.fragment.code";
    public static final String FRAGMENT_STRING_EDIT_SITE="codedleaf.edit.site.fragment.code";


    private SitesLab mSitesLab;
    private DesignationLab mDesignationLab;
    private EmployeeLab mEmployeeLab;

    private Context mContext;

    public static Intent fetchIntent(Context context, int fragCode)
    {
        Intent i=new Intent(context,AdditionActivity.class);
        i.putExtra(FRAGMENT_CODE,fragCode);
        return i;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_addition);
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.addition_fragment_container);


        Toolbar toolbar = (Toolbar) findViewById(R.id.Additiontoolbar);
        setSupportActionBar(toolbar);

//         causing to go to home activity to sites
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (fragment == null) {
            fragment = createFragment();
            fm.beginTransaction()
                    .add(R.id.addition_fragment_container, fragment)
                    .commit();
        }


        mContext=getApplicationContext();
    }

    protected Fragment createFragment() {


        if (mContext==null)
            mContext=getApplicationContext();

        mDesignationLab=DesignationLab.getInstanceOf(mContext);
        mSitesLab=SitesLab.getInstanceOf(mContext);
        mEmployeeLab=EmployeeLab.getInstanceOf(mContext);

        Intent intent=getIntent();
        int i=intent.getIntExtra(FRAGMENT_CODE,FRAGMENT_CODE_ADD_SITE);
        switch (i)
        {
            case FRAGMENT_CODE_ADD_SITE:
            {

                ////// TODO: 18/6/16  remove wala nikal dena lab se agar addition cancel hota hai toh

                Site site=new Site(mContext);
                mSitesLab.addSite(site);
                return SiteAdditionFragment.createInstance(site);
            }
            case FRAGMENT_CODE_ADD_EMPLOYEE:
            {
                Employee employee=new Employee(mContext);
                mEmployeeLab.addEmployee(employee);
                return EmployeeAdditionFragment.createInstance(employee);
            }
            case FRAGMENT_CODE_ADD_DESIGNATION:
            {
                Designation designation=new Designation(mContext);
                mDesignationLab.addDesignation(designation);
                return DesignationAdditionFragment.createInstance(designation);
            }
            case FRAGMENT_CODE_EDIT_EMPLOYEE:
            {
                UUID uuid=(UUID)intent.getSerializableExtra(FRAGMENT_STRING_EDIT_EMPLOYEE);
                return EmployeeAdditionFragment.createInstance(mEmployeeLab.getEmployeeById(uuid));
            }
            case FRAGMENT_CODE_EDIT_DESIGNATION:
            {
                UUID uuid=(UUID)intent.getSerializableExtra(FRAGMENT_STRING_EDIT_DESIGNATION);
                return DesignationAdditionFragment.createInstance(mDesignationLab.getDesigantionById(uuid));
            }
            case FRAGMENT_CODE_EDIT_SITE:
            {
                UUID uuid=(UUID)intent.getSerializableExtra(FRAGMENT_STRING_EDIT_SITE);
                Site site=mSitesLab.getSiteById(uuid);
                return SiteAdditionFragment.createInstance(site);
            }
        }

        Site site=new Site(mContext);
        return SiteAdditionFragment.createInstance(site);
    }
}
