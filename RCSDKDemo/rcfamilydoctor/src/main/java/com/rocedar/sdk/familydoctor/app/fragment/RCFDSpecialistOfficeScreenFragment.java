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
import com.rocedar.lib.base.unit.RCJavaUtil;
import com.rocedar.lib.base.view.loading.PullOnLoading;
import com.rocedar.sdk.familydoctor.R;
import com.rocedar.sdk.familydoctor.app.RCFDSpecialistIntroducedActivity;
import com.rocedar.sdk.familydoctor.app.adapter.FDConsultListAdapter;
import com.rocedar.sdk.familydoctor.config.RCFDConfigUtil;
import com.rocedar.sdk.familydoctor.dto.RCFDDoctorListDTO;
import com.rocedar.sdk.familydoctor.dto.RCFDListDTO;
import com.rocedar.sdk.familydoctor.request.listener.fd.RCFDGetDoctorListListener;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;


/**
 * @author liuyi
 * @date 2017/4/18
 * @desc 科室筛选（家庭医生）
 * @veison V3.4.00新增
 */

public class RCFDSpecialistOfficeScreenFragment extends RCFDSpecialistBaseFragment {

    private ListView lvFamilyDoctorOffice;
    private ImageView ivFamilyDoctorOffice;
    private TextView tvFamilyDoctorOffice;


    private ArrayList<RCFDListDTO> mDatas = new ArrayList<>();
    private FDConsultListAdapter adapter;
    private PullOnLoading pullOnLoading;

    private int pn = 0;
    private int departmentId = -1;

    //手机号
    private PtrClassicFrameLayout pull_to_refresh;

    public static RCFDSpecialistOfficeScreenFragment newInstance(String phone, String deviceNumbers) {
        RCFDSpecialistOfficeScreenFragment fragment = new RCFDSpecialistOfficeScreenFragment();
        Bundle args = new Bundle();
        args.putString(KEY_PHONENO, phone);
        args.putString(KEY_DEVICENO, deviceNumbers);
        fragment.setArguments(args);
        return fragment;
    }


    private void initView(View view) {
        lvFamilyDoctorOffice = (ListView) view.findViewById(R.id.lv_family_doctor_office);

        ivFamilyDoctorOffice = (ImageView) view.findViewById(R.id.iv_family_doctor_office);
        if (RCFDConfigUtil.getConfig().imageResFDOfficeDefault() > 0) {
            ivFamilyDoctorOffice.setImageResource(RCFDConfigUtil.getConfig().imageResFDOfficeDefault());
        }

        tvFamilyDoctorOffice = (TextView) view.findViewById(R.id.tv_family_doctor_office);
        pull_to_refresh = (PtrClassicFrameLayout) view.findViewById(R.id.pull_to_refresh);
        pull_to_refresh.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                /**
                 *检查是否可以刷新，这里使用默认的PtrHandler进行判断
                 */
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                pn = 0;
                loadData(departmentId);
            }
        });
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.rc_fd_fragment_specialist_office_screen, null);
        initView(view);
        initAdapter();
        return view;

    }

    /**
     * 删除我的医生
     *
     * @param doctorId 医生ID
     */
    public void deleteMyDoctor(String doctorId) {
        for (int i = 0; i < mDatas.size(); i++) {
            if (mDatas.get(i).getRcfdDoctorListDTO().getDoctor_id().equals(doctorId)) {
                mDatas.get(i).getRcfdDoctorListDTO().setFocus(false);
                adapter.notifyDataSetChanged();
                return;
            }
        }
    }

    /**
     * 添加我的医生
     *
     * @param doctorId 医生ID
     */
    public void addMyDoctor(String doctorId) {
        for (int i = 0; i < mDatas.size(); i++) {
            if (mDatas.get(i).getRcfdDoctorListDTO().getDoctor_id().equals(doctorId)) {
                mDatas.get(i).getRcfdDoctorListDTO().setFocus(true);
                adapter.notifyDataSetChanged();
                return;
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    /**
     * @param department_id 科室ID(-1为全部科室)
     */
    public void loadData(int department_id) {
        if (departmentId != department_id) {
            departmentId = department_id;
            pn = 0;
        }
        mRcHandler.sendMessage(RCHandler.START);
        doctorRequest.getDoctorListFormDepartment(department_id + "", pn,
                new RCFDGetDoctorListListener() {
                    @Override
                    public void getDataSuccess(List<RCFDDoctorListDTO> doctorListDTO) {

                        if (pn == 0 && mDatas.size() > 0) {
                            mDatas.clear();
                        }

                        RCFDListDTO dto;
                        for (int i = 0; i < doctorListDTO.size(); i++) {
                            dto = new RCFDListDTO();
                            dto.setOpen(true);
                            dto.setHasOpen(RCJavaUtil.textCount(doctorListDTO.get(i).getSkilled(),
                                    1, 109, 12));
                            dto.setRcfdDoctorListDTO(doctorListDTO.get(i));
                            mDatas.add(dto);
                        }
                        isshowEmpty();
                        pullOnLoading.loadOver(doctorListDTO.size() == 20);
                        if (pn == 0) {
                            adapter.notifyDataSetInvalidated();
                            pull_to_refresh.refreshComplete();
                        } else {
                            adapter.notifyDataSetChanged();
                        }
                        pn++;
                        mRcHandler.sendMessage(RCHandler.GETDATA_OK);
                    }

                    @Override
                    public void getDataError(int status, String msg) {
                        isshowEmpty();
                        if (pn == 0) {
                            pull_to_refresh.refreshComplete();
                        }
                        mRcHandler.sendMessage(RCHandler.GETDATA_OK);
                    }
                });


    }

    private void isshowEmpty() {
        if (mDatas.size() > 0) {
            lvFamilyDoctorOffice.setVisibility(View.VISIBLE);
            ivFamilyDoctorOffice.setVisibility(View.GONE);
            tvFamilyDoctorOffice.setVisibility(View.GONE);
        } else {
            lvFamilyDoctorOffice.setVisibility(View.GONE);
            ivFamilyDoctorOffice.setVisibility(View.VISIBLE);
            tvFamilyDoctorOffice.setVisibility(View.VISIBLE);
            ivFamilyDoctorOffice.setImageResource(R.mipmap.rc_fd_ic_doctor_not);
        }
    }

    private void initAdapter() {
        adapter = new FDConsultListAdapter(this, mDatas,
                FDConsultListAdapter.CONSULT_DOCTOR_LIST);
        lvFamilyDoctorOffice.setAdapter(adapter);
        lvFamilyDoctorOffice.setFocusable(false);

        lvFamilyDoctorOffice.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RCFDSpecialistIntroducedActivity.goActivity(mActivity,
                        mDatas.get(position).getRcfdDoctorListDTO().getDoctor_id() + "",
                        mPhoneNumber, mDeviceNumber);
            }
        });

        pullOnLoading = new PullOnLoading(mActivity, lvFamilyDoctorOffice);
        pullOnLoading.setOnPullOnLoadingLintener(new PullOnLoading.OnPullOnLoadingListener() {
            @Override
            public void loading() {
                loadData(departmentId);
            }
        });
        loadData(departmentId);
    }


}
