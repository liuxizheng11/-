package com.rocedar.sdk.familydoctor.app.fragment;

import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rocedar.lib.base.unit.RCAndroid;
import com.rocedar.lib.base.unit.RCHandler;
import com.rocedar.sdk.familydoctor.R;
import com.rocedar.sdk.familydoctor.app.adapter.PopWindowDoctorOfficeGridAdapter;
import com.rocedar.sdk.familydoctor.app.eventbean.EventFDAddBean;
import com.rocedar.sdk.familydoctor.app.eventbean.EventFDAdvisoryBean;
import com.rocedar.sdk.familydoctor.app.eventbean.EventFDBaseBean;
import com.rocedar.sdk.familydoctor.app.eventbean.EventFDDeleteBean;
import com.rocedar.sdk.familydoctor.dto.RCFDDepartmentDTO;
import com.rocedar.sdk.familydoctor.request.listener.fd.RCFDGetDepartmentListener;
import com.rocedar.sdk.familydoctor.view.MyPopupWindow;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名称：平台库-行为数据
 * <p>
 * 作者：phj
 * 日期：2018/2/9 下午5:55
 * 版本：V1.0.00
 * 描述：
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public class RCFDSpecialistFragment<T extends EventFDBaseBean> extends RCFDSpecialistBaseFragment implements View.OnClickListener {

    public static RCFDSpecialistFragment familyDoctorMainFragment;

    public static RCFDSpecialistFragment newInstance(String phone, String deviceNumbers) {
        Bundle args = new Bundle();
        args.putString(KEY_PHONENO, phone);
        args.putString(KEY_DEVICENO, deviceNumbers);
        RCFDSpecialistFragment fragment = new RCFDSpecialistFragment();
        fragment.setArguments(args);
        familyDoctorMainFragment = fragment;
        return fragment;
    }

    //当前显示的Fragment对象
    private int showFragmentIndex = -1;
    //科室列表索引
    private static final int OFFICE_SCREEN = 0;
    //咨询记录索引
    private static final int CONSULT_RECORD = 1;
    //我的医生索引
    private static final int MY_DOCTOR = 2;

    //科室列表Fragment对象
    private RCFDSpecialistOfficeScreenFragment familyDoctorOfficeScreenFragment;
    //咨询记录Fragment对象
    private RCFDSpecialistRecordFragment familyDoctorConsultRecordFragment;
    //我的医生Fragment对象
    private RCFDSpecialistMyDoctorFragment familyDoctorMyDoctorFragment;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.rc_fd_activity_specialist_main, null);
        initView(view);
        getDoctorOfficeList();
        EventBus.getDefault().register(this);
        return view;
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(T event) {
        switch (event.getType()) {
            case EventFDBaseBean.FD_EVENT_TYPE_SHOW_OFFICE:
                showOfficeScreenFragment();
                break;
            case EventFDBaseBean.FD_EVENT_TYPE_DO_START_ADVISORY:
                if (event instanceof EventFDAdvisoryBean) {
                    EventFDAdvisoryBean dto = (EventFDAdvisoryBean) event;
                    doStartAdvisory(dto.doctor_id, dto.portrait, dto.doctor_name, dto.title_name, dto.department);
                }
                break;
            case EventFDBaseBean.FD_EVENT_TYPE_DO_REFRESH_MY_DOCTOR:
                if (familyDoctorMyDoctorFragment != null) {
                    familyDoctorMyDoctorFragment.refreshDate();
                }
                break;
            case EventFDBaseBean.FD_EVENT_TYPE_DO_DELETE_MY_DOCTOR:
                if (familyDoctorOfficeScreenFragment != null)
                    if (event instanceof EventFDDeleteBean) {
                        EventFDDeleteBean dto = (EventFDDeleteBean) event;
                        familyDoctorOfficeScreenFragment.deleteMyDoctor(dto.getDoctor_id());
                    }
                break;
            case EventFDBaseBean.FD_EVENT_TYPE_DO_ADD_MY_DOCTOR:
                if (familyDoctorMyDoctorFragment != null) {
                    familyDoctorMyDoctorFragment.refreshDate();
                }
                if (familyDoctorOfficeScreenFragment != null)
                    if (event instanceof EventFDAddBean) {
                        EventFDAddBean dto = (EventFDAddBean) event;
                        familyDoctorOfficeScreenFragment.addMyDoctor(dto.getDoctor_id());
                    }
                break;
        }
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    private TextView tvDoctorOfficeScreen;
    private ImageView ivDoctorOfficeScreen;
    private RelativeLayout rlDoctorOfficeScreen;
    private TextView tvDoctorConsultRecord;
    private TextView tvDoctorMyDoctor;
    private FrameLayout flDoctorContainer;
    private LinearLayout headLayout;


    private void initView(View view) {
        headLayout = (LinearLayout) view.findViewById(R.id.rl_doctor_office_headlayout);
        tvDoctorOfficeScreen = (TextView) view.findViewById(R.id.tv_doctor_office_screen);
        ivDoctorOfficeScreen = (ImageView) view.findViewById(R.id.iv_doctor_office_screen);
        rlDoctorOfficeScreen = (RelativeLayout) view.findViewById(R.id.rl_doctor_office_screen);
        tvDoctorConsultRecord = (TextView) view.findViewById(R.id.tv_doctor_consult_record);
        tvDoctorMyDoctor = (TextView) view.findViewById(R.id.tv_doctor_my_doctor);
        flDoctorContainer = (FrameLayout) view.findViewById(R.id.fl_doctor_container);
        tvDoctorOfficeScreen.setOnClickListener(this);
        ivDoctorOfficeScreen.setOnClickListener(this);
        rlDoctorOfficeScreen.setOnClickListener(this);
        tvDoctorConsultRecord.setOnClickListener(this);
        tvDoctorMyDoctor.setOnClickListener(this);
        flDoctorContainer.setOnClickListener(this);
        //显示医生列表
        showOfficeScreenFragment();
        //初始化选择医生弹框
        initChooseOfficePopWindow();

    }


    //科室选择弹出框
    private MyPopupWindow chooseDoctorOfficePopup;
    //科室选择弹出框列表适配器
    private PopWindowDoctorOfficeGridAdapter popWindowAdapter;
    //医生部门列表
    private ArrayList<RCFDDepartmentDTO> mDepartmentDTOs = new ArrayList<>();

    /**
     * 初始化科室选择弹出框
     */
    private void initChooseOfficePopWindow() {
        if (chooseDoctorOfficePopup == null) {
            View popWindow = LayoutInflater.from(mActivity).inflate(R.layout.rc_fd_popwindow_officelist, null);
            popWindowAdapter = new PopWindowDoctorOfficeGridAdapter(mDepartmentDTOs, mActivity);
            GridView gv_family_doctor_office = (GridView) popWindow.findViewById(R.id.gv_family_doctor_office);
            gv_family_doctor_office.setAdapter(popWindowAdapter);
            gv_family_doctor_office.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    offChooseOfficePopWindow();
                    popWindowAdapter.mSelectPos = position;
                    familyDoctorOfficeScreenFragment.loadData(mDepartmentDTOs.get(position).getDepartment_id());
                    tvDoctorOfficeScreen.setText(mDepartmentDTOs.get(position).getDepartment_name());
                    popWindowAdapter.notifyDataSetInvalidated();
                }
            });
            chooseDoctorOfficePopup = new MyPopupWindow(popWindow, GridLayout.LayoutParams.MATCH_PARENT, GridLayout.LayoutParams.MATCH_PARENT);
            //设置背景颜色
            chooseDoctorOfficePopup.setBackgroundDrawable(new BitmapDrawable());
            // 使其聚集
