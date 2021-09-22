package com.anthonyeden.jnm;

public class JNMHeadless{
	
	public JNMHeadless(){
		client = new JNMClient();
	}
	
	public void startMonitor(){
		client.startAllMonitors();
	}
	
	public void stopMonitor(){
		client.stopAllMonitors();
	}
	
	private JNMClient client;
	
}
