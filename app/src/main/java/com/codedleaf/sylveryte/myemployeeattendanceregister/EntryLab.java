package com.codedleaf.sylveryte.myemployeeattendanceregister;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by sylveryte on 27/6/16.
 */
public class EntryLab {

    private static EntryLab sEntryLab;

    private SQLiteDatabase mDatabase;

    private EntryLab(Context context)
    {
        mDatabase=AttendanceBaseHelper.getDatabaseWritable(context);

    }

    public static EntryLab getInstanceOf(Context context)
    {
        if (sEntryLab==null)
        {
            sEntryLab=new EntryLab(context);
        }
        return sEntryLab;
    }

    public EntrySetOfDay getEntrySetOfDay(LocalDate date, UUID siteId, Context context)
    {
        EntrySetOfDay entrySetOfDayToReturn = new EntrySetOfDay(siteId,date);
        entrySetOfDayToReturn.startEntriesProcess(context);
        return entrySetOfDayToReturn;
    }

    public EntrySetOfDay getEntrySetOfMonth(LocalDate date, UUID siteId, Context context)
    {
        // TODO: 22/7/16    could there be possibility of pickable's id

        return null;
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
    private EntryCursorWrapper queryEntriesOfMonth(String day,String month,String year,String remark, String siteId)
    {
        if (day==null)
            day="*";
        if (month==null)
            month="*";
        if (year==null)
            year="*";
        if (remark==null)
            remark="*";
        if (siteId==null)
            siteId="*";

        Cursor cursor=mDatabase.query(
                AttendanceDbSchema.EntriesTable.NAME,
                null, //columns null coz select all columns :)
                        AttendanceDbSchema.EntriesTable.Cols.DAY+"=? AND "+
                                AttendanceDbSchema.EntriesTable.Cols.MONTH+"=? AND "+
                                AttendanceDbSchema.EntriesTable.Cols.YEAR+"=? AND "+
                                AttendanceDbSchema.EntriesTable.Cols.REMARK+"=? AND "+
                                AttendanceDbSchema.EntriesTable.Cols.SITEID+"=?",
        new String[]{day
                ,month
                ,year
                ,remark
                ,siteId},
                null, //group by
                null, //having
                null //orderby
        );
        return new EntryCursorWrapper(cursor);

    }
    //end of db queries


    public void updateEntrySet(EntrySetOfDay entrySetOfDay)
    {
        List<Entry> entries= entrySetOfDay.getEntries();
        for (Entry entry:entries)
        {
            updateEntry(entry);
        }
    }

    public void adddEntrySetToDatabase(EntrySetOfDay entrySetOfDay)
    {
        List<Entry> entries= entrySetOfDay.getEntries();
        for (Entry entry:entries)
        {
            addEntryToDatabase(entry);
        }
    }

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
                        AttendanceDbSchema.EntriesTable.Cols.SITEID+"=?"
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
    public List<Entry> getEntries(@Nullable Integer day, @Nullable Integer month, @Nullable Integer year,@Nullable Integer remark, @Nullable UUID siteId)
    {
        List<Entry> entries=new ArrayList<>();
        int i=0;

        String site;
        if (siteId==null)
            site = null;
        else site=siteId.toString();

        EntryCursorWrapper cursorWrapper= queryEntriesOfMonth(String.valueOf(day)
                ,String.valueOf(month)
                ,String.valueOf(year)
                ,String.valueOf(remark)
                ,site);
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
            return null;

        return entries;
    }

    public List<Entry> getEntries(LocalDate dayDate,@Nullable Integer remark,@Nullable UUID siteId)
    {
        return getEntries(dayDate.getDayOfMonth(),dayDate.getMonthOfYear(),dayDate.getYear(),remark,siteId);
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
            int month=getInt(getColumnIndex(AttendanceDbSchema.EntriesTable.Cols.DAY));
            int year=getInt(getColumnIndex(AttendanceDbSchema.EntriesTable.Cols.DAY));

            String note=getString(getColumnIndex(AttendanceDbSchema.EntriesTable.Cols.NOTE));

            Entry entry=new Entry(UUID.fromString(empIdString),UUID.fromString(siteIdString),new LocalDate(year,month,day));
            entry.setRemark(Integer.parseInt(remark));
            entry.setNote(note);
            return entry;
        }
    }
}
