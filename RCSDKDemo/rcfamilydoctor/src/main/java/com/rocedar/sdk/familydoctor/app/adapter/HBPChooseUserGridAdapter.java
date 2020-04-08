package com.rocedar.sdk.familydoctor.app.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rocedar.lib.base.unit.RCDrawableUtil;
import com.rocedar.sdk.familydoctor.R;
import com.rocedar.sdk.familydoctor.dto.hdp.RCHBPRecordListDTO;

import java.util.ArrayList;
import java.util.List;


/**
 * @author liuyi
 * @date 2017/11/24
 * @desc 选择咨询用户列表适配器
 * @veison
 */

public class HBPChooseUserGridAdapter extends RecyclerView.Adapter<HBPChooseUserGridAdapter.ViewHolder> {


    //用户列表
    private List<RCHBPRecordListDTO.SicksDTO> mDatas = new ArrayList<>();

    public List<RCHBPRecordListDTO.SicksDTO> getmDatas() {
        return mDatas;
    }

    private Context mContext;

    public interface OnItemClickListener {
        void onItemClick(View view);
    }

    private OnItemClickListener mOnItemClickListener = null;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public HBPChooseUserGridAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_hbp_consult_choose_user, parent, false);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null)
                    mOnItemClickListener.onItemClick(v);
            }
        });

        return new ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.view.setTag(position);
        RCHBPRecordListDTO.SicksDTO dto = mDatas.get(position);
        holder.tvItemBhpConsultUserName.setText(dto.getSick_name());
        //如果为选中，显示对应的样式
        if (dto.getChoose() > 0) {
            selectUser = dto.getSick_id();
            holder.tvItemBhpConsultUserName.setBackground(
                    RCDrawableUtil.getMainColorDrawableBaseRadius(mContext));
            holder.tvItemBhpConsultUserName.setTextColor(Color.WHITE);
        } else {
            holder.tvItemBhpConsultUserName.setBackground(
                    RCDrawableUtil.getDrawableStroke(mContext, Color.TRANSPARENT, 1,
                            0xffcccccc, RCDrawableUtil.getThemeAttrDimension(mContext,R.attr.RCButtonRadius)));
            holder.tvItemBhpConsultUserName.setTextColor(
                    Color.parseColor("#999999")
            );
        }
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvItemBhpConsultUserName;
        private View view;

        ViewHolder(View view) {
            super(view);
            this.view = view;
            tvItemBhpConsultUserName = (TextView) view.findViewById(R.id.tv_item_bhp_consult_user_name);
        }
    }

    //
    private long selectUser = -1;
    private long lastUser;


    public long getSelectUser() {
        return selectUser;
    }

    public void setSelectUser(long userId) {
        this.selectUser = userId;
        if (selectUser != lastUser) {
            for (int i = 0; i < mDatas.size(); i++) {
                RCHBPRecordListDTO.SicksDTO dto = mDatas.get(i);
                if (userId == dto.getSick_id()) {
                    dto.setChoose(2);
                } else {
                    dto.setChoose(-1);
                }
            }
            lastUser = selectUser;
        }
        notifyItemRangeChanged(0, mDatas.size());
    }

    /**
     * 选择其他后增加用户回显
     *
     * @param dto
     */
    public void addUser(RCHBPRecordListDTO.SicksDTO dto) {
        mDatas.add(mDatas.size() - 1, dto);
        notifyItemInserted(mDatas.size() - 1);
    }


    /**
     * 设置用户列表数据
     *
     * @param sicksDTOList
     */
    public void setmDatas(List<RCHBPRecordListDTO.SicksDTO> sicksDTOList) {
        if (mDatas.size() > 0) {
            notifyItemRangeRemoved(0, mDatas.size());
            mDatas.clear();
        }
        if (sicksDTOList.size() > 0) {
            mDatas.addAll(sicksDTOList);
            notifyItemRangeInserted(0, mDatas.size());
        }
    }


    /**
     * 保存问卷ID
     *
     * @param questionnaireId
     */
    public void saveQuestionnaireId(int questionnaireId) {
        for (int i = 0; i < mDatas.size(); i++) {
            if (mDatas.get(i).getSick_id() == selectUser) {
                mDatas.get(i).setQuestionnaire_id(questionnaireId);
                return;
            }
        }
    }
}
