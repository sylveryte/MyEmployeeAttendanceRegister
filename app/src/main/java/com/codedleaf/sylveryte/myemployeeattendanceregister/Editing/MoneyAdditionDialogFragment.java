package com.codedleaf.sylveryte.myemployeeattendanceregister.Editing;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.codedleaf.sylveryte.myemployeeattendanceregister.CodedleafTools;
import com.codedleaf.sylveryte.myemployeeattendanceregister.Labs.MoneyLab;
import com.codedleaf.sylveryte.myemployeeattendanceregister.Labs.SitesLab;
import com.codedleaf.sylveryte.myemployeeattendanceregister.Models.Money;
import com.codedleaf.sylveryte.myemployeeattendanceregister.Picknation.PickCache;
import com.codedleaf.sylveryte.myemployeeattendanceregister.GeneralObserver;
import com.codedleaf.sylveryte.myemployeeattendanceregister.Picknation.Pickable;
import com.codedleaf.sylveryte.myemployeeattendanceregister.Picknation.SimpleListDialogFragment;
import com.codedleaf.sylveryte.myemployeeattendanceregister.R;
import com.codedleaf.sylveryte.myemployeeattendanceregister.Stats.EmpStat.MoneyCache;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by sylveryte on 21/8/16.
 * <p/>
 * Copyright (C) 2016 sylveryte@codedleaf <codedleaf@gmail.com>
 * <p/>
 * This file is part of MyEmployeeAttendanceRegister.
 */

public class MoneyAdditionDialogFragment extends DialogFragment implements GeneralObserver {

    private static String EMP_CODE="empcode";
    private static String CALLER_CODE="MoneyAdditionDialogfragment";

    private EditText mEditText_amount;
    private TextView mText_date;
    private TextView mText_site;
    private EditText mEditText_note;

    private Money mMoney;

    private DateTime mDateTime;

    private UUID siteID;

    private GeneralObserver thisGeneral;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View view= LayoutInflater.from(getActivity())
                .inflate(R.layout.money_addition_fragment,null,false);

        thisGeneral=this;

        mEditText_amount=(EditText)view.findViewById(R.id.amount);
        mText_date =(TextView) view.findViewById(R.id.date);
        mText_site =(TextView) view.findViewById(R.id.site);
        mText_site.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PickCache.getInstance().addObserver(CALLER_CODE,thisGeneral);
                SimpleListDialogFragment.getInstance(CALLER_CODE, new ArrayList<Pickable>(SitesLab.getInstanceOf(getContext()).getSites()),SimpleListDialogFragment.PICK_MODE)
                        .show(getFragmentManager(),CALLER_CODE);
            }
        });
        mEditText_note=(EditText)view.findViewById(R.id.note);
        Button addButton = (Button) view.findViewById(R.id.done);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mEditText_amount.getText().toString().trim().isEmpty())
                {
                    mEditText_amount.setError("Please enter amount");
                    return;
                }
                saveMoney();
                MoneyCache.getMoneyCache().setMoney(mMoney);
                PickCache.getInstance().getObserver(getArguments().getString(CALLER_CODE)).doSomeUpdate(getActivity());
                getDialog().dismiss();
            }
        });


        //idoubt this one though
        mDateTime=new DateTime();


        mText_date.setText(CodedleafTools.getPrettyDateString(mDateTime));

        mMoney=new Money((UUID) getArguments().getSerializable(EMP_CODE),siteID,mDateTime);

        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .create();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    public void saveMoney()
    {
        mMoney.setAmount(Integer.parseInt(mEditText_amount.getText().toString()));
        mMoney.setNote(mEditText_note.getText().toString().trim());
        mMoney.setSiteId(siteID);
        mMoney.setDate(mDateTime);

        MoneyLab.getInstanceOf(getContext()).addMoney(mMoney);
    }

    @Override
    public void doSomeUpdate(Context context) {
        siteID=PickCache.getInstance().getPickable().getId();
        mText_site.setText(PickCache.getInstance().getPickable().getTitle());
    }

    public static MoneyAdditionDialogFragment getMoneyFrag(@NonNull UUID empId,@NonNull String caller,GeneralObserver observer)
    {
        MoneyAdditionDialogFragment fragment=new MoneyAdditionDialogFragment();
            Bundle args=new Bundle();
            args.putSerializable(EMP_CODE,empId);
            args.putSerializable(CALLER_CODE,caller);
            fragment.setArguments(args);
            PickCache.getInstance().addObserver(caller,observer);
            return fragment;
        }
}
