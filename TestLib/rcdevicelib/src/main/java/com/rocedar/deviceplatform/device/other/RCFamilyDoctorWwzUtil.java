package com.rocedar.deviceplatform.device.other;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.TextUtils;
import android.view.View;

import com.cdfortis.ftconsult.ConsultSDK;
import com.rocedar.base.RCBaseConfig;
import com.rocedar.base.RCDeveloperConfig;
import com.rocedar.base.RCDialog;
import com.rocedar.base.RCHandler;
import com.rocedar.base.RCTPJump;
import com.rocedar.base.RCToast;
import com.rocedar.base.permission.AcpListener;
import com.rocedar.base.permission.RCPermissionUtil;
import com.rocedar.base.shareprefernces.RCSPBaseInfo;
import com.rocedar.deviceplatform.app.familydoctor.FamilyDoctorEvaluateActivity;
import com.rocedar.deviceplatform.request.RCFamilyDoctorRequest;
import com.rocedar.deviceplatform.request.impl.RCFamilyDoctorWWZImpl;
import com.rocedar.deviceplatform.request.impl.RCThirdServiceRequestImpl;
import com.rocedar.deviceplatform.request.listener.RCThirdServiceStatusListener;
import com.rocedar.deviceplatform.request.listener.familydoctor.RCFDPostListener;

import java.util.List;

/**
 * 项目名称：设备管理平台
 * <p>
 * 作者：phj
 * 日期：2017/4/22 下午5:09
 * 版本：V1.0.01
 * 描述：
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public class RCFamilyDoctorWwzUtil {


    private Context activity;
    private RCFamilyDoctorRequest rcFamilyDoctorRequest;
    private RCThirdServiceRequestImpl serviceRequest;
    private RCHandler mRcHandler;
    private RCDialog notBuyServiceDialog;
    public static final String WWZ_SERVICE_ID = "1308002";

    public RCFamilyDoctorWwzUtil(Context activity) {
        this.activity = activity;
        activity.registerReceiver(broadcastReceiveGetData, intentFilterGetData());
        rcFamilyDoctorRequest = new RCFamilyDoctorWWZImpl(activity);
        serviceRequest = new RCThirdServiceRequestImpl(activity);
        mRcHandler = new RCHandler(activity);
        if (RCDeveloperConfig.WWZIstest) {
            String address = "http://121.40.126.183:801/sdkService/";
            ConsultSDK.setAddress(address);
        }
    }

    public void onDestory() {
        try {
            activity.unregisterReceiver(broadcastReceiveGetData);
        } catch (Exception e) {
        }
    }


    /**
     * 服务接受操作指令和数据的广播
     */
    private BroadcastReceiver broadcastReceiveGetData = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (TextUtils.equals(intent.getAction(), ConsultSDK.ACTION_STATUS)) {
                int result = intent.getIntExtra(ConsultSDK.EXTRA_RESULT, 0);
                String busiId = intent.getStringExtra(ConsultSDK.EXTRA_BUSIID);
                RCToast.TestCenter(activity, "bid:" + busiId + "<->result:" + result);
                //0为业务正常结束，1为用户取消，2为呼叫医生失败
                if (result == 0) {
                    save(busiId);
                    FamilyDoctorEvaluateActivity.goActivity(activity,busiId,doctorId,portrait,doctor_name,title_name,department);
                }
            }
        }
    };

    private void save(final String recordId) {
        RCToast.TestCenter(activity, "recordId  : " + recordId);
        mRcHandler.sendMessage(RCHandler.START);
        rcFamilyDoctorRequest.saveRecord(recordId, new RCFDPostListener() {
            @Override
            public void getDataSuccess() {

                mRcHandler.sendMessage(RCHandler.GETDATA_OK);
            }

            @Override
            public void getDataError(int status, String msg) {
                mRcHandler.sendMessage(RCHandler.GETDATA_OK);

            }
        });
    }


    private IntentFilter intentFilterGetData() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConsultSDK.ACTION_STATUS);
        return intentFilter;
    }

    /**
     * 内网用：0
     * 公网用：d441f536b83ac43a975564878ba8cfb6
     */
    private String FORMKEY = "d441f536b83ac43a975564878ba8cfb6";
    private String doctorId;
    private String portrait;
    private String doctor_name;
    private String department;
    private String title_name;
    public void startAdvisory(final String doctorId, String serviceID, final String phone, String portrait, String doctor_name,  String title_name, String department) {
        this.doctorId =doctorId;
        this.portrait = portrait;
        this.doctor_name = doctor_name;
        this.department = department;
        this.title_name = title_name;
        if (phone.equals("")) {
            RCToast.Center(activity, "操作失败，请查询登录后重试。");
            RCToast.TestCenter(activity, "baseLib中没有获取手机号方法，请检查代码");
        } else {
            RCToast.TestCenter(activity, "咨询的手机好为：" + phone);
            RCPermissionUtil.getPremission(activity, new AcpListener() {
                        @Override
                        public void onGranted() {
                            ConsultSDK.start(activity, FORMKEY, phone + ""
                                    , RCSPBaseInfo.getLastUserId() + "", RCBaseConfig.APPTAG, doctorId);
                        }

                        @Override
                        public void onDenied(List<String> permissions) {
                            RCToast.Center(activity, "您拒绝了权限，无法使用视频咨询，请在设置中开启权限.");
                        }
                    }
                    , Manifest.permission.RECORD_AUDIO
                    , Manifest.permission.CAMERA);

        }
    }

    /**
     * @param doctorId 医生ID
     */
    public void start(final String doctorId, String serviceID, final String phone) {
        if (doctorId == null) return;
        mRcHandler.sendMessage(RCHandler.START);
        serviceRequest.serviceStatusQuery(serviceID, new RCThirdServiceStatusListener() {
            @Override
            public void getDataSuccess(int status) {
                if (status == 2) {
                    if (phone.equals("")) {
                        RCToast.Center(activity, "操作失败，请查询登录后重试。");
                        RCToast.TestCenter(activity, "baseLib中没有获取手机号方法，请检查代码");
                    } else {
                        RCToast.TestCenter(activity, "咨询的手机好为：" + phone);
                        RCPermissionUtil.getPremission(activity, new AcpListener() {
                                    @Override
                                    public void onGranted() {
                                        ConsultSDK.start(activity, FORMKEY, phone + ""
                                                , RCSPBaseInfo.getLastUserId() + "", RCBaseConfig.APPTAG, doctorId);
                                    }

                                    @Override
                                    public void onDenied(List<String> permissions) {
                                        RCToast.Center(activity, "您拒绝了权限，无法使用视频咨询，请在设置中开启权限.");
                                    }
                                }, Manifest.permission.RECORD_AUDIO
                                , Manifest.permission.CAMERA);

                    }
                } else {
                    if (RCBaseConfig.APPTAG.equals(RCBaseConfig.APPTAG_DONGYA)) {
                        notBuyServiceDialog = new RCDialog(activity, new String[]{null, "您还未购买此服务。", "", "去了解一下"}, null, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                RCTPJump.ActivityJump(activity, "rctp://android##com.rocedar.app.home.ShopShowActivity##{\"url\":\"/shop/goods/1308002/" + "\"}");
                                notBuyServiceDialog.dismiss();
                            }
                        });
                    } else {
                        notBuyServiceDialog = new RCDialog(activity, new String[]{null, "贵公司尚未为您开通该业务或该业务已到期。", null, null}, null, null);
                    }
                    if (notBuyServiceDialog != null)
                        notBuyServiceDialog.show();
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
