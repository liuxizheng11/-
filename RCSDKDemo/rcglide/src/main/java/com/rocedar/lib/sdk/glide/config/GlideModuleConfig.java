package com.rocedar.lib.sdk.glide.config;

import android.content.Context;
import android.support.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator;
import com.bumptech.glide.module.AppGlideModule;

/**
 * @author liuyi
 * @date 2017/2/7
 * @desc 图片配置类
 * @veison
 */
@GlideModule
public class GlideModuleConfig extends AppGlideModule {

    int diskSize = 1024 * 1024 * 200;//磁盘缓存空间，如果不设置，默认是：250 * 1024 * 1024 即250MB
    int memorySize = (int) (Runtime.getRuntime().maxMemory()) / 8;  // 取1/8最大内存作为最大缓存
//
//
//    @Override
//    public void applyOptions(final Context context, GlideBuilder builder) {
//
//        // 自定义内存和图片池大小
//        builder.setMemoryCache(new LruResourceCache(diskSize));
//        MemorySizeCalculator calculator = new MemorySizeCalculator.Builder(context)
//                .setBitmapPoolScreens(3)
//                .build();
//        builder.setBitmapPool(new LruBitmapPool(calculator.getBitmapPoolSize()));
//
//    }

    @Override
    public void registerComponents(@NonNull Context context, @NonNull Glide glide, @NonNull Registry registry) {
        super.registerComponents(context, glide, registry);
    }

    @Override
    public boolean isManifestParsingEnabled() {
        return false;
    }

    @Override
    public void applyOptions(@NonNull Context context, @NonNull GlideBuilder builder) {
        super.applyOptions(context, builder);

        builder.setMemoryCache(new LruResourceCache(diskSize));
        MemorySizeCalculator calculator = new MemorySizeCalculator.Builder(context)
                .setBitmapPoolScreens(3)
                .build();
        builder.setBitmapPool(new LruBitmapPool(calculator.getBitmapPoolSize()));
    }


}