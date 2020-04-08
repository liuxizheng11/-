package com.rocedar.deviceplatform.app.devicelist.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.rocedar.deviceplatform.dto.data.RCDeviceDataListTypeDTO;

import java.util.ArrayList;

/**
 * @author liuyi
 * @date 2017/2/13
 * @desc 设备类型列表
 * @veison V1.0
 */

public class DeviceChooseListFragmentPagerAdapter extends FragmentPagerAdapter {

    private ArrayList<Fragment> mFragments;
    private ArrayList<RCDeviceDataListTypeDTO> mTitles;

    public DeviceChooseListFragmentPagerAdapter(FragmentManager fm, ArrayList<Fragment> mFragments, ArrayList<RCDeviceDataListTypeDTO> mTitles) {
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

        return mTitles.get(position).getType_name();
    }
}

