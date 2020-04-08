package com.rocedar.lib.base.unit.statuscolor;

import android.app.Activity;
import android.view.Window;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author liuyi
 * @date 2017/5/18
 * @desc 设置状态栏字体图标为深色，需要MIUI6以上
 * @veison
 */

public class MIUIHelper {
    /**
     * @param isFontColorDark 是否把状态栏字体及图标颜色设置为深色
     * @return boolean 成功执行返回true
     */
    public boolean setStatusBarLightMode(Activity activity, boolean isFontColorDark) {

        boolean result = false;
        Window window = activity.getWindow();
        Class<? extends Window> clazz = activity.getWindow().getClass();
        try {
            int darkModeFlag = 0;
            Class<?> layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
            Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
            darkModeFlag = field.getInt(layoutParams);
            Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
            extraFlagField.invoke(activity.getWindow(), isFontColorDark ? darkModeFlag : 0, darkModeFlag);
            if (isFontColorDark) {
//                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            } else {
//                int flag = window.getDecorView().getSystemUiVisibility() & ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
//                window.getDecorView().setSystemUiVisibility(flag);
            }
        } catch (Exception e) {
        }

        return result;
    }
}
