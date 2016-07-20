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

    private HashMap<String,List<UUID>> mMap;

    private PickCache()
    {
        mMap=new HashMap<>(3);
    }

    private void addPickables(String id,List<UUID> list)
    {
        mMap.put(id,list);
    }

    public List<UUID> getPickables(String id)
    {
        List<UUID> list=mMap.get(id);
        if (list==null)
        {
            list=new ArrayList<>();
            addPickables(id,list);
        }
        return list;
    }

    public void addThisInMyList(String id,List<UUID> list)
    {
        getPickables(id).addAll(list);
    }

    public void destroyMyCache(String id)
    {
        mMap.remove(id);
    }

    public static PickCache getInstance()
    {
        if (sPickCache==null)
            sPickCache=new PickCache();
        return sPickCache;
    }
}
