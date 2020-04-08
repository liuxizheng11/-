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
package com.rocedar.lib.sdk.rcgallery.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.rocedar.lib.sdk.rcgallery.R;
import com.rocedar.lib.sdk.rcgallery.config.GalleryConfig;
import com.rocedar.lib.sdk.rcgallery.config.GalleryPick;
import com.rocedar.lib.sdk.rcgallery.dto.RCPhotoDTO;

import it.sephiroth.android.library.imagezoom.ImageViewTouch;
import it.sephiroth.android.library.imagezoom.ImageViewTouchBase;


public class PreviewItemFragment extends Fragment {

    private static final String ARGS_ITEM = "args_item";
    private GalleryConfig galleryConfig = GalleryPick.getInstance().getGalleryConfig();

    private boolean choose = true;

    public static PreviewItemFragment newInstance(RCPhotoDTO item, boolean hasChoose) {
        PreviewItemFragment fragment = new PreviewItemFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARGS_ITEM, item);
        bundle.putBoolean("has_choose", hasChoose);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_preview_item, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final RCPhotoDTO item = getArguments().getParcelable(ARGS_ITEM);
        if (item == null) {
            return;
        }
        ImageViewTouch image = (ImageViewTouch) view.findViewById(R.id.image_view);
        final ImageView select = view.findViewById(R.id.rc_activity_photo_select);
        if (getArguments().getBoolean("has_choose")) {
            if (choose) {
                select.setImageResource(R.mipmap.gallery_pick_select_checked);
            } else {
                select.setImageResource(R.mipmap.gallery_pick_select_unchecked);
            }
            select.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (choose) {
                        choose = false;
                        select.setImageResource(R.mipmap.gallery_pick_select_unchecked);
                    } else {
                        choose = true;
                        select.setImageResource(R.mipmap.gallery_pick_select_checked);
                    }
                    if (clickListener != null) {
                        clickListener.click(choose);
                    }
                }
            });
        } else {
            select.setVisibility(View.GONE);
        }
        image.setDisplayType(ImageViewTouchBase.DisplayType.FIT_TO_SCREEN);
        if (item.isNetwork()) {
            galleryConfig.getImageLoader().showNetworkImage(item, image, false);
        } else {
            galleryConfig.getImageLoader().showLocationImage(item.path, image, false);
        }
    }

    public void resetView() {
        if (getView() != null) {
            ((ImageViewTouch) getView().findViewById(R.id.image_view)).resetMatrix();
        }
    }

    private OnClickListener clickListener;

    public void setClickListener(OnClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public interface OnClickListener {

        public void click(boolean choose);

    }


}
