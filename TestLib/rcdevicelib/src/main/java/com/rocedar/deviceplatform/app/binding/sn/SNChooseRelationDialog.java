package com.rocedar.deviceplatform.app.binding.sn;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.rocedar.base.RCHandler;
import com.rocedar.deviceplatform.R;
import com.rocedar.deviceplatform.app.view.RCDeviceWheelView;
import com.rocedar.deviceplatform.dto.data.RCDeviceFamilyRelationDTO;
import com.rocedar.deviceplatform.request.impl.RCDeviceOperationRequestImpl;
import com.rocedar.deviceplatform.request.listener.RCDeviceFamliyRelationListener;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by phj on 16/3/24.
 * <p>
 * 家人选择
 */
public class SNChooseRelationDialog extends Dialog {

    private ArrayList<String> mRelatioNames = new ArrayList<>();
    private RCHandler mRcHandler;
    private RCDeviceOperationRequestImpl operationRequest;
    private List<RCDeviceFamilyRelationDTO> all_List = new ArrayList<>();


    public SNChooseRelationDialog(Context context) {
        super(context);
        mRcHandler = new RCHandler(context);
        operationRequest = RCDeviceOperationRequestImpl.getInstance(context);
        setDialogTheme();
        getChooseData();
    }

    /**
     * set dialog theme(设置对话框主题)
     */
    private void setDialogTheme() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);// android:windowNoTitle
        getWindow().setBackgroundDrawableResource(R.drawable.transparent);// android:windowBackground
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);// android:backgroundDimEnabled默认是true的
    }

    private RCDeviceWheelView mRCDeviceWheelView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_choose_relation);
        mRCDeviceWheelView = (RCDeviceWheelView) findViewById(R.id.wheelview_lxpressure);
        /**
         * 确定按钮
         */
        findViewById(R.id.tv_lxpressure_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < all_List.size(); i++) {
                    if (all_List.get(i).getRelation_name().equals(mRCDeviceWheelView.getSelectedText())) {
                        if (onChooseListener != null)
                            onChooseListener.save(
                                    all_List.get(i)
                            );
                        return;
                    }
                }

            }
        });
        mRCDeviceWheelView.setData(mRelatioNames);
    }

    private OnChooseListener onChooseListener;

    public void setOnChooseListener(OnChooseListener onChooseListener) {
        this.onChooseListener = onChooseListener;
    }

    public interface OnChooseListener {
        void save(RCDeviceFamilyRelationDTO dto);
    }


    /**
     * 请求家人数据
     */
    private void getChooseData() {
        mRcHandler.sendMessage(RCHandler.START);
        operationRequest.queryFamilyRelationList(new RCDeviceFamliyRelationListener() {
            @Override
            public void getDataSuccess(List<RCDeviceFamilyRelationDTO> dtoList) {
                mRcHandler.sendMessage(RCHandler.GETDATA_OK);
                all_List = dtoList;
                mRelatioNames = new ArrayList<>();
                if (all_List != null || all_List.size() > 0) {
                    for (int i = 0; i < all_List.size(); i++) {
                        mRelatioNames.add(all_List.get(i).getRelation_name());
                    }
                    if (mRCDeviceWheelView != null) {
                        mRCDeviceWheelView.resetData(mRelatioNames);
                    }
                }
            }

            @Override
            public void getDataError(int status, String msg) {
                mRcHandler.sendMessage(RCHandler.GETDATA_OK);
            }
        });
    }
}
