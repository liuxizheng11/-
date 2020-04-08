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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rocedar.lib.base.manage.RCBaseFragment;
import com.rocedar.lib.base.unit.RCDrawableUtil;
import com.rocedar.lib.base.unit.RCHandler;
import com.rocedar.sdk.familydoctor.R;
import com.rocedar.sdk.familydoctor.app.adapter.MingYiDoctorRootAdapter;
import com.rocedar.sdk.familydoctor.app.adapter.MingYiDoctorSubAdapter;
import com.rocedar.sdk.familydoctor.dto.mingyi.RCMingYiDoctorListSelectlDTO;
import com.rocedar.sdk.familydoctor.request.impl.RCMingYiRequestImpl;
import com.rocedar.sdk.familydoctor.request.listener.mingyi.RCMingYiDoctorListSelectListener;
import com.rocedar.sdk.familydoctor.view.MyPopupWindow;

import java.util.List;

/**
 * 项目名称：瑰柏SDK-家庭医生
 * <p>
 * 作者：
 * 日期：2018/7/18 上午9:45
 * 版本：V1.0.00
 * 描述：瑰柏SDK-家庭医生，名医医生列表
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class RCMingYiDoctorListMainFragment extends RCBaseFragment {
    private RCMingYiRequestImpl rcMingYiRequest;
    /**
     * 全部科室
     */
    private List<RCMingYiDoctorListSelectlDTO> mOfficeList;

    private RelativeLayout rc_rl_doctor_office_screen;
    private TextView rc_tv_doctor_office_screen;
    private ImageView rc_iv_doctor_office_screen;
    //科室选择弹出框
    private MyPopupWindow OfficePopup;
    //选择的 科室
    private String department = "";

    /**
     * 服务价格
     */
    private List<RCMingYiDoctorListSelectlDTO> mServiceMoneyList;
    private RelativeLayout rc_rl_doctor_consult_record;
    private TextView rc_tv_doctor_consult_record;
    private ImageView rc_iv_doctor_consult_record;
    //价格选择弹出框
    private MyPopupWindow ServiceMoneyPopup;
    //选择的 价格
    private String fee = "";

    /**
     * 全部医院
     */
    private List<RCMingYiDoctorListSelectlDTO> mHospitalList;
    private RelativeLayout rc_rl_doctor_all_hospital;
    private TextView rc_tv_doctor_all_hospital;
    private ImageView rc_iv_doctor_all_hospital;
    //医院选择弹出框
    private MyPopupWindow HospitalPopup;
    //选择的 医院
    private String hospital = "";

    //科室列表索引
    private static final int OFFICE_SCREEN = 0;
    //咨询记录索引
    private static final int SERVICE_MONEY = 1;
    //我的医生索引
    private static final int ALL_HOSPITAL = 2;

    private RCMingYiDoctorListFragment rcmIngYiDoctorListFragment;

    public static RCMingYiDoctorListMainFragment newInstance() {
        Bundle args = new Bundle();
        RCMingYiDoctorListMainFragment fragment = new RCMingYiDoctorListMainFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.rc_mingyi_fragment_doctor_list, null);
        rcMingYiRequest = new RCMingYiRequestImpl(mActivity);
        getDoctorSelectData(OFFICE_SCREEN);
        getDoctorSelectData(SERVICE_MONEY);
        getDoctorSelectData(ALL_HOSPITAL);
        rcmIngYiDoctorListFragment = RCMingYiDoctorListFragment.newInstance();
        initView(view);
        return view;
    }


    private void initView(View view) {

        rc_rl_doctor_office_screen = view.findViewById(R.id.rc_rl_doctor_office_screen);
        rc_tv_doctor_office_screen = view.findViewById(R.id.rc_tv_doctor_office_screen);
        rc_iv_doctor_office_screen = view.findViewById(R.id.rc_iv_doctor_office_screen);

        rc_rl_doctor_consult_record = view.findViewById(R.id.rc_rl_doctor_consult_record);
        rc_tv_doctor_consult_record = view.findViewById(R.id.rc_tv_doctor_consult_record);
        rc_iv_doctor_consult_record = view.findViewById(R.id.rc_iv_doctor_consult_record);

        rc_rl_doctor_all_hospital = view.findViewById(R.id.rc_rl_doctor_all_hospital);
        rc_tv_doctor_all_hospital = view.findViewById(R.id.rc_tv_doctor_all_hospital);
        rc_iv_doctor_all_hospital = view.findViewById(R.id.rc_iv_doctor_all_hospital);

        FragmentTransaction transaction = mActivity.getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.rc_fl_doctor_container, rcmIngYiDoctorListFragment);
        transaction.commit();
        //科室选择点击
        rc_rl_doctor_office_screen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTextColor(true, false, false);
                if (!offChoosePopWindow(OfficePopup)) {
                    showOfficeChoosePopWindow(OfficePopup);
                }
                if (ServiceMoneyPopup != null) {
                    ServiceMoneyPopup.dismiss();
                }
                if (HospitalPopup != null) {
                    HospitalPopup.dismiss();
                }

            }
        });
        //价格选择 点击
        rc_rl_doctor_consult_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTextColor(false, true, false);

                if (!offChoosePopWindow(ServiceMoneyPopup)) {
                    showOfficeChoosePopWindow(ServiceMoneyPopup);
                }
                if (OfficePopup != null) {
                    OfficePopup.dismiss();
                }
                if (HospitalPopup != null) {
                    HospitalPopup.dismiss();
                }
            }
        });
        //医院选择 点击
        rc_rl_doctor_all_hospital.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTextColor(false, false, true);

                if (!offChoosePopWindow(HospitalPopup)) {
                    showOfficeChoosePopWindow(HospitalPopup);
                }
                if (OfficePopup != null) {
                    OfficePopup.dismiss();
                }
                if (ServiceMoneyPopup != null) {
                    ServiceMoneyPopup.dismiss();
                }
            }
        });
    }

    /**
     * 根目录被选中的节点
     */
    //科室选择 possion
    private int selectOfficeScreenPosition;
    //价格选择 possion
    private int selectSeviceMoneyPosition;
    //医院选择 possion
    private int selectAllHospitalPosition;

    /**
     * 初始化 选择弹出框
     *
     * @param dtoList 列表数据
     * @param status  当前状态
     */
    private MyPopupWindow initChooseOfficePopWindow(final List<RCMingYiDoctorListSelectlDTO> dtoList, final int status) {
        MyPopupWindow myPopupWindow = null;

        if (myPopupWindow == null) {
            final View popupLayout = LayoutInflater.from(mActivity).inflate(R.layout.rc_mingyi_doctor_popuwindow, null);

            /**根目录*/
            ListView rootListView = popupLayout.findViewById(R.id.rc_fragment_mingyi_doctor_root_listview);
            final MingYiDoctorRootAdapter adapter = new MingYiDoctorRootAdapter(mActivity);
            adapter.setItems(dtoList);
            rootListView.setAdapter(adapter);
            adapter.notifyDataSetChanged();


            /**子目录*/
            FrameLayout subLayout = popupLayout.findViewById(R.id.rc_fragment_mingyi_doctor_sub);
            final ListView subListView = popupLayout.findViewById(R.id.rc_fragment_mingyi_doctor_sub_listview);
            if (status == SERVICE_MONEY) {
                subLayout.setVisibility(View.GONE);
            } else {
                subLayout.setVisibility(View.VISIBLE);
            }
            rootListView
                    .setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> parent, View view,
                                                int position, long id) {
                            selectSeviceMoneyPosition = position;
                            if (status != SERVICE_MONEY) {
                                /**
                                 *
                                 * 科室选择、医院选择
                                 */
                                final MingYiDoctorSubAdapter subAdapter = new MingYiDoctorSubAdapter(
                                        mActivity, dtoList.get(position).getChildsDTOS());
                                subListView.setAdapter(subAdapter);
                                if (status == OFFICE_SCREEN) {
                                    selectOfficeScreenPosition=position;
                                    for (int i = 0; i < dtoList.get(position).getChildsDTOS().size(); i++) {
                                        if (department.equals(dtoList.get(selectOfficeScreenPosition).getId() + ","
                                                + dtoList.get(selectOfficeScreenPosition).getChildsDTOS().get(i).getId())
                                                ) {
                                            subAdapter.setSelectedPosition(dtoList.get(selectOfficeScreenPosition).getChildsDTOS().get(i).getId());
                                        }
                                    }
                                } else {
                                    selectAllHospitalPosition=position;
                                    for (int i = 0; i < dtoList.get(position).getChildsDTOS().size(); i++) {
                                        if (hospital.equals(dtoList.get(selectAllHospitalPosition).getId() + ","
                                                + dtoList.get(selectAllHospitalPosition).getChildsDTOS().get(i).getId())
                                                ) {
                                            subAdapter.setSelectedPosition(dtoList.get(selectAllHospitalPosition).getChildsDTOS().get(i).getId());
                                        }
                                    }
                                }
                                subListView
                                        .setOnItemClickListener(new AdapterView.OnItemClickListener() {

                                            @Override
                                            public void onItemClick(
                                                    AdapterView<?> parent, View view, int position, long id) {
                                                //科室选择
                                                if (status == OFFICE_SCREEN) {
                                                    subAdapter.setSelectedPosition(dtoList.get(selectOfficeScreenPosition).getChildsDTOS().get(position).getId());
                                                    department = dtoList.get(selectOfficeScreenPosition).getId() + "," +
                                                            dtoList.get(selectOfficeScreenPosition).getChildsDTOS().get(position).getId() + "";
                                                    offChoosePopWindow(OfficePopup);
                                                    //回显到 Title
                                                    if (dtoList.get(selectOfficeScreenPosition).getChildsDTOS().get(position).getName().equals("全部医生")) {
                                                        rc_tv_doctor_office_screen.setText(dtoList.get(selectOfficeScreenPosition).getName());
                                                    } else {
                                                        rc_tv_doctor_office_screen.setText(dtoList.get(selectOfficeScreenPosition).getChildsDTOS().get(position).getName());
                                                    }
                                                    //医院选择
                                                } else if (status == ALL_HOSPITAL) {
                                                    subAdapter.setSelectedPosition(dtoList.get(selectAllHospitalPosition).getChildsDTOS().get(position).getId());
                                                    hospital = dtoList.get(selectAllHospitalPosition).getId() + "," +
                                                            dtoList.get(selectAllHospitalPosition).getChildsDTOS().get(position).getId() + "";
                                                    offChoosePopWindow(HospitalPopup);
                                                    //回显到 Title
                                                    if (dtoList.get(selectAllHospitalPosition).getChildsDTOS().get(position).getName().equals("全部医院")) {
                                                        rc_tv_doctor_all_hospital.setText(dtoList.get(selectAllHospitalPosition).getName());
                                                    } else {
                                                        rc_tv_doctor_all_hospital.setText(dtoList.get(selectAllHospitalPosition).getChildsDTOS().get(position).getName());
                                                    }
                                                }
                                                rcmIngYiDoctorListFragment.loadData(department, fee, hospital, true);
                                            }
                                        });
                            } else {
                                /**
                                 * 价格选择
                                 */
                                fee = dtoList.get(position).getId() + "";
                                rcmIngYiDoctorListFragment.loadData(department, fee, hospital, true);
                                offChoosePopWindow(ServiceMoneyPopup);
                                //回显到 Title
                                rc_tv_doctor_consult_record.setText(dtoList.get(selectSeviceMoneyPosition).getName());
                            }
                            /**
                             * 选中root某项时改变该ListView item的背景色
                             */
                            adapter.setSelectedPosition(position);
                            adapter.notifyDataSetInvalidated();
                        }
                    });

            myPopupWindow = new MyPopupWindow(popupLayout, GridLayout.LayoutParams.MATCH_PARENT, GridLayout.LayoutParams.MATCH_PARENT);
            //设置背景颜色
            myPopupWindow.setBackgroundDrawable(new BitmapDrawable());
            // 使其聚集
            //  window.setFocusable(true);
            // 设置允许在外点击消失
            // window.setOutsideTouchable(true);
            myPopupWindow.update();
        }
        return myPopupWindow;
    }

    /**
     * 显示 科室PopuWindow
     */
    private boolean showOfficeChoosePopWindow(MyPopupWindow mPopupWindow) {
        if (mPopupWindow != null) {
            mPopupWindow.dismiss();
            mPopupWindow.showAsDropDown(mPopupWindow, rc_rl_doctor_office_screen, 0,
                    (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0,
                            mActivity.getResources().getDisplayMetrics()));
            return true;
        }
        return false;
    }

    /**
     * 关闭 PopuWindow
     */
    private boolean offChoosePopWindow(MyPopupWindow mPopupWindow) {
        if (mPopupWindow != null && mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
            return true;
        }
        return false;
    }

    /**
     * 设置被选中的字体颜色
     *
     * @param isSelectOne   科室筛选
     * @param isSelectTwo   服务价格
     * @param isSelectThree 全部医院
     */
    private void setTextColor(boolean isSelectOne, boolean isSelectTwo, boolean isSelectThree) {
        if (isSelectOne) {
            rc_tv_doctor_office_screen.setTextColor(
                    RCDrawableUtil.getThemeAttrColor(mActivity, R.attr.RCDarkColor));
            rc_iv_doctor_office_screen.setImageResource(R.mipmap.rc_fd_ic_unfold);
        } else {
            rc_tv_doctor_office_screen.setTextColor(Color.parseColor("#666666"));
            rc_iv_doctor_office_screen.setImageResource(R.mipmap.rc_fd_ic_packup);
        }

        if (isSelectTwo) {
            rc_tv_doctor_consult_record.setTextColor(
                    RCDrawableUtil.getThemeAttrColor(mActivity, R.attr.RCDarkColor));
            rc_iv_doctor_consult_record.setImageResource(R.mipmap.rc_fd_ic_unfold);

        } else {
            rc_tv_doctor_consult_record.setTextColor(Color.parseColor("#666666"));
            rc_iv_doctor_consult_record.setImageResource(R.mipmap.rc_fd_ic_packup);
        }

        if (isSelectThree) {
            rc_tv_doctor_all_hospital.setTextColor(
                    RCDrawableUtil.getThemeAttrColor(mActivity, R.attr.RCDarkColor));
            rc_iv_doctor_all_hospital.setImageResource(R.mipmap.rc_fd_ic_unfold);
        } else {
            rc_tv_doctor_all_hospital.setTextColor(Color.parseColor("#666666"));
            rc_iv_doctor_all_hospital.setImageResource(R.mipmap.rc_fd_ic_packup);
        }
    }

    /**
     * 获取 一级、二级 数据
     */
    private void getDoctorSelectData(final int status) {
        mRcHandler.sendMessage(RCHandler.START);
        rcMingYiRequest.getDoctorListSelectData(status, new RCMingYiDoctorListSelectListener() {
            @Override
            public void getDataSuccess(List<RCMingYiDoctorListSelectlDTO> doctorListSelectlDTO) {
                switch (status) {
                    case OFFICE_SCREEN:
                        //科室选择
                        mOfficeList = doctorListSelectlDTO;
                        OfficePopup = initChooseOfficePopWindow(mOfficeList, OFFICE_SCREEN);
                        break;
                    case SERVICE_MONEY:
                        //服务价格
                        mServiceMoneyList = doctorListSelectlDTO;
                        ServiceMoneyPopup = initChooseOfficePopWindow(mServiceMoneyList, SERVICE_MONEY);
                        break;
                    case ALL_HOSPITAL:
                        //全部医院
                        mHospitalList = doctorListSelectlDTO;
                        HospitalPopup = initChooseOfficePopWindow(mHospitalList, ALL_HOSPITAL);
                        break;
                }
                mRcHandler.sendMessage(RCHandler.GETDATA_OK);
            }

            @Override
            public void getDataError(int status, String msg) {
                mRcHandler.sendMessage(RCHandler.GETDATA_OK);
            }
        });
    }
}
