package com.anthonyeden.jnm.action;

import java.net.URL;
import java.net.MalformedURLException;
import java.util.Map;
import java.util.HashMap;

import com.anthonyeden.lib.log.Logger;
import com.anthonyeden.lib.log.LogManager;
import com.anthonyeden.lib.config.Configuration;
import com.anthonyeden.lib.config.MutableConfiguration;
import com.anthonyeden.lib.config.ConfigurationException;

import com.anthonyeden.jnm.Action;
import com.anthonyeden.jnm.Monitor;
import com.anthonyeden.jnm.util.Utilities;
import com.anthonyeden.jnm.util.HttpClient;

/**	Action implementation which sends an HTTP request upon execution.

	@author Anthony Eden
*/

public class HttpRequestAction extends Action{

	public void execute(Monitor monitor) throws Exception{
		HttpClient client = new HttpClient();
		client.sendRequest(url, method, getHeaders());
	}
	
	public URL getURL(){
		return url;
	}
	
	public void setURL(URL url){
		this.url = url;
	}
	
	public void setURL(String url) throws MalformedURLException{
		if(url == null){
			throw new IllegalArgumentException("URL must be specified");
		}
		setURL(new URL(url));
	}
	
	public String getMethod(){
		return method;
	}
	
	public void setMethod(String method){
		this.method = method;
	}
	
	public boolean isUseAuthentication(){
		return useAuthentication;
	}
	
	public void setUseAuthentication(boolean useAuthentication){
		log.debug("setUseAuthentication(" + useAuthentication + ")");
		this.useAuthentication = useAuthentication;
	}
	
	public void setUseAuthentication(String useAuthentication){
		setUseAuthentication("true".equalsIgnoreCase(useAuthentication));
	}		
	
	public String getUsername(){
		return username;
	}
	
	public void setUsername(String username){
		this.username = username;
	}
	
	public String getPassword(){
		return password;
	}
	
	public void setPassword(char[] password){
		setPassword(new String(password));
	}
	
	public void setPassword(String password){
		this.password = password;
	}
	
	public void readConfiguration(Configuration configuration) throws ConfigurationException{
		super.readConfiguration(configuration);
		
		try{
			setURL(configuration.getChildValue("url"));
			setMethod(configuration.getChildValue("method"));
			setUseAuthentication(configuration.getChildValue("useAuthentication"));
			setUsername(configuration.getChildValue("username"));
			setPassword(configuration.getChildValue("password"));
		} catch(Exception e){
			throw new ConfigurationException(e);
		}
	}
	
	public void writeConfiguration(MutableConfiguration configuration) throws ConfigurationException{
		super.writeConfiguration(configuration);
		
		configuration.addChild("url", getURL().toString());
		configuration.addChild("method", getMethod());
		configuration.addChild("useAuthentication", new Boolean(isUseAuthentication()).toString());
		configuration.addChild("username", getUsername());
		configuration.addChild("password", getPassword());
	}
	
	private Map getHeaders(){
		HashMap headers = new HashMap();
		if(useAuthentication){
			headers.put("Authorization", getAuthorizationHeaderValue());
		}
		return headers;
	}
	
	private String getAuthorizationHeaderValue(){
		StringBuffer buffer = new StringBuffer();
		buffer.append("Basic ");
		buffer.append(Utilities.base64Encode(username + ":" + password));
		return buffer.toString();
	}
	
	public static final String DEFAULT_METHOD = "GET";
	
	private static final Logger log = LogManager.getLogger(HttpRequestAction.class.getName());
	
	private URL url;
	private String method = DEFAULT_METHOD;
	private boolean useAuthentication;
	private String username;
	private String password;
	
}
