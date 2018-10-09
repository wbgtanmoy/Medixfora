package com.brandhype.medixfora.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.brandhype.medixfora.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactUsFragment extends Fragment {


    TextView contact_us,about_us_title;

    Context context;

    String contactus="Phone : +91-124-4119596\n" +
            "Fax :      +91-124-4105799\n" +
            "E-mail : info@medixfora.com\n" +
            "              medixfora@gmail.com";

    public ContactUsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View rootView =inflater.inflate(R.layout.fragment_contact_us, container, false);
       context=getActivity();

        contact_us = (TextView) rootView.findViewById(R.id.contact_us);
        contact_us.setText(contactus);
        //contact_us = (TextView) rootView.findViewById(R.id.contact_us);

       return rootView;
    }


    @Override
    public void onResume() {
        super.onResume();
        // set the toolbar title
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.contactus);
    }

}
