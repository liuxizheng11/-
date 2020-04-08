package com.rocedar.sdk.familydoctor.app.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.rocedar.sdk.familydoctor.app.fragment.RCFDMainEvaluateFragment;
import com.rocedar.sdk.familydoctor.app.fragment.RCFDMainOrganizationFragment;


/**
 * @author liuyi
 * @date 2018/4/23
 * @desc
 * @veison
 */

public class FDFragmentPagerAdapter extends FragmentPagerAdapter {

    public FDFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0)
            return new RCFDMainEvaluateFragment();
        return new RCFDMainOrganizationFragment();
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "用户评价";
            case 1:
                return "知名机构";
            default:
                return "用户评价";
        }
    }
}
