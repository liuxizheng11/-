package com.rocedar.deviceplatform.app.devicelist.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.rocedar.base.RCHandler;
import com.rocedar.base.RCTPJump;
import com.rocedar.base.manger.RCBaseFragment;
import com.rocedar.deviceplatform.R;
import com.rocedar.deviceplatform.app.devicelist.adapter.DeviceChooseListGridAdapter;
import com.rocedar.deviceplatform.dto.data.RCDeviceDataListDTO;
import com.rocedar.deviceplatform.request.impl.RCDeviceOperationRequestImpl;
import com.rocedar.deviceplatform.request.listener.RCDeviceGetDataListener;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liuyi
 * @date 2017/2/13
 * @desc 设备类型列表
 * @veison V1.0
 */

public class DeviceChooseListFragment extends RCBaseFragment {
    private GridView gv_choice_device;
    private int mDeviceId;//设备列表id
    private ArrayList<RCDeviceDataListDTO> mFragmentDatas = new ArrayList<>();
    private View view;

    public static DeviceChooseListFragment newInstance(int type_id) {
        DeviceChooseListFragment fragment = new DeviceChooseListFragment();
        Bundle args = new Bundle();
        args.putInt("type_id", type_id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view != null) {
            return view;
        }
        view = inflater.inflate(R.layout.fragment_device_list, null);
        mDeviceId = getArguments().getInt("type_id",0);
        gv_choice_device = (GridView) view.findViewById(R.id.gv_choice_device);
        loadData(mDeviceId);
        return view;
    }

    private void loadData(int mDeviceId) {
        mRcHandler.sendMessage(RCHandler.START);
        RCDeviceOperationRequestImpl.getInstance(mActivity).getDeviceTypeList(new RCDeviceGetDataListener() {
            @Override
            public void getDataSuccess(List<RCDeviceDataListDTO> dtoList) {
                mFragmentDatas.addAll(dtoList);
                initGridView();
                mRcHandler.sendMessage(RCHandler.GETDATA_OK);
            }

            @Override
            public void getDataError(int status, String msg) {
                mRcHandler.sendMessage(RCHandler.GETDATA_OK);
            }
        },mDeviceId);

    }

    private void initGridView() {

        gv_choice_device.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RCTPJump.ActivityJump(mActivity, mFragmentDatas.get(position).getBind_url());
            }
        });
        DeviceChooseListGridAdapter deviceListGridAdapter = new DeviceChooseListGridAdapter(mFragmentDatas, mActivity);
        gv_choice_device.setAdapter(deviceListGridAdapter);

    }
}

