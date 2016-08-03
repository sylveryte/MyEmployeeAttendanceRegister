package com.codedleaf.sylveryte.myemployeeattendanceregister.Editing;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.codedleaf.sylveryte.myemployeeattendanceregister.Labs.DesignationLab;
import com.codedleaf.sylveryte.myemployeeattendanceregister.Models.Designation;
import com.codedleaf.sylveryte.myemployeeattendanceregister.R;

import java.util.UUID;

/**
 * Created by sylveryte on 20/7/16.
 *
 * Copyright (C) 2016 sylveryte@codedleaf <codedlaf@gmail.com>
 *
 * This file is part of My Employee Attendance Register.
 *
 */
public class DesignationAdditionDialogFragment extends DialogFragment {


    private static String ARGS_CODE="desargcode";

    private EditText mEditText_designationName;
    private EditText mEditText_description;
    private Button mAddButton;

    private Designation mDesignation;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View v= LayoutInflater.from(getActivity()).inflate(R.layout.designation_additioin_fragment,null,false);

        mEditText_designationName =(EditText)v.findViewById(R.id.editText_designation_name);
        mEditText_description=(EditText)v.findViewById(R.id.editText_designation_description);
        mAddButton = (Button)v.findViewById(R.id.button_add_designation);
        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mEditText_designationName.getText().toString().trim().isEmpty())
                {
                    mEditText_designationName.setError("Please enter designation");
                    return;
                }

                saveDesignation(mDesignation);
                getDialog().dismiss();
            }
        });


        Bundle args=getArguments();
        //differentiate between edit and add calls
        if (args!=null)
        {
            UUID uuid=(UUID)args.getSerializable(ARGS_CODE);

            mDesignation= DesignationLab.getInstanceOf(getActivity()).getDesigantionById(uuid);
            updateData();
        }
        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .create();
    }

    private void updateData()
    {
        mEditText_designationName.setText(mDesignation.getTitle());
        mEditText_description.setText(mDesignation.getPureDescription());
    }

    private void  saveDesignation(Designation designation)
    {
        if (designation==null)
        {
            mDesignation=new Designation();
            DesignationLab.getInstanceOf(getActivity()).addDesignation(mDesignation);
        }

        mDesignation.setTitle(mEditText_designationName.getText().toString().trim());
        mDesignation.setDescription(mEditText_description.getText().toString().trim());

        mDesignation.updateMyDB(getActivity());
    }

    public static DesignationAdditionDialogFragment getDialogFrag(@Nullable UUID uuid)
    {
        if (uuid==null)
            return new DesignationAdditionDialogFragment();
        else
        {
            DesignationAdditionDialogFragment fragment=new DesignationAdditionDialogFragment();
            Bundle args=new Bundle();
            args.putSerializable(ARGS_CODE,uuid);
            fragment.setArguments(args);
            return fragment;
        }
    }
}
