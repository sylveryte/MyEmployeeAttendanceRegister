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

    private HashMap<String,List<UUID>> UUIDMap;
    private HashMap<String,DialogPickObserver> ObserverMap;
    private HashMap<String,List<Pickable>> PickableMap;

    private PickCache()
    {
        UUIDMap =new HashMap<>(5);
        ObserverMap=new HashMap<>(5);
        PickableMap=new HashMap<>(5);
    }

    private void addUUIDs(String id, List<UUID> list)
    {
        UUIDMap.put(id,list);
    }

    public void addObserver(String id,DialogPickObserver observer)
    {
        ObserverMap.put(id,observer);
    }
    public DialogPickObserver getObserver(String id)
    {
        return ObserverMap.get(id);
    }

    public void addPickables(String id,List<Pickable> pickables)
    {
        PickableMap.put(id,pickables);
    }
    public List<Pickable> getPickable(String id)
    {
        return PickableMap.get(id);
    }

    public List<UUID> getUUIDs(String id)
    {
        List<UUID> list= UUIDMap.get(id);
        if (list==null)
        {
            list=new ArrayList<>();
            addUUIDs(id,list);
        }
        return list;
    }

    public List<UUID> getRemovedUUIDs(String id)
    {
        id+="r";

        List<UUID> list= UUIDMap.get(id);
        if (list==null)
        {
            list=new ArrayList<>();
            addUUIDs(id,list);
        }
        return list;
    }

    public void addThisInMyList(String id,List<UUID> list)
    {
        if (list==null)
            return;
        getUUIDs(id).addAll(list);
    }

    public void destroyMyCache(String id)
    {
        UUIDMap.remove(id);
        UUIDMap.remove(id+"r");
    }

    public static PickCache getInstance()
    {
        if (sPickCache==null)
            sPickCache=new PickCache();
        return sPickCache;
    }
}
