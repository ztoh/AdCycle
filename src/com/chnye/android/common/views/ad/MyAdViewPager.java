package com.chnye.android.common.views.ad;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class MyAdViewPager extends ViewPager implements Runnable{

	//缺省时间间隔4秒
    public static final int        DEFAULT_INTERVAL            = 4000;
    //滑动时间间隔
    /** auto scroll time in milliseconds, default is {@link #DEFAULT_INTERVAL} **/
    private long                   interval                    = DEFAULT_INTERVAL;
    private long                   delay_interval              = DEFAULT_INTERVAL;
    
    //滑动方向： 左 或 右
    public static final int        LEFT                        = 0;
    public static final int        RIGHT                       = 1;
    //滑动方向
    /** auto scroll direction, default is {@link #RIGHT} **/
    private int                    direction                   = RIGHT;
    

    
    //当手指触摸时，是否停止滚动
    /** whether stop auto scroll when touching, default is true **/
    private boolean                stopScrollWhenTouch         = true;
    
    //循环滚动时是否需要动画
    /** whether animating when auto scroll at the last or first item **/
    private boolean                isBorderAnimation           = true;
    
	private boolean isAutoScroll = false;


	
	
	/* ------------------------定时器---------------------- */
	
	private Timer mTimer = null;
	private TimerTask mTimerTask = null;
	
//	private TimerTask mTimerTask = new TimerTask() {
//		
//		@Override
//		public void run() {
//			// TODO Auto-generated method stub
//				post( MyAdViewPager.this );
//		}
//	};
	private class MyTimerTask extends TimerTask{
		@Override
		public void run() {
			// TODO Auto-generated method stub
			post( MyAdViewPager.this );
		}
	}
	

	private void timeStart(){
		if( mTimer == null ){
			mTimer = new Timer();
			mTimerTask = new MyTimerTask();
			mTimer.schedule(mTimerTask, delay_interval, interval );
		}
	}
	
	private void timeStop(){
		if( mTimer != null ){
		    mTimer.cancel();
		    mTimer = null;
		    mTimerTask = null;
		}
	}
	
	
	
	
	public MyAdViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init();
	}
	
	private void init(){

	}


	
	 /** 启动自动滚动
     * start auto scroll, first scroll delay time is {@link #getInterval()}
     */
	public void startAutoScroll(){
		isAutoScroll = true;
		timeStart();
	}
	
	public void startAutoScroll( int delayTimeInMills ){
		isAutoScroll = true;
		this.delay_interval = delayTimeInMills;
		timeStart();
	}
	
	public void stopAutoScroll(){
		isAutoScroll = false;
		timeStop();
	}
	
	@Override
	public boolean dispatchTouchEvent( MotionEvent event ){
		if( stopScrollWhenTouch ){
			if( event.getAction() == MotionEvent.ACTION_DOWN ){
				stopAutoScroll();
			} else if( event.getAction() == MotionEvent.ACTION_UP && stopScrollWhenTouch ){
				startAutoScroll();
			}
		}
		return super.dispatchTouchEvent( event );
	}
	
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		scrollOnce();
	}
	
	
	
	
	public void scrollOnce(){
		int currentItem = getCurrentItem();
		int nextItem = 0;
		if( direction == LEFT ){
			nextItem = currentItem- 1;
		} else {
			nextItem = currentItem+ 1;
		}
		setCurrentItem( nextItem );
		
	}
	
}
