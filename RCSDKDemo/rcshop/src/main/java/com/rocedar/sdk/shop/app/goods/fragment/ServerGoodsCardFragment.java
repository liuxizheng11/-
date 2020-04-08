package com.rocedar.sdk.shop.app.goods.fragment;

import android.graphics.Color;
import android.net.http.SslError;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rocedar.lib.base.manage.RCBaseFragment;
import com.rocedar.lib.base.unit.RCDrawableUtil;
import com.rocedar.lib.base.unit.RCHandler;
import com.rocedar.lib.base.unit.RCImageShow;
import com.rocedar.lib.base.unit.RCJavaUtil;
import com.rocedar.lib.base.unit.RCToast;
import com.rocedar.lib.base.view.RCNoScrollWebView;
import com.rocedar.sdk.shop.R;
import com.rocedar.sdk.shop.app.PayWebViewActivity;
import com.rocedar.sdk.shop.app.goods.ServerGoodsUserChooseActivity;
import com.rocedar.sdk.shop.app.goods.config.GoodsIDs;
import com.rocedar.sdk.shop.app.goods.util.OrderUtil;
import com.rocedar.sdk.shop.config.IRCGoodsConfig;
import com.rocedar.sdk.shop.config.RCShopConfigUtil;
import com.rocedar.sdk.shop.dto.RCServerGoodsInfoDTO;
import com.rocedar.sdk.shop.request.IRCServerGoodsRequest;
import com.rocedar.sdk.shop.request.impl.RCServerGoodsImpl;
import com.rocedar.sdk.shop.request.listener.RCGetServerGoodsInfoListener;
import com.rocedar.sdk.shop.request.listener.RCPostOrderListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名称：瑰柏SDK-商城
 * <p>
 * 作者：phj
 * 日期：2018/9/12 下午2:46
 * 版本：V1.1.00
 * 描述：瑰柏SDK-服务商品详情（卡片页面）
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class ServerGoodsCardFragment extends RCBaseFragment {

    public static ServerGoodsCardFragment newInstance(int goodsId) {
        Bundle args = new Bundle();
        args.putInt("goods_id", goodsId);
        ServerGoodsCardFragment fragment = new ServerGoodsCardFragment();
        fragment.setArguments(args);
        return fragment;
    }


    private List<RCServerGoodsInfoDTO> serverCardGoodsList;


    //商品图
    private ImageView imageView;
    //商品列表
    private LinearLayout goodsListLayout;
    //服务时长
    private LinearLayout serverTimeShowLayout;
    private TextView serverTimeShow;
    //商品价格
    private LinearLayout serverPriceShowLayout;
    private TextView serverPriceShow;
    //商品介绍
//    private TextView serverContext;
//    private RCNoScrollWebView serverContext;
    private LinearLayout serverContextLayout;
    //底部布局一
    private RelativeLayout bottomlayout1;
    private TextView bottomPrice;
    private TextView bottomTemp;
    private TextView bottomDay;
    private TextView bottomPay;
    //底部布局二
    private LinearLayout bottomlayout2;
    private TextView bottomGive;
    private TextView bottomSelf;

    private void initView(View view) {
        imageView = view.findViewById(R.id.rc_f_goods_server_card_image);
        goodsListLayout = view.findViewById(R.id.rc_f_goods_server_card_item);
        serverTimeShowLayout = view.findViewById(R.id.rc_f_goods_server_card_server_time_show_layout);
        serverTimeShow = view.findViewById(R.id.rc_f_goods_server_card_server_time_show);
        serverPriceShowLayout = view.findViewById(R.id.rc_f_goods_server_card_server_price_show_layout);
        serverPriceShow = view.findViewById(R.id.rc_f_goods_server_card_server_price_show);
//        serverContext = view.findViewById(R.id.rc_f_goods_server_card_server_context_show);
        serverContextLayout = view.findViewById(R.id.rc_f_goods_server_card_server_context_show_layout);
        bottomlayout1 = view.findViewById(R.id.rc_f_goods_server_card_bottom_layout_price);
        bottomTemp = view.findViewById(R.id.rc_f_goods_server_card_bottom_temp);
        bottomPrice = view.findViewById(R.id.rc_f_goods_server_card_bottom_price);
        bottomDay = view.findViewById(R.id.rc_f_goods_server_card_bottom_day);
        bottomPay = view.findViewById(R.id.rc_f_goods_server_card_bottom_pay);
        bottomlayout2 = view.findViewById(R.id.rc_f_goods_server_card_bottom_layout_noprice);
        bottomGive = view.findViewById(R.id.rc_f_goods_server_card_bottom_left);
        bottomSelf = view.findViewById(R.id.rc_f_goods_server_card_bottom_right);
        bottomPay.setBackground(RCDrawableUtil.getMainColorDrawableBaseRadius(mActivity));
        bottomGive.setBackground(RCDrawableUtil.getMainColorDrawableBaseRadius(mActivity));
        bottomSelf.setBackground(RCDrawableUtil.getDrawableStroke(mActivity, Color.WHITE, 1f,
                RCDrawableUtil.getThemeAttrColor(mActivity, R.attr.RCDarkColor),
                RCDrawableUtil.getThemeAttrDimension(mActivity, R.attr.RCButtonRadius)));
//        WebSettings settings = serverContext.getSettings();
//        settings.setJavaScriptEnabled(true);
//        settings.setCacheMode(WebSettings.LOAD_NO_CACHE); //设置缓存
//        settings.setDomStorageEnabled(true);//设置适应Html5的一些方法
//        settings.setUseWideViewPort(true);
//        settings.setLoadWithOverviewMode(true);
//        settings.setBlockNetworkImage(false);
//        serverContext.setWebViewClient(new WebViewClient() {
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                view.loadUrl(url);
//                return true;
//            }
//
//            @Override
//            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
//                handler.proceed();
//                super.onReceivedSslError(view, handler, error);
//            }
//        });
    }


    private IRCServerGoodsRequest request;

    private IRCGoodsConfig goodsConfig;

    //商品ID
    private int goodsId = -1;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.rc_fragment_goods_server_card, null);
        request = new RCServerGoodsImpl(mActivity);
        goodsConfig = RCShopConfigUtil.getGoodsConfig();
        initView(view);
        getServerCardData(goodsId = getArguments().getInt("goods_id"));
        if (goodsId < 0) {
            mActivity.finish();
        }
        return view;

    }


    private List<LinearLayout> layouts;

    /**
     * 加载商品信息
     */
    private void insetGoods() {
        if (serverCardGoodsList == null) return;
        goodsListLayout.removeAllViews();
        layouts = new ArrayList<>();
        for (int i = 0; i < serverCardGoodsList.size(); i++) {
            final int tempIndex = i;
            View view = LayoutInflater.from(mActivity).inflate(R.layout.rc_fragment_goods_server_card_item, null);
            LinearLayout linearLayout = view.findViewById(R.id.rc_f_goods_server_card_item_layout);
            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    chooseGoods(tempIndex);
                }
            });
            layouts.add(linearLayout);
            goodsListLayout.addView(view);
        }
    }


    private int mChooseGoodsIndex = -1;

    /**
     * 选择商品切换页面
     *
     * @param chooseIndex
     */
    private void chooseGoods(int chooseIndex) {
        if (layouts == null || layouts.size() < chooseIndex || chooseIndex == mChooseGoodsIndex)
            return;
        mChooseGoodsIndex = chooseIndex;
        //更改选中的选项卡
        for (int i = 0; i < layouts.size(); i++) {
            setGoodsLayoutInfo(layouts.get(i), serverCardGoodsList.get(i), chooseIndex == i);
        }
        //
        final RCServerGoodsInfoDTO dto = serverCardGoodsList.get(chooseIndex);
        if (dto.getValidityPeriod() > 0) {
            serverTimeShow.setText(dto.getValidityPeriodName());
            serverPriceShow.setText(dto.getFeeName());
            serverTimeShowLayout.setVisibility(View.VISIBLE);
            serverPriceShowLayout.setVisibility(View.VISIBLE);
        } else {
            serverTimeShowLayout.setVisibility(View.GONE);
            serverPriceShowLayout.setVisibility(View.GONE);
        }
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
        webView.loadData(dto.getSkuDesc(), "text/html;charset=utf-8", "utf-8");
        serverContextLayout.removeAllViews();
        serverContextLayout.addView(webView);

        //底部选项
        if (dto.isSelfUse() && goodsConfig != null) {
            bottomlayout2.setVisibility(View.VISIBLE);
            bottomlayout1.setVisibility(View.GONE);
            //送亲友
            bottomGive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ServerGoodsUserChooseActivity.goActivity(mActivity, dto.getSkuId(), dto.getSkuName());
                }
            });
            //自用
            bottomSelf.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    doBuy(dto);
                }
            });
        } else {
            bottomlayout1.setVisibility(View.VISIBLE);
            bottomlayout2.setVisibility(View.GONE);
            bottomPrice.setText("¥" + dto.getFeeName());
            if (!dto.getSkuSubTitle().equals("")) {
                bottomTemp.setVisibility(View.VISIBLE);
                bottomDay.setText(dto.getSkuSubTitle());
            } else {
                bottomDay.setText("");
                bottomTemp.setVisibility(View.INVISIBLE);
            }
            bottomPay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    doBuy(dto);
                }
            });
        }
        RCImageShow.loadUrl(dto.getSkuBanner(), imageView, RCImageShow.IMAGE_TYPE_ALBUM, R.mipmap.rc_good_placeholder);
    }

    private void doBuy(RCServerGoodsInfoDTO dto) {
        switch (goodsId) {
            case GoodsIDs.Famliy_Doctor:
                buyGoods(dto);
                break;
            case GoodsIDs.XIEYI:
                ServerGoodsUserChooseActivity.goActivity(mActivity, dto.getSkuId(), dto.getSkuName());
                break;
        }
    }


    private void buyGoods(RCServerGoodsInfoDTO dto) {
        OrderUtil.saveOrder(mActivity, mRcHandler, dto.getTypeId() + "", dto.getGoodsId() + "",
                dto.getSkuId() + "", dto.getOuterId() + "",
                "", "", "", new RCPostOrderListener() {

                    @Override
                    public void getDataSuccess(int orderId) {
                        mActivity.finish();
                        PayWebViewActivity.goActivity(mActivity, orderId + "");
                    }

                    @Override
                    public void getDataError(int status, String msg) {
                        RCToast.Center(mActivity, "下单失败，请重试");
                    }
                });
    }

    /**
     * 加载商品列表
     *
     * @param linearLayout
     * @param dto
     * @param isChoose
     */
    private void setGoodsLayoutInfo(LinearLayout linearLayout, RCServerGoodsInfoDTO dto, boolean isChoose) {
        if (!isAdded()) return;
        TextView textView = linearLayout.findViewById(R.id.rc_f_goods_server_card_item_title);
        if (RCJavaUtil.textHasNumber(dto.getSkuSubTitle())) {
            textView.setText(RCJavaUtil.setNumberCharTextSize(dto.getSkuTitle(),
                    10, false, 15, true));
        } else {
            textView.setText(dto.getSkuTitle());
        }

        TextView textView2 = linearLayout.findViewById(R.id.rc_f_goods_server_card_item_info);
        if (dto.getSkuSubTitle().trim().equals("")) {
            textView2.setVisibility(View.GONE);
        } else {
            textView2.setText(dto.getSkuSubTitle());
        }
        if (isChoose) {
            linearLayout.setBackgroundColor(Color.parseColor("#f5f5f5"));
            textView.setTextColor(RCDrawableUtil.getThemeAttrColor(mActivity, R.attr.RCDarkColor));
            textView2.setTextColor(RCDrawableUtil.getThemeAttrColor(mActivity, R.attr.RCDarkColor));
        } else {
            linearLayout.setBackgroundColor(Color.WHITE);
            textView.setTextColor(Color.parseColor("#b9b9b9"));
            textView2.setTextColor(Color.parseColor("#b9b9b9"));
        }
    }


    private void getServerCardData(int goodsId) {
        mRcHandler.sendMessage(RCHandler.START);
        request.getGoodsInfo(new RCGetServerGoodsInfoListener() {
            @Override
            public void getDataSuccess(List<RCServerGoodsInfoDTO> infoDTO, String goodsName) {
                serverCardGoodsList = infoDTO;
                if (isAdded()) {
                    insetGoods();
                    chooseGoods(0);
                    getRCBaseActivity().mRcHeadUtil.setTitle(goodsName);
                }
                mRcHandler.sendMessage(RCHandler.GETDATA_OK);
            }


            @Override
            public void getDataError(int status, String msg) {
                mRcHandler.sendMessage(RCHandler.GETDATA_OK);
            }
        }, goodsId + "");


    }

}
