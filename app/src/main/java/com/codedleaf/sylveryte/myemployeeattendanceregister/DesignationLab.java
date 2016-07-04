package com.codedleaf.sylveryte.myemployeeattendanceregister;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by sylveryte on 15/6/16.
 */
public class DesignationLab implements LabObeservable {


    private List<Designation> mDesignations;
    private static DesignationLab sDesignationLab;
    private List<LabObserver> mLabObservers;

    private DesignationLab()
    {
        mDesignations=new ArrayList<>();
        mLabObservers=new ArrayList<>();

        //// TODO: 15/6/16 clean up this fake designations
//        for (int i=0;i<5;i++)
//        {
//            Designation designation=new Designation();
//            designation.setTitle("desfg "+i*232);
//            designation.setDescription("kya bas kya");
//            addDesignation(designation);
//        }
    }

    public void addDesignation(Designation designation)
    {
        mDesignations.add(designation);
        alertAllObservers();
    }

    public void deleteDesignation(Designation designation)
    {
        designation.delete();

        mDesignations.remove(designation);
        alertAllObservers();
    }

    public List<Designation> getDesignations() {
        return mDesignations;
    }

    public void addListener(LabObserver labObserver)
    {
        mLabObservers.add(labObserver);
    }

    public void alertAllObservers()
    {
        for (LabObserver labObserver :mLabObservers)
            labObserver.update();
    }

    public static DesignationLab getInstanceOf()
    {
        if (sDesignationLab==null)
        {
            sDesignationLab=new DesignationLab();
        }
        return sDesignationLab;
    }

    public String getDesignationStringById(UUID uuid)
    {
        return getDesigantionById(uuid).getTitle();
    }

    public Designation getDesigantionById(UUID uuid)
    {
        for (Designation designation: mDesignations)
        {
            if (designation.getId().equals(uuid))
                return designation;
        }
        return null;
    }
}
