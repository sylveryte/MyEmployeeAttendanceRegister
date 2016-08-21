package com.codedleaf.sylveryte.myemployeeattendanceregister.Labs;

/**
 * Created by sylveryte on 14/6/16.
 *
 * Copyright (C) 2016 sylveryte@codedleaf <codedleaf@gmail.com>
 *
 * This file is part of My Employee Attendance Register.
 *
 */
public interface LabObeservable {

    void addListener(LabObserver labObserver);
    void alertAllObservers();
}
