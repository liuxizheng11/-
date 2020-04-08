//package sdk.lib.rocedar.com.rcsdkdemo.glide;
//
//import android.content.Context;
//import android.content.res.Resources;
//import android.graphics.drawable.Drawable;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//import android.widget.ImageView;
//
//import com.bumptech.glide.Glide;
//import com.bumptech.glide.Priority;
//import com.bumptech.glide.load.engine.DiskCacheStrategy;
//import com.bumptech.glide.request.RequestOptions;
//import com.bumptech.glide.request.target.SimpleTarget;
//import com.bumptech.glide.request.target.Target;
//import com.bumptech.glide.request.transition.Transition;
//
//import sdk.lib.rocedar.com.rcsdkdemo.R;
//
///**
// * 项目名称：瑰柏SDK-健康服务（家庭医生）
// * <p>
// * 作者：phj
// * 日期：2018/8/25 下午3:56
// * 版本：V1.1.00
// * 描述：瑰柏SDK-
// * <p>
// * CopyRight©北京瑰柏科技有限公司
// */
//public class RCGlideUtil {
//
//    private final float THUMB_SIZE = 0.5f; //0-1之间  10%原图的大小
//
//
//    public static void resumeRequests(Context context) {
//        Glide.with(context).resumeRequests();
//    }
//
//    public static void pauseRequests(Context context) {
//        Glide.with(context).pauseRequests();
//    }
//
//    private static int error = R.mipmap.rc_image_default;
//    private static int placeholder = R.mipmap.rc_image_default;
//
//    public static void loadHttpImage(String imgUrl, ImageView imageView) {
//        final ImageView view = imageView;
//        RequestOptions options = new RequestOptions().priority(Priority.NORMAL).
//                diskCacheStrategy(DiskCacheStrategy.ALL).skipMemoryCache(true)
//                .error(error).placeholder(placeholder);
//        options.override(Target.SIZE_ORIGINAL);
//        if (imgUrl.toUpperCase().contains(".GIF")) {
//            options.skipMemoryCache(true);
//        }
//        if (imageView.getScaleType() == ImageView.ScaleType.CENTER_CROP) {
//            options.centerCrop();
//        } else {
//            options.fitCenter();
//        }
//        if (imageView.getScaleType() == ImageView.ScaleType.CENTER) {
//            Glide.with(imageView.getContext()).load(imgUrl).apply(options).into(new SimpleTarget<Drawable>() {
//                @Override
//                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
//                    view.setImageDrawable(resource);
//                }
//            });
//        } else {
//            Glide.with(imageView.getContext()).load(imgUrl).apply(options).into(imageView);
//        }
//    }
//
//
//    public static void loadHttpImageThumbnail(String imgUrl, ImageView imageView,
//                                              int widths, int heights) {
//        final ImageView view = imageView;
//        RequestOptions options = new RequestOptions().priority(Priority.HIGH).
//                diskCacheStrategy(DiskCacheStrategy.ALL)
//                .error(error).placeholder(placeholder);
//        if (imgUrl.toUpperCase().contains(".GIF")) {
//            options.skipMemoryCache(true);
//        }
//        if (imageView.getScaleType() == ImageView.ScaleType.CENTER_CROP) {
//            options.centerCrop();
//        } else {
//            options.fitCenter();
//        }
//        int width = (int) (Resources.getSystem().getDisplayMetrics().density * widths);
//        int height = (int) (Resources.getSystem().getDisplayMetrics().density * heights);
//        options.override(width, height);
//        if (imageView.getScaleType() == ImageView.ScaleType.CENTER) {
//            Glide.with(imageView.getContext()).load(imgUrl).apply(options).into(new SimpleTarget<Drawable>() {
//                @Override
//                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
//                    view.setImageDrawable(resource);
//                }
//            });
//        } else {
//            Glide.with(imageView.getContext()).load(imgUrl).apply(options).into(imageView);
//        }
//
//    }
//
//
//    public static void loadResImage(int imgUrl, ImageView imageView, boolean isThumbnail) {
//        RequestOptions options = new RequestOptions().priority(Priority.HIGH)
//                .diskCacheStrategy(DiskCacheStrategy.NONE)
//                .error(error).placeholder(placeholder);
//        if (imageView.getScaleType() == ImageView.ScaleType.CENTER_CROP) {
//            options.centerCrop();
//        } else {
//            options.fitCenter();
//        }
//        if (isThumbnail) {
//            int width = (int) (Resources.getSystem().getDisplayMetrics().density * 50);
//            int height = (int) (Resources.getSystem().getDisplayMetrics().density * 50);
//            options.override(width, height);
//        }
//        Glide.with(imageView.getContext()).load(imgUrl).apply(options).into(imageView);
//    }
//
//
//    public static void loadFileImage(String imgUrl, ImageView imageView, boolean isThumbnail) {
//        RequestOptions options = new RequestOptions().priority(Priority.HIGH)
//                .diskCacheStrategy(DiskCacheStrategy.NONE)
//                .error(error).placeholder(placeholder);
//        if (imgUrl.toUpperCase().contains(".GIF")) {
//            options.skipMemoryCache(true);
//        }
//        if (imageView.getScaleType() == ImageView.ScaleType.CENTER_CROP) {
//            options.centerCrop();
//        } else {
//            options.fitCenter();
//        }
//        if (isThumbnail) {
//            int width = (int) (Resources.getSystem().getDisplayMetrics().density * 50);
//            int height = (int) (Resources.getSystem().getDisplayMetrics().density * 50);
//            options.override(width, height);
//        } else {
//            options.override(Target.SIZE_ORIGINAL).skipMemoryCache(true);
//        }
//        Glide.with(imageView.getContext()).load(imgUrl).apply(options).into(imageView);
//    }
//
//
//    public static void loadFileImage(String imgUrl, ImageView imageView, int widths, int heights) {
//        RequestOptions options = new RequestOptions().priority(Priority.HIGH)
//                .diskCacheStrategy(DiskCacheStrategy.NONE)
//                .error(error).priority(Priority.HIGH).placeholder(placeholder);
//        if (imageView.getScaleType() == ImageView.ScaleType.CENTER_CROP) {
//            options.centerCrop();
//        } else {
//            options.fitCenter();
//        }
//        int width = (int) (Resources.getSystem().getDisplayMetrics().density * widths);
//        int height = (int) (Resources.getSystem().getDisplayMetrics().density * heights);
//        options.override(width, height);
//        Glide.with(imageView.getContext()).load(imgUrl).apply(options).into(imageView);
//    }
//
//
//}
