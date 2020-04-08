package com.rocedar.lib.sdk.scanner;

import android.content.Context;

/**
 * 项目名称：瑰柏SDK-基础库
 * <p>
 * 作者：phj
 * 日期：2018/7/15 下午9:39
 * 版本：V1.0.00
 * 描述：瑰柏SDK-
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class RCScannerManager {

    public static void init(Context context) {
        ZXingLibrary.initDisplayOpinion(context);//获取屏幕的尺寸，用于扫描二维码
    }

}
