package com.codedleaf.sylveryte.myemployeeattendanceregister;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by sylveryte on 27/6/16.
 */
public class EntryLab implements LabObeservable {

    private static EntryLab sEntryLab;
    private List<LabObserver> mLabObservers;
    private List<EntrySet> mEntrySets;

    private Context mContext;
    private SQLiteDatabase mDatabase;

    private EntryLab(Context context)
    {
        mContext=context.getApplicationContext();
        mDatabase=AttendanceBaseHelper.getDatabaseWritable(mContext);

        mLabObservers=new ArrayList<>();
        mEntrySets=new ArrayList<>();
    }

    public static EntryLab getInstanceOf(Context context)
    {
        if (sEntryLab==null)
        {
            sEntryLab=new EntryLab(context);
        }
        return sEntryLab;
    }

    public EntrySet getEntrySetBySiteIdAndDate(LocalDate date, UUID siteId)
    {
        EntrySet entrySetToReturn = null;
        boolean same=false;
        for (EntrySet entrySet:mEntrySets)
        {

            same = entrySet.getDate().equals(date)&&entrySet.getSiteId().equals(siteId);
            if (same)
            {
                entrySetToReturn=entrySet;
                break;
            }
        }

        if (!same)
        {
            EntrySet entrySetNew=new EntrySet(siteId,mContext);
            entrySetNew.setDate(date);
            addEntrySet(entrySetNew);
            entrySetToReturn=entrySetNew;
        }

        return entrySetToReturn;
    }

    public void addEntrySet(EntrySet entrySet)
    {
        mEntrySets.add(entrySet);
    }

    public void cleanseEntriesOfEmployeeId(UUID empId)
    {
        for (EntrySet entrySet:mEntrySets)
        {
            entrySet.cleanseEntriesOfEmployee(empId);
        }

        //// TODO: 5/7/16 db codes
    }

    public void cleanseEntriesOfSiteId(UUID siteId)
    {
        for (EntrySet entrySet:mEntrySets)
        {
            if (entrySet.getSiteId().equals(siteId))
            {
                mEntrySets.remove(entrySet);
            }
        }

        //// TODO: 5/7/16 db codes
    }

    @Override
    public void addListener(LabObserver labObserver) {
        mLabObservers.add(labObserver);
    }

    @Override
    public void alertAllObservers() {
        for (LabObserver labObserver :mLabObservers)
            labObserver.update();
    }
}
