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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by sylveryte on 20/7/16.
 */
public class EmployeeAdditionFragmentDialog extends DialogFragment implements PickDialogObserver {

    private static final String ARGS_CODE="empargscode";
    private static final String DIALOG_FRAGMENT_CODE="emppickdialogcode";

    private EditText mName;
    private EditText mAddress;
    private EditText mNote;
    private EditText mAge;

    private RadioButton mRadioButtonMale;
    private RadioButton mRadioButtonFemale;

    private Employee mEmployee;

    private LinearLayout mDesignationsLinearLayout;
    private LinearLayout mSitesLinearLayout;

    private PickDialogObserver mItSelf;

    private List<UUID> designationPicked;
    private List<UUID> sitesPicked;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        mItSelf=this;
        View view= LayoutInflater.from(getActivity()).inflate(R.layout.employee_addition_fragment,null,false);

        mName=(EditText)view.findViewById(R.id.employee_add_name);
        mAddress=(EditText)view.findViewById(R.id.employee_add_address);
        mNote=(EditText)view.findViewById(R.id.employee_add_note);
        mAge=(EditText)view.findViewById(R.id.employee_add_age);
        mDesignationsLinearLayout =(LinearLayout) view.findViewById(R.id.employee_add_designation_linear_layout);
        mSitesLinearLayout =(LinearLayout)view.findViewById(R.id.employee_add_sites_linear_layout);
        RadioGroup radioGroupMaleFemale = (RadioGroup) view.findViewById(R.id.employee_add_radiobuttongroup_malefemale);
        mRadioButtonFemale=(RadioButton)view.findViewById(R.id.employee_add_radiobutton_female);
        mRadioButtonMale=(RadioButton)view.findViewById(R.id.employee_add_radiobutton_male);
        Button chooseDesignationButton = (Button) view.findViewById(R.id.employee_choose_designation_button);
        Button chooseSiteButton = (Button) view.findViewById(R.id.employee_choose_site_button);
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
        //choose
        chooseDesignationButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                PickDialogFragment.getInstance(mEmployee.getId().toString()+"d",
                        mItSelf,
                        designationPicked,
                        DesignationLab.getInstanceOf(getActivity()).getDesignations()
                        )
                    .show(getActivity().getSupportFragmentManager(),DIALOG_FRAGMENT_CODE);
            }
        });

        chooseSiteButton.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

               PickDialogFragment.getInstance(mEmployee.getId().toString()+"s",
                       mItSelf,
                       sitesPicked,
                       SitesLab.getInstanceOf(getActivity()).getSites())
                       .show(getActivity().getSupportFragmentManager(),DIALOG_FRAGMENT_CODE);
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

            mEmployee=EmployeeLab.getInstanceOf(getActivity()).getEmployeeById(uuid);
            designationPicked=mEmployee.getDesignations();
            sitesPicked=mEmployee.getSites();
            updateData();
        }

        if (designationPicked==null)
        {
            designationPicked=new ArrayList<>();
        }
        if (sitesPicked==null)
        {
            sitesPicked=new ArrayList<>();
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

            updateDesgsAndSites();
        }


    private void updateDesgsAndSites()
    {

        designationPicked=PickCache.getInstance().getPicked(mEmployee.getId()+"d");
        sitesPicked=PickCache.getInstance().getPicked(mEmployee.getSites()+"s");

        LayoutInflater layoutInflater=LayoutInflater.from(getActivity());

        mDesignationsLinearLayout.removeAllViews();
        mSitesLinearLayout.removeAllViews();

        for (UUID uuid:designationPicked)
        {
            new SimpleDesignationView(mDesignationsLinearLayout,layoutInflater,uuid);
        }

        for (UUID uuid:sitesPicked)
        {
            new SimpleSiteView(mSitesLinearLayout,layoutInflater,uuid);
        }
    }

    @Override
    public void doSomeUpdate(Context context) {
        updateDesgsAndSites();
    }

    private class SimpleDesignationView
    {
        private LinearLayout mLinearLayout;
        private View mView;
        private UUID mUUID;

        public SimpleDesignationView(LinearLayout linearLayout, LayoutInflater layoutInflater, UUID uuid)
        {
            mLinearLayout=linearLayout;
            mUUID=uuid;
            mView=layoutInflater.inflate(R.layout.simple_tex_with_close_card,null);
            TextView textView=(TextView)mView.findViewById(R.id.simple_with_close_text);
            textView.setText(DesignationLab.getInstanceOf(getActivity()).getDesignationStringById(uuid));
            ImageButton closeButton=(ImageButton)mView.findViewById(R.id.simple_with_close_close_Button);
            closeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    designationPicked.remove(mUUID);
                    mLinearLayout.removeView(mView);
                }
            });

            mLinearLayout.addView(mView);
        }
    }
    private class SimpleSiteView
    {
        LinearLayout mLinearLayout;
        private View mView;
        private UUID mUUID;

        public SimpleSiteView(LinearLayout linearLayout, LayoutInflater layoutInflater, UUID uuid)
        {
            mLinearLayout=linearLayout;
            mUUID=uuid;
            mView=layoutInflater.inflate(R.layout.simple_tex_with_close_card,null);
            TextView textView=(TextView)mView.findViewById(R.id.simple_with_close_text);
            textView.setText(SitesLab.getInstanceOf(getActivity()).getSiteStringById(uuid));
            ImageButton closeButton=(ImageButton)mView.findViewById(R.id.simple_with_close_close_Button);
            closeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sitesPicked.remove(mUUID);
                    mLinearLayout.removeView(mView);
                }
            });
            mLinearLayout.addView(mView);
        }
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

        mEmployee.setDesignations(designationPicked,getActivity());
        mEmployee.setSites(sitesPicked,getActivity());

        mEmployee.updateMyDB(getActivity());
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
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
