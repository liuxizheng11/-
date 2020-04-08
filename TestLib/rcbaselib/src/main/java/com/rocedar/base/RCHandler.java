package com.rocedar.base;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.rocedar.base.network.RequestCode;
import com.rocedar.base.network.RequestData;

import java.util.Date;

/**
 * 项目名称：设备管理平台
 * <p>
 * 作者：phj
 * 日期：2017/2/13 下午4:42
 * 版本：V1.0
 * 描述：用于网络异步请求，弹出请求中的样式，UI中可以显示正在加载或其它内容
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public class RCHandler extends Handler {


    public RCDialog dialog;

    //取消handler显示
    public final static int GETDATA_OK = 0;
    //更新handler显示的内容
    public final static int MESSAGE = 2;
    //开始展示handler
    public final static int START = 1;


    private Context context;

    public RCHandler(Context context) {
        this.context = context;
    }


    private long outTime = 40 * 1000;


    /**
     * 设置关闭监听
     *
     * @param mDismissListener
     */
    public void setmDismissListener(RCDialog.DialogDismissListener mDismissListener) {
        if (dialog != null)
            dialog.setDismissListener(mDismissListener);
    }


    /**
     * 设置超时时间
     *
     * @param outTime
     */
    public void setOutTime(long outTime) {
        this.outTime = outTime;
    }

    /**
     * 数据解析/加载完毕
     */
    public void sendMessage(int what) {
        Message message = new Message();
        message.what = what;
        this.sendMessage(message);
    }

    /**
     * handler消息
     *
     * @param what 消息类型
     * @param obj  显示的内容
     */
    public void sendMessage(int what, String obj) {
        Message message = new Message();
        message.what = what;
        message.obj = obj;
        this.sendMessage(message);
    }


    /**
     * 设置是否可以返回，
     *
     * @param mAllowedcancel true可以返回，false不可返回
     */
    public void setAllowedcancel(boolean mAllowedcancel) {
        if (dialog != null) {
            dialog.setmAllowedcancel(mAllowedcancel);
        }
    }


    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case GETDATA_OK:
                if (dialog != null) {
                    dialog.dismiss();
                }
                dialog = null;
                break;
            case START:
                if (dialog == null) {
                    if (msg.obj != null) {
                        dialog = new RCDialog(context, msg.obj.toString());
                    } else {
                        dialog = new RCDialog(context, "加载中…");
                    }
                    dialog.show();
                } else {
                    if (!dialog.isShowing()) {
                        dialog.show();
                    }
                }
                showDialogTag = new Date().getTime();
                settingTimeOut(showDialogTag);
                break;
            case MESSAGE:
                if (dialog != null) {
                    dialog.setMessage(msg.obj.toString());
                }
                break;
        }
    }


    private long showDialogTag = -1;

    private void settingTimeOut(final long tag) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                if (showDialogTag == tag)
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                        if (RequestData.mIResponseDataLast != null)
                            RequestData.mIResponseDataLast.getDataErrorListener("网络响应过慢或无网络",
                                    RequestCode.STATUS_CODE_NOT_NETWORK);
                        else
                            RCToast.Center(context, "网络响应过慢或无网络");

                    }
            }
        }, outTime);
    }


}
