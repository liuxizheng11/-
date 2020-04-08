package com.rocedar.deviceplatform.app.measure.fragment;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rocedar.base.manger.RCBaseFragment;
import com.rocedar.deviceplatform.R;
import com.rocedar.deviceplatform.app.measure.BloodPressure37Activity;
import com.rocedar.deviceplatform.app.measure.adapter.WaitMeasurePagerAdapter;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

/**
 * @author liuyi
 * @date 2017/2/11
 * @desc 37血压计等待测量页面
 * @veison V1.0
 */

public class WaitMeasureBloodFragment extends RCBaseFragment {
    private BloodPressure37Activity activity;
    private View view;
    private ViewPager viewpager_waiting_measure;
    private WaitMeasurePagerAdapter adapter;
    private ImageView ic_measure_circle_right;
    private ImageView iv_wait_measure_start;
    private TextView tv_wait_measure_start;
    private ImageView iv_measure_wait_conn;
    private TextView tv_measure_wait_conn;
    private ImageView ic_measure_circle_left;
    private ImageView iv_measure_bluetooth_start;
    private TextView tv_measure_bluetooth_start;
    public BluetoothAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_waiting_measure, null);
        activity = (BloodPressure37Activity) getActivity();
        mAdapter = BluetoothAdapter.getDefaultAdapter();

        //需要改
//        RCBlueTooth.blueOpen(activity,mAdapter);

        //监听蓝牙状态发生改变的广播
        IntentFilter bluetoothFilter = new IntentFilter();
        bluetoothFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        activity.registerReceiver(mReceiver, bluetoothFilter);


        initView(view);
        initViewPager();


        if (mAdapter.isEnabled()) {
            isBlueTooth(true);
        } else {
            Intent mIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(mIntent, 1);
        }
        return view;
    }

    private void initView(View view) {
        viewpager_waiting_measure = (ViewPager) view.findViewById(R.id.viewpager_waiting_measure);
        ic_measure_circle_right = (ImageView) view.findViewById(R.id.ic_measure_circle_right);
        iv_wait_measure_start = (ImageView) view.findViewById(R.id.iv_wait_measure_start);
        tv_wait_measure_start = (TextView) view.findViewById(R.id.tv_wait_measure_start);
        iv_measure_wait_conn = (ImageView) view.findViewById(R.id.iv_measure_wait_conn);
        tv_measure_wait_conn = (TextView) view.findViewById(R.id.tv_measure_wait_conn);
        ic_measure_circle_left = (ImageView) view.findViewById(R.id.ic_measure_circle_left);
        iv_measure_bluetooth_start = (ImageView) view.findViewById(R.id.iv_measure_bluetooth_start);
        tv_measure_bluetooth_start = (TextView) view.findViewById(R.id.tv_measure_bluetooth_start);
    }

    public void isBlueTooth(boolean isOpen) {
        if (isOpen) {
            iv_measure_bluetooth_start.setBackgroundResource(R.mipmap.ic_measure_bluetooth);
            tv_measure_bluetooth_start.setTextColor(getResources().getColor(R.color.my_wait_select));
            ic_measure_circle_left.setBackgroundResource(R.mipmap.ic_measure_circle_select);
            showNextPager(1);
            activity.connBloodPressure();
        } else {
            iv_measure_bluetooth_start.setBackgroundResource(R.mipmap.ic_measure_bluetooth_not);
            tv_measure_bluetooth_start.setTextColor(getResources().getColor(R.color.my_wait_normal));
            ic_measure_circle_left.setBackgroundResource(R.mipmap.ic_measure_circle_normal);
            iv_measure_wait_conn.setBackgroundResource(R.mipmap.ic_measure_connection_not);
            tv_measure_wait_conn.setTextColor(getResources().getColor(R.color.my_wait_normal));
            ic_measure_circle_right.setBackgroundResource(R.mipmap.ic_measure_circle_normal);
            iv_wait_measure_start.setBackgroundResource(R.mipmap.ic_measure_start_not);
            tv_wait_measure_start.setTextColor(getResources().getColor(R.color.my_wait_normal));
        }

    }

    public void waitConntection() {
        iv_measure_wait_conn.setBackgroundResource(R.mipmap.ic_measure_connection);
        tv_measure_wait_conn.setTextColor(getResources().getColor(R.color.my_wait_select));
        ic_measure_circle_left.setBackgroundResource(R.mipmap.ic_measure_circle_select);
        showNextPager(2);
    }

    private void initViewPager() {
        adapter = new WaitMeasurePagerAdapter(activity);
        viewpager_waiting_measure.setAdapter(adapter);
    }

    public void showNextPager(int pos) {
        viewpager_waiting_measure.setCurrentItem(pos);
        if (pos == 2) {
            ic_measure_circle_right.setBackgroundResource(R.mipmap.ic_measure_circle_select);
            iv_wait_measure_start.setBackgroundResource(R.mipmap.ic_measure_start);
            tv_wait_measure_start.setTextColor(getResources().getColor(R.color.my_wait_select));
            activity.initFragment(true);

        }
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case BluetoothAdapter.ACTION_STATE_CHANGED:
                    int blueState = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, 0);
                    switch (blueState) {

                        case BluetoothAdapter.STATE_ON:
                            isBlueTooth(true);
                            break;
                        case BluetoothAdapter.STATE_OFF:
                            isBlueTooth(false);
                            break;
                    }
                    break;
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        activity.unregisterReceiver(mReceiver);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                isBlueTooth(true);

            } else if (resultCode == RESULT_CANCELED) {
                mActivity.finish();
            }
        }
    }
}


