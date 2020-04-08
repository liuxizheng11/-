package com.rocedar.base;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

/**
 * Created by phj on 16/3/7.
 * <p>
 * Toast封装
 */
public class RCToast {


    /**
     * (弹出在中间-文字)
     *
     * @param context
     * @param text    文字
     * @param isLong  是否LONG
     */
    public static void Center(Context context, String text, boolean isLong) {
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
        Toast toast = Toast.makeText(context, context.getText(text),
                isLong ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
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


}
