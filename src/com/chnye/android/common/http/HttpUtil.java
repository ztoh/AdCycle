package com.chnye.android.common.http;

import java.nio.charset.Charset;
import java.util.Map;


public class HttpUtil{

	public static final String HEADER_COOKIE_KEY = "Set-Cookie";
	public static final String COOKIE_KEY = "JSESSIONID";

    public static Charset getCharsetFromHeader( Map<String,String> headers ) {
       return null;
    }
    
    public static String getSessionFromHeader( Map<String,String> headers ){
    	String cookieValue = null;
    	if( headers != null ){
    		for( String key : headers.keySet() ){
    			String value = headers.get(key);
    			if( HEADER_COOKIE_KEY.equals( key) ){
    				cookieValue = getCookie( value );
    				break;
    			}
    		}
    	}
    	return cookieValue;
    }

    private static String getCookie( String headerString ){
    	String cookieValue = null;
    	if( headerString != null ){
    		int keyIndex = headerString.indexOf(  COOKIE_KEY );
    		if( keyIndex >= 0 ){
    			int endIndex = headerString.indexOf( ";",  keyIndex );
    			if( endIndex > keyIndex ){
    				cookieValue = headerString.substring( keyIndex + COOKIE_KEY.length() + 1, endIndex );
    			}
    		}
    	}
    	return cookieValue;
    }
    
}