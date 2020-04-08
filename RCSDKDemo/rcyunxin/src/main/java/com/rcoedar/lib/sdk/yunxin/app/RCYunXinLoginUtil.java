package com.rcoedar.lib.sdk.yunxin.app;

import android.Manifest;
import android.content.Context;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.rcoedar.lib.sdk.yunxin.config.RCYunXinConfig;
import com.rocedar.lib.base.permission.AcpListener;
import com.rocedar.lib.base.unit.RCPermissionUtil;
import com.rocedar.lib.base.unit.RCToast;

import java.util.List;

/**
 * 作者：lxz
 * 日期：2018/8/6 上午9:52
 * 版本：V1.0
 * 描述：云信 登陆
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class RCYunXinLoginUtil {

    /**
     * 云信 登陆
     *
     * @param account 用户帐号
     * @param token   登录 token
     */
    public static void YunXinLogin(final Context context, final String account, String token, final String doctor_account,
                                   final String doctor_name, final String doctor_head) {
        LoginInfo info = new LoginInfo(account, token); // config...
        RequestCallback<LoginInfo> callback =
                new RequestCallback<LoginInfo>() {
                    @Override
                    public void onSuccess(LoginInfo param) {
                        // 可以在此保存LoginInfo到本地，下次启动APP做自动登录用
                        RCYunXinConfig.setYunXinLoginData(context, param);
                        RCPermissionUtil.getPremission(context
                                , new AcpListener() {
                                    @Override
                                    public void onGranted() {
                                        RCYunXinCallActivity.goActivity(context, doctor_head, doctor_name, account, doctor_account);
                                    }

                                    @Override
                                    public void onDenied(List<String> permissions) {
                                        RCToast.Center(context, "您拒绝了权限，无法视频通话.");
                                    }
                                }
                                , android.Manifest.permission.RECORD_AUDIO
                                , Manifest.permission.CAMERA);
                    }

                    @Override
                    public void onFailed(int code) {
                    }

                    @Override
                    public void onException(Throwable exception) {
                    }

                };
        NIMClient.getService(AuthService.class).login(info)
                .setCallback(callback);
    }

    /**
     * 注销接口
     */
    public void YunXinLogout() {
        NIMClient.getService(AuthService.class).logout();
    }


}
