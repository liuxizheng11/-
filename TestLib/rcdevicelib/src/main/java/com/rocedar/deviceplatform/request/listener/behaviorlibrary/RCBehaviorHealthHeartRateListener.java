package com.rocedar.deviceplatform.request.listener.behaviorlibrary;

/**
 * 项目名称：FangZhou2.1
 * <p>
 * 作者：phj
 * 日期：2017/8/24 下午7:50
 * 版本：V2.2.00
 * 描述：
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public interface RCBehaviorHealthHeartRateListener {

    void getDataSuccess(int validHeartHigh, int validHeartLow, int targetHeartHigh, int targetHeartLow);

    void getDataError(int status, String msg);


}
