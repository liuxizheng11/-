package com.rocedar.deviceplatform.request.listener.behaviorlibrary;

import com.rocedar.deviceplatform.dto.behaviorlibrary.RCBehaviorChartsDTO;

/**
 * 作者：lxz
 * 日期：17/11/6 下午4:14
 * 版本：V1.0
 * 描述：
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public interface RCBehaviorChartsListener {
    void getDataSuccess(RCBehaviorChartsDTO dtoList);

    void getDataError(int status, String msg);
}
