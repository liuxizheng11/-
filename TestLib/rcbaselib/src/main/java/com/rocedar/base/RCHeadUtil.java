package com.rocedar.base;

import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;


/**
 * 项目名称：设备管理平台
 * <p>
 * 作者：phj
 * 日期：2017/2/22 下午2:11
 * 版本：V1.0
 * 描述：
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
        Toolbar toolbar = (Toolbar) activity.findViewById(R.id.toolbar);
        activity.setSupportActionBar(toolbar);
        //隐藏默认title
        activity.getSupportActionBar().setDisplayShowTitleEnabled(false);
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

    public RCHeadUtil setBGColor(int colorId) {
        if (!isIncludeLayout()) {
            return this;
        }
        activity.findViewById(R.id.toolbar).setBackgroundColor(activity.getResources().getColor(colorId));
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

    public RCHeadUtil setTitle(String title, int colorId) {
        if (!isIncludeLayout()) {
            return this;
        }
        TextView textView1 = (TextView) activity.findViewById(R.id.toolbar_title);
        if (textView1 != null && title != null) {
            textView1.setText(title);
            textView1.setTextColor(activity.getResources().getColor(colorId));
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

    public RCHeadUtil setToolbarLine(int colorId) {
        if (!isIncludeLayout()) {
            return this;
        }
        activity.findViewById(R.id.toolbar_line).setBackgroundColor(
                activity.getResources().getColor(colorId)
        );
        return this;
    }

    public RCHeadUtil setLeftBack() {
        if (RCBaseConfig.APPTAG == RCBaseConfig.APPTAG_DONGYA) {
            setLeftButton(R.mipmap.dy_arrow_left, null);
        } else {
            setLeftButton(R.mipmap.n3_arrow_left, null);
        }
        return this;
    }

    public RCHeadUtil setLeftBack(View.OnClickListener clickListener) {
        if (RCBaseConfig.APPTAG == RCBaseConfig.APPTAG_DONGYA) {
            setLeftButton(R.mipmap.dy_arrow_left, clickListener);
        } else {
            setLeftButton(R.mipmap.n3_arrow_left, clickListener);
        }
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
        activity.findViewById(R.id.toolbar_line).setVisibility(View.INVISIBLE);
        return this;
    }

    public RCHeadUtil setRightButton(String text, View.OnClickListener clickListener) {
        if (!isIncludeLayout()) {
            return this;
        }
        TextView rightTextView = (TextView) activity.findViewById(R.id.toolbar_subtitle);
        if (rightTextView != null && text != null) {
            rightTextView.setVisibility(View.VISIBLE);
            rightTextView.setText(text);
            rightTextView.setOnClickListener(clickListener);
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
