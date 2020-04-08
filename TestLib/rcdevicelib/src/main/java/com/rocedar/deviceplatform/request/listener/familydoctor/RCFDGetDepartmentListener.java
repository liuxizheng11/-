package com.rocedar.deviceplatform.request.listener.familydoctor;


import com.rocedar.deviceplatform.dto.familydoctor.RCFDDepartmentDTO;

import java.util.List;

/**
 * 项目名称：设备管理平台
 * <p>
 * 作者：phj
 * 日期：2017/4/20 上午11:40
 * 版本：V1.0.01
 * 描述：获取医生部门（科室）列表监听方法
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public interface RCFDGetDepartmentListener {

    void getDataSuccess(List<RCFDDepartmentDTO> dto);

    void getDataError(int status, String msg);

}
