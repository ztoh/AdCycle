package com.chnye.android.common.http;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.JsonObjectRequest;

public class MyJsonObjectRequest extends JsonObjectRequest{

public static final String TAG = MyJsonObjectRequest.class.getSimpleName();
	
	public static final String COOKIE_KEY = "Cookie";
	public static final String SESSION_KEY = "JSESSIONID";
	
	private  Map<String, String>  mHeaders = new HashMap<String,String>();
	private  Map<String, String> mPostData = new HashMap<String,String>();
	Listener<JSONObject> mListener = null;
	MyHeaderListener myHeaderListener = null;
	Map<String, String> mResponseHeaders = null;
	boolean bResponseHeader = false;
	
	public MyJsonObjectRequest( int method, String url, Map<String, String> appendHeader,
			JSONObject jsonRequest, 
			Listener<JSONObject> listener, 
			ErrorListener errorListener, MyHeaderListener headerListener ){
		super( method, url, jsonRequest, listener, errorListener );
		mHeaders.putAll( appendHeader );
		this.mListener = listener;
		this.myHeaderListener = headerListener;
		
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
    protected void deliverResponse(JSONObject response) {
        mListener.onResponse(response);
        if( myHeaderListener != null )
        	myHeaderListener.onResponseHeader( mResponseHeaders );
    }
	
	@Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
		this.mResponseHeaders = response.headers;
		return super.parseNetworkResponse(response);
    }
	
	
}
