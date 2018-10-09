package com.brandhype.medixfora.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.brandhype.medixfora.R;
import com.brandhype.medixfora.interfaces.FragmentLifecycle;
import com.brandhype.medixfora.models.DoctorItem;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 */
public class AppointmentFragment extends Fragment {


    Context context;
    String doctor_id,doctor_name,doctor_desig,doctor_address,doctor_city,img;
    DoctorItem doctor;

    ViewPager viewPager;
    PagerAdapter_inner adapter;

    TextView doctor_appt_name,doctor_appt_desig,doctor_appt_address;
    ImageView doctor_image;

    public AppointmentFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context=getActivity();
        try {

            Gson gson = new Gson();
            String strObj = getArguments().getString("doctor");
            DoctorItem obj = gson.fromJson(strObj, DoctorItem.class);

            doctor_id = obj.getId();
            doctor_name=obj.getName();
            doctor_desig=obj.getDesignation();
            doctor_address=obj.getAddress();
            doctor_city=obj.getCity_name();
            img=obj.getImage();

            //doctor_id = getArguments().getString("doctor_id");

            Log.d("@ doctor id : ",doctor_id);
            //Toast.makeText(context,"Doctor id: "+doctor_id + obj.getName(),Toast.LENGTH_SHORT).show();

        }catch (Exception e){e.printStackTrace();}
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_appointment, container, false);

        doctor_appt_name = (TextView) rootView.findViewById(R.id.doctor_appt_name);
        doctor_appt_desig = (TextView) rootView.findViewById(R.id.doctor_appt_desig);
        doctor_appt_address = (TextView) rootView.findViewById(R.id.doctor_appt_address);
        doctor_appt_name.setText(doctor_name);
        doctor_appt_desig.setText(doctor_desig);
        doctor_appt_address.setText(doctor_address+" "+doctor_city);

        doctor_image = (ImageView) rootView.findViewById(R.id.doctor_appt_image);

        try {
            Picasso.with(context)
                    .load(img)
                    .placeholder(R.drawable.profilepic1)
                    .error(R.drawable.default_avatar)
                    .into(doctor_image);
        }catch(Exception e){e.printStackTrace();}


        TabLayout tabLayout = (TabLayout) rootView.findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Appointment"));
        tabLayout.addTab(tabLayout.newTab().setText("Patient Info"));
        tabLayout.addTab(tabLayout.newTab().setText("Finished"));

        tabLayout.setTabTextColors(
                ContextCompat.getColor(context, R.color.tab_unselected_text_color),
                ContextCompat.getColor(context, R.color.tab_selected_text_color)
        );

        viewPager = (ViewPager) rootView.findViewById(R.id.pager);
        //*******--use getChildFragmentManager() instead of getActivity().getSupportFragmentManager()---------*******
        adapter = new PagerAdapter_inner(getActivity().getSupportFragmentManager() , tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager.setOnPageChangeListener(pageChangeListener);

        return rootView;
    }

    private ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {

        int currentPosition = 0;

        @Override
        public void onPageSelected(int newPosition) {

            FragmentLifecycle fragmentToHide = (FragmentLifecycle) adapter.getItem(currentPosition);
            fragmentToHide.onPauseFragment(context);

            FragmentLifecycle fragmentToShow = (FragmentLifecycle) adapter.getItem(newPosition);
            fragmentToShow.onResumeFragment(context);

            currentPosition = newPosition;
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) { }

        public void onPageScrollStateChanged(int arg0) { }
    };




    public class PagerAdapter_inner extends FragmentStatePagerAdapter {
        int mNumOfTabs;

        private final SparseArray<Fragment> mPageReferences = new SparseArray<Fragment>();


        public PagerAdapter_inner(FragmentManager fm, int NumOfTabs) {
            super(fm);
            this.mNumOfTabs = NumOfTabs;
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    ApptFragment tab1 = new ApptFragment();
                    mPageReferences.put(position, tab1);
                    return tab1;
                case 1:
                    PatientinfoFragment tab2 = new PatientinfoFragment();
                    mPageReferences.put(position, tab2);
                    return tab2;
                case 2:
                    FinishedFragment tab3 = new FinishedFragment();
                    mPageReferences.put(position, tab3);
                    return tab3;

                default:
                    return null;
            }
        }

        public void destroyItem(View container, int position, Object object) {
            super.destroyItem(container, position, object);
            mPageReferences.remove(position);
        }

        public Fragment getFragment(int key) {
            return mPageReferences.get(key);
        }
        @Override
        public int getCount() {
            return mNumOfTabs;
        }
    }


    public ViewPager getPager()
    {
        return viewPager;
    }
}
