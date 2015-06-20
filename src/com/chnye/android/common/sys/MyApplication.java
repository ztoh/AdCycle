package com.chnye.android.common.sys;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import android.app.Application;
import android.os.Handler;
import android.os.HandlerThread;
import android.text.TextUtils;




public class MyApplication extends Application {

	public static final String TAG = MyApplication.class.getSimpleName();
	
	private static MyApplication mInstance;
	
	@Override
	public void onCreate(){
		super.onCreate();
		mInstance = this;	
	}
	
	public static synchronized MyApplication getInstance(){	
		return mInstance;	
	}
	
	/**------------------------------------------------------------**/
	/** 用于数据传递的缓存集合
	*/
	private Map<String, Object> mTransMap = new ConcurrentHashMap<String, Object>();
	
	public void put( String key, Object value ){
		if( TextUtils.isEmpty( key ) )
			return;
		mTransMap.put( key, value );
	}
	public Object get( String key ){	
		if( TextUtils.isEmpty( key ) )
			return null;
		return mTransMap.get( key );	
	}
	public String getString( String key ){
		Object localObject = get( key );
		if( localObject == null )
			return "";
		return localObject.toString();	
	}
	public void remove( String key ){
		if( TextUtils.isEmpty( key ) )
			return;
		mTransMap.remove( key );	
	}
	
	/**------------------------------------------------------------**/
	/**工作线程 
	*/
	private static final HandlerThread mThread = new HandlerThread( "MyApplication" );
	private static final Handler mHandler;
	
	/**
	使Application内始终有个独立的线程在运行者
	作用：方便任何Activity向使用工作线程的
	*/
	static{
		mThread.start();
		mHandler = new Handler( mThread.getLooper() );
	}
	
	public void todo( Runnable paramRunable ){
		mHandler.post( paramRunable );
	}
	
	
}
