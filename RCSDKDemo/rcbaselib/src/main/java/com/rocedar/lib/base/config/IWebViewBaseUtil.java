package com.rocedar.lib.base.config;

import android.app.Activity;

import java.util.Map;

/**
 * 项目名称：FangZhou2.1
 * <p>
 * 作者：phj
 * 日期：2017/9/28 下午5:47
 * 版本：V2.2.00
 * 描述：
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public interface IWebViewBaseUtil {

    /**
     * 添加请求头
     *
     * @return
     */
    Map<String, String> addHeadInfo();


    void jsShare(Activity mContext, String shareInfo);

}
