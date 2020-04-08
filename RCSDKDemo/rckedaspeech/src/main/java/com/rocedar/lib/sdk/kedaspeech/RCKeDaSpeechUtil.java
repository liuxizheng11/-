package com.rocedar.lib.sdk.kedaspeech;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.ui.RecognizerDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.HashMap;
import java.util.LinkedHashMap;


/**
 * 项目名称：瑰柏SDK-商城
 * <p>
 * 作者：phj
 * 日期：2018/11/12 5:37 PM
 * 版本：V1.1.00
 * 描述：瑰柏SDK-服务商品
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class RCKeDaSpeechUtil {


    private String TAG = "kedaspeech";

    private Activity activity;

    private IRCSpeechListener listener;

    public RCKeDaSpeechUtil(Activity activity) {
        this.activity = activity;
        init();
    }

    public void setListener(IRCSpeechListener listener) {
        this.listener = listener;
    }

    //语音听写对象
    private SpeechRecognizer speechRecognizer;

    //语音听写UI
    private RecognizerDialog recognizerDialog;

    //是否显示听写UI
    private boolean isShowDialog = true;

    //用hashmap存储听写结果
    private HashMap<String, String> hashMap = new LinkedHashMap<String, String>();

    //引擎类型（云端或本地）
    private String mEngineType = null;

    //函数返回值
    private int ret = 0;


    private void init() {
        //初始化sdk 将自己申请的appid放到下面
        //此句代码应该放在application中的，这里为了方便就直接放代码中了
        SpeechUtility.createUtility(activity.getApplication(), "appid=5bd80789");
        speechRecognizer = SpeechRecognizer.createRecognizer(activity, mInitListener);
        recognizerDialog = new RecognizerDialog(activity, mInitListener);
        //这里我直接将引擎类型设置为云端，因为本地需要下载讯飞语记，这里为了方便直接使用云端
        //有需要的朋友可以加个单选框 让用户选择云端或本地
        mEngineType = SpeechConstant.TYPE_CLOUD;

    }


    /**
     * 初始化监听器。
     */
    private InitListener mInitListener = new InitListener() {

        @Override
        public void onInit(int code) {
            Log.d(TAG, "SpeechRecognizer init() code = " + code);
            if (code != ErrorCode.SUCCESS) {
                Log.e(TAG, "初始化失败，错误码：" + code);
            }
        }
    };

    /**
     * 听写监听器。
     */
    private RecognizerListener mRecognizerListener = new RecognizerListener() {

        @Override
        public void onError(SpeechError error) {
            // Tips：
            // 错误码：10118(您没有说话)，可能是录音机权限被禁，需要提示用户打开应用的录音权限。

        }

        @Override
        public void onBeginOfSpeech() {
            // 此回调表示：sdk内部录音机已经准备好了，用户可以开始语音输入
            Log.i(TAG, "开始说话");
            if (listener != null)
                listener.onStart();
        }


        @Override
        public void onEndOfSpeech() {
            // 此回调表示：检测到了语音的尾端点，已经进入识别过程，不再接受语音输入
            Log.i(TAG, "结束说话");
            if (listener != null)
                listener.onStop();
        }

        @Override
        public void onResult(RecognizerResult results, boolean isLast) {
            Log.d(TAG, results.getResultString());
            printResult(results);
            if (isLast) {
                StringBuffer resultBuffer = new StringBuffer();
                for (String key : hashMap.keySet()) {
                    resultBuffer.append(hashMap.get(key));
                }
                if (listener != null)
                    listener.results(resultBuffer.toString());
            }
        }

        @Override
        public void onVolumeChanged(int volume, byte[] data) {
            Log.i(TAG, "当前正在说话，音量大小：" + volume);
            Log.d(TAG, "返回音频数据：" + data.length);
            if (listener != null)
                listener.onRmsChanged((int) (volume/1.5));
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
            // 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
            // 若使用本地能力，会话id为null
            //	if (SpeechEvent.EVENT_SESSION_ID == eventType) {
            //		String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
            //		Log.d(TAG, "session id =" + sid);
            //	}
        }
    };


    public void onStart() {
        hashMap.clear();
        setParams();
        ret = speechRecognizer.startListening(mRecognizerListener);
        if (ret != ErrorCode.SUCCESS) {
            Log.e(TAG, "听写失败,错误码" + ret);
        }
    }


    public void onStop() {
        speechRecognizer.stopListening();
        Log.i(TAG, "停止听写");

    }


    public void onCancel() {
        speechRecognizer.cancel();
        Log.i(TAG, "取消听写");
    }


    private void printResult(RecognizerResult results) {
        String text = parseIatResult(results.getResultString());

        String sn = null;
        // 读取json结果中的sn字段
        try {
            JSONObject resultJson = new JSONObject(results.getResultString());
            sn = resultJson.optString("sn");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        hashMap.put(sn, text);

        StringBuffer resultBuffer = new StringBuffer();
        for (String key : hashMap.keySet()) {
            resultBuffer.append(hashMap.get(key));
        }
        if (listener != null)
            listener.partialResults(resultBuffer.toString());
    }

    private String parseIatResult(String json) {
        StringBuffer ret = new StringBuffer();
        try {
            JSONTokener tokener = new JSONTokener(json);
            JSONObject joResult = new JSONObject(tokener);

            JSONArray words = joResult.getJSONArray("ws");
            for (int i = 0; i < words.length(); i++) {
                // 转写结果词，默认使用第一个结果
                JSONArray items = words.getJSONObject(i).getJSONArray("cw");
                JSONObject obj = items.getJSONObject(0);
                ret.append(obj.getString("w"));
//				如果需要多候选结果，解析数组其他字段
//				for(int j = 0; j < items.length(); j++)
//				{
//					JSONObject obj = items.getJSONObject(j);
//					ret.append(obj.getString("w"));
//				}
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret.toString();
    }

    public void onDestroy() {
        if (null != speechRecognizer) {
            // 退出时释放连接
            speechRecognizer.cancel();
            speechRecognizer.destroy();
        }
    }

    private void setParams() {
        //清空参数
        speechRecognizer.setParameter(SpeechConstant.PARAMS, null);
        //设置引擎
        speechRecognizer.setParameter(SpeechConstant.ENGINE_TYPE, mEngineType);
        //设置返回数据类型
        speechRecognizer.setParameter(SpeechConstant.RESULT_TYPE, "json");
        //设置中文 普通话
        speechRecognizer.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        speechRecognizer.setParameter(SpeechConstant.ACCENT, "mandarin");

        // 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
        speechRecognizer.setParameter(SpeechConstant.VAD_BOS, "10000");

        // 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
        speechRecognizer.setParameter(SpeechConstant.VAD_EOS, "5000");

        // 设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
        speechRecognizer.setParameter(SpeechConstant.ASR_PTT, "0");

//        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
//        // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
//        speechRecognizer.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
//        speechRecognizer.setParameter(SpeechConstant.ASR_AUDIO_PATH,
//                Environment.getExternalStorageDirectory() + "/msc/iat.wav");

    }

}
