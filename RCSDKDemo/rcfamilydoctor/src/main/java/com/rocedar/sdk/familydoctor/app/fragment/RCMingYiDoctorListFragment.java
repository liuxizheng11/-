package com.rocedar.sdk.familydoctor.app.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.rocedar.lib.base.manage.RCBaseFragment;
import com.rocedar.lib.base.unit.RCHandler;
import com.rocedar.lib.base.view.loading.PullOnLoading;
import com.rocedar.sdk.familydoctor.R;
import com.rocedar.sdk.familydoctor.app.RCFDSpecialistIntroducedActivity;
import com.rocedar.sdk.familydoctor.app.adapter.FDConsultListAdapter;
import com.rocedar.sdk.familydoctor.app.adapter.RCMingYiDoctorListAdapter;
import com.rocedar.sdk.familydoctor.dto.mingyi.RCMIngYiDoctorListDTO;
import com.rocedar.sdk.familydoctor.request.impl.RCMingYiRequestImpl;
import com.rocedar.sdk.familydoctor.request.listener.mingyi.RCMingYiDoctorListListener;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * 作者：lxz
 * 日期：2018/7/24 下午2:30
 * 版本：V1.0
 * 描述：名医列表页面
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class RCMingYiDoctorListFragment extends RCBaseFragment {

    private String department = "";
    private String fee = "";
    private String hospital = "";

    private int pn = 0;
    private ListView rc_mingyi_fragment_doctor_list;

    private RCMingYiRequestImpl rcMingYiRequest;

    private RCMingYiDoctorListAdapter doctorListAdapter;
    private PtrClassicFrameLayout rc_mingyi_fragment_doctor_pull_to_refresh;
    private PullOnLoading pullOnLoading;

    private LinearLayout rc_family_include_no_data_ll;
    private List<RCMIngYiDoctorListDTO> mAllData = new ArrayList<>();

    public static RCMingYiDoctorListFragment newInstance() {
        Bundle args = new Bundle();
        RCMingYiDoctorListFragment fragment = new RCMingYiDoctorListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static RCMingYiDoctorListFragment newInstance(String department, String fee, String hospital) {
        Bundle args = new Bundle();
        args.putString("department", department);
        args.putString("fee", fee);
        args.putString("hospital", hospital);
        RCMingYiDoctorListFragment fragment = new RCMingYiDoctorListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.rc_mingyi_fragment_doctor_list_data, null);
        rcMingYiRequest = new RCMingYiRequestImpl(mActivity);
        initView(view);
        initAdapter();
        return view;

    }

    private void initView(View view) {
        rc_mingyi_fragment_doctor_list = view.findViewById(R.id.rc_mingyi_fragment_doctor_list);
        rc_family_include_no_data_ll = view.findViewById(R.id.rc_family_include_no_data_ll);
        rc_mingyi_fragment_doctor_pull_to_refresh = view.findViewById(R.id.rc_mingyi_fragment_doctor_pull_to_refresh);

        rc_mingyi_fragment_doctor_pull_to_refresh.setPtrHandler(new PtrHandler() {
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
                loadData(department, fee, hospital,false);
            }
        });
    }

    /**
     * @param department_data 科室 ID
     * @param fee_data        价格ID
     * @param hospital_data   医院ID
     */
    public void loadData(String department_data, String fee_data, String hospital_data,boolean isClick) {
        if (isClick) {
            department = department_data;
            fee = fee_data;
            hospital = hospital_data;
            pn = 0;
        }
        mRcHandler.sendMessage(RCHandler.START);
        rcMingYiRequest.getDoctorListData(department, fee, hospital, pn, new RCMingYiDoctorListListener() {
            @Override
            public void getDataSuccess(List<RCMIngYiDoctorListDTO> doctorListDTOS) {

                if (pn == 0 && mAllData.size() > 0) {
                    mAllData.clear();
                }
                for (int i = 0; i < doctorListDTOS.size(); i++) {
                    RCMIngYiDoctorListDTO mDTO = doctorListDTOS.get(i);
                    mAllData.add(mDTO);
                }
                isshowEmpty();
                pullOnLoading.loadOver(doctorListDTOS.size() == 20);
                if (pn == 0) {
                    doctorListAdapter.notifyDataSetInvalidated();
                    rc_mingyi_fragment_doctor_pull_to_refresh.refreshComplete();
                } else {
                    doctorListAdapter.notifyDataSetChanged();
                }
                pn++;
                mRcHandler.sendMessage(RCHandler.GETDATA_OK);
            }

            @Override
            public void getDataError(int status, String msg) {
                isshowEmpty();
                if (pn == 0) {
                    rc_mingyi_fragment_doctor_pull_to_refresh.refreshComplete();
                }
                mRcHandler.sendMessage(RCHandler.GETDATA_OK);
            }
        });
    }

    private void initAdapter() {
        doctorListAdapter = new RCMingYiDoctorListAdapter(mActivity, mAllData);
        rc_mingyi_fragment_doctor_list.setAdapter(doctorListAdapter);
        rc_mingyi_fragment_doctor_list.setFocusable(false);
        doctorListAdapter.notifyDataSetChanged();
        pullOnLoading = new PullOnLoading(mActivity, rc_mingyi_fragment_doctor_list);
        pullOnLoading.setOnPullOnLoadingLintener(new PullOnLoading.OnPullOnLoadingListener() {
            @Override
            public void loading() {
                loadData(department, fee, hospital,false);
            }
        });
        loadData(department, fee, hospital,false);
    }

    /**
     * 空页面展示
     */
    private void isshowEmpty() {
        if (mAllData.size() > 0) {
            rc_mingyi_fragment_doctor_list.setVisibility(View.VISIBLE);
            rc_family_include_no_data_ll.setVisibility(View.GONE);
        } else {
            rc_family_include_no_data_ll.setVisibility(View.VISIBLE);
            rc_mingyi_fragment_doctor_list.setVisibility(View.GONE);
        }
    }

}
