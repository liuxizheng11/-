package com.rocedar.sdk.familydoctor.config;

import android.app.Activity;

import com.rocedar.lib.base.network.IRCPostListener;
import com.rocedar.lib.base.unit.RCHandler;
import com.rocedar.sdk.familydoctor.dto.mingyi.RCOrderVideoAccidDTO;
import com.rocedar.sdk.familydoctor.request.impl.RCOrderYunXinImpl;
import com.rocedar.sdk.familydoctor.request.listener.mingyi.RCOrderVideoAccidListener;
import com.rocedar.sdk.shop.config.IRCShopConfig;

/**
 * 项目名称：瑰柏SDK-健康服务（家庭医生）
 * <p>
 * 作者：phj
 * 日期：2018/8/9 下午5:24
 * 版本：V1.1.00
 * 描述：瑰柏SDK-
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class ShopConfigImpl implements IRCShopConfig {


    private YunXinUtil yunXinUtil;
    private RCHandler rcHandler;
    private RCOrderYunXinImpl rcOrderYunXin;

    @Override
    public void yunXinUnRegisterBroad() {
        if (yunXinUtil != null)
            yunXinUtil.unRegister();
    }

    /**
     * 实现云信视频咨询
     *
     * @param orderId
     */
    @Override
    public void yunXinAdvisory(final Activity activity, final int orderId) {
        yunXinUtil = new YunXinUtil(activity);
        rcHandler = new RCHandler(activity);
        rcOrderYunXin = RCOrderYunXinImpl.getInstance(activity);
        //接口请求数据
        rcHandler.sendMessage(RCHandler.START);
        rcOrderYunXin.getOrderVideoAccid(orderId, new RCOrderVideoAccidListener() {
            @Override
            public void getDataSuccess(RCOrderVideoAccidDTO videoAccidDTO) {
                rcHandler.sendMessage(RCHandler.GETDATA_OK);
                yunXinUtil.startAdvisory(videoAccidDTO.getUser_accid(), videoAccidDTO.getUser_accid_token(),
                        videoAccidDTO.getDoctor_accid(), orderId, videoAccidDTO.getDoctor_name(), videoAccidDTO.getDoctor_portrait());
            }

            @Override
            public void getDataError(int status, String msg) {
                rcHandler.sendMessage(RCHandler.GETDATA_OK);
            }
        });

    }


    /**
     * 实现云信视频咨询完成处理
     *
     * @param orderId
     */
    @Override
    public void yunXinAdvisoryOver(int orderId, int status) {
        //咨询完成
        rcHandler.sendMessage(RCHandler.START);
        rcOrderYunXin.putOverOrderVider(orderId, new IRCPostListener() {
            @Override
            public void getDataSuccess() {
                rcHandler.sendMessage(RCHandler.GETDATA_OK);
            }

            @Override
            public void getDataError(int status, String msg) {
                rcHandler.sendMessage(RCHandler.GETDATA_OK);
            }
        });
    }


}
