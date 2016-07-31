package com.codedleaf.sylveryte.myemployeeattendanceregister.SiteAttendance;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;

import org.joda.time.LocalDate;

import java.util.UUID;

/**
 * Created by sylveryte on 31/7/16.
 * <p/>
 * Copyright (C) 2016 sylveryte@codedleaf <codedlaf@gmail.com>
 * <p/>
 * This file is part of My Employee Attendance Register.
 */
public class MonthViewDialogFragment extends DialogFragment {

    private static final String EMP_ARG="empargmvd";
    private static final String SITE_ARG="siteargvmd";

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Bundle arg=getArguments();

        MonthView monthView=MonthView.getInstance(LayoutInflater.from(getActivity()),
                getActivity(),
                new LocalDate(),
                (UUID) arg.getSerializable(EMP_ARG),
                (UUID)arg.getSerializable(SITE_ARG));


        return new AlertDialog.Builder(getActivity())
                .setView(monthView.getView())
                .create();

//        Dialog dialog=new AlertDialog.Builder(getActivity())
//                .setView(monthView.getView())
//                .create();
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//        return dialog;
    }

    public static MonthViewDialogFragment getInstance(UUID empid, UUID siteId)
    {
        MonthViewDialogFragment fragment=new MonthViewDialogFragment();

        Bundle args=new Bundle(2);
        args.putSerializable(EMP_ARG,empid);
        args.putSerializable(SITE_ARG,siteId);

        fragment.setArguments(args);

        return fragment;
    }
}
