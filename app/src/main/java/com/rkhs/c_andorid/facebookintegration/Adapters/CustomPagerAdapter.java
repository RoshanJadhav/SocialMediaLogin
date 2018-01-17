package com.rkhs.c_andorid.facebookintegration.Adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.rkhs.c_andorid.facebookintegration.Fragments.*;

/**
 * Created by Admin on 16-01-2018.
 */

public class CustomPagerAdapter extends FragmentPagerAdapter {

    Context context;
    public int pos = 0;

    public CustomPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {

        pos = position;
        switch (position) {
            case 0:
                return FragmentOne.newInstance("Fragment_one");
            case 1:
                return FragmentTwo.newInstance("Fragment_two");
            case 2:
                return FragmentThree.newInstance("Fragment_three");
        }
        return FragmentOne.newInstance("Fragment_one");
    }

    @Override
    public int getCount() {
        return 3;
    }
}
