package com.rocedar.sdk.shop.app.goods.fragment;

import android.net.http.SslError;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rocedar.lib.base.manage.RCBaseFragment;
import com.rocedar.lib.base.unit.RCDrawableUtil;
import com.rocedar.lib.base.unit.RCHandler;
import com.rocedar.lib.base.unit.RCToast;
import com.rocedar.lib.base.view.RCNoScrollWebView;
import com.rocedar.sdk.shop.R;
import com.rocedar.sdk.shop.app.PayWebViewActivity;
import com.rocedar.sdk.shop.app.goods.util.OrderUtil;
import com.rocedar.sdk.shop.dto.RCRenewOrderDTO;
import com.rocedar.sdk.shop.request.IRCServerGoodsRequest;
import com.rocedar.sdk.shop.request.impl.RCServerGoodsImpl;
import com.rocedar.sdk.shop.request.listener.RCGetRenewOrderListener;
import com.rocedar.sdk.shop.request.listener.RCPostOrderListener;

/**
 * 项目名称：瑰柏SDK-商城
 * <p>
 * 作者：phj
 * 日期：2018/10/10 下午2:31
 * 版本：V1.1.00
 * 描述：瑰柏SDK-服务商品
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class ServerGoodsRenewFragment extends RCBaseFragment {

    public static ServerGoodsRenewFragment newInstance(int orderId) {
        Bundle args = new Bundle();
        args.putInt("order_id", orderId);
        ServerGoodsRenewFragment fragment = new ServerGoodsRenewFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private IRCServerGoodsRequest request;

    private int orderId = -1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        orderId = getArguments().getInt("order_id");
        if (orderId < 0) {
            mActivity.finish();
        }
        View view = inflater.inflate(R.layout.rc_fragment_goods_renew, null);
        request = new RCServerGoodsImpl(mActivity);
        initView(view);
        return view;
    }


    private TextView phoneNoTextView;
    private TextView trueNameTextView;
    private TextView idCardTextView;
    private TextView goodsNameTextView;
    private TextView serverPeriodTextView;
    private LinearLayout buyNotes;
    private TextView feeShowTextView;
    private TextView validityTextView;
    private TextView payButton;


    private void initView(View view) {
        phoneNoTextView = view.findViewById(R.id.rc_f_goods_renew_phone);
        trueNameTextView = view.findViewById(R.id.rc_f_goods_renew_truename);
        idCardTextView = view.findViewById(R.id.rc_f_goods_renew_card);
        goodsNameTextView = view.findViewById(R.id.rc_f_goods_renew_goods_name);
        serverPeriodTextView = view.findViewById(R.id.rc_f_goods_renew_server_period);
        buyNotes = view.findViewById(R.id.rc_f_goods_renew_notes_layout);
        feeShowTextView = view.findViewById(R.id.rc_f_goods_renew_bottom_price);
        validityTextView = view.findViewById(R.id.rc_f_goods_renew_bottom_day);
        payButton = view.findViewById(R.id.rc_f_goods_renew_bottom_pay);
        payButton.setBackground(RCDrawableUtil.getMainColorDrawableBaseRadius(mActivity));
    }


    @Override
    public void onResume() {
        super.onResume();
        getGoodsInfo();
    }


    private void getGoodsInfo() {
        mRcHandler.sendMessage(RCHandler.START);
        request.getRenewOrder(new RCGetRenewOrderListener() {
            @Override
            public void getDataSuccess(RCRenewOrderDTO infoDTO) {
                insetDateToView(infoDTO);
                mRcHandler.sendMessage(RCHandler.GETDATA_OK);
            }

            @Override
            public void getDataError(int status, String msg) {
                mRcHandler.sendMessage(RCHandler.GETDATA_OK);
            }
        }, orderId);
    }


    private void insetDateToView(final RCRenewOrderDTO dto) {
        if (!isAdded()) return;
        phoneNoTextView.setText(dto.getPhoneNo() + "");
        trueNameTextView.setText(dto.getTrueName() + "");
        idCardTextView.setText(dto.getIdCardNo());
        goodsNameTextView.setText(dto.getSkuName());
        serverPeriodTextView.setText(dto.getServer_time());

        RCNoScrollWebView webView = new RCNoScrollWebView(mActivity);
        webView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE); //设置缓存
        settings.setDomStorageEnabled(true);//设置适应Html5的一些方法
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setBlockNetworkImage(false);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
                super.onReceivedSslError(view, handler, error);
            }
        });
        buyNotes.addView(webView);
        webView.loadData(dto.getPurchaseNotes(), "text/html;charset=utf-8", "utf-8");

        feeShowTextView.setText(dto.getFeeName());
        validityTextView.setText(dto.getValidityPeriodName());

        getRCBaseActivity().mRcHeadUtil.setTitle(dto.getSkuName());
        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doPostOrder(dto);
            }
        });
    }

    /**
     * 提交订单
     */
    private void doPostOrder(RCRenewOrderDTO dto) {
        RCPostOrderListener tempListener = new RCPostOrderListener() {
            @Override
            public void getDataSuccess(int orderId) {
                mActivity.finish();
                PayWebViewActivity.goActivity(mActivity, orderId + "");
            }

            @Override
            public void getDataError(int status, String msg) {
                RCToast.Center(mActivity, "下单失败，请重试");
            }
        };
        if (!dto.getTrueName().equals("") && !dto.getIdCardNo().equals("")) {
            OrderUtil.saveOrder(mActivity, mRcHandler, dto.getTypeId() + "",
                    dto.getGoodsId() + "", dto.getSkuId() + "",
                    dto.getOuterId() + "", dto.getPhoneNo() + "",
                    dto.getRelation_user_id() + "", dto.getIdCardNo(), dto.getTrueName(),
                    "", tempListener);
        } else {
            OrderUtil.saveOrder(mActivity, mRcHandler, dto.getTypeId() + "",
                    dto.getGoodsId() + "", dto.getSkuId() + "",
                    dto.getOuterId() + "", dto.getPhoneNo() + "",
                    dto.getRelation_user_id() + "", "", tempListener);
        }
    }

}
