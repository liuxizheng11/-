package com.rocedar.sdk.shop.app.goods.fragment;

import android.graphics.Color;
import android.net.http.SslError;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rocedar.lib.base.manage.RCBaseFragment;
import com.rocedar.lib.base.unit.RCDrawableUtil;
import com.rocedar.lib.base.unit.RCHandler;
import com.rocedar.lib.base.unit.RCIDCardCheckout;
import com.rocedar.lib.base.unit.RCPhoneNoCheckout;
import com.rocedar.lib.base.unit.RCToast;
import com.rocedar.lib.base.view.RCNoScrollWebView;
import com.rocedar.sdk.shop.R;
import com.rocedar.sdk.shop.app.PayWebViewActivity;
import com.rocedar.sdk.shop.app.goods.adapter.UserChooseGridAdapter;
import com.rocedar.sdk.shop.app.goods.config.GoodsIDs;
import com.rocedar.sdk.shop.app.goods.util.OrderUtil;
import com.rocedar.sdk.shop.config.IRCGoodsConfig;
import com.rocedar.sdk.shop.config.IRCShopGoodsChooseUserListener;
import com.rocedar.sdk.shop.config.IRCShopGoodsPostUserListener;
import com.rocedar.sdk.shop.config.RCShopConfigUtil;
import com.rocedar.sdk.shop.dto.RCServerGoodsInfoDTO;
import com.rocedar.sdk.shop.dto.RCShopChooseUserDTO;
import com.rocedar.sdk.shop.request.IRCServerGoodsRequest;
import com.rocedar.sdk.shop.request.impl.RCServerGoodsImpl;
import com.rocedar.sdk.shop.request.listener.RCGetServerGoodsSkuListener;
import com.rocedar.sdk.shop.request.listener.RCPostOrderListener;

import java.util.List;

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
public class ServerGoodsUserChooseFragment extends RCBaseFragment {

    public static ServerGoodsUserChooseFragment newInstance(int skuId) {
        Bundle args = new Bundle();
        args.putInt("sku_id", skuId);
        ServerGoodsUserChooseFragment fragment = new ServerGoodsUserChooseFragment();
        fragment.setArguments(args);
        return fragment;
    }


    private IRCGoodsConfig goodsConfig;

    private IRCServerGoodsRequest request;

