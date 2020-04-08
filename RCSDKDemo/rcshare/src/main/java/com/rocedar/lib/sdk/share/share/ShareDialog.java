package com.rocedar.lib.sdk.share.share;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.rocedar.lib.sdk.share.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * 项目名称：动吖3.0
 * <p>
 * 作者：phj
 * 日期：2018/5/4 下午2:52
 * 版本：V3.6.00
 * 描述：
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public class ShareDialog extends Dialog {

    private final String SHAREKEY_QQ = "QQ";
    private final String SHAREKEY_QZONE = "Qzone";
    private final String SHAREKEY_WX = "WX";
    private final String SHAREKEY_WXPYQ = "WX_PYQ";


    private String title;
    private String content;
    private String web_url;
    private String imageUrl;

    private ImageView circle_share_delcet;
    private ImageView share_qq;
    private ImageView share_qq_space;
    private ImageView share_weixin;
    private ImageView share_pyq;

    private ShareUtil shareUtil;


    private List<String> toArray;


    private Activity context;


    /**
     * {"title":"母亲节动吖送福利","icon":"http://img.dongya.rocedar.com/d/m/1000000.png",
     * "desc":"妈妈今年母亲节孩子送健康给您",
     * "url":"http://dongya.rocedar.com/download/download.html","to":["WX","Qzone"]}
     *
     * @param context
     * @param message
     */
    public ShareDialog(Activity context, String message) {
        super(context, R.style.RC_share_Theme_dialog);
        toArray = new ArrayList<>();
        try {
            JSONObject object = new JSONObject(message);
            this.title = object.optString("title");
            this.content = object.optString("desc");
            this.web_url = object.optString("url");
            this.imageUrl = object.optString("icon");
            JSONArray array = object.getJSONArray("to");
            for (int i = 0; i < array.length(); i++) {
                toArray.add(array.optString(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        initDialog(context);
        initShareHtml();
    }

    public ShareDialog(Activity context, String title, String desc, String url, String icon, String to) {
        super(context, R.style.RC_share_Theme_dialog);
        this.title = title;
        this.content = desc;
        this.web_url = url;
        this.imageUrl = icon;
        toArray = new ArrayList<>();
        try {
            JSONArray array = new JSONArray(to);
            for (int i = 0; i < array.length(); i++) {
                toArray.add(array.optString(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        initDialog(context);
        initShareHtml();
    }

    public ShareDialog(Activity context, Bitmap bitmap) {
        super(context, R.style.RC_share_Theme_dialog);
        initDialog(context);
        initShareImage(bitmap);
    }


    private void initDialog(Activity context) {
        setContentView(R.layout.view_dialog_share);
        this.context = context;
        setCancelable(true);
        setCanceledOnTouchOutside(true);
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;


    }

    private void initShareHtml() {
        shareUtil = new ShareUtil(context);
        if (null != imageUrl && !imageUrl.equals(""))
            shareUtil.setHtmlInfo(title, web_url, content, shareUtil.getImageFromUrl(imageUrl));
        else
            shareUtil.setHtmlInfo(title, web_url, content, shareUtil.setIconFromBitmap(shareUtil.getAppIconBitmap(context)));
        doShare();
    }

    private void initShareImage(Bitmap bitmap) {
        shareUtil = new ShareUtil(context);
        shareUtil.setImageFromBitmap(bitmap);
        doShare();
    }

    private void doShare() {
        if (toArray == null || toArray.size() == 0)
            return;
        if (toArray.size() == 1) {
            if (toArray.get(0).equals(SHAREKEY_QQ)) {
                shareUtil.setShareType(2).share();
            } else if (toArray.get(0).equals(SHAREKEY_QZONE)) {
                shareUtil.setShareType(3).share();
            } else if (toArray.get(0).equals(SHAREKEY_WX)) {
                shareUtil.setShareType(0).share();
            } else if (toArray.get(0).equals(SHAREKEY_WXPYQ)) {
                shareUtil.setShareType(1).share();
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_dialog_share);
        initView();
    }

    private void initView() {
        share_qq = (ImageView) findViewById(R.id.find_activity_share_qq);
        share_qq_space = (ImageView) findViewById(R.id.find_activity_share_space);
        share_weixin = (ImageView) findViewById(R.id.find_activity_share_weixin);
        share_pyq = (ImageView) findViewById(R.id.find_activity_share_pyq);
        circle_share_delcet = (ImageView) findViewById(R.id.find_activity_share_delcet);


        share_qq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareUtil.setShareType(2).share();
                dismiss();
            }
        });
        share_weixin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareUtil.setShareType(0).share();
                dismiss();
            }
        });
        share_pyq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareUtil.setShareType(1).share();
                dismiss();
            }
        });
        share_qq_space.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareUtil.setShareType(3).share();
                dismiss();
            }
        });
        circle_share_delcet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        if (toArray == null || toArray.size() == 0) {
            share_qq.setVisibility(View.VISIBLE);
            share_qq_space.setVisibility(View.VISIBLE);
            share_weixin.setVisibility(View.VISIBLE);
            share_pyq.setVisibility(View.VISIBLE);
        } else {
            for (int i = 0; i < toArray.size(); i++) {
                if (toArray.get(i).equals(SHAREKEY_QQ)) {
                    share_qq.setVisibility(View.VISIBLE);
                } else if (toArray.get(i).equals(SHAREKEY_QZONE)) {
                    share_qq_space.setVisibility(View.VISIBLE);
                } else if (toArray.get(i).equals(SHAREKEY_WX)) {
                    share_weixin.setVisibility(View.VISIBLE);
                } else if (toArray.get(i).equals(SHAREKEY_WXPYQ)) {
                    share_pyq.setVisibility(View.VISIBLE);
                }
            }
        }
    }


    @Override
    public void show() {
        if (toArray == null)
            super.show();
        if (toArray != null && toArray.size() != 1)
            super.show();
    }
}
