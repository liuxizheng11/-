package com.rocedar.lib.base.unit.other;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;

import com.rocedar.lib.base.unit.RCDrawableUtil;

/**
 * 项目名称：瑰柏SDK-基础库
 * <p>
 * 作者：phj
 * 日期：2018/6/5 下午6:24
 * 版本：V1.0.00
 * 描述：瑰柏SDK-
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class RCBaseDarawableUtil {

    public static GradientDrawable view_dialog_message_center(Context mContext) {
        return RCDrawableUtil.getMainColorDrawable(mContext, 0, 0,
                0, 0);
//
//        RCAndroid.dip2px(mContext,
//                        RCDrawableUtil.getThemeAttrDimension(mContext, R.attr.RCButtonRadius)),
//                RCAndroid.dip2px(mContext,
//                        RCDrawableUtil.getThemeAttrDimension(mContext, R.attr.RCButtonRadius)));
    }


    public static GradientDrawable view_dialog_message_left(Context context) {
        GradientDrawable gradientDrawable = new GradientDrawable();//创建drawable
        gradientDrawable.setColor(Color.parseColor("#CCCCCC"));
        //        int radiusBottomLeft = RCAndroid.dip2px(context,
//                RCDrawableUtil.getThemeAttrDimension(context, R.attr.RCButtonRadius));
        int radiusBottomLeft = 0;

        gradientDrawable.setCornerRadii(new float[]{0, 0, 0, 0, 0, 0, radiusBottomLeft, radiusBottomLeft});
        return gradientDrawable;
    }

    public static GradientDrawable view_dialog_message_right(Context mContext) {
        return RCDrawableUtil.getMainColorDrawable(mContext, 0, 0,
                0, 0);
//        RCAndroid.dip2px(mContext,
//                        RCDrawableUtil.getThemeAttrDimension(mContext, R.attr.RCButtonRadius)));
    }

}
