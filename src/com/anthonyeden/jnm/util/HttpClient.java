package com.anthonyeden.jnm.util;

import java.net.URL;
import java.util.Map;
import java.util.Iterator;
import java.util.Collections;

public class HttpClient extends TCPClient{
	
	public String sendRequest(URL url) throws Exception{
		return sendRequest(url, DEFAULT_METHOD, Collections.EMPTY_MAP);
	}
	
	public String sendRequest(URL url, Map headers) throws Exception{
		return sendRequest(url, DEFAULT_METHOD, headers);
	}
	
	public String sendRequest(URL url, String method, Map headers) throws Exception{
		setHost(url.getHost());
		setPort(getPort(url.getPort()));
		
		StringBuffer buffer = new StringBuffer();
		
		buffer.append(method);
		buffer.append(" ");
		buffer.append(url.getFile());
		buffer.append(" ");
		buffer.append(HTTP_VERSION);
		buffer.append("\n");
		
		Iterator keys = headers.keySet().iterator();
		while(keys.hasNext()){
			Object key = keys.next();
			Object value = headers.get(key);
			
			buffer.append(key.toString());
			buffer.append(": ");
			buffer.append(value.toString());
			
			if(keys.hasNext()){
				buffer.append("\n");
			}
		}
		
		buffer.append("\n\n");
		
		this.lastRequestData = buffer.toString();
		
		return new String(super.sendRequest(lastRequestData));
	}
	
	public String getLastRequestData(){
		return lastRequestData;
	}
	
	private int getPort(int port){
		if(port == -1){
			return DEFAULT_PORT;
		} else {
			return port;
		}
	}
	
	private static final String HTTP_VERSION = "HTTP/1.0";
	private static final String DEFAULT_METHOD = "GET";
	private static final int DEFAULT_PORT = 80;
	
	private String lastRequestData;
	
}
