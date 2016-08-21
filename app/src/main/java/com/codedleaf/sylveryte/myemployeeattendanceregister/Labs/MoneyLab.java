package com.codedleaf.sylveryte.myemployeeattendanceregister.Labs;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;

import com.codedleaf.sylveryte.myemployeeattendanceregister.Models.Money;
import com.codedleaf.sylveryte.myemployeeattendanceregister.SQLite.AttendanceBaseHelper;
import com.codedleaf.sylveryte.myemployeeattendanceregister.SQLite.AttendanceDbSchema;
import com.codedleaf.sylveryte.myemployeeattendanceregister.SQLite.AttendanceDbToolsProvider;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by sylveryte on 5/8/16.
 * <p>
 * Copyright (C) 2016 sylveryte@codedleaf <codedleaf@gmail.com>
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
        deleteMoneyLogByEmpId(empId);
    }

    public void addMoney(Money money)
    {
        ContentValues values= AttendanceDbToolsProvider.getContentValues(money);
        mDatabase.insert(AttendanceDbSchema.MoneyTable.NAME,null,values);
    }

    //think this should be not there and make sure if site not found you better watchout
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

    public List<Money> getMoneyLogs(UUID empId,int year)
    {
        List<Money> moneys=new ArrayList<>();
        int i=0;

        String emp;
        if (empId==null)
            emp = "";
        else emp=empId.toString();

        MoneyCursorWrapper cursorWrapper= queryMoneyOfYear(
                null
                ,null
                ,year
                ,emp);
        try
        {
            cursorWrapper.moveToFirst();
            while (!cursorWrapper.isAfterLast())
            {
                i++;
                moneys.add(cursorWrapper.getMoney());
                cursorWrapper.moveToNext();
            }
        }
        finally
        {
            cursorWrapper.close();
        }

        if (i<1)
            return new ArrayList<>();

        return moneys;

    }


    private MoneyCursorWrapper queryMoneyOfYear(@Nullable  Integer day,@Nullable Integer month,Integer year,String empId)
    {

        //// TODO: 21/8/16 someday you might wanna optimize this one

        String sqlStatementString="SELECT * FROM "+AttendanceDbSchema.MoneyTable.NAME+" WHERE ";

        ArrayList<String> args=new ArrayList<>();

        int conditionsCounter=0;

        boolean last=false;//to know if last one was true [in down ifs]

        if (day!=null)
        {
            sqlStatementString+=AttendanceDbSchema.MoneyTable.Cols.DAY+"=? ";
            args.add(String.valueOf(day));

            last=true;
            conditionsCounter++;
        }
        if (month!=null)
        {
            if (last)
                sqlStatementString+=" AND ";

            sqlStatementString+=AttendanceDbSchema.MoneyTable.Cols.MONTH+"=? ";
            args.add(String.valueOf(month));

            last=true;
            conditionsCounter++;
        }
        if (year!=null)
        {
            if (last)
                sqlStatementString+=" AND ";

            sqlStatementString+=AttendanceDbSchema.MoneyTable.Cols.YEAR+"=? ";
            args.add(String.valueOf(year));

            last=true;
            conditionsCounter++;
        }
        if (empId!=null)
        {
            if (last)
                sqlStatementString+=" AND ";

            sqlStatementString+=AttendanceDbSchema.MoneyTable.Cols.EMPLOYEEID+"=? ";
            args.add(empId);

            last=true;
            conditionsCounter++;
        }

        if (!last)
            sqlStatementString="SELECT * FROM "+AttendanceDbSchema.MoneyTable.NAME;


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

        return new MoneyCursorWrapper(cursor);
    }

    private  class MoneyCursorWrapper extends CursorWrapper
    {
        public MoneyCursorWrapper(Cursor cursor)
        {
            super(cursor);
        }

        public Money getMoney()
        {
            String empIdString=getString(getColumnIndex(AttendanceDbSchema.MoneyTable.Cols.EMPLOYEEID));
            String siteIdString=getString(getColumnIndex(AttendanceDbSchema.MoneyTable.Cols.SITEID));


            int amount=getInt(getColumnIndex(AttendanceDbSchema.MoneyTable.Cols.AMOUNT));

            int minute=getInt(getColumnIndex(AttendanceDbSchema.MoneyTable.Cols.MINUTE));
            int hout=getInt(getColumnIndex(AttendanceDbSchema.MoneyTable.Cols.HOUR));
            int day=getInt(getColumnIndex(AttendanceDbSchema.MoneyTable.Cols.DAY));
            int month=getInt(getColumnIndex(AttendanceDbSchema.MoneyTable.Cols.MONTH));
            int year=getInt(getColumnIndex(AttendanceDbSchema.MoneyTable.Cols.YEAR));

            String note=getString(getColumnIndex(AttendanceDbSchema.MoneyTable.Cols.NOTE));

            DateTime dateTime=new DateTime(year,month,day,hout,minute);

            Money money=new Money(UUID.fromString(empIdString),siteIdString.trim().isEmpty()?null:UUID.fromString(siteIdString),dateTime);
            money.setAmount(amount);
            money.setNote(note);

            return money;
        }
    }

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
    public void deleteMoney(Money money)
    {

        String empIdString=money.getEmployeeId().toString();
        String siteIdString=money.getSiteId()==null?" ":money.getSiteId().toString();
        DateTime date=money.getDate();




        mDatabase.delete(AttendanceDbSchema.MoneyTable.NAME,
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
