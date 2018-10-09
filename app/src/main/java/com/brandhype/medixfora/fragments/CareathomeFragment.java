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
public class CareathomeFragment extends Fragment {

    private ImageView careathome_row2_img_l,careathome_row2_img_2;


    public CareathomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_careathome, container, false);
        View rootView = inflater.inflate(R.layout.fragment_careathome, container, false);


        careathome_row2_img_l = (ImageView) rootView.findViewById(R.id.careathome_row2_img_l);
        careathome_row2_img_l.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment fragment = new CareathomeListFragment();

                Bundle bundle=new Bundle();
                bundle.putString("listType", "category");
                fragment.setArguments(bundle);


                if (fragment != null) {
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.container_body, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();

                    // set the toolbar title
                    ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(R.string.careathome);
                }
            }
        });


        careathome_row2_img_2 = (ImageView) rootView.findViewById(R.id.careathome_row2_img_2);
        careathome_row2_img_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment fragment = new CareathomeListFragment();

                Bundle bundle=new Bundle();
                bundle.putString("listType", "company");
                fragment.setArguments(bundle);


                if (fragment != null) {
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.container_body, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();

                    // set the toolbar title
                    ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(R.string.careathome);
                }
            }
        });


        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(R.string.careathome);
    }

}
