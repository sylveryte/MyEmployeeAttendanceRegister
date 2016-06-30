package com.codedleaf.sylveryte.myemployeeattendanceregister;

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
            EntrySet entrySetNew=new EntrySet(siteId);
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
