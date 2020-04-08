package com.rocedar.base.manger;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.rocedar.base.R;
import com.rocedar.base.RCAndroid;
import com.rocedar.base.RCHandler;
import com.rocedar.base.RCUmeng;

/**
 * Fragment基础类
 */
public abstract class RCBaseFragment extends Fragment {

    protected String TAG = this.getClass().getCanonicalName();


    protected AppCompatActivity mActivity;

    public RCHandler mRcHandler;

    private LayoutInflater inflater;

    private View view;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RCUmeng.umengFragmentCreate();
        mActivity = (AppCompatActivity) getActivity();
        mRcHandler = new RCHandler(mActivity);
        inflater = LayoutInflater.from(mActivity);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                view = getView().findViewById(R.id.include_top_view);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (view != null) {
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) view.getLayoutParams();
                layoutParams.height = RCAndroid.getStatusBarHeight(mActivity);
                view.setLayoutParams(layoutParams);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        RCUmeng.umengFragmentResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        RCUmeng.umengFragmentPause(this);
    }


}
