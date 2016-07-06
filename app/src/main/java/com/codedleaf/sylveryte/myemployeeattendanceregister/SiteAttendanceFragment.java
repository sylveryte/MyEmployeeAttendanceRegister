package com.codedleaf.sylveryte.myemployeeattendanceregister;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
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
import java.util.UUID;

/**
 * Created by sylveryte on 27/6/16.
 */
public class SiteAttendanceFragment extends Fragment {

    private Site mSite;
    private EntrySet mEntrySet;
    private RecyclerView mRecyclerView;
    private EmployeeAttendanceAdapter mEmployeeAttendanceAdpater;
    private Context mContext;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.recycler_fragment_attendance, container, false);

        UUID siteId=mSite.getId();
        LocalDate date=new LocalDate();

        mEntrySet=EntryLab.getInstanceOf(mContext).getEntrySetBySiteIdAndDate(date,siteId);
        mSite=SitesLab.getInstanceOf(mContext).getSiteById(siteId);

        mEmployeeAttendanceAdpater=new EmployeeAttendanceAdapter(mEntrySet.getEntries());
        mRecyclerView = (RecyclerView) view.findViewById(R.id.fragment_recycler_view_attendance);


//        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL,false));
        //for automatic
        //// TODO: 22/6/16  looks suspicious
        DisplayMetrics metrics=new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int cardWidth=(int)metrics.xdpi;
        int spans=(int)Math.floor(mRecyclerView.getContext().getResources().getDisplayMetrics().widthPixels/(float)cardWidth);
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(spans,StaggeredGridLayoutManager.VERTICAL));

        mRecyclerView.setAdapter(mEmployeeAttendanceAdpater);

        return view;
    }

    private class EmployeeAttendanceHolder extends RecyclerView.ViewHolder
    {
        private Entry mEntry;
        private TextView mTextViewName;
        private TextView mTextViewRemark;
        private CardView mCardView;

        public EmployeeAttendanceHolder(View itemView) {
            super(itemView);
            mTextViewName=(TextView)itemView.findViewById(R.id.employee_attendance_name);
            mTextViewRemark=(TextView)itemView.findViewById(R.id.employee_attendance_remark);
            mCardView=(CardView)itemView.findViewById(R.id.employee_attendance_card);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CharSequence choices[] = new CharSequence[] {"Present", "Late", "Halftime", "Overtime","Absent"};

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Set status");
                    builder.setItems(choices, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            setColorOfCardAndState(which);
                        }
                    });
                    builder.show();
                }
            });
        }

        public void setColorOfCardAndState(int which)
        {
            switch (which)
            {
                case 0: mEntry.setRemark(Entry.PRESENT);
                    mCardView.setCardBackgroundColor(ContextCompat.getColor(getActivity(),R.color.attendancePresent));
                    break;
                case 1: mEntry.setRemark(Entry.LATE);
                    mCardView.setCardBackgroundColor(ContextCompat.getColor(getActivity(),R.color.attendanceLate));
                    break;
                case 2: mEntry.setRemark(Entry.HALF_TIME);
                    mCardView.setCardBackgroundColor(ContextCompat.getColor(getActivity(),R.color.attendanceHalfTime));
                    break;
                case 3: mEntry.setRemark(Entry.OVER_TIME);
                    mCardView.setCardBackgroundColor(ContextCompat.getColor(getActivity(),R.color.attendanceOvertime));break;
                case 4: mEntry.setRemark(Entry.ABSENT);
                    mCardView.setCardBackgroundColor(ContextCompat.getColor(getActivity(),R.color.attendanceAbsent));
                    break;
            }
            setRemark();
        }

        private void setRemark()
        {
            mTextViewRemark.setText(mEntry.getRemarkString());
        }

        private void bind(Entry entry)
        {
            mEntry=entry;
            mTextViewName.setText(entry.getEmployeeInfo(mContext));
            setColorOfCardAndState(mEntry.getRemark()%10);
            setRemark();
        }
    }


    private class EmployeeAttendanceAdapter extends RecyclerView.Adapter<EmployeeAttendanceHolder>
    {
        List<Entry> mEntries;

        public EmployeeAttendanceAdapter(List<Entry> entries)
        {
            mEntries=entries;
        }

        @Override
        public EmployeeAttendanceHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater=getActivity().getLayoutInflater();
            View view=layoutInflater.inflate(R.layout.employee_attendance_layout,parent,false);

            EmployeeAttendanceHolder holder=new EmployeeAttendanceHolder(view);

            return holder;
        }

        @Override
        public void onBindViewHolder(EmployeeAttendanceHolder holder, int position) {
            holder.bind(mEntries.get(position));
        }

        @Override
        public int getItemCount() {
            return mEntries.size();
        }
    }



    public void setSite(Site site) {
        mSite = site;
    }


    public static SiteAttendanceFragment createInstance(Site site)
    {
        SiteAttendanceFragment siteAttendanceFragment=new SiteAttendanceFragment();
        siteAttendanceFragment.setSite(site);
        return siteAttendanceFragment;
    }
}
