package com.rocedar.lib.base.config;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import com.rocedar.lib.base.image.load.IRCImageManager;
import com.rocedar.lib.base.image.load.IRCImageManagerBase;
import com.rocedar.lib.base.image.load.ImageManagerImpl;
import com.rocedar.lib.base.network.NetworkMethod;
import com.rocedar.lib.base.unit.RCLog;

import java.util.HashMap;
import java.util.Map;

/**
 * 项目名称：瑰柏SDK-基础库
 * <p>
 * 作者：phj
 * 日期：2018/5/21 下午5:15
 * 版本：V1.0.00
 * 描述：瑰柏SDK-基础配置
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class RCBaseConfig {

    private static final String TAG = "RCBaseConfig";


    public static void setBaseConfigClassPath(Class c) {
        RCSPUtilInfo.saveClassPath(BASECONFIGCLASSKEY, c.getName());
        iRCFDConfig = null;
    }


    public static void setWebViewConfigClassPath(Class c) {
        RCSPUtilInfo.saveClassPath(WEBVIEWCONFIGCLASSKEY, c.getName());
        iWebViewBaseUtil = null;
    }

    public static void setImageManagerBase(IRCImageManagerBase c) {
        RCSPUtilInfo.saveClassPath(BASE_LOAD_IMAGE, c.getClass().getName());
        imageManager = null;
    }


    public static String getPNetworkUrl() {
        return Config.P_NETWORK_URL;
    }




    /**
     * 获取图片或视频的绝对路径
     *
     * @param endUrl
     * @return
     */
    public static String getImageURL(String endUrl) {
        if (endUrl.startsWith("/")) {
            if (Config.APPIMAGEURL.endsWith("/")) {
                return Config.APPIMAGEURL + endUrl.substring(1);
            } else {
                return Config.APPIMAGEURL + endUrl;
            }
        } else {
            if (Config.APPIMAGEURL.endsWith("/")) {
                return Config.APPIMAGEURL + endUrl;
            } else {
                return Config.APPIMAGEURL + "/" + endUrl;
            }
        }
    }


    private static final String BASECONFIGCLASSKEY = "base";
    private static final String WEBVIEWCONFIGCLASSKEY = "webview";
    private static final String BASE_LOAD_IMAGE = "load_image";

    //动态主题样式
    public static int themes = -1;


    private static IRCBaseUtil iRCFDConfig;


    public static IRCBaseUtil getBaseConfig() {
        if (iRCFDConfig != null) return iRCFDConfig;
        if (!RCSPUtilInfo.getClassPath(BASECONFIGCLASSKEY).equals("")) {
            try {
                Class classInfo = Class.forName(RCSPUtilInfo.getClassPath(BASECONFIGCLASSKEY));
                iRCFDConfig = (IRCBaseUtil) classInfo.newInstance();
                if (!(iRCFDConfig instanceof IRCBaseUtil)) {
                    iRCFDConfig = null;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (iRCFDConfig == null) {
            iRCFDConfig = new RCBaseUtilImpl();
        }
        return iRCFDConfig;
    }


    private static IWebViewBaseUtil iWebViewBaseUtil;


    public static IWebViewBaseUtil getWebViewConfig() {
        if (iWebViewBaseUtil != null) return iWebViewBaseUtil;
        if (!RCSPUtilInfo.getClassPath(WEBVIEWCONFIGCLASSKEY).equals("")) {
            try {
                Class classInfo = Class.forName(RCSPUtilInfo.getClassPath(WEBVIEWCONFIGCLASSKEY));
                iWebViewBaseUtil = (IWebViewBaseUtil) classInfo.newInstance();
                if (!(iWebViewBaseUtil instanceof IWebViewBaseUtil)) {
                    iWebViewBaseUtil = null;
                }
            } catch (Exception e) {
                e.printStackTrace();
                iWebViewBaseUtil = null;
                RCLog.e(TAG, "类对象获取失败");
            }
        }
        if (iWebViewBaseUtil == null) {
//            iWebViewBaseUtil = new IWebViewBaseUtil();
        }
        return iWebViewBaseUtil;
    }


    public static Map<NetworkMethod, String> netWorkConfigMap = new HashMap<>();

    public static Map<NetworkMethod, String> getNetWorkConfigMap() {
        return netWorkConfigMap;
    }

    /**
     * 设置网络URL
     *
     * @param method
     * @param url
     */
    public static void setNetWorkConfig(NetworkMethod method, String url) {
        RCBaseConfig.netWorkConfigMap.put(method, url);
    }

    private static IRCImageManager imageManager;

    public static IRCImageManager getImageManager() {
        if (imageManager != null) return imageManager;
        if (!RCSPUtilInfo.getClassPath(BASE_LOAD_IMAGE).equals("")) {
            try {
                Class classInfo = Class.forName(RCSPUtilInfo.getClassPath(BASE_LOAD_IMAGE));
                IRCImageManagerBase imageManagerBase = (IRCImageManagerBase) classInfo.newInstance();
                if (!(imageManagerBase instanceof IRCImageManager) &&
                        !(imageManagerBase instanceof IRCImageManagerBase)) {
                    imageManager = null;
                }
                imageManager = new TempImageManager(imageManagerBase);
            } catch (Exception e) {
                e.printStackTrace();
                imageManager = null;
                RCLog.e(TAG, "类对象获取失败");
            }
        }
        if (imageManager == null) {
            ImageManagerImpl manager = new ImageManagerImpl();
            if (manager.hasImplementation()) {
                imageManager = manager;
            }
        }
        if (imageManager == null) {
            Log.e(TAG, "请实现IRCImageManagerBase接口中的方法以加载图片，" +
                    "或引用工具库<>，引用工具库后无需其他配置");
        }
        return imageManager;
    }


    public static class TempImageManager implements IRCImageManager {

        private IRCImageManagerBase imageManagerBase;

        public TempImageManager(IRCImageManagerBase imageManagerBase) {
            this.imageManagerBase = imageManagerBase;
        }

        @Override
        public void setHoldImage(int imageRes) {

        }

        @Override
        public void setErrorImage(int imageRes) {

        }

        @Override
        public long getImageDiskCacheSize(Context context) {
            return -1;
        }

        @Override
        public void clearImageDiskCache(Context context) {

        }

        @Override
        public void loadHttpImage(ImageView imageView, String imageUrl) {
            imageManagerBase.loadHttpImage(imageView, imageUrl);
        }

        @Override
        public void loadHttpImage(ImageView imageView, String imageUrl, int w, int h) {
            imageManagerBase.loadHttpImage(imageView, imageUrl, w, h);
        }

        @Override
        public void loadResImage(ImageView imageView, int imageRes) {
            imageManagerBase.loadResImage(imageView, imageRes);
        }

        @Override
        public void loadFileImage(ImageView imageView, String imagePath) {
            imageManagerBase.loadFileImage(imageView, imagePath);
        }

        @Override
        public void loadFileImage(ImageView imageView, String imagePath, int w, int h) {
            imageManagerBase.loadFileImage(imageView, imagePath, w, h);
        }

        @Override
        public void resumeRequests(Context context) {
            imageManagerBase.resumeRequests(context);
        }

        @Override
        public void pauseRequests(Context context) {
            imageManagerBase.pauseRequests(context);
        }
    }

}
