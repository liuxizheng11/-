package com.rocedar.lib.base.view;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v4.view.ViewCompat;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Scroller;

import com.rocedar.lib.base.R;
import com.rocedar.lib.base.unit.RCAndroid;
import com.rocedar.lib.base.unit.RCDrawableUtil;
import com.rocedar.lib.base.unit.RCJavaUtil;
import com.rocedar.lib.base.unit.RCLog;


/**
 * 项目名称：DongYa3.0
 * <p>
 * 作者：phj
 * 日期：2017/11/21 下午3:29
 * 版本：V3.5.00
 * 描述：刻度尺图表
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public class RCScaleChat extends View implements GestureDetector.OnGestureListener {


    private RectF mContentRectF;

    //最大值，最小值，默认值
    private int mMaxNumber = 300, mMinNumber = 10;
    private float mDefaultNumber = 60;
    //刻度粒度
    private float numberMultiply = 1.0f;
    //数据单位
    private String unit = "";


    /* 灰色大刻度每项的高*/
    private float mBigScaleHight;
    /* 灰色大刻度每项的宽*/
    private float mBigScaleWight;
    /* 灰色小刻度每项的高*/
    private float mSmallScaleHight;
    /* 灰色小刻度每项的宽*/
    private float mSmallScaleWight;
    /* 每个刻度之间的间隙*/
    private float mItemInterval;


    /* 屏幕一半的宽度*/
    private float mScreenHalfWight;
    /* 总共有多少个项*/
    private int mCountItem = 0;
    /* top间隙*/
    private float mTopInterval = 0;

    private float startXIndex = 0;


    /* 布局占位总宽*/
    private int mViewWidth = 0;
    private int mViewHeight = 0;
    /* 画布总宽*/
    private int mWidth = 0;

    private Bitmap centerImage;

    private int imageWidth, imageHeight;

    private Scroller mScroller = null;


    /* */
    private Paint linePaint = new Paint();

    private TextPaint mTextPaint;

    public RCScaleChat(Context context) {
        super(context);
        init(context, null);
    }

    public RCScaleChat(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public RCScaleChat(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    protected void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RCScaleChat);
            mMaxNumber = a.getInteger(R.styleable.RCScaleChat_rc_max_number, mMaxNumber);
            mMinNumber = a.getInteger(R.styleable.RCScaleChat_rc_min_number, mMinNumber);
            mDefaultNumber = a.getFloat(R.styleable.RCScaleChat_rc_min_number, mDefaultNumber);
            int numberMultiplyTemp = a.getInt(R.styleable.RCScaleChat_rc_multiply, 1);
            if (numberMultiplyTemp == 1) {
                numberMultiply = 1;
            } else if (numberMultiplyTemp == 2) {
                numberMultiply = 0.1f;
            } else if (numberMultiplyTemp == 3) {
                numberMultiply = 10;
            }
            unit = a.getString(R.styleable.RCScaleChat_rc_unit);
        }
        if (numberMultiply == 10 && (mMaxNumber - mMinNumber) % 10 != 0) {
            mCountItem = (int) ((mMaxNumber - mMinNumber) / numberMultiply) + 1;
        } else {
            mCountItem = (int) ((mMaxNumber - mMinNumber) / numberMultiply);
        }
        showNumber = mDefaultNumber / numberMultiply;

        mGestureDetector = new GestureDetector(context, this);
        //初始化Scroller实例
        mScroller = new Scroller(context);

        Resources res = getResources();
        centerImage = BitmapFactory.decodeResource(res, R.mipmap.rc_ic_scalechat_selected);
        imageWidth = centerImage.getWidth();
        imageHeight = centerImage.getHeight();

        mContentRectF = new RectF();

        float density = getResources().getDisplayMetrics().density;
        mBigScaleWight = (float) (density * 3.5);
        mBigScaleHight = (float) (density * 14);
        mSmallScaleWight = (float) (density * 0.67);
        mSmallScaleHight = (float) (density * 10);
        mItemInterval = density * 4;


        mTopInterval = density * 5;

        linePaint.setAntiAlias(true);
        linePaint.setColor(0xFFebebeb);
        linePaint.setStyle(Paint.Style.FILL);
        linePaint.setStrokeWidth(RCAndroid.dip2px(getContext(), 3.5f));

        mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setTextSize(density * 12);
        mTextPaint.setFakeBoldText(true);
        mTextPaint.setColor(RCDrawableUtil.getThemeAttrColor(getContext(), R.attr.RCDarkColor));

