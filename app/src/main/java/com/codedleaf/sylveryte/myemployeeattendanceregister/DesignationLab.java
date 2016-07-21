package com.codedleaf.sylveryte.myemployeeattendanceregister;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;

import com.codedleaf.sylveryte.myemployeeattendanceregister.AttendanceDbSchema.DesignationsTable;

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

    private SQLiteDatabase mDatabase;

    private DesignationLab(Context context)
    {

        mDatabase=AttendanceBaseHelper.getDatabaseWritable(context);
        mLabObservers=new ArrayList<>();
        mDesignations=new ArrayList<>();
    }


    private void initializeDatabase(Context context)
    {

        DesignationCursorWrapper cursorWrapper=queryDesignations(null,null);
        try
        {
            cursorWrapper.moveToFirst();
            while (!cursorWrapper.isAfterLast())
            {
                mDesignations.add(cursorWrapper.getDesignation());
                cursorWrapper.moveToNext();
            }
        }
        finally
        {
            cursorWrapper.close();
        }
    }

    public void addDesignation(Designation designation)
    {
        mDesignations.add(designation);
        alertAllObservers();

        ContentValues values= AttendanceDbToolsProvider.getContentValues(designation);
        mDatabase.insert(DesignationsTable.NAME,null,values);
    }

    public void updateDesignation(Designation designation)
    {
        alertAllObservers();
        String designationIdString=designation.getId().toString();
        ContentValues values= AttendanceDbToolsProvider.getContentValues(designation);
        mDatabase.update(DesignationsTable.NAME,values,DesignationsTable.Cols.UID+"=?",new String[]{designationIdString});
    }

    public void deleteDesignation(Designation designation,Context context)
    {
        designation.delete(context);

        mDesignations.remove(designation);
        alertAllObservers();

        String designationIdString=designation.getId().toString();
        mDatabase.delete(DesignationsTable.NAME,DesignationsTable.Cols.UID+"=?",new String[]{designationIdString});
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
            sDesignationLab.initializeDatabase(context);
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

    private DesignationCursorWrapper queryDesignations(String whereClause, String[] wherwArgs)
    {
        Cursor cursor=mDatabase.query(
                DesignationsTable.NAME,
                null, //columns null coz select all columns :)
                whereClause,
                wherwArgs,
                null, //group by
                null, //having
                null //orderby
        );

        return new DesignationCursorWrapper(cursor);

    }

    private  class DesignationCursorWrapper extends CursorWrapper
    {
        public DesignationCursorWrapper(Cursor cursor)
        {
            super(cursor);
        }

        public Designation getDesignation()
        {
            String uuidString=getString(getColumnIndex(DesignationsTable.Cols.UID));
            String description=getString(getColumnIndex(DesignationsTable.Cols.DESC));
            String title=getString(getColumnIndex(DesignationsTable.Cols.TITLE));

            Designation designation=new Designation(UUID.fromString(uuidString));
            designation.setTitle(title);
            designation.setDescription(description);

            return designation;
        }
    }
}
