package com.rocedar.deviceplatform.app.familydoctor;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rocedar.base.RCHandler;
import com.rocedar.base.RCLog;
import com.rocedar.base.manger.RCBaseActivity;
import com.rocedar.deviceplatform.R;
import com.rocedar.deviceplatform.app.familydoctor.adapter.PopWindowDoctorOfficeGridAdapter;
import com.rocedar.deviceplatform.app.familydoctor.fragment.FamilyDoctorConsultRecordFragment;
import com.rocedar.deviceplatform.app.familydoctor.fragment.FamilyDoctorMyDoctorFragment;
import com.rocedar.deviceplatform.app.familydoctor.fragment.FamilyDoctorOfficeScreenFragment;
import com.rocedar.deviceplatform.app.view.MyPopupWindow;
import com.rocedar.deviceplatform.device.other.RCFamilyDoctorWwzUtil;
import com.rocedar.deviceplatform.dto.familydoctor.RCFDDepartmentDTO;
import com.rocedar.deviceplatform.request.impl.RCFamilyDoctorWWZImpl;
import com.rocedar.deviceplatform.request.listener.familydoctor.RCFDGetDepartmentListener;
import com.rocedar.deviceplatform.unit.ReadPlatformConfig;

import java.util.ArrayList;
import java.util.List;


/**
 * @author liuyi
 * @date 2017/4/18
 * @desc 家庭医生
 * @veison V3.4.00新增
 */
public class FamilyDoctorActivity extends RCBaseActivity implements View.OnClickListener {


    public static void goActivity(Context context, String phone) {
        Intent intent = new Intent(context, FamilyDoctorActivity.class);
        intent.putExtra("phone", phone);
        context.startActivity(intent);
    }


    private MyPopupWindow chooseDoctorOfficePopup;
    private PopWindowDoctorOfficeGridAdapter popWindowAdapter;
    public FamilyDoctorOfficeScreenFragment familyDoctorOfficeScreenFragment;
    FamilyDoctorConsultRecordFragment familyDoctorConsultRecordFragment;
    FamilyDoctorMyDoctorFragment familyDoctorMyDoctorFragment;

    private RCFamilyDoctorWWZImpl wwzImpl;


    //展开时的图标
    private int chooseUnfold;
    //收起时的图标
    private int choosePackup;
    //不可选时的图标
    private int chooseNone;

    private TextView tvDoctorOfficeScreen;
    private ImageView ivDoctorOfficeScreen;
    private RelativeLayout rlDoctorOfficeScreen;
    private TextView tvDoctorConsultRecord;
    private TextView tvDoctorMyDoctor;
    private FrameLayout flDoctorContainer;
    private LinearLayout headLayout;

    private String phone;

    private IFamilyDoctorPlatformUtil iPlatformUtil;

    public IFamilyDoctorPlatformUtil getiPlatformUtil() {
        return iPlatformUtil;
    }

