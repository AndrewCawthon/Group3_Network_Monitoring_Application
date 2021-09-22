package com.anthonyeden.jnm.util;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.LinkedList;

import com.anthonyeden.lib.log.Logger;
import com.anthonyeden.lib.log.LogManager;
import com.anthonyeden.lib.util.IOUtilities;

public class TCPClient{
	
	public TCPClient(){
		
	}
	
	public TCPClient(String host, int port){
		this.host = host;
		this.port = port;
	}
	
	public String getHost(){
		return host;
	}
	
	public void setHost(String host){
		this.host = host;
	}
	
	public int getPort(){
		return port;
	}
	
	public void setPort(int port){
		this.port = port;
	}
	
	public void setPort(String port){
		if(port != null){
			setPort(Integer.parseInt(port));
		}
	}
	
	public int getTimeout(){
		return timeout;
	}
	
	public void setTimeout(int timeout){
		this.timeout = timeout;
	}
	
	public byte[] sendRequest(String requestData) throws Exception{
		return sendRequest(requestData.getBytes());
	}
	
	public byte[] sendRequest(byte[] requestData) throws Exception{
		OutputStream out = null;
		InputStream in = null;
		
		LinkedList responseData = new LinkedList();
		
		try{
			log.debug("Connecting [" + host + ":" + port + "]");
			Socket s = new Socket(host, port);
			log.debug("SO_TIMEOUT = " + timeout);
			s.setSoTimeout(timeout);
			
			out = s.getOutputStream();
			out.write(requestData);
			out.flush();
			
			in = s.getInputStream();
			int c;
			while((c = in.read()) != -1){
				responseData.add(new Byte((byte)c));
			}
		} finally {
			IOUtilities.close(in);
			IOUtilities.close(out);
		}
		
		byte[] responseDataArray = new byte[responseData.size()];
		for(int i = 0; i < responseData.size(); i++){
			responseDataArray[i] = ((Byte)responseData.get(i)).byteValue();
		}
		return responseDataArray;
	}
	
	private static final Logger log = LogManager.getLogger(TCPClient.class.getName());
	
	private String host;
	private int port;
	private int timeout;
	
}
