package com.rocedar.deviceplatform.app.highbloodpressure;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.rocedar.base.RCHandler;
import com.rocedar.base.manger.RCBaseActivity;
import com.rocedar.deviceplatform.R;
import com.rocedar.deviceplatform.app.highbloodpressure.adapter.HBPProfessorIntroduceListAdapter;
import com.rocedar.deviceplatform.dto.highbloodpressure.RCHBPDoctorDTO;
import com.rocedar.deviceplatform.request.RCHighBloodPressureRequest;
import com.rocedar.deviceplatform.request.impl.RCHighBloodPressureRequestImpl;
import com.rocedar.deviceplatform.request.listener.highbloodpressure.RCHBPGetDoctorDetailsListener;




/**
 * @author liuyi
 * @date 2017/11/20
 * @desc 高血压专题-- 专家介绍
 * @veison V3.5.00
 */
public class HBPProfessorIntroduceActivity extends RCBaseActivity {

    RecyclerView rvHbpprpfessorIntroduce;
    private int doctorId = 0;
    private RCHighBloodPressureRequest request;
    private HBPProfessorIntroduceListAdapter adapter;
    private RCHBPDoctorDTO mDto = new RCHBPDoctorDTO();

    public static void goActivity(Context context, int doctorId) {
        Intent intent = new Intent(context, HBPProfessorIntroduceActivity.class);
        intent.putExtra("doctorId", doctorId);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hbpprofessor_introduce);
        mRcHeadUtil.setTitle(getString(R.string.high_blood_pressure_professor_introduce));

        request = new RCHighBloodPressureRequestImpl(mContext);
        doctorId = getIntent().getIntExtra("doctorId", 0);
        if (doctorId == 0)
            finishAffinity();
        rvHbpprpfessorIntroduce = (RecyclerView) findViewById(R.id.rv_hbpprpfessor_introduce);
        rvHbpprpfessorIntroduce.setLayoutManager(new LinearLayoutManager(mContext));
        rvHbpprpfessorIntroduce.setAdapter(adapter = new HBPProfessorIntroduceListAdapter(mContext, mDto));
        loadData();
    }

    private void loadData() {
        mRcHandler.sendMessage(RCHandler.START);
        request.getHBPDoctorDetails(doctorId, new RCHBPGetDoctorDetailsListener() {
            @Override
            public void getDataSuccess(RCHBPDoctorDTO dto) {
                mDto = dto;
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
