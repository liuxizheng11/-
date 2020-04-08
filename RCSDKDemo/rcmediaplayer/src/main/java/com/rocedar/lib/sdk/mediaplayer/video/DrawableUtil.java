//package com.rocedar.lib.sdk.mediaplayer.video;
//
//import android.content.Context;
//import android.graphics.Paint;
//import android.graphics.drawable.Drawable;
//import android.graphics.drawable.GradientDrawable;
//import android.graphics.drawable.LayerDrawable;
//import android.graphics.drawable.ShapeDrawable;
//import android.graphics.drawable.shapes.OvalShape;
//
//import com.rocedar.lib.sdk.mediaplayer.R;
//
///**
// * 项目名称：瑰柏SDK-基础库
// * <p>
// * 作者：phj
// * 日期：2018/7/9 下午10:26
// * 版本：V1.0.00
// * 描述：瑰柏SDK-
// * <p>
// * CopyRight©北京瑰柏科技有限公司
// */
//public class DrawableUtil {
//
//
//    public static StateListDrawable select_change_clarity(Context mContext) {
//        StateListDrawable stateListDrawable = new StateListDrawable();
//        stateListDrawable.addState(new int[]{android.R.attr.state_selected},
//                bg_change_clarity_checked(mContext));
//        stateListDrawable.addState(new int[]{},
//                mContext.getResources().getDrawable(R.drawable.rc_mp_bg_change_clarity_normal));
//        return stateListDrawable;
//    }
//
//
//    public static void rc_mp_seek_progress(Context mContext, SeekBar seekBar) {
//        int radius1 = RCAndroid.dip2px(mContext, 2);
//        float[] outerR1 = new float[]{radius1, radius1, radius1, radius1, radius1, radius1, radius1, radius1};
//        RoundRectShape roundRectShape1 = new RoundRectShape(outerR1, null, null);
//
//        ShapeDrawable progressDrawable = new ShapeDrawable();
//        progressDrawable.getPaint().setStyle(Paint.Style.FILL);
//        progressDrawable.getPaint().setColor(RCDrawableUtil.getThemeAttrColor
//                (mContext, R.attr.RcBaseAppMainColor));
//        progressDrawable.setShape(roundRectShape1);
//        ClipDrawable progress = new ClipDrawable(progressDrawable, Gravity.START, ClipDrawable.HORIZONTAL);
//
//        ShapeDrawable secondaryProgressDrawable = new ShapeDrawable();
//        secondaryProgressDrawable.getPaint().setStyle(Paint.Style.FILL);
//        secondaryProgressDrawable.getPaint().setColor(0xffffffff);
//        secondaryProgressDrawable.setShape(roundRectShape1);
//        ClipDrawable secondaryProgress = new ClipDrawable(secondaryProgressDrawable, Gravity.START, ClipDrawable.HORIZONTAL);
//
//        ShapeDrawable background = new ShapeDrawable();
//        background.getPaint().setStyle(Paint.Style.FILL);
//        background.getPaint().setColor(0x7dffffff);
//        background.setShape(roundRectShape1);
//
//
//        LayerDrawable layerDrawable = (LayerDrawable) seekBar.getProgressDrawable();
//        // 有多少个层次（最多三个）
//        int layers = layerDrawable.getNumberOfLayers();
//        Drawable[] drawables = new Drawable[layers];
//        for (int i = 0; i < layers; i++) {
//            switch (layerDrawable.getId(i)) {
//                // 如果是seekbar背景
//                case android.R.id.background:
//                    drawables[i] = background;
//                    break;
//                case android.R.id.secondaryProgress:
//                    drawables[i] = secondaryProgress;
//                    drawables[i].setBounds(layerDrawable.getDrawable(0).getBounds());
//                    break;
//                // 如果是拖动条第一进度
//                case android.R.id.progress:
//                    //这里为动态的颜色值
//                    drawables[i] = progress;
//                    drawables[i].setBounds(layerDrawable.getDrawable(0).getBounds());
//                    break;
//            }
//        }
//        seekBar.setProgressDrawable(new LayerDrawable(drawables));
//        seekBar.setThumb(rc_mp_seek_thumb(mContext));
//        seekBar.invalidate();
//
//    }
//
//
//    public static LayerDrawable rc_mp_seek_thumb(Context mContext) {
//        OvalShape ovalShape = new OvalShape();
//        ShapeDrawable drawable = new ShapeDrawable(ovalShape);
//        drawable.getPaint().setColor(RCDrawableUtil.getThemeAttrColor(mContext,
//                R.attr.RcBaseAppBtnFocusColor));
//        drawable.getPaint().setAntiAlias(true);
//        drawable.getPaint().setStyle(Paint.Style.FILL);
//        drawable.setIntrinsicWidth(RCAndroid.dip2px(mContext, 16));
//        drawable.setIntrinsicHeight(RCAndroid.dip2px(mContext, 16));
//
//        OvalShape ovalShape2 = new OvalShape();
//        ShapeDrawable drawable2 = new ShapeDrawable(ovalShape2);
//        drawable2.getPaint().setColor(RCDrawableUtil.getThemeAttrColor(mContext,
//                R.attr.RcBaseAppBtnColor));
//        drawable2.getPaint().setAntiAlias(true);
//        drawable2.getPaint().setStyle(Paint.Style.FILL);
//        drawable2.setIntrinsicWidth(RCAndroid.dip2px(mContext, 8));
//        drawable2.setIntrinsicHeight(RCAndroid.dip2px(mContext, 8));
//
//        LayerDrawable layerDrawable = new LayerDrawable(new Drawable[]{drawable, drawable2});
//        layerDrawable.setLayerInset(0, 0, 0, 0, 0);
//        int temp = RCAndroid.dip2px(mContext, 4);
//        layerDrawable.setLayerInset(1, temp, temp, temp, temp);
//        return layerDrawable;
//
//
//    }
//
//    private static GradientDrawable bg_change_clarity_checked(Context mContext) {
//        GradientDrawable gradientDrawable = new GradientDrawable();//创建drawable
//        gradientDrawable.setColor(0x00000000);
//        gradientDrawable.setStroke(RCAndroid.dip2px(mContext, 1.5f),
//                RCDrawableUtil.getThemeAttrColor(mContext, R.attr.RcBaseAppMainColor));
//        gradientDrawable.setCornerRadius(RCAndroid.dip2px(mContext, 32f));
//        gradientDrawable.setBounds(12, 6, 12, 7);
//        return gradientDrawable;
//    }
//
//
//}
