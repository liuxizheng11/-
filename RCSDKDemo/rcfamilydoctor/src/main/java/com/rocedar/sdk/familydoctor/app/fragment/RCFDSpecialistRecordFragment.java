package com.rocedar.sdk.familydoctor.app.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.rocedar.lib.base.unit.RCHandler;
import com.rocedar.lib.base.view.loading.PullOnLoading;
import com.rocedar.sdk.familydoctor.R;
import com.rocedar.sdk.familydoctor.app.RCFDSpecialistRecordDetailActivity;
import com.rocedar.sdk.familydoctor.app.adapter.FDRecordListAdapter;
import com.rocedar.sdk.familydoctor.config.RCFDConfigUtil;
import com.rocedar.sdk.familydoctor.dto.RCFDRecordListDTO;
import com.rocedar.sdk.familydoctor.request.listener.fd.RCFDGetRecordListListener;

import java.util.ArrayList;
import java.util.List;


/**
 * @author liuyi
 * @date 2017/4/18
 * @desc 咨询记录（家庭医生）
 * @veison V3.4.00新增
 */

public class RCFDSpecialistRecordFragment extends RCFDSpecialistBaseFragment {

    ListView lvFamilyDoctorConsult;
    ImageView ivFamilyDoctorConsult;
    TextView tvFamilyDoctorConsult;

    private ArrayList<RCFDRecordListDTO> mDatas = new ArrayList<>();
    private FDRecordListAdapter adapter;
    private PullOnLoading pullOnLoading;
    private int pn = 0;

    public static RCFDSpecialistRecordFragment newInstance(String phone, String deviceNumbers) {
        RCFDSpecialistRecordFragment fragment = new RCFDSpecialistRecordFragment();
        Bundle args = new Bundle();
        args.putString(KEY_PHONENO, phone);
        args.putString(KEY_DEVICENO, deviceNumbers);
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.rc_fd_fragment_specialist_consult_record, null);
        initView(view);
        loadData();
        return view;
    }

    private void initView(View view) {
        lvFamilyDoctorConsult = (ListView) view.findViewById(R.id.lv_family_doctor_consult);
        ivFamilyDoctorConsult = (ImageView) view.findViewById(R.id.iv_family_doctor_consult);
        if (RCFDConfigUtil.getConfig().imageResFDRecordDefault() > 0) {
            ivFamilyDoctorConsult.setImageResource(RCFDConfigUtil.getConfig().imageResFDRecordDefault());
        }
        tvFamilyDoctorConsult = (TextView) view.findViewById(R.id.tv_family_doctor_consult);
        lvFamilyDoctorConsult.setFocusable(false);

        adapter = new FDRecordListAdapter(mDatas, mActivity);
        lvFamilyDoctorConsult.setAdapter(adapter);

        lvFamilyDoctorConsult.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                RCFDSpecialistRecordDetailActivity.goActivity(mActivity,
                        mDatas.get(position).getRecord_id(), mPhoneNumber, mDeviceNumber);

            }
        });

        pullOnLoading = new PullOnLoading(mActivity, lvFamilyDoctorConsult);
        pullOnLoading.setOnPullOnLoadingLintener(new PullOnLoading.OnPullOnLoadingListener() {
            @Override
            public void loading() {
                loadData();
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    private void loadData() {

        mRcHandler.sendMessage(RCHandler.START);
        recordRequest.getRecordList(pn, new RCFDGetRecordListListener() {
            @Override
            public void getDataSuccess(List<RCFDRecordListDTO> dto) {
                if (pn == 0) {
                    mDatas.clear();
                }
                mDatas.addAll(dto);

                pullOnLoading.loadOver(dto.size() == 20);
                adapter.notifyDataSetChanged();
                pn++;
                isShowEmpty();
                mRcHandler.sendMessage(RCHandler.GETDATA_OK);
            }

            @Override
            public void getDataError(int status, String msg) {
                mRcHandler.sendMessage(RCHandler.GETDATA_OK);
            }
        });

    }

    private void isShowEmpty() {
        if (mDatas.size() > 0) {
            lvFamilyDoctorConsult.setVisibility(View.VISIBLE);
            ivFamilyDoctorConsult.setVisibility(View.GONE);
            tvFamilyDoctorConsult.setVisibility(View.GONE);
        } else {
            lvFamilyDoctorConsult.setVisibility(View.GONE);
            ivFamilyDoctorConsult.setVisibility(View.VISIBLE);
            tvFamilyDoctorConsult.setVisibility(View.VISIBLE);
        }
    }


}

