package com.rocedar.sdk.familydoctor.app.fragment;

import android.os.Bundle;

import com.rocedar.lib.base.manage.RCBaseFragment;
import com.rocedar.sdk.familydoctor.app.util.RCFamilyDoctorWwzUtil;
import com.rocedar.sdk.familydoctor.request.IRCFDDoctorRequest;
import com.rocedar.sdk.familydoctor.request.IRCFDRecordRequest;
import com.rocedar.sdk.familydoctor.request.impl.RCFDDoctorRequestImpl;
import com.rocedar.sdk.familydoctor.request.impl.RCFDRecordRequestImpl;


/**
 * 项目名称：平台库-家庭医生
 * <p>
 * 作者：phj
 * 日期：2018/2/25 下午12:35
 * 版本：V1.0.00
 * 描述：
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public class RCFDSpecialistBaseFragment extends RCBaseFragment {


    public static final String KEY_PHONENO = "phone";
    public static final String KEY_DEVICENO = "device_number";

    private String TAG = "FamilyDoctorBaseFragment";

    //微问诊 接口数据实现类实例
    public IRCFDDoctorRequest doctorRequest;
    public IRCFDRecordRequest recordRequest;
    //手机号码
    public String mPhoneNumber;
    //设备SN列表
    public String mDeviceNumber;

    public RCFamilyDoctorWwzUtil doctorWwzUtil;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        mPhoneNumber = getArguments().getString(KEY_PHONENO);
        mDeviceNumber = getArguments().getString(KEY_DEVICENO);
        doctorRequest = new RCFDDoctorRequestImpl(mActivity);
        recordRequest = new RCFDRecordRequestImpl(mActivity);
        doctorWwzUtil = new RCFamilyDoctorWwzUtil(mActivity, mPhoneNumber, mDeviceNumber);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        doctorWwzUtil.onDestory();
    }
}
