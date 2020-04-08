package com.rocedar.lib.base.image.load;

import android.content.Context;
import android.widget.ImageView;

import com.rocedar.lib.base.R;
import com.rocedar.lib.base.unit.RCLog;

import java.lang.reflect.Method;

/**
 * 项目名称：瑰柏SDK-健康服务（家庭医生）
 * <p>
 * 作者：phj
 * 日期：2018/8/25 下午4:22
 * 版本：V1.1.00
 * 描述：瑰柏SDK-
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class ImageManagerImpl implements IRCImageManager {

    private String TAG = "RC_IMAGE_GLIDE";


    public int lastHoldImage = R.mipmap.rc_image_default;
    public int lastErrorImage = R.mipmap.rc_image_default;

    public ImageManagerImpl() {
        initConfigClass();
    }

    public boolean hasImplementation() {
        return classObject != null;
    }

    @Override
    public void setHoldImage(int imageRes) {
        this.lastHoldImage = imageRes;
    }

    @Override
    public void setErrorImage(int imageRes) {
        this.lastErrorImage = imageRes;
    }

    @Override
    public long getImageDiskCacheSize(Context context) {
        if (utilClass == null) return 0;
        try {
            Method m1 = utilClass.getMethod("getDiskCacheSize", Context.class);
            return (long) m1.invoke(classObject, context);
        } catch (Exception e) {
            RCLog.w(TAG, "找不到工具类中的方法(getImageDiskCacheSize)");
            return 0;
        }
    }

    @Override
    public void clearImageDiskCache(Context context) {
        if (utilClass == null) return;
        try {
            Method m1 = utilClass.getMethod("clearGlideCacheDisk", Context.class);
            m1.invoke(classObject, context);
        } catch (Exception e) {
            RCLog.w(TAG, "找不到工具类中的方法(clearImageDiskCache)");
            return;
        }
    }

    @Override
    public void loadHttpImage(ImageView imageView, String imageUrl) {
        if (utilClass == null) return;
        try {
            Method m1 = utilClass.getMethod("loadHttpImage",
                    String.class, ImageView.class, int.class, int.class);
            m1.invoke(classObject, imageUrl, imageView, lastErrorImage, lastHoldImage);
        } catch (Exception e) {
            RCLog.w(TAG, "找不到工具类中的方法(loadHttpImage1)");
            return;
        }
    }

    @Override
    public void loadHttpImage(ImageView imageView, String imageUrl, int w, int h) {
        if (utilClass == null) return;
        try {
            Method m1 = utilClass.getMethod("loadHttpImageThumbnail",
                    String.class, ImageView.class, int.class, int.class, int.class, int.class);
            m1.invoke(classObject, imageUrl, imageView, w, h, lastErrorImage, lastHoldImage);
        } catch (Exception e) {
            RCLog.w(TAG, "找不到工具类中的方法(loadHttpImage2)");
            return;
        }
    }

    @Override
    public void loadResImage(ImageView imageView, int imageRes) {
        if (utilClass == null) return;
        try {
            Method m1 = utilClass.getMethod("loadResImage",
                    int.class, ImageView.class, boolean.class, int.class, int.class);
            m1.invoke(classObject, imageRes, imageView, false, lastErrorImage, lastHoldImage);
        } catch (Exception e) {
            RCLog.w(TAG, "找不到工具类中的方法(loadResImage)");
            return;
        }
    }

    @Override
    public void loadFileImage(ImageView imageView, String imagePath) {
        if (utilClass == null) return;
        try {
            Method m1 = utilClass.getMethod("loadFileImage",
                    String.class, ImageView.class, boolean.class, int.class, int.class);
            m1.invoke(classObject, imagePath, imageView, false, lastErrorImage, lastHoldImage);
        } catch (Exception e) {
            RCLog.w(TAG, "找不到工具类中的方法(loadFileImage1)");
            return;
        }
    }

    @Override
    public void loadFileImage(ImageView imageView, String imagePath, int w, int h) {
        if (utilClass == null) return;
        try {
            Method m1 = utilClass.getMethod("loadFileImage",
                    String.class, ImageView.class, int.class, int.class, int.class, int.class);
            m1.invoke(classObject, imagePath, imageView, lastErrorImage, lastHoldImage, w, h);
        } catch (Exception e) {
            RCLog.w(TAG, "找不到工具类中的方法(loadFileImage2)");
            return;
        }
    }

    @Override
    public void resumeRequests(Context context) {
        if (utilClass == null) return;
        try {
            Method m1 = utilClass.getMethod("resumeRequests", Context.class);
            m1.invoke(classObject, context);
        } catch (Exception e) {
            RCLog.w(TAG, "找不到工具类中的方法(resumeRequests)");
            return;
        }
    }

    @Override
    public void pauseRequests(Context context) {
        if (utilClass == null) return;
        try {
            Method m1 = utilClass.getMethod("pauseRequests", Context.class);
            m1.invoke(classObject, context);
        } catch (Exception e) {
            RCLog.w(TAG, "找不到工具类中的方法(pauseRequests)");
            return;
        }
    }


    public Class<?> utilClass = null;

    public Object classObject = null;

    private void initConfigClass() {
        if (utilClass == null || classObject == null)
            try {
                if (utilClass == null)
                    utilClass = Class.forName("com.rocedar.lib.sdk.glide.RCGlideUtil");
                if (utilClass != null) {
                    classObject = utilClass.newInstance();
                }
            } catch (ClassNotFoundException e) {
                RCLog.w(TAG, "找不到工具类");
            } catch (IllegalAccessException e) {
                RCLog.w(TAG, "工具类实例对象非法");
            } catch (InstantiationException e) {
                RCLog.w(TAG, "工具类实例对象获取出错");
            }
    }

}
