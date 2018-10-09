package com.brandhype.medixfora.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.brandhype.medixfora.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SeeksecondopinionFragment extends Fragment {


    ImageView seek_row2_img_l,seek_row2_img_2;


    public SeeksecondopinionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_seeksecondopinion, container, false);

        seek_row2_img_l = (ImageView) rootView.findViewById(R.id.seek_row2_img_l);
        seek_row2_img_l.setOnClickListener(new View.OnClickListener() {
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


        seek_row2_img_2 = (ImageView) rootView.findViewById(R.id.seek_row2_img_2);
        seek_row2_img_2.setOnClickListener(new View.OnClickListener() {
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


        return rootView;
    }


    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(R.string.seeksecondopinion);
        //((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(Html.fromHtml("<small>Seek Second Opinion</small>"));
    }


}
