package com.rocedar.deviceplatform.request.listener.target;

import com.rocedar.deviceplatform.dto.target.RCIndicatorTargetDayDTO;

import java.util.List;

/**
 * 作者：lxz
 * 日期：17/7/7 下午5:08
 * 版本：V1.0
 * 描述：
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public interface RCUserTargetDayDataListener {
    void getDataSuccess(List<RCIndicatorTargetDayDTO> dtoList);

    void getDataError(int status, String msg);

}
