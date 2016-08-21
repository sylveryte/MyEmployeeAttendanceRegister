package com.codedleaf.sylveryte.myemployeeattendanceregister.Editing;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.codedleaf.sylveryte.myemployeeattendanceregister.Labs.EmployeeLab;
import com.codedleaf.sylveryte.myemployeeattendanceregister.Models.Employee;
import com.codedleaf.sylveryte.myemployeeattendanceregister.R;

import java.util.UUID;

/**
 * Created by sylveryte on 20/7/16.
 *
 * Copyright (C) 2016 sylveryte@codedleaf <codedleaf@gmail.com>
 *
 * This file is part of My Employee Attendance Register.
 *
 */
public class EmployeeAdditionFragmentDialog extends DialogFragment{

    private static final String ARGS_CODE="empargscode";

    private EditText mName;
    private EditText mAddress;
    private EditText mNote;
    private EditText mAge;

    private RadioButton mRadioButtonMale;
    private RadioButton mRadioButtonFemale;

    private Employee mEmployee;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View view= LayoutInflater.from(getActivity()).inflate(R.layout.employee_addition_fragment,null,false);

        mName=(EditText)view.findViewById(R.id.employee_add_name);
        mAddress=(EditText)view.findViewById(R.id.employee_add_address);
        mNote=(EditText)view.findViewById(R.id.employee_add_note);
        mAge=(EditText)view.findViewById(R.id.employee_add_age);
        RadioGroup radioGroupMaleFemale = (RadioGroup) view.findViewById(R.id.employee_add_radiobuttongroup_malefemale);
        mRadioButtonFemale=(RadioButton)view.findViewById(R.id.employee_add_radiobutton_female);
        mRadioButtonMale=(RadioButton)view.findViewById(R.id.employee_add_radiobutton_male);
        ImageButton done = (ImageButton) view.findViewById(R.id.emp_addition_done);

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //validation errors

                if (mName.getText().toString().trim().isEmpty())
                {
                    mName.setError("Please Enter Name");
                    return;
                }
                if (mAge.getText().toString().trim().isEmpty())
                {
                    mAge.setError("Please enter age");
                    return;
                }

                if(!(mRadioButtonMale.isChecked()||mRadioButtonFemale.isChecked()))
                {
                    mRadioButtonMale.setError("Please Select Gender");
                    return;
                }

                saveEmployee(mEmployee);
                getDialog().dismiss();
            }
        });

        radioGroupMaleFemale.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId==R.id.employee_add_radiobutton_male)
                {
                    mRadioButtonFemale.setChecked(false);
                    mRadioButtonMale.setChecked(true);
                }else if (checkedId==R.id.employee_add_radiobutton_female)
                {
                    mRadioButtonMale.setChecked(false);
                    mRadioButtonFemale.setChecked(true);
                }
            }
        });




        Bundle args=getArguments();
        //differentiate between edit and add calls
        if (args!=null)
        {
            UUID uuid=(UUID)args.getSerializable(ARGS_CODE);

            mEmployee= EmployeeLab.getInstanceOf(getActivity()).getEmployeeById(uuid);
            updateData();
        }

        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .create();
    }

    private void updateData()
        {
            mName.setText(mEmployee.getName());
            mAddress.setText(mEmployee.getAddress());
            mNote.setText(mEmployee.getNote());

            if(mEmployee.isMale())
            {
                mRadioButtonMale.setChecked(true);
                mRadioButtonFemale.setChecked(false);
            }else if (!mEmployee.isMale())
            {
                mRadioButtonFemale.setChecked(true);
                mRadioButtonMale.setChecked(false);
            }


            mAge.setText(mEmployee.getAgeString());
        }

    private void  saveEmployee(Employee employee)
    {
        if (employee==null)
        {
            mEmployee=new Employee();
        }
        EmployeeLab.getInstanceOf(getActivity()).addEmployee(mEmployee);
        mEmployee.setActive(true);
        mEmployee.setName(mName.getText().toString().trim());
        mEmployee.setAge(Integer.parseInt(mAge.getText().toString()));
        mEmployee.setAddress(mAddress.getText().toString().trim());
        mEmployee.setNote(mNote.getText().toString().trim());
        mEmployee.setMale(mRadioButtonMale.isChecked());
        mEmployee.updateMyDB(getActivity());
    }


    public static EmployeeAdditionFragmentDialog getDialogFrag(@Nullable UUID uuid)
    {
        if (uuid==null)
            return new EmployeeAdditionFragmentDialog();
        else
        {
            EmployeeAdditionFragmentDialog fragment=new EmployeeAdditionFragmentDialog();
            Bundle args=new Bundle();
            args.putSerializable(ARGS_CODE,uuid);
            fragment.setArguments(args);
            return fragment;
        }
    }
}
