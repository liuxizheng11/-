package com.rocedar.lib.base.unit;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.support.annotation.AttrRes;
import android.support.annotation.ColorInt;
import android.support.annotation.Dimension;

import com.rocedar.lib.base.R;

/**
 * 项目名称：平台库-行为数据
 * <p>
 * 作者：phj
 * 日期：2018/1/23 下午3:24
 * 版本：V1.0.00
 * 描述：
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public class RCDrawableUtil {


    /**
     * 主颜色值的Drawable
     *
     * @param context
     * @param radiusTopLeft
     * @param radiusTopRight
     * @param radiusBottomLeft
     * @param radiusBottomRight
     * @return
     */
    public static GradientDrawable getMainColorDrawable(Context context, int radiusTopLeft, int radiusTopRight,
                                                        int radiusBottomLeft, int radiusBottomRight) {
        GradientDrawable gradientDrawable = new GradientDrawable();//创建drawable
        gradientDrawable.setColor(getThemeAttrColor(context, R.attr.RCDarkColor));
        gradientDrawable.setCornerRadii(new float[]{radiusTopLeft, radiusTopLeft, radiusTopRight,
                radiusTopRight, radiusBottomRight, radiusBottomRight, radiusBottomLeft, radiusBottomLeft});
        return gradientDrawable;
    }

    /**
     * 主颜色值的Drawable
     *
     * @param context
     * @param radius  弧角
     * @return
     */
    public static GradientDrawable getDarkMainColorDrawable(Context context, float radius) {
        GradientDrawable gradientDrawable = new GradientDrawable();//创建drawable
        gradientDrawable.setColor(getThemeAttrColor(context, R.attr.RCDarkColor));
        gradientDrawable.setCornerRadius(RCAndroid.dip2px(context, radius));
        return gradientDrawable;
    }

    /**
     * 主颜色值(浅色)的Drawable
     *
     * @param context
     * @param radius  弧角
     * @return
     */
    public static GradientDrawable getLightMainColorDrawable(Context context, float radius) {
        GradientDrawable gradientDrawable = new GradientDrawable();//创建drawable
        gradientDrawable.setColor(getThemeAttrColor(context, R.attr.RCLightColor));
        gradientDrawable.setCornerRadius(RCAndroid.dip2px(context, radius));
        return gradientDrawable;
    }

    /**
     * 主颜色值的Drawable
     *
     * @param context
     * @return
     */
    public static GradientDrawable getMainColorDrawableBaseRadius(Context context) {
        GradientDrawable gradientDrawable = new GradientDrawable();//创建drawable
        gradientDrawable.setColor(getThemeAttrColor(context, R.attr.RCDarkColor));
        gradientDrawable.setCornerRadius(RCAndroid.dip2px(context,
                getThemeAttrDimension(context, R.attr.RCButtonRadius)));
        return gradientDrawable;
    }

    /**
     * 指定颜色的Drawable弧角为配置角度
     *
     * @param context
     * @param color
     * @return
     */
    public static GradientDrawable getColorDrawableBaseRadius(Context context, int color) {
        GradientDrawable gradientDrawable = new GradientDrawable();//创建drawable
        gradientDrawable.setColor(color);
        gradientDrawable.setCornerRadius(RCAndroid.dip2px(context,
                getThemeAttrDimension(context, R.attr.RCButtonRadius)));
        return gradientDrawable;
    }


    /**
     * 自定义Drawable工具方法
     *
     * @param slideColor  填充色
     * @param strokeWidth 边框宽度（dp）
     * @param strokeColor 边框颜色
     * @return
     */
    public static GradientDrawable getDrawableStroke(
            Context context, int slideColor, float strokeWidth, int strokeColor) {
        return getDrawableStroke(context, slideColor, strokeWidth, strokeColor, 0);
    }

    /**
     * 自定义Drawable工具方法
     *
     * @param slideColor  填充色
     * @param strokeWidth 边框宽度（dp）
     * @param strokeColor 边框颜色
     * @param radius      弧角
     * @return
     */
    public static GradientDrawable getDrawableStroke(
            Context context, int slideColor, float strokeWidth, int strokeColor, float radius) {
        GradientDrawable gradientDrawable = new GradientDrawable();//创建drawable
        gradientDrawable.setColor(slideColor);
        gradientDrawable.setCornerRadius(RCAndroid.dip2px(context, radius));
        gradientDrawable.setStroke(RCAndroid.dip2px(context, strokeWidth), strokeColor);
        return gradientDrawable;
    }


    /**
     * 创建一个选中样式StateLisDrawable对象
     *
     * @param context
     * @param imageFocusRes   选中的图片
     * @param imageDefaultRes 默认的图片
     * @return
     */
    public static StateListDrawable getCheckedDrawable(Context context, int imageFocusRes
            , int imageDefaultRes) {
        return getCheckedDrawable(context, imageFocusRes, imageDefaultRes
                , android.R.attr.state_checked);
    }


    /**
     * 创建一个StateLisDrawable对象
     *
     * @param context
     * @param state           样式
     * @param imageFocusRes   选中的图片
     * @param imageDefaultRes 默认的图片
     * @return
     */
    public static StateListDrawable getCheckedDrawable(
            Context context, int imageFocusRes, int imageDefaultRes, int... state) {
        StateListDrawable stateListDrawable = new StateListDrawable();
        //给当前的颜色选择器添加选中图片指向状态，未选中图片指向状态
        stateListDrawable.addState(state,
                context.getResources().getDrawable(imageFocusRes));
        //设置默认状态
        stateListDrawable.addState(new int[]{},
                context.getResources().getDrawable(imageDefaultRes));
        return stateListDrawable;
    }


    @ColorInt
    public static int getThemeAttrColor(Context context, @AttrRes int colorAttr) {
        final TypedArray array = context.obtainStyledAttributes(null, new int[]{colorAttr});
        try {
            return array.getColor(0, 0);
        } finally {
            array.recycle();
        }
    }

    @Dimension
    public static float getThemeAttrDimension(Context context, @AttrRes int dimensionAttr) {
        final TypedArray array = context.obtainStyledAttributes(null, new int[]{dimensionAttr});
        try {
            return array.getDimension(0, 0.0f);
        } finally {
            array.recycle();
        }
    }

    /**
     * 订单详情 双圆
     *
     * @param mContext
     * @return
     */
    public static LayerDrawable getDoubleCircle(Context mContext) {
        OvalShape ovalShape = new OvalShape();
        ShapeDrawable drawable = new ShapeDrawable(ovalShape);
        drawable.getPaint().setColor(RCDrawableUtil.getThemeAttrColor(mContext, R.attr.RCDarkColor));

        OvalShape ovalShape2 = new OvalShape();
        ShapeDrawable drawable2 = new ShapeDrawable(ovalShape2);
        drawable2.getPaint().setColor(RCDrawableUtil.getThemeAttrColor(mContext, R.attr.RCLightColor));
        int temp = RCAndroid.dip2px(mContext, 2);

        LayerDrawable layerDrawable = new LayerDrawable(new Drawable[]{drawable2, drawable});
        layerDrawable.setLayerInset(1, temp, temp, temp, temp);
        return layerDrawable;
    }

}
