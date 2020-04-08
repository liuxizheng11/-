package com.rocedar.sdk.familydoctor.app.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.rocedar.lib.base.manage.RCBaseFragment;
import com.rocedar.lib.base.unit.RCHandler;
import com.rocedar.sdk.familydoctor.R;
import com.rocedar.sdk.familydoctor.app.RCFDAddUserActivity;
import com.rocedar.sdk.familydoctor.app.RCFDPatientListActivity;
import com.rocedar.sdk.familydoctor.app.adapter.FDPatientListAdapter;
import com.rocedar.sdk.familydoctor.dto.RCFDPatientListDTO;
import com.rocedar.sdk.familydoctor.request.IRCFDHBPRequest;
import com.rocedar.sdk.familydoctor.request.impl.RCFDHBPRequestImpl;
import com.rocedar.sdk.familydoctor.request.listener.RCFDPatientListListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名称：瑰柏SDK-基础库
 * <p>
 * 作者：phj
 * 日期：2018/7/24 下午5:39
 * 版本：V1.0.00
 * 描述：瑰柏SDK-
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class RCFDPatientListFragment extends RCBaseFragment {

    public static RCFDPatientListFragment newInstance() {
        Bundle args = new Bundle();
        RCFDPatientListFragment fragment = new RCFDPatientListFragment();
        fragment.setArguments(args);
        return fragment;
    }


    private ListView listView;
    private TextView addButton;
    private FDPatientListAdapter adapter;

    private List<RCFDPatientListDTO> patientListDTOS = new ArrayList<>();

    private IRCFDHBPRequest request;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.rc_fd_fragment_patient, null);
        request = new RCFDHBPRequestImpl(mActivity);
        listView = view.findViewById(R.id.rc_fd_fragment_patient_listview);
        addButton = view.findViewById(R.id.rc_fd_fragment_patient_button);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().register(RCFDPatientListFragment.this);
                RCFDAddUserActivity.goActivity(mActivity);
            }
        });


        listView.setAdapter(adapter = new FDPatientListAdapter(mActivity, patientListDTOS));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onBack(position);
            }
        });
        getPatientList();
        return view;
    }


    private void getPatientList() {
        mRcHandler.sendMessage(RCHandler.START);
        request.getPatientList(new RCFDPatientListListener() {
            @Override
            public void getDataSuccess(List<RCFDPatientListDTO> dtoList) {
                patientListDTOS.clear();
                patientListDTOS.addAll(dtoList);
                if (adapter != null)
                    adapter.notifyDataSetInvalidated();
                mRcHandler.sendMessage(RCHandler.GETDATA_OK);
            }

            @Override
            public void getDataError(int status, String msg) {
                mRcHandler.sendMessage(RCHandler.GETDATA_OK);
            }
        });
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(JSONObject jsonObject) {
        EventBus.getDefault().unregister(this);
        if (jsonObject.has("user_id")) {
            getPatientList();
        }
    }


    private void onBack(int chooseIndex) {
        Intent intent = new Intent();
        if (chooseIndex >= 0) {
            intent.putExtra(RCFDPatientListActivity.RESULT_KEY_PATIENT_ID, patientListDTOS.get(chooseIndex).getSick_id());
            intent.putExtra(RCFDPatientListActivity.RESULT_KEY_PATIENT_NAME, patientListDTOS.get(chooseIndex).getSick_name());
            mActivity.setResult(mActivity.RESULT_OK, intent);
        } else {
            mActivity.setResult(mActivity.RESULT_CANCELED, intent);
        }
        mActivity.finish();
    }

}
