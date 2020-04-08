package com.rcoedar.lib.sdk.yunxin;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.rcoedar.lib.sdk.yunxin.app.RCYunXin;
import com.rcoedar.lib.sdk.yunxin.app.RCYunXinLoginUtil;

/**
 * 项目名称：瑰柏SDK-云信库（用于名医视频咨询）
 * <p>
 * 作者：phj
 * 日期：2018/8/9 上午11:28
 * 版本：V1.1.00
 * 描述：瑰柏SDK-云信对外工具类
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class RCYXUtil {


    /**
     * 初始化云信
     *
     * @param application
     */
    public void initYunXin(Application application) {
        RCYunXin.getInstance(application);
    }


    /**
     * 开始咨询
     *
     * @param activity
     * @param userAccid
     * @param userToken
     * @param doctorAccid
     */
    public void startAdvisory(Activity activity, String userAccid, String userToken, String doctorAccid
            , String doctorName, String doctorHead) {
        RCYunXinLoginUtil.YunXinLogin(activity, userAccid, userToken, doctorAccid, doctorName, doctorHead);
    }

    private static final String BROADCAST_YUNXIN = "com.rocedar.sdk.fd.yunxin";
    private static final String BROADCAST_YUNXIN_KEY_STATUS = "com.rocedar.sdk.fd.yunxin.RESULT";

    public static String getBroadcastYunxin() {
        return BROADCAST_YUNXIN;
    }

    public static String getBroadcastYunxinKeyStatus() {
        return BROADCAST_YUNXIN_KEY_STATUS;
    }


    /**
     * 结束发送广播
     *
     * @param c
     * @param status
     */
    public static void sendBroadcast(Context c, int status) {
        Intent intent = new Intent();
        intent.setAction(BROADCAST_YUNXIN);
        intent.putExtra(BROADCAST_YUNXIN_KEY_STATUS, status);
        c.sendBroadcast(intent);
    }

}
