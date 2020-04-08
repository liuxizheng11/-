package com.rocedar.deviceplatform.app.behavior.chart;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import com.rocedar.base.RCDateUtil;
import com.rocedar.deviceplatform.R;
import com.rocedar.deviceplatform.request.impl.RCBehaviorLibraryImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名称：FangZhou2.1
 * <p>
 * 作者：phj
 * 日期：2017/8/21 下午2:37
 * 版本：V2.2.00
 * 描述：
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public class LineChat extends View {


    public static class LineChatDataDTO {

        public LineChatDataDTO() {
        }

        public LineChatDataDTO(String date, double data) {
            this.date = date;
            this.data = data;
            this.dataShowText = (int) (data / 60) + "小时" + (int) (data % 60) + "分";
        }

        //日期yyyyMMddHHmmss
        public String date;
        //数据
        public double data;
        //用于显示的文字
        public String dataShowText;
        //图表选中颜色
        public int select_theme;
        //图表选中图片
        public int select_image;
        //图表未选中图片
        public int image;

        public int getSelect_theme() {
            return select_theme;
        }

        public void setSelect_theme(int select_theme) {
            this.select_theme = select_theme;
        }

        public int getSelect_image() {
            return select_image;
        }

        public void setSelect_image(int select_image) {
            this.select_image = select_image;
        }

        public int getImage() {
            return image;
        }

        public void setImage(int image) {
            this.image = image;
        }

        public String getDataShowText() {
            return dataShowText;
        }

        public void setDataShowText(String dataShowText) {
            this.dataShowText = dataShowText;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public double getData() {
            return data;
        }

        public void setData(double data) {
            this.data = data;
        }

        public void setData(float data) {
            this.data = data;
        }

        public void setData(int data) {
            this.data = data;
        }

        public void setData(long data) {
            this.data = data;
        }

        public void setData(String data) {
            try {
                this.data = Double.parseDouble(data);
            } catch (NumberFormatException e) {
                this.data = 0.0;
            }
        }

    }


    public LineChat(Context context) {
        super(context);
        initialize();
    }

    public LineChat(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public LineChat(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
    }

    //选中的点颜色
    private Bitmap bitmapDotChoose;
    //未选中的点颜色
    private Bitmap bitmapDotNone;
    //最大值/选中的点显示的气泡
    private Bitmap bitmapBubbleDown;
    //最小值显示的气泡
    private Bitmap bitmapBubbleUp;
    //底部虚线
    private Paint bottomLinePaint;
    //数据背景虚线
    private Paint bgLinePaint;
    //底部文字背景色
    private Paint bottomTextBGPaint;
    //底部文字
    private TextPaint bottomTextPaint;
    private int bottomTextChooseColor = 0xFF888B90;
    private int bottomTextNoneColor = Color.WHITE;
    //7天数据文字
    private TextPaint dataTextPaint;
    //气泡数据文字
    private TextPaint bubbleTextPaint;
    //线
    private Paint linePaint;


    //左边边距
    private float leftPadding;
    //右边边距
    private float rightPadding;

    private void initialize() {
        Resources res = getResources();
//        bitmapDotChoose = BitmapFactory.decodeResource(res, R.mipmap.ic_chat_dot_select);
//        bitmapDotNone = BitmapFactory.decodeResource(res, R.mipmap.ic_chat_dot_none);
//        bitmapBubbleUp = BitmapFactory.decodeResource(res, R.mipmap.ic_chat_bubble_up);
//        bitmapBubbleDown = BitmapFactory.decodeResource(res, R.mipmap.ic_chat_bubble_down);
        //底部虚线
        bottomLinePaint = new Paint();
        bottomLinePaint.setAntiAlias(true);
        bottomLinePaint.setColor(0xCA01B8A1);
        bottomLinePaint.setStyle(Paint.Style.STROKE);
        bottomLinePaint.setStrokeWidth(dp2px(1.0f));
        DashPathEffect effectBottomLine = new DashPathEffect(new float[]{8, 8, 8, 8}, 0);
        bottomLinePaint.setPathEffect(effectBottomLine);
        //x轴虚线
        bgLinePaint = new Paint();
        bgLinePaint.setAntiAlias(true);
        bgLinePaint.setColor(0x7F039380);
        bgLinePaint.setStyle(Paint.Style.STROKE);
        bgLinePaint.setStrokeWidth(2);
        DashPathEffect effectBgLine = new DashPathEffect(new float[]{8, 8, 8, 8}, 0);
        bgLinePaint.setPathEffect(effectBgLine);
        //底部文字背景
        bottomTextBGPaint = new Paint();
        bottomTextBGPaint.setColor(0xFF4D4E64);
        bottomTextBGPaint.setStyle(Paint.Style.FILL);
        //底部文字
        bottomTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        bottomTextPaint.setTextAlign(Paint.Align.CENTER);
        bottomTextPaint.setTextSize(dp2px(12));
        //7天数据文字
        dataTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        dataTextPaint.setTextAlign(Paint.Align.CENTER);
        dataTextPaint.setTextSize(dp2px(9));
        dataTextPaint.setColor(0x99DFDFDF);
        //7天数据文字（气泡）
        bubbleTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        bubbleTextPaint.setTextAlign(Paint.Align.CENTER);
        bubbleTextPaint.setTextSize(dp2px(10.5f));
        bubbleTextPaint.setColor(Color.WHITE);
        //左边边距
        leftPadding = dp2px(29);
        //右边边距
        rightPadding = dp2px(29);
        //折线样式
        linePaint = new Paint();
        linePaint.setAntiAlias(true);
        linePaint.setColor(0xFF2D7671);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(dp2px(1.5f));
    }


    //数据点
    private List<LineChatDataDTO> mDataList = new ArrayList<>();
    //是否可以选中
    private boolean hasChoose;
    //数据周期
    private String date_period;

    //最大的数据值
    private double MaxData = -1;
    //最大的数据的索引
    private int MaxDataIndex = 0;
    //最小的数据值
    private double MinData = Double.MAX_VALUE;
    //最小的数据的索引
    private int MinDataIndex = 0;

    /**
     * 设置数据
     *
     * @param dataList  数据
     * @param hasChoose 是否可以选中
     * @param period    周期 日、周、月
     */
    public void setDataList(List<LineChatDataDTO> dataList, String period, boolean hasChoose) {
        setDataList(dataList, period, hasChoose, true);
    }

    /**
     * 设置数据
     *
     * @param dataList  数据
     * @param hasChoose 是否可以选中
     * @param period    周期 日、周、月
     */
    public void setDataList(List<LineChatDataDTO> dataList, String period, boolean hasChoose, boolean hasDefaultChoose) {
        this.hasChoose = hasChoose;
        MaxDataIndex = 0;
        MinDataIndex = 0;
        MaxData = -1;
        date_period = period;
        MinData = Double.MAX_VALUE;
        mDataList.clear();
        mDataList.addAll(dataList);
        for (int i = 0; i < mDataList.size(); i++) {
            if (mDataList.get(i).getData() <= 0) {
                mDataList.get(i).setData(0);
            }
            MaxData = Math.max(mDataList.get(i).getData(), MaxData);
            MinData = Math.min(mDataList.get(i).getData(), MinData);
            if (mDataList.get(i).getData() == MaxData && mDataList.get(i).getData() > 0) {
                MaxDataIndex = i;
            }
            if (mDataList.get(i).getData() == MinData) {
                MinDataIndex = i;
            }
        }
        if (MaxData <= 10) {
            MaxData = 10;
        }
        if (hasChoose && hasDefaultChoose) {
            mChooseIndex = mDataList.size() - 1;
        }
        postInvalidate();
    }

    public void setmChooseIndex(int mChooseIndex) {
        this.mChooseIndex = mChooseIndex;
        postInvalidate();
    }

    private int mHeight, mWidth;

    private int mChooseIndex = -1;

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (w != oldw || h != oldh) {
            mHeight = h;
            mWidth = w;
        }
    }

    private Paint mPanint;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //总共有多少项
        int itemCount = mDataList.size();
        if (itemCount == 0) return;
        //画图时Y坐标使用索引
        int yIndex = mHeight;
        //画图⬇
        Path path = new Path();
        //第一步，画底部虚线
        //底部虚线的位置（折线的基线）
        float bottomLineYTemp = 0;
        float bottomLineYTextTemp = 0;
        if (hasChoose) {
            path.moveTo(0, mHeight - dp2px(40));
            path.lineTo(mWidth, mHeight - dp2px(40));
            bottomLineYTemp = mHeight - dp2px(40);
            bottomLineYTextTemp = mHeight - dp2px(40) / 2;
        } else {
            path.moveTo(0, mHeight - dp2px(30));
            path.lineTo(mWidth, mHeight - dp2px(30));
            bottomLineYTemp = mHeight - dp2px(30);
            bottomLineYTextTemp = mHeight - dp2px(30) / 2;
        }
        canvas.drawPath(path, bottomLinePaint);
        //总宽度
        int lineAll = (int) (mWidth - leftPadding - rightPadding);
        //计算每一项的宽
        float temp = lineAll / (itemCount - 1);
        //第二步：画背景虚线和底部轴坐标
        for (int i = 0; i < itemCount; i++) {
            if (i == 0) {
                //画背景虚线
                if (itemCount == 7) {
                    path.moveTo(leftPadding, bottomLineYTemp);
                    path.lineTo(leftPadding, 0);
                    canvas.drawPath(path, bgLinePaint);
                }
                //画底部轴数据
                drawBottomText(canvas, i, leftPadding, bottomLineYTextTemp);
            } else {
                if (itemCount == 7) {
                    //画背景虚线
                    path.moveTo(leftPadding + temp * i, bottomLineYTemp);
                    path.lineTo(leftPadding + temp * i, 0);
                    canvas.drawPath(path, bgLinePaint);
                    //画底部轴数据
                    drawBottomText(canvas, i, leftPadding + temp * i, bottomLineYTextTemp);
                } else {
                    if (i == itemCount - 1) {
                        drawBottomText(canvas, i, leftPadding + temp * i, bottomLineYTextTemp);
                    } else {
                        if (i % (itemCount / 4) == 0) {
                            if (itemCount - i > (itemCount / 8)) {
                                // 画底部轴数据
                                drawBottomText(canvas, i, leftPadding + temp * i, bottomLineYTextTemp);
                            }
                        }
                    }
                }
            }
        }
        //折线的最大高度(折线的占位)
        float allHeightTemp = bottomLineYTemp - bitmapBubbleUp.getHeight() - dp2px(5);
        //第三步：画折线
        for (int i = 0; i < itemCount; i++) {
            if (i != 0) {
                float dataYTemp = bottomLineYTemp - (float) ((allHeightTemp / MaxData) * mDataList.get(i).getData());
                float dataYLastTemp = bottomLineYTemp - (float) ((allHeightTemp / MaxData) * mDataList.get(i - 1).getData());
                canvas.drawLine(leftPadding + temp * (i - 1), dataYLastTemp, leftPadding + temp * i, dataYTemp, linePaint);
            }
        }
        //第四步：画点
        for (int i = 0; i < itemCount; i++) {
            float tempDotX = leftPadding + temp * i;
            float tempDotY = 0;
            if ((hasChoose && mChooseIndex == i) || (!hasChoose && i == MaxDataIndex) || (!hasChoose && i == MinDataIndex)) {
                tempDotX = tempDotX - bitmapDotChoose.getWidth() / 2;
                tempDotY = bottomLineYTemp - (float) ((allHeightTemp / MaxData) * mDataList.get(i).getData())
                        - bitmapDotChoose.getHeight() / 2;
                canvas.drawBitmap(bitmapDotChoose, tempDotX, tempDotY, linePaint);
            } else {
                tempDotX = tempDotX - bitmapDotNone.getWidth() / 2;
                tempDotY = bottomLineYTemp - (float) ((allHeightTemp / MaxData) * mDataList.get(i).getData())
                        - bitmapDotNone.getHeight() / 2;
                canvas.drawBitmap(bitmapDotNone, tempDotX, tempDotY, linePaint);
            }
            //画数据
        }
        //第五步：画数据文字
        //第六步：画气泡
        if (hasChoose) {
            for (int i = 0; i < itemCount; i++) {
                if (mChooseIndex == i) {
                    //选中的气泡
                    float tempDotX = leftPadding + temp * mChooseIndex - bitmapBubbleUp.getWidth() / 2;
                    float tempDotY = bottomLineYTemp - (float) ((allHeightTemp / MaxData) * mDataList.get(mChooseIndex).getData())
                            - bitmapDotChoose.getHeight() / 2 - bitmapBubbleUp.getHeight() - dp2px(2);
                    canvas.drawBitmap(bitmapBubbleUp, tempDotX, tempDotY, linePaint);
                    //画气泡文字
                    canvas.drawText(mDataList.get(i).getDataShowText()
                            , 0, mDataList.get(i).getDataShowText().length(),
                            leftPadding + temp * i,
                            bottomLineYTemp - (float) ((allHeightTemp / MaxData) * mDataList.get(i).getData())
                                    - bitmapDotChoose.getHeight() / 2 - bitmapBubbleUp.getHeight() / 2
                            , bubbleTextPaint);
                }
//                else {
//                    //没有选中的文字
//                    float tempNoneTextX = leftPadding + temp * i + bitmapDotNone.getWidth() / 2 +
//                            dataTextPaint.measureText(mDataList.get(i).getDataShowText()) / 2 + dp2px(2);
//                    float tempNoneTextY = bottomLineYTemp - (float) ((allHeightTemp / MaxData) * mDataList.get(i).getData())
//                            + (Math.abs(bottomTextPaint.ascent()) - Math.abs(bottomTextPaint.descent())) / 2;
//                    canvas.drawText(mDataList.get(i).getDataShowText()
//                            , 0, mDataList.get(i).getDataShowText().length(), tempNoneTextX
//                            , tempNoneTextY, dataTextPaint);
//                }
            }
        } else {
            //max 气泡
            float tempDotX = leftPadding + temp * MaxDataIndex - bitmapBubbleUp.getWidth() / 2;
            float tempDotY = bottomLineYTemp - (float) ((allHeightTemp / MaxData) * mDataList.get(MaxDataIndex).getData())
                    - bitmapDotChoose.getHeight() / 2 - bitmapBubbleUp.getHeight() - dp2px(2);
            canvas.drawBitmap(bitmapBubbleUp, tempDotX, tempDotY, linePaint);
            canvas.drawText(mDataList.get(MaxDataIndex).getDataShowText()
                    , 0, mDataList.get(MaxDataIndex).getDataShowText().length(),
                    leftPadding + temp * MaxDataIndex,
                    bottomLineYTemp - (float) ((allHeightTemp / MaxData) * mDataList.get(MaxDataIndex).getData())
                            - bitmapDotChoose.getHeight() / 2 - bitmapBubbleUp.getHeight() / 2
                    , bubbleTextPaint);
            //min 气泡
            //判断底部的高度是否足够
            if ((float) ((allHeightTemp / MaxData) * mDataList.get(MinDataIndex).getData()) >
                    bitmapBubbleDown.getHeight() + dp2px(2)) {
                tempDotX = leftPadding + temp * MinDataIndex - bitmapBubbleDown.getWidth() / 2;
                tempDotY = bottomLineYTemp - (float) ((allHeightTemp / MaxData) * mDataList.get(MinDataIndex).getData())
                        + bitmapDotChoose.getHeight() / 2 + dp2px(2);
                canvas.drawBitmap(bitmapBubbleDown, tempDotX, tempDotY, linePaint);
                tempDotY = bottomLineYTemp - (float) ((allHeightTemp / MaxData) * mDataList.get(MinDataIndex).getData())
                        + bitmapDotChoose.getHeight() / 2 + bitmapBubbleDown.getHeight();
                canvas.drawText(mDataList.get(MinDataIndex).getDataShowText(), 0, mDataList.get(MinDataIndex).getDataShowText().length(),
                        leftPadding + temp * MinDataIndex, tempDotY, bubbleTextPaint);
            } else {
                tempDotX = leftPadding + temp * MinDataIndex - bitmapBubbleUp.getWidth() / 2;
                tempDotY = bottomLineYTemp - (float) ((allHeightTemp / MaxData) * mDataList.get(MinDataIndex).getData())
                        - bitmapDotChoose.getHeight() / 2 - bitmapBubbleUp.getHeight() - dp2px(2);
                canvas.drawBitmap(bitmapBubbleUp, tempDotX, tempDotY, linePaint);
                canvas.drawText(mDataList.get(MinDataIndex).getDataShowText(), 0, mDataList.get(MinDataIndex).getDataShowText().length(),
                        leftPadding + temp * MinDataIndex,
                        bottomLineYTemp - (float) ((allHeightTemp / MaxData) * mDataList.get(MinDataIndex).getData())
                                - bitmapDotChoose.getHeight() / 2 - bitmapBubbleUp.getHeight() / 2
                        , bubbleTextPaint);
            }
        }

    }


    private void drawBottomText(Canvas canvas, int index, float centreX, float centreY) {
        String date = "";
//        if (mDataList.size() == 12) {
//            date = RCDateUtil.formatServiceTime(mDataList.get(index).getDate(), "MM月");
//        } else {
//            if (!mDataList.get(index).getDate().equals(RCDateUtil.getFormatToday())) {
//                date = RCDateUtil.formatServiceTime(mDataList.get(index).getDate(), "MM/dd");
//            }
//        }
        //判断当前 是 日、周、月
        if (date_period.equals(RCBehaviorLibraryImpl.DAY)) {
            if (!mDataList.get(index).getDate().equals(RCDateUtil.getFormatToday())) {
                date = RCDateUtil.formatServiceTime(mDataList.get(index).getDate(), "MM/dd");
            } else {
                date = getContext().getString(R.string.rcdevice_record_today);
            }
        } else if (date_period.equals(RCBehaviorLibraryImpl.WEEK)) {
            String time = RCDateUtil.formatServiceTime(mDataList.get(index).getDate(), "yyyyMMdd");
            if (!RCDateUtil.getThisWeekMonday(time).equals(RCDateUtil.getFirstMondayString())) {
                date = RCDateUtil.formatServiceTime(mDataList.get(index).getDate(), "MM/dd");
            } else {
                date = getContext().getString(R.string.rcdevice_record_this_week);
            }
        } else {
            if (!RCDateUtil.formatServiceTime(mDataList.get(index).getDate() + "", "yyyyMM")
                    .equals(RCDateUtil.getFormatNow("yyyyMM"))) {
                date = RCDateUtil.formatServiceTime(mDataList.get(index).getDate(), "M月");
            } else {
                date = getContext().getString(R.string.rcdevice_record_this_month);
            }
        }

        if (mChooseIndex == index) {
            if (hasChoose) {
                //画背景色
                canvas.drawRect(centreX - dp2px(20), centreY - dp2px(19), centreX + dp2px(20), centreY + dp2px(20),
                        bottomTextBGPaint);
                bottomTextPaint.setColor(bottomTextNoneColor);
            } else {
                bottomTextPaint.setColor(bottomTextChooseColor);
            }
        } else {
            bottomTextPaint.setColor(bottomTextChooseColor);
        }
        canvas.drawText(date, 0, date.length(), centreX
                , centreY + (Math.abs(bottomTextPaint.ascent()) - Math.abs(bottomTextPaint.descent())) / 2, bottomTextPaint);
    }

    private float downX = 0.0f, downY = 0 / 0f;
    private boolean isClick = false;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (hasChoose) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_MOVE:
                    if (Math.abs(event.getX() - downX) > 10 ||
                            Math.abs(event.getY() - downY) > 10) {
                        isClick = false;
                        break;
                    }
                    return true;
                case MotionEvent.ACTION_DOWN:
                    //获取屏幕上点击的坐标
                    downX = event.getX();
                    downY = event.getY();
                    isClick = true;
                    return true;
                case MotionEvent.ACTION_UP:
                    if (!isClick) break;
                    int lineAll = (int) (mWidth - leftPadding - rightPadding);
                    //计算每一项的宽
                    float temp = lineAll / (mDataList.size() - 1);
                    for (int i = 0; i < mDataList.size(); i++) {
                        if (downX > leftPadding + temp * (i) - dp2px(20) && downX < leftPadding + temp * (i) + dp2px(20)) {
                            if (mChatClickListener != null)
                                mChatClickListener.onClick(i);
                            mChooseIndex = i;
                            invalidate();
                            return true;
                        }
                    }
                    return true;
            }
        }
        return super.onTouchEvent(event);
    }


    private float dp2px(float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }


    private ChatClickListener mChatClickListener;

    public void setmChatClickListener(ChatClickListener mChatClickListener) {
        this.mChatClickListener = mChatClickListener;
    }

    public interface ChatClickListener {

        void onClick(int i);

    }
}
