package com.codedleaf.sylveryte.myemployeeattendanceregister;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by sylveryte on 14/6/16.
 */
public class EmployeeFragment extends Fragment implements LabObserver {

    private EmployeeLab mLab;
    private RecyclerView mRecyclerView;
    private EmployeeAdapter mEmployeeAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mLab=EmployeeLab.getInstanceOf();
        mLab.addListener(this);

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.recycler_fragment,container,false);

        mRecyclerView =(RecyclerView)view.findViewById(R.id.fragment_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mEmployeeAdapter=new EmployeeAdapter(mLab.getEmployees());
        mRecyclerView.setAdapter(mEmployeeAdapter);

        return view;
    }


    private class EmployeeAdapter extends RecyclerView.Adapter<EmployeeHolder>
    {
        List<Employee> mEmployeeList;

        public EmployeeAdapter(List<Employee> employees)
        {
            mEmployeeList=employees;
        }

        @Override
        public EmployeeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater=getActivity().getLayoutInflater();
            View view=inflater.inflate(R.layout.employee_card,parent,false);
            return  new EmployeeHolder(view);
        }

        @Override
        public void onBindViewHolder(EmployeeHolder holder, int position) {
            holder.bind(mEmployeeList.get(position));
        }

        @Override
        public int getItemCount() {
            return mEmployeeList.size();
        }
    }

    private class EmployeeHolder extends RecyclerView.ViewHolder
    {


        TextView name;
        TextView site;
        TextView designation;
        TextView age;
        TextView active;

        public EmployeeHolder(View itemView)
        {
            super(itemView);
            name=(TextView)itemView.findViewById(R.id.employee_card_name);
            site=(TextView)itemView.findViewById(R.id.employee_card_site);
            designation=(TextView)itemView.findViewById(R.id.employee_card_designation);
            age=(TextView)itemView.findViewById(R.id.employee_card_age);
            active=(TextView)itemView.findViewById(R.id.employee_card_active);

        }

        public void bind(Employee employee)
        {
            name.setText(employee.getName());
            site.setText(employee.getSiteString());
            designation.setText(employee.getDesignationString());
            age.setText(String.format("%d", employee.getAge()));
            //// TODO: 14/6/16 get these strings somewhere flexible
            active.setText(employee.isActive()?"Active":"Not Active");
        }
    }

    @Override
    public void update() {

    }

    public static Fragment newInstance()
    {
        return new EmployeeFragment();
    }

}
