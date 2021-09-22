package com.anthonyeden.jnm;

import com.anthonyeden.lib.config.Configuration;
import com.anthonyeden.lib.config.MutableConfiguration;
import com.anthonyeden.lib.config.ConfigurationException;

public abstract class Action{
	
	public ActionDefinition getActionDefinition(){
		return actionDefinition;
	}
	
	/**	Set the ActionDefinition which describes this action.
	
		@param actionDefinition The ActionDefinition
	*/
	
	void setActionDefinition(ActionDefinition actionDefinition){
		this.actionDefinition = actionDefinition;
	}
	
	/**	Get the editor for this action.
	
		@return The ActionEditor
	*/
	
	public ActionEditor getActionEditor() throws Exception{
		ActionEditor actionEditor = actionDefinition.getEditorInstance();
		actionEditor.setAction(this);
		return actionEditor;
	}
	
	/**	Execute the given action.  The monitor provided is the monitor which
		caused the action to execute.
		
		@param monitor The Monitor
		@throws Exception
	*/
	
	public abstract void execute(Monitor monitor) throws Exception;
	
	/**	Read the action's configuration information.
	
		@param configuration The configuration object
		@throws ConfigurationException
	*/
	
	protected void readConfiguration(Configuration configuration) throws ConfigurationException{
		
	}
	
	/**	Write the action's configuration information.
	
		@param configuration The configuration object
		@throws ConfigurationException
	*/
	
	protected void writeConfiguration(MutableConfiguration configuration) throws ConfigurationException{
		
	}
	
	/**	Get the action's display name.
	
		@return The action's display name
	*/
	
	public String getName(){
		if(name == null){
			return defaultName;
		}
		return name;
	}
	
	/**	Set the action's display name.
	
		@param name The action's display name
	*/
	
	public void setName(String name){
		this.name = name;
	}
	
	public String getId(){
		return id;
	}
	
	public void setId(String id){
		this.id = id;
	}
	
	protected void setDefaultName(String defaultName){
		this.defaultName = defaultName;	
	}
	
	/**	Log an error.
	
		@param t The error
	*/
	
	protected void logError(Throwable t){
		// need something better here
		t.printStackTrace();
	}
	
	public final void loadConfiguration(Configuration configuration) throws ConfigurationException{
		setId(configuration.getAttribute("id"));
		setName(configuration.getChildValue("name"));
		
		readConfiguration(configuration);
	}
	
	public final void saveConfiguration(MutableConfiguration configuration) throws ConfigurationException{
		configuration.addAttribute("id", getId());
		configuration.addChild("name", getName());
		
		writeConfiguration(configuration);
	}
	
	public String toString(){
		return getName();
	}
	
	private String id;
	private String name;
	private String defaultName = "New Action";
	private ActionDefinition actionDefinition;
	
}
