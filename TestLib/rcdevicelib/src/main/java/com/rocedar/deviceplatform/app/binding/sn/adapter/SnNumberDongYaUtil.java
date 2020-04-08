package com.rocedar.deviceplatform.app.binding.sn.adapter;

import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.rocedar.base.RCImageShow;
import com.rocedar.base.RCToast;
import com.rocedar.deviceplatform.R;
import com.rocedar.deviceplatform.app.binding.sn.RCSnNumberActivity;
import com.rocedar.deviceplatform.app.binding.sn.SNChooseRelationDialog;
import com.rocedar.deviceplatform.dto.data.RCDeviceFamilyRelationDTO;
import com.rocedar.deviceplatform.dto.data.RCDeviceSnDetailsDTO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by lxz on 17/2/11.
 * <p>
 * <p>
 * by phj on 17/4/25修改为
 * SN设备绑定动吖场景工具类
 */

public class SnNumberDongYaUtil {

    private RCSnNumberActivity mContext;
    //角色数据（定制后）
    private List<RCDeviceSnDetailsDTO.RolesBean> mList;
    //家人选择器
    private SNChooseRelationDialog childUserChooseDialog;
    //家人选择列表
    private List<RCDeviceFamilyRelationDTO> name_list;

    private ScrollView scrollView;

    private Handler handler;

    public final static String MyNameString = "我";

    /**
     * @param mContext
     * @param mList    角色列表（定制前的数据）
     * @param map_list 家人列表
     */
    public SnNumberDongYaUtil(RCSnNumberActivity mContext, List<RCDeviceSnDetailsDTO.RolesBean> mList
            , List<RCDeviceFamilyRelationDTO> map_list, ScrollView scrollView) {
        this.mContext = mContext;
        this.mList = mList;
        this.scrollView = scrollView;
        handler = new Handler();
//        for (int i = 0; i < mList.size(); i++) {
//            SnNumberDTO sndto = new SnNumberDTO();
//            sndto.rolesBean = mList.get(i);
//            this.mList.add(sndto);
//        }
        this.name_list = map_list;
        name_list.clear();
        for (int i = 0; i < mList.size(); i++) {
            name_list.add(null);
        }
        childUserChooseDialog = new SNChooseRelationDialog(mContext);
    }


    /*view列表对象*/
    private Map<Integer, ViewHolder> viewHolderMap = new HashMap<>();

