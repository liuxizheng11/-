package com.rocedar.deviceplatform.app.highbloodpressure.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rocedar.base.RCHandler;
import com.rocedar.base.RCToast;
import com.rocedar.base.manger.RCBaseFragment;
import com.rocedar.deviceplatform.R;
import com.rocedar.deviceplatform.app.highbloodpressure.adapter.HBPSeminarListAdapter;
import com.rocedar.deviceplatform.dto.highbloodpressure.RCHBPVideoInstituteDTO;
import com.rocedar.deviceplatform.request.RCHighBloodPressureRequest;
import com.rocedar.deviceplatform.request.impl.RCHighBloodPressureRequestImpl;
import com.rocedar.deviceplatform.request.listener.highbloodpressure.RCHBPGetVideoInstituListener;

import java.util.ArrayList;
import java.util.List;


/**
 * @author liuyi
 * @date 2017/11/21
 * @desc 高血压专题（专题讲座）
 * @veison
 */

public class HBPSeminarFragment extends RCBaseFragment {

    RecyclerView rvHighBloodPressure;
    List<RCHBPVideoInstituteDTO> mDatas = new ArrayList<>();
    private HBPSeminarListAdapter adapter;
    private RCHighBloodPressureRequest request;
    private int pn = 0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_high_blood_pressure, container, false);


        request = new RCHighBloodPressureRequestImpl(mActivity);
        rvHighBloodPressure = (RecyclerView) view.findViewById(R.id.rv_high_blood_pressure);
        rvHighBloodPressure.setLayoutManager(new LinearLayoutManager(mActivity));
        adapter = new HBPSeminarListAdapter(mActivity, mDatas);

        rvHighBloodPressure.setAdapter(adapter);

        adapter.setOnItemClickListener(new HBPSeminarListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view) {
                RCToast.TestCenter(mActivity, "点了"+view.getTag(),false);
            }
        });

        loadData();
        return view;
    }

    private void loadData() {
        mRcHandler.sendMessage(RCHandler.START);
        request.getVideoInstituteList(pn+"", new RCHBPGetVideoInstituListener() {
            @Override
            public void getDataSuccess(List<RCHBPVideoInstituteDTO> dtoList) {
                mDatas.addAll(dtoList);
                adapter.notifyDataSetChanged();
                mRcHandler.sendMessage(RCHandler.GETDATA_OK);
            }

            @Override
            public void getDataError(int status, String msg) {
                mRcHandler.sendMessage(RCHandler.GETDATA_OK);
            }
        });

    }

}
