package com.rocedar.deviceplatform.request.listener.target;

import com.rocedar.deviceplatform.dto.target.RCIndicatorTargetDTO;

import java.util.List;

/**
 * 作者：lxz
 * 日期：17/7/7 下午3:55
 * 版本：V2.0.05 健康立方
 * 描述：查询标签目标 查询用户目标
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public interface RCTargetLoginDataListener {
    void getDataSuccess(List<RCIndicatorTargetDTO> dtoList);

    void getDataError(int status, String msg);
}
