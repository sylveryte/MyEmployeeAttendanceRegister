package com.codedleaf.sylveryte.myemployeeattendanceregister;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.joda.time.LocalDate;

import java.util.List;
import java.util.Locale;

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

        mLab=EmployeeLab.getInstanceOf(getActivity());
        mLab.addListener(this);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.recycler_fragment,container,false);

        mRecyclerView =(RecyclerView)view.findViewById(R.id.fragment_recycler_view);

        //for automatic
        //// TODO: 22/6/16  looks suspicious
        DisplayMetrics metrics=new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int cardWidth=(int)metrics.xdpi*2;
        int spans=(int)Math.floor(mRecyclerView.getContext().getResources().getDisplayMetrics().widthPixels/(float)cardWidth);

        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(spans,StaggeredGridLayoutManager.VERTICAL));

      //  mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL));
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

        private static final String DIALOG_FRAGMENT_CODE = "editdialogemp";

        private TextView name;
        private TextView site;
        private TextView designation;
        private TextView age;
        private TextView active;
        private Employee mEmployee;
        private CardView mCardView;

        public EmployeeHolder(View itemView)
        {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    CharSequence choices[] = new CharSequence[] {"Edit","Active??","Show Site(s)","Show Designation(s)","Delete"};

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
                                case 4:
                                {
                                    new AlertDialog.Builder(getActivity())
                                            .setTitle("Delete "+mEmployee.getTitle())
                                            .setMessage("Are you sure you want to delete this employee?")
                                            .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    // continue with delete
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

                                case 1:
                                {
                                    mEmployee.setActive(!mEmployee.isActive());
                                    mEmployee.updateMyDB(getActivity());
                                    update();
                                    break;
                                }


                                case 0: {
                                    EmployeeAdditionDialogFragment.getDialogFrag(mEmployee.getId())
                                            .show(getActivity().getSupportFragmentManager(),DIALOG_FRAGMENT_CODE);

                                    break;
                                }
                                case 2: {

                                    //hah haha :<
                                    //after few weeks :grin:
                                    SimpleListDialogFragment.getInstance(mEmployee.getId().toString(),
                                            SitesLab.getInstanceOf(getActivity()).getPickables(mEmployee.getSites()))
                                            .show(getActivity().getSupportFragmentManager(),DIALOG_FRAGMENT_CODE);

                                    break;
                                }
                                case 3:
                                {
                                    SimpleListDialogFragment.getInstance(mEmployee.getId().toString(),
                                            DesignationLab.getInstanceOf(getActivity()).getPickables(mEmployee.getDesignations()))
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
        }
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
            mEmployeeAdapter.notifyDataSetChanged();
    }

    public static Fragment newInstance()
    {
        return new EmployeeFragment();
    }

}
