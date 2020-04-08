package com.rocedar.lib.base.dialog;

import android.Manifest;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.rocedar.lib.base.R;
import com.rocedar.lib.base.manage.RCBaseDialog;
import com.rocedar.lib.base.permission.AcpListener;
import com.rocedar.lib.base.unit.RCPermissionUtil;
import com.rocedar.lib.base.unit.RCSpeechUtil;
import com.rocedar.lib.base.unit.RCToast;
import com.rocedar.lib.base.unit.speech.IRCSpeechListener;

import java.util.List;

/**
 * 项目名称：瑰柏SDK-商城
 * <p>
 * 作者：phj
 * 日期：2018/11/8 4:11 PM
 * 版本：V1.1.00
 * 描述：瑰柏SDK-服务商品
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class RCSpeechDialog extends RCBaseDialog implements IRCSpeechListener {


    private EditText editText;

    private TextView button;
    private RCSpeechUtil speechUtil;
    private ImageView showImage;

    private String baseText;
    private Activity activity;

    public RCSpeechDialog(Activity context, EditText editText) {
        super(context);
        this.activity = context;
        this.editText = editText;
        baseText = editText.getText().toString().trim();
        setCancelable(false);
    }

    private boolean speaking = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rc_dialog_speech);
        button = findViewById(R.id.rc_dialog_speech_button);
        showImage = findViewById(R.id.rc_dialog_speech_show);
        speechUtil = new RCSpeechUtil(activity, this);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RCPermissionUtil.getPremission(activity, new AcpListener() {
                    @Override
                    public void onGranted() {
                        if (speaking) {
                            speechUtil.getISpeech().stop();
                            dismiss();
                        } else {
                            speechUtil.getISpeech().start();
                        }
                    }

                    @Override
                    public void onDenied(List<String> permissions) {
                        RCToast.Center(activity, "您拒绝了权限，无法使用该功能.");
                    }
                }, Manifest.permission.RECORD_AUDIO);
            }
        });
        findViewById(R.id.rc_dialog_speech_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speechUtil.getISpeech().cancel();
                dismiss();
            }
        });
    }


    @Override
    public void show() {
        super.show();
        RCPermissionUtil.getPremission(activity, new AcpListener() {
            @Override
            public void onGranted() {
                speechUtil.getISpeech().start();
            }

            @Override
            public void onDenied(List<String> permissions) {
                RCToast.Center(activity, "您拒绝了权限，无法使用该功能.");
            }
        }, Manifest.permission.RECORD_AUDIO);
    }

    @Override
    public void onStart() {
        button.setText("说完了");
        speaking = true;
    }

    @Override
    public void onStop() {
        speaking =false;
        speechUtil.getISpeech().stop();
        button.setText("继续说");
        onRmsChanged(0);
    }

    @Override
    public void onRmsChanged(int rmsdB) {
        switch (rmsdB) {
            case 0:
                showImage.setImageResource(R.mipmap.rc_speech_ic_one);
                break;
            case 1:
                showImage.setImageResource(R.mipmap.rc_speech_ic_two);
                break;
            case 2:
                showImage.setImageResource(R.mipmap.rc_speech_ic_thr);
                break;
            case 3:
                showImage.setImageResource(R.mipmap.rc_speech_ic_four);
                break;
            case 4:
                showImage.setImageResource(R.mipmap.rc_speech_ic_five);
                break;
            case 5:
                showImage.setImageResource(R.mipmap.rc_speech_ic_six);
                break;
            case 6:
                showImage.setImageResource(R.mipmap.rc_speech_ic_sev);
                break;
            default:
                showImage.setImageResource(R.mipmap.rc_speech_ic_eig);
                break;
        }

    }

    @Override
    public void onError() {
        speechUtil.getISpeech().stop();
        speaking = false;
        button.setText("没听清，请重试");
        onRmsChanged(0);
    }

    @Override
    public void results(String info) {
        editText.setText(baseText + info);
        baseText = editText.getText().toString();
        editText.setSelection(editText.getText().toString().length());
        speaking = false;
        button.setText("继续说");
        onRmsChanged(0);
    }

    @Override
    public void partialResults(String info) {
        editText.setText(baseText + info);
        editText.setSelection(editText.getText().toString().length());
    }

    @Override
    public void dismiss() {
        super.dismiss();
        speaking = false;
        speechUtil.getISpeech().stop();
        speechUtil.getISpeech().onDestroy();
    }

    @Override
    public void onBackPressed() {
        dismiss();
    }
}
