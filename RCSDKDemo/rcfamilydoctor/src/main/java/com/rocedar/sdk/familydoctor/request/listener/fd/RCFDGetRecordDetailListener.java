package com.rocedar.sdk.familydoctor.request.listener.fd;


import com.rocedar.sdk.familydoctor.dto.RCFDRecordDetailDTO;

/**
 * 项目名称：设备管理平台
 * <p>
 * 作者：phj
 * 日期：2017/4/20 上午11:40
 * 版本：V1.0.01
 * 描述：获取问诊记录详情监听方法
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public interface RCFDGetRecordDetailListener {

    void getDataSuccess(RCFDRecordDetailDTO dto);

    void getDataError(int status, String msg);

}
