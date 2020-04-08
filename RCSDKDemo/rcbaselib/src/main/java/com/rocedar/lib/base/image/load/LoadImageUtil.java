package com.rocedar.lib.base.image.load;

import android.content.Context;
import android.widget.AbsListView;
import android.widget.ImageView;

import com.rocedar.lib.base.config.RCBaseConfig;


/**
 * @author liuyi
 * @date 2017/2/7
 * @desc 图片加载工具类
 * @veison
 */

public class LoadImageUtil {


    public static void loadHttpImage(String imgUrl, final ImageView imageView,
                                     int error, int placeholder) {
        if (RCBaseConfig.getImageManager() != null) {
            RCBaseConfig.getImageManager().setErrorImage(error);
            RCBaseConfig.getImageManager().setHoldImage(placeholder);
            RCBaseConfig.getImageManager().loadHttpImage(imageView, imgUrl);
        }
    }

    public static void loadHttpImageThumbnail(String imgUrl, final ImageView imageView,
                                              int widths, int error, int placeholder) {
        if (RCBaseConfig.getImageManager() != null) {
            RCBaseConfig.getImageManager().setErrorImage(error);
            RCBaseConfig.getImageManager().setHoldImage(placeholder);
            RCBaseConfig.getImageManager().loadHttpImage(imageView, imgUrl, widths, widths);
        }
    }


    public static void loadFileImage(String imgUrl, final ImageView imageView,
                                     boolean isThumbnail, int error, int placeholder) {
        if (RCBaseConfig.getImageManager() != null) {
            RCBaseConfig.getImageManager().setErrorImage(error);
            RCBaseConfig.getImageManager().setHoldImage(placeholder);
            if (isThumbnail) {
                RCBaseConfig.getImageManager().loadFileImage(imageView, imgUrl, 50, 50);
            } else {
                RCBaseConfig.getImageManager().loadFileImage(imageView, imgUrl);
            }
        }
    }

    public static void loadResImage(int imgUrl, final ImageView imageView, int error, int placeholder) {
        if (RCBaseConfig.getImageManager() != null) {
            RCBaseConfig.getImageManager().setErrorImage(error);
            RCBaseConfig.getImageManager().setHoldImage(placeholder);
            RCBaseConfig.getImageManager().loadResImage(imageView, imgUrl);
        }
    }


    /**
     * 使用ListView、GridView时。
     */
    public static AbsListView.OnScrollListener OnScrollListener(
            final Context context,
            final AbsListView.OnScrollListener scrollListener) {
        AbsListView.OnScrollListener temp = new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
                if (scrollListener != null)
                    scrollListener.onScrollStateChanged(absListView, i);
                switch (i) {
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE://空闲状态
                        resumeRequests(context);
                        break;
                    case AbsListView.OnScrollListener.SCROLL_STATE_FLING://手指不动了，但是屏幕还在滚动状态
                        break;
                    case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL://触摸后滚动
                        pauseRequests(context);   //停止图片加载
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {
                if (scrollListener != null)
                    scrollListener.onScroll(absListView, i, i1, i2);
            }
        };
        return temp;
    }

    public static void resumeRequests(Context context) {
        //停止图片加载
        if (RCBaseConfig.getImageManager() != null)
            RCBaseConfig.getImageManager().resumeRequests(context);
    }

    public static void pauseRequests(Context context) {
        if (RCBaseConfig.getImageManager() != null)
            RCBaseConfig.getImageManager().pauseRequests(context);
    }


    /**
     * 清除Glide磁盘缓存，自己获取缓存文件夹并删除
     */
    public static void clearGlideCacheDisk(final Context context) {
        if (RCBaseConfig.getImageManager() != null)
            RCBaseConfig.getImageManager().clearImageDiskCache(context);
    }

    /**
     * 获取Glide缓存大小
     *
     * @return
     */
    public static long getDiskCaheSize(Context context) {
        if (RCBaseConfig.getImageManager() != null) {
            return RCBaseConfig.getImageManager().getImageDiskCacheSize(context);
        }
        return 0;
    }

}
