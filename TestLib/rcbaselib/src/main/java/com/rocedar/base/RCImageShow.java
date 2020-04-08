package com.rocedar.base;

import android.content.Context;
import android.widget.AbsListView;
import android.widget.ImageView;

import com.rocedar.base.image.load.GlideUtil;


/**
 * 项目名称：设备管理平台
 * <p>
 * 作者：phj
 * 日期：2017/2/23 下午2:13
 * 版本：V1.0
 * 描述：图片显示加载工具类
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public class RCImageShow {

    private static final String TAG = "RCBase_image_load";


    public static final int IMAGE_TYPE_NONE = 0;//原图
    public static final int IMAGE_TYPE_HEAD = 1;//头像
    public static final int IMAGE_TYPE_ALBUM = 2;//大图
    public static final int IMAGE_TYPE_NINE = 3;//缩略图
    public static final int IMAGE_TYPE_STAR = 4;//缩略图


    /* 头像类图片,分辨率100*100*/
    public static final String IMAGE_TYPE_INFO_HEAD = "!portrait";
    /* 大图图片,分辨率1080短边*/
    public static final String IMAGE_TYPE_INFO_ALBUM = "!album";
    /* 缩略图图片,分辨率200*200*/
    public static final String IMAGE_TYPE_INFO_NINE = "!nine";
    /* 缩略图图片,分辨率500*500*/
    public static final String IMAGE_TYPE_INFO_STAR = "!star";


    private static int getImageHold(int imagetype) {
        int holdImage;
        switch (imagetype) {
            case IMAGE_TYPE_HEAD:
                if (RCBaseConfig.APPTAG == RCBaseConfig.APPTAG_DONGYA) {
                    holdImage = R.mipmap.dy_head_default;
                } else {
                    holdImage = R.mipmap.n3_image_default_head;
                }
                break;
            default:
                if (RCBaseConfig.APPTAG == RCBaseConfig.APPTAG_DONGYA) {
                    holdImage = R.mipmap.dy_image_default;
                } else {
                    holdImage = R.mipmap.n3_image_default;
                }
                break;
        }
        return holdImage;
    }


    public static void loadUrlToCircle(String url, ImageView imageView, int imagetype) {
        loadUrl(url, imageView, imagetype, true, -1);
    }

    public static void loadUrl(String url, ImageView imageView, int imagetype) {
        loadUrl(url, imageView, imagetype, false, -1);
    }

    public static void loadUrl(String url, ImageView imageView, int imagetype, int mHoldImage) {
        loadUrl(url, imageView, imagetype, false, mHoldImage);
    }

    /**
     * 加载网络图片
     *
     * @param url
     * @param imageView
     * @param imagetype
     */
    private static void loadUrl(String url, ImageView imageView, int imagetype, boolean isCircle, int mHoldImage) {
        if (imageView == null)
            return;
        int holdImage = getImageHold(imagetype);
        if (mHoldImage != -1) {
            holdImage = mHoldImage;
        }
        if (url == null || url.equals("")) {
            imageView.setImageResource(holdImage);
            return;
        }
        if (!url.startsWith("http")) {
            url = RCBaseConfig.APPIMAGEURL + url;
            switch (imagetype) {
                case IMAGE_TYPE_HEAD:
                    url = url + IMAGE_TYPE_INFO_HEAD;
                    break;
                case IMAGE_TYPE_ALBUM:
                case IMAGE_TYPE_NONE:
                    url = url + IMAGE_TYPE_INFO_ALBUM;
                    break;
                case IMAGE_TYPE_NINE:
                    url = url + IMAGE_TYPE_INFO_NINE;
                    break;
                case IMAGE_TYPE_STAR:
                    url = url + IMAGE_TYPE_INFO_STAR;
                    break;
                default:
                    url = url + IMAGE_TYPE_INFO_NINE;
                    break;
            }
        }
        if (imagetype == IMAGE_TYPE_ALBUM &&
                (url.endsWith(".gif") || url.endsWith(".GIF"))) {
            RCLog.d(TAG, "网络图片加载的是GIF:" + url);
            GlideUtil.loadHttpGifImage(RCBaseManage.getInstance().getContext(),
                    url, imageView, false, holdImage, holdImage);
        } else {//静态图
            switch (imagetype) {
                case IMAGE_TYPE_HEAD:
                    GlideUtil.loadHttpImageThumbnail(RCBaseManage.getInstance().getContext(),
                            url, imageView, 100, holdImage, holdImage);
                    break;
                case IMAGE_TYPE_ALBUM:
                case IMAGE_TYPE_NONE:
                    GlideUtil.loadHttpImage(RCBaseManage.getInstance().getContext(),
                            url, imageView, isCircle, holdImage, holdImage);
                    break;
                case IMAGE_TYPE_NINE:
                    GlideUtil.loadHttpImageThumbnail(RCBaseManage.getInstance().getContext(),
                            url, imageView, 200, holdImage, holdImage);
                    break;
                case IMAGE_TYPE_STAR:
                    GlideUtil.loadHttpImage(RCBaseManage.getInstance().getContext(),
                            url, imageView, isCircle, holdImage, holdImage);
                    break;
                default:
                    GlideUtil.loadHttpImageThumbnail(RCBaseManage.getInstance().getContext(),
                            url, imageView, 200, holdImage, holdImage);

                    break;
            }
            RCLog.d(TAG, "网络图片加载类型:" + imagetype + "\n网络图片加载:" + url);

        }

    }


    public static void loadResGif(int resId, ImageView imageView) {
        if (imageView == null)
            return;
        int holdImage = getImageHold(IMAGE_TYPE_ALBUM);
        GlideUtil.loadAlbumGIFImage(
                RCBaseManage.getInstance().getContext(), resId, imageView, false, holdImage, holdImage);
    }

    public static void loadFileGif(String path, ImageView imageView) {
        if (imageView == null)
            return;
        int holdImage = getImageHold(IMAGE_TYPE_ALBUM);
        GlideUtil.loadAlbumGIFImage(
                RCBaseManage.getInstance().getContext(), path, imageView, false, holdImage, holdImage);
    }

    public static void loadFilethumbnail(String path, ImageView imageView) {
        if (imageView == null)
            return;
        int holdImage = getImageHold(IMAGE_TYPE_ALBUM);
        GlideUtil.loadAlbumImage(
                RCBaseManage.getInstance().getContext(), path, imageView, true, holdImage, holdImage);
    }

    public static void loadFileImage(String path, ImageView imageView) {
        if (imageView == null)
            return;
        int holdImage = getImageHold(IMAGE_TYPE_ALBUM);
        GlideUtil.loadAlbumImage(
                RCBaseManage.getInstance().getContext(), path, imageView, false, holdImage, holdImage);
    }

    /**
     * 使用ListView、GridView时。
     */
    public static AbsListView.OnScrollListener OnScrollListener(
            final Context context,
            final AbsListView.OnScrollListener scrollListener) {
        return GlideUtil.OnScrollListener(context, scrollListener);
    }

    public static void scrollResumeRequests(Context context) {
        RCLog.d(TAG, "scrollResumeRequests");
        GlideUtil.resumeRequests(context);
    }

    public static void scrollPauseRequests(Context context) {
        RCLog.d(TAG, "scrollPauseRequests");
        GlideUtil.pauseRequests(context);
    }

    /**
     * 图片缓存大小
     *
     * @return
     */
    public static long getImageDiskCacheSize() {
        return GlideUtil.getDiskCaheSize();
    }

    /**
     * 清除缓存
     *
     * @param context
     */
    public static void clearImageDiskCache(Context context) {
        GlideUtil.clearGlideCacheDisk(context);
    }

}
