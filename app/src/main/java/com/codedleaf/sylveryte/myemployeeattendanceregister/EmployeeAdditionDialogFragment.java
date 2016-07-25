package com.codedleaf.sylveryte.myemployeeattendanceregister;

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
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.List;
import java.util.UUID;

/**
 * Created by sylveryte on 20/7/16.
 */
public class EmployeeAdditionDialogFragment extends DialogFragment {

    private static final String ARGS_CODE="empargscode";

    private EditText mName;
    private EditText mAddress;
    private EditText mNote;
    private EditText mAge;

    private RadioGroup mRadioGroupMaleFemale;
    private RadioButton mRadioButtonMale;
    private RadioButton mRadioButtonFemale;

    private ImageButton mDone;

    private Employee mEmployee;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view= LayoutInflater.from(getActivity()).inflate(R.layout.employee_addition_fragment,null,false);

        mName=(EditText)view.findViewById(R.id.employee_add_name);
        mAddress=(EditText)view.findViewById(R.id.employee_add_address);
        mNote=(EditText)view.findViewById(R.id.employee_add_note);
        mAge=(EditText)view.findViewById(R.id.employee_add_age);
        mRadioGroupMaleFemale=(RadioGroup)view.findViewById(R.id.employee_add_radiobuttongroup_malefemale);
        mRadioButtonFemale=(RadioButton)view.findViewById(R.id.employee_add_radiobutton_female);
        mRadioButtonMale=(RadioButton)view.findViewById(R.id.employee_add_radiobutton_male);
        mDone=(ImageButton)view.findViewById(R.id.emp_addition_done);

        mDone.setOnClickListener(new View.OnClickListener() {
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


        mRadioGroupMaleFemale.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
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

            mEmployee=EmployeeLab.getInstanceOf(getActivity()).getEmployeeById(uuid);
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
            EmployeeLab.getInstanceOf(getActivity()).addEmployee(mEmployee);
        }
        mEmployee.setActive(true);
        mEmployee.setName(mName.getText().toString());
        mEmployee.setAge(Integer.parseInt(mAge.getText().toString()));
        mEmployee.setAddress(mAddress.getText().toString());
        mEmployee.setNote(mNote.getText().toString());
        mEmployee.setMale(mRadioButtonMale.isChecked());
        mEmployee.updateMyDB(getActivity());
    }

    public static EmployeeAdditionDialogFragment getDialogFrag(@Nullable UUID uuid)
    {
        if (uuid==null)
            return new EmployeeAdditionDialogFragment();
        else
        {
            EmployeeAdditionDialogFragment fragment=new EmployeeAdditionDialogFragment();
            Bundle args=new Bundle();
            args.putSerializable(ARGS_CODE,uuid);
            fragment.setArguments(args);
            return fragment;
        }
    }
}