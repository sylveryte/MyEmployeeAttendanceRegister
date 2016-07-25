package com.codedleaf.sylveryte.myemployeeattendanceregister;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;
import java.util.UUID;

/**
 * Created by sylveryte on 21/7/16.
 */
public class SimpleListDialogFragment extends DialogFragment {

    private static final String CALLER_CODE = "callerofshow";

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view= LayoutInflater.from(getActivity()).inflate(R.layout.recycler_fragment,null,false);

        final String caller=getArguments().getString(CALLER_CODE);
        List<Pickable> pickableList = PickCache.getInstance().getPickables(caller);

        if(pickableList.isEmpty())
        {
            view= LayoutInflater.from(getActivity()).inflate(R.layout.nothing_to_show,null,false);

        }else
        {
            RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.fragment_recycler_view);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

            PickAdapter pickAdapter = new PickAdapter(pickableList);
            recyclerView.setAdapter(pickAdapter);
        }

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
            View view=inflater.inflate(R.layout.simple_list_card,parent,false);
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



    public static SimpleListDialogFragment getInstance(@NonNull String callerId, @Nullable List<Pickable> showThese)
    {
        SimpleListDialogFragment fragment=new SimpleListDialogFragment();

        Bundle bundle=new Bundle(1);
        bundle.putString(CALLER_CODE,callerId);
        fragment.setArguments(bundle);

        PickCache.getInstance().storePickables(callerId,showThese);
        return fragment;
    }
}
