package com.rocedar.base.webview;

import android.app.Activity;
import android.content.Intent;

import com.rocedar.base.RCHeadUtil;

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

public interface IWebviewBaseUtil {

    /**
     * 头修改，主要是添加分享的按钮
     *
     * @param mContext
     * @param rcHeadUtil
     * @param url
     */
    void changeHeadUtil(Activity mContext, RCHeadUtil rcHeadUtil, String url);

    /**
     * 收到的为其它标识时，返回URL
     * ps：动吖中有收到（"other":"gostore"）类型的数据，跳转到线下商城地址页面
     *
     * @param intent
     */
    String otherFunctionInOpenChangeUrl(WebViewActivity mContext, Intent intent);

    /**
     * 收到的为其它标识时，返回类型
     * ps：（同上）
     *
     * @param intent
     */
    String otherFunctionInOpenChangeType(Intent intent);

}
