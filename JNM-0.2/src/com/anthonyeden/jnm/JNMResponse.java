package com.anthonyeden.jnm;

import java.util.List;

public class JNMResponse{
	
	public JNMResponse(List data, long timeOfRequest){
		this.data = new byte[data.size()];
		for(int i = 0; i < data.size(); i++){
			Byte b = (Byte)data.get(i);
			this.data[i] = b.byteValue();
		}
		this.timeOfRequest = timeOfRequest;
	}
	
	public JNMResponse(byte[] data, long timeOfRequest){
		this.data = data;
		this.timeOfRequest = timeOfRequest;
	}		
	
	public JNMResponse(String data, long timeOfRequest){
		this(data.getBytes(), timeOfRequest);
	}
	
	public byte[] getData(){
		return data;
	}
	
	public long getTimeOfRequest(){
		return timeOfRequest;
	}
	
	public String toString(){
		return new String(data);
	}
	
	private byte[] data;
	private long timeOfRequest;
	
}
