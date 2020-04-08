package com.rocedar.base.chart.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rocedar.base.chart.R;
import com.rocedar.base.chart.dto.XBaseColorEntity;
import com.rocedar.base.chart.dto.XBaseEntity;
import com.rocedar.base.chart.dto.XCoordColor;

import java.util.List;

/**
 * 项目名称：TestXT
 * <p>
 * 作者：phj
 * 日期：2017/12/27 下午4:30
 * 版本：V2.2.00
 * 描述：
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public class RCBaseChartAdapter extends RecyclerView.Adapter<RCBaseChartAdapter.MViewHolder> {

    //选中的项
    public int selIndex = -1;

    /**
     * 设置选中的项
     *
     * @param selIndex
     */
    public void setSelIndex(int selIndex) {
        this.selIndex = selIndex;
        notifyDataSetChanged();
    }

    /**
     * 获取当前选中的项的索引
     *
     * @return
     */
    public int getSelIndex() {
        return selIndex;
    }

    //默认的X轴颜色
    private XCoordColor baseXColorDefault = null;
    //选中的的X轴颜色
    private XCoordColor baseXColorFocus = null;
    // X轴显示的内容
    private List<XBaseEntity> mList;


    /**
     * @param xBaseColorEntity X轴的颜色属性
     * @param xBaseEntityList  X轴显示的内容
     */
    public RCBaseChartAdapter(XBaseColorEntity xBaseColorEntity, List<XBaseEntity> xBaseEntityList) {
        this.baseXColorDefault = xBaseColorEntity.getxColorDefault();
        this.baseXColorFocus = xBaseColorEntity.getxColorFocus();
        this.mList = xBaseEntityList;
    }

    @Override
    public MViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.view_rc_base_chart_bottom_item, parent, false);
        return new MViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(final MViewHolder holder, final int position) {
        String oneLineShow = position + "", twoLineShow = null;
        if (mList.get(position).getLineOne() != null)
            oneLineShow = mList.get(position).getLineOne();
        if (mList.get(position).getLineTwo() != null)
            twoLineShow = mList.get(position).getLineTwo();
        holder.lineOne.setText(oneLineShow);
        if (twoLineShow == null) {
            holder.lineTwo.setVisibility(View.GONE);
        } else {
            holder.lineTwo.setVisibility(View.VISIBLE);
            holder.lineTwo.setText(twoLineShow);
        }
        //设置背景颜色及文字颜色
        int dBG, dText, fBG, fText;
        if (mList.get(position).getxColorDefault() != null) {
            dBG = mList.get(position).getxColorDefault().getBackgroundColor();
            dText = mList.get(position).getxColorDefault().getTextColor();
        } else if (baseXColorDefault != null) {
            dBG = baseXColorDefault.getBackgroundColor();
            dText = baseXColorDefault.getTextColor();
        } else {
            dBG = Color.TRANSPARENT;
            dText = Color.parseColor("#dfdfdf");
        }
        if (mList.get(position).getxColorFocus() != null) {
            fBG = mList.get(position).getxColorFocus().getBackgroundColor();
            fText = mList.get(position).getxColorFocus().getTextColor();
        } else if (baseXColorFocus != null) {
            fBG = baseXColorFocus.getBackgroundColor();
            fText = baseXColorFocus.getTextColor();
        } else {
            fBG = Color.TRANSPARENT;
            fText = Color.parseColor("#dfdfdf");
        }
        if (selIndex == position) {
            holder.lineOne.setTextColor(fText);
            holder.lineTwo.setTextColor(fText);
            holder.textBg.setBackgroundColor(fBG);
        } else {
            holder.lineOne.setTextColor(dText);
            holder.lineTwo.setTextColor(dText);
            holder.textBg.setBackgroundColor(dBG);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("12", "1231231->" + position);
                setSelIndex(position);
                if (clickListener != null) {
                    clickListener.chartOnSelect(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    class MViewHolder extends RecyclerView.ViewHolder {

        public TextView lineOne;
        public TextView lineTwo;
        public LinearLayout textBg;

        public MViewHolder(View itemView) {
            super(itemView);
            lineOne = (TextView) itemView.findViewById(R.id.view_rc_base_chart_bottom_item_one);
            lineTwo = (TextView) itemView.findViewById(R.id.view_rc_base_chart_bottom_item_two);
            textBg = (LinearLayout) itemView.findViewById(R.id.view_rc_base_chart_bottom_item);
        }

    }

    private BaseChartAdapterClickListener clickListener;

    public void setClickListener(BaseChartAdapterClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public interface BaseChartAdapterClickListener {

        void chartOnSelect(int position);

    }

}


