package com.anthonyeden.jnm.monitor;

import java.io.InterruptedIOException;
import java.net.URL;
import java.net.URLEncoder;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.util.Map;
import java.util.HashMap;

import org.apache.oro.text.regex.Pattern;
import org.apache.oro.text.regex.Perl5Compiler;
import org.apache.oro.text.regex.Perl5Matcher;
import org.apache.oro.text.regex.MalformedPatternException;

import com.anthonyeden.lib.log.Logger;
import com.anthonyeden.lib.log.LogManager;
import com.anthonyeden.lib.config.Configuration;
import com.anthonyeden.lib.config.MutableConfiguration;
import com.anthonyeden.lib.config.ConfigurationException;

import com.anthonyeden.jnm.Monitor;
import com.anthonyeden.jnm.JNMRequest;
import com.anthonyeden.jnm.JNMResponse;
import com.anthonyeden.jnm.MonitorState;
import com.anthonyeden.jnm.util.Utilities;
import com.anthonyeden.jnm.util.HttpClient;

public class HttpMonitor extends Monitor{
	
	public HttpMonitor(){
		compiler = new Perl5Compiler();
		
		matcher = new Perl5Matcher();
		matcher.setMultiline(true);
	}
	
	public URL getURL(){
		return url;
	}
	
	public void setURL(URL url){
		log.debug("setURL(" + url + ")");
		
		if(url == null){
			throw new IllegalArgumentException("URL is required");
		}
		
		this.url = url;
		
		setDefaultName(url.toString());
	}
	
	public void setURL(String url) throws MalformedURLException{
		setURL(new URL(url));
	}
	
	public String getMethod(){
		return method;
	}
	
	public void setMethod(String method){
		this.method = method;
	}
	
	public String getExpression(){
		return expression;
	}
	
	public void setExpression(String expression) throws MalformedPatternException{
		this.expression = expression;
		this.pattern = compiler.compile(expression);
	}
	
	public int getTimeout(){
		return timeout;
	}
	
	public void setTimeout(int timeout){
		this.timeout = timeout;
	}
	
	public void setTimeout(String timeout){
		if(timeout != null){
			setTimeout(Integer.parseInt(timeout));
		}
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
	
	public void execute(){
		HttpClient client = new HttpClient();
		client.setTimeout(getTimeout());
		
		try{
			String responseData = client.sendRequest(getURL(), getMethod(), getHeaders());
			setLastRequest(new JNMRequest(client.getLastRequestData()));
			setLastResponse(new JNMResponse(responseData, System.currentTimeMillis()));
		} catch(InterruptedIOException e){
			log.error("Interrupted IO exception caught");
			setLastResponse(null);
			setState(MonitorState.TIMEOUT);
			return;
		} catch(ConnectException e){
			log.error("Connection failed");
			setLastResponse(null);
			setState(MonitorState.DOWN);
			return;
		} catch(Exception e){
			log.error("Exception caught");
			setLastResponse(null);
			logError(e);
			setState(MonitorState.ERROR);
			return;
		}
		
		if(test(getLastResponse())){
			setState(MonitorState.OK);
			log.info(getURL() + " is OK");
		} else {
			setState(MonitorState.FAILED);
			log.info(getURL() + " is FAILED");
		}
	}
	
	protected void readConfiguration(Configuration configuration) throws ConfigurationException{
		log.debug("Reading configuration");

		try{
			setURL(configuration.getChildValue("url"));
			setMethod(configuration.getChildValue("method", DEFAULT_METHOD));
			setExpression(configuration.getChildValue("expression", DEFAULT_EXPRESSION));
			setTimeout(configuration.getChildValue("timeout", Integer.toString(DEFAULT_TIMEOUT)));
			setUseAuthentication(configuration.getChildValue("useAuthentication"));
			setUsername(configuration.getChildValue("username"));
			setPassword(configuration.getChildValue("password"));
		} catch(Exception e){
			throw new ConfigurationException(e);
		}
	}
	
	protected void writeConfiguration(MutableConfiguration configuration) throws ConfigurationException{
		configuration.addChild("url", getURL().toString());
		configuration.addChild("method", getMethod());
		configuration.addChild("timeout", Integer.toString(getTimeout()));
		configuration.addChild("expression", getExpression());
		configuration.addChild("useAuthentication", new Boolean(isUseAuthentication()).toString());
		configuration.addChild("username", getUsername());
		configuration.addChild("password", getPassword());
	}
	
	protected boolean test(JNMResponse response){
		log.debug("Testing response");
		boolean result = matcher.contains(response.toString(), pattern);
		log.debug("Test complete: " + result);
		return result;
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
	
	public static final String HTTP_VERSION = "HTTP/1.0";
	public static final String DEFAULT_METHOD = "GET";
	public static final int DEFAULT_PORT = 80;
	public static final String DEFAULT_EXPRESSION = "";
	public static final int DEFAULT_TIMEOUT = 30000;
	
	private static final Logger log = LogManager.getLogger(HttpMonitor.class.getName());
	
	private Perl5Compiler compiler;
	private Perl5Matcher matcher;
	private Pattern pattern;
	
	private URL url;
	private String method = DEFAULT_METHOD;
	private String expression = DEFAULT_EXPRESSION;
	private int timeout = DEFAULT_TIMEOUT;
	private boolean useAuthentication = false;
	private String username;
	private String password;
	
}
		
