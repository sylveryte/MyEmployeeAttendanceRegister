package com.codedleaf.sylveryte.myemployeeattendanceregister;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.codedleaf.sylveryte.myemployeeattendanceregister.AttendanceDbSchema.DesignationsTable;
import com.codedleaf.sylveryte.myemployeeattendanceregister.AttendanceDbSchema.EmployeesTable;
import com.codedleaf.sylveryte.myemployeeattendanceregister.AttendanceDbSchema.EntriesTable;
import com.codedleaf.sylveryte.myemployeeattendanceregister.AttendanceDbSchema.SitesTable;

/**
 * Created by sylveryte on 6/7/16.
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

        db.execSQL("create table " + EntriesTable.NAME + "("+
        " _id integer primary key autoincrement, " +
                EntriesTable.Cols.EMPLOYEEID+","+
                EntriesTable.Cols.DATE+","+
                EntriesTable.Cols.NOTE+","+
                EntriesTable.Cols.REMARK+","+
                EntriesTable.Cols.SITEID+
                ")"

        );

        db.execSQL("create table " + DesignationsTable.NAME + "("+
                " _id integer primary key autoincrement, " +
                DesignationsTable.Cols.DESC+","+
                DesignationsTable.Cols.TITLE+","+
                DesignationsTable.Cols.UID+
                ")"
        );

        db.execSQL("create table " + SitesTable.NAME + "("+
                " _id integer primary key autoincrement, " +
                SitesTable.Cols.UID+","+
                SitesTable.Cols.TITLE+","+
                SitesTable.Cols.DESC+","+
                SitesTable.Cols.ACTIVE+","+
                SitesTable.Cols.BEGINDATE+","+
                SitesTable.Cols.ENDDATE+
                ")"
        );

        db.execSQL("create table " + EmployeesTable.NAME + "("+
                " _id integer primary key autoincrement, " +
                EmployeesTable.Cols.UID+","+
                EmployeesTable.Cols.NAME+","+
                EmployeesTable.Cols.ADDRESS+","+
                EmployeesTable.Cols.ACTIVE+","+
                EmployeesTable.Cols.AGE+","+
                EmployeesTable.Cols.SITEIDS+","+
                EmployeesTable.Cols.GENDER+","+
                EmployeesTable.Cols.DESIGNATIONIDS+","+
                EmployeesTable.Cols.NOTE+
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
