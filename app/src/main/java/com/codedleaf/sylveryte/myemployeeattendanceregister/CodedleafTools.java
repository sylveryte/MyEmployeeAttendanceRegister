package com.codedleaf.sylveryte.myemployeeattendanceregister;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.PointF;
import android.net.Uri;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeParser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Currency;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;

/**
 * Created by sylveryte on 6/7/16.
 *
 * Copyright (C) 2016 sylveryte@codedleaf <codedleaf@gmail.com>
 *
 * This file is part of My Employee Attendance Register.
 *
 */
public class CodedleafTools {


    public static final String localDateFormatString="yyyy-MM-dd";
    public static final String prettyLocalDateFormatString="E-dd-M-yyy";
    public static final String prettyDateTimeFormatString="h:m a E-dd-M-yyy";

    public static final String monthFormat="MMMM-yyy";

    public static String getStringOfBoolean(boolean b)
    {
        return b?"1":"0";
    }

    public static boolean getBooleanFromString(String s)
    {
        if (s.compareTo("1")==0)
            return true;
        else return false;
    }

    public static String getUUIDStringFromList(List<UUID> list)
    {
        String s="";
        if (list==null)
            return "";
        for (UUID uuid:list)
        {
            s+=uuid.toString()+":";
        }
        return s;
    }

    public static List<UUID> getUUIDListFromString(String string)
    {
        String[] strings=string.split(":");

        List<UUID> uuidList=new ArrayList<>();

        for (int i=0;i<strings.length;i++)
        {
            if (strings[i].isEmpty())
                continue;
            uuidList.add(UUID.fromString(strings[i]));
        }

        return uuidList;
    }

    public static String getDateString(LocalDate date)
    {
        if (date==null)
            return "";

        return date.toString(localDateFormatString);

    }

    public static String getMonthYearString(LocalDate date)
    {
        if (date==null)
            return "";

        return date.toString(monthFormat);

    }


    public static String getPrettyDateString(LocalDate date)
    {
        if (date==null)
            return "";

        DateTimeFormatter format=DateTimeFormat.forPattern(prettyLocalDateFormatString);

        return format.print(date);

    }

    public static String getPrettyDateString(DateTime date)
    {
        if (date==null)
            return "";

        DateTimeFormatter format=DateTimeFormat.forPattern(prettyDateTimeFormatString);
        return format.print(date);
    }


    public static LocalDate getLocalDateFromString(String dateString)
    {
        if (dateString==null||dateString.trim().isEmpty())
            return null;

        return LocalDate.parse(dateString);
    }

    public static Bitmap getScaledBitmap(String path,int destWidth,int destHeight)
    {
        BitmapFactory.Options options=new BitmapFactory.Options();
        options.inJustDecodeBounds=true;
        BitmapFactory.decodeFile(path,options);

        float srcWidth=options.outWidth;
        float srcHeight=options.outHeight;

        int inSampleSize=1;
        if (srcHeight>destHeight||srcHeight>destWidth) {
            if (srcWidth > srcHeight) {
                inSampleSize = Math.round(srcHeight / destHeight);
            } else {
                inSampleSize = Math.round(srcWidth / destWidth);
            }
        }

        options=new BitmapFactory.Options();
        options.inSampleSize=inSampleSize;

        return BitmapFactory.decodeFile(path,options);
    }

    public static Bitmap getScaledBitmap(String path, Activity activity)
    {
        Point size=new Point();
        activity.getWindowManager().getDefaultDisplay().getSize(size);

        return getScaledBitmap(path,size.x,size.y);
    }

    public static void saveFile(String filePath, File destination)
    {

        try {

            if (destination.exists())
            {
                if (!destination.delete())
                    return;
            }
            if(!destination.createNewFile())
                return;
            File source=new File(filePath);
            FileChannel src=new FileInputStream(source).getChannel();
            FileChannel dst=new FileOutputStream(destination).getChannel();
            dst.transferFrom(src,0,src.size());
            src.close();
            dst.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static String getCurrencySymbol(String currencyCode)
    {
        return "$";
//        final Map<String,String> MYCURRENCIES= new HashMap<String, String>(){
//            {
//                for (Locale ll:Locale.getAvailableLocales())
//                {
//                    try {
//                        Currency a=Currency.getInstance(ll);
//                        put(a.getCurrencyCode(),a.getSymbol());
//                    }
//                    catch (Exception e)
//                    {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        };
//        return MYCURRENCIES.get(currencyCode);
    }
}
