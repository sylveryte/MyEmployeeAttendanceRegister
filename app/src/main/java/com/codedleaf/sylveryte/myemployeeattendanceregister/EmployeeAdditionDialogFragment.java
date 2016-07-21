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
public class EmployeeAdditionDialogFragment extends DialogFragment implements DialogPickObserver {

    private static final String ARGS_CODE="empargscode";
    public static final String MY_SITE_CALLER_CODE = "somethingsite";
    public static final String MY_DESIGNATION_CALLER_CODE = "somethingdesig";

    private EditText mName;
    private EditText mAddress;
    private EditText mNote;
    private EditText mAge;

    private RadioGroup mRadioGroupMaleFemale;
    private RadioButton mRadioButtonMale;
    private RadioButton mRadioButtonFemale;

    private Button mChooseDesignationButton;
    private Button mChooseSiteButton;

    private ImageButton mDone;

    private Employee mEmployee;

    private LinearLayout mDesignationsLinearLayout;
    private LinearLayout mSitesLinearLayout;

    private DialogPickObserver mItSelf;

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
        mRadioGroupMaleFemale=(RadioGroup)view.findViewById(R.id.employee_add_radiobuttongroup_malefemale);
        mRadioButtonFemale=(RadioButton)view.findViewById(R.id.employee_add_radiobutton_female);
        mRadioButtonMale=(RadioButton)view.findViewById(R.id.employee_add_radiobutton_male);
        mChooseDesignationButton=(Button)view.findViewById(R.id.employee_choose_designation_button);
        mChooseSiteButton=(Button)view.findViewById(R.id.employee_choose_site_button);
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
        //choose
        mChooseDesignationButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new PickDialogFragment().getInstance(MY_DESIGNATION_CALLER_CODE,mItSelf,PickDialogFragment.DESIGNATION,null)
                    .show(getActivity().getSupportFragmentManager(),MY_DESIGNATION_CALLER_CODE);
            }
        });

        mChooseSiteButton.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

               new PickDialogFragment().getInstance(MY_SITE_CALLER_CODE,mItSelf,PickDialogFragment.SITE,null)
                       .show(getActivity().getSupportFragmentManager(),MY_SITE_CALLER_CODE);
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
            PickCache.getInstance().addThisInMyList(MY_DESIGNATION_CALLER_CODE,mEmployee.getDesignations());
            PickCache.getInstance().addThisInMyList(MY_SITE_CALLER_CODE,mEmployee.getSites());
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

            updateDesgsAndSites();
        }


    private void updateDesgsAndSites()
    {

        List<UUID> designations=PickCache.getInstance().getUUIDs(MY_DESIGNATION_CALLER_CODE);
        List<UUID> sites=PickCache.getInstance().getUUIDs(MY_SITE_CALLER_CODE);

        LayoutInflater layoutInflater=LayoutInflater.from(getActivity());

        mDesignationsLinearLayout.removeAllViews();
        mSitesLinearLayout.removeAllViews();

        for (UUID uuid:designations)
        {
            new SimpleDesignationView(mDesignationsLinearLayout,layoutInflater,uuid);
        }

        for (UUID uuid:sites)
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
                    PickCache.getInstance().getUUIDs(MY_DESIGNATION_CALLER_CODE).remove(mUUID);
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
                    PickCache.getInstance().getUUIDs(MY_SITE_CALLER_CODE).remove(mUUID);
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

        mEmployee.setDesignations(PickCache.getInstance().getUUIDs(MY_DESIGNATION_CALLER_CODE),getActivity());
        mEmployee.setSites(PickCache.getInstance().getUUIDs(MY_SITE_CALLER_CODE),getActivity());

        mEmployee.updateMyDB(getActivity());
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        PickCache.getInstance().destroyMyCache(MY_DESIGNATION_CALLER_CODE);
        PickCache.getInstance().destroyMyCache(MY_SITE_CALLER_CODE);
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
