package com.rcoedar.sdk.healthclass.app.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import java.util.ArrayList;

/**
 * @author liuyi
 * @date 2018/2/27
 * @desc
 * @veison
 */

public class ViewPagerFragmentAdapter extends android.support.v4.app.FragmentPagerAdapter {

    private ArrayList<Fragment> mFragments;
    private ArrayList<String> mTitles;

    public ViewPagerFragmentAdapter(FragmentManager fm, ArrayList<Fragment> mFragments, ArrayList<String> mTitles) {
        super(fm);
        this.mFragments = mFragments;
        this.mTitles = mTitles;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {

        return mTitles.get(position);
    }
}
