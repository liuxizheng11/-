package com.rocedar.base;

import android.content.Context;

import com.rocedar.base.shareprefernces.RCSPBaseInfo;
import com.rocedar.base.webview.WebViewActivity;

/**
 * 项目名称：DongYa3.0
 * <p>
 * 作者：phj
 * 日期：2017/7/17 上午11:43
 * 版本：V2.2.00
 * 描述：
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public abstract class RCURLConfig {


    public enum ServerUrl {
        XinLiCePing, GuaHaoPeiZhen, ZhiNengWenZhen, JueShiHaoYi, JianKe, DingDang;
    }

    /**
     * 心理测评：/p/server/1309001/?p_token=xxxx
     * 挂号陪诊：/p/server/1302001/?p_token=xxxx
     * 智能问诊：/p/server/1303001/?p_token=xxxx
     * 绝世好医：/p/server/1304002/?p_token=xxxx
     * 健客：/p/server/1306001/?p_token=xxxx
     * 叮当：/p/server07001/?p_token=xxxx
     */

    public static void openService(Context context, ServerUrl serverUrl) {
        String url = "";
        switch (serverUrl) {
            case XinLiCePing:
                url = get9001URL();
                break;
            case GuaHaoPeiZhen:
                url = get2001URL();
                break;
            case ZhiNengWenZhen:
                url = get3001URL();
                break;
            case JueShiHaoYi:
                url = get4002URL();
                break;
            case JianKe:
                url = get6001URL();
                break;
            case DingDang:
                url = get7001URL();
                break;
        }
        WebViewActivity.goActivity(context, url);
    }


    /**
     * 心理测评页面URL
     */
    private static String get9001URL() {
        return getPTURL() + "p/server/1309001/?p_token=" + RCSPBaseInfo.getLastToken() + "&notJump=1";
    }

    /**
     * 挂号陪诊页面URL
     */
    private static String get2001URL() {
        return getPTURL() + "p/server/1302001/?p_token=" + RCSPBaseInfo.getLastToken()+ "&notJump=1";
    }

    /**
     * 智能问诊页面URL
     */
    private static String get3001URL() {
        return getPTURL() + "p/server/1303001/?p_token=" + RCSPBaseInfo.getLastToken()+ "&notJump=1";
    }

    /**
     * 绝世好医页面URL
     */
    private static String get4002URL() {
        return getPTURL() + "p/server/1304002/?p_token=" + RCSPBaseInfo.getLastToken()+ "&notJump=1";
    }

    /**
     * 健客页面URL
     */
    private static String get6001URL() {
        return getPTURL() + "p/server/1306001/?p_token=" + RCSPBaseInfo.getLastToken()+ "&notJump=1";
    }


    /**
     * 叮当页面URL
     */
    private static String get7001URL() {
        return getPTURL() + "p/server/1307001/?p_token=" + RCSPBaseInfo.getLastToken()+ "&notJump=1";
    }


    private static String getPTURL() {
        return RCBaseConfig.APP_PT_NETWORK_URL + (RCBaseConfig.APP_PT_NETWORK_URL.endsWith("/") ? "" : "/");
    }


}
