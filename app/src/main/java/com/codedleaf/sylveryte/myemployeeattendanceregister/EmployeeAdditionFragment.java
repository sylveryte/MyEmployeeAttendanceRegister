package com.codedleaf.sylveryte.myemployeeattendanceregister;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.UUID;

/**
 * Created by sylveryte on 15/6/16.
 */
public class EmployeeAdditionFragment extends Fragment {


    private EditText mName;
    private EditText mAddress;
    private EditText mAge;
    private RadioGroup mRadioGroupMaleFemale;
    private RadioButton mRadioButtonMale;
    private RadioButton mRadioButtonFemale;
    private Button mAddButton;
    private Button mChooseDesignationButton;
    private Button mChooseSiteButton;
    private Employee mEmployee;
    private TextView mDesignations;
    private TextView mSites;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.employee_addition_fragment,container,false);


        mName=(EditText)view.findViewById(R.id.employee_add_name);
        mAddress=(EditText)view.findViewById(R.id.employee_add_address);
        mAge=(EditText)view.findViewById(R.id.employee_add_age);
        mDesignations=(TextView)view.findViewById(R.id.employee_add_textview_designation);
        mSites=(TextView)view.findViewById(R.id.employee_add_textview_site);
        mRadioGroupMaleFemale=(RadioGroup)view.findViewById(R.id.employee_add_radiobuttongroup_malefemale);
        mRadioButtonFemale=(RadioButton)view.findViewById(R.id.employee_add_radiobutton_female);
        mRadioButtonMale=(RadioButton)view.findViewById(R.id.employee_add_radiobutton_male);
        mAddButton=(Button)view.findViewById(R.id.employee_add_add_button);
        mChooseDesignationButton=(Button)view.findViewById(R.id.employee_choose_designation_button);
        mChooseSiteButton=(Button)view.findViewById(R.id.employee_choose_site_button);


        //choose
        mChooseDesignationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=PickActivity.fetchIntent(getActivity(),PickActivity.FRAGMENT_CODE_PICK_DESIGNATION);
                startActivityForResult(intent,PickActivity.FRAGMENT_CODE_PICK_DESIGNATION);
            }
        });

        mChooseSiteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=PickActivity.fetchIntent(getActivity(),PickActivity.FRAGMENT_CODE_PICK_SITE);
                startActivityForResult(intent,PickActivity.FRAGMENT_CODE_PICK_SITE);
            }
        });

        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mEmployee.setActive(true);
                mEmployee.setName(mName.getText().toString());
                mEmployee.setAge(Integer.parseInt(mAge.getText().toString()));
                mEmployee.setAdress(mAddress.getText().toString());
                getActivity().finish();
            }
        });

        mRadioGroupMaleFemale.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId==R.id.employee_add_radiobutton_male)
                {
                    mRadioButtonFemale.setChecked(false);
                    mRadioButtonMale.setChecked(true);
                    mEmployee.setMale(true);
                }else if (checkedId==R.id.employee_add_radiobutton_female)
                {
                    mRadioButtonMale.setChecked(false);
                    mRadioButtonFemale.setChecked(true);
                    mEmployee.setMale(false);
                }
            }
        });

        update();
        return view;
    }

    public void update()
    {
        mName.setText(mEmployee.getName());
        mAddress.setText(mEmployee.getAdress());

        if(mEmployee.isMale())
        {
            mRadioButtonMale.setChecked(true);
            mRadioButtonFemale.setChecked(false);
        }else if (!mEmployee.isMale())
        {
            mRadioButtonFemale.setChecked(true);
            mRadioButtonMale.setChecked(false);
        }

        mSites.setText(mEmployee.getSiteString());
        mDesignations.setText(mEmployee.getDesignationString());
        mAge.setText(mEmployee.getAgeString());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(data==null)
            return;

        UUID uuid=(UUID)data.getSerializableExtra(PickActivity.RESULT_DATA_STRING_PICK_GENRAL);

        if(requestCode==PickActivity.FRAGMENT_CODE_PICK_DESIGNATION)
        {
            mEmployee.addDesignation(uuid);

            Designation designation=DesignationLab.getInstanceOf().getDesigantionById(uuid);
            String s=designation.getTitle();
            s=mEmployee.getDesignationString();

            mDesignations.setText(mEmployee.getDesignationString());
        }
        else if (requestCode==PickActivity.FRAGMENT_CODE_PICK_SITE)
        {
            mEmployee.addSite(uuid);
            mSites.setText(mEmployee.getSiteString());
        }

    }

    public void setEmployee(Employee employee) {
        mEmployee = employee;
    }

    public static EmployeeAdditionFragment createInstance(Employee employee)
    {
        EmployeeAdditionFragment employeeAdditionFragment=new EmployeeAdditionFragment();
        employeeAdditionFragment.setEmployee(employee);
        return employeeAdditionFragment;
    }
}