//            chooseDoctorOfficePopup.setFocusable(true);
            // 设置允许在外点击消失
//            chooseDoctorOfficePopup.setOutsideTouchable(true);
            chooseDoctorOfficePopup.update();
        }
    }

    /**
     * 显示科室列表
     */
    private boolean showChooseOfficePopWindow() {
        if (chooseDoctorOfficePopup != null) {
            chooseDoctorOfficePopup.dismiss();
            ivDoctorOfficeScreen.setImageResource(R.mipmap.rc_fd_ic_packup);
            chooseDoctorOfficePopup.showAsDropDown(chooseDoctorOfficePopup, rlDoctorOfficeScreen, 0,
                    (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0,
                            mActivity.getResources().getDisplayMetrics()));
            return true;
        }
        return false;
    }

    /**
     * 关闭科室列表
     */
    private boolean offChooseOfficePopWindow() {
        if (chooseDoctorOfficePopup != null && chooseDoctorOfficePopup.isShowing()) {
            chooseDoctorOfficePopup.dismiss();
            ivDoctorOfficeScreen.setImageResource(R.mipmap.rc_fd_ic_unfold);
            return true;
        }
        return false;
    }


    /**
     * 加载医生科室列表数据
     */
    private void getDoctorOfficeList() {
        mRcHandler.sendMessage(RCHandler.START);
        doctorRequest.getDoctorDepartment(new RCFDGetDepartmentListener() {
            @Override
            public void getDataSuccess(List<RCFDDepartmentDTO> dto) {
                if (mDepartmentDTOs.size() > 0) {
                    mDepartmentDTOs.clear();
                }
                mDepartmentDTOs.addAll(dto);
                popWindowAdapter.notifyDataSetChanged();
                mRcHandler.sendMessage(RCHandler.GETDATA_OK);
            }

            @Override
            public void getDataError(int status, String msg) {
                mRcHandler.sendMessage(RCHandler.GETDATA_OK);
            }
        });
    }

    /**
     * 加载医生列表页面
     */
    private void showOfficeScreenFragment() {
        showContent(OFFICE_SCREEN);
    }


    /**
     * 设置被选中的字体颜色
     *
     * @param isSelectOne   科室筛选
     * @param isSelectTwo   咨询记录
     * @param isSelectThree 我的医生
     */
    private void setTextColor(boolean isSelectOne, boolean isSelectTwo, boolean isSelectThree) {
        if (isSelectOne) {
            tvDoctorOfficeScreen.setTextColor(RCAndroid.getAttColor(mActivity, R.attr.RCDarkColor));
        } else {
            tvDoctorOfficeScreen.setTextColor(Color.parseColor("#666666"));
        }
        if (isSelectTwo) {
            tvDoctorConsultRecord.setTextColor(RCAndroid.getAttColor(mActivity, R.attr.RCDarkColor));
        } else {
            tvDoctorConsultRecord.setTextColor(Color.parseColor("#666666"));
        }
        if (isSelectThree) {
            tvDoctorMyDoctor.setTextColor(RCAndroid.getAttColor(mActivity, R.attr.RCDarkColor));
        } else {
            tvDoctorMyDoctor.setTextColor(Color.parseColor("#666666"));
        }
    }


    /**
     * 返回处理
     *
     * @return
     */
    public boolean onBackPressed() {
        if (offChooseOfficePopWindow()) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.tv_doctor_office_screen || i == R.id.rl_doctor_office_screen) {
            if (showFragmentIndex == OFFICE_SCREEN) {
                if (!offChooseOfficePopWindow()) {
                    showChooseOfficePopWindow();
                }
            } else {
                showContent(OFFICE_SCREEN);
            }
        } else if (i == R.id.tv_doctor_consult_record) {
            if (chooseDoctorOfficePopup != null)
                chooseDoctorOfficePopup.dismiss();
            showContent(CONSULT_RECORD);
        } else if (i == R.id.tv_doctor_my_doctor) {
            if (chooseDoctorOfficePopup != null)
                chooseDoctorOfficePopup.dismiss();
            showContent(MY_DOCTOR);
        }
    }

    /**
     * 隐藏
     *
     * @param transaction
     */
    private void hideFragments(FragmentTransaction transaction) {
        if (familyDoctorOfficeScreenFragment != null) {
            transaction.hide(familyDoctorOfficeScreenFragment);
        }
        if (familyDoctorConsultRecordFragment != null) {
            transaction.hide(familyDoctorConsultRecordFragment);
        }
        if (familyDoctorMyDoctorFragment != null) {
            transaction.hide(familyDoctorMyDoctorFragment);
        }
    }


    private void showContent(int choose) {
        if (showFragmentIndex == choose) {
            return;
        }
        showFragmentIndex = choose;
        FragmentTransaction transaction = mActivity.getSupportFragmentManager().beginTransaction();
        // 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
        hideFragments(transaction);
        //选中状态
        setTextColor(choose == OFFICE_SCREEN, choose == CONSULT_RECORD,
                choose == MY_DOCTOR);
        switch (choose) {
            case OFFICE_SCREEN:
                if (familyDoctorOfficeScreenFragment == null) {
                    familyDoctorOfficeScreenFragment =
                            RCFDSpecialistOfficeScreenFragment.newInstance(mPhoneNumber, mDeviceNumber);
                    transaction.add(R.id.fl_doctor_container, familyDoctorOfficeScreenFragment);
                } else {
                    transaction.show(familyDoctorOfficeScreenFragment);
                }
                break;
            case CONSULT_RECORD:
                if (familyDoctorConsultRecordFragment == null) {
                    familyDoctorConsultRecordFragment =
                            RCFDSpecialistRecordFragment.newInstance(mPhoneNumber, mDeviceNumber);
                    transaction.add(R.id.fl_doctor_container, familyDoctorConsultRecordFragment);
                } else {
                    transaction.show(familyDoctorConsultRecordFragment);
                }
                break;
            case MY_DOCTOR:
                if (familyDoctorMyDoctorFragment == null) {
                    familyDoctorMyDoctorFragment =
                            RCFDSpecialistMyDoctorFragment.newInstance(mPhoneNumber, mDeviceNumber);
                    transaction.add(R.id.fl_doctor_container, familyDoctorMyDoctorFragment);
                } else {
                    transaction.show(familyDoctorMyDoctorFragment);
                }
                break;
        }
        transaction.commit();
    }


    /**
     * 开始咨询医生
     *
     * @param doctor_id   医生id
     * @param portrait    图片地址
     * @param doctor_name 医生姓名
     * @param title_name  医生职务
     * @param department  医师科室
     */
    public void doStartAdvisory(final String doctor_id, final String portrait, final String doctor_name,
                                final String title_name, final String department) {
        doctorWwzUtil.startAdvisory(doctor_id, portrait, doctor_name, title_name, department);
    }


}
