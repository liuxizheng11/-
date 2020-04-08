package com.rocedar.deviceplatform.request.listener;

import com.rocedar.deviceplatform.dto.indicatorconduct.IndicatorMoreInfoDTO;

/**
 * 项目名称：设备管理平台
 * <p>
 * 作者：phj
 * 日期：2017/5/3 下午3:54
 * 版本：V1.0.01
 * 描述：
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public interface RCGetIndicatorMoreInfoListener {

    void getDataSuccess(IndicatorMoreInfoDTO dto);

    void getDataError(int status, String msg);
}
