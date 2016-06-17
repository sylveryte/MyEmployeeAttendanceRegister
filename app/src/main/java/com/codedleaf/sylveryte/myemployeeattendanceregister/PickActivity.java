package com.codedleaf.sylveryte.myemployeeattendanceregister;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

/**
 * Created by sylveryte on 17/6/16.
 */
public class PickActivity extends SingleFragmentActivity {


    public static final String FRAGMENT_CODE="codedleaf.pick.fragment.code";
    public static final int FRAGMENT_CODE_PICK_SITE=422;
    public static final int FRAGMENT_CODE_PICK_EMPLOYEE=421;
    public static final int FRAGMENT_CODE_PICK_DESIGNATION=423;

    public static Intent fetchIntent(Context context, int fragCode)
    {
        Intent i=new Intent(context,AdditionActivity.class);
        i.putExtra(FRAGMENT_CODE,fragCode);
        return i;
    }

    @Override
    protected Fragment createFragment() {
        switch (getIntent().getIntExtra(FRAGMENT_CODE,FRAGMENT_CODE_PICK_SITE))
        {
            case FRAGMENT_CODE_PICK_SITE:
            {
            }
            case FRAGMENT_CODE_PICK_EMPLOYEE:
            {
            }
            case FRAGMENT_CODE_PICK_DESIGNATION:
            {
            }
        }
        return null;
    }
}
