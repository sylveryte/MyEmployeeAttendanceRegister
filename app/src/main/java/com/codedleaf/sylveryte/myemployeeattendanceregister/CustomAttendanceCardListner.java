package com.codedleaf.sylveryte.myemployeeattendanceregister;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

/**
 * Created by sylveryte on 29/7/16.
 * <p/>
 * Copyright (C) 2016 sylveryte@codedleaf <codedlaf@gmail.com>
 * <p/>
 * This file is part of My Employee Attendance Register.
 */
public class CustomAttendanceCardListner implements View.OnClickListener {

    private Entry mEntry;

    private CardView mGolaCard;

    private Context mContext;

    public CustomAttendanceCardListner(Context context, Entry entry, CardView cardView)
    {
        mEntry=entry;
        mContext=context;
        mGolaCard=cardView;
    }

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


        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Set status");
        builder.setItems(choices, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                setColorOfCardAndState(which);

                if (mEntry.isNew())
                {
                    EntryLab.getInstanceOf(mContext).addEntryToDatabase(mEntry);
                    mEntry.setNew(false);
                }
                updateEntry();
            }
        });
        builder.show();
    }

    public void setColorOfCardAndState(int which)
    {
        switch (which)
        {
            case 0: mEntry.setRemark(Entry.PRESENT);
                break;
            case 1: mEntry.setRemark(Entry.LATE);
                break;
            case 2: mEntry.setRemark(Entry.HALF_TIME);
                break;
            case 3: mEntry.setRemark(Entry.OVER_TIME);
                break;
            case 4: mEntry.setRemark(Entry.ABSENT);
                break;
            case 5: mEntry.setRemark(Entry.NOTSPECIFIED);
                break;
            case 6:
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("Enter Note");

                // Set up the input
                final EditText input = new EditText(mContext);
                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text

                LinearLayout layout=new LinearLayout(mContext);
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
        setColor(mEntry,mContext,mGolaCard);
    }

    public void updateEntry()
    {
        EntryLab.getInstanceOf(mContext).updateEntry(mEntry);
    }

    public static void setColor(Entry entry,Context context,CardView cardView)
    {
        switch (entry.getRemark()) {
            case Entry.PRESENT:
                cardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.attendancePresent));
                break;
            case Entry.LATE:
                cardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.attendanceLate));
                break;
            case Entry.HALF_TIME:
                cardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.attendanceHalfTime));
                break;
            case Entry.OVER_TIME:
                cardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.attendanceOvertime));
                break;
            case Entry.ABSENT:
                cardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.attendanceAbsent));
                break;
        }
        if (entry.isNew())
        {
            cardView.setCardBackgroundColor(ContextCompat.getColor(context,R.color.grey_600));

        }
    }
}
