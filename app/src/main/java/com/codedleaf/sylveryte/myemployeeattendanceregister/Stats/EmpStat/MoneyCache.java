package com.codedleaf.sylveryte.myemployeeattendanceregister.Stats.EmpStat;

import com.codedleaf.sylveryte.myemployeeattendanceregister.Models.Money;

/**
 * Created by sylveryte on 21/8/16.
 * <p/>
 * Copyright (C) 2016 sylveryte@codedleaf <codedleaf@gmail.com>
 * <p/>
 * This file is part of MyEmployeeAttendanceRegister.
 */

public class MoneyCache {

    private static MoneyCache sMoneyCache;

    private Money mMoney;

    public static MoneyCache getMoneyCache()
    {
        if (sMoneyCache==null)
        {
            sMoneyCache=new MoneyCache();
        }
        return sMoneyCache;
    }

    public Money getMoney() {
        return mMoney;
    }

    public void setMoney(Money money) {
        mMoney = money;
    }
}
