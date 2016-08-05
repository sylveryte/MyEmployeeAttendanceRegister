package com.codedleaf.sylveryte.myemployeeattendanceregister.GeneralFragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codedleaf.sylveryte.myemployeeattendanceregister.Models.Designation;
import com.codedleaf.sylveryte.myemployeeattendanceregister.Editing.DesignationAdditionDialogFragment;
import com.codedleaf.sylveryte.myemployeeattendanceregister.Labs.LabObserver;
import com.codedleaf.sylveryte.myemployeeattendanceregister.Labs.DesignationLab;
import com.codedleaf.sylveryte.myemployeeattendanceregister.Labs.EmployeeLab;
import com.codedleaf.sylveryte.myemployeeattendanceregister.Picknation.PickDialogFragment;
import com.codedleaf.sylveryte.myemployeeattendanceregister.R;
import com.codedleaf.sylveryte.myemployeeattendanceregister.Picknation.SimpleListDialogFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sylveryte on 17/6/16.
 *
 * Copyright (C) 2016 sylveryte@codedleaf <codedlaf@gmail.com>
 *
 * This file is part of My Employee Attendance Register.
 *
 */
public class DesignationFragment extends Fragment implements LabObserver,SearchView.OnQueryTextListener{


    private RecyclerView mRecyclerView;
    private DesignationLab mLab;
    private DesignationAdapter mDesignationAdapter;

    private List<Designation> mDesignations;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mLab=DesignationLab.getInstanceOf(getActivity());
        mLab.addListener(this);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View view=inflater.inflate(R.layout.recycler_fragment,container,false);

        mRecyclerView =(RecyclerView)view.findViewById(R.id.fragment_recycler_view);

        setHasOptionsMenu(true);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

           /* //for automatic
        //// TODO: 22/6/16  looks suspicious
        DisplayMetrics metrics=new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int cardWidth=(int)metrics.xdpi;
        int spans=(int)Math.floor(mRecyclerView.getContext().getResources().getDisplayMetrics().widthPixels/(float)cardWidth);*/


