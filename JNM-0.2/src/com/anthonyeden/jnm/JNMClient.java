package com.anthonyeden.jnm;

import java.io.File;
import java.io.Reader;
import java.io.Writer;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import com.anthonyeden.lib.log.Logger;
import com.anthonyeden.lib.log.LogManager;
import com.anthonyeden.lib.util.XArrayList;
import com.anthonyeden.lib.util.IOUtilities;
import com.anthonyeden.lib.config.Configuration;
import com.anthonyeden.lib.config.Dom4jConfiguration;
import com.anthonyeden.lib.config.MutableConfiguration;
import com.anthonyeden.lib.config.ConfigurationException;

public class JNMClient{
	
	public JNMClient(){
		monitors = new XArrayList();
		monitors.addListDataListener(new ListDataListener(){
			public void intervalAdded(ListDataEvent evt){
				log.debug("Monitor added");
				modified = true;
			}
			public void intervalRemoved(ListDataEvent evt){
				log.debug("Monitor removed");
				modified = true;
			}
			public void contentsChanged(ListDataEvent evt){
				log.debug("Contents changed");
				modified = true;
			}
		});
		
		actions = new XArrayList();
		actions.addListDataListener(new ListDataListener(){
			public void intervalAdded(ListDataEvent evt){
				log.debug("Action added");
				modified = true;
			}
			public void intervalRemoved(ListDataEvent evt){
				log.debug("Action removed");
				modified = true;
			}
			public void contentsChanged(ListDataEvent evt){
				log.debug("Contents changed");
				modified = true;
			}
		});
		
		threads = new HashMap();
	}
	
	/**	Start all monitors.  If there is already one or more monitors 
		running then this method will throw an IllegalStateException 
		and the stopAllMonitors() method should be called first.	
	*/
	
	public synchronized void startAllMonitors(){
		log.debug("Starting all monitors");
		if(threads.size() > 0){
			stopAllMonitors();
		}
		
		Iterator iter = getMonitors().iterator();
		while(iter.hasNext()){
			Monitor monitor = (Monitor)iter.next();
			MonitorThread t = new MonitorThread(monitor);
			threads.put(monitor, t);
			t.startMonitor();
		}
	}
	
	/**	Stop all running monitors. */
	
	public synchronized void stopAllMonitors(){
		log.debug("Stopping all monitors");
		Iterator iter = threads.keySet().iterator();
		while(iter.hasNext()){
			Object key = iter.next();
			MonitorThread t = (MonitorThread)threads.get(key);
			if(t != null){
				t.stopMonitor();
			}
		}
	}
	
	/**	Start the specified monitor.  If the monitor is not yet in the
		list of known monitors then it will be added.  If the monitor is
		already running then an IllegalStateException will be thrown.
		
		@param monitor The monitor
	*/
	
	public synchronized void startMonitor(Monitor monitor){
		log.debug("Starting monitor: " + monitor.getName());
		MonitorThread t = (MonitorThread)threads.get(monitor);
		if(t != null){
			throw new IllegalStateException("Monitor already running");
		}
		
		if(!monitors.contains(monitor)){
			log.debug("Adding monitor to collection");
			monitors.add(monitor);
		}
		
		t = new MonitorThread(monitor);
		threads.put(monitor, t);
		
		t.startMonitor();
	}
	
	/**	Stop the specified monitor.  If the monitor is not running then
		this method will throw an IllegalStateException.
		
		@param monitor The monitor to stop
	*/
	
	public synchronized void stopMonitor(Monitor monitor){
		log.debug("Stopping monitor: " + monitor.getName());
		MonitorThread t = (MonitorThread)threads.remove(monitor);
		if(t == null){
			return;
		}
		
		if(!monitors.contains(monitor)){
			monitors.add(monitor);
		}
		
		t.stopMonitor();
	}
	
	public synchronized void removeMonitor(Monitor monitor){
		stopMonitor(monitor);
		monitors.remove(monitor);
	}
	
	/**	Get a list of all monitors known to this client.
	
		@return A list of all Monitors
	*/
	
	public XArrayList getMonitors(){
		return monitors;
	}
	
	/**	Get a list of all actions known to this client.
	
		@return A List of all Actions
	*/
	
	public XArrayList getActions(){
		return actions;
	}
	
	public boolean isRunning(Monitor monitor){
		return threads.get(monitor) != null;
	}
	
	public boolean isModified(){
		return modified;
	}
	
	public void setModified(boolean modified){
		this.modified = modified;
	}
	
	public void loadConfiguration(File file) throws ConfigurationException{
		FileReader reader = null;
		
		try{
			reader = new FileReader(file);
			loadConfiguration(reader);
		} catch(Exception e){
			throw new ConfigurationException(e);
		} finally {
			IOUtilities.close(reader);
		}
	}
	
