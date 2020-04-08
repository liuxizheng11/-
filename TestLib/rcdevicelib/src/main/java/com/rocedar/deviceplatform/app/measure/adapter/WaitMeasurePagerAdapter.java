package com.rocedar.deviceplatform.app.measure.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.rocedar.deviceplatform.R;
import com.rocedar.deviceplatform.app.measure.BloodPressure37Activity;

/**
 * @author liuyi
 * @date 2017/2/11
 * @desc 血压测量的adapter
 * @veison V1.0
 */

public class WaitMeasurePagerAdapter extends PagerAdapter {
    private int[] mImgIds = new int[]{R.mipmap.bg_one,R.mipmap.bg_two,R.mipmap.bg_three};
    private BloodPressure37Activity activity;
    public WaitMeasurePagerAdapter(BloodPressure37Activity activity) {
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return mImgIds.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {

        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imageView = new ImageView(activity);
        imageView.setImageResource(mImgIds[position]);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        container.addView(imageView);
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
