package com.rocedar.deviceplatform.request.listener.behaviorlibrary;

import com.rocedar.deviceplatform.dto.behaviorlibrary.RCBehaviorRecordDTO;

/**
 * 作者：lxz
 * 日期：17/11/7 下午2:43
 * 版本：V1.0
 * 描述：
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public interface RCBehaviorRecordListener  {
    void getDataSuccess(RCBehaviorRecordDTO dtoList);

    void getDataError(int status, String msg);
}
