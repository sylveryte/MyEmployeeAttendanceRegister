package com.codedleaf.sylveryte.myemployeeattendanceregister;

import android.content.ContentValues;

import com.codedleaf.sylveryte.myemployeeattendanceregister.AttendanceDbSchema.DesignationsTable;
import com.codedleaf.sylveryte.myemployeeattendanceregister.AttendanceDbSchema.EmployeesTable;
import com.codedleaf.sylveryte.myemployeeattendanceregister.AttendanceDbSchema.EntriesTable;
import com.codedleaf.sylveryte.myemployeeattendanceregister.AttendanceDbSchema.SitesTable;

/**
 * Created by sylveryte on 6/7/16.
 */
public class AttendanceDbToolsProvider {

    public static ContentValues getContentValues(Designation designation)
    {
            ContentValues values=new ContentValues();
            values.put(DesignationsTable.Cols.UID,designation.getId().toString());
            values.put(DesignationsTable.Cols.TITLE,designation.getTitle());
            values.put(DesignationsTable.Cols.DESC,designation.getPureDescription());

        return values;
    }




    public static ContentValues getContentValues(Site site)
    {
        ContentValues values=new ContentValues();
        values.put(SitesTable.Cols.UID,site.getId().toString());
        values.put(SitesTable.Cols.TITLE,site.getTitle());
        values.put(SitesTable.Cols.DESC,site.getDescriptionPure());
        values.put(SitesTable.Cols.ACTIVE,CodedleafTools.getStringOfBoolean(site.isActive()));
        values.put(SitesTable.Cols.BEGINDATE,CodedleafTools.getStringFromLocalDate(site.getBeginDate()));
        values.put(SitesTable.Cols.ENDDATE,CodedleafTools.getStringFromLocalDate(site.getFinishedDate()));

        return values;
    }

    public static ContentValues getContentValues(Entry entry)
    {
        ContentValues values=new ContentValues();
        //// TODO: 6/7/16 doubts about putting int values of remark :/
        values.put(EntriesTable.Cols.REMARK,entry.getRemark());
        values.put(EntriesTable.Cols.EMPLOYEEID,entry.getEmployeeId().toString());
        values.put(EntriesTable.Cols.SITEID,entry.getSiteId().toString());
        values.put(EntriesTable.Cols.NOTE,entry.getNote());
        values.put(EntriesTable.Cols.DAY,entry.getDate().getDayOfMonth());
        values.put(EntriesTable.Cols.MONTH,entry.getDate().getMonthOfYear());
        values.put(EntriesTable.Cols.YEAR,entry.getDate().getYear());

        return values;
    }

    public static ContentValues getContentValues(Employee employee)
    {
        ContentValues values=new ContentValues();
        values.put(EmployeesTable.Cols.UID,employee.getId().toString());


        values.put(EmployeesTable.Cols.ACTIVE,CodedleafTools.getStringOfBoolean(employee.isActive()));
        values.put(EmployeesTable.Cols.GENDER,CodedleafTools.getStringOfBoolean(employee.isMale()));


        values.put(EmployeesTable.Cols.AGE,employee.getAge());
        values.put(EmployeesTable.Cols.ADDRESS,employee.getAddress());
        values.put(EmployeesTable.Cols.NAME,employee.getName());
        values.put(EmployeesTable.Cols.NOTE,employee.getNote());
        values.put(EmployeesTable.Cols.DESIGNATIONIDS,CodedleafTools.getUUIDStringFromList(employee.getDesignations()));
        values.put(EmployeesTable.Cols.SITEIDS,CodedleafTools.getUUIDStringFromList(employee.getSites()));

        return values;
    }
}
