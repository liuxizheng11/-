package com.rocedar.deviceplatform.app.highbloodpressure.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rocedar.base.RCImageShow;
import com.rocedar.deviceplatform.R;
import com.rocedar.deviceplatform.dto.highbloodpressure.RCHBPVideoInstituteDTO;

import java.util.List;


/**
 * @author liuyi
 * @date 2017/11/21
 * @desc 专题讲座
 * @veison
 */

public class HBPSeminarListAdapter extends RecyclerView.Adapter<HBPSeminarListAdapter.ViewHolder> {
    private List<RCHBPVideoInstituteDTO> mDatas;
    private Context mContext;

    public interface OnItemClickListener {
        void onItemClick(View view);
    }

    private OnItemClickListener mOnItemClickListener = null;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public HBPSeminarListAdapter(Context mContext, List<RCHBPVideoInstituteDTO> mDatas) {
        this.mDatas = mDatas;
        this.mContext = mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_hbp_professor, parent, false);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onItemClick(v);
            }
        });

        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.item.setTag(position);
        RCHBPVideoInstituteDTO dto = mDatas.get(position);
        RCImageShow.loadUrl(dto.getVideo_img(), holder.ivItemHbpProfessorImg, RCImageShow.IMAGE_TYPE_STAR, R.mipmap.img_home_occupied_classroom_holder);
        holder.ivItemHbpProfessorTitle.setText(dto.getTitle());
        holder.ivItemHbpProfessorCount.setText(dto.getAccess_count());
        holder.ivItemHbpProfessorTime.setText(dto.getVideo_time());
        //动态添加标签
        holder.llItemHbpProfessorTag.removeAllViews();

        for (int i = 0; dto.getLabel() != null && i < dto.getLabel().size(); i++) {

            View view = LayoutInflater.from(mContext).inflate(R.layout.include_textview_shop_list, null);
            TextView tv_home_tag = (TextView) view.findViewById(R.id.tv_home_tag);
            tv_home_tag.setText(dto.getLabel().get(i));
            holder.llItemHbpProfessorTag.addView(view);
        }
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        View item;
        ImageView ivItemHbpProfessorImg;
        TextView ivItemHbpProfessorTitle;
        LinearLayout llItemHbpProfessorTag;
        TextView ivItemHbpProfessorCount;
        TextView ivItemHbpProfessorTime;

        ViewHolder(View view) {
            super(view);
            this.item = view;
            ivItemHbpProfessorImg = (ImageView) view.findViewById(R.id.iv_item_hbp_professor_img);
            ivItemHbpProfessorTitle = (TextView) view.findViewById(R.id.iv_item_hbp_professor_title);
            llItemHbpProfessorTag = (LinearLayout) view.findViewById(R.id.ll_item_hbp_professor_tag);
            ivItemHbpProfessorCount = (TextView) view.findViewById(R.id.iv_item_hbp_professor_count);
            ivItemHbpProfessorTime = (TextView) view.findViewById(R.id.tv_item_hbp_professor_time);
        }
    }
}
