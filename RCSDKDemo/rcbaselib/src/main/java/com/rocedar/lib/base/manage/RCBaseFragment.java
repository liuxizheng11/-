package com.rocedar.lib.base.manage;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.rocedar.lib.base.R;
import com.rocedar.lib.base.unit.RCAndroid;
import com.rocedar.lib.base.unit.RCHandler;

/**
 * 项目名称：瑰柏SDK-基础库
 * <p>
 * 作者：phj
 * 日期：2018/5/17 下午5:48
 * 版本：V1.0.00
 * 描述：瑰柏SDK-
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class RCBaseFragment extends Fragment {


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
    }

    @Override
    public void onPause() {
        super.onPause();
    }


    public void showContent(int layoutId, Fragment fragment) {
        mActivity.getSupportFragmentManager()
                .beginTransaction()
                .replace(layoutId, fragment).commitAllowingStateLoss();
    }


    public RCBaseActivity getRCBaseActivity() {
        if (getActivity() instanceof RCBaseActivity) {
            return (RCBaseActivity) mActivity;
        }
        return null;
    }


}
