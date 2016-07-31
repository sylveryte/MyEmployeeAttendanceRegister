package com.codedleaf.sylveryte.myemployeeattendanceregister.Picknation;

import java.util.UUID;

/**
 * Created by sylveryte on 17/6/16.
 *
 * Copyright (C) 2016 sylveryte@codedleaf <codedlaf@gmail.com>
 *
 * This file is part of My Employee Attendance Register.
 *
 */
public interface Pickable {

    String getTitle();
    String getDescription();
    UUID getId();
    int getType();
}
