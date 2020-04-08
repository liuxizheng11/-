package com.rocedar.lib.sdk.rcgallery.inter;

import java.util.List;

/**
 * IHandlerCallBack
 * Created by Yancy on 2016/10/26.
 */

public interface IHandlerCallBack {


    void onGallerySuccess(List<String> photoList);

    void onGalleryError();

    void chooseFolder(String name, boolean isOpen);

    void chooseChange(List<String> photoList,int maxNumber);
}
