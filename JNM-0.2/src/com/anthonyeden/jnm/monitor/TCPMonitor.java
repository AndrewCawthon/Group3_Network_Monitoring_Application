package com.anthonyeden.jnm.monitor;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.InterruptedIOException;
import java.net.Socket;
import java.net.ConnectException;
import java.util.LinkedList;

import org.apache.oro.text.regex.Pattern;
import org.apache.oro.text.regex.Perl5Compiler;
import org.apache.oro.text.regex.Perl5Matcher;
import org.apache.oro.text.regex.MalformedPatternException;

import com.anthonyeden.lib.log.Logger;
import com.anthonyeden.lib.log.LogManager;
import com.anthonyeden.lib.util.IOUtilities;
import com.anthonyeden.lib.config.Configuration;
import com.anthonyeden.lib.config.MutableConfiguration;
import com.anthonyeden.lib.config.ConfigurationException;

import com.anthonyeden.jnm.Monitor;
import com.anthonyeden.jnm.JNMRequest;
import com.anthonyeden.jnm.JNMResponse;
import com.anthonyeden.jnm.MonitorState;
import com.anthonyeden.jnm.util.TCPClient;

/**	Monitor implementation which uses a tests a remote host using
	a TCP socket.
	
	@author Anthony Eden
*/

public class TCPMonitor extends Monitor{
	
	public TCPMonitor(){
		compiler = new Perl5Compiler();
		
		matcher = new Perl5Matcher();
		matcher.setMultiline(true);
	}
	
	public String getHost(){
		return host;
	}

	public void setHost(String host){
		log.debug("setHost(" + host + ")");
		if(host == null){
			return;
		}
		this.host = host;
	}
	
	public int getPort(){
		return port;
	}
	
	public void setPort(int port){
		log.debug("setPort(" + port + ")");
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
	
	public void setTimeout(String timeout){
		log.debug("setTimeout(" + timeout + ")");
		if(timeout != null){
			setTimeout(Integer.parseInt(timeout));
		} else {
			setTimeout(DEFAULT_TIMEOUT);
		}
	}
	
	public String getRequestData(){
		return requestData;
	}
	
	public void setRequestData(String requestData){
		this.requestData = requestData;
	}
	
	public String getExpression(){
		return expression;
	}
	
	public void setExpression(String expression) throws MalformedPatternException{
		this.expression = expression;
		this.pattern = compiler.compile(expression);
	}
	
	protected boolean test(JNMResponse response){
		return true;
	}
	
	public void execute(){
		String requestData = getRequestData();
		TCPClient client = new TCPClient(host, port);
		client.setTimeout(getTimeout());
		setLastRequest(new JNMRequest(requestData));
		
		try{
			byte[] responseData = client.sendRequest(requestData);
			setLastResponse(new JNMResponse(responseData, System.currentTimeMillis()));
		} catch(InterruptedIOException e){
			log.debug("Interrupted IO exception caught");
			setLastResponse(null);
			setState(MonitorState.TIMEOUT);
			return;
		} catch(ConnectException e){
			log.debug("Connection failed");
			setLastResponse(null);
			setState(MonitorState.DOWN);
			return;
		} catch(Exception e){
			log.debug("Exception caught");
			setLastResponse(null);
			logError(e);
			setState(MonitorState.ERROR);
			return;
		}
		
		if(test(getLastResponse())){
			setState(MonitorState.OK);
		} else {
			setState(MonitorState.FAILED);
		}
	}
	
	protected void readConfiguration(Configuration configuration) throws ConfigurationException{
		try{
			setHost(configuration.getChildValue("host"));
			setPort(configuration.getChildValue("port"));
			setTimeout(configuration.getChildValue("timeout"));
			setRequestData(configuration.getChildValue("request-data"));
			setExpression(configuration.getChildValue("expression", DEFAULT_EXPRESSION));
		} catch(Exception e){
			throw new ConfigurationException(e);
		}
	}
	
	protected void writeConfiguration(MutableConfiguration configuration) throws ConfigurationException{
		configuration.addChild("host", getHost());
		configuration.addChild("port", Integer.toString(getPort()));
		configuration.addChild("timeout", Integer.toString(getTimeout()));
		configuration.addChild("request-data", getRequestData());
		configuration.addChild("expression", getExpression());
	}
	
	/**	Default timeout (20 seconds). */
	public static final int DEFAULT_TIMEOUT = 30000;
	public static final String DEFAULT_EXPRESSION = "";
	
	private static final Logger log = LogManager.getLogger(TCPMonitor.class.getName());
	
	private Perl5Compiler compiler;
	private Perl5Matcher matcher;
	private Pattern pattern;
	
	private String host;
	private int port;
	private int timeout = DEFAULT_TIMEOUT;
	private String requestData;
	private String expression = DEFAULT_EXPRESSION;

}	