//        selectIndex(mDefaultNumber);
    }

    public void setData(int mMaxNumber, int mMinNumber, int mDefaultNumber, int numberMultiplyTemp) {
        this.mMaxNumber = mMaxNumber;
        this.mMinNumber = mMinNumber;
        this.mDefaultNumber = mDefaultNumber;
        if (numberMultiplyTemp == 1) {
            numberMultiply = 1;
        } else if (numberMultiplyTemp == 2) {
            numberMultiply = 0.1f;
        } else if (numberMultiplyTemp == 3) {
            numberMultiply = 10;
        }
        if (numberMultiply == 10 && (mMaxNumber - mMinNumber) % 10 != 0) {
            mCountItem = (int) ((mMaxNumber - mMinNumber) / numberMultiply) + 1;
        } else {
            mCountItem = (int) ((mMaxNumber - mMinNumber) / numberMultiply);
        }
        showNumber = mDefaultNumber / numberMultiply;
        mWidth = (int) ((mCountItem / 10 + 1) * mBigScaleWight +
                (mCountItem - (mCountItem / 10 + 1)) * mSmallScaleWight
                + (mCountItem - 1) * mItemInterval) + mViewWidth;

        float temp = mBigScaleWight + 9 * mSmallScaleWight + 10 * mItemInterval;
        startXIndex = Math.round(mScreenHalfWight / temp) * temp;

        //设置画布
        mContentRectF.set(0, 0, mWidth, mViewHeight);
        selectIndex(mDefaultNumber);
    }


    public void setCenterImage(int imageRes) {
        Resources res = getResources();
        centerImage = BitmapFactory.decodeResource(res, imageRes);
        imageWidth = centerImage.getWidth();
        imageHeight = centerImage.getHeight();
        invalidate();
    }


    // 由父视图调用用来请求子视图根据偏移值 mScrollX,mScrollY重新绘制
    @Override
    public void computeScroll() {
        // 如果返回true，表示动画还没有结束
        // 因为前面startScroll，所以只有在startScroll完成时 才会为false
        if (mScroller.computeScrollOffset()) {
            RCLog.e("ScaleChat", mScroller.getCurrX() + "======" + mScroller.getCurrY());
            // 产生了动画效果，根据当前值 每次滚动一点
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            RCLog.e("ScaleChat", "### getleft is " + getLeft() + " ### getRight is " + getRight());
            //此时同样也需要刷新View ，否则效果可能有误差
            postInvalidate();
        } else
            RCLog.i("ScaleChat", "have done the scoller -----");
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        RCLog.e("scaleChat", "onSizeChanged");
        if (w != oldw || h != oldh) {
            mViewWidth = w;
            mViewHeight = h;
            //一屏显示71个,计算每项的宽
//            mItemWight = w / 71;
            //总项数=刻度的项加左右填充21个
//            mCountItem += 71;
            //总宽度
            mWidth = (int) ((mCountItem / 10 + 1) * mBigScaleWight +
                    (mCountItem - (mCountItem / 10 + 1)) * mSmallScaleWight
                    + (mCountItem - 1) * mItemInterval) + w;
            //半屏幕的宽
            mScreenHalfWight = w / 2.f;

            float temp = mBigScaleWight + 9 * mSmallScaleWight + 10 * mItemInterval;
            startXIndex = Math.round(mScreenHalfWight / temp) * temp;


            //设置画布
            mContentRectF.set(0, 0, mWidth, h);
            RCLog.e("scaleChat", "总宽度：" + mWidth);
            selectIndex(mDefaultNumber);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int tempX = 0;
        int tempCount = 0;
        while (tempX < mWidth) {
            RectF r2 = new RectF();                           //RectF对象

            linePaint.setColor(0xFFe0e0e0);

            if (tempCount % 10 == 0) {
                r2 = new RectF();                           //RectF对象
                r2.left = tempX;//左边
                r2.top = mTopInterval;//上边
                r2.right = tempX + mBigScaleWight;//右边
                r2.bottom = mTopInterval + mBigScaleHight;//下边
                canvas.drawRect(r2, linePaint);
                tempX += mBigScaleWight + mItemInterval;
            } else {
                r2 = new RectF();
                r2.left = tempX;//左边
                r2.top = mTopInterval;//上边
                r2.right = tempX + mSmallScaleWight;//右边
                r2.bottom = mTopInterval + mSmallScaleHight;//下边
                canvas.drawRect(r2, linePaint);
                tempX += mSmallScaleWight + mItemInterval;
            }

            tempCount++;
        }
        int x = (int) (getScrollX() + startXIndex - imageWidth / 2 + mSmallScaleWight * 2);
        canvas.drawBitmap(centerImage, x, 0, linePaint);
        String info;
        if (showNumber == (int) showNumber)
            info = (int) showNumber + "";
        else
            info = RCJavaUtil.formatBigDecimalUP(showNumber, 1) + "";
        canvas.drawText(info, 0, info.length(),
                x + mTopInterval * 6 / 5, imageHeight + mTopInterval * 2, mTextPaint);
    }

    private float tempSelectX;

    public void selectIndex(float index) {
        mDefaultNumber = index;
//        if (startXIndex <= 0) {
//            RCToast.Center(getContext(), "Null");
//            return;
//        } else {
//            RCToast.Center(getContext(), "index:" + index);
//        }
        int tempIndex = (int) ((index - mMinNumber) / numberMultiply);
        float temp = mBigScaleWight + 9 * mSmallScaleWight + 10 * mItemInterval;

        int tempXB = tempIndex / 10;
        int tempXS = tempIndex % 10;
        tempSelectX = temp * tempXB;
        if (tempXS > 0) {
            tempSelectX += (mBigScaleWight + (tempXS - 1) * mSmallScaleWight + tempXS * mItemInterval);
        }
        showNumber = index;
        post(new Runnable() {
            @Override
            public void run() {
                scrollTo((int) tempSelectX, 0);
                postInvalidate();
            }
        });
    }

    private float scrollerCenter(int scrollerX, boolean isup) {
        float temp = mBigScaleWight + 9 * mSmallScaleWight + 10 * mItemInterval;
        float tempX = scrollerX;
        int tempXB = (int) (tempX / temp);
        tempX = tempX - temp * tempXB - mBigScaleWight;
        int tempXS = Math.round(tempX / (mSmallScaleWight + mItemInterval));
        float choose = (tempXB * 10 + tempXS) * numberMultiply + mMinNumber;
        if (isup)
            selectIndex(choose);
        if (choose < mMinNumber) {
            choose = mMinNumber;
        }
        if (choose > mMaxNumber) {
            choose = mMaxNumber;
        }
        return choose;

    }

    private float lastPointX;
    private float movingLeftThisTime = 0.0f;

    private float showNumber;

    private GestureDetector mGestureDetector;


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mCountItem == 0 || !isEnabled()) {
            return false;
        }
