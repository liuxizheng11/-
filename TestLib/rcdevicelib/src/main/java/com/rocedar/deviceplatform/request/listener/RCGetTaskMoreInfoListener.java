package com.rocedar.deviceplatform.request.listener;

import com.rocedar.deviceplatform.dto.indicatorconduct.TaskMoreInfoDataDTO;

/**
 * @author liuyi
 * @date 2017/5/5
 * @desc 任务查看更多
 * @veison V1.0.01
 */

public interface RCGetTaskMoreInfoListener {
    void getDataSuccess(TaskMoreInfoDataDTO dto);

    void getDataError(int status, String msg);
}
