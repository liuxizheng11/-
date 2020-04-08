package com.rocedar.lib.sdk.rcgallery.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rocedar.lib.sdk.rcgallery.R;
import com.rocedar.lib.sdk.rcgallery.adapter.PreviewPagerAdapter;
import com.rocedar.lib.sdk.rcgallery.dto.RCPhotoDTO;

import java.util.ArrayList;

/**
 * 项目名称：瑰柏SDK-基础库
 * <p>
 * 作者：phj
 * 日期：2018/6/7 下午10:23
 * 版本：V1.0.00
 * 描述：瑰柏SDK-
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class PreviewFragment extends Fragment implements ViewPager.OnPageChangeListener {

    private static final String ARGS_ITEM = "args_item";
    private static final String ARGS_INDEX = "args_choose_index";

    private boolean hasChoose = true;

    public static PreviewFragment newInstance(ArrayList<RCPhotoDTO> itemList, int chooseIndex, boolean hasChoose) {
        PreviewFragment fragment = new PreviewFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(ARGS_ITEM, itemList);
        bundle.putInt(ARGS_INDEX, chooseIndex);
        bundle.putBoolean("has_choose", hasChoose);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_media_preview, container, false);
    }


    protected ViewPager mPager;
    protected PreviewPagerAdapter mAdapter;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPager = view.findViewById(R.id.pager);
        mPager.addOnPageChangeListener(this);
        mAdapter = new PreviewPagerAdapter(getActivity().getSupportFragmentManager(),
                new PreviewPagerAdapter.OnItemSetListener() {
                    @Override
                    public void chooseChange(ArrayList<RCPhotoDTO> list) {
                        if (setListener != null) {
                            setListener.chooseChange(list);
                        }
                    }
                }, getArguments().getBoolean("has_choose"));
        mAdapter.addAll(getArguments().<RCPhotoDTO>getParcelableArrayList(ARGS_ITEM));
        mPager.setAdapter(mAdapter);
        mPager.setCurrentItem(getArguments().getInt(ARGS_INDEX, 0));

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    protected int mPreviousPos = -1;

    @Override
    public void onPageSelected(int position) {
        PreviewPagerAdapter adapter = (PreviewPagerAdapter) mPager.getAdapter();
        if (mPreviousPos != -1 && mPreviousPos != position) {
            ((PreviewItemFragment) adapter.instantiateItem(mPager, mPreviousPos)).resetView();
        }
        mPreviousPos = position;
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private OnSetListener setListener;

    public void setSetListener(OnSetListener setListener) {
        this.setListener = setListener;
    }

    public interface OnSetListener {

        void chooseChange(ArrayList<RCPhotoDTO> list);
    }

    public ArrayList<RCPhotoDTO> getChooseList() {
        return mAdapter.getChooseItems();
    }


}
