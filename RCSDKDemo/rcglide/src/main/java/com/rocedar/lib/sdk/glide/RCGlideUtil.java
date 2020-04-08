package com.rocedar.lib.sdk.glide;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.disklrucache.DiskLruCache;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.engine.cache.DiskCache;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.SafeKeyGenerator;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.bumptech.glide.signature.EmptySignature;
import com.rocedar.lib.sdk.glide.cache.DataCacheKey;

import java.io.File;

/**
 * 项目名称：瑰柏SDK-健康服务（家庭医生）
 * <p>
 * 作者：phj
 * 日期：2018/8/25 下午3:56
 * 版本：V1.1.00
 * 描述：瑰柏SDK-
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class RCGlideUtil {

    private final float THUMB_SIZE = 0.5f; //0-1之间  10%原图的大小


    public void resumeRequests(Context context) {
        Glide.with(context).resumeRequests();
    }

    public void pauseRequests(Context context) {
        Glide.with(context).pauseRequests();
    }


    public void loadHttpImage(String imgUrl, ImageView imageView, int error, int placeholder) {
        final ImageView view = imageView;
        RequestOptions options = new RequestOptions().priority(Priority.NORMAL).
                diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(error).placeholder(placeholder);
        if (imgUrl.toUpperCase().contains(".GIF")) {
            options.skipMemoryCache(true);
        }
        if (imageView.getScaleType() == ImageView.ScaleType.CENTER_CROP) {
            options.centerCrop();
        } else {
            options.fitCenter();
        }
        if (imageView.getScaleType() == ImageView.ScaleType.CENTER) {
            Glide.with(imageView.getContext()).load(imgUrl).apply(options).into(new SimpleTarget<Drawable>() {
                @Override
                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                    view.setImageDrawable(resource);
                }
            });
        } else {
            Glide.with(imageView.getContext()).load(imgUrl).apply(options).into(imageView);
        }
    }


    public void loadHttpImageThumbnail(String imgUrl, ImageView imageView,
                                       int widths, int heights, int error, int placeholder) {
        final ImageView view = imageView;
        RequestOptions options = new RequestOptions().priority(Priority.NORMAL).
                diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(error).placeholder(placeholder);
        if (imgUrl.toUpperCase().contains(".GIF")) {
            options.skipMemoryCache(true);
        }
        if (imageView.getScaleType() == ImageView.ScaleType.CENTER_CROP) {
            options.centerCrop();
        } else {
            options.fitCenter();
        }
        int width = (int) (Resources.getSystem().getDisplayMetrics().density * widths);
        int height = (int) (Resources.getSystem().getDisplayMetrics().density * heights);
        options.override(width, height);
        if (imageView.getScaleType() == ImageView.ScaleType.CENTER) {
            Glide.with(imageView.getContext()).load(imgUrl).apply(options).into(new SimpleTarget<Drawable>() {
                @Override
                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                    view.setImageDrawable(resource);
                }
            });
        } else {
            Glide.with(imageView.getContext()).load(imgUrl).apply(options).into(imageView);
        }

    }

//
//    public void loadHttpGifImage(String imgUrl, final ImageView imageView,
//                                 boolean isThumbnail, int error, int placeholder) {
//        RequestOptions options = new RequestOptions().priority(Priority.NORMAL)
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .skipMemoryCache(true)
//                .error(error).placeholder(placeholder);
//        if (imageView.getScaleType() == ImageView.ScaleType.CENTER_CROP) {
//            options.centerCrop();
//        } else {
//            options.fitCenter();
//        }
//        RequestBuilder requestBuilder = Glide.with(imageView.getContext()).asGif().load(imgUrl).apply(options);
//        if (isThumbnail) {
//            requestBuilder.thumbnail(THUMB_SIZE);
//        }
//        requestBuilder.into(imageView);
//    }


    public void loadResImage(int imgUrl, ImageView imageView, boolean isThumbnail,
                             int error, int placeholder) {
        RequestOptions options = new RequestOptions().priority(Priority.HIGH)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .error(error).placeholder(placeholder);
        if (imageView.getScaleType() == ImageView.ScaleType.CENTER_CROP) {
            options.centerCrop();
        } else {
            options.fitCenter();
        }
        if (isThumbnail) {
            int width = (int) (Resources.getSystem().getDisplayMetrics().density * 50);
            int height = (int) (Resources.getSystem().getDisplayMetrics().density * 50);
            options.override(width, height);
        }
        Glide.with(imageView.getContext()).load(imgUrl).apply(options).into(imageView);
    }


    public void loadFileImage(String imgUrl, ImageView imageView, boolean isThumbnail,
                              int error, int placeholder) {
        RequestOptions options = new RequestOptions().priority(Priority.HIGH)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .error(error).placeholder(placeholder);
        if (imgUrl.toUpperCase().contains(".GIF")) {
            options.skipMemoryCache(true);
        }
        if (imageView.getScaleType() == ImageView.ScaleType.CENTER_CROP) {
            options.centerCrop();
        } else {
            options.fitCenter();
        }
        if (isThumbnail) {
            int width = (int) (Resources.getSystem().getDisplayMetrics().density * 50);
            int height = (int) (Resources.getSystem().getDisplayMetrics().density * 50);
            options.override(width, height);
        }
        Glide.with(imageView.getContext()).load(imgUrl).apply(options).into(imageView);
    }


    public void loadFileImage(String imgUrl, ImageView imageView
            , int error, int placeholder, int widths, int heights) {
        RequestOptions options = new RequestOptions().priority(Priority.HIGH)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .placeholder(placeholder).error(error).priority(Priority.HIGH);
        if (imageView.getScaleType() == ImageView.ScaleType.CENTER_CROP) {
            options.centerCrop();
        } else {
            options.fitCenter();
        }
        int width = (int) (Resources.getSystem().getDisplayMetrics().density * widths);
        int height = (int) (Resources.getSystem().getDisplayMetrics().density * heights);
        options.override(width, height);
        Glide.with(imageView.getContext()).load(imgUrl).apply(options).into(imageView);
    }


    /**
     * 清除Glide磁盘缓存，自己获取缓存文件夹并删除
     */
    public void clearGlideCacheDisk(final Context context) {
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
    public long getDiskCacheSize(Context context) {
        try {
            return getFolderSize(new File(context.getCacheDir() + "/" +
                    InternalCacheDiskCacheFactory.DEFAULT_DISK_CACHE_DIR));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0L;
    }


    private long getFolderSize(File file) throws Exception {
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

    public static void downloadImage(final Context context, final String imageUrl,
                                     final RCGlideDownListener listener) {
        File flie = getGlideCacheFile(context, imageUrl);
        if (flie != null) {
            listener.onResourceReady(flie);
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Glide.with(context).asFile()
                            .load(imageUrl).listener(new RequestListener<File>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<File> target, boolean isFirstResource) {
                            listener.onLoadFailed(e.getMessage());
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(File resource, Object model, Target<File> target, DataSource dataSource, boolean isFirstResource) {
                            listener.onResourceReady(resource);
                            return false;
                        }
                    }).submit();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    /**
     * 获取是否有某张原图的缓存
     * 缓存模式必须是：DiskCacheStrategy.SOURCE 才能获取到缓存文件
     */
    public static File getGlideCacheFile(Context context, String url) {
        try {
            DataCacheKey dataCacheKey = new DataCacheKey(new GlideUrl(url), EmptySignature.obtain());
            SafeKeyGenerator safeKeyGenerator = new SafeKeyGenerator();
            String safeKey = safeKeyGenerator.getSafeKey(dataCacheKey);
            File file = new File(context.getCacheDir(), DiskCache.Factory.DEFAULT_DISK_CACHE_DIR);
            DiskLruCache diskLruCache =
                    DiskLruCache.open(file, 1, 1, DiskCache.Factory.DEFAULT_DISK_CACHE_SIZE);
            DiskLruCache.Value value = diskLruCache.get(safeKey);
            if (value != null) {
                return value.getFile(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
