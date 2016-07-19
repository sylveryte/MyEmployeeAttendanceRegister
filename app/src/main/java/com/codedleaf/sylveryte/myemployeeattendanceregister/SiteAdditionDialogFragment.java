package com.codedleaf.sylveryte.myemployeeattendanceregister;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by sylveryte on 19/7/16.
 */
public class SiteAdditionDialogFragment extends DialogFragment {

    private EditText mEditText_siteName;
    private EditText mEditText_description;
    private Button mAddButton;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        View view= LayoutInflater.from(getActivity())
                .inflate(R.layout.site_addition_fragment,null,false);

        mEditText_siteName=(EditText)view.findViewById(R.id.editText_site_name);
        mEditText_description=(EditText)view.findViewById(R.id.editText_site_description);


        mAddButton = (Button)view.findViewById(R.id.button_add_site);
        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addSite(new Site());
                getDialog().dismiss();
            }
        });


        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .create();
    }


    public void addSite(Site site)
    {
        site.setTitle(mEditText_siteName.getText().toString());
        site.setDescription(mEditText_description.getText().toString());

        SitesLab.getInstanceOf(getActivity()).addSite(site);
    }
}
