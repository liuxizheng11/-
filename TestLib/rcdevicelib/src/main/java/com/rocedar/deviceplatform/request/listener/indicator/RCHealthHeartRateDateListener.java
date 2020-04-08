package com.rocedar.deviceplatform.request.listener.indicator;

import com.rocedar.deviceplatform.dto.indicatorconduct.IndicatorHeartDTO;

import java.util.List;

/**
 * 项目名称：FangZhou2.1
 * <p>
 * 作者：phj
 * 日期：2017/8/25 下午5:38
 * 版本：V2.2.00
 * 描述：
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public interface RCHealthHeartRateDateListener {

    void getDataSuccess(List<List<IndicatorHeartDTO>> dtoList, int date);

    void getDataError(int status, String msg);

}
