package com.rocedar.base.image.load;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Looper;
import android.widget.AbsListView;
import android.widget.ImageView;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.GifRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;
import com.rocedar.base.RCBaseManage;
import com.rocedar.base.RCToast;

import java.io.File;


/**
 * @author liuyi
 * @date 2017/2/7
 * @desc 图片加载工具类
 * @veison
 */

public class GlideUtil {

    public static String glideFileName = RCBaseManage.getInstance().getContext().getExternalCacheDir() + "/" + "dongya";
    public static final int IMAGE_CORNER_RADIUS = 5; //圆角度数

    public static final float THUMB_SIZE = 0.5f; //0-1之间  10%原图的大小


    public static void loadHttpImage(Context context, String imgUrl, final ImageView imageView,
                                     boolean isCircle, int error, int placeholder) {
        DrawableRequestBuilder requestBuilder = Glide.with(context)
                .load(imgUrl).priority(Priority.NORMAL).diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(error).placeholder(placeholder).crossFade();
        if (imageView.getScaleType() == ImageView.ScaleType.CENTER_CROP) {
            requestBuilder.centerCrop();
        } else {
            requestBuilder.fitCenter();
        }
        if (isCircle) {
            requestBuilder = requestBuilder
                    .bitmapTransform(new GlideCircleTransform(context));
        }
        if (imageView.getScaleType() == ImageView.ScaleType.CENTER) {
            requestBuilder.into(requestBuilder.into(new SimpleTarget<GlideBitmapDrawable>() {
                @Override
                public void onResourceReady(GlideBitmapDrawable resource, GlideAnimation<? super GlideBitmapDrawable> glideAnimation) {
                    imageView.setImageDrawable(resource);
                }
            }));
        } else {
            requestBuilder.into(imageView);
        }
    }

    public static void loadHttpImageThumbnail(Context context, String imgUrl, final ImageView imageView,
                                              int widths, int error, int placeholder) {
        DrawableRequestBuilder requestBuilder = Glide.with(context)
                .load(imgUrl).priority(Priority.NORMAL).diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(error).placeholder(placeholder).crossFade();
        if (imageView.getScaleType() == ImageView.ScaleType.CENTER_CROP) {
            requestBuilder.centerCrop();
        } else {
            requestBuilder.fitCenter();
        }
        int width = (int) (Resources.getSystem().getDisplayMetrics().density * widths);
        int height = (int) (Resources.getSystem().getDisplayMetrics().density * widths);
        requestBuilder.override(width, height);
        if (imageView.getScaleType() == ImageView.ScaleType.CENTER) {
            requestBuilder.into(requestBuilder.into(new SimpleTarget<GlideBitmapDrawable>() {
                @Override
                public void onResourceReady(GlideBitmapDrawable resource, GlideAnimation<? super GlideBitmapDrawable> glideAnimation) {
                    imageView.setImageDrawable(resource);
                }
            }));
        } else {
            requestBuilder.dontAnimate().into(imageView);
        }
    }


    public static void loadHttpGifImage(Context context, String imgUrl, final ImageView imageView,
                                        boolean isThumbnail, int error, int placeholder) {
        GifRequestBuilder requestBuilder = Glide.with(context)
                .load(imgUrl).asGif().priority(Priority.NORMAL).diskCacheStrategy(DiskCacheStrategy.ALL)
                .skipMemoryCache(true)
                .error(error).placeholder(placeholder).crossFade();
        if (imageView.getScaleType() == ImageView.ScaleType.CENTER_CROP) {
            requestBuilder.centerCrop();
        } else {
            requestBuilder.fitCenter();
        }
        if (isThumbnail) {
            requestBuilder.thumbnail(THUMB_SIZE);
        }
        requestBuilder.dontAnimate().into(imageView);
    }


