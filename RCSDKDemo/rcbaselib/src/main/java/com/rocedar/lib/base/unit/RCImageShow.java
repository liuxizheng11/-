package com.rocedar.lib.base.unit;

import android.content.Context;
import android.widget.AbsListView;
import android.widget.ImageView;

import com.rocedar.lib.base.R;
import com.rocedar.lib.base.config.Config;
import com.rocedar.lib.base.config.RCBaseConfig;
import com.rocedar.lib.base.image.load.LoadImageUtil;

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
                if (RCBaseConfig.getBaseConfig().imageResDefaultHead() > 0)
                    holdImage = RCBaseConfig.getBaseConfig().imageResDefaultHead();
                else
                    holdImage = R.mipmap.rc_head_default;
                break;
            default:
                if (RCBaseConfig.getBaseConfig().imageResDefault() > 0)
                    holdImage = RCBaseConfig.getBaseConfig().imageResDefault();
                else
                    holdImage = R.mipmap.rc_image_default;
                break;
        }
        return holdImage;
    }


    public static void loadUrlToCircle(String url, ImageView imageView, int imagetype) {
        loadUrl(url, imageView, imagetype, -1);
    }

    public static void loadUrl(String url, ImageView imageView, int imagetype) {
        loadUrl(url, imageView, imagetype, -1);
    }

    /**
     * 加载网络图片
     *
     * @param url
     * @param imageView
     * @param imagetype
     */
    public static void loadUrl(String url, ImageView imageView, int imagetype, int mHoldImage) {
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
            if (url.startsWith("/")) {
                if (Config.APPIMAGEURL.endsWith("/"))
                    url = Config.APPIMAGEURL + url.substring(1);
                else
                    url = Config.APPIMAGEURL + url;
            } else {
                if (Config.APPIMAGEURL.endsWith("/"))
                    url = Config.APPIMAGEURL + url;
                else
                    url = Config.APPIMAGEURL + "/" + url;
            }
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
        switch (imagetype) {
            case IMAGE_TYPE_HEAD:
                LoadImageUtil.loadHttpImageThumbnail(url, imageView, 100, holdImage, holdImage);
                break;
            case IMAGE_TYPE_ALBUM:
            case IMAGE_TYPE_NONE:
                LoadImageUtil.loadHttpImage(url, imageView, holdImage, holdImage);
                break;
            case IMAGE_TYPE_NINE:
                LoadImageUtil.loadHttpImageThumbnail(url, imageView, 200, holdImage, holdImage);
                break;
            case IMAGE_TYPE_STAR:
                LoadImageUtil.loadHttpImage(url, imageView, holdImage, holdImage);
                break;
            default:
                LoadImageUtil.loadHttpImageThumbnail(url, imageView, 200, holdImage, holdImage);
                break;
        }
        RCLog.d(TAG, "网络图片加载类型:" + imagetype + "\n网络图片加载:" + url);
    }


    public static void loadResGif(int resId, ImageView imageView) {
        if (imageView == null)
            return;
        int holdImage = getImageHold(IMAGE_TYPE_ALBUM);
        LoadImageUtil.loadResImage(resId, imageView, holdImage, holdImage);
    }

    public static void loadFileGif(String path, ImageView imageView) {
        if (imageView == null)
            return;
        int holdImage = getImageHold(IMAGE_TYPE_ALBUM);
        LoadImageUtil.loadFileImage(path, imageView, false, holdImage, holdImage);
    }

    public static void loadFilethumbnail(String path, ImageView imageView) {
        if (imageView == null)
            return;
        int holdImage = getImageHold(IMAGE_TYPE_ALBUM);
        LoadImageUtil.loadFileImage(path, imageView, true, holdImage, holdImage);
    }

    public static void loadFileImage(String path, ImageView imageView) {
        if (imageView == null)
            return;
        int holdImage = getImageHold(IMAGE_TYPE_ALBUM);
        LoadImageUtil.loadFileImage(path, imageView, false, holdImage, holdImage);
    }

//    public static void load

    /**
     * 使用ListView、GridView时。
     */
    public static AbsListView.OnScrollListener OnScrollListener(
            final Context context,
            final AbsListView.OnScrollListener scrollListener) {
        return LoadImageUtil.OnScrollListener(context, scrollListener);
    }

    public static void scrollResumeRequests(Context context) {
        RCLog.d(TAG, "scrollResumeRequests");
        LoadImageUtil.resumeRequests(context);
    }

    public static void scrollPauseRequests(Context context) {
        RCLog.d(TAG, "scrollPauseRequests");
        LoadImageUtil.pauseRequests(context);
    }

    /**
     * 图片缓存大小
     *
     * @return
     */
    public static long getImageDiskCacheSize(Context context) {
        return LoadImageUtil.getDiskCaheSize(context);
    }

    /**
     * 清除缓存
     *
     * @param context
     */
    public static void clearImageDiskCache(Context context) {
        LoadImageUtil.clearGlideCacheDisk(context);
    }


    public static void downloadImage(){

    }

}
