package com.rocedar.base.image.load;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;
import com.bumptech.glide.load.engine.cache.ExternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.module.GlideModule;

/**
 * @author liuyi
 * @date 2017/2/7
 * @desc  图片配置类
 * @veison
 */

public class GlideModuleConfig implements GlideModule {
    int diskSize = 1024 * 1024 * 100;//磁盘缓存空间，如果不设置，默认是：250 * 1024 * 1024 即250MB
    int memorySize = (int) (Runtime.getRuntime().maxMemory()) / 8;  // 取1/8最大内存作为最大缓存


    @Override
    public void applyOptions(final Context context, GlideBuilder builder) {

        // 定义缓存大小和位置
//        builder.setDiskCache(new InternalCacheDiskCacheFactory(context, "dongya", diskSize));  //手机磁盘
        builder.setDiskCache(new ExternalCacheDiskCacheFactory(context, "dongya", diskSize)); //sd卡磁盘

        // 默认内存和图片池大小
      /*MemorySizeCalculator calculator = new MemorySizeCalculator(context);
      int defaultMemoryCacheSize = calculator.getMemoryCacheSize(); // 默认内存大小
      int defaultBitmapPoolSize = calculator.getBitmapPoolSize(); // 默认图片池大小
      builder.setMemoryCache(new LruResourceCache(defaultMemoryCacheSize)); // 该两句无需设置，是默认的
      builder.setBitmapPool(new LruBitmapPool(defaultBitmapPoolSize));*/

//        //指定缓存目录
//        builder.setDiskCache(new DiskCache.Factory() {
//            @Override
//            public DiskCache build() {
//                File cacheLocation = new File(context.getExternalCacheDir(), "cache_dir");
//                cacheLocation.mkdirs();
//
//                return DiskLruCacheWrapper.get(cacheLocation, diskSize);
//            }
//        });

        // 自定义内存和图片池大小
        builder.setMemoryCache(new LruResourceCache(memorySize));
        builder.setBitmapPool(new LruBitmapPool(memorySize));

        // 定义图片格式
        builder.setDecodeFormat(DecodeFormat.PREFER_ARGB_8888);
    }

    @Override
    public void registerComponents(Context context, Glide glide) {

    }
}

