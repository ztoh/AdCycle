package com.chnye.android.common.http;


import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Environment;
import android.util.Log;

import com.chnye.android.common.sys.MyApplication;


public class DiskLruCacheUtil{
	
	private static final String TAG = BitmapSoftRefCache.class.getSimpleName();
	
	public static Context getContext(){
		return MyApplication.getInstance();
	}

	/** 取得磁盘缓存的路径 
	调用示例
	    DiskLruCache mDiskLruCache = null;
	    try{
	    	File cacheDir = getDiskCacheDir( "bitmap" );
	    	if( !cacheDir ){
	    		cacheDir.mkdirs();
	    	}
	    	mDiskLruCache = DiskLruCache.open( cacheDir, DisLruCacheUtil.getAppVersion(context), 1, 10*1024*1024 );
	    } catch( IOException e ){
	    	e.printStackTrace();
	    }
	**/
	@SuppressLint("NewApi")
	public static File getDiskCacheDir( String uniqueName) {  
    	String cachePath;  
    	Context context = getContext();
    	if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())  
            || !Environment.isExternalStorageRemovable()) {
    		Log.d(TAG, "DiskCache Path:SDCard" );
        	cachePath = context.getExternalCacheDir().getPath();  
    	} else {
    		Log.d(TAG, "DiskCache Path:InnerCard" );
        	cachePath = context.getCacheDir().getPath();  
    	}  
    	Log.d(TAG, "DiskCache Path:" + cachePath );
    	return new File(cachePath + File.separator + uniqueName);  
	}
	
	/** 获取应用的版本号  **/
	public static int getAppVersion() {
		Context context = getContext();  
    	try {  
        	PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);  
        	return info.versionCode;  
    	} catch (NameNotFoundException e) {  
        	e.printStackTrace();  
    	}  
    	return 1;  
	}
	
	/** 将图片的URL转换成MD5的KEY 
	调用示例:
		String imageUrl = "http://www.baidu.com/a.png";
		String key = hashKeyForDisk( imageUrl );
		DiskLruCache.Editor editor = mDiskLruCache.edit( key );  //写入操作
	**/
	public static String hashKeyForDisk(String key) {  
    	String cacheKey;  
    	try {  
        	final MessageDigest mDigest = MessageDigest.getInstance("MD5");  
        	mDigest.update(key.getBytes());  
        	cacheKey = bytesToHexString(mDigest.digest());  
    	} catch (NoSuchAlgorithmException e) {  
        	cacheKey = String.valueOf(key.hashCode());  
    	}  
    	return cacheKey;  
	}  
  
	private static String bytesToHexString(byte[] bytes) {  
    	StringBuilder sb = new StringBuilder();  
    	for (int i = 0; i < bytes.length; i++) {  
        	String hex = Integer.toHexString(0xFF & bytes[i]);  
        	if (hex.length() == 1) {  
            	sb.append('0');  
        	}  
        	sb.append(hex);  
    	}  
    	return sb.toString();  
	}  	    


}