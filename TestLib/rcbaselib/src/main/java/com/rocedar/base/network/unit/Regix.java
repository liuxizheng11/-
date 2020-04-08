package com.rocedar.base.network.unit;


import com.rocedar.base.RCBaseConfig;
import com.rocedar.base.RCLog;
import com.rocedar.base.network.RCBean;
import com.rocedar.base.unit.Encrypt;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * 反射机生成Post参数对象
 *
 * @param <T> 数据DTO
 * @author PHJ 2013-8-7
 */
public class Regix<T extends RCBean> {


    private static String TAG = "RC网络请求";


    /**
     * 生成网络请求参数
     *
     * @param t 网络Bean对象
     * @return
     */
    public Map<String, String> getdates(T t) {
        Map<String, String> params = new HashMap<String, String>();
        for (Field fd : t.getClass().getFields()) {
            try {
                String filedName = fd.getName();// 属性的名字
                //actionName\baseurl\\userId需要过滤
                if (!filedName.equals("actionName") && !filedName.equals("baseurl")
                        && !filedName.equals("baseUserId")) {
                    String firstLetter = filedName.substring(0, 1)
                            .toUpperCase(); // 获得字段第一个字母大写
                    String getMethodName = "get" + firstLetter
                            + filedName.substring(1); // 转换成字段的get方法
                    Method getMethod;
                    getMethod = t.getClass().getMethod(getMethodName,
                            new Class[]{});
                    String values = ""; // 这个对象字段get方法的值
                    if (getMethod.invoke(t, new Object[]{}) == null) {
                        values = "";
                    } else {
                        values = getMethod.invoke(t, new Object[]{})
                                .toString();
                        if (!values.equals("")) {
                            if (values.equals("@null")) {
                                params.put(filedName, "");
                            } else {
                                params.put(filedName, values);
                            }
                        }
                    }
                    RCLog.i(TAG, "网络参数：" + filedName + "<-->" + values);
                }
            } catch (NoSuchMethodException e) {
                RCLog.w(TAG, "没有该方法");
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return params;
    }

    public String geturl(T t) {
        String url = "";
        for (Field fd : t.getClass().getFields()) {
            try {
                String filedName = fd.getName();// 属性的名字
                //actionName\baseurl\\userId需要过滤
                if (!filedName.equals("actionName") && !filedName.equals("baseurl")
                        && !filedName.equals("baseUserId")) {
                    String firstLetter = filedName.substring(0, 1)
                            .toUpperCase(); // 获得字段第一个字母大写
                    String getMethodName = "get" + firstLetter
                            + filedName.substring(1); // 转换成字段的get方法
                    Method getMethod;
                    getMethod = t.getClass().getMethod(getMethodName,
                            new Class[]{});

                    String values = ""; // 这个对象字段get方法的值
                    if (getMethod.invoke(t, new Object[]{}) == null) {
                        values = "";
                    } else {
                        values = getMethod.invoke(t, new Object[]{})
                                .toString();
                        if (!values.equals("")) {
                            if (!values.equals("")) {
                                if (values.equals("@null")) {
                                    if (!url.equals("")) {
                                        url += "&";
                                    }
                                    url += (filedName + "=");
                                } else {
                                    if (!url.equals("")) {
                                        url += "&";
                                    }
                                    url += (filedName + "=" + values);
                                }
                            }
                        }
                    }
                    RCLog.i(TAG, "URL拼接参数：" + filedName + "<-->" + values);
                    RCLog.i(TAG, "URL拼接参数：url=" + url);
                }
            } catch (NoSuchMethodException e) {
                RCLog.w(TAG, "没有该方法");
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        if (url.equals("")) {
            return t.getActionName();
        }
        return t.getActionName() + "?" + url;
    }


    /**
     * 参数排序，用于sign校验生成密钥串
     *
     * @param t
     * @return
     */
    private SortedMap<String, String> getParams(T t) {
        SortedMap<String, String> mSortedMap = new TreeMap<>();
        for (Field fd : t.getClass().getFields()) {
            try {
                String filedName = fd.getName();// 属性的名字
                //actionName\baseurl\\userId需要过滤
                if (!filedName.equals("actionName") && !filedName.equals("baseurl")
                        && !filedName.equals("baseUserId")) {
                    String firstLetter = filedName.substring(0, 1)
                            .toUpperCase(); // 获得字段第一个字母大写
                    String getMethodName = "get" + firstLetter
                            + filedName.substring(1); // 转换成字段的get方法
                    Method getMethod;
                    getMethod = t.getClass().getMethod(getMethodName,
                            new Class[]{});
                    String values = ""; // 这个对象字段get方法的值

                    if (getMethod.invoke(t, new Object[]{}) == null) {
                    } else {
                        values = getMethod.invoke(t, new Object[]{})
                                .toString();
                        if (!values.equals("")) {
                            if (values.equals("@null")) {
                                mSortedMap.put(filedName, "");
                            } else {
                                mSortedMap.put(filedName, values);
                            }
                        }
                    }
                }
            } catch (NoSuchMethodException e) {
                RCLog.w(TAG, "没有该方法");
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return mSortedMap;
    }


    /**
     * 生成SignCode（密钥串）
     * 密钥生成规则(32位MD5串)：
     * MD5[请求类型大写（GET|POST|PUT|DELETE）+参数按字母顺序排序后的字符串(汉字逐个Encode)+app指定混淆串]
     *
     * @param t
     * @param methodName
     * @return
     */
    public String getSignCode(T t, String methodName) {
        String postBody = "";
        for (Map.Entry entry : getParams(t).entrySet()) {
            if (null != entry.getValue()) {
                if (!entry.getValue().equals("@null")) {
                    postBody += entry.getKey() + "=" + entry.getValue() + "";
                } else {
                    postBody += entry.getKey() + "=";
                }
            }
        }
        String sign = methodName + postBody + RCBaseConfig.NETWORK_SIGN_SECRET_KEY;
        try {
            sign = URLEncoder.encode(sign, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            RCLog.e(TAG, "密钥生成时转Encode出错");
        }
        RCLog.d(TAG, "密钥生成前的字符串：" + sign);
        RCLog.d(TAG, "密钥生成后的串： " + Encrypt.MD5StrLower32(sign));
        return Encrypt.MD5StrLower32(sign);
    }


}