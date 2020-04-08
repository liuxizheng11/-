package com.rocedar.deviceplatform.request.listener.familydoctor;

/**
 * 项目名称：设备管理平台
 * <p>
 * 作者：phj
 * 日期：2017/4/20 上午11:40
 * 版本：V1.0.01
 * 描述：请求保存记录、保存我的医生、删除我的医生通用请求监听
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public interface RCFDPostListener {

    void getDataSuccess();

    void getDataError(int status, String msg);
}
