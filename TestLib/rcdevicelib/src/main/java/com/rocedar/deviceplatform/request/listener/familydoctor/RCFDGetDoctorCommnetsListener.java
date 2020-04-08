package com.rocedar.deviceplatform.request.listener.familydoctor;

import com.rocedar.deviceplatform.dto.familydoctor.RCFDDoctorCommentsDTO;

import java.util.List;

/**
 * 项目名称：设备管理平台
 * <p>
 * 作者：phj
 * 日期：2017/11/7 下午10:42
 * 版本：V2.2.00
 * 描述：家庭医生医生评论列表
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public interface RCFDGetDoctorCommnetsListener {

    void getDataSuccess(List<RCFDDoctorCommentsDTO> dto);

    void getDataError(int status, String msg);

}
