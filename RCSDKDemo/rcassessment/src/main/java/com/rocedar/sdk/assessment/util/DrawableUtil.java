package com.rocedar.sdk.assessment.util;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;

import com.rocedar.lib.base.unit.RCAndroid;
import com.rocedar.lib.base.unit.RCDrawableUtil;
import com.rocedar.sdk.assessment.R;

/**
 * 项目名称：瑰柏SDK-基础库
 * <p>
 * 作者：phj
 * 日期：2018/6/29 下午4:46
 * 版本：V1.0.00
 * 描述：瑰柏SDK-
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class DrawableUtil {

    public static StateListDrawable activity_select(Context mContext) {
        StateListDrawable stateListDrawable = new StateListDrawable();
        //给当前的颜色选择器添加选中图片指向状态，未选中图片指向状态
        stateListDrawable.addState(new int[]{android.R.attr.state_checked},
                RCDrawableUtil.getMainColorDrawableBaseRadius(mContext));
        //设置默认状态
        stateListDrawable.addState(new int[]{},
                RCDrawableUtil.getDrawableStroke(mContext, Color.TRANSPARENT, 1f,
                        RCDrawableUtil.getThemeAttrColor(mContext, R.attr.RCDarkColor),
                        RCDrawableUtil.getThemeAttrDimension(mContext, R.attr.RCButtonRadius)));
        return stateListDrawable;
    }


    public static GradientDrawable bg_assessment_plan(Context mContext) {
        GradientDrawable gradientDrawable = new GradientDrawable();//创建drawable
        gradientDrawable.setColor(RCDrawableUtil.getThemeAttrColor(mContext, R.attr.RCLightColor));
        gradientDrawable.setCornerRadius(RCAndroid.dip2px(mContext, 20));
        return gradientDrawable;
    }

}
