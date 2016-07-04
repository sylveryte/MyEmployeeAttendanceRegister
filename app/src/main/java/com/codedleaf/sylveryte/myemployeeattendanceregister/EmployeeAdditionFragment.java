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
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
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
    private LinearLayout mDesignations;
    private LinearLayout mSites;

    private List<UUID> already;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        already=new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.employee_addition_fragment,container,false);

        mName=(EditText)view.findViewById(R.id.employee_add_name);
        mAddress=(EditText)view.findViewById(R.id.employee_add_address);
        mAge=(EditText)view.findViewById(R.id.employee_add_age);
        mDesignations=(LinearLayout) view.findViewById(R.id.employee_add_designation_linear_layout);
        mSites=(LinearLayout)view.findViewById(R.id.employee_add_sites_linear_layout);
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
                saveData();
                Intent intent=PickActivity.fetchIntent(getActivity(),PickActivity.FRAGMENT_CODE_PICK_DESIGNATION);
                startActivityForResult(intent,PickActivity.FRAGMENT_CODE_PICK_DESIGNATION);
            }
        });

        mChooseSiteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
                Intent intent=PickActivity.fetchIntent(getActivity(),PickActivity.FRAGMENT_CODE_PICK_SITE);
                startActivityForResult(intent,PickActivity.FRAGMENT_CODE_PICK_SITE);
            }
        });

        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                saveData();
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

    private void saveData() {
        mEmployee.setActive(true);
        mEmployee.setName(mName.getText().toString());
        mEmployee.setAge(Integer.parseInt(mAge.getText().toString()));
        mEmployee.setAdress(mAddress.getText().toString());
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


        mAge.setText(mEmployee.getAgeString());

        updateDesgsAndSites();
    }


    private void updateDesgsAndSites()
    {

        List<UUID> designations=mEmployee.getDesignations();
        List<UUID> sites=mEmployee.getSites();

        LayoutInflater layoutInflater=getLayoutInflater(null);

        mDesignations.removeAllViews();
        mSites.removeAllViews();

        for (UUID uuid:designations)
        {
            new SimpleDesignationView(mDesignations,layoutInflater,uuid);
        }

        for (UUID uuid:sites)
        {
            new SimpleSiteView(mSites,layoutInflater,uuid);
        }
    }


    private class SimpleDesignationView
    {
        private LinearLayout mLinearLayout;
        private View mView;
        private DesignationLab mDesignationLab;
        private UUID mUUID;

        public SimpleDesignationView(LinearLayout linearLayout, LayoutInflater layoutInflater, UUID uuid)
        {
            mLinearLayout=linearLayout;
            mDesignationLab=DesignationLab.getInstanceOf();
            mUUID=uuid;
            mView=layoutInflater.inflate(R.layout.simple_tex_with_close_card,null);
            TextView textView=(TextView)mView.findViewById(R.id.simple_with_close_text);
            textView.setText(mDesignationLab.getDesignationStringById(uuid));
            ImageButton closeButton=(ImageButton)mView.findViewById(R.id.simple_with_close_close_Button);
            closeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mEmployee.removeDesignationById(mUUID);
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
        private SitesLab mSitesLab;
        private UUID mUUID;

        public SimpleSiteView(LinearLayout linearLayout, LayoutInflater layoutInflater, UUID uuid)
        {
            mLinearLayout=linearLayout;
            mSitesLab=SitesLab.getInstanceOf();
            mUUID=uuid;
            mView=layoutInflater.inflate(R.layout.simple_tex_with_close_card,null);
            TextView textView=(TextView)mView.findViewById(R.id.simple_with_close_text);
            textView.setText(mSitesLab.getSiteStringById(uuid));
            ImageButton closeButton=(ImageButton)mView.findViewById(R.id.simple_with_close_close_Button);
            closeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mEmployee.removeSiteByid(mUUID);
                    mLinearLayout.removeView(mView);
                }
            });
            mLinearLayout.addView(mView);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(data==null)
            return;

        UUID uuid=(UUID)data.getSerializableExtra(PickActivity.RESULT_DATA_STRING_PICK_GENRAL);

        if(requestCode==PickActivity.FRAGMENT_CODE_PICK_DESIGNATION)
        {
            mEmployee.addDesignationById(uuid);
            update();
        }
        else if (requestCode==PickActivity.FRAGMENT_CODE_PICK_SITE)
        {
            mEmployee.addSiteById(uuid);
            update();
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
