package com.anthonyeden.jnm;

import java.util.Map;
import java.util.HashMap;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;

import com.anthonyeden.lib.log.Logger;
import com.anthonyeden.lib.log.LogManager;
import com.anthonyeden.lib.util.XArrayList;
import com.anthonyeden.lib.config.Configuration;
import com.anthonyeden.lib.config.MutableConfiguration;
import com.anthonyeden.lib.config.ConfigurationException;

/**	Standard base class which is extended by all JNM monitors.  Implementations
	must implement the execute() method.  Implementations may also implement the
	configure() method if they would like use configuration.

	@author Anthony Eden
*/

public abstract class Monitor{
	
	/**	Construct a new Monitor. */
	
	protected Monitor(){
		actions = new HashMap();
		propertyChangeSupport = new PropertyChangeSupport(this);
	}
	
	/**	Get the MonitorDefinition for this Monitor.
		
		@return The MonitorDefinition
	*/
	
	public MonitorDefinition getMonitorDefinition(){
		return monitorDefinition;
	}
	
	/**	Set the MonitorDefinition which describes this monitor.
	
		@param monitorDefinition The MonitorDefinition
	*/
	
	void setMonitorDefinition(MonitorDefinition monitorDefinition){
		this.monitorDefinition = monitorDefinition;
	}
	
	/**	Get the MonitorEditor instance of this Monitor.
		
		@return The MonitorEditor
	*/

	public MonitorEditor getMonitorEditor() throws Exception{
		MonitorEditor monitorEditor = monitorDefinition.getEditorInstance();
		monitorEditor.setMonitor(this);
		return monitorEditor;
	}
	
	/**	Execute the Monitor in its own thread.  This method is used by
		components which execute the monitor directly in order to have
		immediate feedback of the state of the target.
	*/
	
	public void executeAsynchronously(){
		Thread t = new Thread(new Runnable(){
			public void run(){
				Monitor.this.execute();
			}
		});
		t.setDaemon(true);
		t.start();
	}
	
	/**	Execute the monitor's logic.  Subclasses must implement this
		method to perform monitoring logic.
	*/
	
	public abstract void execute();
	
	/**	Return a List of actions which should be executed when the Monitor
		enters the given state.  If no actions are defined then this method
		returns an empty List.
		
		<p>Note: this method returns an ArrayList in order to allow the
		cloning of the list.</p>
		
		@param state The MonitorState
		@return an ArrayList of actions
	*/
	
	public synchronized XArrayList getActions(MonitorState state){
		XArrayList actionList = (XArrayList)actions.get(state);
		if(actionList == null){
			actionList = new XArrayList();
			actions.put(state, actionList);
		}
		return actionList;
	}
	
	/**	This method is called after the basic Monitor configuration
		occurs.  The default implementation is empty.  Subclasses
		may override this method to be configured.
		
		@param configuration The configuration object
		@throws ConfigurationException
	*/
	
	protected void readConfiguration(Configuration configuration) throws ConfigurationException{
	
	}
	
	protected void writeConfiguration(MutableConfiguration configuration) throws ConfigurationException{
		
	}
	
	/**	Get the monitor name.  If the monitor name is not set then
		the monitor implementation should try to provide something
		useful as the monitor name.
		
		@return The monitor name
	*/
	
	public String getName(){
		if(name == null){
			return defaultName;
		}
		return name;
	}
	
	/**	Set the monitor name.  If the monitor name is not set then
		the monitor implementation should try to provide something
		useful as the monitor name.
		
		@param name The monitor name
	*/
	
	public void setName(String name){
		this.name = name;
	}
	
	/**	Set default name.  This is the name to use if no name is specified.  Monitor
		implementations should set this value to something descriptive.
		
		@param defaultName The name if no name is specified
	*/
	
	protected void setDefaultName(String defaultName){
		this.defaultName = defaultName;
	}
	
	/**	Get the monitor delay in milliseconds.
	
		@return The delay in milliseconds
	*/
	
	public int getDelay(){
		return delay;
	}
	
