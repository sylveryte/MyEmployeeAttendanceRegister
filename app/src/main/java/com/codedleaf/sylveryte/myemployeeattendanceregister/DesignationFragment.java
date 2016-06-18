package com.codedleaf.sylveryte.myemployeeattendanceregister;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

        mLab=DesignationLab.getInstanceOf();
        mLab.addListener(this);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View view=inflater.inflate(R.layout.recycler_fragment,container,false);

        mRecyclerView =(RecyclerView)view.findViewById(R.id.fragment_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mDesignationAdapter=new DesignationAdapter(mLab.getDesignations());
        mRecyclerView.setAdapter(mDesignationAdapter);

        return view;
    }

    private class DesignationHolder extends RecyclerView.ViewHolder
    {


        private TextView title;
        private TextView description;
        private Designation mDesignation;

        public DesignationHolder(final View itemView) {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=AdditionActivity.fetchIntent(getActivity(),AdditionActivity.FRAGMENT_CODE_EDIT_DESIGNATION);
                    intent.putExtra(AdditionActivity.FRAGMENT_STRING_EDIT_DESIGNATION,mDesignation.getId());
                    startActivity(intent);
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
        mDesignationAdapter.notifyDataSetChanged();
    }

    public static Fragment newInstance()
    {
        return new DesignationFragment();
    }
}
