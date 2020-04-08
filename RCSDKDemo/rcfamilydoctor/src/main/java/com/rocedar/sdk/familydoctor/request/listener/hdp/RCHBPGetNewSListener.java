package com.rocedar.sdk.familydoctor.request.listener.hdp;

/**
 * @author liuyi
 * @date 2017/11/28
 * @desc
 * @veison
 */

public interface RCHBPGetNewSListener {

    void getDataSuccess(int news);

    void getDataError(int status, String msg);
}
