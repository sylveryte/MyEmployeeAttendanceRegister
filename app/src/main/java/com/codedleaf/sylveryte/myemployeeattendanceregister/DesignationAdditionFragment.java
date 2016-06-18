package com.codedleaf.sylveryte.myemployeeattendanceregister;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.UUID;

/**
 * Created by sylveryte on 17/6/16.
 */
public class DesignationAdditionFragment extends Fragment {


    private EditText mEditText_siteName;
    private EditText mEditText_description;
    private Button mAddButton;
    private Designation mDesignation;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v=inflater.inflate(R.layout.designation_additioin_fragment,container,false);


        mEditText_siteName=(EditText)v.findViewById(R.id.editText_designation_name);
        mEditText_description=(EditText)v.findViewById(R.id.editText_designation_description);

        mAddButton = (Button)v.findViewById(R.id.button_add_designation);
        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //// TODO: 12/6/16 clean all this
                mDesignation.setTitle(mEditText_siteName.getText().toString());
                mDesignation.setDescription(mEditText_description.getText().toString());
                getActivity().finish();

            }
        });

        update();
        return v;

    }

    public void update()
    {
        mEditText_siteName.setText(mDesignation.getTitle());
        mEditText_description.setText(mDesignation.getDescription());
    }

    public void setDesignation(Designation designation) {
        mDesignation = designation;
    }

    public static DesignationAdditionFragment createInstance(Designation designation)
    {
        DesignationAdditionFragment designationAdditionFragment=new DesignationAdditionFragment();
        designationAdditionFragment.setDesignation(designation);
        return designationAdditionFragment;
    }

}
