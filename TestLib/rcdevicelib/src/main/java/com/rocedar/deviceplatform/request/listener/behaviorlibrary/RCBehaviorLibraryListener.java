package com.rocedar.deviceplatform.request.listener.behaviorlibrary;

import com.rocedar.deviceplatform.dto.behaviorlibrary.RCBehaviorLibraryDTO;

import java.util.List;

/**
 * 作者：lxz
 * 日期：17/7/28 上午11:42
 * 版本：V1.0
 * 描述：
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public interface RCBehaviorLibraryListener {
    void getDataSuccess(RCBehaviorLibraryDTO dtoList);

    void getDataError(int status, String msg);
}
