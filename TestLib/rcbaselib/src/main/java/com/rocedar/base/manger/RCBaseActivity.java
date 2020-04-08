package com.rocedar.base.manger;

import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.support.v4.view.WindowCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.jaeger.library.StatusBarUtil;
import com.rocedar.base.R;
import com.rocedar.base.RCAndroid;
import com.rocedar.base.RCBaseConfig;
import com.rocedar.base.RCBaseManage;
import com.rocedar.base.RCHandler;
import com.rocedar.base.RCHeadUtil;
import com.rocedar.base.RCUmeng;
import com.rocedar.base.statuscolor.RCStatusColorHelper;

import cn.bingoogolapple.swipebacklayout.BGASwipeBackHelper;


/**
 * Activity管理类，所有activity都继承该类
 */
public abstract class RCBaseActivity extends AppCompatActivity implements BGASwipeBackHelper.Delegate {

    public String TAG = "RCBase_activity";

    public AppCompatActivity mContext;
    public RCHeadUtil mRcHeadUtil;
    public RCHandler mRcHandler;

    private boolean isDestroy;

    public LayoutInflater layoutInflater;

    protected BGASwipeBackHelper mSwipeBackHelper;

    @Override
    protected void onStart() {
        super.onStart();
    }

    private boolean notAddHead = false;

    private boolean notSwipeBack = false;

    /**
     * 不加载头
     *
     * @param notAddHead
     */
    public void setNotAddHead(boolean notAddHead) {
        this.notAddHead = notAddHead;
    }

    public void setNotSwipeBack(boolean notSwipeBack) {
        this.notSwipeBack = notSwipeBack;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // 在 super.onCreate(savedInstanceState) 之前调用该方法
        initSwipeBackFinish();
        super.onCreate(savedInstanceState);
        mContext = this;
        mRcHeadUtil = new RCHeadUtil(mContext);
        mRcHandler = new RCHandler(mContext);
        layoutInflater = LayoutInflater.from(mContext);
        supportRequestWindowFeature(WindowCompat.FEATURE_ACTION_MODE_OVERLAY);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        RCBaseManage.getScreenManger().addActivity(this);
        isDestroy = false;

    }

    /**
     * 初始化滑动返回。在 super.onCreate(savedInstanceState) 之前调用该方法
     */
    private void initSwipeBackFinish() {
        if (notSwipeBack) return;
        mSwipeBackHelper = new BGASwipeBackHelper(this, this);

        // 「必须在 Application 的 onCreate 方法中执行 BGASwipeBackManager.getInstance().init(this) 来初始化滑动返回」
        // 下面几项可以不配置，这里只是为了讲述接口用法。

        // 设置滑动返回是否可用。默认值为 true
        mSwipeBackHelper.setSwipeBackEnable(true);
        // 设置是否仅仅跟踪左侧边缘的滑动返回。默认值为 true
        mSwipeBackHelper.setIsOnlyTrackingLeftEdge(true);
        // 设置是否是微信滑动返回样式。默认值为 true
        mSwipeBackHelper.setIsWeChatStyle(true);
        // 设置阴影资源 id。默认值为 R.drawable.bga_sbl_shadow
        mSwipeBackHelper.setShadowResId(R.drawable.bga_sbl_shadow);
        // 设置是否显示滑动返回的阴影效果。默认值为 true
        mSwipeBackHelper.setIsNeedShowShadow(true);
        // 设置阴影区域的透明度是否根据滑动的距离渐变。默认值为 true
        mSwipeBackHelper.setIsShadowAlphaGradient(true);
        // 设置触发释放后自动滑动返回的阈值，默认值为 0.3f
        mSwipeBackHelper.setSwipeBackThreshold(0.3f);
    }

    /**
     * 关闭滑动返回
     */
    public void setSwipeBackFalse() {
        if (notSwipeBack) return;
        mSwipeBackHelper.setSwipeBackEnable(false);
    }

    /**
     * 是否支持滑动返回。这里在父类中默认返回 true 来支持滑动返回，如果某个界面不想支持滑动返回则重写该方法返回 false 即可
     *
     * @return
     */
    @Override
    public boolean isSupportSwipeBack() {
        return true;
    }

