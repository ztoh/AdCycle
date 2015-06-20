package com.chnye.android.common.http;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.SoftReference;
import java.util.LinkedHashMap;

import com.android.volley.toolbox.ImageLoader.ImageCache;

import libcore.io.DiskLruCache;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.util.LruCache;
import android.util.Log;


/** 软引用缓存管理类
http://blog.csdn.net/zimo2013/article/details/16981945
*/

public class BitmapSoftRefCache implements ImageCache{

	private static final String TAG = BitmapSoftRefCache.class.getSimpleName();
	
	private LinkedHashMap<String, SoftReference<Bitmap>> mMap;
	
	//磁盘缓存
	DiskLruCache mDiskLruCache = null;
	
	public BitmapSoftRefCache(){
		mMap = new LinkedHashMap<String, SoftReference<Bitmap>>();
		
		//实例化磁盘缓存
		try{          
			File cacheDir = DiskLruCacheUtil.getDiskCacheDir( "bitmap" );
          	if( !cacheDir.exists() ){  
          		cacheDir.mkdirs();   
          	}
          	Log.d(TAG, "DiskCache Path:" + cacheDir.getAbsolutePath() );
          	mDiskLruCache = DiskLruCache.open( cacheDir, DiskLruCacheUtil.getAppVersion(), 1, 10*1024*1024 );
      	} catch( IOException e ){  
      		e.printStackTrace();  
      	}  
		
	}
	
	@Override
	public Bitmap getBitmap( String url ){
		Log.d(TAG, "SoftCache:" + url );
		Bitmap bitmap = null;
		SoftReference<Bitmap> softRef = mMap.get( url );
		String key = DiskLruCacheUtil.hashKeyForDisk( url );
		if( softRef == null ){
			Log.d(TAG, "SoftCache:Null" );
			bitmap = getBitmapFromDiskLruCache( key );
		} else {
			bitmap = softRef.get();
			if( bitmap == null ){
				Log.d(TAG, "SoftCache:Remove");
				mMap.remove( url );		//从mMap中移除
				bitmap = getBitmapFromDiskLruCache( key );
			}
		} 
		return bitmap;
	}
	
	@Override
	public void putBitmap( String url, Bitmap bitmap ){
		SoftReference<Bitmap> softRef = new SoftReference<Bitmap>( bitmap );
		mMap.put( url, softRef );
		
		//图片被挤出一级强引用缓存，进入弱引用缓存时，将其写入磁盘缓存
		String key = DiskLruCacheUtil.hashKeyForDisk( url );
		if( getBitmapFromDiskLruCache( key) == null ){
			putBitmapToDiskLruCache(key,bitmap);
		}
	}
	
	private void putBitmapToDiskLruCache(String key, Bitmap bitmap) {
		Log.d("TAG", "写入Disk!");
        try {  
            DiskLruCache.Editor editor = mDiskLruCache.edit(key);  
            if(editor!=null)  
            {  
                OutputStream outputStream = editor.newOutputStream(0);  
                bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);  
                editor.commit();  
            }  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
    }  
    
    
    private Bitmap getBitmapFromDiskLruCache(String key) {  
        try {  
            DiskLruCache.Snapshot snapshot= mDiskLruCache.get(key);  
            if(snapshot!=null)  
            {  
            	Log.d(TAG, "DiskCache:OK!");
                InputStream inputStream = snapshot.getInputStream(0);  
                if (inputStream != null) {  
                    Bitmap bmp = BitmapFactory.decodeStream(inputStream);  
                    inputStream.close();  
                    return bmp;  
                }  
            } else {
            	Log.d(TAG, "DiskCache:Null!");
            }  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        return null;  
    } 
    
    
	
}