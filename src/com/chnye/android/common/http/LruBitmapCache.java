package com.chnye.android.common.http;

/**
LruCache缓存管理类,该类实现了ImageCache接口
一旦bitmap对象从LruCache中被挤出,将会被放置在BitmapSoftRefCache中，再配合该框架本身支持的硬盘缓存，就可以完成图片的三级缓存
*/

import com.android.volley.toolbox.ImageLoader.ImageCache;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.util.Log;

public class LruBitmapCache extends LruCache<String,Bitmap> implements ImageCache{
	
	private static final String TAG = LruBitmapCache.class.getSimpleName();
	
	//软引用缓存
	private BitmapSoftRefCache mSoftRefCache = null;
	
	public static int getDefaultLruCacheSize(){
		final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024 );
		final int cacheSize = maxMemory / 8;
		return cacheSize;
	}
	
	public LruBitmapCache(){
		this( getDefaultLruCacheSize() );
	}
	
	public LruBitmapCache( int sizeInKiloBytes ){
		super( sizeInKiloBytes );
		mSoftRefCache = new BitmapSoftRefCache();
	}
	
	@Override
	protected int sizeOf( String key, Bitmap value ){
		return value.getRowBytes() * value.getHeight() / 1024;
	}
	
	/** 得到缓存对象
	
	*/
	@Override
	public Bitmap getBitmap( String url ){
		Log.d(TAG, "First Cache:" + url );
		Bitmap bitmap =  get( url );
		//如果bitmap为null,尝试从软引用缓存中查找
		if( bitmap == null ){
			Log.d(TAG, "FirstCache:Null" );
			bitmap = mSoftRefCache.getBitmap( url );
			if( bitmap != null ){
				//写入强引用内存缓存
				put( url, bitmap );
			}
		} 
		return bitmap;
	}
	
	/** 添加缓存对象
	*/
	@Override
	public void putBitmap( String url, Bitmap bitmap ){
		put( url, bitmap );
	}
	
	
	@Override
	protected void entryRemoved( boolean evicted, String key, Bitmap oldValue, Bitmap newValue ){
		if( evicted ){
			//将被挤出的bitmap对象，添加到软引用BitmapSoftCache中
			mSoftRefCache.putBitmap( key, oldValue );
		}
	}
	
	
}
