package com.rocedar.deviceplatform.app.behavior.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * 作者：lxz
 * 日期：17/7/27 上午10:39
 * 版本：V1.0
 * 描述：
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class MyPagerAdapter extends PagerAdapter {
    private List<View> mListViews;

    public MyPagerAdapter(List<View> mListViews) {
        this.mListViews = mListViews;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(mListViews.get(position % mListViews.size()));
        return mListViews.get(position % mListViews.size());
    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(mListViews.get(position % mListViews.size()));
    }

    @Override
    public int getCount() {
        return mListViews.size();
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {

        return arg0 == arg1;
    }
}
