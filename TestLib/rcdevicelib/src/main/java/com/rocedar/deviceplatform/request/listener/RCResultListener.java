package com.rocedar.deviceplatform.request.listener;

/**
 * 作者：lxz
 * 日期：17/8/3 上午11:01
 * 版本：V1.0
 * 描述：带result 返回值的listener
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public interface RCResultListener {
    void getDataSuccess(String result);

    void getDataError(int status, String msg);
}
