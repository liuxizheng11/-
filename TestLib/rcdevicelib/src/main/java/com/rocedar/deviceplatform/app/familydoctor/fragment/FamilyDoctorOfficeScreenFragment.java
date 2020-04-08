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
import com.rocedar.base.RCJavaUtil;
import com.rocedar.base.RCToast;
import com.rocedar.base.manger.RCBaseFragment;
import com.rocedar.base.view.PullOnLoading;
import com.rocedar.deviceplatform.R;
import com.rocedar.deviceplatform.app.familydoctor.FamilyDoctorActivity;
import com.rocedar.deviceplatform.app.familydoctor.FamilyDoctorIntroducedActivity;
import com.rocedar.deviceplatform.app.familydoctor.adapter.FamilyDoctorConsultListAdapter;
import com.rocedar.deviceplatform.dto.familydoctor.FamilyDoctorListDTO;
import com.rocedar.deviceplatform.dto.familydoctor.RCFDDoctorListDTO;
import com.rocedar.deviceplatform.request.impl.RCFamilyDoctorWWZImpl;
import com.rocedar.deviceplatform.request.listener.familydoctor.RCFDGetDoctorListListener;
import com.rocedar.deviceplatform.request.listener.familydoctor.RCFDPostListener;

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

public class FamilyDoctorOfficeScreenFragment extends RCBaseFragment {
    ListView lvFamilyDoctorOffice;
    ImageView ivFamilyDoctorOffice;
    TextView tvFamilyDoctorOffice;
    private ArrayList<FamilyDoctorListDTO> mDatas = new ArrayList<>();
    private FamilyDoctorConsultListAdapter adapter;
    private PullOnLoading pullOnLoading;
    private RCFamilyDoctorWWZImpl wwzImpl;
    private int pn = 0;
    private int departmentId = -1;


    //手机号
    private String phoneNumber;
    private PtrClassicFrameLayout pull_to_refresh;

    public static FamilyDoctorOfficeScreenFragment newInstance(String phone) {
        FamilyDoctorOfficeScreenFragment fragment = new FamilyDoctorOfficeScreenFragment();
        Bundle args = new Bundle();
        args.putString("phone", phone);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_doctor_office_screen, null);
        phoneNumber = getArguments().getString("phone");
        wwzImpl = new RCFamilyDoctorWWZImpl(mActivity);
        lvFamilyDoctorOffice = (ListView) view.findViewById(R.id.lv_family_doctor_office);
        ivFamilyDoctorOffice = (ImageView) view.findViewById(R.id.iv_family_doctor_office);
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
                wwzImpl.getDoctorListFormDepartment(departmentId + "", pn, new RCFDGetDoctorListListener() {
                    @Override
                    public void getDataSuccess(List<RCFDDoctorListDTO> doctorListDTO) {

                        if (pn == 0 && mDatas.size() > 0) {
                            mDatas.clear();
                        }

                        FamilyDoctorListDTO dto;
                        for (int i = 0; i < doctorListDTO.size(); i++) {
                            dto = new FamilyDoctorListDTO();
                            dto.setOpen(true);
                            dto.setHasOpen(RCJavaUtil.textCount(doctorListDTO.get(i).getSkilled(), 1, 109, 12));
                            dto.setRcfdDoctorListDTO(doctorListDTO.get(i));
                            mDatas.add(dto);
                        }
                        isshowEmpty();
                        pullOnLoading.loadOver(doctorListDTO.size() == 20);
                        if (pn == 0) {
                            adapter.notifyDataSetInvalidated();
                        } else {
                            adapter.notifyDataSetChanged();
                        }

                        pull_to_refresh.refreshComplete();
                    }

                    @Override
                    public void getDataError(int status, String msg) {
                        isshowEmpty();
                        pull_to_refresh.refreshComplete();
                    }
                });

            }
        });

        initView(view);

        return view;

    }

    private void loadingData() {
        pn++;
        loadData(departmentId);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData(departmentId);
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
        wwzImpl.getDoctorListFormDepartment(department_id + "", pn, new RCFDGetDoctorListListener() {
            @Override
            public void getDataSuccess(List<RCFDDoctorListDTO> doctorListDTO) {

                if (pn == 0 && mDatas.size() > 0) {
                    mDatas.clear();
                }

                FamilyDoctorListDTO dto;
                for (int i = 0; i < doctorListDTO.size(); i++) {
                    dto = new FamilyDoctorListDTO();
                    dto.setOpen(true);
                    dto.setHasOpen(RCJavaUtil.textCount(doctorListDTO.get(i).getSkilled(), 1, 109, 12));
                    dto.setRcfdDoctorListDTO(doctorListDTO.get(i));
                    mDatas.add(dto);
                }
                isshowEmpty();
                pullOnLoading.loadOver(doctorListDTO.size() == 20);
                if (pn == 0) {
                    adapter.notifyDataSetInvalidated();
                } else {
                    adapter.notifyDataSetChanged();
                }

                mRcHandler.sendMessage(RCHandler.GETDATA_OK);
            }

            @Override
            public void getDataError(int status, String msg) {
                isshowEmpty();
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
            ivFamilyDoctorOffice.setImageResource(((FamilyDoctorActivity) mActivity).getiPlatformUtil().getDoctorListEmptyImg());
        }
    }

    private void initView(View view) {
        adapter = new FamilyDoctorConsultListAdapter((FamilyDoctorActivity) mActivity, mDatas,
                FamilyDoctorConsultListAdapter.CONSULT_DOCTOR_LIST);
        lvFamilyDoctorOffice.setAdapter(adapter);
        lvFamilyDoctorOffice.setFocusable(false);

        // TODO: 2017/11/7 进入医生详情
        lvFamilyDoctorOffice.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FamilyDoctorIntroducedActivity.goActivity(mActivity,
                        mDatas.get(position).getRcfdDoctorListDTO().getDoctor_id() + "", phoneNumber);
            }
        });

        pullOnLoading = new PullOnLoading(mActivity, lvFamilyDoctorOffice);
        pullOnLoading.setOnPullOnLoadingLintener(new PullOnLoading.OnPullOnLoadingLintener() {
            @Override
            public void loading() {
                loadingData();
            }
        });
    }

    /**
     * 添加为我的医生
     *
     * @param doctorId 医生id
     * @param position 当前索引
     */
    public void addMyDoctor(String doctorId, final int position) {
        mRcHandler.sendMessage(RCHandler.START);
        wwzImpl.addMyDoctor(doctorId, new RCFDPostListener() {
            @Override
            public void getDataSuccess() {

                mDatas.get(position).getRcfdDoctorListDTO().setFocus(true);
                adapter.notifyDataSetChanged();
                RCToast.Center(mActivity, "添加成功，可以去我的医生中查看哦～");
                mRcHandler.sendMessage(RCHandler.GETDATA_OK);
            }

            @Override
            public void getDataError(int status, String msg) {
                mRcHandler.sendMessage(RCHandler.GETDATA_OK);
            }
        });
    }
}
