package com.codedleaf.sylveryte.myemployeeattendanceregister.Labs;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.codedleaf.sylveryte.myemployeeattendanceregister.SQLite.AttendanceBaseHelper;
import com.codedleaf.sylveryte.myemployeeattendanceregister.SQLite.AttendanceDbSchema;
import com.codedleaf.sylveryte.myemployeeattendanceregister.SQLite.AttendanceDbToolsProvider;
import com.codedleaf.sylveryte.myemployeeattendanceregister.Models.Entry;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by sylveryte on 27/6/16.
 *
 * Copyright (C) 2016 sylveryte@codedleaf <codedleaf@gmail.com>
 *
 * This file is part of My Employee Attendance Register.
 *
 */
public class EntryLab {

    private static EntryLab sEntryLab;

    private SQLiteDatabase mDatabase;

    private EntryLab(Context context)
    {
        mDatabase= AttendanceBaseHelper.getDatabaseWritable(context);

    }

    public static EntryLab getInstanceOf(Context context)
    {
        if (sEntryLab==null)
        {
            sEntryLab=new EntryLab(context);
        }
        return sEntryLab;
    }


    private void deleteEntriesByEmpId(UUID empId)
    {

        String empIdString=empId.toString();
        mDatabase.delete(AttendanceDbSchema.EntriesTable.NAME, AttendanceDbSchema.EntriesTable.Cols.EMPLOYEEID+"=?",new String[]{empIdString});
    }

    private void deleteEntriesBySite(UUID siteId)
    {

        String siteIdString=siteId.toString();
        mDatabase.delete(AttendanceDbSchema.EntriesTable.NAME, AttendanceDbSchema.EntriesTable.Cols.SITEID+"=?",new String[]{siteIdString});
    }

    public void cleanseEntriesOfEmployeeId(UUID empId)
    {
        ///delete from db
        deleteEntriesByEmpId(empId);
    }

    public void cleanseEntriesOfSiteId(UUID siteId)
    {

        //delete dfrom database
        deleteEntriesBySite(siteId);
    }


    //databases code queries
    private EntryCursorWrapper queryEntriesOfMonth(@Nullable  Integer day,@Nullable Integer month,@Nullable Integer year,@Nullable Integer remark,@Nullable  String siteId,@Nullable String empId)
    {

        String sqlStatementString="SELECT * FROM "+AttendanceDbSchema.EntriesTable.NAME+" WHERE ";

        ArrayList<String> args=new ArrayList<>();

        int conditionsCounter=0;

        boolean last=false;//to know if last one was true [in down ifs]

        if (day!=null)
        {
            sqlStatementString+=AttendanceDbSchema.EntriesTable.Cols.DAY+"=? ";
            args.add(String.valueOf(day));

            last=true;
            conditionsCounter++;
        }
        if (month!=null)
        {
            if (last)
                sqlStatementString+=" AND ";

            sqlStatementString+=AttendanceDbSchema.EntriesTable.Cols.MONTH+"=? ";
            args.add(String.valueOf(month));

            last=true;
            conditionsCounter++;
        }
        if (year!=null)
        {
            if (last)
                sqlStatementString+=" AND ";

            sqlStatementString+=AttendanceDbSchema.EntriesTable.Cols.YEAR+"=? ";
            args.add(String.valueOf(year));

            last=true;
            conditionsCounter++;
        }
        if (remark!=null)
        {
            if (last)
                sqlStatementString+=" AND ";

            sqlStatementString+=AttendanceDbSchema.EntriesTable.Cols.REMARK+"=? ";
            args.add(String.valueOf(remark));

            last=true;
            conditionsCounter++;
        }
        if (siteId!=null)
        {
            if (last)
                sqlStatementString+=" AND ";

            sqlStatementString+=AttendanceDbSchema.EntriesTable.Cols.SITEID+"=? ";
            args.add(siteId);

            last=true;
            conditionsCounter++;
        }
        if (empId!=null)
        {
            if (last)
                sqlStatementString+=" AND ";

            sqlStatementString+=AttendanceDbSchema.EntriesTable.Cols.EMPLOYEEID+"=? ";
            args.add(empId);

            last=true;
            conditionsCounter++;
        }

        if (!last)
            sqlStatementString="SELECT * FROM "+AttendanceDbSchema.EntriesTable.NAME;


        String[] argsArray=new String[conditionsCounter];


        if (!last)
            argsArray=null;
        else
        {
            for (int i=0;i<args.size();i++)
            {
                argsArray[i]=args.get(i);

            }
        }


        Cursor cursor=mDatabase.rawQuery(sqlStatementString,argsArray);

        return new EntryCursorWrapper(cursor);

    }
    //end of db queries




