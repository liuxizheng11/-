package com.rocedar.sdk.familydoctor.app.util;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.cdfortis.ftconsult.ConsultSDK;
import com.rocedar.lib.base.manage.RCSDKManage;
import com.rocedar.lib.base.network.IRCPostListener;
import com.rocedar.lib.base.network.IRCRequestCode;
import com.rocedar.lib.base.network.IRequestCode;
import com.rocedar.lib.base.permission.AcpListener;
import com.rocedar.lib.base.unit.RCDialog;
import com.rocedar.lib.base.unit.RCHandler;
import com.rocedar.lib.base.unit.RCPermissionUtil;
import com.rocedar.lib.base.unit.RCPhoneNoCheckout;
import com.rocedar.lib.base.unit.RCToast;
import com.rocedar.sdk.familydoctor.app.RCFDSpecialistEvaluateActivity;
import com.rocedar.sdk.familydoctor.dto.RCFDRecordDetailDTO;
import com.rocedar.sdk.familydoctor.dto.RCFDServiceStatusInfoDTO;
import com.rocedar.sdk.familydoctor.request.IRCFDRecordRequest;
import com.rocedar.sdk.familydoctor.request.impl.RCFDRecordRequestImpl;
import com.rocedar.sdk.familydoctor.request.listener.fd.RCFDGetRecordDetailListener;
import com.rocedar.sdk.familydoctor.request.listener.fd.RCFDGetServerStatusListener;
import com.rocedar.sdk.shop.app.goods.ServerGoodsCardActivity;

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
    private IRCFDRecordRequest recordRequest;
    private RCHandler mRcHandler;
    private RCDialog notBuyServiceDialog;
    public static final String WWZ_SERVICE_ID = "1308002";

    private String phoneNumber;
    private String deviceNumber;

    public RCFamilyDoctorWwzUtil(Context activity, String phoneNo, String deviceNo) {
        this.activity = activity;
        this.phoneNumber = phoneNo;
        this.deviceNumber = deviceNo;
        activity.registerReceiver(broadcastReceiveGetData, intentFilterGetData());
        recordRequest = new RCFDRecordRequestImpl(activity);
        mRcHandler = new RCHandler(activity);
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
                //0为业务正常结束，1为用户取消，2为呼叫医生失败
                RCToast.TestCenter(context, result + "/咨询完成：" + busiId);
                if (result == 0) {
                    saveRecord(busiId);
                    //咨询完成，跳转到评价页面
                    RCFDSpecialistEvaluateActivity.goActivity(activity, busiId, doctorId,
                            portrait, doctor_name, title_name, department);
                }
            }
        }
    };


    private void saveRecord(final String recordId) {
        mRcHandler.sendMessage(RCHandler.START);
        recordRequest.saveRecord(recordId, new IRCPostListener() {
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

    public void startAdvisory(String doctorId, String portrait, String doctor_name,
                              String title_name, String department) {
        this.doctorId = doctorId;
        this.portrait = portrait;
        this.doctor_name = doctor_name;
        this.department = department;
        this.title_name = title_name;
        start(doctorId);
    }

    /**
     * @param doctorId 医生ID
     */
    private void start(final String doctorId) {
        if (doctorId == null) return;
        checkStatus(new RCFDGetServerStatusListener() {
            @Override
            public void getDataSuccess(final RCFDServiceStatusInfoDTO serviceStatusInfoDTO) {

                if (serviceStatusInfoDTO.isValid()) {
                    if (serviceStatusInfoDTO.getPhoneNo().equals("")) {
                        //没有绑定手机号判断传入的手机号是否有
                        if (phoneNumber.equals("")) {
//                            RCToast.Center(activity, "操作失败，请重试。");
                            Log.e("rocedar", "手机号为空");
                            if (RCSDKManage.getInstance().getRequestDataErrorLister() != null) {
                                RCSDKManage.getInstance().getRequestDataErrorLister().error(activity,
                                        IRCRequestCode.STATUS_APP_CODE_NO_PHONE_NUMBER, "手机号为空"
                                );
                            }
                            return;
                        }
                        if (!RCPhoneNoCheckout.isMobileNO(phoneNumber)) {
                            if (RCSDKManage.getInstance().getRequestDataErrorLister() != null) {
                                RCSDKManage.getInstance().getRequestDataErrorLister().error(activity,
                                        IRCRequestCode.STATUS_APP_CODE_NO_PHONE_NUMBER, "手机号格式不正确"
                                );
                            }
                            return;
                        }
                        //验证传入的手机号有效，开始绑定手机号
                        recordRequest.doBindPhoneNumber(phoneNumber, new IRCPostListener() {
                            @Override
                            public void getDataSuccess() {

                                RCToast.TestCenter(activity, "咨询的手机好为：" + serviceStatusInfoDTO.getPhoneNo());
                                RCPermissionUtil.getPremission(activity, new AcpListener() {
                                            @Override
                                            public void onGranted() {
                                                ConsultSDK.start(activity, FORMKEY, phoneNumber,
                                                        phoneNumber, "101", doctorId);
                                            }

                                            @Override
                                            public void onDenied(List<String> permissions) {
                                                RCToast.Center(activity, "您拒绝了权限，无法使用视频咨询，请在设置中开启权限.");
                                            }
                                        }, Manifest.permission.RECORD_AUDIO
                                        , Manifest.permission.CAMERA);
//                    }
                            }

                            @Override
                            public void getDataError(int status, String msg) {
                                if (RCSDKManage.getInstance().getRequestDataErrorLister() != null) {
                                    if (status == IRequestCode.STATUS_CODE_PHONE_BIND) {
                                        RCSDKManage.getInstance().getRequestDataErrorLister().error(activity,
                                                IRCRequestCode.STATUS_APP_CODE_PHONE_NUMBER_INVALID, msg
                                        );
                                    } else {
                                        RCSDKManage.getInstance().getRequestDataErrorLister().error(activity,
                                                IRCRequestCode.STATUS_APP_CODE_NO_PHONE_NUMBER, msg
                                        );
                                    }
                                }
                            }
                        });
                        return;
                    }
                    RCToast.TestCenter(activity, "咨询的手机好为：" + serviceStatusInfoDTO.getPhoneNo());
                    RCPermissionUtil.getPremission(activity, new AcpListener() {
                                @Override
                                public void onGranted() {
                                    ConsultSDK.start(activity, FORMKEY, serviceStatusInfoDTO.getPhoneNo()
                                            , serviceStatusInfoDTO.getPhoneNo(), "101", doctorId);
                                }

                                @Override
                                public void onDenied(List<String> permissions) {
                                    RCToast.Center(activity, "您拒绝了权限，无法使用视频咨询，请在设置中开启权限.");
                                }
                            }, Manifest.permission.RECORD_AUDIO
                            , Manifest.permission.CAMERA);
//                    }
                } else {
                    showBuyDialog();
                }
            }

            @Override
            public void getDataError(int status, String msg) {

            }
        });
    }

    public void start() {

    }


    /**
     * 检查服务权限
     *
     * @param listener
     */
    public void checkStatus(final RCFDGetServerStatusListener listener) {
        mRcHandler.sendMessage(RCHandler.START);
        recordRequest.getDoctorServerStatus(WWZ_SERVICE_ID,
                deviceNumber, new RCFDGetServerStatusListener() {
                    @Override
                    public void getDataSuccess(RCFDServiceStatusInfoDTO serviceStatusInfoDTO) {
                        mRcHandler.sendMessage(RCHandler.GETDATA_OK);
                        listener.getDataSuccess(serviceStatusInfoDTO);
                    }

                    @Override
                    public void getDataError(int status, String msg) {
                        listener.getDataError(status, msg);
                        mRcHandler.sendMessage(RCHandler.GETDATA_OK);
                    }
                });
    }

    public void showBuyDialog() {
        notBuyServiceDialog = new RCDialog(activity, new String[]{null, "您还未购买此服务。", "", "去了解一下"}, null, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goShop();
                notBuyServiceDialog.dismiss();
            }
        });
        if (notBuyServiceDialog != null && !notBuyServiceDialog.isShowing())
            notBuyServiceDialog.show();
    }


    public void goShop() {
        //跳转到有赞商城
//        ShopShowActivity.goActivity(activity);
        //新版跳到购买服务
        ServerGoodsCardActivity.goActivity(activity, 100001);
    }

    /**
     * 获取视频通话参数
     */
    public void getVideoPermission() {
        mRcHandler.sendMessage(RCHandler.START);
        recordRequest.getFDSpecificDoctor(new RCFDGetRecordDetailListener() {
            @Override
            public void getDataSuccess(RCFDRecordDetailDTO dto) {
                mRcHandler.sendMessage(RCHandler.GETDATA_OK);
                startAdvisory(dto.getDoctor_id(), dto.getPortrait(),
                        dto.getDoctor_name(), dto.getTitle_name(), dto.getDepartment_name());
            }

            @Override
            public void getDataError(int status, String msg) {
                mRcHandler.sendMessage(RCHandler.GETDATA_OK);
            }
        });
    }

}
