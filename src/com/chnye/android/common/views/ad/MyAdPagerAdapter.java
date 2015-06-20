package com.chnye.android.common.views.ad;

import java.util.List;
import java.util.Stack;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.chnye.android.common.http.VolleySingleton;
import com.chnye.android.common.sys.MyApplication;
import com.example.adtest2.R;


public class MyAdPagerAdapter extends PagerAdapter {

	public static final int MAX_AD_SIZE = 20;
	
	private List<IAdBean> mDatas = null;
	
	private MyAdViewPager mViewPager = null;
	

//	//滑动模式: 支持没有任何操作、轮播以及传递到父View三种模式
//    /** do nothing when sliding at the last or first item **/
//    public static final int        SLIDE_BORDER_MODE_NONE      = 0;
//    /** cycle when sliding at the last or first item **/
//    public static final int        SLIDE_BORDER_MODE_CYCLE     = 1;
//    /** deliver event to parent when sliding at the last or first item **/
//    public static final int        SLIDE_BORDER_MODE_TO_PARENT = 2;
//    //滑动模式: 滑动到第一个或最后一个Item的处理方式
//    /** how to process when sliding at the last or first item, default is {@link #SLIDE_BORDER_MODE_NONE} **/
//    private int                    slideBorderMode             = SLIDE_BORDER_MODE_CYCLE;
    
    //滑动是否循环
    /** whether automatic cycle when auto scroll reaching the last or first item, default is true **/
    private boolean                isCycle                     = true;
    
    
	
	private Context mContext; 
	
	ImageLoader imgLoader = VolleySingleton.getInstance( MyApplication.getInstance() ).getImageLoader();
	
	public MyAdPagerAdapter( Context context, List<IAdBean> datas, MyAdViewPager viewPager ){
		this.mContext = context;
		this.mDatas = datas;
		this.mViewPager = viewPager;
	}
	
	@Override
	public Object instantiateItem(ViewGroup container, final int position) {
		// TODO Auto-generated method stub
		int srcPosition = position;
		final int newPosition = position % mDatas.size();
//		View view = mDatas.get( newPosition ).getView(); 
		View view = DefaultCacheView.getInstance().getCacheView();
		
//		if( view == null ){
//			view = new TextView( mContext );
//		}
//		((TextView)view).setText( mDatas.get(newPosition).getUrl() );
		
		
		if( view == null ){
			view = new ImageView( mContext );
			
		}
		ImageView imageView = (ImageView)view;
		imageView.setScaleType( ImageView.ScaleType.FIT_XY );
		String url = mDatas.get(newPosition).getUrl();
		
		imageView.setTag( url );
		ImageListener imgListener = imgLoader.getImageListener( imageView,		//需求加载图片的控件
				R.drawable.ic_launcher,
				R.drawable.ic_launcher );
			imgLoader.get( url, imgListener );

		view.setOnClickListener( new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AdListener listener = mDatas.get( newPosition ).getListener();
				listener.onViewClick( newPosition, v );
			}
		});
		
		container.addView( view );
		return view;
		//return super.instantiateItem(container, position);
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		// TODO Auto-generated method stub
		int srcPosition = position;
		position = position % mDatas.size();
		
		DefaultCacheView.getInstance().setCacheView( (View)object );
		container.removeView(  (View)object );
		//super.destroyItem(container, position, object);
	}

	@Override
	public void finishUpdate(ViewGroup container) {
		// TODO Auto-generated method stub
//		super.finishUpdate(container);

		int position = mViewPager.getCurrentItem();

		if( isCycle ){
			if( position == 0 ){
				position = mDatas.size();
				mViewPager.setCurrentItem( position, false );
			} else if( position == MAX_AD_SIZE - 1 ){
				position = mDatas.size() - 1;
				mViewPager.setCurrentItem( position, false );
			}
		}
		
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		int iSize = 1;
		if( isCycle ){
			iSize = MAX_AD_SIZE; 
		} else {
			iSize = mDatas.size(); 
		}
		return iSize;
	}

	@Override
	public boolean isViewFromObject(View paramView, Object paramObject) {
		// TODO Auto-generated method stub
		return paramView == paramObject;
	}

	/** 广告轮播是否无限循环
	 * 
	 * @param isCycle
	 */
	public void setIsCycle( boolean isCycle ){
		this.isCycle = isCycle;
	}
	public boolean getIsCycle(){
		return this.isCycle;
	}
	
	
	
	/* ------------------------- 广告轮播的点击接口 ------------------------- */
	
	public static interface AdListener{
		void onViewClick( int position, View view );
	}
	
	/* ------------------------- 广告轮播的数据接口 ------------------------- */
	
	public static interface IAdBean{
		String getUrl();
//		View getView();
//		void setView( View view );
		String getTitle();
		AdListener getListener();
	}
	
	
	/* ------------------------- 视图缓存 ------------------------- */
	
	public static interface ICacehView{
		View getCacheView();
		void setCacheView( View view );
	}
	
	private static class DefaultCacheView implements ICacehView{
		private static DefaultCacheView mInstance = null;
		private Stack<View> mStack = null;
		private DefaultCacheView(){
			mStack = new Stack<View>();
		}
		
		public static DefaultCacheView getInstance(){
			if( mInstance == null ){
				mInstance = new DefaultCacheView();
			}
			return mInstance;
		}
		
	@Override
	public View getCacheView() {
		// TODO Auto-generated method stub
		if( mStack.isEmpty() ){
			return null;
		}
		return mStack.pop();
	}

	@Override
	public void setCacheView(View view) {
		// TODO Auto-generated method stub
		mStack.push( view );
	}
		
	}
	
}
