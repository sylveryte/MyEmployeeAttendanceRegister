package com.codedleaf.sylveryte.myemployeeattendanceregister;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;


public class AdditionActivity extends SingleFragmentActivity {


    public static final String FRAGMENT_CODE="codedleaf.addition.fragment.code";
    public static final int FRAGMENT_CODE_ADD_SITE=722;
    public static final int FRAGMENT_CODE_ADD_EMPLOYEE=721;
    public static final int FRAGMENT_CODE_ADD_DESIGNATION=723;

    public static Intent fetchIntent(Context context, int fragCode)
    {
        Intent i=new Intent(context,AdditionActivity.class);
        i.putExtra(FRAGMENT_CODE,fragCode);
        return i;
    }

    @Override
    protected Fragment createFragment() {
        switch (getIntent().getIntExtra(FRAGMENT_CODE,FRAGMENT_CODE_ADD_SITE))
        {
            case FRAGMENT_CODE_ADD_SITE:
            {
                return SiteAdditionFragment.createInstance();
            }
            case FRAGMENT_CODE_ADD_EMPLOYEE:
            {
                return EmployeeAdditionFragment.createInstance();
            }
            case FRAGMENT_CODE_ADD_DESIGNATION:
            {
                return DesignationAdditionFragment.createInstance();
            }
        }

        return SiteAdditionFragment.createInstance();
    }
}
