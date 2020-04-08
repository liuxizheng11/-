package com.rocedar.sdk.familydoctor.request.listener.fd;


import com.rocedar.sdk.familydoctor.dto.RCFDRecordListDTO;

import java.util.List;

/**
 * 项目名称：设备管理平台
 * <p>
 * 作者：phj
 * 日期：2017/4/20 上午11:40
 * 版本：V1.0.01
 * 描述：获取问诊记录列表监听方法
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public interface RCFDGetRecordListListener {

    void getDataSuccess(List<RCFDRecordListDTO> dto);

    void getDataError(int status, String msg);

}
