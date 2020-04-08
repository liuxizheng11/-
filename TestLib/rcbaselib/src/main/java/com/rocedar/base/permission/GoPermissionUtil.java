package com.rocedar.base.permission;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.view.View;

import com.rocedar.base.RCDialog;

/**
 * 项目名称：TestLib
 * <p>
 * 作者：phj
 * 日期：2017/8/17 上午10:15
 * 版本：V2.2.00
 * 描述：
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public class GoPermissionUtil {


    private static RCDialog rcDialog;

    private static void showInfo(Context context, String info, View.OnClickListener clickListener) {
        rcDialog = new RCDialog(context, new String[]{null, info, null, null}, null, clickListener);
        rcDialog.show();
    }

    public static void gotoPermission6(final Context context) {
        Intent intent = null;
        if (Build.MANUFACTURER.toUpperCase().equals("XIAOMI")) {
            intent = gotoMiuiPermission(context);
        } else if (Build.MANUFACTURER.toUpperCase().equals("MEIZU")) {
            intent = gotoMeizuPermission(context);
        } else if (Build.MANUFACTURER.toUpperCase().equals("SONY")) {
            intent = gotoSonyPermission(context);
        } else if (Build.MANUFACTURER.toUpperCase().equals("HUAWEI")) {
            intent = gotoHuaweiPermission();
        } else if (Build.MANUFACTURER.toUpperCase().equals("OPPO")) {
            intent = gotoOppoPermission(context);
        } else if (Build.MANUFACTURER.toUpperCase().equals("LG")) {
            intent = gotoLGPermission(context);
        } else if (Build.MANUFACTURER.toUpperCase().equals("LETV")) {
            intent = gotoLeTvPermission(context);
        } else if (Build.MANUFACTURER.toUpperCase().equals("VIVO")) {
            showInfo(context, "请在【i管家】->【软件管理】->【软件权限管理】->【软件】打开应用权限", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (rcDialog != null)
                        rcDialog.dismiss();
                    try {
                        context.startActivity(gotoVivoPermission(context));
                    } catch (Exception e) {
                        e.printStackTrace();
                        context.startActivity(getAppDetailSettingIntent(context));
                    }
                }
            });
            return;
        } else {
            intent = getAppDetailSettingIntent6();
        }
        if (intent != null) {
            try {
                context.startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
                context.startActivity(getAppDetailSettingIntent6());
            }
        } else {
            context.startActivity(getAppDetailSettingIntent6());
        }


    }


    /**
     * 华为——Huawei
     * 魅族——Meizu
     * 小米——Xiaomi
     * 索尼——Sony
     * oppo——OPPO
     * LG——LG
     * vivo——vivo
     * 三星——samsung
     * 乐视——Letv
     * 中兴——ZTE
     * 酷派——YuLong
     * 联想——LENOVO
     *
     * @param context
     */
    public static void gotoPermission(final Context context) {
        Intent intent = null;
        if (Build.MANUFACTURER.toUpperCase().equals("XIAOMI")) {
            intent = gotoMiuiPermission(context);
        } else if (Build.MANUFACTURER.toUpperCase().equals("MEIZU")) {
            intent = gotoMeizuPermission(context);
        } else if (Build.MANUFACTURER.toUpperCase().equals("SONY")) {
            intent = gotoSonyPermission(context);
        } else if (Build.MANUFACTURER.toUpperCase().equals("HUAWEI")) {
            intent = gotoHuaweiPermission();
        } else if (Build.MANUFACTURER.toUpperCase().equals("OPPO")) {
            intent = gotoOppoPermission(context);
        } else if (Build.MANUFACTURER.toUpperCase().equals("LG")) {
            intent = gotoLGPermission(context);
        } else if (Build.MANUFACTURER.toUpperCase().equals("LETV")) {
            intent = gotoLeTvPermission(context);
        } else if (Build.MANUFACTURER.toUpperCase().equals("VIVO")) {
            showInfo(context, "请在i管家中选择【软件管理】->【软件权限管理】->【软件】打开应用权限", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (rcDialog != null)
                        rcDialog.dismiss();
                    try {
                        context.startActivity(gotoVivoPermission(context));
                    } catch (Exception e) {
                        e.printStackTrace();
                        context.startActivity(getAppDetailSettingIntent(context));
                    }
                }
            });
            return;
        } else {
            intent = getAppDetailSettingIntent(context);
        }
        if (intent != null) {
            try {
                context.startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
                context.startActivity(getAppDetailSettingIntent(context));
            }
        } else {
            context.startActivity(getAppDetailSettingIntent(context));
        }
    }


    /**
     * 跳转到miui的权限管理页面
     */
    private static Intent gotoMiuiPermission(Context context) {
//        String rom = getMiuiVersion();
        Intent intent = null;
//        if ("V5".equals(rom)) {
//            Uri packageURI = Uri.parse("package:" + context.getApplicationInfo().packageName);
//            intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
//        } else if ("V6".equals(rom) || "V7".equals(rom)) {
        intent = new Intent("miui.intent.action.APP_PERM_EDITOR");
        intent.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.AppPermissionsEditorActivity");
        intent.putExtra("extra_pkgname", context.getPackageName());
//        }
        return intent;
    }

    /**
     * 华为的权限管理页面
     */
    private static Intent gotoHuaweiPermission() {
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ComponentName comp = new ComponentName("com.huawei.systemmanager", "com.huawei.permissionmanager.ui.MainActivity");//华为权限管理
        intent.setComponent(comp);
        return intent;
    }


    /**
     * 跳转到魅族的权限管理系统
     */
    private static Intent gotoMeizuPermission(Context context) {
        Intent intent = new Intent("com.meizu.safe.security.SHOW_APPSEC");
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.putExtra("packageName", context.getPackageName());
        return intent;
    }

    /**
     * 跳转到魅族的权限管理系统
     *
     * @return
     */
    private static Intent gotoSonyPermission(Context context) {
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("packageName", context.getPackageName());
        ComponentName comp = new ComponentName("com.sonymobile.cta", "com.sonymobile.cta.SomcCTAMainActivity");
        intent.setComponent(comp);
        return intent;
    }

    /**
     * 跳转到Oppo的权限管理系统
     *
     * @return
     */
    private static Intent gotoOppoPermission(Context context) {
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("packageName", context.getPackageName());
        ComponentName comp = new ComponentName("com.color.safecenter", "com.color.safecenter.permission.PermissionManagerActivity");
        intent.setComponent(comp);
        return intent;
    }

    /**
     * 跳转到Oppo的权限管理系统
     *
     * @return
     */
    private static Intent gotoVivoPermission(Context context) {
        Intent appIntent = context.getPackageManager().getLaunchIntentForPackage("com.iqoo.secure");
        if (appIntent != null) {
            return appIntent;
        }
        return null;
    }


    /**
     * 跳转到LG的权限管理系统
     *
     * @return
     */
    private static Intent gotoLGPermission(Context context) {
        Intent intent = new Intent("android.intent.action.MAIN");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("packageName", context.getPackageName());
        ComponentName comp = new ComponentName("com.android.settings", "com.android.settings.Settings$AccessLockSummaryActivity");
        intent.setComponent(comp);
        return intent;
    }

    /**
     * 跳转到LG的权限管理系统
     *
     * @return
     */
    private static Intent gotoLeTvPermission(Context context) {
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("packageName", context.getPackageName());
        ComponentName comp = new ComponentName("com.letv.android.letvsafe", "com.letv.android.letvsafe.PermissionAndApps");
        intent.setComponent(comp);
        return intent;
    }


    /**
     * 获取应用详情页面intent
     *
     * @return
     */
    private static Intent getAppDetailSettingIntent(Context context) {
        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            localIntent.setData(Uri.fromParts("package", context.getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            localIntent.setAction(Intent.ACTION_VIEW);
            localIntent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            localIntent.putExtra("com.android.settings.ApplicationPkgName", context.getPackageName());
        }
        return localIntent;
    }

    /**
     * 获取应用详情页面intent
     *
     * @return
     */
    private static Intent getAppDetailSettingIntent6() {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.MAIN");
        intent.setClassName("com.android.settings", "com.android.settings.ManageApplications");
        return intent;
    }


//    /**
//     * 获取MIUI系统版本
//     */
//    private static String getMiuiVersion() {
//        String propName = "ro.miui.ui.version.name";
//        String line;
//        BufferedReader input = null;
//        try {
//            Process p = Runtime.getRuntime().exec("getprop " + propName);
//            input = new BufferedReader(
//                    new InputStreamReader(p.getInputStream()), 1024);
//            line = input.readLine();
//            input.close();
//        } catch (IOException ex) {
//            return null;
//        } finally {
//            if (input != null) {
//                try {
//                    input.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//        return line;
//    }

}
