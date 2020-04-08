package com.rocedar.deviceplatform.app.devicelist;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.rocedar.base.RCHandler;
import com.rocedar.base.manger.RCBaseActivity;
import com.rocedar.deviceplatform.R;
import com.rocedar.deviceplatform.app.devicelist.adapter.DeviceChooseListFragmentPagerAdapter;
import com.rocedar.deviceplatform.app.devicelist.fragment.DeviceChooseListFragment;
import com.rocedar.deviceplatform.dto.data.RCDeviceDataListTypeDTO;
import com.rocedar.deviceplatform.request.impl.RCDeviceOperationRequestImpl;
import com.rocedar.deviceplatform.request.listener.RCDeviceDataListTypeListener;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liuyi
 * @date 2017/2/13
 * @desc 设备类型列表（有分类）
 * @veison V1.0
 */
public class DeviceChooseListActivity extends RCBaseActivity {


    private TabLayout tablayout_choice_device;
    private ViewPager viewpager_choice_device;
    private TextView tv_choice_other_device;
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private ArrayList<RCDeviceDataListTypeDTO> mTitles = new ArrayList<>();

    public static void goActivity(Context context) {
        Intent intent = new Intent(context, DeviceChooseListActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_choose_list);
        mRcHeadUtil.setLeftBack().setTitle(getString(R.string.rcdevice_choice_device));

        initView();
        loadData();

    }

    private void loadData() {
        mRcHandler.sendMessage(RCHandler.START);
        RCDeviceOperationRequestImpl.getInstance(mContext).getDeviceDataType(new RCDeviceDataListTypeListener() {
            @Override
            public void getDataSuccess(List<RCDeviceDataListTypeDTO> dtoList) {
                mTitles.addAll(dtoList);
                initViewPager();
                mRcHandler.sendMessage(RCHandler.GETDATA_OK);
            }

            @Override
            public void getDataError(int status, String msg) {
                mRcHandler.sendMessage(RCHandler.GETDATA_OK);
            }
        });
    }

    private void initViewPager() {

        if (mTitles != null && mTitles.size() > 0) {
            for (int i = 0; i < mTitles.size(); i++) {
                //创建相应的fragment
                mFragments.add(DeviceChooseListFragment.newInstance(mTitles.get(i).getType_id()));
                //添加tab标题名字
                tablayout_choice_device.addTab(tablayout_choice_device.newTab().setText(mTitles.get(i).getType_name()));
            }
        }

        DeviceChooseListFragmentPagerAdapter deviceListFragmentPagerAdapter = new DeviceChooseListFragmentPagerAdapter(getSupportFragmentManager(), mFragments, mTitles);
        viewpager_choice_device.setAdapter(deviceListFragmentPagerAdapter);
        tablayout_choice_device.setupWithViewPager(viewpager_choice_device);//将TabLayout和ViewPager关联起来。
        tablayout_choice_device.setTabsFromPagerAdapter(deviceListFragmentPagerAdapter);//给Tabs设置适配器
        //暂时去掉指示器颜色
        tablayout_choice_device.setSelectedTabIndicatorColor(getResources().getColor(R.color.white));
        tablayout_choice_device.setTabTextColors(
                getResources().getColor(R.color.rcbase_app_text_default),
                getResources().getColor(R.color.rcbase_app_main)
        );

    }

    private void initView() {
        tablayout_choice_device = (TabLayout) findViewById(R.id.tablayout_chocie_device);
        viewpager_choice_device = (ViewPager) findViewById(R.id.viewpager_choice_device);
        tv_choice_other_device = (TextView) findViewById(R.id.tv_choice_other_device);

        tv_choice_other_device.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                暂时去掉选择其他硬件的按钮
//                startActivity(new Intent(mContext,MyDeviceManagerActivity.class));
            }
        });

    }
}

