package com.anthonyeden.jnm;

import java.util.List;

public class JNMRequest{
	
	public JNMRequest(List data){
		this.data = new byte[data.size()];
		for(int i = 0; i < data.size(); i++){
			Byte b = (Byte)data.get(i);
			this.data[i] = b.byteValue();
		}
	}
			
	
	public JNMRequest(String data){
		this.data = data.getBytes();
	}
	
	public JNMRequest(byte[] data){
		this.data = data;
	}
	
	public byte[] getData(){
		return data;
	}
	
	public String toString(){
		return new String(data);
	}
	
	private byte[] data;
	
}
