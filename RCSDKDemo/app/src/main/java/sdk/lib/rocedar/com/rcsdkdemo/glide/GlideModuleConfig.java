//package sdk.lib.rocedar.com.rcsdkdemo.glide;
//
//import android.content.Context;
//
//import com.bumptech.glide.GlideBuilder;
//import com.bumptech.glide.annotation.GlideModule;
//import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;
//import com.bumptech.glide.load.engine.cache.LruResourceCache;
//import com.bumptech.glide.module.AppGlideModule;
//
///**
// * @author liuyi
// * @date 2017/2/7
// * @desc 图片配置类
// * @veison
// */
//@GlideModule
//public class GlideModuleConfig extends AppGlideModule {
//
//    int diskSize = 1024 * 1024 * 50;//磁盘缓存空间，如果不设置，默认是：250 * 1024 * 1024 即250MB
//    int memorySize = (int) (Runtime.getRuntime().maxMemory()) / 8;  // 取1/8最大内存作为最大缓存
//
//
//    @Override
//    public void applyOptions(final Context context, GlideBuilder builder) {
//
//        // 自定义内存和图片池大小
//        builder.setMemoryCache(new LruResourceCache(diskSize));
//        builder.setBitmapPool(new LruBitmapPool(memorySize));
//
//    }
//
//
//}