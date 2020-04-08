package com.rocedar.sdk.familydoctor.request.listener.mingyi;


import com.rocedar.lib.base.network.IRCBaseListener;
import com.rocedar.sdk.familydoctor.dto.mingyi.RCMingYiDoctorListSelectlDTO;

import java.util.List;

/**
 * 作者：lxz
 * 日期：2018/7/22 下午12:58
 * 版本：V1.0
 * 描述： 医生列表一二级选择数据
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public interface RCMingYiDoctorListSelectListener extends IRCBaseListener {
    void getDataSuccess(List<RCMingYiDoctorListSelectlDTO> doctorListSelectlDTO);
}
