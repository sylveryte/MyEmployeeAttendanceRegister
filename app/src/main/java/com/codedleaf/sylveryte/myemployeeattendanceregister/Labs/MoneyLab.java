package com.codedleaf.sylveryte.myemployeeattendanceregister.Labs;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.codedleaf.sylveryte.myemployeeattendanceregister.Models.Money;
import com.codedleaf.sylveryte.myemployeeattendanceregister.SQLite.AttendanceBaseHelper;

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

    public void updateMoney(Money money)
    {
        //// TODO: 5/8/16 code here
    }

}
