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
    private static final String CALLER_CODE="callercodebro";

    private String callerid;

    private List<PickingChavaClass> chavaList;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view=LayoutInflater.from(getActivity()).inflate(R.layout.pick_fragment,null,false);

        //get datas
        callerid=getArguments().getString(CALLER_CODE);
        List<? extends Pickable> pickFrom=PickCache.getInstance().getPickables(callerid);
        List<UUID> picked=PickCache.getInstance().getPicked(callerid);


        if (pickFrom.isEmpty())
        {
            view=LayoutInflater.from(getActivity()).inflate(R.layout.nothing_to_show,null,false);
        }else
        {
            chavaList = new ArrayList<>();
            for (Pickable pickable:pickFrom)
            {
                //herer
                if (picked.contains(pickable.getId()))
                    chavaList.add(new PickingChavaClass(true,pickable));
                else
                    chavaList.add(new PickingChavaClass(false,pickable));
            }

            ImageButton done = (ImageButton) view.findViewById(R.id.pick_done);
            done.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    List<UUID> picked=new ArrayList<>();
                    List<UUID> removed=new ArrayList<>();

                    for (PickingChavaClass chavaClass:chavaList)
                    {
                        if (chavaClass.isChecked())
                            picked.add(chavaClass.getPickable().getId());
                        else
                            removed.add(chavaClass.getPickable().getId());
                    }

                    PickCache.getInstance().storePicked(callerid,picked);
                    PickCache.getInstance().storeRemoved(callerid,removed);
                    PickCache.getInstance().getObserver(callerid).doSomeUpdate(getActivity());

                    getDialog().dismiss();
                }
            });

            RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.pick_fragment_recycler_view);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

            PickAdapter pickAdapter = new PickAdapter(chavaList);
            recyclerView.setAdapter(pickAdapter);
        }

        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .create();
    }

    private class PickAdapter extends RecyclerView.Adapter<PickChavaHolder>
    {
        private List<PickingChavaClass> mChavaClasses;
        public PickAdapter(List<PickingChavaClass> list)
        {
            mChavaClasses=list;
        }

        @Override
        public PickChavaHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater=getActivity().getLayoutInflater();
            View view=inflater.inflate(R.layout.pick_card,parent,false);
            return new PickChavaHolder(view);
        }

        @Override
        public void onBindViewHolder(PickChavaHolder holder, int position) {
            holder.bind(mChavaClasses.get(position));
        }

        @Override
        public int getItemCount() {
            return mChavaClasses.size();
        }
    }

    private class PickChavaHolder extends RecyclerView.ViewHolder
    {
        private TextView title;
        private TextView description;
        private CheckBox mCheckBox;
        private PickingChavaClass mChavaClass;

        public PickChavaHolder(View itemView) {
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
                        mChavaClass.setChecked(false);
                        mCheckBox.setChecked(false);
                    }else
                    {
                        mChavaClass.setChecked(true);
                        mCheckBox.setChecked(true);
                    }
                }
            });

        }

        public void bind(PickingChavaClass chavaClass)
        {
            mChavaClass=chavaClass;
            title.setText(mChavaClass.getPickable().getTitle());
            description.setText(mChavaClass.getPickable().getDescription());

            if (mChavaClass.isChecked())
            {
                mCheckBox.setChecked(true);
            }else mCheckBox.setChecked(false);
        }
    }

    public static PickDialogFragment getInstance(@NonNull String id, DialogPickObserver observer,List<UUID> picked,List<? extends Pickable> pickFrom)
    {
        PickDialogFragment fragment=new PickDialogFragment();

        Bundle bundle=new Bundle(1);
        bundle.putString(CALLER_CODE,id);

        PickCache.getInstance().addObserver(id,observer);
        PickCache.getInstance().storePickables(id,pickFrom);
        PickCache.getInstance().storePicked(id,picked);

        fragment.setArguments(bundle);

        return fragment;
    }
}
