package com.rocedar.lib.base.view.loading;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ScrollView;


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
                }
                if ((scrollY + height) == scrollViewMeasuredHeight) {
                    implLinsten.onBottom();
                }
                if ((scrollY + height) < scrollViewMeasuredHeight - 100) {
                    implLinsten.onBottomLast();
                }
                break;
            default:
                break;
        }
        return false;
    }
};