    //微问诊工具类
    private RCFamilyDoctorWwzUtil wwzUtil;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family_doctor);
        mRcHeadUtil.setTitle(getString(R.string.rcdevice_family_doctor));

        phone = String.valueOf(getIntent().getLongExtra("phone", -1));

        if (!ReadPlatformConfig.getFamilyDoctorClass().equals("")) {
            try {
                String className = ReadPlatformConfig.getFamilyDoctorClass();
                iPlatformUtil = (IFamilyDoctorPlatformUtil) Class.forName(className).newInstance();
            } catch (Exception e) {
                e.printStackTrace();
                RCLog.e(TAG, "类对象获取失败");
            }
        }
        if (iPlatformUtil == null) {
            iPlatformUtil = new FamilyDoctorPlatformUtilBaseImpl();
        }

        wwzImpl = new RCFamilyDoctorWWZImpl(mContext);
        wwzUtil = new RCFamilyDoctorWwzUtil(mContext);

        chooseUnfold = iPlatformUtil.getChooseOfficeSelected();
        choosePackup = iPlatformUtil.getChooseOfficeRetract();
        chooseNone = iPlatformUtil.getChooseOfficeNone();

        initView();

        getDoctorOfficeList();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        wwzUtil.onDestory();
    }

    private ArrayList<RCFDDepartmentDTO> mDatas = new ArrayList<>();

    private void initView() {
        headLayout = (LinearLayout) findViewById(R.id.rl_doctor_office_headlayout);
        tvDoctorOfficeScreen = (TextView) findViewById(R.id.tv_doctor_office_screen);
        ivDoctorOfficeScreen = (ImageView) findViewById(R.id.iv_doctor_office_screen);
        rlDoctorOfficeScreen = (RelativeLayout) findViewById(R.id.rl_doctor_office_screen);
        tvDoctorConsultRecord = (TextView) findViewById(R.id.tv_doctor_consult_record);
        tvDoctorMyDoctor = (TextView) findViewById(R.id.tv_doctor_my_doctor);
        flDoctorContainer = (FrameLayout) findViewById(R.id.fl_doctor_container);
        tvDoctorOfficeScreen.setOnClickListener(this);
        ivDoctorOfficeScreen.setOnClickListener(this);
        rlDoctorOfficeScreen.setOnClickListener(this);
        tvDoctorConsultRecord.setOnClickListener(this);
        tvDoctorMyDoctor.setOnClickListener(this);
        flDoctorContainer.setOnClickListener(this);
        //处理头布局
        iPlatformUtil.showHeadView(mContext, new IFamilyDoctorPlatformUtil.InitHeadViewListener() {
            @Override
            public void error() {

            }

            @Override
            public void succeed(View view) {
                if (view != null) {
                    headLayout.addView(view);
                }
            }
        });
        //显示选择的图标
        ivDoctorOfficeScreen.setBackgroundResource(chooseUnfold);
        //显示医生列表
        showOfficeScreenFragment();
        //初始化选择医生弹框
        initChooseOfficePopWindow();
    }

    /**
     * 显示化科室选择弹出框
     *
     * @param view
     */
    private void showPopWindow(View view) {
        chooseDoctorOfficePopup.showAsDropDown(view, 0,
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0,
                        mContext.getResources().getDisplayMetrics()));

    }

    /**
     * 初始化科室选择弹出框
     */
    private void initChooseOfficePopWindow() {
        if (chooseDoctorOfficePopup == null) {
            View popWindow = layoutInflater.inflate(R.layout.popwindow_family_doctor_office, null);
            popWindowAdapter = new PopWindowDoctorOfficeGridAdapter(mDatas, mContext, iPlatformUtil);
            GridView gv_family_doctor_office = (GridView) popWindow.findViewById(R.id.gv_family_doctor_office);
            gv_family_doctor_office.setAdapter(popWindowAdapter);
            gv_family_doctor_office.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (chooseDoctorOfficePopup != null) {
                        chooseDoctorOfficePopup.dismiss();
                        doctorPopWindowIsShow = true;
                        ivDoctorOfficeScreen.setBackgroundResource(chooseUnfold);
                    }

                    popWindowAdapter.mSelectPos = position;
                    familyDoctorOfficeScreenFragment.loadData(mDatas.get(position).getDepartment_id());
                    tvDoctorOfficeScreen.setText(mDatas.get(position).getDepartment_name());
                }
            });
            chooseDoctorOfficePopup = new MyPopupWindow(popWindow, GridLayout.LayoutParams.MATCH_PARENT, GridLayout.LayoutParams.MATCH_PARENT);
            //设置背景颜色
            chooseDoctorOfficePopup.setBackgroundDrawable(new BitmapDrawable());
            // 使其聚集
            //  window.setFocusable(true);
            // 设置允许在外点击消失
            // window.setOutsideTouchable(true);
            chooseDoctorOfficePopup.update();
        }
    }


    /**
     * 加载医生科室列表数据
     */
    private void getDoctorOfficeList() {
        mRcHandler.sendMessage(RCHandler.START);
        wwzImpl.getDoctorDepartment(new RCFDGetDepartmentListener() {
            @Override
            public void getDataSuccess(List<RCFDDepartmentDTO> dto) {
                if (mDatas.size() > 0) {
                    mDatas.clear();
                }
                mDatas.addAll(dto);
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
    public void showOfficeScreenFragment() {
        if (familyDoctorOfficeScreenFragment == null) {
            familyDoctorOfficeScreenFragment = FamilyDoctorOfficeScreenFragment.newInstance(phone);
        }
        setTextColor(true, false, false);
        doctorPopWindowIsShow = true;
        showContent(R.id.fl_doctor_container, familyDoctorOfficeScreenFragment, null);
    }

    //医生列表是否显示
    private boolean doctorPopWindowIsShow = true;


    /**
     * 设置被选中的字体颜色
     *
     * @param isSelectOne   科室筛选
     * @param isSelectTwo   咨询记录
     * @param isSelectThree 我的医生
     */
    private void setTextColor(boolean isSelectOne, boolean isSelectTwo, boolean isSelectThree) {
        if (isSelectOne) {
            tvDoctorOfficeScreen.setTextColor(getResources().getColor(R.color.rcbase_app_main));
            ivDoctorOfficeScreen.setBackgroundResource(chooseUnfold);
        } else {
            tvDoctorOfficeScreen.setTextColor(getResources().getColor(R.color.rcbase_app_text_default_666));
            ivDoctorOfficeScreen.setBackgroundResource(chooseNone);
        }

        if (isSelectTwo) {
            tvDoctorConsultRecord.setTextColor(getResources().getColor(R.color.rcbase_app_main));

        } else {
            tvDoctorConsultRecord.setTextColor(getResources().getColor(R.color.rcbase_app_text_default_666));
        }

        if (isSelectThree) {
            tvDoctorMyDoctor.setTextColor(getResources().getColor(R.color.rcbase_app_main));

        } else {
            tvDoctorMyDoctor.setTextColor(getResources().getColor(R.color.rcbase_app_text_default_666));
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if (chooseDoctorOfficePopup != null && chooseDoctorOfficePopup.isShowing()) {
                chooseDoctorOfficePopup.dismiss();
                ivDoctorOfficeScreen.setBackgroundResource(chooseUnfold);
                doctorPopWindowIsShow = true;
                return false;
            }
        }

        return super.onKeyDown(keyCode, event);

    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.tv_doctor_office_screen || i == R.id.rl_doctor_office_screen) {
            if (familyDoctorOfficeScreenFragment == null) {
                familyDoctorOfficeScreenFragment = FamilyDoctorOfficeScreenFragment.newInstance(phone);
            }

            setTextColor(true, false, false);
            if (doctorPopWindowIsShow) {
                ivDoctorOfficeScreen.setBackgroundResource(choosePackup);
                showPopWindow(v);
                doctorPopWindowIsShow = false;
            } else {
                if (chooseDoctorOfficePopup != null) {
                    chooseDoctorOfficePopup.dismiss();
                    ivDoctorOfficeScreen.setBackgroundResource(chooseUnfold);
                }
                doctorPopWindowIsShow = true;
            }
            showContent(R.id.fl_doctor_container, familyDoctorOfficeScreenFragment, null);


        } else if (i == R.id.tv_doctor_consult_record) {
            if (familyDoctorConsultRecordFragment == null) {
                familyDoctorConsultRecordFragment = FamilyDoctorConsultRecordFragment.newInstance(phone);
            }
            if (chooseDoctorOfficePopup != null)
                chooseDoctorOfficePopup.dismiss();
            setTextColor(false, true, false);
            showContent(R.id.fl_doctor_container, familyDoctorConsultRecordFragment, null);
            doctorPopWindowIsShow = false;

        } else if (i == R.id.tv_doctor_my_doctor) {
            if (familyDoctorMyDoctorFragment == null) {
                familyDoctorMyDoctorFragment = FamilyDoctorMyDoctorFragment.newInstance(phone);
            }

            if (chooseDoctorOfficePopup != null)
                chooseDoctorOfficePopup.dismiss();

            setTextColor(false, false, true);
            showContent(R.id.fl_doctor_container, familyDoctorMyDoctorFragment, null);
            doctorPopWindowIsShow = false;

        }
    }


    /**
     * 开始咨询
     *
     * @param doctor_id   医生id
     * @param portrait    图片地址
     * @param doctor_name 医生姓名
     * @param title_name  医生职务
     * @param department  医师科室
     */
    public void doStartAdvisory(final String doctor_id, final String portrait, final String doctor_name, final String title_name, final String department) {
        iPlatformUtil.checkAccredit(mContext, new IFamilyDoctorPlatformUtil.CheckAccreditListener() {
            @Override
            public void error() {

            }

            @Override
            public void succeed() {
                wwzUtil.startAdvisory(doctor_id, RCFamilyDoctorWwzUtil.WWZ_SERVICE_ID, phone, portrait, doctor_name, title_name, department);
            }
        });
    }

}
