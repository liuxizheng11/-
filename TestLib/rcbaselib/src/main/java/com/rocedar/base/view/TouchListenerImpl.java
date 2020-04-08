package com.rocedar.base.view;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ScrollView;

import com.rocedar.base.RCLog;

public class TouchListenerImpl implements OnTouchListener {

    public interface TouchListenerImplLinsten {

        void onBottom();

        void onScroll();

        void onBottomLast();

    }

    private ScrollView scrollView;
    private TouchListenerImplLinsten implLinsten;

    public TouchListenerImpl(ScrollView scrollView,
                             TouchListenerImplLinsten implLinsten) {
        this.scrollView = scrollView;
        this.implLinsten = implLinsten;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                implLinsten.onScroll();
                break;
            case MotionEvent.ACTION_MOVE:
                int scrollY = view.getScrollY();
                int height = view.getHeight();
                int scrollViewMeasuredHeight = scrollView.getChildAt(0)
                        .getMeasuredHeight();
                if (scrollY == 0) {
                    // System.out.println("滑动到了顶端 view.getScrollY()=" + scrollY);
                }
                if ((scrollY + height) == scrollViewMeasuredHeight) {
                    RCLog.e("PullOnLoading","滑动到了底部 scrollY=" + scrollY);
                    implLinsten.onBottom();
                }
                if ((scrollY + height) < scrollViewMeasuredHeight - 100) {
                    // System.out.println("快滑动到了底部 scrollY=" + scrollY);
                    RCLog.e("PullOnLoading","快滑动到了底部 height=" + height);
                    // System.out.println("快滑动到了底部 scrollViewMeasuredHeight="
                    // + scrollViewMeasuredHeight);
                    implLinsten.onBottomLast();
                }
                break;
            default:
                break;
        }
        return false;
    }
};