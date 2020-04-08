package com.rocedar.sdk.familydoctor.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rocedar.lib.base.unit.RCDrawableUtil;
import com.rocedar.lib.base.unit.RCImageShow;
import com.rocedar.lib.base.view.CircleImageView;
import com.rocedar.sdk.familydoctor.R;
import com.rocedar.sdk.familydoctor.app.RCFDHBPConsultProfessorActivity;
import com.rocedar.sdk.familydoctor.dto.hdp.RCHBPOrganizationListDTO;

import java.util.List;


/**
 * @author liuyi
 * @date 2018/4/24
 * @desc
 * @veison
 */

public class FDOrganizationAdapter extends RecyclerView.Adapter<FDOrganizationAdapter.FDOrganization> {

    private List<RCHBPOrganizationListDTO> mData;
    private Context mContext;

    public FDOrganizationAdapter(List<RCHBPOrganizationListDTO> mData, Context mContext) {
        this.mData = mData;
        this.mContext = mContext;
    }

    @Override
    public FDOrganization onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FDOrganization(LayoutInflater.from(parent.getContext()).
                inflate(R.layout.rc_fd_item_list_organization, parent, false));
    }

    @Override
    public void onBindViewHolder(FDOrganization holder, final int position) {
        final RCHBPOrganizationListDTO dto = mData.get(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(position, dto);
                }
            }
        });

        RCImageShow.loadUrl(dto.getOrg_icon(), holder.civItemFdOrganizationIcon, RCImageShow.IMAGE_TYPE_HEAD);
        holder.tvItemFdOrganizationDesc.setText(dto.getOrg_name());
        holder.tvItemFdOrganizationConsult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RCFDHBPConsultProfessorActivity.goActivity(mContext, dto.getOrg_id());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class FDOrganization extends RecyclerView.ViewHolder {
        CircleImageView civItemFdOrganizationIcon;
        TextView tvItemFdOrganizationDesc;
        TextView tvItemFdOrganizationConsult;

        public FDOrganization(View itemView) {
            super(itemView);
            civItemFdOrganizationIcon = itemView.findViewById(R.id.civ_item_fd_organization_icon);
            tvItemFdOrganizationDesc = itemView.findViewById(R.id.tv_item_fd_organization_desc);
            tvItemFdOrganizationConsult = itemView.findViewById(R.id.tv_item_fd_organization_consult);
            tvItemFdOrganizationConsult.setBackground(
                    RCDrawableUtil.getMainColorDrawableBaseRadius(mContext)
            );
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position, RCHBPOrganizationListDTO dto);
    }

    private OnItemClickListener mOnItemClickListener = null;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
}
