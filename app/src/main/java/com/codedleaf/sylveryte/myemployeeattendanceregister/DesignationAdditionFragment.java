package com.codedleaf.sylveryte.myemployeeattendanceregister;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
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


    private EditText mEditText_designationName;
    private EditText mEditText_description;
    private Button mAddButton;
    private Designation mDesignation;

    private static String ARGS_CODE="desargcode";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActivity().setTitle("Edit Designation");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v=inflater.inflate(R.layout.designation_additioin_fragment,container,false);

        //reccover data
        UUID uuid=(UUID)getArguments().getSerializable(ARGS_CODE);
        mDesignation=DesignationLab.getInstanceOf(getActivity()).getDesigantionById(uuid);

        mEditText_designationName =(EditText)v.findViewById(R.id.editText_designation_name);
        mEditText_description=(EditText)v.findViewById(R.id.editText_designation_description);

        mAddButton = (Button)v.findViewById(R.id.button_add_designation);
        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //// TODO: 12/6/16 clean all this
                saveUpdateData();
                getActivity().finish();

            }
        });

        update();
        return v;

    }


    public void saveUpdateData()
    {
        mDesignation.setTitle(mEditText_designationName.getText().toString());
        mDesignation.setDescription(mEditText_description.getText().toString());

        DesignationLab.getInstanceOf(getActivity()).updateDesignation(mDesignation);
    }

    public void update()
    {
        mEditText_designationName.setText(mDesignation.getTitle());
        mEditText_description.setText(mDesignation.getDescription());
    }


    public static DesignationAdditionFragment createInstance(Designation designation)
    {
        DesignationAdditionFragment designationAdditionFragment=new DesignationAdditionFragment();

        Bundle args=new Bundle();
        args.putSerializable(ARGS_CODE,designation.getId());
        designationAdditionFragment.setArguments(args);

        return designationAdditionFragment;
    }

}