	/**	Set the monitor delay in milliseconds.
	
		@param delay The new delay in milliseconds
	*/
	
	public void setDelay(int delay){
		log.debug("setDelay(" + delay + ")");
		this.delay = delay;
	}
	
	/**	Set the monitor delay in milliseconds.  This method will try
		to parse the given String into an Integer.  If the argument is
		null then the DEFAULT_DELAY value is used.
		
		@param delay The new delay in milliseconds as a String
	*/
	
	public void setDelay(String delay){
		if(delay != null && !(delay.equals(""))){
			setDelay(Integer.parseInt(delay));
		} else {
			setDelay(DEFAULT_DELAY);
		}
	}
	
	/**	Get the current state of the monitor.
	
		@return The monitor state
	*/
	
	public MonitorState getState(){
		return state;
	}
	
	/**	Set the monitor state.
	
		@param state The new monitor state
	*/
	
	protected void setState(MonitorState state){
		MonitorState oldState = this.state;
		this.state = state;
		propertyChangeSupport.firePropertyChange("state", oldState, state);
		
		executeActions(this.state);
	}
	
	public ActivityState getActivity(){
		return activity;
	}
	
	public void setActivity(ActivityState activity){
		ActivityState oldActivity = this.activity;
		this.activity = activity;
		propertyChangeSupport.firePropertyChange("activity", oldActivity, activity);
	}
	
	/** Get the last response or null if no response was received due
		either to error or timeout.
	
		@return The last response or null
	*/
	
	public JNMResponse getLastResponse(){
		return lastResponse;
	}
	
	/**	Set the last response value.
	
		@param lastResponse The last response
	*/
	
	protected void setLastResponse(JNMResponse lastResponse){
		this.lastResponse = lastResponse;
	}
	
	public JNMRequest getLastRequest(){
		return lastRequest;
	}
	
	protected void setLastRequest(JNMRequest lastRequest){
		this.lastRequest = lastRequest;
	}
	
	/**	Log an error.
	
		@param t The error
	*/
	
	protected void logError(Throwable t){
		// need something better here
		t.printStackTrace();
	}
	
	/**	Load the Monitor's configuration.  Subclasses should overload the
		readConfiguration() method if they want to use configuration information. 
		The readConfiguration() method is called after all basic configuration is
		complete.
		
		<p>The following fields are always configured here:
		
		<li>name</li>
		<li>delay</li>
	
		@param configuration The configuration object
		@throws ConfigurationException
	*/
	
	public final void loadConfiguration(Configuration configuration) throws ConfigurationException{
		setName(configuration.getChildValue("name"));
		setDelay(configuration.getChildValue("delay"));
		
		readConfiguration(configuration);
	}
	
	public final void saveConfiguration(MutableConfiguration configuration) throws ConfigurationException{
		configuration.addChild("name", getName());
		configuration.addChild("delay", new Integer(getDelay()).toString());
		
		writeConfiguration(configuration);
	}
	
	public void addPropertyChangeListener(PropertyChangeListener l){
		propertyChangeSupport.addPropertyChangeListener(l);
	}
	
	public void removePropertyChangeListener(PropertyChangeListener l){
		propertyChangeSupport.removePropertyChangeListener(l);
	}
	
	public String toString(){
		return getName();
	}
	
	private void executeActions(MonitorState state){
		XArrayList actions = (XArrayList)(getActions(state).clone());
		
		ActionExecutionThread t = new ActionExecutionThread(this, actions);
		t.start();
	}
	
	/**	Default delay (60 seconds). */
	public static final int DEFAULT_DELAY = 60000;
	
	private static final Logger log = LogManager.getLogger(Monitor.class.getName());
	
	private MonitorDefinition monitorDefinition;
	
	private String name;
	private String defaultName = "New Monitor";
	private int delay = DEFAULT_DELAY;
	private MonitorState state = MonitorState.NOT_TESTED;
	private ActivityState activity = ActivityState.STOPPED;
	private JNMResponse lastResponse;
	private JNMRequest lastRequest;
	private Map actions;
	
	private PropertyChangeSupport propertyChangeSupport;
	
}
