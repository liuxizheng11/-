package com.rocedar.base;

/**
 * 项目名称：设备管理平台
 * <p>
 * 作者：phj
 * 日期：2017/2/5 下午1:42
 * 版本：V1.0
 * 描述：
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public abstract class RCBaseConfig {


    //APP Tag
    public static final String APPTAG_DONGYA = "101";
    public static final String APPTAG_N3 = "102";

    //签名、密码计算用的乱序串
    private static final String SIGN_SECRET_KEY_DONGYA = "5e7545427d72c391";
    private static final String SIGN_SECRET_KEY_N3 = "12b24ee8b7dfdc3c";

    //动吖图片服务器地址、健康立方图片服务器地址
    private static String APPIMAGEURL_DONGYA = "http://img.dongya.rocedar.com";
    private static String APPIMAGEURL_N3 = "http://img.cubehealthy.com";

    //网络请求地址前缀
    private static String APP_NETWORK_URL_DONGYA = "http://dongya.rocedar.com/rest/3.0/";
    private static String APP_NETWORK_URL_N3 = "http://www.cubehealthy.com";

    //平台网络请求地址前缀
    private static String APP_PT_NETWORK_URL_DONGYA = "http://dongya.rocedar.com/";
    private static String APP_PT_NETWORK_URL_N3 = "http://www.cubehealthy.com";


    // TODO: 2017/3/3 以下为配置项，不同的应用需要配置不同的值

    /**
     * 平台的版本号
     * <p>
     * 1005：2017／11／14
     * 1110：2017/08/27 n3
     */
    public static final String version = "1005";
    /**
     * app标识，{动吖：101，健康立方：102}
     */
//    public static String APPTAG = APPTAG_N3;
    public static String APPTAG = APPTAG_DONGYA;

    /**
     * Sign Key ,用于签名计算{动吖：5e7545427d72c391，健康立方：12b24ee8b7dfdc3c}
     */
//    public static String NETWORK_SIGN_SECRET_KEY = SIGN_SECRET_KEY_N3;
    public static String NETWORK_SIGN_SECRET_KEY = SIGN_SECRET_KEY_DONGYA;

    /**
     * 图片网络地址前缀
     */
//    public static String APPIMAGEURL = APPIMAGEURL_N3;
    public static String APPIMAGEURL = APPIMAGEURL_DONGYA;

    /**
     * 网络地址前缀
     */
//    public static String APP_NETWORK_URL = APP_NETWORK_URL_N3;
    public static String APP_NETWORK_URL = APP_NETWORK_URL_DONGYA;


    /**
     * 平台网络地址前缀
     */
    public static String APP_PT_NETWORK_URL = APP_PT_NETWORK_URL_DONGYA;


    public static void setNetWorkUrl(String url) {
        RCBaseConfig.APP_NETWORK_URL = url;
    }

    public static void setPTNetWorkUrl(String url) {
        RCBaseConfig.APP_PT_NETWORK_URL = url;
    }


    /**
     * @param apptag
     */
    public static void setAPPTag(String apptag) {
        if (apptag.equals(APPTAG_DONGYA)) {
            APPTAG = APPTAG_DONGYA;
            NETWORK_SIGN_SECRET_KEY = SIGN_SECRET_KEY_DONGYA;
            APPIMAGEURL = APPIMAGEURL_DONGYA;
            APP_NETWORK_URL = APP_NETWORK_URL_DONGYA;
            APP_PT_NETWORK_URL = APP_PT_NETWORK_URL_DONGYA;
        } else {
            APPTAG = APPTAG_N3;
            NETWORK_SIGN_SECRET_KEY = SIGN_SECRET_KEY_N3;
            APPIMAGEURL = APPIMAGEURL_N3;
            APP_NETWORK_URL = APP_NETWORK_URL_N3;
            APP_PT_NETWORK_URL = APP_PT_NETWORK_URL_N3;
        }
    }

}
