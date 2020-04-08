package com.rocedar.deviceplatform.app.highbloodpressure.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rocedar.base.RCHandler;
import com.rocedar.base.manger.RCBaseFragment;
import com.rocedar.deviceplatform.R;
import com.rocedar.deviceplatform.app.highbloodpressure.HBPProfessorIntroduceActivity;
import com.rocedar.deviceplatform.app.highbloodpressure.adapter.HBPProfessorListadapter;
import com.rocedar.deviceplatform.dto.highbloodpressure.RCHBPDoctorDTO;
import com.rocedar.deviceplatform.request.RCHighBloodPressureRequest;
import com.rocedar.deviceplatform.request.impl.RCHighBloodPressureRequestImpl;
import com.rocedar.deviceplatform.request.listener.highbloodpressure.RCHBPGetDoctorListener;

import java.util.ArrayList;
import java.util.List;




/**
 * @author liuyi
 * @date 2017/11/21
 * @desc 高血压专题（实验室专家）
 * @veison
 */

public class HBPProfessorFragment extends RCBaseFragment {


    RecyclerView rvHighBloodPressure;
    List<RCHBPDoctorDTO> mDatas = new ArrayList<>();
    private HBPProfessorListadapter adapter;
    private RCHighBloodPressureRequest request;
    private int pn = 0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_high_blood_pressure, container, false);

        request = new RCHighBloodPressureRequestImpl(mActivity);
        rvHighBloodPressure = (RecyclerView) view.findViewById(R.id.rv_high_blood_pressure);
        rvHighBloodPressure.setLayoutManager(new LinearLayoutManager(mActivity));
        adapter = new HBPProfessorListadapter(mActivity,mDatas);

        rvHighBloodPressure.setAdapter(adapter);

        adapter.setOnItemClickListener(new HBPProfessorListadapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view) {
                HBPProfessorIntroduceActivity.goActivity(mActivity,mDatas.get((Integer) view.getTag()).getDoctor_id());
            }
        });

        initData();
        return view;
    }

    private void initData() {
        mRcHandler.sendMessage(RCHandler.START);
        request.getHBPDoctor(pn+"", new RCHBPGetDoctorListener() {
            @Override
            public void getDataSuccess(List<RCHBPDoctorDTO> dtoList) {
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
