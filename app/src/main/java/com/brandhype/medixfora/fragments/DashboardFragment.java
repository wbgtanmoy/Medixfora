package com.brandhype.medixfora.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.brandhype.medixfora.MainActivity;
import com.brandhype.medixfora.R;
import com.brandhype.medixfora.Utils.commonutils;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;

import java.util.HashMap;


/**
 * Created by Tanmoy on 29/07/15.
 */
public class DashboardFragment extends Fragment implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener{


    ImageView dash_row1_img_2,dash_row1_img_l,dash_row2_img_l,dash_row2_img_2,dash_row3_img_l,dash_row3_img_2;
    Context context;

    SliderLayout sliderLayout;
    HashMap<String,String> Hash_file_maps ;

    Button chat_button;

    public DashboardFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_dashboard, container, false);

        context=getActivity();

        //-------------image slider--------------------

        Hash_file_maps = new HashMap<String, String>();

        sliderLayout = (SliderLayout) rootView.findViewById(R.id.slider);

        Hash_file_maps.put("Seek Second", "file:///android_asset/images/seeksec.png");
        Hash_file_maps.put("Pharmacy", "file:///android_asset/images/pahrmacy.png");
        Hash_file_maps.put("health tips", "file:///android_asset/images/healthtips.png");
        Hash_file_maps.put("Medical tour", "file:///android_asset/images/medicaltour.png");
        Hash_file_maps.put("Care at home", "file:///android_asset/images/careathome.png");
        //Hash_file_maps.put("Android Froyo", "http://androidblog.esy.es/images/froyo-4.png");
        //Hash_file_maps.put("Android GingerBread", "http://androidblog.esy.es/images/gingerbread-5.png");

        for(String name : Hash_file_maps.keySet()){

            //TextSliderView textSliderView = new TextSliderView(MainActivity.this);
            DefaultSliderView textSliderView = new DefaultSliderView (context);
            textSliderView
                    .description(name)
                    .image(Hash_file_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(this);
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra",name);
            sliderLayout.addSlider(textSliderView);
        }
        sliderLayout.setPresetTransformer(SliderLayout.Transformer.Default);
        sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        sliderLayout.setCustomAnimation(new DescriptionAnimation());
        sliderLayout.setDuration(3000);
        sliderLayout.addOnPageChangeListener(this);
        sliderLayout.setIndicatorVisibility(PagerIndicator.IndicatorVisibility.Invisible);
        //----------------------------------------------

        //seeksecondopinion
        dash_row1_img_2 = (ImageView) rootView.findViewById(R.id.dash_row1_img_2);
        dash_row1_img_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new SeeksecondopinionFragment();
                if (fragment != null) {
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.container_body, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();

                    // set the toolbar title
                    ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(R.string.seeksecondopinion);
                    //((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(Html.fromHtml("<small>Seek Second Opinion</small>"));
                }
            }
        });

        //pharmacy
        dash_row1_img_l = (ImageView) rootView.findViewById(R.id.dash_row1_img_l);
        dash_row1_img_l.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment fragment = new PharmacyListFragment();

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
                    ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(R.string.pharmacy);
                    //((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(Html.fromHtml("<small>Pharmacy</small>"));
                }
                /*Fragment fragment = new PharmacyFragment();
                if (fragment != null) {
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.container_body, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();

                    // set the toolbar title
                    ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(R.string.pharmacy);
                }*/
            }
        });

        //diagononistic service
        dash_row2_img_l = (ImageView) rootView.findViewById(R.id.dash_row2_img_l);
        dash_row2_img_l.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new DiagonisticFragment();
                if (fragment != null) {
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.container_body, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();

                    // set the toolbar title
                    ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(R.string.diagonistic);
                    //((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(Html.fromHtml("<small>Lab Testing</small>"));
                }
            }
        });

        //care at home
        dash_row2_img_2 = (ImageView) rootView.findViewById(R.id.dash_row2_img_2);
        dash_row2_img_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Fragment fragment = new CareathomeFragment();
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
                    //((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(Html.fromHtml("<small>Care At Home</small>"));
                }
            }
        });



        //-medical tourism
        dash_row3_img_2 = (ImageView) rootView.findViewById(R.id.dash_row3_img_2);
        dash_row3_img_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new MedicaltourismFragment();
                if (fragment != null) {
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.container_body, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                     // set the toolbar title
                    ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(R.string.title_medicaltourism);
                    //((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(Html.fromHtml("<small>Care At Home</small>"));
                }
            }
        });


        //-queries -health tip
        dash_row3_img_l = (ImageView) rootView.findViewById(R.id.dash_row3_img_l);
        dash_row3_img_l.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new HealthtipsFragment();
                if (fragment != null) {
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.container_body, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    // set the toolbar title
                    ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(R.string.title_healthtips);
                    //((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(Html.fromHtml("<small>Care At Home</small>"));
                }
            }
        });


        // ----------Ask Me Anything -------
        chat_button = (Button) rootView.findViewById(R.id.chat_button);
        chat_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getContext(),"Under development",Toast.LENGTH_SHORT).show();
                commonutils.setFragmentPatient((MainActivity)context,new AskMeAnyFragment(),null,R.string.title_askme,true);

                //--chat functionality -- to test latter ---
                //Intent main_intent = new Intent( context, ChatActivity.class);
                //startActivity(main_intent);
            }
        });

        return rootView;
    }


    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(R.string.dashboard);
        //((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(Html.fromHtml("<small>Dashboard</small>"));
        //----gets cartcount , method of activity
        ((MainActivity)context).getCartCountFromServer();
        ((MainActivity)context).getNotificationCountFromServer();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {

        Toast.makeText(context,slider.getBundle().get("extra") + "",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

    @Override
    public void onPageSelected(int position) {

        Log.d("Slider Demo", "Page Changed: " + position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {}
}
