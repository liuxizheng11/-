package com.rocedar.lib.base.manage;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.support.v4.view.WindowCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rocedar.lib.base.R;
import com.rocedar.lib.base.config.RCBaseConfig;
import com.rocedar.lib.base.network.RCRequestNetwork;
import com.rocedar.lib.base.unit.RCAndroid;
import com.rocedar.lib.base.unit.RCDrawableUtil;
import com.rocedar.lib.base.unit.RCHandler;
import com.rocedar.lib.base.unit.RCHeadUtil;
import com.rocedar.lib.base.unit.RCJavaUtil;
import com.rocedar.lib.base.unit.RCLog;
import com.rocedar.lib.base.unit.RCToast;
import com.rocedar.lib.base.unit.statuscolor.RCStatusColorHelper;

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

    //不加headView。true：不加；false：加（默认）
    private boolean notAddHead = false;
    //不允许滑动返回。true：不允许；false：允许（默认）
    private boolean notSwipeBack = RCSDKManage.hasSwipeBack;
    //滑动时头变化
    private boolean scrollChangeHead = false;


    /**
     * 不加载头
     *
     * @param notAddHead
     */
    public void setNotAddHead(boolean notAddHead) {
        this.notAddHead = notAddHead;
    }

    /**
     * 滑动时头变化
     *
     * @param scrollChangeHead
     */
    public void setScrollChangeHead(boolean scrollChangeHead) {
        this.scrollChangeHead = scrollChangeHead;
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
        RCSDKManage.getScreenManger().addActivity(this);
        isDestroy = false;
    }


    /**
     * 初始化滑动返回。在 super.onCreate(savedInstanceState) 之前调用该方法
     */
    private void initSwipeBackFinish() {
        mSwipeBackHelper = new BGASwipeBackHelper(this, this);

        // 「必须在 Application 的 onCreate 方法中执行 BGASwipeBackHelper.init 来初始化滑动返回」

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
    }

    /**
     * 关闭滑动返回
     */
    public void setSwipeBackFalse() {
        notSwipeBack = true;
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

    private TextView errorInfoTextView;

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        if (RCBaseConfig.themes != -1) {
            setTheme(RCBaseConfig.themes);
        }
        if (notAddHead) {
            super.setContentView(layoutResID);
            setSwipeBack();
        } else {
            try {
                View BaseView;
                if (scrollChangeHead) {
                    BaseView = layoutInflater.inflate(R.layout.rc_activity_base_scroll, null);
                } else {
                    BaseView = layoutInflater.inflate(R.layout.rc_activity_base, null);
                }
                errorInfoTextView = (TextView) BaseView.findViewById(R.id.activity_base_info);
                ((FrameLayout) BaseView.findViewById(R.id.activity_base_main))
                        .addView(layoutInflater.inflate(layoutResID, null));
                setContentView(BaseView);
                setSwipeBack(BaseView);
                setHeadBGColor(RCDrawableUtil.getThemeAttrColor(mContext, R.attr.RCHeadBG), scrollChangeHead ? 0 : 1);
                mRcHeadUtil.setLeftBack();
            } catch (Exception e) {
                super.setContentView(layoutResID);
                setSwipeBack();
            }
        }
    }


    /**
     * 滑动时改变头的背景颜色
     *
     * @param alpha
     */
    public void setScroll(float alpha) {
        if (!scrollChangeHead) return;
        if (alpha > 1) alpha = 1;
        setHeadBGColor(headBgColor, alpha);
    }

    private int headBgColor;


    /**
     * 设置头背景颜色
     *
     * @param color
     * @param alpha
     */
    public void setHeadBGColor(int color, float alpha) {
        headBgColor = color;
        mRcHeadUtil.setBGColor(color, alpha);
        if (topView != null) {
            topView.setBackgroundColor(RCAndroid.changeAlpha(color, alpha));
        }
        if (alpha > 0.5) {
            setStatusBar(RCJavaUtil.isDrakRGB(headBgColor));
        } else {
            setStatusBar(false);
        }
    }

    private void setSwipeBack() {
        setTransparentForWindow(mContext);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                topView = findViewById(R.id.include_top_view);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (topView != null) {
                topView.setFitsSystemWindows(false);
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) topView.getLayoutParams();
                layoutParams.height = RCAndroid.getStatusBarHeight(mContext);
                topView.setLayoutParams(layoutParams);
            }
        }
    }

    /**
     * 为滑动返回界面设置状态栏颜色
     */
    private void setSwipeBack(View view) {
        setTransparentForWindow(mContext);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                topView = view.findViewById(R.id.include_top_view);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (topView != null) {
                topView.setFitsSystemWindows(false);
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) topView.getLayoutParams();
                layoutParams.height = RCAndroid.getStatusBarHeight(mContext);
                topView.setLayoutParams(layoutParams);
            }
        }
    }


    /**
     * 设置透明
     */
    private void setTransparentForWindow(Activity activity) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