        //keeping two spans only
//        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));

        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));

        mDesignations=mLab.getDesignations();
        mDesignationAdapter=new DesignationAdapter(mDesignations);
        mRecyclerView.setAdapter(mDesignationAdapter);
    }

    private class DesignationHolder extends RecyclerView.ViewHolder
    {


        private static final String DIALOG_FRAGMENT_CODE = "editdialogdesig";
        private static final String DIALOG_PICK_FRAGMENT_CODE = "pickempdialogdesig";

        private TextView title;
        private TextView description;
        private Designation mDesignation;

        public DesignationHolder(final View itemView) {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    CharSequence choices[] = new CharSequence[] {"Assign Employees", "Show Assignments","Edit","Delete"};

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle(mDesignation.getTitle());
                    builder.setItems(choices, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            switch (which)
                            {
                                case 3:
                                {
                                    new AlertDialog.Builder(getActivity())
                                            .setTitle("Delete "+mDesignation.getTitle())
                                            .setMessage("Are you sure you want to delete this designation?")
                                            .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    // continue with delete
                                                    mLab.deleteDesignation(mDesignation,getActivity());
                                                    update();
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

                                case 2: {

                                    DesignationAdditionDialogFragment.getDialogFrag(mDesignation.getId())
                                            .show(getActivity().getSupportFragmentManager(),DIALOG_FRAGMENT_CODE);

                                    break;
                                }
                                case 1:
                                {
                                    SimpleListDialogFragment.getInstance(mDesignation.getId().toString()
                                            , EmployeeLab.getInstanceOf(getActivity()).getPickables(mDesignation.getEmployees()))
                                            .show(getActivity().getSupportFragmentManager(),DIALOG_FRAGMENT_CODE);
                                    break;
                                }
                                case 0: {

                                    //haha a :( lot of work
                                    // (afte few weeks) yeah :? :grin: :grin:
                                    PickDialogFragment.getInstance(mDesignation.getId().toString(),
                                            mDesignation,
                                            mDesignation.getEmployees(),
                                            EmployeeLab.getInstanceOf(getActivity()).getEmployees())
                                            .show(getActivity().getSupportFragmentManager(),DIALOG_PICK_FRAGMENT_CODE);

                                    break;
                                }
                            }
                        }
                    });
                    builder.show();
                }
            });

            title=(TextView)itemView.findViewById(R.id.designation_card_title);
            description=(TextView)itemView.findViewById(R.id.designation_card_description);
        }

        public void bind(Designation designation)
        {
            mDesignation=designation;
            title.setText(designation.getTitle());
            description.setText(designation.getDescription());
        }

    }

    private class DesignationAdapter extends RecyclerView.Adapter<DesignationHolder> {

        private List<Designation> mAdapterDesignations;

        public DesignationAdapter(List<Designation> designationList)
        {
            mAdapterDesignations =new ArrayList<>(designationList);
        }

        @Override
        public DesignationHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            LayoutInflater inflater=getActivity().getLayoutInflater();
            View view=inflater.inflate(R.layout.designation_card,parent,false);

            return new DesignationHolder(view);
        }

        @Override
        public void onBindViewHolder(DesignationHolder holder, int position) {

            holder.bind(mAdapterDesignations.get(position));

        }

        @Override
        public int getItemCount() {
            return mAdapterDesignations.size();
        }

        public Designation removeItem(int position)
        {
            final Designation designation=mAdapterDesignations.remove(position);
            notifyItemRemoved(position);
            return designation;
        }

        public void addItem(int position, Designation designation)
        {
            mAdapterDesignations.add(position,designation);
            notifyItemInserted(position);
        }

        public void moveItem(int fromPosition,int toPosition)
        {
            final Designation designation=mAdapterDesignations.remove(fromPosition);
            mAdapterDesignations.add(toPosition,designation);
            notifyItemMoved(fromPosition,toPosition);
        }

        public void animateTo(List<Designation> designations)
        {
            applyAndAnimateRemovals(designations);
            applyAndAnimateAddition(designations);
            applyAndAnimateMovedItems(designations);
        }

        private void applyAndAnimateRemovals(List<Designation> designations)
        {
            for (int i=mAdapterDesignations.size()-1;i>=0;i--)
            {
                final Designation designation=mAdapterDesignations.get(i);
                if (!designations.contains(designation))
                {
                    removeItem(i);
                }
            }
        }

        private void applyAndAnimateAddition(List<Designation> designations)
        {
            for (int i=0,count=designations.size();i<count;i++)
            {
                final Designation designation=designations.get(i);
                if (!mAdapterDesignations.contains(designation))
                {
                    addItem(i,designation);
                }
            }
        }

        private void applyAndAnimateMovedItems(List<Designation> designations)
        {
            for (int toPosition=designations.size()-1;toPosition >= 0; toPosition--)
            {
                final Designation designation=designations.get(toPosition);
                final int fromPosition=mAdapterDesignations.indexOf(designation);
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
        final List<Designation> filteredDesignations=filter(mDesignations,newText);
        mDesignationAdapter.animateTo(filteredDesignations);
//        mRecyclerView.scrollTo(0,0);

        //this one is buggy glitchy
//        mRecyclerView.scrollToPosition(0);
        return true;
    }

    private List<Designation> filter(List<Designation> designationList, String newText)
    {
        newText=newText.toLowerCase();

        final List<Designation> filteredDesignation=new ArrayList<>();

        for (Designation designation:designationList)
        {
            final String text=designation.getTitle().toLowerCase();
            if (text.contains(newText))
            {
                filteredDesignation.add(designation);
            }
        }
        return filteredDesignation;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        MenuItem item=menu.findItem(R.id.action_search);
        SearchView searchView=(SearchView) item.getActionView();
        searchView.setOnQueryTextListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        update();
    }

    @Override
    public void update() {
        if (mDesignationAdapter!=null)
        {
            List<Designation> designations=new ArrayList<>(DesignationLab.getInstanceOf(getActivity()).getDesignations());
            mDesignations=designations;
            mDesignationAdapter.animateTo(designations);
            mDesignationAdapter.notifyDataSetChanged();
        }
    }

    public static Fragment newInstance()
    {
        return new DesignationFragment();
    }
}
