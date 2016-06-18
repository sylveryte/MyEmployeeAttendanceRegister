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
import java.util.UUID;

/**
 * Created by sylveryte on 17/6/16.
 */
public class PickFragment extends Fragment {

    private List<? extends Pickable> mPickableList;
    private RecyclerView mRecyclerView;
    private PickAdapter mPickAdapter;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.recycler_fragment,container,false);

        mRecyclerView =(RecyclerView)view.findViewById(R.id.fragment_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mPickAdapter=new PickAdapter(mPickableList);
        mRecyclerView.setAdapter(mPickAdapter);

        return view;
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
        private Pickable mPickable;

        public PickHolder(View itemView) {
            super(itemView);

            //PICKING done here

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setResultId(mPickable.getId());
                    getActivity().finish();
                }
            });

            title=(TextView)itemView.findViewById(R.id.pick_card_title);
            description=(TextView)itemView.findViewById(R.id.pick_card_description);
        }

        public void bind(Pickable pickable)
        {
            mPickable=pickable;
            title.setText(pickable.getTitle());
            description.setText(pickable.getDescription());
        }

    }

    public void setPickableList(List<? extends Pickable> pickableList) {
        mPickableList = pickableList;
    }


    private void setResultId(UUID uuid)
    {
        Intent data=new Intent();
        data.putExtra(PickActivity.RESULT_DATA_STRING_PICK_GENRAL,uuid);
        getActivity().setResult(PickActivity.RESULT_CODE_PICK_GENRAL,data);
    }

    public static Fragment newInstance(List<? extends Pickable> pickables)
    {
            PickFragment pickFragment=new PickFragment();
            pickFragment.setPickableList(pickables);
            return pickFragment;
    }

}
