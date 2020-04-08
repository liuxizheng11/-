package com.rocedar.sdk.familydoctor.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rocedar.sdk.familydoctor.R;
import com.rocedar.sdk.familydoctor.dto.RCFDSpecificCommentsDTO;
import com.rocedar.sdk.familydoctor.view.RCMyRatingBar;

import java.util.List;


/**
 * @author liuyi
 * @date 2018/4/24
 * @desc
 * @veison
 */

public class FDEvaluateListAdapter extends RecyclerView.Adapter<FDEvaluateListAdapter.EvaluateViewHolder> {

    private Context mContext;
    private List<RCFDSpecificCommentsDTO> mData;

    public FDEvaluateListAdapter(Context mContext, List<RCFDSpecificCommentsDTO> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @Override
    public EvaluateViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new EvaluateViewHolder(LayoutInflater.from(parent.getContext()).
                inflate(R.layout.rc_fd_item_list_evluate, parent, false));
    }

    @Override
    public void onBindViewHolder(EvaluateViewHolder holder, final int position) {
        final RCFDSpecificCommentsDTO dto = mData.get(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(position, dto);
                }
            }
        });

        holder.tvItemFdEvaluateName.setText(dto.getUser_phone());
        holder.tvItemDoctorEvaluateStarNumber.setText(dto.getGrade());
        if (!TextUtils.isEmpty(dto.getGrade()))
            holder.ratingbarItemDoctorEvaluate.setStar(Float.parseFloat(dto.getGrade()));

        holder.tvItemFdEvaluateDesc.setText(dto.getComment());
        holder.tvItemFdEvaluateDoctorName.setText(dto.getHospital_name() + "\t\t" + dto.getDoctor_name());

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class EvaluateViewHolder extends RecyclerView.ViewHolder {
        TextView tvItemFdEvaluateName;
        RCMyRatingBar ratingbarItemDoctorEvaluate;
        TextView tvItemDoctorEvaluateStarNumber;
        TextView tvItemFdEvaluateDesc;
        TextView tvItemFdEvaluateDoctorName;

        public EvaluateViewHolder(View itemView) {
            super(itemView);
            tvItemFdEvaluateName = itemView.findViewById(R.id.tv_item_fd_evaluate_name);
            ratingbarItemDoctorEvaluate = itemView.findViewById(R.id.ratingbar_item_doctor_evaluate);
            tvItemDoctorEvaluateStarNumber = itemView.findViewById(R.id.tv_item_doctor_evaluate_star_number);
            tvItemFdEvaluateDesc = itemView.findViewById(R.id.tv_item_fd_evaluate_desc);
            tvItemFdEvaluateDoctorName = itemView.findViewById(R.id.tv_item_fd_evaluate_doctor_name);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position, RCFDSpecificCommentsDTO dto);
    }

    private OnItemClickListener mOnItemClickListener = null;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }


//    public class HolderFooter extends RecyclerView.ViewHolder {
//
//        public LinearLayout loadmore;
//
//        public HolderFooter(View itemView) {
//            super(itemView);
//            loadmore = (LinearLayout) itemView.findViewById(com.rocedar.deviceplatform.R.id.view_load_more_main);
//            loadmore.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (loadmoreLintener != null)
//                        loadmoreLintener.load();
//                }
//            });
//        }
//
//    }
//
//    private boolean mHasMore = true;
//
//    public void setMoLoadMore(boolean hasMore) {
//        if (mHasMore) {
//            mHasMore = hasMore;
//            if (!hasMore) {
//                notifyItemRemoved(getItemCount());
//            }
//        } else {
//            mHasMore = hasMore;
//            if (hasMore) {
//                notifyItemInserted(getItemCount());
//            }
//        }
//    }
//
//    public void setLoadmoreLintener(LoadmoreLintener loadmoreLintener) {
//        this.loadmoreLintener = loadmoreLintener;
//    }
//
//    private LoadmoreLintener loadmoreLintener;
//
//
//    public interface LoadmoreLintener {
//
//        void load();
//
//    }
}
