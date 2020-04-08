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

import com.rocedar.lib.base.network.IRCPostListener;
import com.rocedar.lib.base.unit.RCDialog;
import com.rocedar.lib.base.unit.RCDrawableUtil;
import com.rocedar.lib.base.unit.RCHandler;
import com.rocedar.lib.base.unit.RCJavaUtil;
import com.rocedar.lib.base.view.loading.PullOnLoading;
import com.rocedar.sdk.familydoctor.R;
import com.rocedar.sdk.familydoctor.app.RCFDSpecialistIntroducedActivity;
import com.rocedar.sdk.familydoctor.app.adapter.FDConsultListAdapter;
import com.rocedar.sdk.familydoctor.app.eventbean.EventFDBaseBean;
import com.rocedar.sdk.familydoctor.app.eventbean.EventFDDeleteBean;
import com.rocedar.sdk.familydoctor.config.RCFDConfigUtil;
import com.rocedar.sdk.familydoctor.dto.RCFDDoctorListDTO;
import com.rocedar.sdk.familydoctor.dto.RCFDListDTO;
import com.rocedar.sdk.familydoctor.request.listener.fd.RCFDGetDoctorListListener;

import org.greenrobot.eventbus.EventBus;

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

public class RCFDSpecialistMyDoctorFragment extends RCFDSpecialistBaseFragment {

    private TextView tvMyDoctorAdd;
    private ListView lvMyDoctor;
    private ImageView ivMyDoctor;
    private PtrClassicFrameLayout pull_to_refresh;

    private RCDialog delMyDoctorDialog;
    private PullOnLoading pullOnLoading;

    private FDConsultListAdapter adapter;
    //页码
    private int pn = 0;
    //医生列表数据
    private ArrayList<RCFDListDTO> doctorListDTOS = new ArrayList<>();

    public static RCFDSpecialistMyDoctorFragment newInstance(String phone, String deviceNumbers) {
        RCFDSpecialistMyDoctorFragment fragment = new RCFDSpecialistMyDoctorFragment();
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
        View view = inflater.inflate(R.layout.rc_fd_fragment_specialist_my_doctor, null);
        initView(view);
        //加载数据
        loadData();
        return view;
    }

    /**
     * 刷新数据，用于在列表中添加我的医生后刷新数据
     */
    public void refreshDate() {
        pn = 0;
        loadData();
    }

    private void initView(View view) {
        tvMyDoctorAdd = (TextView) view.findViewById(R.id.tv_my_doctor_add);
        tvMyDoctorAdd.setBackground(RCDrawableUtil.getMainColorDrawableBaseRadius(mActivity));
        lvMyDoctor = (ListView) view.findViewById(R.id.lv_my_doctor);
        ivMyDoctor = (ImageView) view.findViewById(R.id.iv_my_doctor);
        if (RCFDConfigUtil.getConfig().imageResFDMYDoctorDefault() > 0) {
            ivMyDoctor.setImageResource(RCFDConfigUtil.getConfig().imageResFDMYDoctorDefault());
        }

        pull_to_refresh = (PtrClassicFrameLayout) view.findViewById(R.id.pull_to_refresh);

        //添加医生处理
        tvMyDoctorAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //显示医生列表页面
                EventBus.getDefault().post(
                        new EventFDBaseBean(EventFDBaseBean.FD_EVENT_TYPE_SHOW_OFFICE));
            }
        });


        lvMyDoctor.setFocusable(false);
        adapter = new FDConsultListAdapter(this, doctorListDTOS,
                FDConsultListAdapter.MY_DOCTOR_LIST);
        lvMyDoctor.setAdapter(adapter);
        lvMyDoctor.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                //长按删除医生
                final String doctor_id = doctorListDTOS.get(position).getRcfdDoctorListDTO().getDoctor_id();
                delMyDoctorDialog = new RCDialog(mActivity, new String[]{null, "您确定要删除吗？", "", getResources().getString(R.string.rc_delete)}, null, new View.OnClickListener() {
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
                RCFDSpecialistIntroducedActivity.goActivity(mActivity,
                        doctorListDTOS.get(position).getRcfdDoctorListDTO().getDoctor_id() + "",
                        mPhoneNumber, mDeviceNumber);
            }
        });


        //下拉刷新处理
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
                loadData();
            }
        });
        //上拉加载处理
        pullOnLoading = new PullOnLoading(mActivity, lvMyDoctor);
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
        doctorRequest.getMyDoctorList(pn, new RCFDGetDoctorListListener() {
            @Override
            public void getDataSuccess(List<RCFDDoctorListDTO> doctorListDTO) {
                if (pn == 0) {
                    pull_to_refresh.refreshComplete();
                    doctorListDTOS.clear();
                }
                for (int i = 0; i < doctorListDTO.size(); i++) {
                    RCFDListDTO dto = new RCFDListDTO();
                    dto.setOpen(true);
                    dto.setHasOpen(RCJavaUtil.textCount(doctorListDTO.get(i).getSkilled(), 1, 109, 12));
                    dto.setRcfdDoctorListDTO(doctorListDTO.get(i));
                    doctorListDTOS.add(dto);
                }
                pn++;
                showView();
                pullOnLoading.loadOver(doctorListDTO.size() == 20);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void getDataError(int status, String msg) {
                showView();
            }
        });
    }

    private void showView() {
        if (doctorListDTOS.size() > 0) {
            pull_to_refresh.setVisibility(View.VISIBLE);
            ivMyDoctor.setVisibility(View.GONE);
            tvMyDoctorAdd.setVisibility(View.GONE);
        } else {
            pull_to_refresh.setVisibility(View.GONE);
            ivMyDoctor.setVisibility(View.VISIBLE);
            tvMyDoctorAdd.setVisibility(View.VISIBLE);
        }
    }

    private void delMyDoctor(final String doctorID, final int position) {
        mRcHandler.sendMessage(RCHandler.START);
        doctorRequest.deleteMyDoctor(doctorID, new IRCPostListener() {
            @Override
            public void getDataSuccess() {
                doctorListDTOS.remove(position);
                adapter.notifyDataSetChanged();
                showView();
                EventFDDeleteBean deleteBean = new EventFDDeleteBean(EventFDBaseBean.FD_EVENT_TYPE_DO_DELETE_MY_DOCTOR);
                deleteBean.setDoctor_id(doctorID);
                EventBus.getDefault().post(deleteBean);
                mRcHandler.sendMessage(RCHandler.GETDATA_OK);
            }

            @Override
            public void getDataError(int status, String msg) {
                mRcHandler.sendMessage(RCHandler.GETDATA_OK);
            }
        });

    }

}
