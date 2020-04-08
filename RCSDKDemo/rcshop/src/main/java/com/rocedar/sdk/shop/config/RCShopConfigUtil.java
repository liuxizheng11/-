package com.rocedar.sdk.shop.config;

import com.rocedar.lib.base.config.RCSPUtilInfo;
import com.rocedar.lib.base.unit.RCLog;

/**
 * 项目名称：瑰柏SDK-商城
 * <p>
 * 作者：phj
 * 日期：2018/5/22 下午6:12
 * 版本：V1.0.00
 * 描述：瑰柏SDK-商城配置项
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class RCShopConfigUtil {


    private static String TAG = RCShopConfigUtil.class.getName();


    public static void setClassPath(Class c) {
        RCSPUtilInfo.saveClassPath(SHOPCONFIGCLASSKEY, c.getName());
        iRCFDConfig = null;
    }

    private static final String SHOPCONFIGCLASSKEY = "shop_config";

    private static IRCShopConfig iRCFDConfig;

    public static IRCShopConfig getConfig() {
        if (iRCFDConfig != null) return iRCFDConfig;
        if (!RCSPUtilInfo.getClassPath(SHOPCONFIGCLASSKEY).equals("")) {
            try {
                Class classInfo = Class.forName(RCSPUtilInfo.getClassPath(SHOPCONFIGCLASSKEY));
                if (classInfo.newInstance() instanceof IRCShopConfig)
                    iRCFDConfig = (IRCShopConfig) Class.forName(RCSPUtilInfo.
                            getClassPath(SHOPCONFIGCLASSKEY)).newInstance();
            } catch (Exception e) {
                e.printStackTrace();
                RCLog.e(TAG, "类对象获取失败");
            }
        }
        return iRCFDConfig;
    }


    public static void setGoodsClassPath(Class c) {
        RCSPUtilInfo.saveClassPath(SHOP_GOODS_CONFIG_CLASS_KEY, c.getName());
        iRCGoodsConfig = null;
    }

    private static final String SHOP_GOODS_CONFIG_CLASS_KEY = "shop_goods_config";

    private static IRCGoodsConfig iRCGoodsConfig;

    public static IRCGoodsConfig getGoodsConfig() {
        if (iRCGoodsConfig != null) return iRCGoodsConfig;
        if (!RCSPUtilInfo.getClassPath(SHOP_GOODS_CONFIG_CLASS_KEY).equals("")) {
            try {
                Class classInfo = Class.forName(RCSPUtilInfo.getClassPath(SHOP_GOODS_CONFIG_CLASS_KEY));
                if (classInfo.newInstance() instanceof IRCGoodsConfig)
                    iRCGoodsConfig = (IRCGoodsConfig) Class.forName(RCSPUtilInfo.
                            getClassPath(SHOP_GOODS_CONFIG_CLASS_KEY)).newInstance();
            } catch (Exception e) {
                e.printStackTrace();
                RCLog.e(TAG, "类对象获取失败");
            }
        }
        return iRCGoodsConfig;
    }






}
