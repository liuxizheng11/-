package com.rocedar.lib.base.unit;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rocedar.lib.base.R;
import com.rocedar.lib.base.config.RCBaseConfig;
import com.rocedar.lib.base.unit.other.RCBaseDarawableUtil;

/**
 * 项目名称：设备管理平台
 * <p>
 * 作者：phj
 * 日期：2017/2/13 下午4:49
 * 版本：V1.0
 * 描述：
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public class RCDialog extends Dialog {

    private Context mContext;

    /**
     * 加载中点击返回按钮取消请求
     *
     * @author phj
     */
    public interface DialogDismissListener {
        void onDismiss();
    }


    //手动关闭监听
    private DialogDismissListener mDismissListener;

    public void setDismissListener(DialogDismissListener mDismissListener) {
        this.mDismissListener = mDismissListener;
    }


    //是否可以使用返回按钮关闭
    private boolean mAllowedcancel;

    public void setmAllowedcancel(boolean mAllowedcancel) {
        this.mAllowedcancel = mAllowedcancel;
        this.setCancelable(mAllowedcancel);
    }


    public void show() {
        try {
            super.show();
        } catch (Exception e) {
        }
    }

    public void dismiss() {
        try {
            super.dismiss();
        } catch (Exception e) {
        }
    }


    /**
     * 返回按钮处理
     */
    private OnKeyListener onKeyListener = new OnKeyListener() {

        @Override
        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_BACK
                    && event.getAction() == KeyEvent.ACTION_DOWN) {
                if (!mAllowedcancel) {
                    return false;
                } else {
                    if (null != mDismissListener) {
                        mDismissListener.onDismiss();
                    }
                    dismiss();
                }
            }
            return false;
        }
    };


    public void setMessage(String temp) {
        if (mMessage != null)
            mMessage.setText(temp);
    }

    private TextView mMessage;
    private ImageView imageView;


    /**
     * 加载中的dialog
     *
     * @param context
     */
    public RCDialog(Context context) {
        super(context, R.style.RC_Theme_dialog);
        mContext = context;
        setmAllowedcancel(true);
        setContentView(R.layout.rc_dialog_handler);
        imageView = (ImageView) findViewById(R.id.view_handler_dialog_image);
        try {
            if (RCBaseConfig.getBaseConfig().imageResLoading() > 0)
                RCImageShow.loadResGif(RCBaseConfig.getBaseConfig().imageResLoading(), imageView);
            else
                RCImageShow.loadResGif(R.mipmap.rc_handler, imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Window wi = getWindow();
        this.setCancelable(false);
        WindowManager.LayoutParams params = wi.getAttributes();
        params.gravity = Gravity.CENTER;
        this.setOnKeyListener(onKeyListener);
    }


    /**
     * 按钮类的dialog，两个按钮的样式
     *
     * @param context
     * @param message            Dialog显示的信息String[]
     *                           [0]Title文字，传Null为隐藏
     *                           [1]提示内容文字
     *                           [2]左按钮文字,传null为只有一个按钮，传“”为取消
     *                           [3]右按钮文字,传null为只有一个按钮，传“”为确定
     *                           注：1.标题文字或提示内容文字传空则只显示不为空的项，为空隐藏，两个都传空显示空内容；
     *                           2.两按钮文字其中一个为空则只显示一个，两空显示取消。
     * @param clickListenerleft
     * @param clickListenerright
     */
    public RCDialog(Context context, String[] message,
                    View.OnClickListener clickListenerleft,
                    View.OnClickListener clickListenerright) {
        super(context, R.style.RC_Theme_dialog);
        setContentView(R.layout.rc_dialog_message);
        Window wi = getWindow();
        mMessage = (TextView) findViewById(R.id.view_dialog_message_text_message);
        TextView titleView = (TextView) findViewById(R.id.view_dialog_message_text_title);
        TextView but1 = (TextView) findViewById(R.id.view_dialog_message_button_left);
        TextView but2 = (TextView) findViewById(R.id.view_dialog_message_button_right);
        TextView but3 = (TextView) findViewById(R.id.view_dialog_message_button_certen);
        LinearLayout l1 = (LinearLayout) findViewById(R.id.messgagedialog_button_left_layout);
        LinearLayout l2 = (LinearLayout) findViewById(R.id.messgagedialog_button_right_layout);
        l1.setBackground(RCBaseDarawableUtil.view_dialog_message_left(context));
        l2.setBackground(RCBaseDarawableUtil.view_dialog_message_right(context));
        but3.setBackground(RCBaseDarawableUtil.view_dialog_message_center(context));
        if (message.length >= 0 && message[0] != null) {
            titleView.setVisibility(View.VISIBLE);
            titleView.setText(message[0]);
        }
        if (message.length >= 1 && message[1] != null) {
            mMessage.setText(message[1]);
        }
        if (clickListenerleft == null) {
            clickListenerleft = new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    dismiss();
                }
            };
        }
        if (clickListenerright == null) {
            clickListenerright = new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    dismiss();
                }
            };
        }
        if (message.length >= 4) {
            if (message[2] != null && message[3] != null) {
                if (!message[2].equals("")) {
                    but1.setText(message[2]);
                }
                if (!message[3].equals(""))
                    but2.setText(message[3]);
                l1.setOnClickListener(clickListenerleft);
                l2.setOnClickListener(clickListenerright);
            } else if (message[2] != null && message[3] == null) {
                if (!message[2].equals("")) {
                    but3.setText(message[2]);
                }
                l1.setVisibility(View.GONE);
                l2.setVisibility(View.GONE);
                but3.setVisibility(View.VISIBLE);
                but3.setOnClickListener(clickListenerleft);
            } else if (message[2] == null && message[3] != null) {
                if (!message[3].equals(""))
                    but3.setText(message[3]);
                l1.setVisibility(View.GONE);
                l2.setVisibility(View.GONE);
                but3.setVisibility(View.VISIBLE);
                but3.setOnClickListener(clickListenerright);
            } else if (message[2] == null && message[3] == null) {
                but3.setText("确定");
                l1.setVisibility(View.GONE);
                l2.setVisibility(View.GONE);
                but3.setVisibility(View.VISIBLE);
                but3.setOnClickListener(clickListenerright);
            }
        }

        WindowManager.LayoutParams params = wi.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.gravity = Gravity.CENTER;

    }


}