    public static void loadAlbumImage(Context context, Object imgUrl, final ImageView imageView,
                                      boolean isThumbnail, int error, int placeholder) {
        DrawableRequestBuilder requestBuilder = Glide.with(context)
                .load(imgUrl).priority(Priority.HIGH).diskCacheStrategy(DiskCacheStrategy.NONE)
                .error(error).placeholder(placeholder).crossFade();
        if (imageView.getScaleType() == ImageView.ScaleType.CENTER_CROP) {
            requestBuilder.centerCrop();
        } else {
            requestBuilder.fitCenter();
        }
        if (isThumbnail) {
            int width = (int) (Resources.getSystem().getDisplayMetrics().density * 150);
            int height = (int) (Resources.getSystem().getDisplayMetrics().density * 150);
            requestBuilder.override(width, height);
        }
        requestBuilder.dontAnimate().into(imageView);
    }


    public static void loadAlbumGIFImage(Context context, Object imgUrl, final ImageView imageView,
                                         boolean isThumbnail, int error, int placeholder) {
        GifRequestBuilder requestBuilder = Glide.with(context)
                .load(imgUrl).asGif().priority(Priority.HIGH).diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .error(error).placeholder(placeholder).crossFade();
        if (imageView.getScaleType() == ImageView.ScaleType.CENTER_CROP) {
            requestBuilder.centerCrop();
        } else {
            requestBuilder.fitCenter();
        }
        if (isThumbnail) {
            int width = (int) (Resources.getSystem().getDisplayMetrics().density * 150);
            int height = (int) (Resources.getSystem().getDisplayMetrics().density * 150);
            requestBuilder.override(width, height);
        }
        requestBuilder.dontAnimate().into(imageView);
    }

    public static void downLoadImage(final Context context, Object imgUrl) {
        Glide.with(context).load(imgUrl).downloadOnly(
                new Target<File>() {
                    @Override
                    public void onLoadStarted(Drawable placeholder) {

                    }

                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {

                    }

                    @Override
                    public void onResourceReady(File resource, GlideAnimation<? super File> glideAnimation) {
                        RCToast.TestCenter(context, "图片下载完成：" + resource);
                    }

                    @Override
                    public void onLoadCleared(Drawable placeholder) {

                    }

                    @Override
                    public void getSize(SizeReadyCallback cb) {

                    }

                    @Override
                    public void setRequest(Request request) {

                    }

                    @Override
                    public Request getRequest() {
                        return null;
                    }

                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onStop() {

                    }

                    @Override
                    public void onDestroy() {

                    }
                }
        );
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
                        Glide.with(context).resumeRequests();
                        break;
                    case AbsListView.OnScrollListener.SCROLL_STATE_FLING://手指不动了，但是屏幕还在滚动状态
                        break;
                    case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL://触摸后滚动
                        Glide.with(context).pauseRequests();   //停止图片加载
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
        Glide.with(context).resumeRequests();   //停止图片加载
    }

    public static void pauseRequests(Context context) {
        Glide.with(context).pauseRequests();
    }


    /**
     * 清除Glide磁盘缓存，自己获取缓存文件夹并删除
     */
    public static void clearGlideCacheDisk(final Context context) {
        try {

            if (Looper.myLooper() == Looper.getMainLooper()) {
                //清除内存缓存 在UI主线程中进行
                Glide.get(context).clearMemory();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //清理磁盘缓存 需要在子线程中执行
                        Glide.get(context).clearDiskCache();
                    }
                }).start();
            } else {
                Glide.get(context).clearDiskCache();
            }

        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    /**
     * 获取Glide缓存大小
     *
     * @return
     */
    public static long getDiskCaheSize() {
        try {
            return getFolderSize(new File(GlideUtil.glideFileName));
        } catch (Exception e) {
            return 0L;
        }
    }


    private static long getFolderSize(File file) throws Exception {
        long size = 0;
        if (!file.isDirectory()) {
            return size;
        }
        try {
            File[] fileList = file.listFiles();
            if (fileList != null && fileList.length > 0) {
                for (int i = 0; i < fileList.length; i++) {
                    // 如果下面还有文件
                    if (fileList[i].isDirectory()) {
                        size = size + getFolderSize(fileList[i]);
                    } else {
                        size = size + fileList[i].length();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }

}
