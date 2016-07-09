package com.codedleaf.sylveryte.myemployeeattendanceregister;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by sylveryte on 12/6/16.
 */


public abstract class SingleFragmentActivity extends AppCompatActivity {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_addition);
            FragmentManager fm = getSupportFragmentManager();
            Fragment fragment = fm.findFragmentById(R.id.addition_fragment_container);

            if (fragment == null) {
                fragment = createFragment();
                fm.beginTransaction()
                        .add(R.id.addition_fragment_container, fragment)
                        .commit();
            }
        }
    

        protected abstract Fragment createFragment();

}

