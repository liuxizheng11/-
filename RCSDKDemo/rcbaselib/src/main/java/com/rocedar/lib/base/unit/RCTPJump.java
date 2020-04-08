package com.rocedar.lib.base.unit;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import com.rocedar.lib.base.RCWebViewActivity;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


/**
 * 项目名称：设备管理平台
 * <p>
 * 作者：phj
 * 日期：2017/2/14 下午6:47
 * 版本：V1.0
 * 描述：
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public abstract class RCTPJump {


    public static Map<String, String> replaceList = new HashMap<>();

    static {
        //单个指向


    }

    /**
     * 跳转到对应的页面
     *
     * @param context
     * @param url     打开的URL
     */
    public static void ActivityJump(Context context, String url) {
        ActivityJump(context, url, null);
    }


    /**
     * 跳转到对应的页面
     *
     * @param context
     * @param url     打开的URL
     */
    public static void ActivityJump(Context context, String url, String title) {
        RCLog.i("rctp_jump", "ActivityJump url : " + url);
        if (null == url || url.equals("")) {
            return;
        }
        if (url.toLowerCase().startsWith("http://") || url.toLowerCase().startsWith("https://")) {
            JumpToHTML(context, url, title);
        } else if (url.toLowerCase().startsWith("rctp://")) {
            String[] temps = url.split("##");
            if (temps.length == 3) {
                if (temps[0].toLowerCase().contains("android")) {
                    gotoActivity(context, temps[1], temps[2]);
                } else if (temps[0].toLowerCase().contains("h5")) {
                    String tempUrl = temps[1];
                    if (!temps[2].equals("") && !temps[2].equals("null"))
                        if (temps[1].contains("?")) {
                            tempUrl = tempUrl + "&v=" + temps[2];
                        } else {
                            tempUrl = tempUrl + "?v=" + temps[2];
                        }
                    JumpToHTML(context, tempUrl, title);
                } else if (temps[0].toLowerCase().contains("ios")) {
                    RCToast.Center(context, "请使用IPhone设备扫描", false);
                } else if (temps[0].toLowerCase().contains("pay")) {
                    JumpPayActivity(context, temps[1], temps[2]);
                }
            } else {
                RCToast.Center(context, "页面打开失败", false);
            }
        } else {
            AlertDialog.Builder dialog = new AlertDialog.Builder(context);
            dialog.setTitle("内容");
            dialog.setMessage(url);
            dialog.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            dialog.create().show();
        }
    }


    /**
     * 通过反射跳转到指定的Activity
     * <p/>
     * 提供给JS调用，如果要使用改方法，将所有的或需要使用的activity配置混淆
     *
     * @param context
     */
    public static void gotoActivity(Context context, String action, String params) {
        try {
            action = urlRepalce(action);
            RCLog.i("rctp_jump", "action : " + action);
            if (!action.equals("")) {
                Class forName = Class.forName(action);
                Intent intent = new Intent(context, forName);
                if (null != params && !params.equals("") && !params.equals("{}")) {
                    JSONObject object = new JSONObject(params);
                    Iterator itt = object.keys();
                    while (itt.hasNext()) {
                        String key = itt.next().toString();
                        Object param = object.get(key);
                        if (param == null) {
                            continue;
                        } else {
                            if (param instanceof Integer) {
                                int value = ((Integer) param).intValue();
                                intent.putExtra(key, value);
                            } else if (param instanceof String) {
                                String s = (String) param;
                                intent.putExtra(key, s);
                            } else if (param instanceof Double) {
                                double d = ((Double) param).doubleValue();
                                intent.putExtra(key, d);
                            } else if (param instanceof Float) {
                                float f = ((Float) param).floatValue();
                                intent.putExtra(key, f);
                            } else if (param instanceof Long) {
                                long l = ((Long) param).longValue();
                                intent.putExtra(key, l);
                            } else if (param instanceof Boolean) {
                                boolean b = ((Boolean) param).booleanValue();
                                intent.putExtra(key, b);
                            }
                        }
                    }
                }
                try {
                    context.startActivity(intent);
                } catch (Exception e) {
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }

            }
        } catch (Exception e) {
            RCToast.Center(context, "没有找到页面，请升级到最新版本。", false);
        }
    }

    /**
     * 版本兼容
     *
     * @param action
     * @return
     */
    private static String urlRepalce(String action) {
        for (Map.Entry<String, String> entry : replaceList.entrySet()) {
            if (action.contains(entry.getKey())) {
                return action.replace(entry.getKey(), entry.getValue());
            }
        }
        return action;
    }


    /**
     * 跳转到HTML页面
     *
     * @param mActivity Activity对象
     * @param Url       跳转的URL
     * @param title     显示的标题（不传默认显示HTMLtitle）
     */
    private static void JumpToHTML(Context mActivity, String Url, String title) {
        if (Url.trim().equals("")) {
            return;
        }
        RCWebViewActivity.goActivityNew(mActivity, Url, title);
    }


    public static void JumpActivity(Context context, String className, String param) {
        StringBuffer jumpInfo = new StringBuffer();
        jumpInfo.append("rctp://android##");
        jumpInfo.append(className);
        jumpInfo.append("##");
        jumpInfo.append(param);
        ActivityJump(context, jumpInfo.toString());
    }

    public static void JumpPayActivity(Context context, String url, String info) {
        try {
            Class forName = Class.forName("com.rocedar.sdk.shop.app.PayWebViewActivity");
            Intent intent = new Intent(context, forName);
            intent.putExtra("url", url);
            intent.putExtra("info", info);
            try {
                context.startActivity(intent);
            } catch (Exception e) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        } catch (Exception e) {
            RCToast.Center(context, "没有找到页面，请升级到最新版本。", false);
        }

    }

}
