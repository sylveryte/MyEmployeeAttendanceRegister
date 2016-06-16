package com.codedleaf.sylveryte.myemployeeattendanceregister;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by sylveryte on 15/6/16.
 */
public class EmployeeAdditionFragment extends Fragment {


<<<<<<< HEAD
    private EditText mName;
    private EditText mAddress;
    private EditText mAge;
    private EditText mMaleFemale;
    private Button mAddButton;
=======
    EditText mName;
    EditText mAddress;
    EditText mAge;
    EditText mMaleFemale;
    Button mAddButton;
>>>>>>> 90fd633ae6a99ff6fd78612066532db8e63b1857

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
        mMaleFemale=(EditText)view.findViewById(R.id.employee_add_male_female);
        mAddButton=(Button)view.findViewById(R.id.employee_add_add_button);

        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               Employee employee=new Employee();
                employee.setActive(true);
                employee.setName(mName.getText().toString());
                employee.setAge(Integer.parseInt(mAge.getText().toString()));
                employee.setAdress(mAddress.getText().toString());
                employee.setMale(mMaleFemale.getText().toString().compareToIgnoreCase("male")==0);

                EmployeeLab.getInstanceOf().addEmployee(employee);

            }
        });

        return view;
    }

    public static EmployeeAdditionFragment createInstance()
    {
        return new EmployeeAdditionFragment();
    }
}
