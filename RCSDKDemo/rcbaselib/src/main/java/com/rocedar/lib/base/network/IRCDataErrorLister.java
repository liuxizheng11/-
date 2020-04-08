package com.rocedar.lib.base.network;

import android.content.Context;

/**
 * Created by phj on 2016/12/17.
 * <p>
 * 网络请求异常，需要全局处理的错误异常监听
 */

public interface IRCDataErrorLister {

    void error(Context context, int code, String msg);


}
