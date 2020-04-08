package com.rocedar.sdk.familydoctor.app;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.TextWatcher;
import android.text.style.AbsoluteSizeSpan;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.TextView;

import com.rocedar.lib.base.dialog.PersonClickListener;
import com.rocedar.lib.base.dialog.PersonDateDialog;
import com.rocedar.lib.base.manage.RCBaseActivity;
import com.rocedar.lib.base.unit.RCDrawableUtil;
import com.rocedar.lib.base.unit.RCToast;
import com.rocedar.lib.base.view.RCScaleChat;
import com.rocedar.lib.base.view.wheel.PersonDatePicker;
import com.rocedar.sdk.familydoctor.R;
import com.rocedar.sdk.familydoctor.config.RCFDConfigUtil;
import com.rocedar.sdk.familydoctor.request.impl.RCFDHBPRequestImpl;
import com.rocedar.sdk.familydoctor.request.listener.hdp.RCHBPSaveOthersInfoListener;
import com.rocedar.sdk.familydoctor.util.RCFDDrawableUtil;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Pattern;

/**
 * 项目名称：瑰柏SDK-家庭医生
 * <p>
 * 作者：phj
 * 日期：2018/6/2 下午10:37
 * 版本：V1.0.00
 * 描述：瑰柏SDK-添加用户
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class RCFDAddUserActivity extends RCBaseActivity implements View.OnClickListener, View.OnTouchListener {

    private String TAG = "RegisterUserInfoActivity";

    public static void goActivity(Context context) {
        Intent intent = new Intent(context, RCFDAddUserActivity.class);
        context.startActivity(intent);
    }


    EditText activityBasicRUINameInput;//姓名输入
    TextView activityBasicRUIBirthday;//生日选择
    TextView activityBasicRUIStatureTv;//身高选择后显示
    RCScaleChat activityBasicRUIStatureChat;//身高选择刻度尺
    TextView activityBasicRUIWeightTv;//体重选择后显示
    RCScaleChat activityBasicRUIWeightChat;//体重选择刻度尺
    TextView activityBasicRUIOver;//完成按钮
    RadioButton activityBasicRUISexMan;//性别选择-男
    RadioButton activityBasicRUISexWoman;//性别选择-女
    ScrollView activityBasicRUIScrollview;


    private void initView() {
        activityBasicRUINameInput = findViewById(R.id.activity_basic_r_u_i_name_input);
        activityBasicRUIBirthday = findViewById(R.id.activity_basic_r_u_i_birthday);
        activityBasicRUIBirthday.setOnClickListener(this);
        activityBasicRUIStatureTv = findViewById(R.id.activity_basic_r_u_i_stature_tv);
        activityBasicRUIStatureChat = findViewById(R.id.activity_basic_r_u_i_stature_chat);
        activityBasicRUIWeightTv = findViewById(R.id.activity_basic_r_u_i_weight_tv);
        activityBasicRUIWeightChat = findViewById(R.id.activity_basic_r_u_i_weight_chat);
        if (RCFDConfigUtil.getConfig().imageResVernier() != -1) {
            activityBasicRUIStatureChat.setCenterImage(RCFDConfigUtil.getConfig().imageResVernier());
            activityBasicRUIWeightChat.setCenterImage(RCFDConfigUtil.getConfig().imageResVernier());
        }
        activityBasicRUIStatureChat.setOnTouchListener(this);
        activityBasicRUIWeightChat.setOnTouchListener(this);
        activityBasicRUIOver = findViewById(R.id.activity_basic_r_u_i_over);
        activityBasicRUIOver.setOnClickListener(this);
        activityBasicRUISexMan = findViewById(R.id.activity_basic_r_u_i_sex_man);
        activityBasicRUISexMan.setOnClickListener(this);
        activityBasicRUISexMan.setBackground(RCFDDrawableUtil.choose_button_sex(mContext));
        activityBasicRUISexWoman = findViewById(R.id.activity_basic_r_u_i_sex_woman);
        activityBasicRUISexWoman.setOnClickListener(this);
        activityBasicRUISexWoman.setBackground(RCFDDrawableUtil.choose_button_sex(mContext));
        activityBasicRUIScrollview = findViewById(R.id.activity_basic_r_u_i_scrollview);

    }

    private int SEX_CHOOSE_MAN = 1;
    private int SEX_CHOOSE_WOMEN = 0;

    private int chooseWeight = 50;
    private int chooseStature = 150;
    private int chooseSex = SEX_CHOOSE_MAN;
    private String chooseBirthday = "20000101";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rc_fd_activity_add_user_info);
        initView();
        setSwipeBackFalse();
        mRcHeadUtil.setTitle(getString(R.string.rc_fd_perfect_information)).
                setToolbarLine(Color.TRANSPARENT);
        mRcHeadUtil.setLeftBack(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new JSONObject());
                finishActivity();
            }
        });

        SpannableString name = new SpannableString(getString(R.string.rc_fd_truename_hint_input));//定义hint的值
        AbsoluteSizeSpan ass = new AbsoluteSizeSpan(9, true);//设置字体大小 true表示单位是sp
        name.setSpan(ass, 0, name.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        activityBasicRUINameInput.setHint(new SpannedString(name));
        activityBasicRUINameInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                checkIsInputOk(false);

            }
        });

        //设置生日

        setTextSize(showAge(chooseBirthday), 19, 14, activityBasicRUIBirthday);
//        activityBasicRUIBirthday.setTypeface(DYFontCustom.getTf(mContext));
        //

        setTextSize(chooseStature + "cm", 19, 14, activityBasicRUIStatureTv);
//        activityBasicRUIStatureTv.setTypeface(DYFontCustom.getTf(mContext));
        activityBasicRUIStatureChat.setScaleChatChooseListener(new RCScaleChat.ScaleChatChooseListener() {
            @Override
            public void chooseData(String data, float number) {
                setTextSize(data, 19, 14, activityBasicRUIStatureTv);
                chooseStature = (int) number;
                checkIsInputOk(false);
            }
        });
        activityBasicRUIStatureChat.selectIndex(chooseStature);

        //

        setTextSize(chooseWeight + "kg", 19, 14, activityBasicRUIWeightTv);
//        activityBasicRUIWeightTv.setTypeface(DYFontCustom.getTf(mContext));
        activityBasicRUIWeightChat.setScaleChatChooseListener(new RCScaleChat.ScaleChatChooseListener() {
            @Override
            public void chooseData(String data, float number) {
                setTextSize(data, 19, 14, activityBasicRUIWeightTv);
                chooseWeight = (int) number;
                checkIsInputOk(false);
            }
        });
        activityBasicRUIWeightChat.selectIndex(chooseWeight);


        activityBasicRUIScrollview.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
        activityBasicRUIScrollview.setFocusable(true);
        activityBasicRUIScrollview.setFocusableInTouchMode(true);
        activityBasicRUIScrollview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.requestFocusFromTouch();
                return false;
            }
        });

        checkIsInputOk(false);

    }


    private String showAge(String ageTime) {
        return ageTime.substring(0, 4) + mContext.getString(R.string.rc_fd_year) +
                ageTime.substring(4, 6) + mContext.getString(R.string.rc_fd_month) +
                ageTime.substring(6, 8) + mContext.getString(R.string.rc_fd_day);
    }


    /**
     * 检查是否都已经填写完成
     *
     * @return
     */
    private boolean checkIsInputOk(boolean hasToast) {
        if (activityBasicRUINameInput.getText().toString().trim().equals("")) {
            if (hasToast) RCToast.Center(mContext, getString(R.string.rc_fd_truename_hint_input));
            activityBasicRUIOver.setBackground(
                    RCDrawableUtil.getColorDrawableBaseRadius(mContext, Color.parseColor("#cccccc")));
            return false;
        }
        activityBasicRUIOver.setBackground(RCDrawableUtil.getMainColorDrawableBaseRadius(mContext));
        return true;
    }


    public void setTextSize(String text, int maxSize, int minSize, TextView mTextView) {
        SpannableString spannableString = new SpannableString(text);
        String regEx = "[0-9]";
        for (int i = 0; i < text.length(); i++) {
            if (Pattern.matches(regEx, text.substring(i, i + 1))) {
                AbsoluteSizeSpan spanMin = new AbsoluteSizeSpan(maxSize, true);
//                new TypefaceSpan(typeface)
                spannableString.setSpan(spanMin, i, i + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else {
                AbsoluteSizeSpan spanMax = new AbsoluteSizeSpan(minSize, true);
                spannableString.setSpan(spanMax, i, i + 1,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        mTextView.setText(spannableString);
//        mTextView.setTypeface(DYFontCustom.getTf(mContext));
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        EventBus.getDefault().post(new JSONObject());
        finishActivity();
    }


    private PersonDateDialog personTimeDialog;


    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.activity_basic_r_u_i_sex_man) {
            activityBasicRUISexMan.setChecked(true);
            chooseSex = SEX_CHOOSE_MAN;
            checkIsInputOk(false);
        } else if (i == R.id.activity_basic_r_u_i_sex_woman) {
            activityBasicRUISexWoman.setChecked(true);
            chooseSex = SEX_CHOOSE_WOMEN;
            checkIsInputOk(false);
        } else if (i == R.id.activity_basic_r_u_i_birthday) {
            if (personTimeDialog == null)
                personTimeDialog = new PersonDateDialog(mContext, chooseBirthday, new PersonClickListener() {
                    @Override
                    public void onclick(String time) {
                        if (!time.equals("")) {
                            setTextSize(showAge(time), 19, 14, activityBasicRUIBirthday);
//                            activityBasicRUIBirthday.setTypeface(DYFontCustom.getTf(mContext));
                        }
                        chooseBirthday = time;
                        personTimeDialog.dismiss();
                        checkIsInputOk(false);
                    }
                }, PersonDatePicker.CHOOSE_DATE_TYPE_BIRTHDAY);
            personTimeDialog.show();

        } else if (i == R.id.activity_basic_r_u_i_over) {
            if (checkIsInputOk(true)) {
                new RCFDHBPRequestImpl(mContext).saveOthersInfo(
                        activityBasicRUINameInput.getText().toString().trim(),
                        chooseSex + "", chooseBirthday + "",
                        chooseStature + "", chooseWeight + "",
                        new RCHBPSaveOthersInfoListener() {
                            @Override
                            public void getDataSuccess(long userID) {
                                JSONObject jsonObject = new JSONObject();
                                try {
                                    jsonObject.put("user_id", userID);
                                    jsonObject.put("name", activityBasicRUINameInput.getText().toString().trim());
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                EventBus.getDefault().post(jsonObject);
                                finishActivity();
                            }

                            @Override
                            public void getDataError(int status, String msg) {
                            }
                        }
                );

            }

        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        //触摸的是EditText控件，且当前EditText可以滚动，则将事件交给EditText处理；否则将事件交由其父类处理
        if ((view.getId() == R.id.activity_basic_r_u_i_stature_chat) ||
                (view.getId() == R.id.activity_basic_r_u_i_weight_chat)) {
            view.getParent().requestDisallowInterceptTouchEvent(true);
            if (event.getAction() == MotionEvent.ACTION_UP) {
                view.getParent().requestDisallowInterceptTouchEvent(false);
            }
        }
        return false;
    }
}
