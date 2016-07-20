package com.codedleaf.sylveryte.myemployeeattendanceregister;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by sylveryte on 20/7/16.
 */
public class PickDialogFragment extends DialogFragment {

    //these are to get from args
    private static final String REQUEST_CODE="pickablescode";
    private static final String CALLER_CODE="callercodebro";

    private static final int OBSERVER_CODE=2112;


    public static final int SITE=91;
    public static final int DESIGNATION=92;
    public static final int EMPLOYEE=93;

    private List<? extends Pickable> mPickableList;
    private List<UUID> mPicked;
    private RecyclerView mRecyclerView;
    private PickAdapter mPickAdapter;

    private ImageButton mDone;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view=LayoutInflater.from(getActivity()).inflate(R.layout.pick_fragment,null,false);

        String caller=getArguments().getString(CALLER_CODE);
        mPicked=PickCache.getInstance().getPickables(caller);

        mDone=(ImageButton)view.findViewById(R.id.pick_done);
        mDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogObserver callback = (DialogObserver) getTargetFragment();
                callback.doSomeUpadate();

                getDialog().dismiss();
            }
        });

        mRecyclerView =(RecyclerView)view.findViewById(R.id.pick_fragment_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        switch (getArguments().getInt(REQUEST_CODE))
        {
            case SITE:
            {
                mPickableList=SitesLab.getInstanceOf(getActivity()).getSites();
                break;
            }
            case DESIGNATION:
            {
                mPickableList=DesignationLab.getInstanceOf(getActivity()).getDesignations();
                break;
            }
            case EMPLOYEE:
            {
                mPickableList=EmployeeLab.getInstanceOf(getActivity()).getEmployees();
                break;
            }
        }


        mPickAdapter=new PickAdapter(mPickableList);
        mRecyclerView.setAdapter(mPickAdapter);

        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .create();
    }

    private class PickAdapter extends RecyclerView.Adapter<PickHolder>
    {
        private List<? extends Pickable> mPickables;
        public PickAdapter(List<? extends Pickable> pickableList)
        {
            mPickables=pickableList;
        }

        @Override
        public PickHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater=getActivity().getLayoutInflater();
            View view=inflater.inflate(R.layout.pick_card,parent,false);
            return new PickHolder(view);
        }

        @Override
        public void onBindViewHolder(PickHolder holder, int position) {
            holder.bind(mPickables.get(position));
        }

        @Override
        public int getItemCount() {
            return mPickables.size();
        }
    }

    private class PickHolder extends RecyclerView.ViewHolder
    {
        private TextView title;
        private TextView description;
        private CheckBox mCheckBox;
        private Pickable mPickable;

        public PickHolder(View itemView) {
            super(itemView);

            //PICKING done here
            title=(TextView)itemView.findViewById(R.id.pick_card_title);
            description=(TextView)itemView.findViewById(R.id.pick_card_description);
            mCheckBox=(CheckBox)itemView.findViewById(R.id.checkBox);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mCheckBox.isChecked())
                    {
                        mPicked.remove(mPickable.getId());
                        mCheckBox.setChecked(false);
                    }else
                    {
                        mPicked.add(mPickable.getId());
                        mCheckBox.setChecked(true);
                    }
                }
            });

        }

        public void bind(Pickable pickable)
        {
            mPickable=pickable;
            title.setText(pickable.getTitle());
            description.setText(pickable.getDescription());

            if (mPicked.contains(mPickable.getId()))
            {
                mCheckBox.setChecked(true);
            }

        }
    }

    public PickDialogFragment getInstance(@NonNull String caller, @NonNull int codeFromThisFragmentOnly,Fragment observer)
    {
        PickDialogFragment fragment=new PickDialogFragment();

        Bundle bundle=new Bundle(2);
        bundle.putInt(REQUEST_CODE,codeFromThisFragmentOnly);
        bundle.putString(CALLER_CODE,caller);

        fragment.setTargetFragment(observer,OBSERVER_CODE);
        fragment.setArguments(bundle);

        return fragment;
    }
}
