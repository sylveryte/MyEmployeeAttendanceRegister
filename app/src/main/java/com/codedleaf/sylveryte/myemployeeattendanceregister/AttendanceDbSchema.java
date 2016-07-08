package com.codedleaf.sylveryte.myemployeeattendanceregister;

/**
 * Created by sylveryte on 6/7/16.
 */
public class AttendanceDbSchema {

    public static final class EntriesTable
    {
        public static final String NAME="entries";

        public static final class Cols
        {
            public static final String REMARK="remark";
            public static final String DATE="date";
            public static final String NOTE="note";
            public static final String SITEID="siteuuid";
            public static final String EMPLOYEEID="employeeuuid";
        }
    }


    public static final class DesignationsTable
    {
        public static final String NAME="designations";

        public static final class Cols
        {
            public static final String TITLE="title";
            public static final String DESC="description";
            public static final String UID="uuid";
        }
    }

    public static final class SitesTable
    {
        public static final String NAME="sites";

        public static final class Cols
        {
            public static final String TITLE="title";
            public static final String DESC="description";
            public static final String UID="uuid";
            public static final String BEGINDATE="begindate";
            public static final String ENDDATE="enddate";
        }
    }

    public static final class EmployeesTable
    {
        public static final String NAME="employees";

        public static final class Cols
        {
            public static final String NAME="name";
            public static final String NOTE="note";
            public static final String ADDRESS="address";
            public static final String AGE="age";
            public static final String GENDER="gender";
            public static final String ACTIVE="isactive";

            public static final String UID="uuid";
            public static final String SITEIDS="siteids";
            public static final String DESIGNATIONIDS="designationids";
        }
    }

}