//        } else {
//            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        RCSDKManage.getScreenManger().pushActivityList(this);
    }

    @Override
    protected void onStop() {
        RCSDKManage.getScreenManger().popActivityList(this);
        super.onStop();
    }


    @Override
    public void finish() {
        super.finish();
        RCSDKManage.getScreenManger().finishActivity(this);
        RCLog.d("RCBase", "调用finish" + this.getClass().getName());
    }


    /**
     * 设置沉浸式默认状态栏颜色
     */
    private void setStatusBar(boolean isBlack) {
        RCStatusColorHelper.statusBarLightMode(mContext, isBlack);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        RCRequestNetwork.cleanDetDataDTO(mContext);
        isDestroy = true;
        RCLog.d("RCBase", "调用onDestroy" + this.getClass().getName());
    }

    @Override
    public void onBackPressed() {
        // 正在滑动返回的时候取消返回按钮事件
        if (notSwipeBack) {
            super.onBackPressed();
        } else {
            if (mSwipeBackHelper.isSliding()) {
                return;
            }
            mSwipeBackHelper.backward();
        }
    }


    public void finishActivity() {
        finish();
    }


    public boolean isDestroy() {
        return isDestroy;
    }

    public void showContent(int layoutId, Fragment fragment) {
        if (!isDestroy()) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(layoutId, fragment, fragment.getClass().getSimpleName())
                    .commitAllowingStateLoss();
        }
    }


    public void showErrorInfo(String errorInfo, boolean endGone) {
        showErrorInfo(errorInfo, -1, -1, endGone);
    }

    public void showErrorInfo(String errorInfo, int textColor, boolean endGone) {
        showErrorInfo(errorInfo, textColor, -1, endGone);
    }

    /**
     * @param errorInfo
     */
    public void showErrorInfo(String errorInfo, int textColor, int bgColor, boolean endGone) {
        if (notAddHead) {
            RCToast.Center(mContext, errorInfo);
        } else {
            errorInfoTextView.setText(errorInfo);
            if (bgColor != -1) {
                errorInfoTextView.setBackgroundColor(bgColor);
            }
            if (textColor != -1) {
                errorInfoTextView.setTextColor(textColor);
            } else {
                errorInfoTextView.setTextColor(
                        RCDrawableUtil.getThemeAttrColor(mContext, R.attr.RCDarkColor)
                );
            }
            topMoveToViewLocation(errorInfoTextView, 1500, endGone);
        }
    }


    public void goneErrorInfoTextView() {
        if (!notAddHead && errorInfoTextView != null) {
            moveToViewTop(errorInfoTextView, 1500);
        }
    }

    /**
     * 从控件的顶部移动到控件所在位置
     *
     * @param v
     * @param Duration 动画时间
     */
    private void topMoveToViewLocation(final View v, final long Duration, boolean isGone) {
        if (v.getVisibility() != View.VISIBLE) {
            v.setVisibility(View.VISIBLE);
            TranslateAnimation mShowAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                    Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                    -1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
            mShowAction.setDuration(Duration);
            v.clearAnimation();
            v.setAnimation(mShowAction);
        }
        if (isGone)
            mRcHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    moveToViewTop(v, Duration);
                }
            }, 2000 + Duration);
    }

    private boolean ismHiddenActionstart = false;

    /**
     * 从控件所在位置移动到控件的顶部
     *
     * @param v
     * @param Duration 动画时间
     */
    public void moveToViewTop(final View v, long Duration) {
        if (v.getVisibility() != View.VISIBLE)
            return;
        if (ismHiddenActionstart)
            return;
        TranslateAnimation mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, -1.0f);
        mHiddenAction.setDuration(Duration);
        v.clearAnimation();
        v.setAnimation(mHiddenAction);
        mHiddenAction.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                ismHiddenActionstart = true;
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                v.setVisibility(View.GONE);
                ismHiddenActionstart = false;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }


    public void showNetworkErrorInfo() {
        if (notAddHead) return;
        findViewById(R.id.activity_base_main_error_layout).setVisibility(View.VISIBLE);
        findViewById(R.id.activity_base_main_error_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RCRequestNetwork.refreshGetData(mContext);
            }
        });

    }

    public void goneNetworkErrorInfo() {
        if (notAddHead) return;
        findViewById(R.id.activity_base_main_error_layout).setVisibility(View.GONE);
    }
}

