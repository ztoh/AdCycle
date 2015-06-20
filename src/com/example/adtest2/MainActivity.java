package com.example.adtest2;

import java.util.ArrayList;
import java.util.List;

import com.chnye.android.common.views.ad.MyAdPagerAdapter;
import com.chnye.android.common.views.ad.MyAdViewPager;
import com.chnye.android.common.views.ad.MyAdPagerAdapter.AdListener;
import com.chnye.android.common.views.ad.MyAdPagerAdapter.IAdBean;


import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;

public class MainActivity extends Activity {

//	AutoScrollViewPager mViewPager;
//	MyAutoScrollViewPager mViewPager;
	MyAdViewPager mViewPager;
//	HashMap<String, String> urlMaps = new HashMap<String,String>();
	List<IAdBean> mDatas;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
//		mViewPager = (AutoScrollViewPager)findViewById( R.id.id_ad );
//		mViewPager = (MyAutoScrollViewPager)findViewById( R.id.id_ad );
		mViewPager = (MyAdViewPager)findViewById( R.id.id_ad );
		
//		urlMaps.put("a", "http://www.chnye.com/ad/1.jpg");
//		urlMaps.put("b", "http://www.chnye.com/ad/2.jpg");
//		urlMaps.put("c", "http://www.chnye.com/ad/3.jpg");
		
		
		
//		mViewPager.setBanners( urlMaps );
		//BannerAdapter adapter = new BannerAdapter(  );
//		mViewPager.setAdapter(  adapter );
		init();
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private void init(){
		mDatas = new ArrayList<IAdBean>();
		
		IAdBean bean1 = new DefaultAdBean("http://img.my.csdn.net/uploads/201407/26/1406383299_1976.jpg", "a", mListener );
		mDatas.add( bean1 );
		IAdBean bean2 = new DefaultAdBean("http://img.my.csdn.net/uploads/201407/26/1406383291_6518.jpg", "b", mListener );
		mDatas.add( bean2 );
		IAdBean bean3 = new DefaultAdBean("http://img.my.csdn.net/uploads/201407/26/1406383291_8239.jpg", "c" , mListener);
		mDatas.add( bean3 );
		IAdBean bean4 = new DefaultAdBean("http://img.my.csdn.net/uploads/201407/26/1406383290_9329.jpg", "d", mListener );
		mDatas.add( bean4 );
		IAdBean bean5 = new DefaultAdBean("http://img.my.csdn.net/uploads/201407/26/1406383290_1042.jpg", "e", mListener );
		mDatas.add( bean5 );
		IAdBean bean6 = new DefaultAdBean("http://img.my.csdn.net/uploads/201407/26/1406383275_3977.jpg", "f", mListener );
		mDatas.add( bean6 );
		
		
		MyAdPagerAdapter adapter = new MyAdPagerAdapter( this, mDatas, mViewPager );
		adapter.setIsCycle( true );
		mViewPager.setAdapter( adapter );
//		adapter.setInit(  true );
		mViewPager.startAutoScroll();
	}
	
	private AdListener mListener = new AdListener() {
		
		@Override
		public void onViewClick(int position, View view) {
			// TODO Auto-generated method stub
			Log.d("tag", "aaaaaaaaaaaaaaaaaa=" + position);
		}
	};
	
//	private ICacehView mCacheView = new ICacehView(){
//
//		private Stack<View> mStack = new Stack<View>();
//		
//		@Override
//		public View getCacheView() {
//			// TODO Auto-generated method stub
//			return mStack.pop();
//		}
//
//		@Override
//		public void setCacheView( View view ) {
//			// TODO Auto-generated method stub
//			mStack.push( view );
//		}
//		
//	};

	
	
	
	private class DefaultAdBean implements IAdBean{

		private String mUrl;
//		private ImageView mImageView;
//		private TextView mTextView;
		private String mTitle;
		private AdListener mListener;
		
		public DefaultAdBean( String url, String title ){
			this( url, title, null );
		}
		
		public DefaultAdBean( String url, String title, AdListener listener ){
			this.mUrl = url;
			this.mTitle = title;
			this.mListener = listener;
//			this.mImageView = new ImageView(MainActivity.this);
			init();
		}
		
		private void init(){
//			mTextView = new TextView(MainActivity.this);
//			mTextView.setText(mUrl);
			
		}
		
//		@Override
//		public View getView() {
//			// TODO Auto-generated method stub
////			return mImageView;
//			return mTextView;
//		}

		@Override
		public String getTitle() {
			// TODO Auto-generated method stub
			return mTitle;
		}

		@Override
		public AdListener getListener() {
			// TODO Auto-generated method stub
			return mListener;
		}

		@Override
		public String getUrl() {
			// TODO Auto-generated method stub
			return mUrl;
		}

//		@Override
//		public void setView(View view) {
//			// TODO Auto-generated method stub
////			this.mImageView = (ImageView)view;
//			this.mTextView = (TextView)view;
//		}
		
	}
	

}
