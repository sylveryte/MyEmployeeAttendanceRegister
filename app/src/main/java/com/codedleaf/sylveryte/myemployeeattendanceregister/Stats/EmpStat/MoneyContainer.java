package com.codedleaf.sylveryte.myemployeeattendanceregister.Stats.EmpStat;

import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.codedleaf.sylveryte.myemployeeattendanceregister.Editing.DesignationAdditionDialogFragment;
import com.codedleaf.sylveryte.myemployeeattendanceregister.Editing.MoneyAdditionDialogFragment;
import com.codedleaf.sylveryte.myemployeeattendanceregister.GeneralObserver;
import com.codedleaf.sylveryte.myemployeeattendanceregister.Labs.EmployeeLab;
import com.codedleaf.sylveryte.myemployeeattendanceregister.Labs.MoneyLab;
import com.codedleaf.sylveryte.myemployeeattendanceregister.Models.Money;
import com.codedleaf.sylveryte.myemployeeattendanceregister.Picknation.PickDialogFragment;
import com.codedleaf.sylveryte.myemployeeattendanceregister.Picknation.SimpleListDialogFragment;
import com.codedleaf.sylveryte.myemployeeattendanceregister.R;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by sylveryte on 21/8/16.
 * <p/>
 * Copyright (C) 2016 sylveryte@codedleaf <codedleaf@gmail.com>
 * <p/>
 * This file is part of MyEmployeeAttendanceRegister.
 */

public class MoneyContainer extends FrameLayout implements GeneralObserver {

    private static final String MONEY_ADD_CALLER_ID="yolothisimoneycall";

    private ImageButton mAddButton;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;

    private List<Money> mMoneyList;

    private UUID mEmpID;
    private GeneralObserver thisObserver;

    private FragmentManager mFragmentManager;

    private Context mContext;

    public MoneyContainer(Context context) {
        super(context);
        init(context);
    }

    public MoneyContainer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public MoneyContainer(Context context, AttributeSet attributeSet) {
        super(context,attributeSet);
        init(context);
    }

    private void init(Context context)
    {
        mContext=context;
        LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=inflater.inflate(R.layout.money_view_container,this,false);
        addView(view);
        thisObserver=this;
        mAddButton=(ImageButton)view.findViewById(R.id.add_money);
        mAddButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEmpID!=null)
                    MoneyAdditionDialogFragment.getMoneyFrag(mEmpID,MONEY_ADD_CALLER_ID,thisObserver)
                            .show(mFragmentManager,"SOmeCodeTag");
            }
        });
        mRecyclerView=(RecyclerView)view.findViewById(R.id.recycler_view);
    }

    public void initialize(UUID empId, FragmentManager fragmentManager)
    {
        mFragmentManager=fragmentManager;
        mEmpID=empId;
        mMoneyList= new ArrayList<>();
        mAdapter=new MoneyAdapter();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);

        new MoneyLogFetcher().execute(empId);
    }

//    MoneyLab.getInstanceOf(getContext()).getMoneyLogs(empId,new LocalDate().getYear());
    class MoneyLogFetcher extends AsyncTask<UUID,Void,Void>
{
    @Override
    protected Void doInBackground(UUID... params) {
        List<Money> moneys=MoneyLab.getInstanceOf(mContext).getMoneyLogs(params[0],new LocalDate().getYear());
        for (Money money:moneys)
        {
            mMoneyList.add(0,money);
//            Log.i("Money","added money "+money.getAmountString());
            mAdapter.notifyItemInserted(0);
        }
        return null;
    }
}

    private class MoneyHolder extends RecyclerView.ViewHolder
    {
        private MoneyView mMoneyView;
        private Money mMoney;

        public MoneyHolder(MoneyView itemView) {
            super(itemView);
            mMoneyView=itemView;
            itemView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                    CharSequence choices[] = new CharSequence[] {"Delete"};

                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setItems(choices, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            switch (which)
                            {
                                case 0: {
                                    mMoneyList.remove(mMoney);
                                    mAdapter.notifyDataSetChanged();
                                    MoneyLab.getInstanceOf(getContext()).deleteMoney(mMoney);
                                    break;
                                }
                            }
                        }
                    });
                    builder.show();
                }
            });
        }

        public void bind(Money money)
        {
            mMoneyView.setMoney(money);
            mMoney=money;
        }
    }

    private class MoneyAdapter extends RecyclerView.Adapter<MoneyHolder>
    {
        @Override
        public MoneyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MoneyHolder(new MoneyView(getContext()));
        }

        @Override
        public void onBindViewHolder(MoneyHolder holder, int position) {
            holder.bind(mMoneyList.get(position));
        }

        @Override
        public int getItemCount() {
            return mMoneyList.size();
        }
    }

    @Override
    public void doSomeUpdate(Context context) {
        mMoneyList.add(0,MoneyCache.getMoneyCache().getMoney());
        mAdapter.notifyItemInserted(0);
    }
}
