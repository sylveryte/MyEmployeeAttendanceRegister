package com.codedleaf.sylveryte.myemployeeattendanceregister.SQLite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by sylveryte on 6/7/16.
 *
 * Copyright (C) 2016 sylveryte@codedleaf <codedlaf@gmail.com>
 *
 * This file is part of My Employee Attendance Register.
 *
 */
public class AttendanceBaseHelper extends SQLiteOpenHelper {

    private static final int VERSION=1;
    private static final String DATABASE_NAME="register.db";
    private static SQLiteDatabase sDatabaseWritable;
    private static SQLiteDatabase sDatabaseReadable;

    public AttendanceBaseHelper(Context context)
    {
        super(context,DATABASE_NAME,null,VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table " + AttendanceDbSchema.EntriesTable.NAME + "("+
        " _id integer primary key autoincrement, " +
                AttendanceDbSchema.EntriesTable.Cols.EMPLOYEEID+","+
                AttendanceDbSchema.EntriesTable.Cols.DAY+" INTEGER,"+
                AttendanceDbSchema.EntriesTable.Cols.MONTH+" INTEGER,"+
                AttendanceDbSchema.EntriesTable.Cols.YEAR+" INTEGER,"+
                AttendanceDbSchema.EntriesTable.Cols.NOTE+","+
                AttendanceDbSchema.EntriesTable.Cols.REMARK+" INTEGER,"+
                AttendanceDbSchema.EntriesTable.Cols.SITEID+
                ")"

        );

        db.execSQL("create table " + AttendanceDbSchema.DesignationsTable.NAME + "("+
                " _id integer primary key autoincrement, " +
                AttendanceDbSchema.DesignationsTable.Cols.DESC+","+
                AttendanceDbSchema.DesignationsTable.Cols.TITLE+","+
                AttendanceDbSchema.DesignationsTable.Cols.UID+
                ")"
        );

        db.execSQL("create table " + AttendanceDbSchema.SitesTable.NAME + "("+
                " _id integer primary key autoincrement, " +
                AttendanceDbSchema.SitesTable.Cols.UID+","+
                AttendanceDbSchema.SitesTable.Cols.TITLE+","+
                AttendanceDbSchema.SitesTable.Cols.DESC+","+
                AttendanceDbSchema.SitesTable.Cols.ACTIVE+","+
                AttendanceDbSchema.SitesTable.Cols.BEGINDATE+","+
                AttendanceDbSchema.SitesTable.Cols.ENDDATE+
                ")"
        );

        db.execSQL("create table " + AttendanceDbSchema.EmployeesTable.NAME + "("+
                " _id integer primary key autoincrement, " +
                AttendanceDbSchema.EmployeesTable.Cols.UID+","+
                AttendanceDbSchema.EmployeesTable.Cols.NAME+","+
                AttendanceDbSchema.EmployeesTable.Cols.ADDRESS+","+
                AttendanceDbSchema.EmployeesTable.Cols.ACTIVE+","+
                AttendanceDbSchema.EmployeesTable.Cols.AGE+","+
                AttendanceDbSchema.EmployeesTable.Cols.SITEIDS+","+
                AttendanceDbSchema.EmployeesTable.Cols.GENDER+","+
                AttendanceDbSchema.EmployeesTable.Cols.DESIGNATIONIDS+","+
                AttendanceDbSchema.EmployeesTable.Cols.NOTE+
                ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public static SQLiteDatabase getDatabaseWritable(Context context)
    {
        if (sDatabaseWritable!=null)
            return sDatabaseWritable;
        else
        {
            AttendanceBaseHelper helper;
            helper=new AttendanceBaseHelper(context);
            sDatabaseWritable=helper.getWritableDatabase();
            sDatabaseReadable=helper.getReadableDatabase();
        }
        return sDatabaseWritable;
    }

    public static SQLiteDatabase getDatabaseReadable(Context context)
    {
        if (sDatabaseReadable!=null)
            return sDatabaseReadable;
        else
        {
            AttendanceBaseHelper helper;
            helper=new AttendanceBaseHelper(context);
            sDatabaseWritable=helper.getWritableDatabase();
            sDatabaseReadable=helper.getReadableDatabase();
        }
        return sDatabaseReadable;
    }

}
