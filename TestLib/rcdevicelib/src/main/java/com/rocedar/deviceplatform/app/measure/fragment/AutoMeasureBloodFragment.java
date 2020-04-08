package com.rocedar.deviceplatform.app.measure.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rocedar.base.manger.RCBaseFragment;
import com.rocedar.deviceplatform.R;
import com.rocedar.deviceplatform.app.view.CircleProgressView;
import com.rocedar.deviceplatform.app.measure.BloodPressure37Activity;

/**
 * @author liuyi
 * @date 2017/2/11
 * @desc 37血压计自动测量页面
 * @veison V1.0
 */

public class AutoMeasureBloodFragment extends RCBaseFragment {
    private BloodPressure37Activity activity;
    private CircleProgressView circle_ring;
    private TextView tv_auto_measure;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_auto_measure, null);
        activity = (BloodPressure37Activity) getActivity();
        initView(view);
        activity.srartMeasure();
        return view;
    }

    private void initView(View view) {
        circle_ring = (CircleProgressView) view.findViewById(R.id.circle_ring);
        tv_auto_measure = (TextView) view.findViewById(R.id.tv_auto_measure);
        circle_ring.setMaxProgress(300);
    }

    public void showMeasureProcess(int bloodpress) {
        tv_auto_measure.setText(bloodpress+"");
        circle_ring.setProgress(bloodpress);
    }
}
