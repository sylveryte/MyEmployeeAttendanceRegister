package com.codedleaf.sylveryte.myemployeeattendanceregister;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import java.util.List;
import java.util.Objects;

/**
 * Created by sylveryte on 17/6/16.
 */
public class PickActivity extends SingleFragmentActivity {


    public static final String FRAGMENT_CODE="codedleaf.pick.fragment.code";
    public static final int FRAGMENT_CODE_PICK_SITE=422;
    public static final int FRAGMENT_CODE_PICK_EMPLOYEE=421;
    public static final int FRAGMENT_CODE_PICK_DESIGNATION=423;

    public static final int RESULT_CODE_PICK_GENRAL=800;

    public static final String RESULT_DATA_STRING_PICK_GENRAL="codedleaf.result.returned.uuid";


    public static Intent fetchIntent(Context context, int fragCode)
    {
        Intent i=new Intent(context,PickActivity.class);
        i.putExtra(FRAGMENT_CODE,fragCode);
        return i;
    }

    @Override
    protected Fragment createFragment() {

        List<? extends Pickable> list;

        int code;
        code = getIntent().getIntExtra(FRAGMENT_CODE,FRAGMENT_CODE_PICK_SITE);

        switch (code)
        {
            case FRAGMENT_CODE_PICK_SITE:
            {
                list=SitesLab.getInstanceOf().getSites();
                break;
            }
            case FRAGMENT_CODE_PICK_EMPLOYEE:
            {
                list=EmployeeLab.getInstanceOf().getEmployees();
                break;
            }
            case FRAGMENT_CODE_PICK_DESIGNATION:
            {
                list=DesignationLab.getInstanceOf().getDesignations();
                break;
            }
            default:
            {
                list=SitesLab.getInstanceOf().getSites();
                break;
            }
        }
        return  PickFragment.newInstance(list);
    }
}
