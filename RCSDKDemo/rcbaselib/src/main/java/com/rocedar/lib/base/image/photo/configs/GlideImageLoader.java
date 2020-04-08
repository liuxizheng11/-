package com.rocedar.lib.base.image.photo.configs;

import android.content.Context;
import android.widget.ImageView;

import com.rocedar.lib.base.unit.RCImageShow;
import com.rocedar.lib.base.unit.RCJavaUtil;
import com.rocedar.lib.sdk.rcgallery.dto.RCPhotoDTO;
import com.rocedar.lib.sdk.rcgallery.inter.ImageLoader;


/**
 * 项目名称：瑰柏SDK-基础库
 * <p>
 * 作者：phj
 * 日期：2018/6/2 下午10:28
 * 版本：V1.0.00
 * 描述：瑰柏SDK-
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class GlideImageLoader implements ImageLoader {

    @Override
    public void showLocationImage(String path, ImageView imageView, boolean isSmall) {
        if (RCJavaUtil.getExtensionNames(path).toLowerCase().equals("gif")) {
            RCImageShow.loadFileGif(path, imageView);
        } else {
            if (isSmall) {
                RCImageShow.loadFilethumbnail(path, imageView);
            } else
                RCImageShow.loadFileImage(path, imageView);
        }
    }

    @Override
    public void showNetworkImage(RCPhotoDTO path, ImageView galleryImageView, boolean isSmall) {
        if (isSmall) {
            RCImageShow.loadUrl(path.path, galleryImageView, RCImageShow.IMAGE_TYPE_NINE);
        } else {
            RCImageShow.loadUrl(path.path, galleryImageView, RCImageShow.IMAGE_TYPE_ALBUM);
        }
    }

    @Override
    public void clearMemoryCache() {

    }

    @Override
    public void resumeRequests(Context context) {
        RCImageShow.scrollResumeRequests(context);

    }

    @Override
    public void pauseRequests(Context context) {
        RCImageShow.scrollPauseRequests(context);
    }
}