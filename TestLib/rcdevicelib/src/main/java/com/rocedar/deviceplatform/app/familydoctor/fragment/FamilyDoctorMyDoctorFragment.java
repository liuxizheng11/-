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

import com.rocedar.base.RCDialog;
import com.rocedar.base.RCHandler;
import com.rocedar.base.RCJavaUtil;
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
 * @desc 我的医生（家庭医生）
 * @veison V3.4.00新增
 */

public class FamilyDoctorMyDoctorFragment extends RCBaseFragment {
    TextView tvMyDoctorAdd;
    ListView lvMyDoctor;
    ImageView ivMyDoctor;

    private ArrayList<FamilyDoctorListDTO> mDatas = new ArrayList<>();
    FamilyDoctorConsultListAdapter adapter;
    private RCDialog delMyDoctorDialog;
    private PullOnLoading pullOnLoading;
    private RCFamilyDoctorWWZImpl wwzImpl;
    private int pn = 0;

    private PtrClassicFrameLayout pull_to_refresh;
    //手机号
    private String phoneNumber;

    public static FamilyDoctorMyDoctorFragment newInstance(String phone) {
        FamilyDoctorMyDoctorFragment fragment = new FamilyDoctorMyDoctorFragment();
        Bundle args = new Bundle();
        args.putString("phone", phone);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_doctor_my_doctor, null);
        phoneNumber = getArguments().getString("phone");
        tvMyDoctorAdd = (TextView) view.findViewById(R.id.tv_my_doctor_add);
        lvMyDoctor = (ListView) view.findViewById(R.id.lv_my_doctor);
        ivMyDoctor = (ImageView) view.findViewById(R.id.iv_my_doctor);
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
                wwzImpl.getMyDoctorList(pn, new RCFDGetDoctorListListener() {
                    @Override
                    public void getDataSuccess(List<RCFDDoctorListDTO> doctorListDTO) {

                        mDatas.clear();

                        FamilyDoctorListDTO dto;
                        for (int i = 0; i < doctorListDTO.size(); i++) {
                            dto = new FamilyDoctorListDTO();
                            dto.setOpen(true);
                            dto.setHasOpen(RCJavaUtil.textCount(doctorListDTO.get(i).getSkilled(), 1, 109, 12));
                            dto.setRcfdDoctorListDTO(doctorListDTO.get(i));
                            mDatas.add(dto);
                        }

                        showView();
                        pullOnLoading.loadOver(doctorListDTO.size() == 20);
                        adapter.notifyDataSetChanged();
                        pull_to_refresh.refreshComplete();
                    }

                    @Override
                    public void getDataError(int status, String msg) {
                        showView();
                        pull_to_refresh.refreshComplete();
                    }
                });

            }
        });


        tvMyDoctorAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mActivity instanceof FamilyDoctorActivity) {
                    ((FamilyDoctorActivity) mActivity).showOfficeScreenFragment();
                }
            }
        });
        initView(view);

        return view;

    }

    private void initView(View view) {
        lvMyDoctor.setFocusable(false);
        wwzImpl = new RCFamilyDoctorWWZImpl(mActivity);
        adapter = new FamilyDoctorConsultListAdapter((FamilyDoctorActivity) mActivity, mDatas,
                FamilyDoctorConsultListAdapter.MY_DOCTOR_LIST);
        lvMyDoctor.setAdapter(adapter);
        lvMyDoctor.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                final String doctor_id = mDatas.get(position).getRcfdDoctorListDTO().getDoctor_id();
                delMyDoctorDialog = new RCDialog(mActivity, new String[]{null, "您确定要删除吗？", "", getResources().getString(R.string.base_delete)}, null, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        delMyDoctor(doctor_id, position);
                        delMyDoctorDialog.dismiss();
                    }
                });
                delMyDoctorDialog.show();
                return true;
            }
        });

        lvMyDoctor.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FamilyDoctorIntroducedActivity.goActivity(mActivity,
                        mDatas.get(position).getRcfdDoctorListDTO().getDoctor_id() + "", phoneNumber);
            }
        });


        pullOnLoading = new PullOnLoading(mActivity, lvMyDoctor);
        pullOnLoading.setOnPullOnLoadingLintener(new PullOnLoading.OnPullOnLoadingLintener() {
            @Override
            public void loading() {
                loadingData();
            }
        });
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
        wwzImpl.getMyDoctorList(pn, new RCFDGetDoctorListListener() {
            @Override
            public void getDataSuccess(List<RCFDDoctorListDTO> doctorListDTO) {

                if (pn == 0) {
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

                showView();
                pullOnLoading.loadOver(doctorListDTO.size() == 20);
                adapter.notifyDataSetChanged();
                mRcHandler.sendMessage(RCHandler.GETDATA_OK);
            }

            @Override
            public void getDataError(int status, String msg) {
                showView();
                mRcHandler.sendMessage(RCHandler.GETDATA_OK);
            }
        });
    }

    private void showView() {
        if (mDatas.size() > 0) {
            lvMyDoctor.setVisibility(View.VISIBLE);
            ivMyDoctor.setVisibility(View.GONE);
            tvMyDoctorAdd.setVisibility(View.GONE);
        } else {
            lvMyDoctor.setVisibility(View.GONE);
            ivMyDoctor.setVisibility(View.VISIBLE);
            ivMyDoctor.setBackgroundResource(((FamilyDoctorActivity) mActivity).getiPlatformUtil().getMyDoctorEmptyImg());
            tvMyDoctorAdd.setVisibility(View.VISIBLE);
        }
    }

    private void delMyDoctor(String doctorID, final int position) {
        mRcHandler.sendMessage(RCHandler.START);
        wwzImpl.deleteMyDoctor(doctorID, new RCFDPostListener() {
            @Override
            public void getDataSuccess() {
                mDatas.remove(position);
                adapter.notifyDataSetChanged();
                showView();
                mRcHandler.sendMessage(RCHandler.GETDATA_OK);
            }

            @Override
            public void getDataError(int status, String msg) {
                mRcHandler.sendMessage(RCHandler.GETDATA_OK);
            }
        });

    }

}
