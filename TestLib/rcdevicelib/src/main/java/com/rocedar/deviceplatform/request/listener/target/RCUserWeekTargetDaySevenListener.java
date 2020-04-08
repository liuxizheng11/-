package com.rocedar.deviceplatform.request.listener.target;

import com.rocedar.deviceplatform.dto.target.RCIndicatorTargetDayDTO;

import java.util.List;
import java.util.Map;

/**
 * 项目名称：FangZhou2.1
 * <p>
 * 作者：phj
 * 日期：2017/7/10 下午4:04
 * 版本：V2.2.00
 * 描述：
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public interface RCUserWeekTargetDaySevenListener {

    void getDataSuccess(Map<String, List<RCIndicatorTargetDayDTO>> dtoList);

    void getDataError(int status, String msg);

}
