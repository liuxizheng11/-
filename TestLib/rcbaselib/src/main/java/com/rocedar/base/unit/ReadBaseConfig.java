package com.rocedar.base.unit;

import com.rocedar.base.RCJavaUtil;
import com.rocedar.base.RCLog;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 项目名称：设备管理平台
 * <p>
 * 作者：phj
 * 日期：2017/8/31 下午6:59
 * 版本：V2.2.00
 * 描述：
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public class ReadBaseConfig {

    private static final String CONFIG = "baseLibConfig";


    private static JSONObject getAllInfo() {
        try {
            return new JSONObject(RCJavaUtil.getFromAssets(CONFIG));
        } catch (JSONException e) {
            RCLog.e("－－－找不到任务数据");
            return new JSONObject();
        }
    }

    private static final String KEY_WEBVIEW = "webview";

    /**
     * WebView配置实现类
     *
     * @return
     */
    public static String getWebviewClass() {
        String className = getAllInfo().optString(KEY_WEBVIEW);
        return className != null ? className : "";
    }


}
