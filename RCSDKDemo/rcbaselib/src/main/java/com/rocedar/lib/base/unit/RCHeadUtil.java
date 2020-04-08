package com.rocedar.lib.base.unit;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.rocedar.lib.base.R;

/**
 * 项目名称：瑰柏SDK-基础库
 * <p>
 * 作者：phj
 * 日期：2018/6/1 下午3:17
 * 版本：V1.0.00
 * 描述：瑰柏SDK-
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class RCHeadUtil {


    private AppCompatActivity activity;

    public RCHeadUtil(AppCompatActivity activity) {
        this.activity = activity;
    }


    private boolean isIncludeLayout() {
        if (activity.findViewById(R.id.toolbar) == null)
            return false;
        return true;
    }

    /**
     * 获取headview的高
     *
     * @return
     */
    public int getHeadViewWidgth() {
        if (!isIncludeLayout()) {
            return 0;
        }
        return activity.findViewById(R.id.toolbar).getHeight();
    }

    /**
     * 隐藏头布局
     */
    public void layoutGone() {
        if (!isIncludeLayout()) {
            return;
        }
        activity.findViewById(R.id.toolbar).setVisibility(View.GONE);
    }

    /**
     * 隐藏头布局
     */
    public void layoutVisible() {
        if (!isIncludeLayout()) {
            return;
        }
        activity.findViewById(R.id.toolbar).setVisibility(View.VISIBLE);
    }

    private int backImageRes = R.mipmap.rc_arrow_left;


    public RCHeadUtil setBGColor(int color, float alpha) {
//        if (!isIncludeLayout()) {
//            return this;
//        }
        activity.findViewById(R.id.toolbar).setBackgroundColor(RCAndroid.changeAlpha(color, alpha));
        setToolbarLine(RCAndroid.changeAlpha(activity.getResources().getColor(R.color.rc_line), alpha));
        TextView textView1 = (TextView) activity.findViewById(R.id.toolbar_title);
        TextView textView2 = (TextView) activity.findViewById(R.id.toolbar_back);
        TextView textView3 = (TextView) activity.findViewById(R.id.toolbar_subtitle);
        if (alpha < 0.5 || !RCJavaUtil.isDrakRGB(color)) {
            backImageRes = R.mipmap.rc_arrow_left_w;
            if (textView1 != null) {
                textView1.setTextColor(Color.WHITE);
            }
            if (textView2 != null) {
                textView2.setTextColor(Color.WHITE);
            }
            if (textView3 != null) {
                textView3.setTextColor(Color.WHITE);
            }
        } else {
            backImageRes = R.mipmap.rc_arrow_left;
            if (textView1 != null) {
                textView1.setTextColor(activity.getResources().getColor(R.color.rc_text_color));
            }
            if (textView2 != null) {
                textView2.setTextColor(activity.getResources().getColor(R.color.rc_text_color));
            }
            if (textView3 != null) {
                textView3.setTextColor(activity.getResources().getColor(R.color.rc_text_color));
            }
        }
        if (textView2 != null)
            setTextViewLeftDrawableImage(textView2, backImageRes);
        return this;
    }


    public RCHeadUtil setTitle(String title) {
        if (!isIncludeLayout()) {
            return this;
        }
        TextView textView1 = (TextView) activity.findViewById(R.id.toolbar_title);
        if (textView1 != null && title != null) {
            textView1.setText(title);
        }
        return this;
    }

    public RCHeadUtil setTitle(String title, int color) {
        return setTitle(title, color, null);
    }

    public RCHeadUtil setTitle(String title, View.OnClickListener clickListener) {
        return setTitle(title, -1, clickListener);
    }


    public RCHeadUtil setTitle(String title, int color, View.OnClickListener clickListener) {
        if (!isIncludeLayout()) {
            return this;
        }
        TextView textView1 = (TextView) activity.findViewById(R.id.toolbar_title);
        if (textView1 != null) {
            if (title != null)
                textView1.setText(title);
            if (color != -1)
                textView1.setTextColor(color);
            if (clickListener != null)
                textView1.setOnClickListener(clickListener);
        }
        return this;

    }

    public RCHeadUtil setTitleImageLeft(int imageId) {
        if (!isIncludeLayout()) {
            return this;
        }
        TextView textView1 = (TextView) activity.findViewById(R.id.toolbar_title);
        setTextViewLeftDrawableImage(textView1, imageId);
        return this;
    }

    public RCHeadUtil setToolbarLine(int color) {
        if (!isIncludeLayout()) {
            return this;
        }
        activity.findViewById(R.id.toolbar_line).setBackgroundColor(color);
        return this;
    }

    public RCHeadUtil setLeftBack() {
        setLeftButton(backImageRes, null);
        return this;
    }

    public RCHeadUtil setLeftBack(View.OnClickListener clickListener) {
        setLeftButton(backImageRes, clickListener);
        return this;
    }


    public RCHeadUtil setLeftButton(String text, View.OnClickListener clickListener) {
        if (!isIncludeLayout()) {
            return this;
        }
        TextView backTextView = (TextView) activity.findViewById(R.id.toolbar_back);
        if (backTextView != null && text != null) {
            backTextView.setVisibility(View.VISIBLE);
            backTextView.setText(text);
            backTextView.setOnClickListener(clickListener);
        }
        return this;
    }


    public RCHeadUtil setLeftButton(int imageId, View.OnClickListener clickListener) {
        if (!isIncludeLayout()) {
            return this;
        }
        TextView backTextView = (TextView) activity.findViewById(R.id.toolbar_back);
        if (backTextView != null) {
            backTextView.setVisibility(View.VISIBLE);
            setTextViewLeftDrawableImage(backTextView, imageId);
            if (clickListener != null) {
                backTextView.setOnClickListener(clickListener);
            } else {
                backTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        activity.finish();
                    }
                });
            }
        }
        return this;
    }


    public RCHeadUtil setLeftButtonGone() {
        if (!isIncludeLayout()) {
            return this;
        }
        TextView backTextView = (TextView) activity.findViewById(R.id.toolbar_back);
        if (backTextView != null) {
            backTextView.setVisibility(View.GONE);
        }
        return this;
    }

    public RCHeadUtil setRightButton(String text, View.OnClickListener clickListener) {
        return setRightButton(text, -1, clickListener);
    }

    public RCHeadUtil setRightButton(String text, int textColor, View.OnClickListener clickListener) {
        if (!isIncludeLayout()) {
            return this;
        }
        TextView rightTextView = (TextView) activity.findViewById(R.id.toolbar_subtitle);
        if (rightTextView != null && text != null) {
            rightTextView.setVisibility(View.VISIBLE);
            rightTextView.setText(text);
            rightTextView.setOnClickListener(clickListener);
            if (textColor != -1) {
                rightTextView.setTextColor(textColor);
            }
        }
        return this;
    }


    public RCHeadUtil setRightButton(int imageId, View.OnClickListener clickListener) {
        if (!isIncludeLayout()) {
            return this;
        }
        TextView rightTextView = (TextView) activity.findViewById(R.id.toolbar_subtitle);
        if (rightTextView != null) {
            rightTextView.setVisibility(View.VISIBLE);
            setTextViewRightDrawableImage(rightTextView, imageId);
            rightTextView.setOnClickListener(clickListener);
        }
        return this;
    }

    public RCHeadUtil setRightButtonGone() {
        if (!isIncludeLayout()) {
            return this;
        }
        TextView rightTextView = (TextView) activity.findViewById(R.id.toolbar_subtitle);
        if (rightTextView != null) {
            rightTextView.setVisibility(View.GONE);
        }
        return this;
    }


    /**
     * 设置TEXTVIEW左边边图标内容
     *
     * @param textView textView对象
     * @param imageID  资源ID
     */
    private void setTextViewLeftDrawableImage(TextView textView, int imageID) {
        Drawable icoDrawable;
        icoDrawable = activity.getResources().getDrawable(imageID);
        icoDrawable.setBounds(0, 0, icoDrawable.getMinimumWidth(),
                icoDrawable.getMinimumHeight());
        textView.setCompoundDrawables(icoDrawable, null, null, null);
    }


    /**
     * 设置TEXTVIEW右边边图标内容
     *
     * @param textView textView对象
     * @param imageID  资源ID
     */
    private void setTextViewRightDrawableImage(TextView textView, int imageID) {
        Drawable icoDrawable;
        icoDrawable = activity.getResources().getDrawable(imageID);
        icoDrawable.setBounds(0, 0, icoDrawable.getMinimumWidth(),
                icoDrawable.getMinimumHeight());
        textView.setCompoundDrawables(null, null, icoDrawable, null);
    }


}