    /**
     * 正在滑动返回
     *
     * @param slideOffset 从 0 到 1
     */
    @Override
    public void onSwipeBackLayoutSlide(float slideOffset) {
    }

    /**
     * 没达到滑动返回的阈值，取消滑动返回动作，回到默认状态
     */
    @Override
    public void onSwipeBackLayoutCancel() {
    }

    /**
     * 滑动返回执行完毕，销毁当前 Activity
     */
    @Override
    public void onSwipeBackLayoutExecuted() {
        if (notSwipeBack) return;
        mSwipeBackHelper.swipeBackward();
    }

    private View topView;

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        if (notAddHead) {
            super.setContentView(layoutResID);
            setStatusBar();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                try {
                    topView = findViewById(R.id.include_top_view);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (topView != null) {
                    LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) topView.getLayoutParams();
                    layoutParams.height = RCAndroid.getStatusBarHeight(mContext);
                    topView.setLayoutParams(layoutParams);
                }
            }
            return;
        }

        View BaseView;
        if (TextUtils.equals(RCBaseConfig.APPTAG, RCBaseConfig.APPTAG_DONGYA)) {
            mRcHeadUtil.setToolbarLine(R.color.rcbase_white);
            BaseView = layoutInflater.inflate(R.layout.activity_base_dy, null);
        } else {
            mRcHeadUtil.setToolbarLine(R.color.toolbar_line_n3);
            BaseView = layoutInflater.inflate(R.layout.activity_base_n3, null);
        }
        ((FrameLayout) BaseView.findViewById(R.id.activity_base_main))
                .addView(layoutInflater.inflate(layoutResID, null));
        setContentView(BaseView);
        mRcHeadUtil.setLeftBack();

        setStatusBar();

    }


    /**
     * 设置沉浸式状态栏
     */
    protected void setStatusBar() {
//        //设置状态栏颜色
//        StatusBarUtil.setColor(this, getResources().getColor(R.color.rcbase_white), 0);
        if (RCBaseConfig.APPTAG.equals(RCBaseConfig.APPTAG_N3)) {
            if (notAddHead) {
                setStatusBarTranslucent();
            } else {
                StatusBarUtil.setColorForSwipeBack(this, getResources().getColor(R.color.rcbase_app_main_btn), 0);
                RCStatusColorHelper.statusBarLightMode(mContext, false);
            }
        } else {
            //为滑动返回界面设置状态栏颜色
            StatusBarUtil.setColorForSwipeBack(this, getResources().getColor(R.color.rcbase_white), 0);
//        //设置状态栏黑色字体图标
            RCStatusColorHelper.statusBarLightMode(mContext, true);
        }

    }

    protected void setStatusBarTranslucent() {
        //状态栏透明度
        StatusBarUtil.setTranslucentForImageViewInFragment(mContext, 0, null);
        RCStatusColorHelper.statusBarLightMode(mContext, false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isDestroy = true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // 正在滑动返回的时候取消返回按钮事件
        if (notSwipeBack) return;
        if (mSwipeBackHelper.isSliding()) {
            return;
        }
        finishActivity();
    }

    /**
     * 返回按钮监听
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            finishActivity();
            return false;
        }
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        RCBaseManage.getScreenManger().pushActivityList(this);
        RCUmeng.umengActivityResume(this);
    }

    @Override
    protected void onStop() {
        RCBaseManage.getScreenManger().popActivityList(this);
        super.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
        RCUmeng.umengActivityPause(this);
    }


    @Override
    public void finish() {
        super.finish();
        RCBaseManage.getScreenManger().finishActivity(this);
    }

    public void finishActivity() {
        finish();
    }


    public boolean isDestroy() {
        return isDestroy;
    }

    public void showContent(int layoutId, Fragment fragment, Bundle bundle) {
        if (!isDestroy()) {
            if (bundle != null)
                fragment.setArguments(bundle);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(layoutId, fragment, fragment.getClass().getSimpleName())
                    .commitAllowingStateLoss();
        }
    }

}

