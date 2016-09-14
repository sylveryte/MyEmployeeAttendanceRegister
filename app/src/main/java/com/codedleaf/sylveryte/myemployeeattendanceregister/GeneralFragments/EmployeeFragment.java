package com.codedleaf.sylveryte.myemployeeattendanceregister.GeneralFragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.codedleaf.sylveryte.myemployeeattendanceregister.CircleTransform;
import com.codedleaf.sylveryte.myemployeeattendanceregister.Models.Employee;
import com.codedleaf.sylveryte.myemployeeattendanceregister.Editing.EmployeeAdditionFragmentDialog;
import com.codedleaf.sylveryte.myemployeeattendanceregister.Labs.LabObserver;
import com.codedleaf.sylveryte.myemployeeattendanceregister.Labs.DesignationLab;
import com.codedleaf.sylveryte.myemployeeattendanceregister.Labs.EmployeeLab;
import com.codedleaf.sylveryte.myemployeeattendanceregister.Labs.SitesLab;
import com.codedleaf.sylveryte.myemployeeattendanceregister.Picknation.PickDialogFragment;
import com.codedleaf.sylveryte.myemployeeattendanceregister.R;
import com.codedleaf.sylveryte.myemployeeattendanceregister.RegisterConstants;
import com.codedleaf.sylveryte.myemployeeattendanceregister.Picknation.SimpleListDialogFragment;
import com.codedleaf.sylveryte.myemployeeattendanceregister.Stats.StatActivity;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by sylveryte on 14/6/16.
 *
 * Copyright (C) 2016 sylveryte@codedleaf <codedleaf@gmail.com>
 *
 * This file is part of My Employee Attendance Register.
 *
 */
public class EmployeeFragment extends Fragment implements LabObserver,SearchView.OnQueryTextListener {

    private EmployeeLab mLab;
    private RecyclerView mRecyclerView;
    private EmployeeAdapter mEmployeeAdapter;

    private List<Employee> mEmployeeList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mLab=EmployeeLab.getInstanceOf(getActivity());
        mLab.addListener(this);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.recycler_fragment,container,false);

        setHasOptionsMenu(true);

        mRecyclerView =(RecyclerView)view.findViewById(R.id.fragment_recycler_view);


        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        //for automatic
        //// TODO: 22/6/16  looks suspicious
        DisplayMetrics metrics=new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int cardWidth=(int)metrics.xdpi*2;
        int spans=(int)Math.floor(mRecyclerView.getContext().getResources().getDisplayMetrics().widthPixels/(float)cardWidth);

        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(spans,StaggeredGridLayoutManager.VERTICAL));


        mEmployeeList=new ArrayList<>(mLab.getEmployees());
        mEmployeeAdapter=new EmployeeAdapter(mEmployeeList);
        mRecyclerView.setAdapter(mEmployeeAdapter);
    }

    private class EmployeeAdapter extends RecyclerView.Adapter<EmployeeHolder>
    {

        List<Employee> mAdapterEmployees;
        public EmployeeAdapter(List<Employee> employees)
        {
            mAdapterEmployees=new ArrayList<>(employees);
        }

        @Override
        public EmployeeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater=getActivity().getLayoutInflater();
            View view=inflater.inflate(R.layout.employee_card,parent,false);
            return  new EmployeeHolder(view);
        }

        @Override
        public void onBindViewHolder(EmployeeHolder holder, int position) {
            holder.bind(mAdapterEmployees.get(position));
        }

        @Override
        public int getItemCount() {
            return mAdapterEmployees.size();
        }

        public Employee removeItem(int position)
        {
            final Employee employee=mAdapterEmployees.remove(position);
            notifyItemRemoved(position);
            return employee;
        }

        public void addItem(int position, Employee employee)
        {
            mAdapterEmployees.add(position,employee);
            notifyItemInserted(position);
        }

        public void moveItem(int fromPosition,int toPosition)
        {
            final Employee employee=mAdapterEmployees.remove(fromPosition);
            mAdapterEmployees.add(toPosition,employee);
            notifyItemMoved(fromPosition,toPosition);
        }

        public void animateTo(List<Employee> employees)
        {
            applyAndAnimateRemovals(employees);
            applyAndAnimateAddition(employees);
            applyAndAnimateMovedItems(employees);
        }

        private void applyAndAnimateRemovals(List<Employee> employees)
        {
            for (int i=mAdapterEmployees.size()-1;i>=0;i--)
            {
                final Employee employee=mAdapterEmployees.get(i);
                if (!employees.contains(employee))
                {
                    removeItem(i);
                }
            }
        }

        private void applyAndAnimateAddition(List<Employee> employees)
        {
            for (int i=0,count=employees.size();i<count;i++)
            {
                final Employee employee=employees.get(i);
                if (!mAdapterEmployees.contains(employee))
                {
                    addItem(i,employee);
                }
            }
        }

        private void applyAndAnimateMovedItems(List<Employee> employees)
        {
            for (int toPosition=employees.size()-1;toPosition >= 0; toPosition--)
            {
                final Employee employee=employees.get(toPosition);
                final int fromPosition=mAdapterEmployees.indexOf(employee);
                if (fromPosition>=0&&fromPosition!=toPosition)
                {
                    moveItem(fromPosition,toPosition);
                }
            }
        }
    }
    //end of adapter class


    //onquery
    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        final List<Employee> filteredEmployees=filter(mEmployeeList,newText);
        mEmployeeAdapter.animateTo(filteredEmployees);
