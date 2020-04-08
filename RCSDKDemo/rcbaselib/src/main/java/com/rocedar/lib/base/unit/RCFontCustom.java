package com.rocedar.lib.base.unit;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/**
 * 项目名称：瑰柏SDK-基础库
 * <p>
 * 作者：phj
 * 日期：2018/6/27 下午4:38
 * 版本：V1.0.00
 * 描述：瑰柏SDK-
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class RCFontCustom {

    static String fongUrl_Helverical_Roman = "fonts/HelveticaInserat-Roman.otf";
    static String fongUrl_Helverical_Avenir = "fonts/Avenir.ttc";


    static Typeface tf;

    /***
     * 设置细数字字体（Roman）
     *
     * @param context
     * @param view    要设置字体的View（支持TextView和EditView）
     */
    public static void getRomanFont(Context context, View view) {
        if (tf == null) {
            tf = Typeface.createFromAsset(context.getAssets(), fongUrl_Helverical_Roman);
        }
        setTf(tf, view);
    }

    public static Typeface getTf(Context context) {
        if (tf == null) {
            tf = Typeface.createFromAsset(context.getAssets(), fongUrl_Helverical_Roman);
        }
        return tf;
    }
    /***
     * 设置细数字字体（Avenir）
     *
     * @param context
     * @param view    要设置字体的View（支持TextView和EditView）
     */
    public static void getAvenirFont(Context context, View view) {
        if (tf == null) {
            tf = Typeface.createFromAsset(context.getAssets(), fongUrl_Helverical_Avenir);
        }
        setTf(tf, view);
    }

    /**
     * 将字体设置到View中
     *
     * @param tf   Typeface对象
     * @param view View对象
     */
    private static void setTf(Typeface tf, View view) {
        if (tf != null) {
            if (view instanceof TextView)
                ((TextView) view).setTypeface(tf);
            else if (view instanceof EditText)
                ((EditText) view).setTypeface(tf);
        }
    }


}
