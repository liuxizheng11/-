package com.rocedar.deviceplatform.app.binding.sn;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.rocedar.base.RCBaseConfig;
import com.rocedar.base.RCHandler;
import com.rocedar.base.RCImageShow;
import com.rocedar.base.RCTPJump;
import com.rocedar.base.RCToast;
import com.rocedar.base.manger.RCBaseActivity;
import com.rocedar.base.permission.AcpListener;
import com.rocedar.base.permission.RCPermissionUtil;
import com.rocedar.base.scanner.CaptureActivity;
import com.rocedar.base.scanner.CodeUtils;
import com.rocedar.deviceplatform.R;
import com.rocedar.deviceplatform.app.binding.DeviceBindStatusDialog;
import com.rocedar.deviceplatform.app.binding.sn.adapter.EchoDataAdapter;
import com.rocedar.deviceplatform.app.binding.sn.adapter.SnNumberDongYaUtil;
import com.rocedar.deviceplatform.app.binding.sn.adapter.SnNumberGridViewAdapter;
import com.rocedar.deviceplatform.app.view.MyGridView;
import com.rocedar.deviceplatform.dto.data.RCDeviceFamilyRelationDTO;
import com.rocedar.deviceplatform.dto.data.RCDeviceSnDetailsDTO;
import com.rocedar.deviceplatform.request.impl.RCDeviceOperationRequestImpl;
import com.rocedar.deviceplatform.request.listener.RCDeviceDataDetailsListener;
import com.rocedar.deviceplatform.request.listener.RCDeviceSNDetailsListener;
import com.rocedar.deviceplatform.unit.RCPhoneContactUtil;
import com.rocedar.deviceplatform.unit.RCPhoneNumberCheckout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lxz on 17/2/10.
 * <p>
 * SN码页面
 */

public class RCSnNumberActivity extends RCBaseActivity {

    private ImageView sn_main_iv;
    private EditText et_input_sn;
    private ImageView sn_main_scan;
    private TextView tv_lxpressure_bind;

    private ScrollView scrollView;

    private RCDeviceOperationRequestImpl operationRequest;

    /**
     * 选择家人
     */
    private LinearLayout sn_list;
    /**
     * 选择键位
     */
    private LinearLayout include_sn_grid_ll;
    private MyGridView sn_grid;
    private SnNumberGridViewAdapter myGridAdapter;
    /**
     * 数据回显
     */
    private ListView data_echo_listview;
    private List<RCDeviceSnDetailsDTO.RelationsBean> data_echo_list = new ArrayList<>();
    private EchoDataAdapter echoDataAdapter;

    /**
     * 模型ID
     */
    private int mode_id;
    /**
     * device_id
     */
    private int device_id;

    /**
     * 健康立方键位选择 List
     */
    private List<Integer> mSelectList = new ArrayList<>();


    private List<RCDeviceSnDetailsDTO.RolesBean> detauls_roles = new ArrayList<>();

    /**
     * 家人列表数据
     * 1 列表possion  2 家人info
     */
    private List<RCDeviceFamilyRelationDTO> familyChooseList = new ArrayList<>();
    /**
     * 健康立方单角色 绑定多设备 ID
     */
    public static final int Family_ID = 999;
    /**
     * Wifi 配置 URL
     */
    private String wifi_url = "";

    private ImageView sn_binding_botoom_tv;
    private RelativeLayout rl_sn_go_shopping;

