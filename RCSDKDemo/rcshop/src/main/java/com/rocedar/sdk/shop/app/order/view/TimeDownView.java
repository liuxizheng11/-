package com.rocedar.sdk.shop.app.order.view;

import android.content.Context;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rocedar.sdk.shop.R;


public class TimeDownView extends RelativeLayout implements Runnable {

	private TextView  timedown_hour, timedown_min,
			timedown_second;
	private Paint mPaint; // 画笔,包含了画几何图形、文本等的样式和颜色信息
	private long[] times;
	private long  mhour, mmin, msecond;// 小时，分钟，秒
	private boolean run = false; // 是否启动了

	public TimeDownView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}

	public TimeDownView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	public TimeDownView(Context context) {
		super(context);
		initView(context);
	}

	private void initView(Context context) {
		View.inflate(context, R.layout.rc_view_timedown, TimeDownView.this);
		timedown_hour =  this.findViewById(R.id.rc_view_timedown_hour);
		timedown_min =  this.findViewById(R.id.rc_view_timedown_min);
		timedown_second =  this.findViewById(R.id.rc_view_timedown_second);

		mPaint = new Paint();
	}

	public long[] getTimes() {
		return times;
	}

	public void setTimes(long[] times) {
		this.times = times;
		mhour = times[0];
		mmin = times[1];
		msecond = times[2];
	}

	/**
	 * 倒计时计算
	 */
	private void ComputeTime() {
		msecond--;
		if (msecond < 0) {
			mmin--;
			msecond = 59;
			if (mmin < 0) {
				mmin = 59;
				mhour--;
				if (mhour < 0) {
					// 倒计时结束
					mhour = 59;
				}
			}
		}
	}

	public boolean isRun() {
		return run;
	}

	public void setRun(boolean run) {
		this.run = run;
	}

	@Override
	public void run() {
		// 标示已经启动
		run = true;
		ComputeTime();
		String strTime =  mhour + ":" + mmin + ":"
				+ msecond ;
//		this.setText(strTime);
		timedown_hour.setText(mhour+":");
		timedown_min.setText(mmin+":");
		timedown_second.setText(msecond+"");


        if (mhour >=0 && mhour <= 9) {
            timedown_hour.setText("0" + mhour);
        } else {
            timedown_hour.setText(mhour + "");
        }
        if (mmin >= 0 && mmin <= 9) {
            timedown_min.setText("0" + mmin);
        } else {
            timedown_min.setText(mmin + "");
        }
        if (msecond >= 0 && msecond <= 9) {
            timedown_second.setText("0" + msecond);
        } else {
            timedown_second.setText(msecond + "");
        }
		if (mhour==0&&mmin==0&&msecond==0){
			removeCallbacks(this);
		}else {
			postDelayed(this, 1000);
		}
	}

}
