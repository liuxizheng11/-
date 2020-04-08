package com.rocedar.lib.base.image.photo;

import android.app.Activity;

import com.rocedar.lib.base.image.photo.configs.GlideImageLoader;
import com.rocedar.lib.base.unit.RCAndroid;
import com.rocedar.lib.sdk.rcgallery.config.GalleryConfig;
import com.rocedar.lib.sdk.rcgallery.config.GalleryPick;
import com.rocedar.lib.sdk.rcgallery.inter.IHandlerCallBack;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名称：瑰柏SDK-基础库
 * <p>
 * 作者：phj
 * 日期：2018/6/2 下午10:16
 * 版本：V1.0.00
 * 描述：瑰柏SDK-照片选择，相册
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class RCAlbumActivityUtil {


    private Activity mContext;

    public RCAlbumActivityUtil(Activity mContext) {
        this.mContext = mContext;
        this.iHandlerCallBack = new IHandlerCallBack() {
            @Override
            public void onGallerySuccess(List<String> photoList) {

            }

            @Override
            public void onGalleryError() {

            }

            @Override
            public void chooseFolder(String name, boolean isOpen) {

            }

            @Override
            public void chooseChange(List<String> photoList, int maxNumber) {

            }
        };
        init();
    }

    public RCAlbumActivityUtil(Activity mContext, IHandlerCallBack iHandlerCallBack) {
        this.mContext = mContext;
        this.iHandlerCallBack = iHandlerCallBack;
        init();
    }

    private GalleryConfig galleryConfig;
    private IHandlerCallBack iHandlerCallBack;


    private List<String> path = new ArrayList<>();


    private void init() {
        galleryConfig = new GalleryConfig.Builder()
                .imageLoader(new GlideImageLoader())    // ImageLoader 加载框架（必填）
                .iHandlerCallBack(iHandlerCallBack)     // 监听接口（必填）
                .provider(RCAndroid.getFileProviderName(mContext))   // provider(必填)
                .pathList(path)                         // 记录已选的图片
                .isShowCamera(true)                     // 是否现实相机按钮  默认：false
                .filePath("/rocedar/t/")          // 图片存放路径
                .build();
    }

    public void openCamera() {
        galleryConfig.getBuilder().isOpenCamera(true).build();
        GalleryPick.getInstance().setGalleryConfig(galleryConfig).openCamera(mContext);
    }

    public void openAblumMore(int number) {
        if (number > 1)
            galleryConfig.getBuilder().crop(false).multiSelect(true, number).build();
        else
            galleryConfig.getBuilder().crop(false).multiSelect(false).build();
        GalleryPick.getInstance().setGalleryConfig(galleryConfig).open(mContext);
    }

    public void openGetHeadImage() {
        galleryConfig.getBuilder().multiSelect(false).
                crop(true, 1, 1, 300, 300).build();
        GalleryPick.getInstance().setGalleryConfig(galleryConfig).open(mContext);
    }


    public void openPreview() {
        GalleryPick.getInstance().setGalleryConfig(galleryConfig);
    }



}
