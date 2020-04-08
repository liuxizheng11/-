package com.rocedar.lib.base.image.load;

import android.content.Context;
import android.widget.ImageView;

/**
 * 项目名称：瑰柏SDK-健康服务（家庭医生）
 * <p>
 * 作者：phj
 * 日期：2018/8/25 下午3:57
 * 版本：V1.1.00
 * 描述：瑰柏SDK-
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public interface IRCImageManagerBase {

    /**
     * 加载网络图片
     *
     * @param imageView
     * @param imageUrl  图片地址
     */
    void loadHttpImage(ImageView imageView, String imageUrl);

    /**
     * 加载网络图片
     *
     * @param imageView
     * @param imageUrl  图片地址
     * @param w         需要显示的图片大小（dp）
     * @param h         需要显示的图片大小（dp）
     */
    void loadHttpImage(ImageView imageView, String imageUrl, int w, int h);

    /**
     * 加载资源文件图片
     *
     * @param imageView
     * @param imageRes  资源文件地址
     */
    void loadResImage(ImageView imageView, int imageRes);

    /**
     * 加载本地文件图片
     *
     * @param imageView
     * @param imagePath 文件路径
     */
    void loadFileImage(ImageView imageView, String imagePath);

    /**
     * 加载本地文件图片
     *
     * @param imageView
     * @param imagePath 文件路径
     * @param w         需要显示的图片大小（dp）
     * @param h         需要显示的图片大小（dp）
     */
    void loadFileImage(ImageView imageView, String imagePath, int w, int h);

    /**
     * 恢复加载（用于滑动处理）
     *
     * @param context
     */
    void resumeRequests(Context context);

    /**
     * 暂停加载（用于滑动处理）
     *
     * @param context
     */
    void pauseRequests(Context context);

}
