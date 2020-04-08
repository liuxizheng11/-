/*
 * Copyright 2017 Zhihu Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.rocedar.lib.sdk.rcgallery.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.rocedar.lib.sdk.rcgallery.activity.PreviewItemFragment;
import com.rocedar.lib.sdk.rcgallery.dto.RCPhotoDTO;

import java.util.ArrayList;
import java.util.List;

public class PreviewPagerAdapter extends FragmentPagerAdapter {

    private ArrayList<RCPhotoDTO> mItems = new ArrayList<>();
    private ArrayList<RCPhotoDTO> chooseItems = new ArrayList<>();
    private OnItemSetListener mListener;
    private boolean hasChoose;


    public PreviewPagerAdapter(FragmentManager manager, OnItemSetListener listener, boolean hasChoose) {
        super(manager);
        mListener = listener;
        this.hasChoose = hasChoose;
    }

    @Override
    public Fragment getItem(final int position) {
        PreviewItemFragment fragment = PreviewItemFragment.newInstance(mItems.get(position), hasChoose);
        fragment.setClickListener(new PreviewItemFragment.OnClickListener() {
            @Override
            public void click(boolean choose) {
                if (choose) {
                    chooseItems.add(position, mItems.get(position));
                } else {
                    chooseItems.remove(position);
                }
                if (mListener != null) {
                    mListener.chooseChange(chooseItems);
                }
            }
        });
        return fragment;
    }

    @Override
    public int getCount() {
        return mItems.size();
    }


    public RCPhotoDTO getMediaItem(int position) {
        return mItems.get(position);
    }

    public void addAll(List<RCPhotoDTO> items) {
        mItems.addAll(items);
        for (int i = 0; i < items.size(); i++) {
            chooseItems.add(items.get(i));
        }
    }

    public ArrayList<RCPhotoDTO> getChooseItems() {
        return chooseItems;
    }

    public interface OnItemSetListener {

        void chooseChange(ArrayList<RCPhotoDTO> list);
    }

}
