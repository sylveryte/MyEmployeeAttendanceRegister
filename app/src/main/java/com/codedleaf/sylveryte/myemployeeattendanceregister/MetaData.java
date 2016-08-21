package com.codedleaf.sylveryte.myemployeeattendanceregister;

/**
 * Created by sylveryte on 28/7/16.
 * <p>
 * Copyright (C) 2016 sylveryte@codedleaf <codedleaf@gmail.com>
 * <p>
 * This file is part of My Employee Attendance Register.
 */
public class MetaData {
    private static MetaData ourInstance = new MetaData();

    //1 means monday 0 means sunday
    public static int FIRSTWEEKOFDAY=1;

    public static String EMPLOYEE="employee";
    public static String SITE="site";
    public static String DESIGNATION="designation";

    public static int getFIRSTWEEKOFDAY() {
        return FIRSTWEEKOFDAY;
    }

    public static void setFIRSTWEEKOFDAY(int FIRSTWEEKOFDAY) {
        MetaData.FIRSTWEEKOFDAY = FIRSTWEEKOFDAY;
    }

    public static String getEMPLOYEE() {
        return EMPLOYEE;
    }

    public static void setEMPLOYEE(String EMPLOYEE) {
        MetaData.EMPLOYEE = EMPLOYEE;
    }

    public static String getSITE() {
        return SITE;
    }

    public static void setSITE(String SITE) {
        MetaData.SITE = SITE;
    }

    public static String getDESIGNATION() {
        return DESIGNATION;
    }

    public static void setDESIGNATION(String DESIGNATION) {
        MetaData.DESIGNATION = DESIGNATION;
    }
}
