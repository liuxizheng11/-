package com.rocedar.sdk.shop.app.order.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rocedar.lib.base.manage.RCBaseFragment;
import com.rocedar.lib.base.unit.RCDrawableUtil;
import com.rocedar.sdk.shop.R;
import com.rocedar.sdk.shop.app.order.adapter.MyOrderFromFragmengPagerAdapter;
import com.rocedar.sdk.shop.config.RCShopConfigUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;

import static com.rocedar.lib.base.unit.RCAndroid.dip2px;

/**
 * 作者：lxz
 * 日期：2018/7/10 上午9:58
 * 版本：V1.0
 * 描述：public class MyOrderFromMainFragment extends RCBaseFragment {
 *     private TabLayout rc_fragment_my_order_tab;
 *     private ViewPager rc_fragment_my_order_viewpager;
 *
 *     private String[] title_name = {"全部", "待付款", "已支付", "已完成", "取消/退款"};
 *     /**
 *      * -1: 删除；0: 已取消；1: 待付款；2：已支付，3：已完成；4: 退款中 6:已退款
 *      */
 *private String[]title_id={"0,1,2,3,4,6","1","2","3","0,4,6"};
        *
        *private ArrayList<Fragment> mFragments=new ArrayList<>();
        *
        *public static MyOrderFromMainFragment newInstance(){
        *MyOrderFromMainFragment fragment=new MyOrderFromMainFragment();
        *Bundle args=new Bundle();
        *fragment.setArguments(args);
        *return fragment;
        *}
        *
        *@Override
 *public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){
        *View view=inflater.inflate(R.layout.rc_fragment_my_order_main,null);
        *rc_fragment_my_order_tab=view.findViewById(R.id.rc_fragment_my_order_tab);
        *rc_fragment_my_order_viewpager=view.findViewById(R.id.rc_fragment_my_order_viewpager);
        *initView();
        *
        *
        *return view;
        *
        *}
        *
        *private void initView(){
        *for(int i=0;i<title_name.length;i++){
        *mFragments.add(MyOrderFromListFragment.newInstance(title_id[i]));
        *}
        *MyOrderFromFragmengPagerAdapter pagerAdapter=new MyOrderFromFragmengPagerAdapter(mActivity.getSupportFragmentManager(),mFragments,title_name);
        *rc_fragment_my_order_viewpager.setAdapter(pagerAdapter);
        *rc_fragment_my_order_tab.setupWithViewPager(rc_fragment_my_order_viewpager);//将TabLayout和ViewPager关联起来。
        *         //暂时去掉指示器颜色
        *rc_fragment_my_order_tab.setSelectedTabIndicatorColor(RCDrawableUtil.getThemeAttrColor(mActivity,R.attr.RCDarkColor));
        *reflex(rc_fragment_my_order_tab);
        *}
        *
        *     /**
 *      * 设置 tabLayout下划线 宽度
 *      *
 *      * @param tabLayout
 *      */
        *public void reflex(final TabLayout tabLayout){
        *         //了解源码得知 线的宽度是根据 tabView的宽度来设置的
        *tabLayout.post(new Runnable(){
        *@Override
 *public void run(){
        *try{
        *                     //拿到tabLayout的mTabStrip属性
        *LinearLayout mTabStrip=(LinearLayout)tabLayout.getChildAt(0);
        *
        *int dp10=dip2px(tabLayout.getContext(),15);
        *
        *for(int i=0;i<mTabStrip.getChildCount();i++){
        *View tabView=mTabStrip.getChildAt(i);
        *
        *                         //拿到tabView的mTextView属性  tab的字数不固定一定用反射取mTextView
        *Field mTextViewField=tabView.getClass().getDeclaredField("mTextView");
        *mTextViewField.setAccessible(true);
        *
        *TextView mTextView=(TextView)mTextViewField.get(tabView);
        *
        *tabView.setPadding(0,0,0,0);
        *
        *                         //因为我想要的效果是   字多宽线就多宽，所以测量mTextView的宽度
        *int width=0;
        *width=mTextView.getWidth();
        *if(width==0){
        *mTextView.measure(0,0);
        *width=mTextView.getMeasuredWidth();
        *}
        *
        *                         //设置tab左右间距为10dp  注意这里不能使用Padding 因为源码中线的宽度是根据 tabView的宽度来设置的
        *LinearLayout.LayoutParams params=(LinearLayout.LayoutParams)tabView.getLayoutParams();
        *params.width=width+20;
        *params.leftMargin=dp10;
        *params.rightMargin=dp10;
        *tabView.setLayoutParams(params);
        *
        *tabView.invalidate();
        *}
        *
        *}catch(NoSuchFieldException e){
        *e.printStackTrace();
        *}catch(IllegalAccessException e){
        *e.printStackTrace();
        *}
        *}
        *});
        *
        *}
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (RCShopConfigUtil.getConfig() != null) {
            RCShopConfigUtil.getConfig().yunXinUnRegisterBroad();
        }
    }
}
