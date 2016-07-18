package com.codedleaf.sylveryte.myemployeeattendanceregister;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeParser;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by sylveryte on 6/7/16.
 */
public class CodedleafTools {


    public static final String localDateFormatString="yyyy-MM-dd";
    public static final String prettyLocalDateFormatString="E-dd-M-yyy";

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

    public static String getStringFromLocalDate(LocalDate date)
    {
        if (date==null)
            return null;

        return date.toString(localDateFormatString);

    }

    public static String getPrettyStringFromLocalDate(LocalDate date)
    {
        if (date==null)
            return "";

        DateTimeFormatter format=DateTimeFormat.forPattern(prettyLocalDateFormatString);

        return format.print(date);

    }

    public static LocalDate getLocalDateFromString(String dateString)
    {
        if (dateString==null)
            return null;

        return LocalDate.parse(dateString);
    }
}
