package com.brandhype.medixfora.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.brandhype.medixfora.R;
import com.brandhype.medixfora.Utils.commonutils;

/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardFragmentDoctor extends Fragment {


    Context context;
    ImageView dash_row1_img_2,dash_row1_img_l, dash_row2_img_l,dash_row2_img_2;

    public DashboardFragmentDoctor() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView=inflater.inflate(R.layout.fragment_dashboard_doctor, container, false);;
        context=getActivity();


        //schedule
        dash_row1_img_l = (ImageView) rootView.findViewById(R.id.doc_dash_row1_img_l);
        dash_row1_img_l.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context,"schedule",Toast.LENGTH_SHORT).show();
                commonutils.setFragment(getActivity(),new DoctorscheduleFragment(),null,R.string.title_schedule,true);
            }
        });

        //appointment
        dash_row1_img_2 = (ImageView) rootView.findViewById(R.id.doc_dash_row1_img_2);
        dash_row1_img_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context,"appointments",Toast.LENGTH_SHORT).show();
                commonutils.setFragment(getActivity(),new DoctorAppointmentFragment(),null,R.string.title_appointment,true);
            }
        });

        //profile
        dash_row2_img_l = (ImageView) rootView.findViewById(R.id.doc_dash_row2_img_l);
        dash_row2_img_l.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context,"profile",Toast.LENGTH_SHORT).show();
                commonutils.setFragment(getActivity(),new DoctorProifileFragment(),null,R.string.title_docprofile,true);
            }
        });

        //settings
        dash_row2_img_2 = (ImageView) rootView.findViewById(R.id.doc_dash_row2_img_2);
        dash_row2_img_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context,"settings",Toast.LENGTH_SHORT).show();
                commonutils.setFragment(getActivity(),new DoctorSettingsFragment(),null,R.string.title_settings,true);
            }
        });

        return rootView;
    }




}
