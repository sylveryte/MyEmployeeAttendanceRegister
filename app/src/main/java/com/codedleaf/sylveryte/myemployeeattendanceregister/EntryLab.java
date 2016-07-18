package com.codedleaf.sylveryte.myemployeeattendanceregister;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by sylveryte on 27/6/16.
 */
public class EntryLab {

    private static EntryLab sEntryLab;

    private Context mContext;
    private SQLiteDatabase mDatabase;

    private EntryLab(Context context)
    {
        mContext=context.getApplicationContext();
        mDatabase=AttendanceBaseHelper.getDatabaseWritable(mContext);

    }

    public static EntryLab getInstanceOf(Context context)
    {
        if (sEntryLab==null)
        {
            sEntryLab=new EntryLab(context);
        }
        return sEntryLab;
    }

    public EntrySet getEntrySet(LocalDate date, UUID siteId)
    {
        EntrySet entrySetToReturn = new EntrySet(siteId,mContext,date);
        entrySetToReturn.startEntriesProcess();
        return entrySetToReturn;
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




    //databases code
    private EntryCursorWrapper queryEntries(String date,String siteId)
    {
        Cursor cursor=mDatabase.query(
                AttendanceDbSchema.EntriesTable.NAME,
                null, //columns null coz select all columns :)
                AttendanceDbSchema.EntriesTable.Cols.DATE+"=? AND "+ AttendanceDbSchema.EntriesTable.Cols.SITEID+"=?",
                new String[]{date,siteId},
                null, //group by
                null, //having
                null //orderby
        );

        return new EntryCursorWrapper(cursor);

    }

    public void updateEntrySet(EntrySet entrySet)
    {
        List<Entry> entries=entrySet.getEntries();
        for (Entry entry:entries)
        {
            updateEntry(entry);
        }
    }

    public void adddEntrySetToDatabase(EntrySet entrySet)
    {
        List<Entry> entries=entrySet.getEntries();
        for (Entry entry:entries)
        {
            addEntryToDatabase(entry);
        }
    }

    public void updateEntry(Entry entry)
    {
        String empIdString=entry.getEmployeeId().toString();
        String siteIdString=entry.getSiteId().toString();
        String dateString=CodedleafTools.getStringFromLocalDate(entry.getDate());

        ContentValues values= AttendanceDbToolsProvider.getContentValues(entry);
        mDatabase.update(AttendanceDbSchema.EntriesTable.NAME,
                values,
                AttendanceDbSchema.EntriesTable.Cols.EMPLOYEEID+"=? AND "+ AttendanceDbSchema.EntriesTable.Cols.SITEID+"=? AND "+ AttendanceDbSchema.EntriesTable.Cols.DATE+"=?"
                ,new String[]{empIdString,siteIdString,dateString});
    }

    public void addEntryToDatabase(Entry entry)
    {
        ContentValues values= AttendanceDbToolsProvider.getContentValues(entry);
        mDatabase.insert(AttendanceDbSchema.EntriesTable.NAME,null,values);
    }



    public List<Entry> getEntries(LocalDate date,UUID siteId)
    {
        List<Entry> entries=new ArrayList<>();
        int i=0;

        EntryCursorWrapper cursorWrapper=queryEntries(CodedleafTools.getStringFromLocalDate(date),siteId.toString());
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
            String date=getString(getColumnIndex(AttendanceDbSchema.EntriesTable.Cols.DATE));
            String note=getString(getColumnIndex(AttendanceDbSchema.EntriesTable.Cols.NOTE));

            Entry entry=new Entry(UUID.fromString(empIdString),UUID.fromString(siteIdString),CodedleafTools.getLocalDateFromString(date));
            entry.setRemark(Integer.parseInt(remark));
            entry.setNote(note);
            return entry;
        }
    }
}