    public void updateEntry(Entry entry)
    {
        String empIdString=entry.getEmployeeId().toString();
        String siteIdString=entry.getSiteId().toString();
        LocalDate date=entry.getDate();

        ContentValues values= AttendanceDbToolsProvider.getContentValues(entry);



        mDatabase.update(AttendanceDbSchema.EntriesTable.NAME,
                values,
                AttendanceDbSchema.EntriesTable.Cols.EMPLOYEEID+"=? AND "+
                        AttendanceDbSchema.EntriesTable.Cols.DAY+"=? AND "+
                        AttendanceDbSchema.EntriesTable.Cols.MONTH+"=? AND "+
                        AttendanceDbSchema.EntriesTable.Cols.YEAR+"=? AND "+
                        AttendanceDbSchema.EntriesTable.Cols.SITEID+"=? "
                ,new String[]{empIdString,
                        String.valueOf(date.getDayOfMonth()),
                        String.valueOf(date.getMonthOfYear()),
                        String.valueOf(date.getYear()),
                        siteIdString});
    }

    public void addEntryToDatabase(Entry entry)
    {
        ContentValues values= AttendanceDbToolsProvider.getContentValues(entry);
        mDatabase.insert(AttendanceDbSchema.EntriesTable.NAME,null,values);
    }

//entries
    public List<Entry> getEntries(@Nullable Integer day, @Nullable Integer month, @Nullable Integer year,@Nullable Integer remark, @Nullable UUID siteId,@Nullable UUID empId)
    {
        List<Entry> entries=new ArrayList<>();
        int i=0;

        String site;
        if (siteId==null)
            site = null;
        else site=siteId.toString();
        String emp;
        if (empId==null)
            emp = null;
        else emp=empId.toString();

        EntryCursorWrapper cursorWrapper= queryEntriesOfMonth(
                day
                ,month
                ,year
                ,remark
                ,site
                ,emp);
        try
        {
            cursorWrapper.moveToFirst();
            while (!cursorWrapper.isAfterLast())
            {
                i++;
                entries.add(cursorWrapper.getEntry());
                cursorWrapper.moveToNext();
            }
        }
        finally
        {
            cursorWrapper.close();
        }

        if (i<1)
            return new ArrayList<>();

        return entries;
    }

    public List<Entry> getEntries(LocalDate date,UUID siteId,UUID empid)
    {
       return getEntries(date.getDayOfMonth(),date.getMonthOfYear(),date.getYear(),null,siteId,empid);
    }

    public Entry getEntry(@NonNull LocalDate date,@NonNull UUID siteId,@NonNull UUID empId)
    {

        Entry entry=null;

        EntryCursorWrapper cursorWrapper= queryEntriesOfMonth(
                date.getDayOfMonth()
                ,date.getMonthOfYear()
                ,date.getYear()
                ,null
                ,siteId.toString()
                ,empId.toString());
        try
        {
            cursorWrapper.moveToFirst();
            if (!cursorWrapper.isAfterLast())
                entry=cursorWrapper.getEntry();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        if (entry==null)
        {
            entry=new Entry(empId,siteId,date);
            entry.setNew(true);
        }
        return entry;
    }

    private  class EntryCursorWrapper extends CursorWrapper
    {
        public EntryCursorWrapper(Cursor cursor)
        {
            super(cursor);
        }

        public Entry getEntry()
        {
            String empIdString=getString(getColumnIndex(AttendanceDbSchema.EntriesTable.Cols.EMPLOYEEID));
            String remark=getString(getColumnIndex(AttendanceDbSchema.EntriesTable.Cols.REMARK));
            String siteIdString=getString(getColumnIndex(AttendanceDbSchema.EntriesTable.Cols.SITEID));


            int day=getInt(getColumnIndex(AttendanceDbSchema.EntriesTable.Cols.DAY));
            int month=getInt(getColumnIndex(AttendanceDbSchema.EntriesTable.Cols.MONTH));
            int year=getInt(getColumnIndex(AttendanceDbSchema.EntriesTable.Cols.YEAR));

            String note=getString(getColumnIndex(AttendanceDbSchema.EntriesTable.Cols.NOTE));

            Entry entry=new Entry(UUID.fromString(empIdString),UUID.fromString(siteIdString),new LocalDate(year,month,day));
            entry.setRemark(Integer.parseInt(remark));
            entry.setNote(note);
            return entry;
        }
    }
}
