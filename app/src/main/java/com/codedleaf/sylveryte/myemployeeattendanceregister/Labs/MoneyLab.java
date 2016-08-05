package com.codedleaf.sylveryte.myemployeeattendanceregister.Labs;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.codedleaf.sylveryte.myemployeeattendanceregister.Models.Money;
import com.codedleaf.sylveryte.myemployeeattendanceregister.SQLite.AttendanceBaseHelper;
import com.codedleaf.sylveryte.myemployeeattendanceregister.SQLite.AttendanceDbSchema;
import com.codedleaf.sylveryte.myemployeeattendanceregister.SQLite.AttendanceDbToolsProvider;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import java.util.UUID;

/**
 * Created by sylveryte on 5/8/16.
 * <p>
 * Copyright (C) 2016 sylveryte@codedleaf <codedlaf@gmail.com>
 * <p>
 * This file is part of My Employee Attendance Register.
 */
public class MoneyLab {

    private static MoneyLab sMoneyLab;

    private SQLiteDatabase mDatabase;

    private MoneyLab(Context context)
    {
        mDatabase= AttendanceBaseHelper.getDatabaseWritable(context);
    }

    public static MoneyLab getInstanceOf(Context context)
    {
        if (sMoneyLab==null)
        {
            sMoneyLab=new MoneyLab(context);
        }
        return sMoneyLab;
    }

    private void deleteMoneyLogByEmpId(UUID empId)
    {

        String empIdString=empId.toString();
        mDatabase.delete(AttendanceDbSchema.MoneyTable.NAME, AttendanceDbSchema.MoneyTable.Cols.EMPLOYEEID+"=?",new String[]{empIdString});
    }

    public void cleanseMoneyLogOfEmployeeId(UUID empId)
    {
        ///delete from db
        // TODO: 5/8/16 include this while deleting employeeeeee
        deleteMoneyLogByEmpId(empId);
    }

    //think this should be not there and make sure if site not found you better watchout for that

/*    private void delteMoneyLogBySiteId(UUID siteId)
    {

        String siteIdString=siteId.toString();
        mDatabase.delete(AttendanceDbSchema.MoneyTable.NAME, AttendanceDbSchema.MoneyTable.Cols.SITEID+"=?",new String[]{siteIdString});
    }

    public void cleanseMoneyLogOfSiteId(UUID siteId)
    {
        //delete dfrom database
        delteMoneyLogBySiteId(siteId);
    }
*/

    public void updateMoney(Money money)
    {

        String empIdString=money.getEmployeeId().toString();
        String siteIdString=money.getSiteId().toString();
        DateTime date=money.getDate();

        ContentValues values= AttendanceDbToolsProvider.getContentValues(money);



        mDatabase.update(AttendanceDbSchema.MoneyTable.NAME,
                values,
                AttendanceDbSchema.MoneyTable.Cols.EMPLOYEEID+"=? AND "+
                        AttendanceDbSchema.MoneyTable.Cols.MINUTE+"=? AND "+
                        AttendanceDbSchema.MoneyTable.Cols.HOUR+"=? AND "+
                        AttendanceDbSchema.MoneyTable.Cols.DAY+"=? AND "+
                        AttendanceDbSchema.MoneyTable.Cols.MONTH+"=? AND "+
                        AttendanceDbSchema.MoneyTable.Cols.YEAR+"=? AND "+
                        AttendanceDbSchema.MoneyTable.Cols.SITEID+"=? "
                ,new String[]{empIdString,
                        String.valueOf(date.getMinuteOfHour()),
                        String.valueOf(date.getHourOfDay()),
                        String.valueOf(date.getDayOfMonth()),
                        String.valueOf(date.getMonthOfYear()),
                        String.valueOf(date.getYear()),
                        siteIdString});

    }

}
