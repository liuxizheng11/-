//package com.rocedar.sdk.shop.app.order.adapter;
//
//import android.annotation.SuppressLint;
//import android.app.Activity;
//import android.graphics.Color;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.LinearLayout;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.rocedar.lib.base.network.IRCPostListener;
//import com.rocedar.lib.base.unit.RCAndroid;
//import com.rocedar.lib.base.unit.RCDateUtil;
//import com.rocedar.lib.base.unit.RCDialog;
//import com.rocedar.lib.base.unit.RCDrawableUtil;
//import com.rocedar.lib.base.unit.RCHandler;
//import com.rocedar.lib.base.unit.RCTPJump;
//import com.rocedar.lib.base.unit.RCToast;
//import com.rocedar.sdk.shop.R;
//import com.rocedar.sdk.shop.app.PayWebViewActivity;
//import com.rocedar.sdk.shop.app.order.MyOrderFromRefundActivity;
//import com.rocedar.sdk.shop.app.order.fragment.MyOrderFromListFragment;
//import com.rocedar.sdk.shop.app.order.view.TimeDownView;
//import com.rocedar.sdk.shop.config.RCShopConfigUtil;
//import com.rocedar.sdk.shop.dto.RCOrderFromListDTO;
//import com.rocedar.sdk.shop.request.impl.RCOrderFromImpl;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * 作者：lxz
// * 日期：2018/7/10 下午2:21
// * 版本：V1.0
// * 描述：我的订单列表Adapter
// * <p>
// * CopyRight©北京瑰柏科技有限公司
// */
//public class MyOrderFromListAdapter extends BaseAdapter {
//
//    private Activity mContext;
//    private List<RCOrderFromListDTO> mList = new ArrayList<>();
//    private RCOrderFromImpl rcOrderFrom;
//    private RCDialog rcDialog;
//    private RCHandler handler;
//    private MyOrderFromListFragment fromListFragment;
//
//
//    public MyOrderFromListAdapter(Activity mContext, List<RCOrderFromListDTO> mList, MyOrderFromListFragment fragment) {
//        this.mContext = mContext;
//        this.fromListFragment = fragment;
//        this.mList = mList;
//        rcOrderFrom = RCOrderFromImpl.getInstance(mContext);
//        handler = new RCHandler(mContext);
//    }
//
//    @Override
//    public int getCount() {
//        return mList.size();
//    }
//
//    @Override
//    public Object getItem(int i) {
//        return mList.get(i);
//    }
//
//    @Override
//    public long getItemId(int i) {
//        return i;
//    }
//
//    @Override
//    public View getView(int position, View view, ViewGroup viewGroup) {
//        ViewHolder viewHolder;
//        if (view == null) {
//            view = LayoutInflater.from(mContext).inflate(R.layout.rc_adapter_my_famliy_orderfrom, null);
//            viewHolder = new ViewHolder();
//            viewHolder.rc_adapter_serial_number = view.findViewById(R.id.rc_adapter_serial_number);
//            viewHolder.rc_adapter_serial_status = view.findViewById(R.id.rc_adapter_serial_status);
//            viewHolder.rc_adapter_doctor_name = view.findViewById(R.id.rc_adapter_doctor_name);
//            viewHolder.rc_adapter_hospital_name = view.findViewById(R.id.rc_adapter_hospital_name);
//            viewHolder.rc_adapter_division_name = view.findViewById(R.id.rc_adapter_division_name);
//            viewHolder.rc_adapter_consult = view.findViewById(R.id.rc_adapter_consult);
//            viewHolder.rc_adapter_my_order_expected_time_rl = view.findViewById(R.id.rc_adapter_my_order_expected_time_rl);
//            viewHolder.rc_adapter_my_order_expected_time = view.findViewById(R.id.rc_adapter_my_order_expected_time);
//            viewHolder.rc_adapter_my_order_subscribe_time_rl = view.findViewById(R.id.rc_adapter_my_order_subscribe_time_rl);
//            viewHolder.rc_adapter_my_order_subscribe_time = view.findViewById(R.id.rc_adapter_my_order_subscribe_time);
//            viewHolder.rc_adapter_my_order_money = view.findViewById(R.id.rc_adapter_my_order_money);
//            viewHolder.rc_adapter_my_order_money_name = view.findViewById(R.id.rc_adapter_my_order_money_name);
//            viewHolder.rc_adapter_my_order_pay_ll = view.findViewById(R.id.rc_adapter_my_order_pay_ll);
//            viewHolder.rc_adapter_my_order_refund = view.findViewById(R.id.rc_adapter_my_order_refund);
//            viewHolder.rc_adapter_my_order_cancle_order = view.findViewById(R.id.rc_adapter_my_order_cancle_order);
//            viewHolder.rc_adapter_my_order_historical_data = view.findViewById(R.id.rc_adapter_my_order_historical_data);
//            viewHolder.rc_adapter_my_order_go_pay = view.findViewById(R.id.rc_adapter_my_order_go_pay);
//            viewHolder.rc_adapter_my_order_pay_ok_ll = view.findViewById(R.id.rc_adapter_my_order_pay_ok_ll);
//            viewHolder.rc_adapter_my_order_video = view.findViewById(R.id.rc_adapter_my_order_video);
//            viewHolder.rc_adapter_my_order_examine_particulars = view.findViewById(R.id.rc_adapter_my_order_examine_particulars);
//            viewHolder.rc_adapter_my_order_time_down = view.findViewById(R.id.rc_adapter_my_order_time_down);
//            view.setTag(viewHolder);
//        } else {
//            viewHolder = (ViewHolder) view.getTag();
//        }
//        initView(viewHolder, position);
//        return view;
//    }
//
//    @SuppressLint("NewApi")
//    private void initView(ViewHolder viewHolder, final int position) {
//
//        final RCOrderFromListDTO mDTO = mList.get(position);
//
//        //申请退款
//        viewHolder.rc_adapter_my_order_refund.setBackground(RCDrawableUtil.getDrawableStroke(mContext, Color.WHITE,
//                1, RCDrawableUtil.getThemeAttrColor(mContext, com.rocedar.lib.base.R.attr.RCDarkColor), 1));
//        viewHolder.rc_adapter_my_order_refund.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                MyOrderFromRefundActivity.goActivity(mContext, mList.get(position).getOrder_id(), mList.get(position).getFee_paid());
//            }
//        });
//        //取消订单
//        viewHolder.rc_adapter_my_order_cancle_order.setBackground(RCDrawableUtil.getDrawableStroke(mContext, Color.WHITE,
//                1, RCDrawableUtil.getThemeAttrColor(mContext, com.rocedar.lib.base.R.attr.RCDarkColor), 1));
//        viewHolder.rc_adapter_my_order_cancle_order.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                rcDialog = new RCDialog(mContext, new String[]{null, "确定要关闭订单吗？", "", ""}, null, new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        cancleOrder(position);
//
//                    }
//                });
//                rcDialog.show();
//
//            }
//        });
//        //历史就诊资料
//        viewHolder.rc_adapter_my_order_historical_data.setBackground(RCDrawableUtil.getDrawableStroke(mContext, Color.WHITE,
//                1, RCDrawableUtil.getThemeAttrColor(mContext, R.attr.RCDarkColor), 1));
//        viewHolder.rc_adapter_my_order_historical_data.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                StringBuilder builder = new StringBuilder();
//                builder.append("rctp://android##");
//                builder.append("com.rocedar.sdk.familydoctor.app.RCMingYiCompleteMaterialActivity##");
//                JSONObject object = new JSONObject();
//                try {
//                    object.put("order_id", mDTO.getOrder_id() + "");
//                    object.put("patient_id", mDTO.getPatient_id() + "");
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                builder.append(object.toString());
//                RCTPJump.ActivityJump(mContext, builder.toString());
//            }
//        });
//        //视频咨询
//        viewHolder.rc_adapter_my_order_video.setBackground(RCDrawableUtil.getDrawableStroke(mContext, Color.WHITE,
//                1, RCDrawableUtil.getThemeAttrColor(mContext, R.attr.RCDarkColor), 1));
//
//        viewHolder.rc_adapter_my_order_video.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (Long.parseLong(RCDateUtil.getFormatNow("yyyyMMddHHmmss")) >= mDTO.getService_time()) {
//                    //点击视频咨询，执行咨询操作
//                    if (RCAndroid.isWifiNetWork(mContext)) {
//                        if (RCShopConfigUtil.getConfig() != null) {
//                            RCShopConfigUtil.getConfig().yunXinAdvisory(mContext, mDTO.getOrder_id());
//                        } else {
//                            RCToast.Center(mContext, "不支持该操作");
//                        }
//                    } else {
//                        rcDialog = new RCDialog(mContext, new String[]{null, "当前为非WiFi环境，是否用流量继续视频通话", "放弃", "继续"
//                        }, null, new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                if (RCShopConfigUtil.getConfig() != null) {
//                                    RCShopConfigUtil.getConfig().yunXinAdvisory(mContext, mDTO.getOrder_id());
//                                } else {
//                                    RCToast.Center(mContext, "不支持该操作");
//                                }
//                                rcDialog.dismiss();
//                            }
//                        });
//                        rcDialog.show();
//                    }
//
//                }
//            }
//        });
//
//        //查看详情
//        viewHolder.rc_adapter_my_order_examine_particulars.setBackground(RCDrawableUtil.getDrawableStroke(mContext, Color.WHITE,
//                1, RCDrawableUtil.getThemeAttrColor(mContext, R.attr.RCDarkColor), 1));
//
//        //倒计时
//        viewHolder.rc_adapter_my_order_time_down.setBackground(RCDrawableUtil.getDrawableStroke(mContext, Color.WHITE,
//                1, RCDrawableUtil.getThemeAttrColor(mContext, R.attr.RCDarkColor), 1));
//        //去支付
//        viewHolder.rc_adapter_my_order_go_pay.setBackground(RCDrawableUtil.getDarkMainColorDrawable(mContext, 1));
//        viewHolder.rc_adapter_my_order_go_pay.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                PayWebViewActivity.goActivity(mContext, mDTO.getOrder_id() + "");
//            }
//        });
//
//
//        //订单编号
//        viewHolder.rc_adapter_serial_number.setText("订单编号   " + mDTO.getOrder_id());
//        //订单状态  0: 已取消；1: 待付款；2：已支付，3：已完成；4: 退款中 6:已退款
//        switch (mDTO.getStatus()) {
//            case 0:
//                viewHolder.rc_adapter_serial_status.setText("已取消");
//
//                viewHolder.rc_adapter_my_order_pay_ok_ll.setVisibility(View.VISIBLE);
//                viewHolder.rc_adapter_my_order_pay_ll.setVisibility(View.GONE);
//                //视频咨询、电话咨询
//                viewHolder.rc_adapter_my_order_video.setVisibility(View.GONE);
//                viewHolder.rc_adapter_my_order_time_down.setVisibility(View.GONE);
//
//
//                break;
//            case 1:
//                viewHolder.rc_adapter_serial_status.setText("待付款");
//
//                viewHolder.rc_adapter_my_order_pay_ll.setVisibility(View.VISIBLE);
//                viewHolder.rc_adapter_my_order_pay_ok_ll.setVisibility(View.GONE);
//                //申请退款
//                viewHolder.rc_adapter_my_order_refund.setVisibility(View.GONE);
//                //视频咨询
//                viewHolder.rc_adapter_my_order_time_down.setVisibility(View.GONE);
//                viewHolder.rc_adapter_my_order_video.setVisibility(View.GONE);
//
//                //取消订单
//                viewHolder.rc_adapter_my_order_cancle_order.setVisibility(View.VISIBLE);
//                //去支付
//                viewHolder.rc_adapter_my_order_go_pay.setVisibility(View.VISIBLE);
//                break;
//            case 2:
//                viewHolder.rc_adapter_serial_status.setText("已支付");
//                viewHolder.rc_adapter_my_order_pay_ll.setVisibility(View.VISIBLE);
//                viewHolder.rc_adapter_my_order_pay_ok_ll.setVisibility(View.GONE);
//
//                //申请退款
//                viewHolder.rc_adapter_my_order_refund.setVisibility(View.VISIBLE);
//                //取消订单
//                viewHolder.rc_adapter_my_order_cancle_order.setVisibility(View.GONE);
//                //去支付
//                viewHolder.rc_adapter_my_order_go_pay.setVisibility(View.GONE);
//
//                //1001,电话咨询；1002，视频咨询
//                if (mDTO.getService_type_id() == 1002 && mDTO.getService_time() > 0) {
//                    long[] str = RCDateUtil.getDistanceTimes(mDTO.getService_time());
//                    long[] times = {str[1], str[2], str[3]};
//                    if (Long.parseLong(RCDateUtil.getFormatNow("yyyyMMddHHmmss")) >= mDTO.getService_time()) {
//                        viewHolder.rc_adapter_my_order_video.setVisibility(View.VISIBLE);
//                        viewHolder.rc_adapter_my_order_time_down.setVisibility(View.GONE);
//                        viewHolder.rc_adapter_my_order_video.setText("视频咨询");
//                    } else if (str[0] > 0) {
//                        viewHolder.rc_adapter_my_order_video.setVisibility(View.VISIBLE);
//                        viewHolder.rc_adapter_my_order_video.setText(str[0] + "天后");
//                        viewHolder.rc_adapter_my_order_time_down.setVisibility(View.GONE);
//                    } else {
//                        viewHolder.rc_adapter_my_order_time_down.setVisibility(View.VISIBLE);
//                        viewHolder.rc_adapter_my_order_video.setVisibility(View.GONE);
//                        viewHolder.rc_adapter_my_order_time_down.setTimes(times);
//                        if (!viewHolder.rc_adapter_my_order_time_down.isRun()) {
//                            viewHolder.rc_adapter_my_order_time_down.run();
//                        }
//
//                    }
//
//                } else {
//                    viewHolder.rc_adapter_my_order_video.setVisibility(View.GONE);
//                }
//                break;
//            case 3:
//                viewHolder.rc_adapter_serial_status.setText("已完成");
//
//                viewHolder.rc_adapter_my_order_pay_ok_ll.setVisibility(View.VISIBLE);
//                viewHolder.rc_adapter_my_order_pay_ll.setVisibility(View.GONE);
//
//
//                break;
//            case 4:
//
//                viewHolder.rc_adapter_my_order_pay_ok_ll.setVisibility(View.VISIBLE);
//                viewHolder.rc_adapter_my_order_pay_ll.setVisibility(View.GONE);
//                //视频咨询、电话咨询
//                viewHolder.rc_adapter_my_order_video.setVisibility(View.GONE);
//                viewHolder.rc_adapter_my_order_time_down.setVisibility(View.GONE);
//                viewHolder.rc_adapter_serial_status.setText("退款中");
//                break;
//            case 6:
//
//                viewHolder.rc_adapter_my_order_pay_ok_ll.setVisibility(View.VISIBLE);
//                viewHolder.rc_adapter_my_order_pay_ll.setVisibility(View.GONE);
//                //视频咨询、电话咨询
//                viewHolder.rc_adapter_my_order_video.setVisibility(View.GONE);
//                viewHolder.rc_adapter_my_order_time_down.setVisibility(View.GONE);
//                viewHolder.rc_adapter_serial_status.setText("已退款");
//                break;
//        }
//        //医生名称+ 职称
//        viewHolder.rc_adapter_doctor_name.setText(mDTO.getDoctor_name() + "  " + mDTO.getTitle_name());
//        //医院名称
//        viewHolder.rc_adapter_hospital_name.setText(mDTO.getHospital_name());
//        //科室
//        viewHolder.rc_adapter_division_name.setText(mDTO.getProfession_name());
//        //咨询方式
//        viewHolder.rc_adapter_consult.setText("咨询方式 : " + mDTO.getService_type_name());
//        //期望时间 / 确定预约时间    -1: 删除；0: 已取消；1: 待付款；2：已支付，3：已完成；4: 已退款
//        /**已支付 并且确定预约时间 */
//        if (mDTO.getStatus() == 2 && mDTO.getService_time() > 0) {
//            viewHolder.rc_adapter_my_order_subscribe_time_rl.setVisibility(View.VISIBLE);
//            viewHolder.rc_adapter_my_order_expected_time_rl.setVisibility(View.GONE);
//
//            viewHolder.rc_adapter_my_order_subscribe_time.setText(RCDateUtil.formatTime(mDTO.getService_time() + "",
//                    "yyyy年M月d日 HH:mm"));
//        }
//        /**已完成 */
//        else if (mDTO.getStatus() == 3) {
//            viewHolder.rc_adapter_my_order_subscribe_time_rl.setVisibility(View.VISIBLE);
//            viewHolder.rc_adapter_my_order_expected_time_rl.setVisibility(View.VISIBLE);
//
//            viewHolder.rc_adapter_my_order_subscribe_time.setText(RCDateUtil.formatTime(mDTO.getService_time() + "",
//                    "yyyy年M月d日 HH:mm"));
//            viewHolder.rc_adapter_my_order_expected_time.setText(RCDateUtil.formatTime(mDTO.getReservation_time() + "",
//                    "yyyy年M月d日 HH:mm"));
//        }
//        /** 已取消、待付款、已退款、已支付没有确定预约时间*/
//        else {
//            viewHolder.rc_adapter_my_order_expected_time_rl.setVisibility(View.VISIBLE);
//            viewHolder.rc_adapter_my_order_subscribe_time_rl.setVisibility(View.GONE);
//
//            viewHolder.rc_adapter_my_order_expected_time.setText(RCDateUtil.formatTime(mDTO.getReservation_time() + "",
//                    "yyyy年M月d日 HH:mm"));
//        }
//        //付款
//        viewHolder.rc_adapter_my_order_money.setText("¥" + mDTO.getFee_paid());
//        if (mDTO.getStatus() == 1) {
//            viewHolder.rc_adapter_my_order_money_name.setText("待付 : ");
//        } else {
//            viewHolder.rc_adapter_my_order_money_name.setText("实付 : ");
//        }
//
//
//    }
//
//    /**
//     * 取消订单
//     *
//     * @param position
//     */
//    private void cancleOrder(int position) {
//        handler.sendMessage(RCHandler.START);
//        rcOrderFrom.postOrderFromCancel(mList.get(position).getOrder_id(), new IRCPostListener() {
//            @Override
//            public void getDataSuccess() {
//                handler.sendMessage(RCHandler.GETDATA_OK);
//                fromListFragment.upData();
//                rcDialog.dismiss();
//            }
//
//            @Override
//            public void getDataError(int status, String msg) {
//                handler.sendMessage(RCHandler.GETDATA_OK);
//                fromListFragment.upData();
//                rcDialog.dismiss();
//            }
//        });
//    }
//
//    class ViewHolder {
//        TextView rc_adapter_serial_number;
//        TextView rc_adapter_serial_status;
//        TextView rc_adapter_doctor_name;
//        TextView rc_adapter_hospital_name;
//        TextView rc_adapter_division_name;
//        TextView rc_adapter_consult;
//        RelativeLayout rc_adapter_my_order_expected_time_rl;
//        TextView rc_adapter_my_order_expected_time;
//        RelativeLayout rc_adapter_my_order_subscribe_time_rl;
//        TextView rc_adapter_my_order_subscribe_time;
//        TextView rc_adapter_my_order_money;
//        TextView rc_adapter_my_order_money_name;
//        LinearLayout rc_adapter_my_order_pay_ll;
//        TextView rc_adapter_my_order_refund;
//        TextView rc_adapter_my_order_cancle_order;
//        TextView rc_adapter_my_order_historical_data;
//        TextView rc_adapter_my_order_go_pay;
//        LinearLayout rc_adapter_my_order_pay_ok_ll;
//        TextView rc_adapter_my_order_video;
//        TextView rc_adapter_my_order_examine_particulars;
//        TimeDownView rc_adapter_my_order_time_down;
//    }
//}
