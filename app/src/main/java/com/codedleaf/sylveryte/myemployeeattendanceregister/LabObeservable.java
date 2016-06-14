package com.codedleaf.sylveryte.myemployeeattendanceregister;

/**
 * Created by sylveryte on 14/6/16.
 */
public interface LabObeservable {

    void addListener(LabObserver labObserver);
    void alertAllObservers();
}
