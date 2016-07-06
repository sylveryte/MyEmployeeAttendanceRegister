package com.codedleaf.sylveryte.myemployeeattendanceregister;

import org.joda.time.LocalDate;

import java.util.List;
import java.util.UUID;

/**
 * Created by sylveryte on 6/7/16.
 */
public class CodedleafTools {


    public static final String localDateFormatString="dd/mm/yyyy";

    public static String getUUIDStringFromList(List<UUID> list)
    {
        String s="";
        for (UUID uuid:list)
        {
            s+=uuid.toString();
        }
        return s;
    }

    public static String getLocalDateInStringForm(LocalDate date)
    {
        return date.toString(localDateFormatString);
    }
}
