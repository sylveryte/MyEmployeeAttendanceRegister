package com.codedleaf.sylveryte.myemployeeattendanceregister;

import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by sylveryte on 27/6/16.
 */
public class EntryLab implements LabObeservable {

    private static EntryLab sEntryLab;
    private List<LabObserver> mLabObservers;
    private List<EntrySet> mEntrySets;

    private EntryLab()
    {
        mLabObservers=new ArrayList<>();
        mEntrySets=new ArrayList<>();
    }

    public static EntryLab getInstanceOf()
    {
        if (sEntryLab==null)
        {
            sEntryLab=new EntryLab();
        }
        return sEntryLab;
    }

    public EntrySet getEntrySetBySiteIdAndDate(Date date, UUID siteId)
    {
        for (EntrySet entrySet:mEntrySets)
        {
            if (entrySet.getDate().compareTo(date)!=0&&entrySet.getSiteId().equals(siteId))
            {
                return entrySet;
            }
        }
        EntrySet entrySet=new EntrySet(siteId);
        entrySet.setDate(date);
        mEntrySets.add(entrySet);
        return entrySet;
    }

    public void addEntrySet(EntrySet entrySet)
    {
        mEntrySets.add(entrySet);
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
