package com.rocedar.base;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import com.rocedar.base.scanner.ScannerResultActivity;
import com.rocedar.base.shareprefernces.RCSPBaseInfo;
import com.rocedar.base.webview.WebViewActivity;

import org.json.JSONObject;

import java.util.Iterator;


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


    /**
     * 跳转到对应的页面
     *
     * @param context
     * @param url     打开的URL
     */
    public static void ActivityJump(Context context, String url) {
        ActivityJump(context, url, false);
    }

    public static void ActivityJump(Context context, String url, boolean isScan) {
        ActivityJump(context, url, isScan, "", null);
    }

    /**
     * //     * 跳转到对应的页面（读取HTML标题）
     * //     *
     * //     * @param context
     * //     * @param url        打开的URL
     * //     * @param actionType HTML页面类型
     * //
     */
    public static void ActivityJump(Context context, String url, String actionType) {
        ActivityJump(context, url, false, actionType, null);
    }

    public static void ActivityJump(Context context, String url, String actionType, boolean isScan) {
        ActivityJump(context, url, isScan, actionType, null);
    }


    /**
     * 跳转到对应的页面
     *
     * @param context
     * @param url     打开的URL
     */
    public static void ActivityJump(Context context, String url, boolean isScan, String actionType, String title) {
        RCLog.i("rctp_jump", "ActivityJump url : " + url);
        if (null == url || url.equals("")) {
            /** 扫描结果为空时处理*/
            if (isScan) {
                ScannerResultActivity.goActivity(context);
//              Toast.makeText(context, "扫描失败，请重试！", Toast.LENGTH_SHORT).show();
                return;
            }
//            rctp://android##home.MyHealthy.HealthQuestionnaireActivity##{}
            return;
        }
        if (url.startsWith("http://") || url.startsWith("https://")) {
            if (url.contains(RCBaseConfig.APP_NETWORK_URL) && !url.contains("token="))
                if (url.contains("?")) {
                    url = url + "&token=" + RCSPBaseInfo.getLastToken()
                            + "&os=1&app_version=" + RCAndroid.getVerNumber(context);
                } else {
                    url = url + "?token=" + RCSPBaseInfo.getLastToken()
                            + "&os=1&app_version=" + RCAndroid.getVerNumber(context);
                }
            JumpToHTML(context, url, title, actionType);
        } else if (url.startsWith("rctp://")) {
            String[] temps = url.split("##");
            if (temps.length == 3) {
                if (temps[0].contains("android")) {
                    gotoActivity(context, temps[1], temps[2]);
                } else if (temps[0].contains("h5")) {
                    String tempUrl = RCBaseConfig.APP_NETWORK_URL + temps[1];
                    if (temps[1].contains("?")) {
                        tempUrl = tempUrl + "&v=" + temps[2];
                    } else {
                        tempUrl = tempUrl + "?v=" + temps[2];
                    }
                    tempUrl = tempUrl + ((!tempUrl.contains("token=")) ? "&token=" +
                            RCSPBaseInfo.getLastToken() : "")
                            + "&os=1&app_version=" + RCAndroid.getVerNumber(context);
                    JumpToHTML(context, tempUrl, title, actionType);
                } else if (temps[0].contains("ios")) {
                    RCToast.Center(context, "请使用IPhone设备扫描", false);
                } else {
                    if (isScan) {
                        ScannerResultActivity.goActivity(context);
//                        RCToast.Center(context, "不支持该二维码，请重试！", false);
                    }
                }
            } else {
                if (isScan) {
                    ScannerResultActivity.goActivity(context);
//                    RCToast.Center(context, "不支持该二维码，请重试！", false);
                }
            }
        } else if (url.startsWith("<dongya>")) {
            /**
             *model##function##params
             * 内部链接，使用浏览器内部打开，需要拼接URL，如：http://192.168.18.25/model/function/scan/?v=param，需添加token、
             * os（系统类型，1为Android，2为iOS）、app_version（APP内部版本号，1000），其中拼接params时，需要判断url中是否已经有参数
             */
            String[] temps = url.split("##");
            if (temps.length == 3) {
                String tempUrl = RCBaseConfig.APP_NETWORK_URL
                        + "/" + temps[0].replace("<dongya>", "") + "/" + temps[1] + "/";
                if (temps[1].contains("?")) {
                    tempUrl = tempUrl + "&v=" + temps[2];
                } else {
                    tempUrl = tempUrl + "?v=" + temps[2];
                }
                tempUrl = tempUrl + "&token=" + RCSPBaseInfo.getLastToken()
                        + "&os=1&app_version=" + RCAndroid.getVerNumber(context);
                JumpToHTML(context, tempUrl, title, actionType);
            } else {
                if (isScan) {
                    ScannerResultActivity.goActivity(context);
//                    RCToast.Center(context, "不支持该二维码，请重试！", false);
                }
            }
        } else {
            if (isScan) {
                ScannerResultActivity.goActivity(context);
//                RCToast.Center(context, "不支持该二维码，请重试！", false);
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
            //对历史版本兼容
            if (!action.startsWith("com")) {
                if (RCBaseConfig.APPTAG == RCBaseConfig.APPTAG_DONGYA)
                    action = "com.rocedar.app." + action;
                if (RCBaseConfig.APPTAG == RCBaseConfig.APPTAG_N3)
                    action = "com.huanyi.app.activity." + action;
            }
            action = urlRepalce(action);
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
                    if (RCBaseManage.getScreenManger().getActivityListCount() == 0)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
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
        //动吖3400中微问诊实现移动到平台，对微问诊转发
        if (action.equals("com.rocedar.app.healthy.FamilyDoctorActivity")) {
            return "com.rocedar.deviceplatform.app.familydoctor.FamilyDoctorActivity";
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
    private static void JumpToHTML(Context mActivity, String Url, String title, String action) {
        if (Url.trim().equals("")) {
            return;
        }
        if (!Url.startsWith("http")) {
            Url = RCBaseConfig.APP_NETWORK_URL + Url;
        }
        Intent intentFA = new Intent(mActivity, WebViewActivity.class);
        if (title != null && !title.equals(""))
            intentFA.putExtra(WebViewActivity.EXTRA_TITLE, title);
        intentFA.putExtra(WebViewActivity.EXTRA_URL, Url);
        intentFA.putExtra(WebViewActivity.EXTRA_ACTION, action);
        intentFA.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mActivity.startActivity(intentFA);
    }


}
