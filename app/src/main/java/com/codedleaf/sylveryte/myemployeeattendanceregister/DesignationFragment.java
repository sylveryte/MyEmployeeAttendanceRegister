package com.codedleaf.sylveryte.myemployeeattendanceregister;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by sylveryte on 17/6/16.
 */
public class DesignationFragment extends Fragment implements LabObserver {


    private RecyclerView mRecyclerView;
    private DesignationLab mLab;
    private DesignationAdapter mDesignationAdapter;

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

       /* //for automatic
        //// TODO: 22/6/16  looks suspicious
        DisplayMetrics metrics=new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int cardWidth=(int)metrics.xdpi;
        int spans=(int)Math.floor(mRecyclerView.getContext().getResources().getDisplayMetrics().widthPixels/(float)cardWidth);*/


        //keeping two spans only
//        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));


        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));

        mDesignationAdapter=new DesignationAdapter(mLab.getDesignations());
        mRecyclerView.setAdapter(mDesignationAdapter);

        return view;
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
                                            ,EmployeeLab.getInstanceOf(getActivity()).getPickables(mDesignation.getEmployees()))
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

        private List<Designation> mDesignations;

        public DesignationAdapter(List<Designation> designationList)
        {
            mDesignations=designationList;
        }

        @Override
        public DesignationHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            LayoutInflater inflater=getActivity().getLayoutInflater();
            View view=inflater.inflate(R.layout.designation_card,parent,false);

            return new DesignationHolder(view);
        }

        @Override
        public void onBindViewHolder(DesignationHolder holder, int position) {

            holder.bind(mDesignations.get(position));

        }

        @Override
        public int getItemCount() {
            return mDesignations.size();
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        update();
    }

    @Override
    public void update() {
        if (mDesignationAdapter!=null)
        mDesignationAdapter.notifyDataSetChanged();
    }

    public static Fragment newInstance()
    {
        return new DesignationFragment();
    }
}
