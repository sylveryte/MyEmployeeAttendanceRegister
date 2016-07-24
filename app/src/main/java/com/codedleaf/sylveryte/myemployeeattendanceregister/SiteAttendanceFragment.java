package com.codedleaf.sylveryte.myemployeeattendanceregister;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * Created by sylveryte on 27/6/16.
 */
public class SiteAttendanceFragment extends Fragment {

    private static final String ARGS_CODE="siteattendanceargs";

    private Site mSite;
    private RecyclerView mRecyclerView;
    private EmployeeAttendanceAdapter mEmployeeAttendanceAdpater;

    HashMap<UUID,List<Entry>> mEntriesMap;
    List<UUID> mEmployees;
    List<LocalDate> mLocalDates;

    private LinearLayout dateContainer;

    private int NoOfDays;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle("Taking Attendance");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.attendance_fragment, container, false);
        dateContainer=(LinearLayout)view.findViewById(R.id.date_container_ll);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.attendance_fragment_recycler_view);

        NoOfDays=calculateNoOfDatesShould();
        mLocalDates=new ArrayList<>();
        LocalDate date=new LocalDate();


        inflateSquareDatesViews(inflater,dateContainer,date);
        inflateEntries();





        //// TODO: 23/7/16 do something about this one

        if (mEmployees!=null)
        {
            mEmployeeAttendanceAdpater = new EmployeeAttendanceAdapter(mEmployees);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
            mRecyclerView.setAdapter(mEmployeeAttendanceAdpater);
        }
        return view;
    }



    private int calculateNoOfDatesShould()
    {
        int count;
        DisplayMetrics matrices=getActivity().getResources().getDisplayMetrics();
        count=(int)(matrices.heightPixels/matrices.density)/(2*75);

        if (count<1)
        {
            count=2;
        }

        return count;
    }

    private void inflateSquareDatesViews(LayoutInflater inflater, LinearLayout dateContainer, LocalDate date)
    {

        DateTimeFormatter fmt = DateTimeFormat.forPattern("d\nE");

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT,1.0f);

        for (int i=0;i< NoOfDays;i++)
        {
            LinearLayout view=(LinearLayout)inflater.inflate(R.layout.attendance_gola_square,null,false);

            view.setGravity(Gravity.CENTER);


            view.setLayoutParams(lp);

            TextView textView=(TextView) view.findViewById(R.id.button_square_date);

            LocalDate localDate=date.minusDays(i);
            mLocalDates.add(localDate);

            textView.setText(fmt.print(localDate));

            dateContainer.addView(view);
        }

        dateContainer.invalidate();
    }

    public void inflateEntries()
    {
        UUID siteId=(UUID)getArguments().getSerializable(ARGS_CODE);
        mSite=SitesLab.getInstanceOf(getActivity()).getSiteById(siteId);

        mEntriesMap=new HashMap<>();
        mEmployees=mSite.getEmployeesInvolved();

        if (mEmployees!=null)
        {
            for (UUID empID:mEmployees)
            {
                mEntriesMap.put(empID,getEntries(empID,siteId));
            }
        }
    }

    public List<Entry> getEntries(UUID empId,UUID siteId)
    {
        List<Entry> toReturnEntries=new ArrayList<>(NoOfDays);
        for (int i=0;i<NoOfDays;i++)
        {
            Entry entry;
            entry=EntryLab.getInstanceOf(getActivity()).getEntry(mLocalDates.get(i),siteId,empId);
            //for new entries

            if (entry==null)
            {
                entry = new Entry(empId,siteId,mLocalDates.get(i));
                entry.setNew(true);
            }

            toReturnEntries.add(entry);
        }
        return toReturnEntries;
    }

    private class EmployeeAttendanceHolder extends RecyclerView.ViewHolder
    {
        private Employee mEmployee;
        private List<Entry> mEntryList;
        private List<GolaView> mGolaViews;

        private LinearLayout mGolaContainer;

        private TextView mTextViewName;

        public EmployeeAttendanceHolder(View itemView) {
            super(itemView);

            mTextViewName=(TextView)itemView.findViewById(R.id.employee_attendance_name);
            mGolaContainer=(LinearLayout)itemView.findViewById(R.id.gola_container);

            mGolaViews=new ArrayList<>();
            for (int i=0;i<NoOfDays;i++)
            {
                GolaView golaView=new GolaView(getActivity(),mLocalDates.get(i));

                mGolaContainer.addView(golaView.getLayoutView());
                mGolaViews.add(golaView);
            }
        }

        private void bind(UUID empId)
        {
            mEmployee=EmployeeLab.getInstanceOf(getActivity()).getEmployeeById(empId);
            mTextViewName.setText(mEmployee.getTitle());
            mEntryList=mEntriesMap.get(empId);
            if (mEntryList==null)
                mEntryList=new ArrayList<>();


            for (int i=0;i<NoOfDays;i++)
            {
                mGolaViews.get(i).setEntry(mEntryList.get(i));
            }

            for (GolaView golaView: mGolaViews)
            {
                golaView.setClicability();
            }
        }

        //gola view class
        private class GolaView extends View
        {
            private Entry mEntry;
            private CardView mGolaCard;
            private LinearLayout mLayoutView;
            private LocalDate mDate;

            private TextView mTextView;

            public GolaView(Context context,LocalDate date)
            {
                super(context);
                mLayoutView=(LinearLayout)LayoutInflater.from(getActivity()).inflate(R.layout.attendance_gola,null,true);
                mLayoutView.setGravity(Gravity.CENTER);
                mGolaCard =(CardView) mLayoutView.findViewById(R.id.attendance_gola_b);
                mTextView=(TextView)mLayoutView.findViewById(R.id.gola_text);
                mDate=date;

                String s=date.getDayOfMonth()+" ";

                mTextView.setText(s);

                mLayoutView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CharSequence choices[] = new CharSequence[] {"Present", "Late", "Halftime", "Overtime","Absent","Not Specified","Edit Note"};

                        if (mEntry.getNote()==null) {
                            choices[6] = "Add Note";
                        }
                        else if (mEntry.getNote().trim().isEmpty())
                        {
                            choices[6] = "Add Note";
                        }


                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle("Set status");
                        builder.setItems(choices, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                setColorOfCardAndState(which);

                                if (mEntry.isNew())
                                {
                                    EntryLab.getInstanceOf(getActivity()).addEntryToDatabase(mEntry);
                                    mEntry.setNew(false);
                                }
                                updateEntry();
                            }
                        });
                        builder.show();
                    }
                });
            }

            public void setEntry(Entry entry)
            {
                if (entry.getDate().equals(mDate))
                mEntry = entry;
                setColorOfCardAndState(mEntry.getRemark()%10);

                if (mEntry.isNew())
                {
                    mGolaCard.setCardBackgroundColor(ContextCompat.getColor(getActivity(),R.color.white));
                }
            }

            public void setClicability()
            {
                if (mEntry==null)
                    mLayoutView.setClickable(false);
                else
                    mLayoutView.setClickable(true);
            }

            public LinearLayout getLayoutView() {

                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT,1.0f);
                mLayoutView.setLayoutParams(lp);
                return mLayoutView;
            }

            public void setColorOfCardAndState(int which)
            {
                switch (which)
                {
                    case 0: mEntry.setRemark(Entry.PRESENT);
                        mGolaCard.setCardBackgroundColor(ContextCompat.getColor(getActivity(),R.color.attendancePresent));
                        break;
                    case 1: mEntry.setRemark(Entry.LATE);
                        mGolaCard.setCardBackgroundColor(ContextCompat.getColor(getActivity(),R.color.attendanceLate));
                        break;
                    case 2: mEntry.setRemark(Entry.HALF_TIME);
                        mGolaCard.setCardBackgroundColor(ContextCompat.getColor(getActivity(),R.color.attendanceHalfTime));
                        break;
                    case 3: mEntry.setRemark(Entry.OVER_TIME);
                        mGolaCard.setCardBackgroundColor(ContextCompat.getColor(getActivity(),R.color.attendanceOvertime));break;
                    case 4: mEntry.setRemark(Entry.ABSENT);
                        mGolaCard.setCardBackgroundColor(ContextCompat.getColor(getActivity(),R.color.attendanceAbsent));
                        break;
                    case 5: mEntry.setRemark(Entry.NOTSPECIFIED);
                        mGolaCard.setCardBackgroundColor(ContextCompat.getColor(getActivity(),R.color.attendanceNotSpecified));
                        break;
                    case 6:
                    {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle("Enter Note");

                        // Set up the input
                        final EditText input = new EditText(getActivity());
                        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text

                        LinearLayout layout=new LinearLayout(getActivity());
                        layout.setPadding(35,15,35,15);
                        layout.addView(input);
                        input.setInputType(InputType.TYPE_CLASS_TEXT);

                        input.setText(mEntry.getNote());

                        builder.setView(layout);

                        // Set up the buttons
                        builder.setPositiveButton("Set", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mEntry.setNote(input.getText().toString());
                                updateEntry();
                            }
                        });
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                        builder.show();
                    }
                }
            }

            public void updateEntry()
            {
                EntryLab.getInstanceOf(getActivity()).updateEntry(mEntry);
            }
        }
    }


    private class EmployeeAttendanceAdapter extends RecyclerView.Adapter<EmployeeAttendanceHolder>
    {
        List<UUID> mEntries;

        public EmployeeAttendanceAdapter(List<UUID> entries)
        {
            mEntries=entries;
        }

        @Override
        public EmployeeAttendanceHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater=getActivity().getLayoutInflater();
            View view=layoutInflater.inflate(R.layout.attendance_layout,parent,false);

            return new EmployeeAttendanceHolder(view);
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

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onPause() {
        super.onPause();
        //// TODO: 23/7/16 dont forget to update
       // EntryLab.getInstanceOf(getActivity()).updateEntries(mEntries);
    }

    public static SiteAttendanceFragment createInstance(Site site)
    {
        SiteAttendanceFragment siteAttendanceFragment=new SiteAttendanceFragment();

        Bundle args=new Bundle();
        args.putSerializable(ARGS_CODE,site.getId());
        siteAttendanceFragment.setArguments(args);

        return siteAttendanceFragment;
    }

    private void recreateMe()
    {

    }

    @Override
    public void onResume() {
        super.onResume();
        recreateMe();
    }
}
