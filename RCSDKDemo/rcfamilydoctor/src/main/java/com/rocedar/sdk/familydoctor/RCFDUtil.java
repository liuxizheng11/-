package com.rocedar.sdk.familydoctor;

import android.content.Context;

import com.rocedar.sdk.familydoctor.app.util.RCFamilyDoctorWwzUtil;
import com.rocedar.sdk.familydoctor.dto.RCFDServiceStatusInfoDTO;
import com.rocedar.sdk.familydoctor.request.listener.fd.RCFDGetServerStatusListener;

/**
 * 项目名称：瑰柏SDK-基础库
 * <p>
 * 作者：phj
 * 日期：2018/6/28 下午4:26
 * 版本：V1.0.00
 * 描述：瑰柏SDK-
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class RCFDUtil {

    private RCFamilyDoctorWwzUtil wwzUtil;

    public void startAdvisory(Context activity, String phoneNo, String deviceNo) {
        if (wwzUtil == null)
            wwzUtil = new RCFamilyDoctorWwzUtil(activity, phoneNo, deviceNo);
        if (wwzUtil != null)
            wwzUtil.checkStatus(new RCFDGetServerStatusListener() {
                @Override
                public void getDataSuccess(RCFDServiceStatusInfoDTO serviceStatusInfoDTO) {
                    if (serviceStatusInfoDTO.isValid())
                        wwzUtil.getVideoPermission();
                    else {
                        wwzUtil.showBuyDialog();
                    }
                }

                @Override
                public void getDataError(int status, String msg) {

                }
            });
    }

}
