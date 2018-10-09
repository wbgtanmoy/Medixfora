package com.brandhype.medixfora.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.brandhype.medixfora.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MedicaltourismFragment extends Fragment {

    Button medicaltour_doctor_btn,medicaltour_hospital_btn,medicaltour_treatment_btn;

    public MedicaltourismFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_medicaltourism, container, false);
        View rootView = inflater.inflate(R.layout.fragment_medicaltourism, container, false);

        medicaltour_doctor_btn = (Button) rootView.findViewById(R.id.medicaltour_doctor_btn);
        medicaltour_hospital_btn = (Button) rootView.findViewById(R.id.medicaltour_hospital_btn);
        medicaltour_treatment_btn = (Button) rootView.findViewById(R.id.medicaltour_treatment_btn);

        medicaltour_hospital_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment fragment = new SpecialityFragment();

                Bundle bundle=new Bundle();
                bundle.putString("listType", "hospital");
                fragment.setArguments(bundle);

                if (fragment != null) {
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.container_body, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();

                    // set the toolbar title
                    ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(R.string.speciality);
                    //((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(Html.fromHtml("<small>Speciality</small>"));
                }

            }
        });

        medicaltour_treatment_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment fragment = new SpecialityFragment();

                Bundle bundle=new Bundle();
                bundle.putString("listType", "speciality");
                fragment.setArguments(bundle);

                if (fragment != null) {
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.container_body, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();

                    // set the toolbar title
                    ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(R.string.speciality);
                    //((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(Html.fromHtml("<small>Speciality</small>"));
                }

            }
        });

        medicaltour_doctor_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment fragment = new DoctorListFragment();
                Bundle bundle=new Bundle();
                bundle.putString("speciality_id", "");
                bundle.putString("speciality_name", "");
                bundle.putString("stype", "doctor");

                fragment.setArguments(bundle);

                if (fragment != null) {
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.container_body, fragment);
                    //fragmentTransaction.add(R.id.container_body, fragment);
                    fragmentTransaction.addToBackStack(null);

                    fragmentTransaction.commit();

                    // set the toolbar title
                    ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Doctors");
                }

            }
        });

        return rootView;
    }

}