	public void loadConfiguration(Reader reader) throws ConfigurationException{
		loadConfiguration(new Dom4jConfiguration(reader));
	}
	
	public void loadConfiguration(Configuration configuration) throws ConfigurationException{
		log.debug("Loading configuration");
		
		stopAllMonitors();
		
		setModified(false);
		
		// load actions
		try{
			actions.clear();
			Iterator actionElements = configuration.getChildren("action").iterator();
			while(actionElements.hasNext()){
				Configuration actionElement = (Configuration)actionElements.next();
				String className = actionElement.getAttribute("classname");
				Action action = JNM.getAction(className);
				if(action == null){
					throw new ConfigurationException("Action " + className + " not found");
				}
				action.loadConfiguration(actionElement);
				actions.add(action);
			}
			
			// reset the modified flag
			setModified(false);
		} catch(Exception e){
			throw new ConfigurationException(e);
		}
		
		// load monitors
		try{
			monitors.clear();
			Iterator monitorElements = configuration.getChildren("monitor").iterator();
			while(monitorElements.hasNext()){
				Configuration monitorElement = (Configuration)monitorElements.next();
				String className = monitorElement.getAttribute("classname");
				Monitor monitor = JNM.getMonitor(className);
				if(monitor == null){
					throw new ConfigurationException("Monitor " + className + " not found");
				}
				monitor.loadConfiguration(monitorElement);
				
				// add monitor actions
				Iterator monitorActionElements = monitorElement.getChildren("action").iterator();
				while(monitorActionElements.hasNext()){
					Configuration monitorActionElement = (Configuration)monitorActionElements.next();
					String id = monitorActionElement.getAttribute("id");
					MonitorState monitorState = MonitorState.stringToState(monitorActionElement.getAttribute("state"));
					Action action = findAction(id);
					if(action == null){
						log.error("Action " + id + " not found");
					} else {
						monitor.getActions(monitorState).add(action);
					}
				}
				
				monitors.add(monitor);
			}
		} catch(Exception e){
			throw new ConfigurationException(e);
		}
		
		log.debug("Configuration loaded.");
	}
	
	public void saveConfiguration(File file) throws ConfigurationException{
		FileWriter writer = null;
		
		try{
			writer = new FileWriter(file);
			saveConfiguration(writer);
		} catch(Exception e){
			throw new ConfigurationException(e);
		} finally {
			IOUtilities.close(writer);
		}
	}
	
	public void saveConfiguration(Writer writer) throws ConfigurationException{
		try{
			Dom4jConfiguration configuration = new Dom4jConfiguration("jnm-data");
			saveConfiguration(configuration);
			configuration.save(writer);
		} catch(Exception e){
			throw new ConfigurationException(e);
		}
	}
	
	public void saveConfiguration(MutableConfiguration configuration) throws ConfigurationException{
		log.debug("Saving configuration");
		
		// save all actions
		Iterator actionIter = actions.iterator();
		while(actionIter.hasNext()){
			Action action = (Action)actionIter.next();
			MutableConfiguration actionElement = configuration.addChild("action", null);
			actionElement.addAttribute("classname", action.getClass().getName());
			action.saveConfiguration(actionElement);
		}
		
		// save all monitors
		Iterator monitorIter = monitors.iterator();
		while(monitorIter.hasNext()){
			Monitor monitor = (Monitor)monitorIter.next();
			MutableConfiguration monitorElement = configuration.addChild("monitor", null);
			monitorElement.addAttribute("classname", monitor.getClass().getName());
			monitor.saveConfiguration(monitorElement);
			
			MonitorState[] monitorStates = MonitorState.listAll();
			for(int i = 0; i < monitorStates.length; i++){
				Iterator actions = monitor.getActions(monitorStates[i]).iterator();
				while(actions.hasNext()){
					Action action = (Action)actions.next();
					MutableConfiguration monitorActionElement = monitorElement.addChild("action", null);
					monitorActionElement.addAttribute("id", action.getId());
					monitorActionElement.addAttribute("state", MonitorState.stateToString(monitorStates[i]));
				}
			}
		}
		
		setModified(false);
		
		log.debug("Configuration saved");
	}
	
	protected Action findAction(String id){
		Iterator iter = actions.iterator();
		while(iter.hasNext()){
			Action action = (Action)iter.next();
			if(action.getId().equals(id)){
				return action;
			}
		}
		return null;
	}
	
	private static final Logger log = LogManager.getLogger(JNMClient.class.getName());
	
	private XArrayList monitors;
	private XArrayList actions;
	private Map threads;
	
	private boolean modified = false;
	
}
