package com.rocedar.sdk.familydoctor.request.listener.hdp;

/**
 * @author liuyi
 * @date 2017/12/2
 * @desc
 * @veison
 */

public interface RCHBPSaveConsultRecordListener {

    void getDataSuccess(int quesitionID, String auto_record);

    void getDataError(int status, String msg);
}
