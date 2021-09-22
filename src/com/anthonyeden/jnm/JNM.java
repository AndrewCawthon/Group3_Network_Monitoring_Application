package com.anthonyeden.jnm;

import java.io.File;
import java.io.Reader;
import java.io.FileReader;
import java.io.InputStream;
import java.util.Iterator;

import com.anthonyeden.lib.log.Logger;
import com.anthonyeden.lib.log.LogManager;
import com.anthonyeden.lib.util.XArrayList;
import com.anthonyeden.lib.util.IOUtilities;
import com.anthonyeden.lib.config.Configuration;
import com.anthonyeden.lib.config.Dom4jConfiguration;
import com.anthonyeden.lib.config.MutableConfiguration;
import com.anthonyeden.lib.config.ConfigurationException;

import com.anthonyeden.jnm.ui.JNMWindow;

public class JNM{
	
	public static void main(String[] args){
		LogManager.setLoggerClassName("com.anthonyeden.lib.log.Log4JLogger");
		
		for(int i = 0; i < args.length; i++){
			if(args[i].equals("--nogui")){
				useGUI = false;
			} else if(args[i].equals("--config")){
				i++;
				config = args[i];
			} else if(args[i].equals("--log")){
				i++;
				logConfig = args[i];
			}
		}
		
		try{
			File logConfigFile = new File(logConfig);
			if(logConfigFile.exists()){
				System.out.println("Using file config: " + logConfigFile);
				LogManager.configure(logConfigFile);
				log = LogManager.getLogger(JNM.class.getName());
				//log.info("Using file config: " + logConfig);
			} else {
				LogManager.configure();
				log = LogManager.getLogger(JNM.class.getName());
				//log.info("Using basic configuration");
			}
		} catch(Exception e){
			System.err.println("Error initializing logging");
			e.printStackTrace();
			return;
		}
		
		try{
			loadConfiguration(new File(config));
		} catch(Exception e){
			System.err.println("Error loading configuration: " + config);
			return;
		}
		
		if(useGUI){
			JNMWindow app = new JNMWindow();
			app.setVisible(true);
			app.startMonitor();
		} else {
			JNMHeadless app = new JNMHeadless();
			app.startMonitor();
		}
	}
	
	public static XArrayList getRegisteredMonitors(){
		return registeredMonitors;
	}
	
	public static XArrayList getRegisteredActions(){
		return registeredActions;
	}
	
	public static Monitor getMonitor(String className) throws Exception{
		Iterator i = registeredMonitors.iterator();
		while(i.hasNext()){
			MonitorDefinition monitorDef = (MonitorDefinition)i.next();
			if(monitorDef.getClassName().equals(className)){
				return monitorDef.getMonitorInstance();
			}
		}
		return null;
	}
	
	public static Action getAction(String className) throws Exception{
		Iterator i = registeredActions.iterator();
		while(i.hasNext()){
			ActionDefinition actionDef = (ActionDefinition)i.next();
			if(actionDef.getClassName().equals(className)){
				return actionDef.getActionInstance();
			}
		}
		return null;
	}
	
	private static void loadConfiguration(File file){
		FileReader reader = null;
		
		try{
			reader = new FileReader(file);
			loadConfiguration(reader);
		} catch(Exception e){
			e.printStackTrace();
		} finally {
			IOUtilities.close(reader);
		}
	}
	
	private static void loadConfiguration(Reader reader) throws Exception{
		configuration = new Dom4jConfiguration(reader);
		loadConfiguration(configuration);
	}
	
	private static void loadConfiguration(Configuration configuration) throws ConfigurationException{
		// load registered monitors
		boolean firstMonitorElement = true;
		Iterator registerMonitorElements = configuration.getChildren("register-monitor").iterator();
		while(registerMonitorElements.hasNext()){
			Configuration registerMonitorElement = (Configuration)registerMonitorElements.next();
			
			MonitorDefinition monitorDef = new MonitorDefinition();
			monitorDef.loadConfiguration(registerMonitorElement);
			
			log.debug("Registering monitor " + monitorDef.getName());
			registeredMonitors.add(monitorDef);
			
			if(firstMonitorElement || monitorDef.isDefault()){
				firstMonitorElement = false;
				registeredMonitors.setSelectedItem(monitorDef);
			}
		}
		
		// load registered actions
		boolean firstActionElement = true;
		Iterator registerActionElements = configuration.getChildren("register-action").iterator();
		while(registerActionElements.hasNext()){
			Configuration registerActionElement = (Configuration)registerActionElements.next();
			
			ActionDefinition actionDef = new ActionDefinition();
			actionDef.loadConfiguration(registerActionElement);
			
			log.debug("Registering action " + actionDef.getName());
			registeredActions.add(actionDef);
			
			if(firstActionElement || actionDef.isDefault()){
				firstActionElement = false;
				registeredActions.setSelectedItem(actionDef);
			}
		}
	}
	
	public static final String DEFAULT_CONFIG = "jnm.xml";
	public static final String DEFAULT_LOG_CONFIG = "log.properties";
	
	private static Logger log;
	
	private static boolean useGUI = true;
	private static String config = DEFAULT_CONFIG;
	private static String logConfig = DEFAULT_LOG_CONFIG;
	private static MutableConfiguration configuration;
	private static XArrayList registeredMonitors = new XArrayList();
	private static XArrayList registeredActions = new XArrayList();

}
