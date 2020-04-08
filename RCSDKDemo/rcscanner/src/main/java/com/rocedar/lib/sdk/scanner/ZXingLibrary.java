package com.rocedar.lib.sdk.scanner;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * @author liuyi
 * @date 2017/2/13
 * @desc 获取屏幕的分辨率，需要在application中初始化
 * @veison
 */

public class ZXingLibrary {

    public static void initDisplayOpinion(Context context) {
        if (context == null) {
            return;
        }
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        RCDisplayUtil.density = dm.density;
        RCDisplayUtil.densityDPI = dm.densityDpi;
        RCDisplayUtil.screenWidthPx = dm.widthPixels;
        RCDisplayUtil.screenhightPx = dm.heightPixels;
        RCDisplayUtil.screenWidthDip = RCDisplayUtil.px2dip(context, dm.widthPixels);
        RCDisplayUtil.screenHightDip = RCDisplayUtil.px2dip(context, dm.heightPixels);
    }
}
