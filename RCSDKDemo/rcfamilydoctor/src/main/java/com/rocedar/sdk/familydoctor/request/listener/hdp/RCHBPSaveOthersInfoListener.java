package com.rocedar.sdk.familydoctor.request.listener.hdp;

/**
 * @author liuyi
 * @date 2017/12/2
 * @desc
 * @veison
 */

public interface RCHBPSaveOthersInfoListener {
    /**
     * 病人id
     * @param userID
     */
    void getDataSuccess(long userID);

    void getDataError(int status, String msg);
}
