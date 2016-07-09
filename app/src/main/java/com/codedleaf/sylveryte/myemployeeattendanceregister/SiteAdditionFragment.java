package com.codedleaf.sylveryte.myemployeeattendanceregister;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by sylveryte on 12/6/16.
 */
public class SiteAdditionFragment extends Fragment {


    private EditText mEditText_siteName;
    private EditText mEditText_description;
    private Button mAddButton;
    private Site mSite;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActivity().setTitle("Edit Site");

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.site_addition_fragment,container,false);

        mEditText_siteName=(EditText)v.findViewById(R.id.editText_site_name);
        mEditText_description=(EditText)v.findViewById(R.id.editText_site_description);


        mAddButton = (Button)v.findViewById(R.id.button_add_site);
        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                saveUpdateData();
                getActivity().finish();

            }
        });


        update();
        return v;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mSite.getTitle()==null)
            deleteMe();
        else if (mSite.getTitle().trim().isEmpty())
            deleteMe();
    }

    private void deleteMe()
    {

        /*       AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Dlete this Site?");
        builder.setMessage("Looks like you didn't enter site title. Should we delete this site?");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                // Do nothing but close the dialog


                dialog.dismiss();
            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                // Do nothing
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();*/

        SitesLab.getInstanceOf(getActivity()).deleteSite(mSite);


    }


    public void saveUpdateData()
    {
        mSite.setTitle(mEditText_siteName.getText().toString());
        mSite.setDescription(mEditText_description.getText().toString());

        SitesLab.getInstanceOf(getActivity()).updateSite(mSite);
    }

    public void update()
    {
        mEditText_siteName.setText(mSite.getTitle());
        mEditText_description.setText(mSite.getDescription());
    }

    public void setSite(Site site) {
        mSite = site;

    }

    public static SiteAdditionFragment createInstance(Site site)
    {
        SiteAdditionFragment siteAdditionFragment=new SiteAdditionFragment();
        siteAdditionFragment.setSite(site);
        return siteAdditionFragment;
    }
}
