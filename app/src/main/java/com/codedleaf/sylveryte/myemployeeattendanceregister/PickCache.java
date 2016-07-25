package com.codedleaf.sylveryte.myemployeeattendanceregister;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * Created by sylveryte on 20/7/16.
 */
public class PickCache {
    private static PickCache sPickCache;

    private HashMap<String,List<UUID>> mForPickMap;
    private HashMap<String,List<Pickable>> mForShowMap;
    private HashMap<String,DialogPickObserver> ObserverMap;

    private PickCache()
    {
        mForPickMap =new HashMap<>(1);
        mForShowMap =new HashMap<>(1);
        ObserverMap=new HashMap<>(1);
    }

    public void storePicked(String id, List<UUID> list)
    {
        mForPickMap.put(id,list);
    }

    public void storePickables(String id,List<Pickable> pickables)
    {
        mForShowMap.put(id,pickables);
    }
    public List<Pickable> getPickables(String id)
    {
        return mForShowMap.get(id);
    }

    public void storeRemoved(String id, List<UUID> list)
    {
        storePicked(id+"r",list);
    }

    public List<UUID> getPicked(String id)
    {
        List<UUID> toReturn=new ArrayList<>(0);
        if (mForPickMap.containsKey(id))
        {
            toReturn= mForPickMap.get(id);
            mForPickMap.remove(id);
        }

        return toReturn;
    }

    public List<UUID> getRemoved(String id)
    {
        return getPicked(id+"r");
    }


    public void addObserver(String id,DialogPickObserver observer)
    {
        ObserverMap.put(id,observer);
    }
    public DialogPickObserver getObserver(String id)
    {
        return ObserverMap.get(id);
    }




    public static PickCache getInstance()
    {
        if (sPickCache==null)
            sPickCache=new PickCache();
        return sPickCache;
    }
}
