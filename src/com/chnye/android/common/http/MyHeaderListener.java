package com.chnye.android.common.http;

import java.util.Map;

import com.android.volley.Response.Listener;

public interface MyHeaderListener{

	public void onResponseHeader(Map<String, String> headers );
	
}
