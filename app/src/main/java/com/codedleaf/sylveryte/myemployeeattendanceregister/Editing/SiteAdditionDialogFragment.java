package com.codedleaf.sylveryte.myemployeeattendanceregister.Editing;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.codedleaf.sylveryte.myemployeeattendanceregister.Labs.SitesLab;
import com.codedleaf.sylveryte.myemployeeattendanceregister.Models.Site;
import com.codedleaf.sylveryte.myemployeeattendanceregister.R;

import java.util.UUID;

/**
 * Created by sylveryte on 19/7/16.
 *
 * Copyright (C) 2016 sylveryte@codedleaf <codedlaf@gmail.com>
 *
 * This file is part of My Employee Attendance Register.
 *
 */
public class SiteAdditionDialogFragment extends DialogFragment {

    private static String ARGS_CODE="siteargcode";

    private EditText mEditText_siteName;
    private EditText mEditText_description;

    private Site mSite;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View view= LayoutInflater.from(getActivity())
                .inflate(R.layout.site_addition_fragment,null,false);

        mEditText_siteName=(EditText)view.findViewById(R.id.editText_site_name);
        mEditText_description=(EditText)view.findViewById(R.id.editText_site_description);
        Button addButton = (Button) view.findViewById(R.id.button_add_site);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mEditText_siteName.getText().toString().trim().isEmpty())
                {
                    mEditText_siteName.setError("Please enter site name");
                    return;
                }

                saveDesignation(mSite);
                getDialog().dismiss();
            }
        });

        Bundle args=getArguments();
        //differentiate between edit and add calls
        if (args!=null)
        {
            UUID uuid=(UUID)args.getSerializable(ARGS_CODE);

            mSite= SitesLab.getInstanceOf(getActivity()).getSiteById(uuid);
            updateData();
        }

        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .create();
    }

    private void updateData()
    {
        mEditText_siteName.setText(mSite.getTitle());
        mEditText_description.setText(mSite.getDescriptionPure());
    }



    public void saveDesignation(Site site)
    {
        if (site==null)
        {
            site=new Site();
            SitesLab.getInstanceOf(getActivity()).addSite(site);
        }

        site.setTitle(mEditText_siteName.getText().toString().trim());
        site.setDescription(mEditText_description.getText().toString().trim());

        site.updateMyDB(getActivity());
    }

    public static SiteAdditionDialogFragment getSiteFrag(@Nullable UUID siteId)
    {
        if (siteId==null)
            return new SiteAdditionDialogFragment();
        else
        {
            SiteAdditionDialogFragment fragment=new SiteAdditionDialogFragment();
            Bundle args=new Bundle();
            args.putSerializable(ARGS_CODE,siteId);
            fragment.setArguments(args);
            return fragment;
        }
    }
}
