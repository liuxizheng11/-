package com.rocedar.lib.sdk.scanner;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;

import com.rocedar.lib.base.unit.RCDrawableUtil;

/**
 * 项目名称：瑰柏SDK-基础库
 * <p>
 * 作者：phj
 * 日期：2018/7/10 上午10:29
 * 版本：V1.0.00
 * 描述：瑰柏SDK-
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class DrawableUtil {

    public static GradientDrawable rectangle_main_sloke_2px(Context mContext) {
        return RCDrawableUtil.getDrawableStroke(mContext, Color.TRANSPARENT, 1,
                RCDrawableUtil.getThemeAttrColor(mContext, R.attr.RCDarkColor),
                RCDrawableUtil.getThemeAttrDimension(mContext, R.attr.RCButtonRadius));
    }

}
