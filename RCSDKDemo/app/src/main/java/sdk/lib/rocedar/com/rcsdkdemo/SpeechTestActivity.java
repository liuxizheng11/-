package sdk.lib.rocedar.com.rcsdkdemo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import com.rocedar.lib.base.manage.RCBaseActivity;

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
public class SpeechTestActivity extends RCBaseActivity implements View.OnClickListener, RecognitionListener {
    public static void goActivity(Context context){
        Intent intent = new Intent(context, SpeechTestActivity.class);
        context.startActivity(intent);
    }


    private static final String TAG = "AndroidSpeech";
    private Button mStartBtn;
    private TextView mLogTv;
    private SpeechRecognizer mRecognizer;
    private long mStartTime;

    public static final int STATUS_None = 0;
    public static final int STATUS_WaitingReady = 2;
    public static final int STATUS_Ready = 3;
    public static final int STATUS_Speaking = 4;
    public static final int STATUS_Recognition = 5;
    private int status = STATUS_None;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speech);
        mStartBtn = (Button) findViewById(R.id.and_speech_btn);
        mLogTv = (TextView) findViewById(R.id.and_speech_tv);
        if (mStartBtn != null) {
            mStartBtn.setOnClickListener(this);
        }

        mRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        mRecognizer.setRecognitionListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.equals(mStartBtn)) {
            switch (status) {
                case STATUS_None:
                    start();
                    mStartBtn.setText("取消");
                    status = STATUS_WaitingReady;
                    break;
                case STATUS_WaitingReady:
                    cancel();
                    status = STATUS_None;
                    mStartBtn.setText("开始");
                    break;
                case STATUS_Ready:
                    cancel();
                    status = STATUS_None;
                    mStartBtn.setText("开始");
                    break;
                case STATUS_Speaking:
                    stop();
                    status = STATUS_Recognition;
                    mStartBtn.setText("识别中");
                    break;
                case STATUS_Recognition:
                    cancel();
                    status = STATUS_None;
                    mStartBtn.setText("开始");
                    break;
            }
        }
    }

    private void start() {
        promptSpeechInput();
    }

    private void stop() {
        mRecognizer.stopListening();
    }

    private void cancel() {
        mRecognizer.cancel();
        status = STATUS_None;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRecognizer.destroy();
    }

    private void promptSpeechInput() {
        mStartTime = System.currentTimeMillis();
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
//        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
//                RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 5);
        intent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_MINIMUM_LENGTH_MILLIS, 100);

//        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
//                "提示");
        intent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);
        mRecognizer.startListening(intent);
    }

    @Override
    public void onReadyForSpeech(Bundle params) {
        Log.d(TAG, "onReadyForSpeech");
        status = STATUS_Ready;
        print("准备完毕");
    }

    @Override
    public void onBeginningOfSpeech() {
        Log.d(TAG, "onBeginningOfSpeech");
        mStartBtn.setText("说完了");
        print("开始录音");
        status = STATUS_Speaking;
    }

    @Override
    public void onRmsChanged(float rmsdB) {
        Log.d(TAG, "onRmsChanged: " + rmsdB);
    }

    @Override
    public void onBufferReceived(byte[] buffer) {
    }

    @Override
    public void onEndOfSpeech() {
        Log.d(TAG, "onEndOfSpeech");
        mStartBtn.setText("识别中");
        print("开始识别");
        status = STATUS_Recognition;
    }

    @Override
    public void onError(int error) {
        Log.e(TAG, "error code: " + error + " msg: " + getErrorMsg(error));
        mStartBtn.setText("开始");
        status = STATUS_None;
    }

    @Override
    public void onResults(Bundle results) {
        start();
        Log.d(TAG, "onResults: " + results.toString());
        dump(results);
//        mStartBtn.setText("开始");
//        status = STATUS_None;
//        stop();
        ArrayList<String> nbest = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        if (nbest.size() > 0) {
            print("翻译最终结果: " + nbest.get(0));
        }
    }

    @Override
    public void onPartialResults(Bundle partialResults) {
        Log.d(TAG, "partialResults: " + partialResults.toString());
        dump(partialResults);
        ArrayList<String> nbest = partialResults.getStringArrayList("android.speech.extra.UNSTABLE_TEXT");
        if (nbest.size() > 0) {
            print("翻译部分结果: " + nbest.get(0));
        }
    }

    @Override
    public void onEvent(int eventType, Bundle params) {
        Log.d(TAG, "type: " + eventType + "params: " + params.toString());
        dump(params);
    }

    private void print(final String msg) {
        SpeechTestActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                long t = System.currentTimeMillis() - mStartTime;
                mLogTv.append(t + "ms ---- " + msg + "\n");
                ScrollView sv = (ScrollView) mLogTv.getParent();
                sv.smoothScrollTo(0, 1000000);
                Log.d(TAG, "---- " + t + "ms ---- " + msg);
            }
        });
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

    public static String getErrorMsg(int error) {
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


