package com.rocedar.sdk.familydoctor.request.listener.mingyi;

import com.rocedar.lib.base.network.IRCBaseListener;
import com.rocedar.sdk.familydoctor.dto.mingyi.RCMingYiDoctorDetailDTO;

/**
 * 项目名称：瑰柏SDK-家庭医生
 * <p>
 * 作者：phj
 * 日期：2018/7/20 下午3:31
 * 版本：V1.0.00
 * 描述：瑰柏SDK-医生详情数据获取监听
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public interface RCMingYiDoctorDetailListener extends IRCBaseListener {

    void getDataSuccess(RCMingYiDoctorDetailDTO dto);

}
