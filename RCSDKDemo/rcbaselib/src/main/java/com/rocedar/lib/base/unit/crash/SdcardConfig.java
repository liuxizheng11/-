package com.rocedar.lib.base.unit.crash;

import android.os.Environment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by shenhuniurou
 * on 2017/1/3.
 */

public class SdcardConfig {

    /**
     * sdcard
     */
    public static final String SDCARD_FOLDER = Environment.getExternalStorageDirectory().toString();

    /**
     * 根目录
     */
    public static final String ROOT_FOLDER = SDCARD_FOLDER + "/rocedar/";

    /**
     * 日志目录
     */
    public static final String LOG_FOLDER = ROOT_FOLDER + "crash/";

    private static SdcardConfig sSdcardConfig;

    public static synchronized SdcardConfig getInstance() {
        if (sSdcardConfig == null) {
            sSdcardConfig = new SdcardConfig();
        }
        return sSdcardConfig;
    }

    public SdcardConfig() {
        initSdcard();
    }

    /**
     * sd卡初始化
     */
    private void initSdcard() {
        if (!hasSDCard())
            return;
        File logFile = new File(LOG_FOLDER);
        if (!logFile.exists()) {
            logFile.mkdirs();
        }
    }

    /**
     * 获取crash列表
     *
     * @return
     */
    public static List<File> getFileList() {
        List<File> list = new ArrayList<>();
        File logFile = new File(LOG_FOLDER);
        if (logFile.exists() && logFile.isDirectory()) {
            File files[] = logFile.listFiles();
            for (File file : files) {
                if (!file.isDirectory()) {
                    list.add(0, file);
                }
            }
        }
        return list;
    }


    /**
     * 判断是否存在SDCard
     *
     * @return
     */
    public boolean hasSDCard() {
        String status = Environment.getExternalStorageState();
        return status.equals(Environment.MEDIA_MOUNTED);
    }

    public static void clearList() {
        File logFile = new File(LOG_FOLDER);
        logFile.delete();
    }

}
