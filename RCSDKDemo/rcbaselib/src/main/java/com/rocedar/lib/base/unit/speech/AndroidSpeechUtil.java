package com.rocedar.lib.base.unit.speech;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;

import com.rocedar.lib.base.unit.RCLog;

import java.util.ArrayList;
import java.util.Locale;

/**
 * 项目名称：瑰柏SDK-商城
 * <p>
 * 作者：phj
 * 日期：2018/10/30 3:50 PM
 * 版本：V1.1.00
 * 描述：瑰柏SDK-服务商品
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class AndroidSpeechUtil implements IRCSpeech, RecognitionListener {


    private static final String TAG = "AndroidSpeech";

    private Activity activity;

    public AndroidSpeechUtil(Activity activity) {
        this.activity = activity;
        mRecognizer = SpeechRecognizer.createSpeechRecognizer(activity);
        mRecognizer.setRecognitionListener(this);
    }

    private IRCSpeechListener listener;

    @Override
    public void setListener(IRCSpeechListener listener) {
        this.listener = listener;
    }

    private SpeechRecognizer mRecognizer;
    private long mStartTime;

    @Override
    public void start() {
        promptSpeechInput();
    }

    @Override
    public void stop() {
        mRecognizer.stopListening();
    }

    @Override
    public void cancel() {
        mRecognizer.cancel();
    }

    @Override
    public void onDestroy() {
        mRecognizer.destroy();
    }

    private void promptSpeechInput() {
        mStartTime = System.currentTimeMillis();
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
//        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
//                RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 5);
        intent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_MINIMUM_LENGTH_MILLIS, 10000);
        intent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);
        mRecognizer.startListening(intent);
    }

    @Override
    public void onReadyForSpeech(Bundle params) {
        RCLog.d(TAG, "准备完毕-->onReadyForSpeech");
    }

    @Override
    public void onBeginningOfSpeech() {
        RCLog.d(TAG, "开始录音-->onBeginningOfSpeech");
        if (listener != null)
            listener.onStart();
    }

    @Override
    public void onRmsChanged(float rmsdB) {
        RCLog.d(TAG, "onRmsChanged: " + rmsdB);
        if (listener != null)
            listener.onRmsChanged((int) rmsdB / 3);
    }

    @Override
    public void onBufferReceived(byte[] buffer) {
    }

    @Override
    public void onEndOfSpeech() {

    }

    @Override
    public void onError(int error) {
        RCLog.e(TAG, "error code: " + error + " msg: " + getErrorMsg(error));
        if (listener != null)
            listener.onError();
    }

    @Override
    public void onResults(Bundle results) {
        RCLog.d(TAG, "onResults: " + results.toString());
        dump(results);
        stop();
        ArrayList<String> nbest = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        if (nbest.size() > 0) {
            if (listener != null) {
                listener.results(nbest.get(0));
            }
        }
    }

    @Override
    public void onPartialResults(Bundle partialResults) {
        RCLog.d(TAG, "partialResults: " + partialResults.toString());
        dump(partialResults);
        ArrayList<String> nbest = partialResults.getStringArrayList("android.speech.extra.UNSTABLE_TEXT");
        if (nbest.size() > 0) {
            if (listener != null) {
                listener.partialResults(nbest.get(0));
            }
        }
    }

    @Override
    public void onEvent(int eventType, Bundle params) {
        RCLog.d(TAG, "type: " + eventType + "params: " + params.toString());
        dump(params);
    }

    private void dump(Bundle bundle) {
        if (bundle != null) {
            Log.d(TAG, "--- dumping " + bundle.toString());
            for (String key : bundle.keySet()) {
                Object value = bundle.get(key);
                Log.d(TAG, String.format("%s %s (%s)", key,
                        value.toString(), value.getClass().getName()));
            }
        }
    }

    private static String getErrorMsg(int error) {
        switch (error) {
            case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                return "Network operation timed out.";
            case SpeechRecognizer.ERROR_NETWORK:
                return "Other network related errors.";
            case SpeechRecognizer.ERROR_AUDIO:
                return "Audio recording error.";
            case SpeechRecognizer.ERROR_SERVER:
                return "Server sends error status.";
            case SpeechRecognizer.ERROR_CLIENT:
                return "Other client side errors.";
            case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                return "No speech input.";
            case SpeechRecognizer.ERROR_NO_MATCH:
                return "No recognition result matched.";
            case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                return "RecognitionService busy.";
            case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                return "Insufficient permissions.";
            default:
                return "Unknown error.";
        }
    }


}


