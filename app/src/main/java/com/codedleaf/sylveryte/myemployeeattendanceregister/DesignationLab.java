package com.codedleaf.sylveryte.myemployeeattendanceregister;

import java.util.ArrayList;
import java.util.List;
<<<<<<< HEAD
import java.util.Observer;
=======
>>>>>>> 90fd633ae6a99ff6fd78612066532db8e63b1857

/**
 * Created by sylveryte on 15/6/16.
 */
<<<<<<< HEAD
public class DesignationLab implements LabObeservable {
=======
public class DesignationLab {
>>>>>>> 90fd633ae6a99ff6fd78612066532db8e63b1857


    private List<Designation> mDesignations;
    private static DesignationLab sDesignationLab;
<<<<<<< HEAD
    private List<LabObserver> mLabObservers;
=======
>>>>>>> 90fd633ae6a99ff6fd78612066532db8e63b1857

    private DesignationLab()
    {
        mDesignations=new ArrayList<>();
<<<<<<< HEAD
        mLabObservers=new ArrayList<>();

        //// TODO: 15/6/16 clean up this fake designations
        for (int i=0;i<5;i++)
=======

        //// TODO: 15/6/16 clean up this fake designations
        for (int i=0;i<25;i++)
>>>>>>> 90fd633ae6a99ff6fd78612066532db8e63b1857
        {
            Designation designation=new Designation();
            designation.setTitle("desfg "+i*232);
            designation.setDescription("kya bas kya");
<<<<<<< HEAD
            addDesignation(designation);
        }
    }

    public void addDesignation(Designation designation)
    {
        mDesignations.add(designation);
        alertAllObservers();
    }
=======
            mDesignations.add(designation);
        }
    }

>>>>>>> 90fd633ae6a99ff6fd78612066532db8e63b1857

    public List<Designation> getDesignations() {
        return mDesignations;
    }

<<<<<<< HEAD
    public void addListener(LabObserver labObserver)
    {
        mLabObservers.add(labObserver);
    }

    public void alertAllObservers()
    {
        for (LabObserver labObserver :mLabObservers)
            labObserver.update();
    }

=======
>>>>>>> 90fd633ae6a99ff6fd78612066532db8e63b1857
    public static DesignationLab getInstanceOf()
    {
        if (sDesignationLab==null)
        {
            sDesignationLab=new DesignationLab();
        }
        return sDesignationLab;
    }
}
