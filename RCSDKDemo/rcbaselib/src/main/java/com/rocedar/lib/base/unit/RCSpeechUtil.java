package com.rocedar.lib.base.unit;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.speech.RecognizerIntent;

import com.rocedar.lib.base.unit.speech.AndroidSpeechUtil;
import com.rocedar.lib.base.unit.speech.IRCSpeech;
import com.rocedar.lib.base.unit.speech.IRCSpeechListener;
import com.rocedar.lib.base.unit.speech.KeDaSpeechUtil;

import java.util.List;

/**
 * 项目名称：瑰柏SDK-基础库
 * <p>
 * 作者：phj
 * 日期：2018/11/6 4:46 PM
 * 版本：V1.1.00
 * 描述：瑰柏SDK-语音输入工具类
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class RCSpeechUtil {

    private Activity activity;
    private IRCSpeech ircSpeech;


    public static boolean hasFunction(Activity activity) {
        PackageManager pm = activity.getPackageManager();
        List<ResolveInfo> activities = pm.queryIntentActivities(
                new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
        if (activities.size() != 0) {
            return true;
        } else {
            RCLog.e("手机不支持识别");
            return false;
        }
    }

    public IRCSpeech getISpeech() {
        return ircSpeech;
    }

    public RCSpeechUtil(Activity activity, IRCSpeechListener listener) {
        this.activity = activity;
        ircSpeech = new KeDaSpeechUtil(activity);
//        initAndroidSpeech();
        ircSpeech.setListener(listener);
    }


    private void initAndroidSpeech() {
        PackageManager pm = activity.getPackageManager();
        List<ResolveInfo> activities = pm.queryIntentActivities(
                new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
        if (activities.size() != 0) {
            ircSpeech = new AndroidSpeechUtil(activity);
        } else {
            RCLog.e("手机不支持识别");
        }
    }


}