//        boolean ret = mGestureDetector.onTouchEvent(event);
//        return ret;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastPointX = event.getRawX();
                break;
            case MotionEvent.ACTION_MOVE:
                float x = event.getRawX();
                movingLeftThisTime = lastPointX - x;
                lastPointX = x;
                int sX = (int) (getScrollX() + movingLeftThisTime);
                if (sX < 0) {
                    sX = 0;
                } else if (getScrollX() + movingLeftThisTime > mWidth - mViewWidth) {
                    sX = mWidth - mViewWidth;
                }
                scrollTo(sX, getScrollY());
                reshowNumber(false);
                break;
            case MotionEvent.ACTION_UP:
                reshowNumber(true);
                break;

            default:
                return super.onTouchEvent(event);
        }
        return true;
    }


    private void reshowNumber(boolean isup) {
        showNumber = scrollerCenter(getScrollX(), isup);
        if (scaleChatChooseListener != null) {
            if (showNumber == (int) showNumber) {
                scaleChatChooseListener.chooseData((int) showNumber + " " + unit, (int) showNumber);
            } else {
                scaleChatChooseListener.chooseData(RCJavaUtil.formatBigDecimalUP(showNumber, 1)
                        + " " + unit, RCJavaUtil.formatBigDecimalUP(showNumber, 1));
//                        + " " + unit, (int) showNumber);
            }
        }
        postInvalidate();
    }


    @Override
    public boolean onDown(MotionEvent e) {
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        float dis = distanceX;
        float scrollX = getScrollX();
//        movingLeftThisTime = lastPointX - x;
//        lastPointX = x;
//        int sX = (int) (getScrollX() + movingLeftThisTime);
//        if (sX < 0) {
//            sX = 0;
//        } else if (getScrollX() + movingLeftThisTime > mWidth - mViewWidth) {
//            sX = mWidth - mViewWidth;
//        }
//        scrollTo(sX, 0);
//        reshowNumber();

        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }


    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        float scrollX = getScrollX();
        if (scrollX < mViewWidth / 2 || scrollX > mContentRectF.width() - mViewWidth / 2) {
            return false;
        } else {
//            mFling = true;
            mScroller.fling(getScrollX(), getScrollY(),
                    (int) velocityX, (int) velocityY,
                    (int) startXIndex,
                    (int) (mContentRectF.width() - mViewWidth),
                    0, 0);
            ViewCompat.postInvalidateOnAnimation(this);
            return true;
        }

    }

    private ScaleChatChooseListener scaleChatChooseListener;

    public void setScaleChatChooseListener(ScaleChatChooseListener scaleChatChooseListener) {
        this.scaleChatChooseListener = scaleChatChooseListener;
    }

    public interface ScaleChatChooseListener {

        void chooseData(String data, float number);

    }

}
