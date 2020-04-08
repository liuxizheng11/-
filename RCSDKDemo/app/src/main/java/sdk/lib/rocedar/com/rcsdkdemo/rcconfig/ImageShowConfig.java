//package sdk.lib.rocedar.com.rcsdkdemo.rcconfig;
//
//import android.content.Context;
//import android.widget.ImageView;
//
//import com.rocedar.lib.base.image.load.IRCImageManagerBase;
//
//import sdk.lib.rocedar.com.rcsdkdemo.glide.RCGlideUtil;
//
///**
// * 项目名称：瑰柏SDK-健康服务（家庭医生）
// * <p>
// * 作者：phj
// * 日期：2018/8/26 下午9:17
// * 版本：V1.1.00
// * 描述：瑰柏SDK-
// * <p>
// * CopyRight©北京瑰柏科技有限公司
// */
//public class ImageShowConfig implements IRCImageManagerBase {
//
//    /**
//     * 加载网络图片
//     *
//     * @param imageView
//     * @param url       图片地址
//     */
//    @Override
//    public void loadHttpImage(ImageView imageView, String url) {
//        RCGlideUtil.loadHttpImage(url, imageView);
//    }
//
//    /**
//     * 加载网络图片
//     *
//     * @param imageView
//     * @param url       图片地址
//     * @param w         需要显示的图片大小（dp）
//     * @param h         需要显示的图片大小（dp）
//     */
//    @Override
//    public void loadHttpImage(ImageView imageView, String url, int w, int h) {
//        RCGlideUtil.loadHttpImageThumbnail(url, imageView, w, h);
//    }
//
//    /**
//     * 加载资源文件图片
//     *
//     * @param imageView
//     * @param imageRes  文件地址
//     */
//    @Override
//    public void loadResImage(ImageView imageView, int imageRes) {
//        RCGlideUtil.loadResImage(imageRes, imageView, false);
//    }
//
//    /**
//     * 加载本地文件图片
//     *
//     * @param imageView
//     * @param imagePath 文件路径
//     */
//    @Override
//    public void loadFileImage(ImageView imageView, String imagePath) {
//        RCGlideUtil.loadFileImage(imagePath, imageView, false);
//    }
//
//    /**
//     * 加载本地文件图片
//     *
//     * @param imageView
//     * @param imagePath 文件路径
//     * @param w         需要显示的图片大小（dp）
//     * @param h         需要显示的图片大小（dp）
//     */
//    @Override
//    public void loadFileImage(ImageView imageView, String imagePath, int w, int h) {
//        RCGlideUtil.loadFileImage(imagePath, imageView, w, h);
//    }
//
//    /**
//     * 恢复加载（用于滑动处理）
//     *
//     * @param context
//     */
//    @Override
//    public void resumeRequests(Context context) {
//        RCGlideUtil.resumeRequests(context);
//    }
//
//    /**
//     * 暂停加载（用于滑动处理）
//     *
//     * @param context
//     */
//    @Override
//    public void pauseRequests(Context context) {
//        RCGlideUtil.pauseRequests(context);
//    }
//}
