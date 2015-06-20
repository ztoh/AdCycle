package com.chnye.android.common.http;


import java.util.HashMap;
import java.util.Map;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.StringRequest;


public class MyStringRequest extends StringRequest{

	
	public static final String TAG = MyStringRequest.class.getSimpleName();
	
	public static final String COOKIE_KEY = "Cookie";
	public static final String SESSION_KEY = "JSESSIONID";
	
	private  Map<String, String>  mHeaders = new HashMap<String,String>();
	private  Map<String, String> mPostData = new HashMap<String,String>();
	Listener<String> mListener = null;
	MyHeaderListener myHeaderListener = null;
	Map<String, String> mResponseHeaders = null;
	boolean bResponseHeader = false;
	
	public MyStringRequest( int method, String url, Map<String, String> appendHeader,
			Map<String, String> postData, 
			Listener<String> listener, 
			ErrorListener errorListener, MyHeaderListener headerListener ){
		super( method, url, listener, errorListener );
		if( appendHeader != null )
			mHeaders.putAll( appendHeader );
		if( mPostData != null )
			mPostData.putAll( postData );
		this.mListener = listener;
		this.myHeaderListener = headerListener;
		
	}

	@Override
	protected Map<String, String> getParams() throws AuthFailureError {
		// TODO Auto-generated method stub
		//return super.getParams();
		return mPostData;
	}

	@Override
	public Map<String, String> getHeaders() throws AuthFailureError {
		// TODO Auto-generated method stub
		//return super.getHeaders();
		return mHeaders;
	}
	
	public void setCookie( String value ){
		mHeaders.put( COOKIE_KEY,  SESSION_KEY + "=" + value );
	}
	
//	public String getCookie(){
//		return mHeaders.get( COOKIE_KEY );
//	}
	
	@Override
    protected void deliverResponse(String response) {
        mListener.onResponse(response);
        if( myHeaderListener != null  )
        	myHeaderListener.onResponseHeader( mResponseHeaders );
    }
	
	@Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
		this.mResponseHeaders = response.headers;
		return super.parseNetworkResponse(response);
    }
	
	
	
	
	
	
	
	
	
}
