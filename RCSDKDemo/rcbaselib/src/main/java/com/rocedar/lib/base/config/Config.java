package com.rocedar.lib.base.config;

import com.rocedar.lib.base.network.NetworkMethod;

/**
 * 项目名称：瑰柏SDK-基础库
 * <p>
 * 作者：phj
 * 日期：2018/6/9 下午2:02
 * 版本：V1.0.00
 * 描述：瑰柏SDK-基本配置项
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class Config {

    public static String BASE_URL = "dongya.rocedar.com";

    public static String P_NETWORK_URL = "https://" + BASE_URL + "/";

    public static String APPIMAGEURL = "http://img." + BASE_URL + "/";

    //平台版本号
    public static String p_version = "1201";

    public static String SDKSECRET = "0089ee4d39f6943929963ef8";

    public static NetworkMethod NETWORK_METHOD = NetworkMethod.SDK;

    public static boolean debug = false;


}
