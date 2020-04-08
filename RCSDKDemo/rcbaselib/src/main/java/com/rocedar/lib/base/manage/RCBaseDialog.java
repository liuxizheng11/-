package com.rocedar.lib.base.manage;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.lang.reflect.Field;

/**
 * 项目名称：DongYa3.0
 * <p>
 * 作者：phj
 * 日期：2017/11/28 下午4:37
 * 版本：V2.2.00
 * 描述：
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public class RCBaseDialog extends Dialog {

    public OnDialogChooseListener onDialogChooseListener;

    public OnDialogItemChooseListener onDialogItemChooseListener;

    public Context mContext;


    public void setOnDialogChooseListener(OnDialogChooseListener onDialogChooseListener) {
        this.onDialogChooseListener = onDialogChooseListener;
    }

    public void setOnDialogItemChooseListener(OnDialogItemChooseListener onDialogItemChooseListener) {
        this.onDialogItemChooseListener = onDialogItemChooseListener;
    }

    public RCBaseDialog(Context context) {
        super(context);
        mContext = context;
        setDialogTheme();
        setCanceledOnTouchOutside(true);
    }

    public RCBaseDialog(Context context, int themeResId, int gravity) {
        super(context, themeResId);
        mContext = context;
        setCanceledOnTouchOutside(true);
        setCancelable(true);
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.gravity = gravity;
    }

    public RCBaseDialog(Context context, int themeResId, boolean isfull) {
        super(context, themeResId);
        mContext = context;
        setCancelable(true);
        setCanceledOnTouchOutside(true);
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        if (isfull) {
            params.height = WindowManager.LayoutParams.MATCH_PARENT;
        } else {
            params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        }

    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
    }

    /**
     * set dialog theme(设置对话框主题)
     */
    private void setDialogTheme() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);// android:windowNoTitle
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);// android:windowBackground
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);// android:backgroundDimEnabled默认是true的
    }


    public interface OnDialogChooseListener {
        void onClick();
    }


    public interface OnDialogItemChooseListener {
        void onItemClick(int position);
    }


    @Override
    public void dismiss() {
        super.dismiss();
    }


    public static int getStatusBarHeight(Context context) {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, statusBarHeight = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = context.getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return statusBarHeight;
    }

}
