package com.codedleaf.sylveryte.myemployeeattendanceregister;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sylveryte on 15/6/16.
 */
public class DesignationLab {


    private List<Designation> mDesignations;
    private static DesignationLab sDesignationLab;

    private DesignationLab()
    {
        mDesignations=new ArrayList<>();

        //// TODO: 15/6/16 clean up this fake designations
        for (int i=0;i<25;i++)
        {
            Designation designation=new Designation();
            designation.setTitle("desfg "+i*232);
            designation.setDescription("kya bas kya");
            mDesignations.add(designation);
        }
    }


    public List<Designation> getDesignations() {
        return mDesignations;
    }

    public static DesignationLab getInstanceOf()
    {
        if (sDesignationLab==null)
        {
            sDesignationLab=new DesignationLab();
        }
        return sDesignationLab;
    }
}
