package com.rocedar.lib.sdk.rcgallery.inter;

import android.content.Context;
import android.widget.ImageView;

import com.rocedar.lib.sdk.rcgallery.dto.RCPhotoDTO;

import java.io.Serializable;

/**
 * 自定义图片加载框架
 * Created by Yancy on 2016/1/27.
 */
public interface ImageLoader extends Serializable {

    void showLocationImage(String path, ImageView imageView, boolean isSmall);

    void showNetworkImage(RCPhotoDTO path, ImageView galleryImageView, boolean isSmall);

    void clearMemoryCache();
    /**
     * @param context
     */
    void resumeRequests(Context context);

    /**
     * @param context
     */
    void pauseRequests(Context context);
}