    public static void gotoActivity(Context context, int device_id) {
        Intent intent = new Intent(context, RCSnNumberActivity.class);
        intent.putExtra("device_id", device_id);
        context.startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        二维码扫描
        if (requestCode == 1000) {
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    String result = bundle.getString(CodeUtils.RESULT_STRING);
                    et_input_sn.setText(result);
                    /** 扫描成功－协议解析*/
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    RCToast.Center(this, "解析二维码失败", false);
                }
            }
        } else if (requestCode == 0) {
            if (resultCode == Activity.RESULT_OK) {
                Uri contactData = data.getData();
                Cursor c = managedQuery(contactData, null, null, null, null);
                c.moveToFirst();
                String username = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                String phoneNum = RCPhoneContactUtil.getContactPhone(mContext, c);
                if (choosePhoneFromContactsListener != null) {
                    choosePhoneFromContactsListener.getDataOk(username, phoneNum.replace(" ", "").replace("+86", ""));
                }
            }
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sn_number_main);
        if (!getIntent().hasExtra("device_id")) {
            finishActivity();
        }
        mRcHeadUtil.setLeftBack();
        device_id = getIntent().getIntExtra("device_id", -1);
        operationRequest = RCDeviceOperationRequestImpl.getInstance(mContext);
        initView();
        getData();
    }


    private void initView() {
        sn_main_iv = (ImageView) findViewById(R.id.sn_main_iv);
        et_input_sn = (EditText) findViewById(R.id.et_input_sn);
        sn_main_scan = (ImageView) findViewById(R.id.sn_main_scan);
        tv_lxpressure_bind = (TextView) findViewById(R.id.tv_lxpressure_bind);
        sn_binding_botoom_tv = (ImageView) findViewById(R.id.rcdevice_sn_binding_botoom_iv);
        scrollView = (ScrollView) findViewById(R.id.sn_scrollview);

        sn_list = (LinearLayout) findViewById(R.id.sn_list);

        include_sn_grid_ll = (LinearLayout) findViewById(R.id.include_sn_grid_ll);
        sn_grid = (MyGridView) findViewById(R.id.sn_grid);

        data_echo_listview = (ListView) findViewById(R.id.data_echo_listview);
        rl_sn_go_shopping = (RelativeLayout) findViewById(R.id.rl_sn_go_shopping);

        if (RCBaseConfig.APPTAG == RCBaseConfig.APPTAG_DONGYA) {
            rl_sn_go_shopping.setVisibility(View.VISIBLE);
        } else {
            rl_sn_go_shopping.setVisibility(View.GONE);
        }
        /**
         * 提醒去商城购买
         */
        findViewById(R.id.rl_sn_go_shopping).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 2017/7/5 去商城的协议
                RCTPJump.ActivityJump(mContext, "rctp://android##com.rocedar.app.home.ShopShowActivity##{}");
            }
        });
        /**
         * 扫一扫
         */
        sn_main_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RCPermissionUtil.getPremission(mContext, new AcpListener() {
                    @Override
                    public void onGranted() {
                        Intent intent = new Intent(mContext, CaptureActivity.class);
                        startActivityForResult(intent, 1000);
                    }

                    @Override
                    public void onDenied(List<String> permissions) {
                        RCToast.Center(mContext, "权限拒绝，请在设置中开启相机权限", false);
                    }
                }, Manifest.permission.CAMERA);
            }
        });

        /**
         * 绑定
         */

        tv_lxpressure_bind.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (et_input_sn.getText().toString().trim().equals("")) {
                            RCToast.Center(mContext,
                                    mContext.getString(R.string.rcdevice_device_bind_sn_null), false);
                            return;
                        }
                        mRcHandler.sendMessage(RCHandler.START);
                        /**根据类型 绑定 */
                        switch (mode_id) {
                            /**    * 1 自用单角色,(立方糖护士血糖)*/
                            case RCDeviceSnDetailsDTO.SINGLE_SINGLE_ROLE:
                                operationRequest.doSNBinding(new RCDeviceDataDetailsListener() {
                                    @Override
                                    public void getDataSuccess(JSONObject data) {
                                        mRcHandler.sendMessage(RCHandler.GETDATA_OK);
                                        RCToast.Center(mContext, mContext.getString(R.string.rcdevice_sn_binding_success));
                                        startWifi();
                                    }

                                    @Override
                                    public void getDataError(int status, String msg) {
                                        mRcHandler.sendMessage(RCHandler.GETDATA_OK);
                                        RCToast.Center(mContext, msg);
                                    }
                                }, device_id, et_input_sn.getText().toString().trim(), "-1");

                                break;
                            /**
                             * 2 自用多角色,(立方乐心血压)
                             */
                            case RCDeviceSnDetailsDTO.SINGLE_MORE_ROLE:
                                if (mSelectList.size() > 0) {
                                    try {
                                        JSONArray array = new JSONArray();
                                        JSONObject obj = new JSONObject();
                                        obj.put("relation_name", "");
                                        obj.put("role_id", detauls_roles.get(mSelectList.get(mSelectList.size() - 1)).getRole_id());
                                        obj.put("phone", "-1");
                                        array.put(obj);
                                        operationRequest.doSNBinding(new RCDeviceDataDetailsListener() {
                                            @Override
                                            public void getDataSuccess(JSONObject data) {
                                                mRcHandler.sendMessage(RCHandler.GETDATA_OK);
                                                RCToast.Center(mContext, mContext.getString(R.string.rcdevice_sn_binding_success));
                                                startWifi();
                                            }

                                            @Override
                                            public void getDataError(int status, String msg) {
                                                mRcHandler.sendMessage(RCHandler.GETDATA_OK);
                                                RCToast.Center(mContext, msg);
                                            }
                                        }, device_id, et_input_sn.getText().toString().trim(), array.toString());
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    //没有选择角色
                                    RCToast.Center(mContext, mContext.getString(R.string.rcdevice_device_bind_jianwei_null), false);
                                }
                                break;
                            /**
                             * 3 家人单角色, (动吖糖护士血糖)
                             */
                            /**
                             * 4 家人多角色(动吖乐心血压)
                             */
                            case RCDeviceSnDetailsDTO.FAMILY_SINGLE_ROLE:
                            case RCDeviceSnDetailsDTO.FAMILY_MORE_ROLE:
                                familyBinding();
                                break;

                        }

                    }
                }

        );
    }


    /**
     * 获取SN详情数据
     */

    private void getData() {
        mRcHandler.sendMessage(RCHandler.START);
        operationRequest.getSNDeviceDetails(new RCDeviceSNDetailsListener() {
            @Override
            public void getDataSuccess(RCDeviceSnDetailsDTO dto) {
                mRcHeadUtil.setTitle(dto.getDisplay_name());
                mRcHandler.sendMessage(RCHandler.GETDATA_OK);
                mode_id = dto.getConfig_type();
                /**设备图片*/
//                GlideUtil.loadImage(mContext, dto.getDevice_img(), sn_main_iv, GlideUtil.IMAGE_TYPE_ORIGINAL);
                RCImageShow.loadUrl(dto.getDevice_img(),
                        sn_main_iv, RCImageShow.IMAGE_TYPE_ALBUM);
                /** 是否支持扫描(1 支持，0不支持)*/
                if (dto.getScan() == 0) {
                    sn_main_scan.setVisibility(View.GONE);
                } else {
                    sn_main_scan.setVisibility(View.VISIBLE);
                }
                /**wifi 配置url*/
                wifi_url = dto.getWifi_url();
                /**数据回显 */
                data_echo_list = dto.getRelations();
                echoDataAdapter = new EchoDataAdapter(RCSnNumberActivity.this, data_echo_list);
                data_echo_listview.setAdapter(echoDataAdapter);
                echoDataAdapter.notifyDataSetChanged();

                if (dto.getConfig_type() == RCDeviceSnDetailsDTO.SINGLE_SINGLE_ROLE
                        || dto.getConfig_type() == RCDeviceSnDetailsDTO.FAMILY_SINGLE_ROLE) {
                    sn_binding_botoom_tv.setVisibility(View.GONE);
                } else {
                    sn_binding_botoom_tv.setVisibility(View.VISIBLE);
                }

                /**根据模型ID显示UI*/
                switch (dto.getConfig_type()) {
                    /**
                     * 1 自用单角色,(立方糖护士血糖)
                     * 2 自用多角色,(立方乐心血压)
                     * 3 家人单角色, (动吖糖护士血糖)
                     * 4 家人多角色(动吖乐心血压)
                     */
                    case RCDeviceSnDetailsDTO.SINGLE_MORE_ROLE:
                        if (dto.getRoles().size() > 0) {
                            include_sn_grid_ll.setVisibility(View.VISIBLE);
                            detauls_roles = dto.getRoles();
                            myGridAdapter = new SnNumberGridViewAdapter(mContext, detauls_roles, mSelectList);
                            sn_grid.setSelector(new ColorDrawable(Color.TRANSPARENT));
                            sn_grid.setAdapter(myGridAdapter);
                            myGridAdapter.notifyDataSetChanged();
                        }
                        break;

                    case RCDeviceSnDetailsDTO.FAMILY_SINGLE_ROLE:
                        sn_list.setVisibility(View.VISIBLE);
                        RCDeviceSnDetailsDTO.RolesBean rolesBean = new RCDeviceSnDetailsDTO.RolesBean();
                        rolesBean.setRole_id(-1);
                        rolesBean.setRole_img("");
                        detauls_roles = new ArrayList<>();
                        detauls_roles.add(rolesBean);
                        addChooseFamilyList();
                        break;
                    case RCDeviceSnDetailsDTO.FAMILY_MORE_ROLE:
                        if (dto.getRoles().size() > 0) {
                            sn_list.setVisibility(View.VISIBLE);
                            detauls_roles = dto.getRoles();
                            addChooseFamilyList();
                        }
                        break;
                }

            }

            @Override
            public void getDataError(int status, String msg) {
                mRcHandler.sendMessage(RCHandler.GETDATA_OK);
            }
        }, device_id);
    }

    private SnNumberDongYaUtil myListViewAdapter;

    /**
     * 添加家人选择列表（动吖，多角色场景）
     */
    private void addChooseFamilyList() {
        sn_list.removeAllViews();
        if (detauls_roles == null || detauls_roles.size() == 0) return;
        myListViewAdapter = new SnNumberDongYaUtil(this, detauls_roles, familyChooseList, scrollView);
        for (int i = 0; i < detauls_roles.size(); i++) {
            sn_list.addView(myListViewAdapter.getView(i));
        }

    }


    /**
     * 家人多人绑定设备
     */
    public void familyBinding() {
        boolean hasChoose = false;
        for (int i = 0; i < familyChooseList.size(); i++) {
            if (familyChooseList.get(i) != null) {
                hasChoose = true;
            }
        }
        if (!hasChoose) {
            mRcHandler.sendMessage(RCHandler.GETDATA_OK);
            RCToast.Center(mContext, mContext.getString(R.string.rcdevice_device_bind_least_user));
            return;
        }
        try {
            String temp = "";
            String tempName = "";
            JSONArray array = new JSONArray();
            for (int i = 0; i < familyChooseList.size(); i++) {
                if (familyChooseList.get(i) != null) {
                    if (tempName.contains(familyChooseList.get(i).getRelation_name())) {
                        mRcHandler.sendMessage(RCHandler.GETDATA_OK);
                        RCToast.Center(mContext, getString(R.string.rcdevice_device_bind_name_small));
                        return;
                    }
                    tempName += familyChooseList.get(i).getRelation_name() + ",";
                    if (!familyChooseList.get(i).getRelation_name().equals(SnNumberDongYaUtil.MyNameString) &&
                            !RCPhoneNumberCheckout.isMobileNO(familyChooseList.get(i).getPhoneNumber())) {
                        mRcHandler.sendMessage(RCHandler.GETDATA_OK);
                        RCToast.Center(mContext, String.format(
                                mContext.getString(R.string.rcdevice_device_bind_phone_error),
                                familyChooseList.get(i).getRelation_name()));
                        return;
                    }
                    if (temp.contains(familyChooseList.get(i).getPhoneNumber())) {
                        mRcHandler.sendMessage(RCHandler.GETDATA_OK);
                        RCToast.Center(mContext, getString(R.string.rcdevice_device_bind_phone_small));
                        return;
                    }
                    temp += familyChooseList.get(i).getPhoneNumber() + ",";
                    //[{"role_id":1,"phone":13800138000,"relation_name":"爸爸"}]
                    JSONObject obj = new JSONObject();
                    obj.put("relation_name", familyChooseList.get(i).getRelation_name());
                    obj.put("role_id", detauls_roles.get(i).getRole_id());
                    obj.put("phone", familyChooseList.get(i).getPhoneNumber());
                    array.put(obj);
                }
            }
//            RCToast.TestCenter(mContext, array.toString());
            operationRequest.doSNBinding(new RCDeviceDataDetailsListener() {
                @Override
                public void getDataSuccess(JSONObject data) {
                    mRcHandler.sendMessage(RCHandler.GETDATA_OK);

                    if (data.has("result")) {
                        //弹框
                        new DeviceBindStatusDialog(mContext, data.optJSONObject("result")).show();
                    } else {
                        RCToast.Center(mContext, mContext.getString(R.string.rcdevice_sn_binding_success));
                        startWifi();
                    }
                }

                @Override
                public void getDataError(int status, String msg) {
                    mRcHandler.sendMessage(RCHandler.GETDATA_OK);
                }
            }, device_id, et_input_sn.getText().toString().trim(), array.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /**
     * 数据回显点击
     */
    public void echoData(int possion) {
        et_input_sn.setText(data_echo_list.get(possion).getDevice_no());
        switch (mode_id) {
            /**
             * 1 自用单角色,(立方糖护士血糖)
             * 2 自用多角色,(立方乐心血压)
             * 3 家人单角色, (动吖糖护士血糖)
             * 4 家人多角色(动吖乐心血压)
             */
            case RCDeviceSnDetailsDTO.SINGLE_MORE_ROLE:
                mSelectList.clear();
                for (int i = 0; i < detauls_roles.size(); i++) {
                    if (detauls_roles.get(i).getRole_id() == data_echo_list.get(possion).getDevice_role_id()) {
                        mSelectList.add(data_echo_list.get(possion).getDevice_role_id() - 1);
                        myGridAdapter.notifyDataSetChanged();
                    }
                }
                break;

            case RCDeviceSnDetailsDTO.FAMILY_SINGLE_ROLE:
                if (myListViewAdapter != null) {
                    myListViewAdapter.reShowinfo(0,
                            new RCDeviceFamilyRelationDTO(data_echo_list.get(possion).getRelation_name(), data_echo_list.get(possion).getPhoneNumber()));
                }
                break;
            case RCDeviceSnDetailsDTO.FAMILY_MORE_ROLE:
                for (int i = 0; i < detauls_roles.size(); i++) {
                    if (detauls_roles.get(i).getRole_id() == data_echo_list.get(possion).getDevice_role_id()) {
                        if (myListViewAdapter != null) {
                            myListViewAdapter.reShowinfo(i,
                                    new RCDeviceFamilyRelationDTO(
                                            data_echo_list.get(possion).getRelation_name(), data_echo_list.get(possion).getPhoneNumber()));
                        }
                        return;
                    }
                }
                break;
        }
    }


    /**
     * 绑定成功后是否跳转WiFi配置
     */
    private void startWifi() {
        if (!wifi_url.equals("")) {
            RCTPJump.ActivityJump(mContext, wifi_url);
            finishActivity();
        } else {
            finishActivity();
        }
    }

    private ChoosePhoneFromContactsListener choosePhoneFromContactsListener;

    public void gotoChoosePhoneFromContacts(ChoosePhoneFromContactsListener choosePhoneFromContactsListener) {
        this.choosePhoneFromContactsListener = choosePhoneFromContactsListener;
        RCPermissionUtil.getPremission(mContext, new AcpListener() {
            @Override
            public void onGranted() {
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent, 0);
            }

            @Override
            public void onDenied(List<String> permissions) {
                RCToast.Center(mContext, "权限拒绝，请在设置中开启权限", false);
            }
        }, Manifest.permission.READ_CONTACTS);
    }

    public interface ChoosePhoneFromContactsListener {

        void getDataOk(String userName, String phone);

    }

}
