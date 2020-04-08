package com.rocedar.sdk.assessment.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.rocedar.lib.base.manage.RCBaseFragment;
import com.rocedar.lib.base.unit.RCHandler;
import com.rocedar.sdk.assessment.R;
import com.rocedar.sdk.assessment.RCAssessmentActivity;
import com.rocedar.sdk.assessment.RCAssessmentResultActivity;
import com.rocedar.sdk.assessment.adapter.AssessmentListAdapter;
import com.rocedar.sdk.assessment.dto.RCAssessmentListDTO;
import com.rocedar.sdk.assessment.request.IRCAssessmentRequest;
import com.rocedar.sdk.assessment.request.impl.RCAssessmentRequestImpl;
import com.rocedar.sdk.assessment.request.listener.RCAssessmentListListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名称：瑰柏SDK-问卷
 * <p>
 * 作者：phj
 * 日期：2018/6/26 上午11:54
 * 版本：V1.0.00
 * 描述：瑰柏SDK-问卷列表
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class RCAssessmentListFragment extends RCBaseFragment {


    public static RCAssessmentListFragment newInstance(String phoneNumber, String deviceNo) {
        Bundle args = new Bundle();
        args.putString("phone_number", phoneNumber);
        args.putString("device_no", deviceNo);
        RCAssessmentListFragment fragment = new RCAssessmentListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private AssessmentListAdapter assessmentListAdapter;
    private IRCAssessmentRequest request;
    /**
     * 题目 ID
     */
    private List<RCAssessmentListDTO> mList = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ListView view = (ListView) inflater.inflate(R.layout.rc_view_listview, null);
        request = new RCAssessmentRequestImpl(mActivity);
        assessmentListAdapter = new AssessmentListAdapter(mActivity, mList);
        view.setAdapter(assessmentListAdapter);
        view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mList.size() > 0) {
                    // 0没填  1填了
                    if (mList.get(position).getFill_in() == 0) {
                        RCAssessmentActivity.goActivity(mActivity,
                                mList.get(position).getQuestionnaire_id(),
                                mList.get(position).getQuestionnaire_name(),
                                getArguments().getString("phone_number"),
                                getArguments().getString("device_no"));
                    } else {
                        RCAssessmentResultActivity.goActivity(mActivity,
                                mList.get(position).getQuestionnaire_id(),
                                mList.get(position).getQuestionnaire_name(),
                                getArguments().getString("phone_number"),
                                getArguments().getString("device_no"));
                    }
                }

            }
        });
        return view;

    }


    private void initData() {
        mRcHandler.sendMessage(RCHandler.START);
        request.getEvaluationListData(new RCAssessmentListListener() {

            @Override
            public void getDataSuccess(List<RCAssessmentListDTO> listDTOS) {
                mList.clear();
                mList.addAll(listDTOS);
                assessmentListAdapter.notifyDataSetChanged();
                mRcHandler.sendMessage(RCHandler.GETDATA_OK);
            }

            @Override
            public void getDataError(int status, String msg) {
                mRcHandler.sendMessage(RCHandler.GETDATA_OK);
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        initData();
    }
}