    private int skuId = -1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        skuId = getArguments().getInt("sku_id");
        if (skuId < 0) {
            mActivity.finish();
        }
        View view = inflater.inflate(R.layout.rc_fragment_goods_choose_user, null);
        request = new RCServerGoodsImpl(mActivity);
        goodsConfig = RCShopConfigUtil.getGoodsConfig();
        initView(view);
        return view;
    }

    private GridView userListGridView;
    private EditText phoneNoEditText;
    private RelativeLayout trueNameLayout;
    private EditText trueNameEditText;
    private RelativeLayout isAddLayout;
    private ImageView isAddSelect;
    private RelativeLayout nickNameLayout;
    private EditText nickNameEditText;
    private RelativeLayout idCardLayout;
    private EditText idCardEditText;
    //    private TextView buyNotes;
    private RCNoScrollWebView buyNotes;
    private TextView feeShowTextView;
    private TextView validityTextView;
    private TextView payButton;


    private void initView(View view) {
        userListGridView = view.findViewById(R.id.rc_f_goods_choose_user_gridview);
        phoneNoEditText = view.findViewById(R.id.rc_f_goods_choose_user_phone_input);
        isAddLayout = view.findViewById(R.id.rc_f_goods_choose_user_isadd_layout);
        isAddSelect = view.findViewById(R.id.rc_f_goods_choose_user_isadd_select);
        nickNameLayout = view.findViewById(R.id.rc_f_goods_choose_user_nickname_input_layout);
        nickNameEditText = view.findViewById(R.id.rc_f_goods_choose_user_nickname_input);
        trueNameLayout = view.findViewById(R.id.rc_f_goods_choose_user_truename_input_layout);
        trueNameEditText = view.findViewById(R.id.rc_f_goods_choose_user_truename_input);
        idCardLayout = view.findViewById(R.id.rc_f_goods_choose_user_card_input_layout);
        idCardEditText = view.findViewById(R.id.rc_f_goods_choose_user_card_input);
        buyNotes = view.findViewById(R.id.rc_f_goods_choose_user_notes);
        feeShowTextView = view.findViewById(R.id.rc_f_goods_choose_user_bottom_price);
        validityTextView = view.findViewById(R.id.rc_f_goods_choose_user_bottom_day);
        payButton = view.findViewById(R.id.rc_f_goods_choose_user_bottom_pay);
        payButton.setBackground(RCDrawableUtil.getMainColorDrawableBaseRadius(mActivity));
        phoneNoEditText.setEnabled(false);
        nickNameEditText.setEnabled(false);
        setEditTextListener(phoneNoEditText);
        setEditTextListener(trueNameEditText);
        setEditTextListener(idCardEditText);
        setEditTextListener(nickNameEditText);

        WebSettings settings = buyNotes.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE); //设置缓存
        settings.setDomStorageEnabled(true);//设置适应Html5的一些方法
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setBlockNetworkImage(false);
        buyNotes.setWebViewClient(new WebViewClient() {
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

    }

    private void setEditTextListener(final EditText editText) {
        editText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                editText.setTextColor(Color.parseColor("#666666"));
                editText.setHintTextColor(Color.parseColor("#999999"));
                return false;
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        getGoodsInfo();
    }


    private void getGoodsInfo() {
        mRcHandler.sendMessage(RCHandler.START);
        request.getGoodsSku(new RCGetServerGoodsSkuListener() {
            @Override
            public void getDataSuccess(RCServerGoodsInfoDTO infoDTO) {
                insetDateToView(infoDTO);
                mRcHandler.sendMessage(RCHandler.GETDATA_OK);
            }

            @Override
            public void getDataError(int status, String msg) {
                mRcHandler.sendMessage(RCHandler.GETDATA_OK);
            }
        }, skuId);
    }


    private RCServerGoodsInfoDTO goodsInfoDTO;

    private void insetDateToView(final RCServerGoodsInfoDTO dto) {
        this.goodsInfoDTO = dto;
        if (dto.isSelfUse()) {
            getChooseUserList();
            userListGridView.setVisibility(View.VISIBLE);
        } else {
            userListGridView.setVisibility(View.GONE);
            phoneNoEditText.setEnabled(true);
        }
        switch (dto.getGoodsId()) {
            case GoodsIDs.Famliy_Doctor:
                trueNameLayout.setVisibility(View.GONE);
                idCardLayout.setVisibility(View.GONE);
                nickNameLayout.setVisibility(View.VISIBLE);
                break;
            case GoodsIDs.XIEYI:
                trueNameLayout.setVisibility(View.VISIBLE);
                idCardLayout.setVisibility(View.VISIBLE);
                nickNameLayout.setVisibility(View.GONE);
                break;
            default:
                trueNameLayout.setVisibility(View.GONE);
                idCardLayout.setVisibility(View.GONE);
                nickNameLayout.setVisibility(View.VISIBLE);
                break;
        }
        feeShowTextView.setText(dto.getFeeName());
        validityTextView.setText(dto.getValidityPeriodName());

        if (!dto.getPurchaseNotes().trim().equals("")) {
            buyNotes.loadData(dto.getPurchaseNotes(), "text/html;charset=utf-8", "utf-8");
        } else {

        }

        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (doOrderCheck()) {
                    mRcHandler.sendMessage(RCHandler.START);
                    //如果有添加为家人
                    if (goodsConfig != null && isAddLayout.getVisibility() == View.VISIBLE && isSelect) {
                        goodsConfig.saveUserInfo(mActivity,
                                nickNameEditText.getText().toString().trim(),
                                phoneNoEditText.getText().toString().trim(),
                                idCardEditText.getText().toString().trim(),
                                trueNameEditText.getText().toString().trim(),
                                new IRCShopGoodsPostUserListener() {
                                    @Override
                                    public void getDataSuccess(long userId) {
                                        doPostOrder(userId, nickNameEditText.getText().toString().trim());
                                    }

                                    @Override
                                    public void getDataError(int status, String msg) {
                                        mRcHandler.sendMessage(RCHandler.GETDATA_OK);
                                    }
                                });
                    } else {
                        doPostOrder(-1, "");
                    }
                }
            }
        });
    }


    private UserChooseGridAdapter userChooseGridAdapter;

    private void getChooseUserList() {
        if (goodsConfig != null) {
            goodsConfig.getChooseUserList(mActivity, new IRCShopGoodsChooseUserListener() {

                @Override
                public void getDataSuccess(final List<RCShopChooseUserDTO> dtoList) {
                    if (dtoList.size() == 0) {
                        userListGridView.setVisibility(View.GONE);
                    } else {
                        userListGridView.setVisibility(View.VISIBLE);
                        userChooseGridAdapter = new UserChooseGridAdapter(dtoList, mActivity);
                        userListGridView.setAdapter(userChooseGridAdapter);
                        userListGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                userChooseGridAdapter.mSelectPos = position;
                                changeChooseUser(dtoList.get(position));
                                userChooseGridAdapter.notifyDataSetInvalidated();
                            }
                        });
                        userChooseGridAdapter.mSelectPos = 0;
                        changeChooseUser(dtoList.get(0));
                        userChooseGridAdapter.notifyDataSetInvalidated();
                    }
                }

                @Override
                public void getDataError(int status, String msg) {

                }
            });
        }
    }


    private RCShopChooseUserDTO lastChooseUserDTO = null;

    private boolean isSelect = true;

    private void changeChooseUser(RCShopChooseUserDTO dto) {
        if (lastChooseUserDTO != null && lastChooseUserDTO == dto) {
            return;
        }
        lastChooseUserDTO = dto;
        if (dto.getUserId() == -1) {
            phoneNoEditText.setEnabled(true);
            phoneNoEditText.setText("");
            nickNameLayout.setVisibility(View.VISIBLE);
            nickNameEditText.setEnabled(true);
            nickNameEditText.setText("");
            isAddLayout.setVisibility(View.VISIBLE);
            isAddSelect.setImageResource(R.mipmap.rc_select_default);
            isAddSelect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isSelect = !isSelect;
                    if (isSelect) {
                        isAddSelect.setImageResource(R.mipmap.rc_select_default);
                        nickNameLayout.setVisibility(View.VISIBLE);
                    } else {
                        isAddSelect.setImageResource(R.mipmap.rc_select_no);
                        nickNameLayout.setVisibility(View.GONE);
                    }
                }
            });
            trueNameEditText.setText("");
            idCardEditText.setText("");
        } else {
            isAddLayout.setVisibility(View.GONE);
            nickNameLayout.setVisibility(View.VISIBLE);
            nickNameEditText.setText(dto.getUserNickName());
            if (dto.getUserPhoneNo() > 0) {
                phoneNoEditText.setEnabled(false);
                phoneNoEditText.setText(dto.getUserPhoneNo() + "");
            } else {
                phoneNoEditText.setEnabled(true);
                phoneNoEditText.setText("");
            }
            trueNameEditText.setText(dto.getUserTrueName());
            idCardEditText.setText(dto.getUserCardNo());
        }
        phoneNoEditText.setTextColor(Color.parseColor("#666666"));
        phoneNoEditText.setHintTextColor(Color.parseColor("#999999"));
        nickNameEditText.setHintTextColor(Color.parseColor("#999999"));
        trueNameEditText.setHintTextColor(Color.parseColor("#999999"));
        idCardEditText.setTextColor(Color.parseColor("#666666"));
        idCardEditText.setHintTextColor(Color.parseColor("#999999"));
    }


    private boolean doOrderCheck() {
        boolean temp = true;
        String toastInfo = "";
        if (phoneNoEditText.getText().toString().trim().equals("")) {
            phoneNoEditText.setHintTextColor(Color.RED);
            temp = false;
        } else {
            if (!RCPhoneNoCheckout.isMobileNO(phoneNoEditText.getText().toString().trim())) {
                phoneNoEditText.setTextColor(Color.RED);
                toastInfo = "请输入有效的手机号";
                temp = false;
            }
        }
        if (nickNameLayout.getVisibility() == View.VISIBLE) {
            if (nickNameEditText.getText().toString().trim().equals("")) {
                nickNameEditText.setHintTextColor(Color.RED);
                temp = false;
            }
        }
        if (trueNameLayout.getVisibility() == View.VISIBLE) {
            if (trueNameEditText.getText().toString().trim().equals("")) {
                trueNameEditText.setHintTextColor(Color.RED);
                temp = false;
            }
        }
        if (idCardLayout.getVisibility() == View.VISIBLE) {
            if (idCardEditText.getText().toString().trim().equals("")) {
                idCardEditText.setHintTextColor(Color.RED);
                temp = false;
            } else {
                if (!RCIDCardCheckout.IDCardValidateBoolen(idCardEditText.getText().toString().trim())) {
                    idCardEditText.setTextColor(Color.RED);
                    if (toastInfo.equals(""))
                        toastInfo = "请输入有效的身份证号";
                    else
                        toastInfo = toastInfo + "、身份证号";
                    temp = false;
                }
            }
        }
        payButton.setFocusable(true);
        if (!temp) {
            RCToast.Center(mActivity, (!toastInfo.equals("")) ? toastInfo : "请完善信息");
        }
        return temp;
    }

    /**
     * 提交订单
     */
    private void doPostOrder(long userId, String nickName) {
        String userIdTemp = "-1";
        String userNickName = "";
        if (userId > 0) {
            userIdTemp = userId + "";
            userNickName = nickName;
        } else {
            if (lastChooseUserDTO != null) {
                userIdTemp = lastChooseUserDTO.getUserId() + "";
                userNickName = lastChooseUserDTO.getUserNickName();
            }
        }
        RCPostOrderListener tempListener = new RCPostOrderListener() {
            @Override
            public void getDataSuccess(int orderId) {
                mActivity.finish();
                PayWebViewActivity.goActivity(mActivity, orderId + "");
                mRcHandler.sendMessage(RCHandler.GETDATA_OK);
            }

            @Override
            public void getDataError(int status, String msg) {
                mRcHandler.sendMessage(RCHandler.GETDATA_OK);
                RCToast.Center(mActivity, "下单失败，请重试");
            }
        };
        if (trueNameLayout.getVisibility() == View.VISIBLE && idCardLayout.getVisibility() == View.VISIBLE) {
            OrderUtil.saveOrder(mActivity, mRcHandler, goodsInfoDTO.getTypeId() + "",
                    goodsInfoDTO.getGoodsId() + "", goodsInfoDTO.getSkuId() + "",
                    goodsInfoDTO.getOuterId() + "", phoneNoEditText.getText().toString().trim(),
                    userIdTemp, idCardEditText.getText().toString().trim(),
                    trueNameEditText.getText().toString().trim(), "",
                    tempListener);
        } else {
            OrderUtil.saveOrder(mActivity, mRcHandler, goodsInfoDTO.getTypeId() + "",
                    goodsInfoDTO.getGoodsId() + "", goodsInfoDTO.getSkuId() + "",
                    goodsInfoDTO.getOuterId() + "", phoneNoEditText.getText().toString().trim(),
                    userIdTemp, userNickName, tempListener);
        }
    }


}
