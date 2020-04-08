package com.rocedar.sdk.familydoctor.request.listener;

import com.rocedar.lib.base.network.IRCBaseListener;
import com.rocedar.sdk.familydoctor.dto.RCFDPatientListDTO;

import java.util.List;

/**
 * 项目名称：瑰柏SDK-家庭医生
 * <p>
 * 作者：phj
 * 日期：2018/7/24 上午11:25
 * 版本：V1.0.00
 * 描述：瑰柏SDK-病人列表
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public interface RCFDPatientListListener extends IRCBaseListener {

    void getDataSuccess(List<RCFDPatientListDTO> dtoList);

}
