package com.brandhype.medixfora.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.brandhype.medixfora.LoginActivity;
import com.brandhype.medixfora.R;
import com.brandhype.medixfora.adpaters.ImageViewPagerAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class BannerFragment extends Fragment {
    private ViewPager _mViewPager;
    private ImageViewPagerAdapter _adapter;
    private ImageView _btn1, _btn2, _btn3,_btn4,_btn5,_btn6;
    private Button skip;


    public BannerFragment() {
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpView();
        setTab();
        onCircleButtonClick();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_banner, container, false);
    }

    private void onCircleButtonClick() {

        _btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _btn1.setImageResource(R.drawable.fill_circle);
                _mViewPager.setCurrentItem(0);
            }
        });

        _btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _btn2.setImageResource(R.drawable.fill_circle);
                _mViewPager.setCurrentItem(1);
            }
        });
        _btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _btn3.setImageResource(R.drawable.fill_circle);
                _mViewPager.setCurrentItem(2);
            }
        });

        _btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _btn4.setImageResource(R.drawable.fill_circle);
                _mViewPager.setCurrentItem(3);
            }
        });

        _btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _btn4.setImageResource(R.drawable.fill_circle);
                _mViewPager.setCurrentItem(4);
            }
        });
        _btn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _btn4.setImageResource(R.drawable.fill_circle);
                _mViewPager.setCurrentItem(5);
            }
        });


        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent _intent = new Intent(getActivity(), SignupActivity.class);
                Intent _intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(_intent);
                getActivity().finish();

            }
        });
    }

    private void setUpView() {
        _mViewPager = (ViewPager) getView().findViewById(R.id.imageviewPager);
        _adapter = new ImageViewPagerAdapter(getActivity(), getFragmentManager());
        _mViewPager.setAdapter(_adapter);
        _mViewPager.setCurrentItem(0);
        initButton();
    }

    private void setTab() {
        _mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrollStateChanged(int position) {
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageSelected(int position) {
                // TODO Auto-generated method stub
                _btn1.setImageResource(R.drawable.holo_circle);
                _btn2.setImageResource(R.drawable.holo_circle);
                _btn3.setImageResource(R.drawable.holo_circle);
                _btn4.setImageResource(R.drawable.holo_circle);
                _btn5.setImageResource(R.drawable.holo_circle);
                _btn6.setImageResource(R.drawable.holo_circle);
                btnAction(position);
            }

        });

    }

    private void btnAction(int action) {
        switch (action) {
            case 0:
                _btn1.setImageResource(R.drawable.fill_circle);
                break;
            case 1:
                _btn2.setImageResource(R.drawable.fill_circle);
                break;
            case 2:
                _btn3.setImageResource(R.drawable.fill_circle);
                break;
            case 3:
                _btn4.setImageResource(R.drawable.fill_circle);
                break;
            case 4:
                _btn5.setImageResource(R.drawable.fill_circle);
                break;
            case 5:
                _btn6.setImageResource(R.drawable.fill_circle);
                break;
        }
    }

    private void initButton() {
        _btn1 = (ImageView) getView().findViewById(R.id.btn1);
        _btn1.setImageResource(R.drawable.fill_circle);
        _btn2 = (ImageView) getView().findViewById(R.id.btn2);
        _btn3 = (ImageView) getView().findViewById(R.id.btn3);
        _btn4 = (ImageView) getView().findViewById(R.id.btn4);
        _btn5 = (ImageView) getView().findViewById(R.id.btn5);
        _btn6 = (ImageView) getView().findViewById(R.id.btn6);

        skip = (Button) getView().findViewById(R.id.btnSkip);

    }

    private void setButton(Button btn, String text, int h, int w) {
        btn.setWidth(w);
        btn.setHeight(h);
        btn.setText(text);
    }
}
