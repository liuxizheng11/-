package com.rocedar.sdk.familydoctor.request.listener.fd;

import com.rocedar.sdk.familydoctor.dto.RCFDServiceStatusInfoDTO;

/**
 * 项目名称：瑰柏SDK-基础库
 * <p>
 * 作者：phj
 * 日期：2018/6/5 下午4:46
 * 版本：V1.0.00
 * 描述：瑰柏SDK-
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public interface RCFDGetServerStatusListener {

    void getDataSuccess(RCFDServiceStatusInfoDTO serviceStatusInfoDTO);

    void getDataError(int status, String msg);

}
