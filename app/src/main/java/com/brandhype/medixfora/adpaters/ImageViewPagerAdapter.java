package com.brandhype.medixfora.adpaters;

//import android.app.Fragment;
//import android.app.FragmentManager;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


import com.brandhype.medixfora.fragments.ImageFiveFragment;
import com.brandhype.medixfora.fragments.ImageFourFragment;
import com.brandhype.medixfora.fragments.ImageOneFragment;
import com.brandhype.medixfora.fragments.ImageSixFragment;
import com.brandhype.medixfora.fragments.ImageThreeFragment;
import com.brandhype.medixfora.fragments.ImageTwoFragment;


/**
 * Created by nirmal on 12/08/15.
 */
public class ImageViewPagerAdapter extends FragmentPagerAdapter {
    private Context _context;
    public static int totalPage = 6;

    public ImageViewPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        _context = context;

    }

    @Override
    public Fragment getItem(int position) {
        Fragment f = new Fragment();
        switch (position) {
            case 0:
                f = new ImageOneFragment();
                break;
            case 1:
                f = new ImageTwoFragment();
                break;
            case 2:
                f = new ImageThreeFragment();
                break;
            case 3:
                f = new ImageFourFragment();
                break;
            case 4:
                f = new ImageFiveFragment();
                break;
            case 5:
                f = new ImageSixFragment();
                break;
        }
        return f;
    }

    @Override
    public int getCount() {
        return totalPage;
    }

}