    public View getView(final int position) {
        View convertView = LayoutInflater.from(mContext).inflate(R.layout.include_sn_list_item, null);
        ViewHolder viewHolder = new ViewHolder();
        viewHolder.imageView = (ImageView) convertView.findViewById(R.id.sn_list_iv);
        viewHolder.textView = (TextView) convertView.findViewById(R.id.sn_list_tv);
        viewHolder.chooseUserLayout = (RelativeLayout) convertView.findViewById(R.id.include_sn_list_item_choose_user_layout);
        viewHolder.choosePhoneLayout = (LinearLayout) convertView.findViewById(R.id.include_sn_list_item_choose_phone_layout);
        viewHolder.editText_username = (EditText) convertView.findViewById(R.id.include_sn_list_item_choose_user_title_et);
        viewHolder.editText_phone = (EditText) convertView.findViewById(R.id.include_sn_list_item_choose_user_phone_et);
        viewHolder.btn_choosephone_username = (TextView) convertView.findViewById(R.id.include_sn_list_item_choose_user_title_choose);
        viewHolder.btn_choosephone_phone = (TextView) convertView.findViewById(R.id.include_sn_list_item_choose_user_phone_choose);
        viewHolderMap.put(position, viewHolder);
        if (mList.get(position).getRole_img().equals("")) {
            RCImageShow.loadUrl("/d/s/1000000_1.png",
                    viewHolderMap.get(position).imageView, RCImageShow.IMAGE_TYPE_HEAD);
        } else {
            RCImageShow.loadUrl(mList.get(position).getRole_img(),
                    viewHolderMap.get(position).imageView, RCImageShow.IMAGE_TYPE_HEAD);
        }
        viewHolder.editText_username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().trim().equals(MyNameString)) {
                    viewHolderMap.get(position).choosePhoneLayout.setVisibility(View.GONE);
                    viewHolderMap.get(position).btn_choosephone_username.setVisibility(View.GONE);
                } else {
                    if (viewHolderMap.get(position).choosePhoneLayout.getVisibility() != View.VISIBLE) {
                        viewHolderMap.get(position).choosePhoneLayout.setVisibility(View.VISIBLE);
                        viewHolderMap.get(position).btn_choosephone_username.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
        viewHolderMap.get(position).textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                childUserChooseDialog.setOnChooseListener(new SNChooseRelationDialog.OnChooseListener() {
                    @Override
                    public void save(RCDeviceFamilyRelationDTO dto) {
                        childUserChooseDialog.dismiss();
                        if (dto.getRelation_name().trim().equals("请选择")) {
                            doChooseUser(position, null);
                            return;
                        }
                        if (!nameHave(dto, position)) {
                            doChooseUser(position, dto);
                        } else {
                            RCToast.Center(mContext, mContext.getString(R.string.rcdevice_device_bind_user_repeat), false);
                            return;
                        }

                    }
                });
                childUserChooseDialog.show();
            }
        });
        doChooseUser(position, (name_list.get(position)));
        return convertView;
    }

    /**
     * 回显数据
     */
    public void reShowinfo(int position, RCDeviceFamilyRelationDTO dto) {
        if (name_list.size() > position) {
            doChooseUser(position, dto);
        }
    }

    private void doChooseUser(final int position, RCDeviceFamilyRelationDTO dto) {
        if (dto == null) {
            viewHolderMap.get(position).textView.setText("");
            viewHolderMap.get(position).editText_phone.setText("");
            viewHolderMap.get(position).editText_username.setText("");
            viewHolderMap.get(position).btn_choosephone_phone.setVisibility(View.GONE);
            viewHolderMap.get(position).btn_choosephone_username.setVisibility(View.GONE);
            viewHolderMap.get(position).chooseUserLayout.setVisibility(View.GONE);
            viewHolderMap.get(position).choosePhoneLayout.setVisibility(View.GONE);
            name_list.remove(position);
            name_list.add(position, null);
            return;
        }
        if (dto.getRelation_name().equals(MyNameString)) {
            viewHolderMap.get(position).textView.setText(dto.getRelation_name());
            viewHolderMap.get(position).editText_phone.setText("");
            viewHolderMap.get(position).editText_username.setText("");
            viewHolderMap.get(position).btn_choosephone_phone.setVisibility(View.GONE);
            viewHolderMap.get(position).btn_choosephone_username.setVisibility(View.GONE);
            viewHolderMap.get(position).chooseUserLayout.setVisibility(View.GONE);
            viewHolderMap.get(position).choosePhoneLayout.setVisibility(View.GONE);
            dto.setPhoneNumber("-1");
        } else {
            viewHolderMap.get(position).textView.setText(dto.getRelation_name());
            if (dto.getRelation_name().equals("其他")) {
                viewHolderMap.get(position).chooseUserLayout.setVisibility(View.VISIBLE);
                viewHolderMap.get(position).choosePhoneLayout.setVisibility(View.VISIBLE);
                viewHolderMap.get(position).btn_choosephone_username.setVisibility(View.VISIBLE);
                viewHolderMap.get(position).btn_choosephone_username.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mContext.gotoChoosePhoneFromContacts(new RCSnNumberActivity.ChoosePhoneFromContactsListener() {
                            @Override
                            public void getDataOk(String userName, String phone) {
                                viewHolderMap.get(position).editText_username.setText(userName);
                                viewHolderMap.get(position).editText_phone.setText(phone);
                            }
                        });
                    }
                });
                viewHolderMap.get(position).btn_choosephone_phone.setVisibility(View.GONE);
                viewHolderMap.get(position).editText_username.setText("");
                viewHolderMap.get(position).editText_username.addTextChangedListener(new TextWatcher() {
//                    private int selectionStart;
//                    private int selectionEnd;

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
//                        selectionStart = viewHolderMap.get(position).editText_username.getSelectionStart();
//                        selectionEnd = viewHolderMap.get(position).editText_username.getSelectionEnd();
                        if (getTextChatLength(s.toString()) > 12) {
                            s.delete(s.length() - 1, s.length());
                            int tempSelection = s.length();
                            viewHolderMap.get(position).editText_username.setText(s);
                            viewHolderMap.get(position).editText_username.setSelection(tempSelection);
                        }
                        if (name_list.get(position) != null) {
                            name_list.get(position).setRelation_name(viewHolderMap.get(position).editText_username.getText()
                                    .toString().trim());
                        }
                    }
                });
            } else {
                viewHolderMap.get(position).chooseUserLayout.setVisibility(View.GONE);
                viewHolderMap.get(position).choosePhoneLayout.setVisibility(View.VISIBLE);
                viewHolderMap.get(position).btn_choosephone_username.setVisibility(View.GONE);
                viewHolderMap.get(position).btn_choosephone_phone.setVisibility(View.VISIBLE);
                viewHolderMap.get(position).btn_choosephone_phone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mContext.gotoChoosePhoneFromContacts(new RCSnNumberActivity.ChoosePhoneFromContactsListener() {
                            @Override
                            public void getDataOk(String userName, String phone) {
                                viewHolderMap.get(position).editText_phone.setText(phone);
                            }
                        });
                    }
                });
            }
            viewHolderMap.get(position).editText_phone.setText(dto.getPhoneNumber().equals("-1") ? ""
                    : dto.getPhoneNumber());
            viewHolderMap.get(position).editText_phone.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (name_list.get(position) != null) {
                        name_list.get(position).setPhoneNumber(viewHolderMap.get(position).editText_phone.getText()
                                .toString().trim());
                    }
                }
            });
        }
        name_list.remove(position);
        RCDeviceFamilyRelationDTO dtoinfo = new RCDeviceFamilyRelationDTO(
                dto.getRelation_name(), dto.getPhoneNumber()
        );
        name_list.add(position, dtoinfo);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        }, 500);
    }


    class ViewHolder {
        ImageView imageView;
        TextView textView;
        TextView btn_choosephone_username;
        TextView btn_choosephone_phone;
        EditText editText_username;
        EditText editText_phone;
        RelativeLayout chooseUserLayout;
        LinearLayout choosePhoneLayout;
    }

    /**
     * 是否已经选择该项
     *
     * @param dto     选择的数据
     * @param possion 当前选择的角色索引
     * @return
     */
    private boolean nameHave(RCDeviceFamilyRelationDTO dto, int possion) {
        boolean have = false;
        if (dto.getRelation_name().equals("其他")) return have;
        for (int i = 0; i < name_list.size(); i++) {
            if (i != possion && name_list.get(i) != null) {
                if (dto.getRelation_name().equals(name_list.get(i).getRelation_name())) {
                    have = true;
                }
            }
        }
        return have;
    }

    /**
     * 计算文字字符长度
     *
     * @param text
     * @return
     */
    public static int getTextChatLength(String text) {
        int count = 0;
        String regEx = "[\u4e00-\u9fa5]";
        for (int i = 0; i < text.length(); i++) {
            if (Pattern.matches(regEx, text.substring(i, i + 1))) {
                count += 2;
            } else {
                count += 1;
            }
        }
        return count;
    }


}
