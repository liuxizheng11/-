package com.rocedar.sdk.familydoctor.request.listener.mingyi;

import com.rocedar.lib.base.network.IRCBaseListener;
import com.rocedar.sdk.familydoctor.dto.mingyi.RCMIngYiDoctorListDTO;

import java.util.List;

/**
 * 作者：lxz
 * 日期：2018/7/23 下午5:52
 * 版本：V1.0
 * 描述：
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public interface RCMingYiDoctorListListener extends IRCBaseListener {
    void getDataSuccess(List<RCMIngYiDoctorListDTO> doctorListDTOS);
}