//        mRecyclerView.scrollTo(0,0);

        //this one is buggy glitchy
//        mRecyclerView.scrollToPosition(0);
        return true;
    }

    private List<Employee> filter(List<Employee> employeeList, String newText)
    {
        newText=newText.toLowerCase();

        final List<Employee> filteredEmployeeList=new ArrayList<>();

        for (Employee employee:employeeList)
        {
            final String text=employee.getTitle().toLowerCase();
            if (text.contains(newText))
            {
                filteredEmployeeList.add(employee);
            }
        }
        return filteredEmployeeList;
    }


    //start of holder class
    private class EmployeeHolder extends RecyclerView.ViewHolder
    {

        private static final String DIALOG_FRAGMENT_CODE = "editdialogemp";
        private static final String DIALOG_PICK_FRAGMENT_CODE = "picking up bro";

        private TextView name;
        private TextView site;
        private TextView designation;
        private TextView age;
        private TextView active;
        private Employee mEmployee;
        private CardView mCardView;
        private ImageView mPhotoEmp;

        public EmployeeHolder(View itemView)
        {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    CharSequence choices[] = new CharSequence[] {"Edit","Active??","Show Stat","Show Site(s)","Show Designation(s)","Assign Site(s)","Assign Designation(s)","Delete"};

                    if (mEmployee.isActive())
                    {
                        choices[1]="Deactivate";
                    }
                    else
                    {
                        choices[1]="Activate";
                    }

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle(mEmployee.getTitle());
                    builder.setItems(choices, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            switch (which)
                            {
                                case 7:
                                {
                                    new AlertDialog.Builder(getActivity())
                                            .setTitle("Delete "+mEmployee.getTitle())
                                            .setMessage("Are you sure you want to delete this employee?")
                                            .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    // continue with delete
                                                    mEmployeeAdapter.removeItem(mEmployeeList.indexOf(mEmployee));
                                                    mLab.deleteEmployee(mEmployee,getActivity());
                                                    update();
                                                    getActivity().invalidateOptionsMenu();
                                                }
                                            })
                                            .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    // do nothing
                                                }
                                            })
                                            .setIcon(android.R.drawable.ic_dialog_alert)
                                            .show();

                                    break;
                                }

                                case 2:
                                {
                                    startActivity(StatActivity.fetchIntent(getActivity(), RegisterConstants.EMPLOYEE,mEmployee.getId()));
                                    break;
                                }

                                case 5:
                                {

                                    //assign sites
                                    PickDialogFragment.getInstance(mEmployee.getId().toString()+"s",
                                            mEmployee,
                                            mEmployee.getSites(),
                                            SitesLab.getInstanceOf(getActivity()).getSites())
                                            .show(getActivity().getSupportFragmentManager(),DIALOG_PICK_FRAGMENT_CODE);
                                    break;

                                }
                                case 6:
                                {

                                    //assign desgs
                                    PickDialogFragment.getInstance(mEmployee.getId().toString()+"d",
                                            mEmployee,
                                            mEmployee.getDesignations(),
                                            DesignationLab.getInstanceOf(getActivity()).getDesignations())
                                            .show(getActivity().getSupportFragmentManager(),DIALOG_PICK_FRAGMENT_CODE);
                                    break;
                                }
                                case 1:
                                {
                                    mEmployee.setActive(!mEmployee.isActive());
                                    mEmployee.updateMyDB(getActivity());
                                    update();
                                    break;
                                }


                                case 0: {
                                    EmployeeAdditionFragmentDialog.getDialogFrag(mEmployee.getId())
                                            .show(getActivity().getSupportFragmentManager(),DIALOG_FRAGMENT_CODE);

                                    break;
                                }
                                case 3: {

                                    //hah haha :<
                                    //after few weeks :grin:
                                    SimpleListDialogFragment.getInstance(mEmployee.getId().toString(),
                                            SitesLab.getInstanceOf(getActivity()).getPickables(mEmployee.getSites()),SimpleListDialogFragment.NORMAL_MODE)
                                            .show(getActivity().getSupportFragmentManager(),DIALOG_FRAGMENT_CODE);

                                    break;
                                }
                                case 4:
                                {
                                    SimpleListDialogFragment.getInstance(mEmployee.getId().toString(),
                                            DesignationLab.getInstanceOf(getActivity()).getPickables(mEmployee.getDesignations()),SimpleListDialogFragment.NORMAL_MODE)
                                            .show(getActivity().getSupportFragmentManager(),DIALOG_FRAGMENT_CODE);
                                }
                            }
                        }
                    });
                    builder.show();
                }
            });

            name=(TextView)itemView.findViewById(R.id.employee_card_name);
            site=(TextView)itemView.findViewById(R.id.employee_card_site);
            designation=(TextView)itemView.findViewById(R.id.employee_card_designation);
            age=(TextView)itemView.findViewById(R.id.employee_card_age);
            active=(TextView)itemView.findViewById(R.id.employee_card_active);
            mCardView=(CardView)itemView.findViewById(R.id.employee_card_card);
            mPhotoEmp=(ImageView)itemView.findViewById(R.id.employee_card_photo);

        }

        public void bind(Employee employee)
        {
            mEmployee=employee;
            name.setText(employee.getName());
            site.setText(employee.getSiteString(getActivity()));
            designation.setText(employee.getDesignationString(getActivity()));
            age.setText(String.format(Locale.ENGLISH,"%d", employee.getAge()));
            //// TODO: 14/6/16 get these strings somewhere flexible
            active.setText(employee.isActive()?"Active":"Not Active");
            if (mEmployee.isActive())
                mCardView.setCardBackgroundColor(ContextCompat.getColor(getActivity(),R.color.colorEmployeeCard));
            else
                mCardView.setCardBackgroundColor(ContextCompat.getColor(getActivity(),R.color.colorEmployeeCardDeactivated));

            //// TODO: 13/9/16 this to a service nigga
            File mPhotoFile=EmployeeLab.getInstanceOf(getContext()).getEmpPhotoFile(employee);
            if (mPhotoFile==null)
                mPhotoFile=EmployeeLab.getInstanceOf(getContext()).getEmpPhotoFile(employee);
//            mPhotoEmp.setImageBitmap(CodedleafTools.getScaledBitmap(mPhotoFile.getPath(),250,250));
            if (mPhotoFile.exists())
                Picasso.with(getContext())
                        .load(mPhotoFile)
                        .stableKey(employee.getId().toString())
                        .transform(new CircleTransform())
                        .resize(250,250)
                        .centerCrop()
//                        .centerInside()
                        .into(mPhotoEmp);
            else
                EmployeeLab.getInstanceOf(getContext()).setEmpPlaceHolder(employee,mPhotoEmp);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        MenuItem item=menu.findItem(R.id.action_search);
        SearchView searchView=(SearchView) item.getActionView();
        searchView.setOnQueryTextListener(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        update();
    }

    @Override
    public void onResume() {
        super.onResume();
        update();
    }

    @Override
    public void update() {
        if (mEmployeeAdapter!=null)
        {
            List<Employee> employees=new ArrayList<>(EmployeeLab.getInstanceOf(getActivity()).getEmployees());
            mEmployeeList=employees;
            mEmployeeAdapter.animateTo(employees);
            //causing no animation
            mEmployeeAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void picassoAlert(String path) {
        Picasso.with(getContext()).invalidate(path);
    }

    public static Fragment newInstance()
    {
        return new EmployeeFragment();
    }

}
