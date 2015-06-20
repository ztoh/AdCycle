package com.chnye.android.common.http;

/**
 MyReqeuest request = new MyRequest( Request.METHOD.Post, url, Address.class, 
 	new Listener<Address>(){
 		@Override
 		public void onResponse( User response ){
 			
 		}
 	}, appendHeader, null );
 	
 http://my.oschina.net/liusicong/blog/361853
 
 post:   http://my.oschina.net/liusicong/blog/362215
 
 	
 * */
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class MyRequest<T> extends Request<T> {

	public static final String TAG = MyRequest.class.getSimpleName();
	
	private static final String PROTOCOL_CHARSET = "utf-8";
	private static final String PROTOCOL_CONTENT_TYPE= String.format("application/json; charset=%s", PROTOCOL_CHARSET );
	
	private final Gson gson = new Gson();
	private final Class<T> clazz;
	private final Listener<T> listener;
	private  MyHeaderListener mHeaderListener = null;
	private  Map<String, String>  mHeaders = new HashMap<String,String>();
	private  Map<String, String> mPostData = new HashMap<String,String>();
	private  String mJsonString = null;
	Map<String, String> mResponseHeaders = null;
	
	
	static{
		
	}
	
	/**
	 * 当前是以StringRequest的getParam方式送过去的POST请求。
	 * 前提：不能送json头信息过去。
	 * */
	public MyRequest( int method, String url, Map<String, String> appendHeader,
			Map<String, String> postData, 
			Class<T> clazz, 
			Listener<T> listener, 
			ErrorListener errorListener, 
			MyHeaderListener headerListener ){
		super( method, url, errorListener );
		this.clazz = clazz;
		this.listener = listener;
		this.mHeaderListener = headerListener;
		if( appendHeader != null )
			mHeaders.putAll( appendHeader );
		if( mPostData != null )
			mPostData.putAll( postData );
		
		
	}

	/**
	 * 当前是以JsonObject的将jsonString放入Request.Body中的POST形式送过去的。
	 * 前提：必须送json头信息过去。
	 */
	public MyRequest( int method, String url, Map<String, String> appendHeader,
			String jsonString, 
			Class<T> clazz, 
			Listener<T> listener, 
			ErrorListener errorListener ){
		super( method, url, errorListener );
		this.clazz = clazz;
		this.listener = listener;
		this.mJsonString = jsonString;
		if( appendHeader != null )
			mHeaders.putAll( appendHeader );

		
	}

	
	/**
	 * 将返回数据解析成我们希望的对象
	 */
	@Override
	protected Response<T> parseNetworkResponse(NetworkResponse response) {
		// TODO Auto-generated method stub
		try{
			//得到返回的数据
			String jsonStr = new String( response.data, HttpHeaderParser.parseCharset( response.headers ));
			String responseHeaderStr = response.headers.toString();
			Log.d(TAG, "get headers :" + responseHeaderStr );
			//转换为对象
			return Response.success(  gson.fromJson(jsonStr, clazz), HttpHeaderParser.parseCacheHeaders( response ) );
		} catch( UnsupportedEncodingException e  ){
			return Response.error( new ParseError(e) ) ;
		} catch( JsonSyntaxException e  ){
			return Response.error( new ParseError(e) ) ;
		}

	}

	@Override
    public byte[] getBody() {
        try {
            return mJsonString == null ? null : mJsonString.getBytes(PROTOCOL_CHARSET);
        } catch (UnsupportedEncodingException uee) {
            VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s",
            		mJsonString, PROTOCOL_CHARSET);
            return null;
        }
    }
	
	/**
	 * 回调
	 * */
	@Override
	protected void deliverResponse(T response) {
		// TODO Auto-generated method stub
		this.listener.onResponse(response);
		if( mHeaderListener != null ){
			mHeaderListener.onResponseHeader(mResponseHeaders);
		}
	}
	
	@Override
	public Map<String,String> getHeaders() throws AuthFailureError{
		return this.mHeaders;
	}
	
	@Override
	public Map<String, String> getParams() throws AuthFailureError{
		return this.mPostData;
	}
 	
	public void setSendCookie( String cookie ){
		mHeaders.put("JSESSIONID", cookie );
	}

	
	@Override
	public String getBodyContentType(){
		return PROTOCOL_CONTENT_TYPE;
	}
	
}
