package com.codedleaf.sylveryte.myemployeeattendanceregister;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

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

    private Context mContext;
    private SQLiteDatabase mDatabase;

    private DesignationLab(Context context)
    {
        mContext=context.getApplicationContext();
        mDatabase=AttendanceBaseHelper.getDatabaseWritable(mContext);

        mDesignations=new ArrayList<>();
        mLabObservers=new ArrayList<>();
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

    public static DesignationLab getInstanceOf(Context context)
    {
        if (sDesignationLab==null)
        {
            sDesignationLab=new DesignationLab(context);
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
