package com.rocedar.deviceplatform.request.listener;

/**
 * 项目名称：设备管理平台
 * <p>
 * 作者：phj
 * 日期：2017/4/20 上午11:40
 * 版本：V1.0.01
 * 描述：Post Put 无返回值通用listener
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public interface RCPostListener {

    void getDataSuccess();

    void getDataError(int status, String msg);
}
