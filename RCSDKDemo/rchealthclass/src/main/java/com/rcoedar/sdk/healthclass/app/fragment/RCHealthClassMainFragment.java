package com.rcoedar.sdk.healthclass.app.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rcoedar.sdk.healthclass.R;
import com.rcoedar.sdk.healthclass.app.RCHealthClassSearchActivity;
import com.rcoedar.sdk.healthclass.app.adapter.ViewPagerFragmentAdapter;
import com.rcoedar.sdk.healthclass.dto.RCHealthhClassTitleDTO;
import com.rcoedar.sdk.healthclass.request.impl.RCHealthClassmpl;
import com.rcoedar.sdk.healthclass.request.listener.RCGetHealthCLassTitleListener;
import com.rocedar.lib.base.manage.RCBaseFragment;
import com.rocedar.lib.base.unit.RCDrawableUtil;
import com.rocedar.lib.base.unit.RCHandler;

import android.support.design.widget.TabLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static com.rocedar.lib.base.unit.RCAndroid.dip2px;

/**
 * 作者：lxz
 * 日期：2018/7/10 上午9:58
 * 版本：V1.0
 * 描述：
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class RCHealthClassMainFragment extends RCBaseFragment {
    private ViewPager rc_fragment_health_calss_viewpager;
    private TabLayout rc_fragment_health_calss_tablayout;

    private LinearLayout rc_fragment_health_calss_search_ll;
    private RCHealthClassmpl rcHealthClassmpl;
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private ArrayList<String> mTitles = new ArrayList<>();

    public static RCHealthClassMainFragment newInstance() {
        RCHealthClassMainFragment fragment = new RCHealthClassMainFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rcHealthClassmpl = RCHealthClassmpl.getInstance(mActivity);
        View view = inflater.inflate(R.layout.rc_fragment_class_main, null);

        initView(view);
        initTypeData();

        return view;

    }

    private void initView(View view) {
        rc_fragment_health_calss_viewpager = view.findViewById(R.id.rc_fragment_health_calss_viewpager);
        rc_fragment_health_calss_tablayout = view.findViewById(R.id.rc_fragment_health_calss_tablayout);
        rc_fragment_health_calss_search_ll = view.findViewById(R.id.rc_fragment_health_calss_search_ll);

        /**搜索点击*/
        rc_fragment_health_calss_search_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RCHealthClassSearchActivity.goActivity(mActivity);
            }
        });

    }

    private void initTypeData() {
        mRcHandler.sendMessage((RCHandler.START));
        rcHealthClassmpl.getHealthInfoType(new RCGetHealthCLassTitleListener() {
            @Override
            public void getDataSuccess(List<RCHealthhClassTitleDTO> list) {
                if (mTitles.size() > 0)
                    mTitles.clear();
                if (mFragments.size() > 0)
                    mFragments.clear();

                if (list != null && list.size() > 0) {
                    for (int i = 0; i < list.size(); i++) {
                        //创建相应的fragment
                        mFragments.add(RCHealthClassRoomListFragment.newInstance(list.get(i).getType_id()));
                        mTitles.add(list.get(i).getType_name());
                    }

                    ViewPagerFragmentAdapter healthClassRoomFragmentPagerAdapter = new ViewPagerFragmentAdapter(mActivity.getSupportFragmentManager(), mFragments, mTitles);
                    rc_fragment_health_calss_viewpager.setAdapter(healthClassRoomFragmentPagerAdapter);
                    rc_fragment_health_calss_tablayout.setupWithViewPager(rc_fragment_health_calss_viewpager);//将TabLayout和ViewPager关联起来。
                    //暂时去掉指示器颜色
                    rc_fragment_health_calss_tablayout.setSelectedTabIndicatorColor(RCDrawableUtil.getThemeAttrColor(mActivity, R.attr.RCDarkColor));
                    reflex(rc_fragment_health_calss_tablayout);

                }
                mRcHandler.sendMessage(RCHandler.GETDATA_OK);
            }

            @Override
            public void getDataError(int status, String msg) {
                mRcHandler.sendMessage(RCHandler.GETDATA_OK);
            }
        });
    }

    /**
     * 设置 tabLayout下划线 宽度
     *
     * @param tabLayout
     */
    public void reflex(final TabLayout tabLayout) {
        //了解源码得知 线的宽度是根据 tabView的宽度来设置的
        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                try {
                    //拿到tabLayout的mTabStrip属性
                    LinearLayout mTabStrip = (LinearLayout) tabLayout.getChildAt(0);

                    int dp10 = dip2px(tabLayout.getContext(), 18);

                    for (int i = 0; i < mTabStrip.getChildCount(); i++) {
                        View tabView = mTabStrip.getChildAt(i);

                        //拿到tabView的mTextView属性  tab的字数不固定一定用反射取mTextView
                        Field mTextViewField = tabView.getClass().getDeclaredField("mTextView");
                        mTextViewField.setAccessible(true);

                        TextView mTextView = (TextView) mTextViewField.get(tabView);

                        tabView.setPadding(0, 0, 0, 0);

                        //因为我想要的效果是   字多宽线就多宽，所以测量mTextView的宽度
                        int width = 0;
                        width = mTextView.getWidth();
                        if (width == 0) {
                            mTextView.measure(0, 0);
                            width = mTextView.getMeasuredWidth();
                        }

                        //设置tab左右间距为10dp  注意这里不能使用Padding 因为源码中线的宽度是根据 tabView的宽度来设置的
                        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tabView.getLayoutParams();
                        params.width = width;
                        params.leftMargin = dp10;
                        params.rightMargin = dp10;
                        tabView.setLayoutParams(params);

                        tabView.invalidate();
                    }

                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });

    }
}
