package com.rocedar.sharedlib.umeng.share;

import android.app.Activity;
import android.graphics.Bitmap;

import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;


/**
 * Created by phj on 2016/12/5.
 * <p>
 * 分享工具的实现类，使用分享调用该方法
 */

public class ShareUtil {

    private ShareAction shareAction;

    private Activity activity;


    /**
     * 实例化分享
     *
     * @param activity
     */
    public ShareUtil(Activity activity) {
        this.activity = activity;
        shareAction = new ShareAction(activity);
    }


    /**
     * 设置回调方法
     *
     * @param shareListenerIble
     */
    public ShareUtil setShareListenerIble(final ShareListenerIble shareListenerIble) {
        shareAction.setCallback(new UMShareListener() {
            @Override
            public void onResult(SHARE_MEDIA share_media) {
                shareListenerIble.onSuccess();
            }

            @Override
            public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                shareListenerIble.onError();
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media) {
                shareListenerIble.onCancel();
            }
        });
        return this;
    }


    /**
     * 设置分享类型（ShareType）
     *
     * @param shareType
     */
    public ShareUtil setShareType(int shareType) {
        SHARE_MEDIA shareMedia = SHARE_MEDIA.WEIXIN.toSnsPlatform().mPlatform;
        switch (shareType) {
            case 0:
                shareMedia = SHARE_MEDIA.WEIXIN.toSnsPlatform().mPlatform;
                break;
            case 1:
                shareMedia = SHARE_MEDIA.WEIXIN_CIRCLE.toSnsPlatform().mPlatform;
                break;
            case 2:
                shareMedia = SHARE_MEDIA.QQ.toSnsPlatform().mPlatform;
                break;
            case 3:
                shareMedia = SHARE_MEDIA.QZONE.toSnsPlatform().mPlatform;
                break;
        }
        shareAction.setPlatform(shareMedia);
        return this;
    }

    /**
     * 设置分享的图片（来源于网络地址）
     *
     * @param url
     */
    public ShareUtil setImageFromUrl(String url) {
        UMImage umImage = new UMImage(activity, url);
        shareAction.withMedia(umImage);
        return this;
    }


    /**
     * 设置分享的图片（来源于本地地址）
     *
     * @param url
     */
    public ShareUtil setImageFromFile(String url) {
        UMImage umImage = new UMImage(activity, url);
        shareAction.withMedia(umImage);
        return this;
    }

    /**
     * 设置分享的图片（来源于bitmap）
     *
     * @param bitmap
     */
    public ShareUtil setImageFromBitmap(Bitmap bitmap) {
        UMImage umImage = new UMImage(activity, bitmap);
        shareAction.withMedia(umImage);
        return this;
    }

    /**
     * 设置分享的图片（来源于资源文件）
     *
     * @param rId 资源文件ID
     */
    public ShareUtil setImageFromRId(int rId) {
        UMImage umImage = new UMImage(activity, rId);
        shareAction.withMedia(umImage);
        return this;
    }

    /**
     * 设置分享的网页信息
     *
     * @param title 标题
     * @param text  文字
     * @param url   URL
     */
    public ShareUtil setHtmlInfo(String title, String text, String url) {
        shareAction.withText(text).withTitle(title).withTargetUrl(url);
        return this;
    }

    /**
     * 分享
     */
    public void share() {
        shareAction.share();
    }


}
