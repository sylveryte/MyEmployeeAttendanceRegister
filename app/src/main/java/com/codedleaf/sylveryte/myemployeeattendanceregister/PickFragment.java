package com.codedleaf.sylveryte.myemployeeattendanceregister;

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
public class PickFragment extends Fragment {

    private List<Pickable> mPickableList;
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

        return view;
    }

    private class PickAdapter extends RecyclerView.Adapter<PickHolder>
    {
        private List<Pickable> mPickables;
        public PickAdapter(List<Pickable> pickableList)
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

        public PickHolder(View itemView) {
            super(itemView);
            title=(TextView)itemView.findViewById(R.id.pick_card_title);
            description=(TextView)itemView.findViewById(R.id.pick_card_description);
        }

        public void bind(Pickable pickable)
        {
            title.setText(pickable.getTitle());
            description.setText(pickable.getDescription());
        }

    }

    public void setPickableList(List<Pickable> pickableList) {
        mPickableList = pickableList;
    }

    public static Fragment newInstance(List<Pickable> pickables)
    {
            PickFragment pickFragment=new PickFragment();
            pickFragment.setPickableList(pickables);
            return pickFragment;
    }

}
