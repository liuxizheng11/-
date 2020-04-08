package com.rocedar.sdk.familydoctor.util;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.view.Gravity;

import com.rocedar.lib.base.unit.RCAndroid;
import com.rocedar.lib.base.unit.RCDrawableUtil;
import com.rocedar.sdk.familydoctor.R;

/**
 * 项目名称：瑰柏SDK-家庭医生
 * <p>
 * 作者：phj
 * 日期：2018/5/29 下午5:30
 * 版本：V1.0.00
 * 描述：瑰柏SDK-家庭医生
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class RCFDDrawableUtil {

    public static LayerDrawable progressbar_bg(Context mContext) {
        GradientDrawable p = new GradientDrawable();
        p.setColor(RCDrawableUtil.getThemeAttrColor(mContext, R.attr.RCDarkColor));
        ClipDrawable progress = new ClipDrawable(p, Gravity.LEFT, ClipDrawable.HORIZONTAL);
        GradientDrawable background = new GradientDrawable();
        background.setColor(RCDrawableUtil.getThemeAttrColor(mContext, R.attr.RCLightColor));
        LayerDrawable layerDrawable = new LayerDrawable(new Drawable[]{background, progress});
        layerDrawable.setDrawableByLayerId(0, background);
        layerDrawable.setDrawableByLayerId(1, progress);
        return layerDrawable;
    }

    public static GradientDrawable btn_stroke_main(Context mContext) {
        GradientDrawable gradientDrawable = new GradientDrawable();//创建drawable
        gradientDrawable.setStroke(1, RCDrawableUtil.getThemeAttrColor(mContext,
                R.attr.RCDarkColor));
        return gradientDrawable;

    }


    public static StateListDrawable choose_button_sex(Context mContext) {
        StateListDrawable stateListDrawable = new StateListDrawable();
        //给当前的颜色选择器添加选中图片指向状态，未选中图片指向状态
        stateListDrawable.addState(new int[]{android.R.attr.state_checked},
                RCDrawableUtil.getMainColorDrawableBaseRadius(mContext));
        //设置默认状态
        stateListDrawable.addState(new int[]{},
                RCDrawableUtil.getDrawableStroke(mContext, Color.TRANSPARENT, 0.34f,
                        0xffcbe2ff, RCDrawableUtil.getThemeAttrDimension(mContext, R.attr.RCButtonRadius)));
        return stateListDrawable;
    }

    public static GradientDrawable rectangle_cccccc_5px(Context mContext) {
        GradientDrawable gradientDrawable = new GradientDrawable();//创建drawable
        gradientDrawable.setColor(0xffcccccc);
        gradientDrawable.setCornerRadius(
                RCAndroid.dip2px(mContext,
                        RCDrawableUtil.getThemeAttrDimension(mContext, R.attr.RCButtonRadius))
        );
        return gradientDrawable;
    }


    public static ShapeDrawable mingyi_detail_tag(Context mContext) {
        OvalShape ovalShape = new OvalShape();
        ShapeDrawable drawable = new ShapeDrawable(ovalShape);
        drawable.getPaint().setColor(RCDrawableUtil.getThemeAttrColor(mContext, R.attr.RCDarkColor));
        return drawable;
    }

    public static GradientDrawable mingyi_choose_phoneaddress(Context mContext) {
        return RCDrawableUtil.getDrawableStroke(mContext,Color.TRANSPARENT,0.34f,
                RCDrawableUtil.getThemeAttrColor(mContext,
                        R.attr.RCDarkColor),2);

    }

}
