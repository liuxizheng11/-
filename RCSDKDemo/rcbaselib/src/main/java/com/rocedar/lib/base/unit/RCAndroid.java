package com.rocedar.lib.base.unit;

import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import com.rocedar.lib.base.network.unit.NetWorkUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.List;

/**
 * 项目名称：瑰柏SDK-基础库
 * <p>
 * 作者：phj
 * 日期：2018/5/22 上午10:13
 * 版本：V1.0.00
 * 描述：瑰柏SDK-Android工具类
 * <p>
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class RCAndroid {


    /**
     * 获取自定义颜色
     *
     * @param context
     * @param attrName
     * @return
     */
    public static int getAttColor(Context context, int attrName) {
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(attrName, typedValue, true);
        return Color.parseColor(typedValue.coerceToString().toString());
    }


    /**
     * 获取设备Imei号
     *
     * @param context
     * @return
     */
    public static String getImei(Context context) {
        try {
            TelephonyManager TelephonyMgr = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            String szImei = TelephonyMgr.getDeviceId();
            return szImei;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    // ---------Other--------

    /**
     * 获取App安装包信息
     *
     * @return
     */
    public static PackageInfo getPackageInfo(Context context) {
        PackageInfo info = null;
        try {
            info = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return info;
    }


    /**
     * 获取应用程序名称
     */
    public static synchronized String getAppName(Context context) {
        try {
            int labelRes = getPackageInfo(context).applicationInfo.labelRes;
            return context.getResources().getString(labelRes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取图标 bitmap
     *
     * @param context
     */
    public static synchronized Bitmap getBitmap(Context context) {
        PackageManager packageManager = null;
        ApplicationInfo applicationInfo = null;
        try {
            packageManager = context.getApplicationContext()
                    .getPackageManager();
            applicationInfo = packageManager.getApplicationInfo(
                    context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            applicationInfo = null;
        }
        Drawable d = packageManager.getApplicationIcon(applicationInfo); //xxx根据自己的情况获取drawable
        BitmapDrawable bd = (BitmapDrawable) d;
        Bitmap bm = bd.getBitmap();
        return bm;
    }


    /**
     * 读取版本名称
     *
     * @param context
     * @return 版本名称(String)
     */
    public static String getVerCode(Context context) {
        try {
            return getPackageInfo(context).versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 读取版本号
     *
     * @param context
     * @return 版本号(int)
     */
    public static int getVerNumber(Context context) {
        try {
            return getPackageInfo(context).versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 1;
    }


    /**
     * 检测应用
     */

    public static boolean isAvilible(Context context, String packageName) {
        final PackageManager packageManager = context.getPackageManager();
        // 获取所有已安装程序的包信息
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        for (int i = 0; i < pinfo.size(); i++) {
            if (pinfo.get(i).packageName.equalsIgnoreCase(packageName))
                return true;
        }
        return false;
    }

    /**
     * 检测是否安装了指定应用
     * true 已安装  false 未安装
     */
    public static boolean isDownload(Context context, String pack) {
        if (isAvilible(context, pack)) {
            return true;
        }
        return false;
    }

    /**
     * 通过包名开启已安装 程序
     */
    public static void RunApp(Context context, String packageName) {
        PackageInfo pi;
        try {
            pi = context.getPackageManager().getPackageInfo(packageName, 0);
            Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
            resolveIntent.setPackage(pi.packageName);
            PackageManager pManager = context.getPackageManager();
            List apps = pManager.queryIntentActivities(
                    resolveIntent, 0);

            ResolveInfo ri = (ResolveInfo) apps.iterator().next();
            if (ri != null) {
                packageName = ri.activityInfo.packageName;
                String className = ri.activityInfo.name;
                Intent intent = new Intent(Intent.ACTION_MAIN);
                ComponentName cn = new ComponentName(packageName, className);
                intent.setComponent(cn);
                context.startActivity(intent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private static String[] known_imsi_ids = {"310260000000000"};

    /**
     * 是否是模拟器
     *
     * @param context
     * @return
     */
    public static boolean isEmulator(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        try {
            String imei = tm.getDeviceId();
            if (imei != null && imei.equals("000000000000000")) {
                return true;
            }
        } catch (Exception ioe) {
        }
        try {
            String imsi_ids = tm.getSubscriberId();
            for (String know_imsi : known_imsi_ids) {
                if (know_imsi.equalsIgnoreCase(imsi_ids)) {
                    return true;
                }
            }
        } catch (Exception e) {
        }
        return (Build.MODEL.equals("sdk")) || (Build.MODEL.equals("google_sdk") ||
                (Build.MODEL.toUpperCase().startsWith("android sdk".toUpperCase())));
    }

    /**
     * 获取状态栏高度
     *
     * @return
     */
    public static int getStatusBarHeight(Activity activity) {

        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0;
        int sbar = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            sbar = activity.getApplicationContext().getResources()
                    .getDimensionPixelSize
                            (x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return sbar;
    }

    /**
     * 获取屏幕的宽
     *
     * @param context
     * @return
     */
    public static int getSrceenWidth(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.widthPixels;
    }

    /**
     * 获取屏幕的高
     *
     * @param context
     * @return
     */
    public static int getSrceenHeight(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.heightPixels;
    }

    /**
     * 获取屏幕的密度
     *
     * @param context
     * @return
     */
    public static float getSrceenDensity(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.density;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = getSrceenDensity(context);
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 获取FileProvider名称
     *
     * @param context
     * @return
     */
    public static String getFileProviderName(Context context) {
        return context.getPackageName() + ".base.FileProvider";
    }


    /**
     * 获取配置项
     *
     * @param context
     * @param keyName
     * @return
     */
    public static String getMetaData(Context context, String keyName) {
        ApplicationInfo appInfo = null;
        try {
            appInfo = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(),
                            PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String msg = "";
        if (appInfo != null) {
            try {
                msg = appInfo.metaData.get(keyName).toString();
                if (msg == null) msg = "";
            } catch (ClassCastException e) {
                e.printStackTrace();
            }
        }
        return msg;
    }


    /**
     * 判断GPS是否开启，GPS或者AGPS开启一个就认为是开启的
     * <p>
     * * @return true 表示开启
     */
    public static final boolean isGPSOPen(final Context context) {
        LocationManager locationManager
                = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        // 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        // 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）
        boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (gps) {
            return true;
        }
        return false;

    }

    /**
     * 从assets 文件夹中获取文件并读取数据
     *
     * @param fileName
     * @return
     */
    public static String getFromAssets(Context context, String fileName) {
        String result = "";
        // Return an AssetManager instance for your application's package
        InputStream is;
        try {
            is = context.getAssets().open(fileName);
            int size = is.available();

            // Read the entire asset into a local byte buffer.
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            result = new String(buffer, "UTF-8");
            // Convert the buffer into a string.
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }


    /**
     * 检查当前是否是WIFI网络
     *
     * @param context
     * @return
     */
    public static boolean isWifiNetWork(Context context) {
        return NetWorkUtil.networkAvailable(context).equals("wifi");

    }


    /**
     * 改变颜色的透明度
     *
     * @param color    颜色
     * @param fraction 百分比
     * @return
     */
    public static int changeAlpha(int color, float fraction) {
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        int alpha = (int) (Color.alpha(color) * fraction);
        return Color.argb(alpha, red, green, blue);
    }

    /**
     * 获取一张图片的Uri地址
     *
     * @param context
     * @param imageFileUrl 图片路径
     * @return
     */
    public static Uri getImageContentUri(Context context, String imageFileUrl) {
        return getImageContentUri(context, new File(imageFileUrl));
    }


    /**
     * 获取一张图片的Uri地址
     *
     * @param context
     * @param imageFile 图片文件对象
     * @return
     */
    public static Uri getImageContentUri(Context context, java.io.File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media._ID}, MediaStore.Images.Media.DATA + "=? ",
                new String[]{filePath}, null);
        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }


}
