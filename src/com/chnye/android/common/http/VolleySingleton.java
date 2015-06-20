package com.chnye.android.common.http;

/**

参考： http://blog.csdn.net/zimo2013/article/details/16981945
*/

import android.content.Context;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
How to use?
  VolleySingleton.getInstance().getRequestQueue().addToRequestQueue( request );
  
  
*/

public class VolleySingleton{

	public static final String TAG = VolleySingleton.class.getSimpleName();
	private static Context mContext;
	private static VolleySingleton mInstance ;
	
	public static VolleySingleton getInstance( Context context ){
		if( mInstance == null ){
			mContext = context;
			mInstance = new VolleySingleton();
		}
		return mInstance;
	}
	
	/**网络请求和图片缓存 
	*/
	private RequestQueue mRequestQueue;
	private ImageLoader mImageLader;
	
	/** Volley framework 请求队列对象
	使用：  MyApplication.getInstance().getRequestQueue();
	*/
	public RequestQueue getRequestQueue(){
		if( mRequestQueue == null ){
			mRequestQueue = Volley.newRequestQueue( mContext );
		}
		return mRequestQueue;
	}

	/** Volley framework 用于图片请求
	使用： 	ImageLoader imgLoader = MyApplication.getInstance().getImageLoader();
			ImageListener imgListener = imgLoader.getImageListener( imageView,		//需求加载图片的控件
				R.drawable.default_image,
				R.drawable.failed_image );
			imgLoader.get( "http://www.baidu.com/a.png", imgListener );
			
			//使用NetworkImageView控件
			NetworkImageView  networkImageView = (NetworkImageView) findViewById(R.id.network_image_view);
			networkImageView.setDefaultImageResId(R.drawable.default_image); 
			networkImageView.setErrorImageResId(R.drawable.failed_image);
			networkImageView.setImageUrl( "http://www.baidu.com/a.png", imgLoader );
	*/	
	public ImageLoader getImageLoader(){
		getRequestQueue();
		if( mImageLader == null ){
			mImageLader = new ImageLoader( this.mRequestQueue, new LruBitmapCache() );
		}
		return mImageLader;
	}
	
	/** Volley framework 用于网络数据/信息请求
	使用：MyApplication.getInstance().addToRequestQueue( xRequest, "xxx" );
	*/
	public <T> void addToRequestQueue( Request<T> req, String tag ){
		req.setTag( TextUtils.isEmpty(tag) ? TAG : tag );
		getRequestQueue().add( req );
	}
	
	/** Volley framework 用于网络数据/信息请求
	使用：MyApplication.getInstance().addToRequestQueue( xRequest );
	*/
	public <T> void addToRequestQueue( Request<T> req ){
		req.setTag( TAG );
		getRequestQueue().add( req );
	}
	
	/** Volley framework　取消请求
	使用：MyApplication.getInstance().cancelPendingRequests( "xxx" );
	*/
	public void cancelPendingRequests( Object tag ){
		if( mRequestQueue != null ){	mRequestQueue.cancelAll(tag);	}
	}


}