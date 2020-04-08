package com.rocedar.base;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

import java.util.Date;

/**
 * Created by phj on 16/3/7.
 * <p>
 * Toast封装
 */
public abstract class RCToast {


    private static String tempText = "";
    private static long tempTime = -1;

    /**
     * (弹出在中间-文字)
     *
     * @param context
     * @param text    文字
     * @param isLong  是否LONG
     */
    public static void Center(Context context, String text, boolean isLong) {
        //3S内不弹同一个toast
        if (!tempText.equals("") && tempText.equals(text)) {
            if (tempTime > 0 && new Date().getTime() - tempTime < 1000) {
                return;
            }
        }
        tempText = text;
        tempTime = new Date().getTime();
        Toast toast = Toast.makeText(context, text, isLong ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    /**
     * (弹出在中间-文字)
     *
     * @param context
     * @param text    文字
     */
    public static void Center(Context context, String text) {
        Center(context, text, false);
    }

    /**
     * (弹出在中间-文字资源ID)
     *
     * @param context
     * @param text    文字资源ID
     * @param isLong  是否LONG
     */
    public static void Center(Context context, int text, boolean isLong) {
        Center(context, context.getString(text), isLong);
    }

    /**
     * (弹出在中间-文字资源ID)
     *
     * @param context
     * @param text    文字资源ID
     */
    public static void Center(Context context, int text) {
        Center(context, text, false);
    }


    /**
     * (弹出在中间-文字)
     *
     * @param context
     * @param text    文字
     */
    public static void TestCenter(Context context, String text) {
        TestCenter(context, text, false);
    }

    /**
     * (弹出在中间-文字)
     *
     * @param context
     * @param text    文字
     */
    public static void TestCenter(Context context, String text, boolean isLong) {
        if (RCDeveloperConfig.isDebug && RCDeveloperConfig.testToaseShow)
            Center(context, "(测试)" + text, isLong);
        RCLog.e("test", text);
    }

}
