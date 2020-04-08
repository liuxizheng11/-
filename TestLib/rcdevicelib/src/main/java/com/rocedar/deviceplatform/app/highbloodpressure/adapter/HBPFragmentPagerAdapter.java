package com.rocedar.deviceplatform.app.highbloodpressure.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.rocedar.deviceplatform.app.highbloodpressure.fragment.HBPProfessorFragment;
import com.rocedar.deviceplatform.app.highbloodpressure.fragment.HBPSeminarFragment;


/**
 * @author liuyi
 * @date 2017/11/21
 * @desc
 * @veison
 */

public class HBPFragmentPagerAdapter extends FragmentPagerAdapter {

    public HBPFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new HBPSeminarFragment();
            case 1:
                return new HBPProfessorFragment();
            default:
                return new HBPSeminarFragment();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "专题讲座";
            case 1:
                return "实验室专家";
            default:
                return "专题讲座";
        }
    }
}
