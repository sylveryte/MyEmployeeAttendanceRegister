package com.codedleaf.sylveryte.myemployeeattendanceregister;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by sylveryte on 11/6/16.
 */
public class Sites_Fragment extends Fragment {


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v=inflater.inflate(R.layout.sites_fragment,container,false);

        //// TODO: 12/6/16 clean all this too :)
        TextView siteTitle=(TextView)v.findViewById(R.id.textView_site_title);

        SitesLab sitesLab;
        sitesLab = SitesLab.getInstanceOf();

        siteTitle.setText(sitesLab.getASite().getTitle());

        return v;
    }

    public static Fragment newInstance()
    {
        return new Sites_Fragment();
    }
}
