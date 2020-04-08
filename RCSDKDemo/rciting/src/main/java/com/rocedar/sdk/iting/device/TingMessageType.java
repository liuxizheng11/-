package com.rocedar.sdk.iting.device;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.appscomm.bluetoothsdk.app.SettingType;

/**
 * 项目名称：瑰柏SDK-商城
 * <p>
 * 作者：phj
 * 日期：2019/4/22 4:13 PM
 * 版本：V1.1.00
 * 描述：瑰柏SDK-服务商品
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class TingMessageType implements SettingType {


    //    private static List<String> pkgNameList = new ArrayList<>();
    private static List<String> noFilterPkgNameList = new ArrayList<>();
    private static List<String> noFilterInfoList = new ArrayList<>();
    private static List<String> noFilterPkgNameLikeList = new ArrayList<>();
    private static Map<String, Integer> typeList = new HashMap<>();

    public static void init() {
        typeList.put("com.tencent.mm", MESSAGE_WECHAT);
        typeList.put("com.tencent.mobileqq", MESSAGE_QQ);
        typeList.put("com.android.email", MESSAGE_EMAIL);
        typeList.put("com.android.mms", MESSAGE_SMS);
        typeList.put("com.android.server.telecom", MESSAGE_MISS_CALL);
        noFilterPkgNameList.add("android");
        noFilterPkgNameList.add("com.android.incallui");
        noFilterPkgNameLikeList.add("music");
        noFilterPkgNameLikeList.add("voice");
        noFilterPkgNameLikeList.add("player");
        noFilterInfoList.add("正在运行");
    }

//    public static void setPkgNameList(List<String> list) {
//        pkgNameList.addAll(list);
//    }
//
//    public static void setPkgNameList(String pkgName) {
//        pkgNameList.add(pkgName);
//    }
//
//    public static void deletePkgName(String pkgName) {
//        for (int i = 0; i < pkgNameList.size(); i++) {
//            if (pkgNameList.get(i).equals(pkgName)) {
//                pkgNameList.remove(i);
//                return;
//            }
//        }
//    }


    public static void setNoFilterPkgNameList(List<String> noFilterPkgNameList) {
        TingMessageType.noFilterPkgNameList = noFilterPkgNameList;
    }

    public static void setPkgType(String pkgName) {
        if (pkgName != null && !pkgName.equals("")) {
            typeList.put(pkgName, MESSAGE_SOCIAL);
        }
    }

    public static void setPkgType(String pkgName, int type) {
        if (pkgName != null && !pkgName.equals("") && type >= 0) {
            typeList.put(pkgName, type);
        }
    }

    public static boolean filter(String pkgName, String info) {
        for (int i = 0; i < noFilterPkgNameList.size(); i++) {
            if (pkgName.toLowerCase().equals(noFilterPkgNameList.get(i).toLowerCase())) {
                return false;
            }
        }
        for (int i = 0; i < noFilterPkgNameLikeList.size(); i++) {
            if (pkgName.toLowerCase().contains(noFilterPkgNameLikeList.get(i).toLowerCase())) {
                return false;
            }
        }
        for (int i = 0; i < noFilterInfoList.size(); i++) {
            if (info != null && !info.equals("null") && info.contains(noFilterInfoList.get(i))) {
                return false;
            }
        }
        return true;
    }

    public static boolean check(String pkgName) {
        if (typeList.containsKey(pkgName)) {
            return true;
        }
        return false;
    }

    public static int getType(String pkgName) {
        if (typeList.containsKey(pkgName)) {
            return typeList.get(pkgName);
        }
        return MESSAGE_SOCIAL;
    }
}
