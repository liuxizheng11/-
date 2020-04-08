package com.rocedar.sdk.familydoctor.config;

import android.content.Context;
import android.util.Log;

import com.rocedar.lib.base.config.RCSPUtilInfo;
import com.rocedar.lib.base.manage.RCSDKManage;
import com.rocedar.lib.base.network.IRCPostListener;
import com.rocedar.lib.base.network.IRCRequestCode;
import com.rocedar.lib.base.network.IRequestCode;
import com.rocedar.lib.base.unit.RCLog;
import com.rocedar.sdk.familydoctor.request.impl.RCFDRecordRequestImpl;

/**
 * 项目名称：瑰柏SDK-基础库
 * <p>
 * 作者：phj
 * 日期：2018/5/22 下午6:12
 * 版本：V1.0.00
 * 描述：瑰柏SDK-
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class RCFDConfigUtil {


    private static String TAG = RCFDConfigUtil.class.getName();


    public static void setClassPath(Class c) {
        RCSPUtilInfo.saveClassPath(FDCONFIGCLASSKEY, c.getName());
        iRCFDConfig = null;
    }


    private static final String FDCONFIGCLASSKEY = "family_doctor";

    private static IRCFDConfig iRCFDConfig;


    public static IRCFDConfig getConfig() {
        if (iRCFDConfig != null) return iRCFDConfig;
        if (!RCSPUtilInfo.getClassPath(FDCONFIGCLASSKEY).equals("")) {
            try {
                Class classInfo = Class.forName(RCSPUtilInfo.getClassPath(FDCONFIGCLASSKEY));
                if (classInfo.newInstance() instanceof IRCFDConfig)
                    iRCFDConfig = (IRCFDConfig) Class.forName(RCSPUtilInfo.
                            getClassPath(FDCONFIGCLASSKEY)).newInstance();
            } catch (Exception e) {
                e.printStackTrace();
                RCLog.e(TAG, "类对象获取失败");
            }
        }
        if (iRCFDConfig == null) {
            iRCFDConfig = new RCFDConfigBase();
        }
        return iRCFDConfig;
    }


    public static void setPhoneNumber(final Context context, String phoneNumber) {
        //没有绑定手机号判断传入的手机号是否有
        if (phoneNumber.equals("")) {
            Log.e("rocedar", "手机号为空");
            if (RCSDKManage.getInstance().getRequestDataErrorLister() != null) {
                RCSDKManage.getInstance().getRequestDataErrorLister().error(context,
                        IRCRequestCode.STATUS_APP_CODE_NO_PHONE_NUMBER, "手机号为空"
                );
            }
            return;
        }
        try {
            Long.parseLong(phoneNumber);
        } catch (NumberFormatException e) {
            Log.e("rocedar", "手机号非法");
            if (RCSDKManage.getInstance().getRequestDataErrorLister() != null) {
                RCSDKManage.getInstance().getRequestDataErrorLister().error(context,
                        IRCRequestCode.STATUS_APP_CODE_NO_PHONE_NUMBER, "手机号格式不正确"
                );
            }
            return;
        }
        //验证传入的手机号有效，开始绑定手机号
        new RCFDRecordRequestImpl(context).doBindPhoneNumber(phoneNumber, new IRCPostListener() {
            @Override
            public void getDataSuccess() {
            }

            @Override
            public void getDataError(int status, String msg) {
                if (status == IRequestCode.STATUS_CODE_PHONE_BIND) {
                    Log.e("rocedar", "手机号已经被绑定");
                    if (RCSDKManage.getInstance().getRequestDataErrorLister() != null) {
                        RCSDKManage.getInstance().getRequestDataErrorLister().error(context,
                                IRCRequestCode.STATUS_APP_CODE_PHONE_NUMBER_INVALID, msg
                        );
                    }
                }
            }
        });
    }


}
