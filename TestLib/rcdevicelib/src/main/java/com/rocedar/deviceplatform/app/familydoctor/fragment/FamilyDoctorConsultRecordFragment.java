package com.rocedar.deviceplatform.app.familydoctor.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.rocedar.base.RCHandler;
import com.rocedar.base.manger.RCBaseFragment;
import com.rocedar.base.view.PullOnLoading;
import com.rocedar.deviceplatform.R;
import com.rocedar.deviceplatform.app.familydoctor.FamilyDoctorActivity;
import com.rocedar.deviceplatform.app.familydoctor.FamilyDoctorRecordDetailsActivity;
import com.rocedar.deviceplatform.app.familydoctor.adapter.FamilyDoctorRecordListAdapter;
import com.rocedar.deviceplatform.dto.familydoctor.RCFDRecordListDTO;
import com.rocedar.deviceplatform.request.impl.RCFamilyDoctorWWZImpl;
import com.rocedar.deviceplatform.request.listener.familydoctor.RCFDGetRecordListListener;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liuyi
 * @date 2017/4/18
 * @desc 咨询记录（家庭医生）
 * @veison V3.4.00新增
 */

public class FamilyDoctorConsultRecordFragment extends RCBaseFragment {

    ListView lvFamilyDoctorConsult;
    ImageView ivFamilyDoctorConsult;
    TextView tvFamilyDoctorConsult;

    private ArrayList<RCFDRecordListDTO> mDatas = new ArrayList<>();
    private FamilyDoctorRecordListAdapter adapter;
    private PullOnLoading pullOnLoading;
    private RCFamilyDoctorWWZImpl wwzImpl;
    private int pn = 0;
    private String phoneNumber = "";

    public static FamilyDoctorConsultRecordFragment newInstance(String phone) {
        FamilyDoctorConsultRecordFragment fragment = new FamilyDoctorConsultRecordFragment();
        Bundle args = new Bundle();
        args.putString("phone", phone);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_doctor_consult_record, null);
        phoneNumber = getArguments().getString("phone");
        lvFamilyDoctorConsult = (ListView) view.findViewById(R.id.lv_family_doctor_consult);
        ivFamilyDoctorConsult = (ImageView) view.findViewById(R.id.iv_family_doctor_consult);
        tvFamilyDoctorConsult = (TextView) view.findViewById(R.id.tv_family_doctor_consult);
        initView();
        return view;

    }

    private void loadingData() {
        pn++;
        loadData();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }

    private void loadData() {

        mRcHandler.sendMessage(RCHandler.START);
        wwzImpl.getRecordList(pn, new RCFDGetRecordListListener() {
            @Override
            public void getDataSuccess(List<RCFDRecordListDTO> dto) {
                if (pn == 0) {
                    mDatas.clear();
                }
                mDatas.addAll(dto);

                pullOnLoading.loadOver(dto.size() == 20);
                adapter.notifyDataSetChanged();

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
            ivFamilyDoctorConsult.setImageResource(((FamilyDoctorActivity)mActivity).getiPlatformUtil().getConsultRecordEmptyImg());
            tvFamilyDoctorConsult.setText(((FamilyDoctorActivity)mActivity).getiPlatformUtil().getConsultRecordEmptyText());
        }
    }

    private void initView() {
        lvFamilyDoctorConsult.setFocusable(false);
        wwzImpl = new RCFamilyDoctorWWZImpl(mActivity);
        adapter = new FamilyDoctorRecordListAdapter(mDatas, mActivity);
        lvFamilyDoctorConsult.setAdapter(adapter);
        lvFamilyDoctorConsult.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                FamilyDoctorRecordDetailsActivity.goActivity(mActivity, mDatas.get(position).getRecord_id(), phoneNumber);

            }
        });

        pullOnLoading = new PullOnLoading(mActivity, lvFamilyDoctorConsult);
        pullOnLoading.setOnPullOnLoadingLintener(new PullOnLoading.OnPullOnLoadingLintener() {
            @Override
            public void loading() {
                loadingData();
            }
        });
    }
}

