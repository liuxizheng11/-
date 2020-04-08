package com.rocedar.lib.base.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;

/**
 * 项目名称：瑰柏SDK-基础库
 * <p>
 * 作者：phj
 * 日期：2018/10/12 下午3:06
 * 版本：V1.1.00
 * 描述：瑰柏SDK-scrollview 嵌套webview
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class RCNoScrollWebView extends WebView {

    @SuppressLint("NewApi")
    public RCNoScrollWebView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public RCNoScrollWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public RCNoScrollWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RCNoScrollWebView(Context context) {
        super(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int mExpandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, mExpandSpec);
    }

}